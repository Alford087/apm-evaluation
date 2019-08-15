package com.alford.apmevaluation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.test.NetHelper;
import com.newrelic.agent.android.NewRelic;
import com.newrelic.agent.android.instrumentation.okhttp3.OkHttp3Instrumentation;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
//                httpGet();
                NetHelper netHelper = new NetHelper();
                netHelper.request();
                break;
            case R.id.btn_anr:
                makeAnr();
                break;
            case R.id.btn_crash:
                makeCrash();
                break;
        }
    }

    private void test() {
        new Request.Builder().get().url("https://www.baidu.com");
        OkHttp3Instrumentation.build(new Request.Builder().get().url("https://www.baidu.com"));
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


    //方法：发送网络请求，获取百度首页的数据。在里面开启线程
    private void sendRequestWithHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址
                HttpGet httpGet = new HttpGet("http://www.baidu.com");

                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();//这个start()方法不要忘记了

    }
}
