package com.alford.apmevaluation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.HttpMetric;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    HttpMetric metric;

    public void httpGet() {
        final String uri = "https://www.baidu.com";
//        OkHttpClient client = new OkHttpClient();
//        //构造Request对象
//        //采用建造者模式，链式调用指明进行Get请求,传入Get的请求地址
//        Request request = new Request.Builder().get().url(url).build();
//        Call call = client.newCall(request);
//        //异步调用并设置回调函数
//        metric.start();
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                final String responseStr = response.body().string();
//                Log.e(TAG, responseStr);
//            }
//        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpMetric metric =
                        FirebasePerformance.getInstance().newHttpMetric(uri,
                                FirebasePerformance.HttpMethod.GET);
                try {
                    final URL url = new URL(uri);
                    metric.start();
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    byte[] data = new byte[0];
                    DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                    outputStream.write(data);
                    metric.setRequestPayloadSize(data.length);
                    metric.setHttpResponseCode(conn.getResponseCode());
                    conn.disconnect();
                    metric.stop();
                } catch (IOException ignored) {
                }

            }
        }).start();


    }

    private void makeAnr() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    LimitQueue<String> limitQueue = new LimitQueue<>(5);

    private void makeCrash() {
        int i = 1 / 0;
    }
}
