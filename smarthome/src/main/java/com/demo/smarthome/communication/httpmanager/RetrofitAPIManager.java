package com.demo.smarthome.communication.httpmanager;

import android.content.Context;
import android.content.Intent;

import com.demo.smarthome.LoginActivity;
import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Created by wangdongyang on 16/12/1.
 */
public class RetrofitAPIManager {

    public static OkHttpClient getOkHttpClient(final Context context) {
        int[] certficates = new int[]{R.raw.wangfu};
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3,TimeUnit.MINUTES)
                .writeTimeout(3,TimeUnit.MINUTES)
                .sslSocketFactory(getSSLSocketFactory(context, certficates));
        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
//                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                        .addHeader("Accept", "application/json")
                        .addHeader("App-Key", "1234567890")
                        .addHeader("App-OS", "Android")
                        .addHeader("App-OS-Model", AppUtils.getPhoneModel())
                        .addHeader("App-OS-SDK", AppUtils.getAndroidOSVersion() + "")
                        .addHeader("App-OS-Release", AppUtils.getSystemVersion())
                        .addHeader("App-Package-Name", "com.wfkj.android.smartoffice")
                        .addHeader("App-Version-Name", AppUtils.getAppVersion(context))
                        .addHeader("App-Version-Code", AppUtils.getAppVersionCode(context) + "")
                        .addHeader("App-Install-Time", "143565456")
                        .addHeader("App-Update-Time", "143565456")
                        .addHeader("App-UDID", AppUtils.getIMSI(context))
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);//执行此次请求
                String json = response.body().string();
                int code = 0;
                String tag = "";
                boolean flg = false;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    code = jsonObject.getInt("code");
                    tag = jsonObject.getString("message");
                    flg = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    flg = false;
                }
//                Gson gson = new Gson();
//                ResultBean resultBean = gson.fromJson(json, ResultBean.class);
//                System.out.println("-------403------" + json);

                if (flg) {
                    okhttp3.MediaType mediaType = response.body().contentType();
                    if (code == 403) {
                        MyActivityManager mam = MyActivityManager.getInstance();
                        String name = PreferenceUtil.getString(Constant.UserManager.USERNAME);
                        PreferenceUtil.clear();
                        PreferenceUtil.putString(Constant.UserManager.USERNAME, name);
                        Intent intent = new Intent();
                        intent.setClass(context, LoginActivity.class);
                        intent.putExtra("TYPE", 403);
                        intent.putExtra("TAG", tag);
                        context.startActivity(intent);
                        mam.finishAllActivity();
                    }

                    return response.newBuilder()
                            .body(okhttp3.ResponseBody.create(mediaType, json))
                            .build();
                } else {
                    return chain.proceed(request);
                }
            }
        });

        OkHttpClient client = httpClient.addInterceptor(httpLoggingInterceptor).build();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(context, client)
                .build();
        Fresco.initialize(context, config);
        return client;

    }


    protected static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        //CertificateFactory用来证书生成
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书
                InputStream is = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(is));

                if (is != null) {
                    is.close();
                }
            }
            //Create a TrustManager that trusts the CAs in our keyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {

        }
        return null;
    }
}
