package com.alivc.utilssample;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    ScreenOffReceiver mScreenOffReceiver = new ScreenOffReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(Constant.URL);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downAsynFile();
            }
        });
        IntentFilter mScreenOffFilter = new IntentFilter();
        mScreenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenOffReceiver, mScreenOffFilter);
    }

    String apk_url = "没解析";

    private void downAsynFile() {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
        // String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        // String url = "https://git.idc0.xlhb.com/qianhao/application_update/raw/master/cloud_platform_v2.2.0.apk";
        String url = "https://git.idc0.xlhb.com/qianhao/application_update/raw/master/cloud_paltform_apk_update.txt";
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("qh", e.toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                String str = Inputstr2Str_Reader(inputStream, null);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    apk_url = jsonObject.getString("apk_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), apk_url, Toast.LENGTH_SHORT).show();
                    }
                });
//                FileOutputStream fileOutputStream = null;
//                try {
//                    fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.txt"));
//                    byte[] buffer = new byte[2048];
//                    int len = 0;
//                    while ((len = inputStream.read(buffer)) != -1) {
//                        fileOutputStream.write(buffer, 0, len);
//                    }
//                    fileOutputStream.flush();
//                } catch (IOException e) {
//                    Log.i("qh", "IOException");
//                    e.printStackTrace();
//                }
                Log.d("qh", "文件下载成功");
            }
        });
    }

    private class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public String Inputstr2Str_Reader(InputStream in, String encode) {
        String str = "";
        try {
            if (encode == null || encode.equals("")) {
                // 默认以utf-8形式
                encode = "utf-8";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuffer sb = new StringBuffer();

            while ((str = reader.readLine()) != null) {
                sb.append(str).append("\n");
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }

}