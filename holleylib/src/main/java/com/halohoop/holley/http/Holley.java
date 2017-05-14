package com.halohoop.holley.http;

import com.halohoop.holley.http.core.beans.RequestHolder;
import com.halohoop.holley.http.core.managers.HttpTask;
import com.halohoop.holley.http.core.managers.ThreadPoolManager;
import com.halohoop.holley.http.json.JsonHttpListener;
import com.halohoop.holley.http.json.JsonHttpService;
import com.halohoop.holley.http.core.interfaces.IDataListener;

import java.util.concurrent.FutureTask;

/**
 * Created by Pooholah on 2017/5/13.
 */

public class Holley {
    public static <T, M> void askRequest(T requestInfo, String url,
                                         Class<M> response, IDataListener dataListener) {
        //封装参数
        RequestHolder<T> requestHolder = new RequestHolder<>();
        requestHolder.setUrl(url);
        requestHolder.setHttpListener(new JsonHttpListener<>(response, dataListener));
        requestHolder.setHttpService(new JsonHttpService());
        requestHolder.setRequestInfo(requestInfo);

        //生成任务
        HttpTask<T> httpTask = new HttpTask<>(requestHolder);

        //将httpTask扔到线程池中执行
        try {
            ThreadPoolManager.getInstance().execute(new FutureTask<Object>(httpTask, null));
        } catch (InterruptedException e) {
            e.printStackTrace();
            dataListener.onFail();
        }

    }
}
