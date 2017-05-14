package com.halohoop.holleydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.halohoop.holley.http.core.interfaces.IDataListener;
import com.halohoop.holley.http.Holley;
import com.halohoop.holley.utils.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IDataListener<NewsBean> {
    public static final String url
            = "http://v.juhe.cn/toutiao/index?type=top&key=29da5e8be9ba88b932394b7261092f71";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void test(View v) {
        Holley.askRequest(null, url, NewsBean.class, this);
    }

    @Override
    public void onSuccess(NewsBean newsBean) {
        NewsBean.ResultBean result = newsBean.getResult();
        List<NewsBean.ResultBean.DataBean> data = result.getData();
        for (int i = 0; i < data.size(); i++) {
            NewsBean.ResultBean.DataBean dataBean = data.get(i);
            Utils.logI(dataBean);
        }
    }

    @Override
    public void onFail() {
        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
    }
}
