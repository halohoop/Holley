package com.halohoop.holley.http.managers;

import com.halohoop.holley.http.beans.RequestHolder;
import com.halohoop.holley.http.impls.HttpTask;
import com.halohoop.holley.http.impls.JsonHttpListener;
import com.halohoop.holley.http.impls.JsonHttpService;
import com.halohoop.holley.http.interfaces.IDataListener;

import java.util.concurrent.FutureTask;

/**
 * Created by Pooholah on 2017/5/13.
 */

public class Holley {
    public static <T, M> void askRequest(T requestInfo, String url,
                                         Class<M> response, IDataListener dataListener) {
        RequestHolder<T> requestHolder = new RequestHolder<>();
        requestHolder.setUrl(url);
        requestHolder.setHttpListener(new JsonHttpListener<>(response, dataListener));
        requestHolder.setHttpService(new JsonHttpService());
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
