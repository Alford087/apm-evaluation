package com.alford.apmevaluation;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.networkbench.agent.impl.NBSAppAgent;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NBSAppAgent.setLicenseKey("bbf6d85620c44a779feceed374da2855")
                .withLocationServiceEnabled(true)
                .start(this.getApplicationContext());//Appkey 请从官网获取
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


    private void httpGet() {
        String url = "http://www.publicobject.com/helloworld.txt";
//        String url = "http://www.baidu.com";
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        return response;
                    }
                })

                .build();//新建一个okhttpClient对象，并且设置拦截器

        final Request request = new Request.Builder()
                .url(url)
//                .addHeader(LoggingInterceptor.COMMON_HEADER,String.valueOf(System.currentTimeMillis()))
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
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
