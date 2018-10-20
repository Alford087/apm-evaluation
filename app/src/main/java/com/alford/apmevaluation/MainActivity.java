package com.alford.apmevaluation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.newrelic.agent.android.NewRelic;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NewRelic.withApplicationToken(
                "AA4466744f6e7e7ea7f7cdcae820fe507c876dceab"
        ).start(this.getApplication());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_http_get).setOnClickListener(this);
        findViewById(R.id.btn_anr).setOnClickListener(this);
        findViewById(R.id.btn_crash).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_http_get:
                httpGet();
                break;
            case R.id.btn_anr:
                makeAnr();
                break;
            case R.id.btn_crash:
                makeCrash();
                break;
        }
    }


    public void httpGet() {
        OkHttpClient client = new OkHttpClient();
        //构造Request对象
        //采用建造者模式，链式调用指明进行Get请求,传入Get的请求地址
        Request request = new Request.Builder().get().url("https://www.baidu.com").build();
        Call call = client.newCall(request);
        //异步调用并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = response.body().string();
                Log.e(TAG, responseStr);
            }
        });
    }

    private void makeAnr() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void makeCrash() {
        int i = 1 / 0;
    }
}
