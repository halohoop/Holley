package com.halohoop.holley.http.beans;

import com.halohoop.holley.http.interfaces.IHttpListener;
import com.halohoop.holley.http.interfaces.IHttpService;

/**
 * Created by Pooholah on 2017/5/13.
 */

/**
 * 为了给调用层传入的数据封装一下，不管是的网络请求 还是 下载 都使用这个。
 * @param <T>
 */
public class RequestHolder<T> {
    /**
     * 执行下载的类
     */
    private IHttpService mHttpService;
    /**
     * 回调结果
     */
    private IHttpListener mHttpListener;
    /**
     * 请求参数对应的实体
     */
    private T mRequestInfo;
    private String mUrl;

    public IHttpService getHttpService() {
        return mHttpService;
    }

    public void setHttpService(IHttpService mHttpService) {
        this.mHttpService = mHttpService;
    }

    public IHttpListener getHttpListener() {
        return mHttpListener;
    }

    public void setHttpListener(IHttpListener mHttpListener) {
        this.mHttpListener = mHttpListener;
    }

    public T getRequestInfo() {
        return mRequestInfo;
    }

    public void setRequestInfo(T mRequestInfo) {
        this.mRequestInfo = mRequestInfo;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
