package com.halohoop.holley.http.json;

import android.os.Handler;
import android.os.Looper;

import com.halohoop.holley.http.core.interfaces.IDataListener;
import com.halohoop.holley.http.core.interfaces.IHttpListener;
import com.halohoop.holley.utils.Utils;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Pooholah on 2017/5/13.
 */

/**
 *
 * @param <M> 实体
 */
public class JsonHttpListener<M> implements IHttpListener {

    private Class<M> mResponse;
    /**
     * 回调调用层的接口
     */
    private IDataListener<M> mDataListener;
    /**
     * 用于切换线程的handler
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsonHttpListener(Class<M> response, IDataListener<M> mDataListener) {
        this.mResponse = response;
        this.mDataListener = mDataListener;
    }

    @Override
    public void onSuccess(HttpEntity httpEntity) {
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
            //得到返回的数据，此处还是在子线程，接下去会有切换线程
            String content = getContent(inputStream);
            final M m = Utils.parseObject(content, mResponse);
            //切换线程
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //回调在主线程
                    mDataListener.onSuccess(m);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            mDataListener.onFail();
        }

    }

    @Override
    public void onFail() {
        mDataListener.onFail();
    }

    /**
     * @param inputStream
     * @return
     */
    private String getContent(InputStream inputStream) {
        String content = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                mDataListener.onFail();
                Utils.logI("Error=" + e.toString());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Utils.logI("Error=" + e.toString());
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            mDataListener.onFail();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
}
