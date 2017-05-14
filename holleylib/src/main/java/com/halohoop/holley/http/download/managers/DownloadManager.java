package com.halohoop.holley.http.download.managers;

/**
 * Created by Pooholah on 2017/5/14.
 */

import android.os.Environment;

import com.halohoop.holley.http.core.beans.RequestHolder;
import com.halohoop.holley.http.core.interfaces.IHttpListener;
import com.halohoop.holley.http.core.interfaces.IHttpService;
import com.halohoop.holley.http.core.managers.HttpTask;
import com.halohoop.holley.http.core.managers.ThreadPoolManager;
import com.halohoop.holley.http.download.beans.DownloadItemInfo;
import com.halohoop.holley.http.download.impls.DownloadHttpService;
import com.halohoop.holley.http.download.impls.DownloadListener;
import com.halohoop.holley.http.download.interfaces.IDownloadServiceCallable;
import com.halohoop.holley.utils.Utils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * 下载模块
 */
public class DownloadManager implements IDownloadServiceCallable {

    private byte[] mLock = new byte[0];

    /**
     * 下载一个文件
     */
    public void askDownload(String url) {

        synchronized (mLock) {
            String[] preFixs = url.split("/");
            String fileName = preFixs[preFixs.length - 1];
            File file = new File(Environment.getExternalStorageDirectory(), fileName);

            DownloadItemInfo downloadItemInfo = new DownloadItemInfo(url, file.getAbsolutePath());
            downloadItemInfo.setUrl(url);

            RequestHolder requestHolder = new RequestHolder();
            IHttpService httpService = new DownloadHttpService();
            Map<String, String> httpHeaders = httpService.getHttpHeaders();
            IHttpListener httpListener = new DownloadListener(downloadItemInfo, this, httpService);
            requestHolder.setHttpService(httpService);
            requestHolder.setHttpListener(httpListener);
            requestHolder.setUrl(url);
            HttpTask httpTask = new HttpTask(requestHolder);
            try {
                ThreadPoolManager.getInstance().execute(new FutureTask<>(httpTask, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
                Utils.logI("Fail!");
            }

        }
    }

    @Override
    public void onDownloadStatusChanged(DownloadItemInfo downloadItemInfo) {

    }

    @Override
    public void onTotalLengthReceived(DownloadItemInfo downloadItemInfo) {

    }

    @Override
    public void onCurrentSizeChanged(DownloadItemInfo downloadItemInfo, double downLenth, long speed) {
        Utils.logI("下载速度：" + speed / 1000 + "k/s");
        Utils.logI("-----路径  " + downloadItemInfo.getFilePath() + "  下载长度  " + downLenth + "   速度  " + speed);
    }

    @Override
    public void onDownloadSuccess(DownloadItemInfo downloadItemInfo) {
        Utils.logI("下载成功    路劲  " + downloadItemInfo.getFilePath() + "  url " + downloadItemInfo.getUrl());
    }

    @Override
    public void onDownloadPause(DownloadItemInfo downloadItemInfo) {

    }

    @Override
    public void onDownloadError(DownloadItemInfo downloadItemInfo, int var2, String var3) {

    }
}
