package com.halohoop.holley.http.core.interfaces;

/**
 * Created by Pooholah on 2017/5/13.
 */

import org.apache.http.HttpEntity;

/**
 * 处理结果
 * 此处还是回调框架的实现层
 */
public interface IHttpListener {
    /**
     *
     * @param httpEntity 网络返回的结果在这里面
     */
    void onSuccess(HttpEntity httpEntity);

    void onFail();
}
