package com.halohoop.holley.http.download.impls;

import android.os.Build;
import android.util.ArrayMap;

import com.halohoop.holley.http.core.interfaces.IHttpListener;
import com.halohoop.holley.http.core.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Pooholah on 2017/5/14.
 */

public class DownloadHttpService implements IHttpService {

    private HttpClient mHttpClient = new DefaultHttpClient();
    private String mUrl;
    private IHttpListener mHttpListener;

    /**
     * 请求头
     */
    private Map<String, String> mHeaders;
    private HttpResponseHandler mHttpResponseHandler = new HttpResponseHandler();
    private HttpGet mGet;
    private byte[] mRequestData;

    public DownloadHttpService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用ArrayMap更省内存
            this.mHeaders = Collections.synchronizedMap(new ArrayMap<String, String>());
        } else {
            this.mHeaders = Collections.synchronizedMap(new HashMap<String, String>());
        }
    }

    /**
     * 封装请求头
     */
    private void constructHeaders() {
        Iterator<Map.Entry<String, String>> iterator = this.mHeaders.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
            mGet.addHeader(key, value);
        }
    }

    @Override
    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public void execute() {
        constructHeaders();
        mGet = new HttpGet(mUrl);
//        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(mRequestData);
//        mGet.setEntity(byteArrayEntity);
        try {
            mHttpClient.execute(mGet, mHttpResponseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            mHttpListener.onFail();
        }
    }

    @Override
    public void setHttpListener(IHttpListener httpListener) {
        this.mHttpListener = httpListener;
    }

    @Override
    public void setRequestData(byte[] requestData) {
        this.mRequestData = requestData;
    }

    @Override
    public Map<String, String> getHttpHeaders() {
        return null;
    }

    @Override
    public void pause() {

    }

    @Override
    public boolean isPause() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCancel() {
        return false;
    }

    private class HttpResponseHandler extends BasicResponseHandler {
        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                mHttpListener.onSuccess(response.getEntity());
            } else {
                //TODO 非200
                mHttpListener.onFail();
            }
            return null;
        }
    }

}
