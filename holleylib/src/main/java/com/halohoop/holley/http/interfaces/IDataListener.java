package com.halohoop.holley.http.interfaces;

/**
 * Created by Pooholah on 2017/5/13.
 */

public interface IDataListener<M> {

    /**
     * 回调结果给调用层
     */
    void onSuccess(M m);

    /**
     * 参数还可以加入错误码 404 502 ... int类型
     */
    void onFail();
}
