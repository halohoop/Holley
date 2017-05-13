package com.halohoop.holley.http.impls;

import com.halohoop.holley.http.interfaces.IHttpListener;
import com.halohoop.holley.http.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Pooholah on 2017/5/13.
 */

public class JsonHttpService implements IHttpService {

    private String mUrl;
    private IHttpListener mHttpListener;
    private HttpClient mHttpClient = new DefaultHttpClient();
    private HttpPost mPost;
    private byte[] mRequestData;
    private HttpResponseHandler mHttpResponseHandler = new HttpResponseHandler();

    @Override
    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public void execute() {
        mPost = new HttpPost(mUrl);
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(mRequestData);
        mPost.setEntity(byteArrayEntity);
        try {
            mHttpClient.execute(mPost, mHttpResponseHandler);
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

    private class HttpResponseHandler extends BasicResponseHandler {
        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                mHttpListener.onSuccess(response.getEntity());
            }else{
                //TODO 非200
                mHttpListener.onFail();
            }
            return null;
        }
    }

}
