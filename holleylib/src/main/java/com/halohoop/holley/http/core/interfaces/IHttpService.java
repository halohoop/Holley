package com.halohoop.holley.http.core.interfaces;

/**
 * Created by Pooholah on 2017/5/13.
 */

import java.util.Map;

/**
 * 网络请求
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

    //针对下载再扩充以下方法------

    /**
     * 获取
     *
     * @return 返回请求头的map集合
     */
    Map<String, String> getHttpHeaders();

    /**
     * 暂停下载
     */
    void pause();

    /**
     * 任务是否暂停，因为涉及到暂停下载
     *
     * @return 是否暂停
     */
    boolean isPause();

    /**
     * 取消下载
     */
    void cancel();

    /**
     * 任务是否取消，因为涉及到取消下载
     *
     * @return 是否取消
     */
    boolean isCancel();

    //针对下载再扩充以上方法------

}
