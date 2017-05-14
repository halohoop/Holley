package com.halohoop.holley.http.download.interfaces;

import com.halohoop.holley.http.core.interfaces.IHttpListener;
import com.halohoop.holley.http.core.interfaces.IHttpService;

/**
 * Created by Pooholah on 2017/5/14.
 */

public interface IDownListener extends IHttpListener {
    /**
     * 这里和{@link IHttpService#setHttpListener(IHttpListener)}方法相对，
     * 因为只需网络请求json的时候，只需IHttpService持有引用IHttpListener引用，
     * 而现在下载模块中，
     * IHttpListener也需要持有IHttpService引用，因为后期需要控制暂停下载
     *
     * @param httpService
     */
    void setHttpService(IHttpService httpService);

    void setCancelCallable(/*参数待定*/);

    void setPauseCallable(/*参数待定*/);

}
