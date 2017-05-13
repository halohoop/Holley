package com.halohoop.holleydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.halohoop.holley.http.interfaces.IDataListener;
import com.halohoop.holley.http.managers.Holley;

public class MainActivity extends AppCompatActivity implements IDataListener<DataBean> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void test(View v) {
        String url = "http://192.168.199.101:8080/Demo/LoginServlet";
        User user = new User();
        Holley.askRequest(user, url, DataBean.class, this);
    }

    @Override
    public void onSuccess(DataBean dataBean) {
        String result = dataBean.getResult();
        Toast.makeText(this, "" + result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail() {
        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
    }
}
