package com.halohoop.holley.http.download.impls;

import android.os.Handler;
import android.os.Looper;

import com.halohoop.holley.http.core.interfaces.IHttpService;
import com.halohoop.holley.http.download.beans.DownloadItemInfo;
import com.halohoop.holley.http.download.beans.DownloadStatus;
import com.halohoop.holley.http.download.interfaces.IDownListener;
import com.halohoop.holley.http.download.interfaces.IDownloadServiceCallable;
import com.halohoop.holley.utils.FileUtils;

import org.apache.http.HttpEntity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Pooholah on 2017/5/14.
 */

public class DownloadListener implements IDownListener {


    private String mUrl;
    private File mFile;
    /**
     * 文件下载到哪里了
     */
    private long mBreakPoint;

    /**
     * json的时候是IDataListener回调，而下载有更多的回调，必须要使用一个新的，不然混到一起不好
     * 所以下载的调用层回调就是 IDownloadServiceCallable
     */
    private DownloadItemInfo mDownloadItemInfo;
    private IDownloadServiceCallable mDownloadServiceCallable;
    /**
     * 类比Context，activity中传个this进来，这里实例化的时候需要传入IHttpService
     * 因为暂停和取消
     */
    private IHttpService mHttpService;

    /**
     * 主线程
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DownloadListener(DownloadItemInfo mDownloadItemInfo,
                            IDownloadServiceCallable mDownloadServiceCallable,
                            IHttpService mHttpService) {
        this.mDownloadItemInfo = mDownloadItemInfo;
        this.mDownloadServiceCallable = mDownloadServiceCallable;
        this.mHttpService = mHttpService;

        this.mFile = new File(this.mDownloadItemInfo.getFilePath());
        //已经下载的文件长度
        this.mBreakPoint = mFile.length();
    }

    public DownloadListener(DownloadItemInfo downloadItemInfo) {
        this.mDownloadItemInfo = downloadItemInfo;
    }

    @Override//对比json的处理这里又是另一种策略
    public void onSuccess(HttpEntity httpEntity) {
        //在这里能够拿到文件的流，因此都是模板方法
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        //用于计算每秒多少k
        long speed = 0L;
        //花费时间
        long useTime = 0L;
        //下载的长度
        long getLen = 0L;
        //接受的长度
        long receiveLen = 0L;
        boolean bufferLen = false;
        //得到下载的长度
        long dataLength = httpEntity.getContentLength();
        //单位时间下载的字节数
        long calcSpeedLen = 0L;
        //总数
        long totalLength = this.mBreakPoint + dataLength;
        //更新数量
        this.receviceTotalLength(totalLength);
        //更新状态
        this.downloadStatusChange(DownloadStatus.downloading);
        byte[] buffer = new byte[1024];
        int count = 0;
        long currentTime = System.currentTimeMillis();
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        try {
            if (!FileUtils.makeDirs(this.getFile())) {
                mDownloadServiceCallable.onDownloadError(mDownloadItemInfo, 1, "创建文件夹失败");
            } else {
                fos = new FileOutputStream(this.getFile(), true);
                bos = new BufferedOutputStream(fos);
                int length = 1;
                while ((length = inputStream.read(buffer)) != -1) {
                    if (this.getHttpService().isCancel()) {
                        mDownloadServiceCallable.onDownloadError(mDownloadItemInfo,
                                1/*TODO 替换成集中管理的*/, "用户取消了");
                        return;
                    }

                    if (this.getHttpService().isPause()) {
                        mDownloadServiceCallable.onDownloadError(mDownloadItemInfo, 2, "用户暂停了");
                        return;
                    }

                    bos.write(buffer, 0, length);
                    getLen += (long) length;
                    receiveLen += (long) length;
                    calcSpeedLen += (long) length;
                    ++count;
                    //以下用判断来控制整10回调一次，不然回调次数太多了
                    if (receiveLen * 10L / totalLength >= 1L || count >= 5000) {
                        currentTime = System.currentTimeMillis();
                        useTime = currentTime - startTime;
                        startTime = currentTime;
                        speed = 1000L * calcSpeedLen / useTime;
                        count = 0;
                        calcSpeedLen = 0L;
                        receiveLen = 0L;
                        //网速的回调
                        this.downloadLengthChange(this.mBreakPoint + getLen, totalLength, speed);
                    }
                }
                if (dataLength != getLen) {
                    mDownloadServiceCallable.onDownloadError(mDownloadItemInfo, 3, "下载长度不相等");
                } else {
                    this.downloadLengthChange(this.mBreakPoint + getLen, totalLength, speed);
                    //克隆模式应用：
                    //在回调给调用层的时候传了一个对象，
                    // 但是这个对象是设计到核心逻辑的不能被随便修改，
                    // 因此克隆一个一模一样但是新的给调用层。
                    this.mDownloadServiceCallable.onDownloadSuccess(mDownloadItemInfo.copy());
                }
            }
        } catch (IOException ioException) {
            if (this.getHttpService() != null) {
//                this.getHttpService().abortRequest();
            }
            return;
        } catch (Exception e) {
            if (this.getHttpService() != null) {
//                this.getHttpService().abortRequest();
            }
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void downloadLengthChange(final long downLength, long totalLength, final long speed) {
        //修改当前进度
        mDownloadItemInfo.setCurrentLength(downLength);
        if (mDownloadServiceCallable != null) {
            //让序列化操作发生在子线程
            final DownloadItemInfo copy = mDownloadItemInfo.copy();
            synchronized (this.mDownloadServiceCallable) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadServiceCallable.onCurrentSizeChanged(copy,
                                downLength, speed);

                    }
                });
            }
        }
    }

    private IHttpService getHttpService() {
        return mHttpService;
    }

    private File getFile() {
        return mFile;
    }

    private void downloadStatusChange(DownloadStatus downloading) {
        mDownloadItemInfo.setStatus(downloading);
        if (mDownloadServiceCallable != null) {
            //让序列化操作发生在子线程
            final DownloadItemInfo copy = mDownloadItemInfo.copy();
            synchronized (this.mDownloadServiceCallable) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadServiceCallable.onDownloadStatusChanged(copy);
                    }
                });
            }
        }
    }

    private void receviceTotalLength(long totalLength) {
        mDownloadItemInfo.setTotalLength(totalLength);
        if (mDownloadServiceCallable != null) {
            //让序列化操作发生在子线程
            final DownloadItemInfo copy = mDownloadItemInfo.copy();
            synchronized (this.mDownloadServiceCallable) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadServiceCallable.onTotalLengthReceived(copy);
                    }
                });
            }
        }
    }

    @Override
    public void onFail() {

    }

    @Override
    public void setHttpService(IHttpService httpService) {

    }

    /**
     * 设置取消接口
     */
    @Override
    public void setCancelCallable(/*参数待定*/) {

    }

    /**
     * 设置暂停接口
     */
    @Override
    public void setPauseCallable(/*参数待定*/) {

    }
}
