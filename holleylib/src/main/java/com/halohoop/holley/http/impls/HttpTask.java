package com.halohoop.holley.http.impls;

import com.alibaba.fastjson.JSON;
import com.halohoop.holley.http.beans.RequestHolder;
import com.halohoop.holley.http.interfaces.IHttpService;

import java.io.UnsupportedEncodingException;

/**
 * Created by Pooholah on 2017/5/13.
 */

public class HttpTask<T> implements Runnable {
    private IHttpService mHttpService;//执行execute方法

    /**
     * 接收调用层传入的参数，全部采用一种格式的参数，封装到一起
     *
     * @param requestHolder
     */
    public HttpTask(RequestHolder<T> requestHolder) {
        //得到从调用层传入的实例
        this.mHttpService = requestHolder.getHttpService();

        this.mHttpService.setHttpListener(requestHolder.getHttpListener());
        this.mHttpService.setUrl(requestHolder.getUrl());
        T requestInfo = requestHolder.getRequestInfo();
        String s = JSON.toJSONString(requestInfo);
        try {
            mHttpService.setRequestData(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        mHttpService.execute();
    }
}
