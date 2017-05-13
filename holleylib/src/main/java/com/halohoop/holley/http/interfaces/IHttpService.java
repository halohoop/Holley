package com.halohoop.holley.http.interfaces;

/**
 * Created by Pooholah on 2017/5/13.
 */

public interface IHttpService {
    /**
     * 设置URL
     *
     * @param url
     */
    void setUrl(String url);

    /**
     * 执行网络请求
     */
    void execute();

    /**
     * 设置处理的接口，实现的实例传进来
     *
     * @param httpListener
     */
    void setHttpListener(IHttpListener httpListener);

    /**
     * 设置请求参数
     *
     * @param requestData
     */
    void setRequestData(byte[] requestData);
}
