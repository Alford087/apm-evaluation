package com.alford.apmevaluation;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.networkbench.agent.impl.NBSAppAgent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
//                makeCrash();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpURLConnection();
                    }
                });
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


    private void httpURLConnection() {
        try {
            URL url = new URL("http://www.baidu.com");
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            //获取响应码
            if (coon.getResponseCode() == 200) {
                //获取输入流
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024 * 512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1); // 执行这句并不会创建数据库文件;


    private void testDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.getReadableDatabase();
        db.insert("", "", null);
    }
}
