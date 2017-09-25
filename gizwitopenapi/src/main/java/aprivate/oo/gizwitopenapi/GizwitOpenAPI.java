package aprivate.oo.gizwitopenapi;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.security.MessageDigest;

import aprivate.oo.gizwitopenapi.requestbody.BindBody;
import aprivate.oo.gizwitopenapi.requestbody.LoginBody;
import aprivate.oo.gizwitopenapi.requestbody.RegistBody;
import aprivate.oo.gizwitopenapi.response.BindingList;
import aprivate.oo.gizwitopenapi.response.DeviceData;
import aprivate.oo.gizwitopenapi.response.LoginResponse;
import aprivate.oo.gizwitopenapi.response.RegistResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhuxiaolong on 2017/8/31.
 */

public class GizwitOpenAPI {

    private final String TAG = "GizwitOpenAPI";
    private static GizwitOpenAPI instance;
    private Retrofit retrofit;
    private OkHttpClient client;
    private OpenApi openApi;

    private String token;
    private String appid;

    public static GizwitOpenAPI getInstance() {
        return instance;
    }


    public static void init(String appid) {
        instance = new GizwitOpenAPI(appid);
    }

    public GizwitOpenAPI(final String appid) {
        this.appid = appid;
        Retrofit.Builder builder = new Retrofit.Builder();
        /*设置请求头*/
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request result = request.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("X-Gizwits-Application-Id", appid)
                        .build();
                return chain.proceed(result);
            }
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = clientBuilder.addInterceptor(logging).build();

        /*生成 retrofit 类实例*/
        retrofit = builder.baseUrl("https://api.gizwits.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openApi = retrofit.create(OpenApi.class);
    }


    private void resetHttpHead() {
        Retrofit.Builder builder = new Retrofit.Builder();
        /*设置请求头*/
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        Log.i(TAG, "resetHttpHead: appid  " + appid);
        Log.i(TAG, "resetHttpHead: token  " + token);
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request result = request.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("X-Gizwits-Application-Id", appid)
                        .header("X-Gizwits-User-token", token)
                        .build();

                return chain.proceed(result);
            }
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = clientBuilder.addInterceptor(logging).build();

        /*生成 retrofit 类实例*/
        retrofit = builder.baseUrl("https://api.gizwits.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openApi = retrofit.create(OpenApi.class);
    }
    //注册
    public void registServer(String username, String password, final RequestCallback<RegistResponse> callback) {
        openApi.registServer(new RegistBody(username, password)).enqueue(new Callback<RegistResponse>() {
            @Override
            public void onResponse(Call<RegistResponse> call, retrofit2.Response<RegistResponse> response) {
                Log.e(TAG,"code" + response.code());
                if (response.isSuccessful()) {
//                    if (response.code() == 200) {
                        token = response.body().getToken();
                        resetHttpHead();// 添加 请求头的 token
                        callback.onSuccess(response.body());
//                    } else {
//                    Log.e(TAG,"成功的请求失败respose " + response.message());
//                    callback.onFailure("请求错误");
//                    }
                } else {
                    try {
                        Log.e(TAG,"我就是请求失败respose " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onFailure("请求错误");
                }
            }
            @Override
            public void onFailure(Call<RegistResponse> call, Throwable t) {
                callback.onFailure("网络请求错误");
            }
        });
    }

    //绑定
    public void bindServer(String product_key, String mac,String dev_alias, String product_secret , final RequestCallback<BindingList.DevicesBean> callback) {

        Retrofit.Builder bindbuilder = new Retrofit.Builder();
        /*设置请求头*/
        OkHttpClient.Builder bindclientBuilder = new OkHttpClient.Builder();

        Log.i(TAG, "bind: appid  " + appid);
        Log.i(TAG, "bind: token  " + token);

        Long l = System.currentTimeMillis()/1000;
        final String timestamp = l + "";

        final String singature = MD5Encode(product_secret + timestamp).trim().toLowerCase();

        bindclientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request result = request.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("X-Gizwits-Application-Id", appid)
                        .header("X-Gizwits-User-token", token)
                        .header("X-Gizwits-Timestamp", timestamp)
                        .header("X-Gizwits-Signature", singature)
                        .build();

                return chain.proceed(result);
            }
        });


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient bingclient = bindclientBuilder.addInterceptor(logging).build();

        /*生成 retrofit 类实例*/
        Retrofit bindretrofit = bindbuilder.baseUrl("https://api.gizwits.com/")
                .client(bingclient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OpenApi bindopenApi = bindretrofit.create(OpenApi.class);

        bindopenApi.bindServer(new BindBody(product_key, mac,dev_alias)).enqueue(new Callback<BindingList.DevicesBean>() {
            @Override
            public void onResponse(Call<BindingList.DevicesBean> call, retrofit2.Response<BindingList.DevicesBean> response) {
                Log.e(TAG,"code" + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG,"机智云绑定成功啦！" + response);
                    callback.onSuccess(response.body());
                } else {
                    try {
                        Log.e(TAG,"我就是请求失败respose " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onFailure("请求错误");
                }
            }
            @Override
            public void onFailure(Call<BindingList.DevicesBean> call, Throwable t) {
                callback.onFailure("网络请求错误");
            }
        });
    }




    public void loginServer(String username, String password, final RequestCallback<LoginResponse> callback) {
        openApi.loginServer(new LoginBody(username, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        token = response.body().getToken();
                        resetHttpHead();// 添加 请求头的 token
                        callback.onSuccess(response.body());

                    } else {
                        callback.onFailure("请求错误");
                    }
                } else {
                    callback.onFailure("请求错误");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure("loginfailure");
            }
        });
    }


    public void getDeviceList(final RequestCallback callback) {
        String url = "/app/bindings?limit=20&skip=0";
        openApi.getDeviceList(url).enqueue(new Callback<BindingList>() {
            @Override
            public void onResponse(Call<BindingList> call, retrofit2.Response<BindingList> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Log.i(TAG, "onResponse: device list size : " + response.body().getDevices().size());
                        callback.onSuccess(response.body());

                    } else {
                        callback.onFailure("请求错误");
                    }
                } else {
                    callback.onFailure("请求错误");
                }
                Log.i(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(Call<BindingList> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }


    public void getDeviceData(String did, final RequestCallback<DeviceData> callback) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.build();
        openApi.getDeviceData(did).enqueue(new Callback<DeviceData>() {
            @Override
            public void onResponse(Call<DeviceData> call, retrofit2.Response<DeviceData> response) {
                Log.i(TAG, "onResponse: is succcessful " + response.isSuccessful());
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onFailure("请求错误" + response.code());
                    }
                } else {
                    try {
                        callback.onFailure("请求错误" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeviceData> call, Throwable t) {

            }
        });
    }

    public void sendCommand(String did, String tag, String cmd, final RequestCallback callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"attrs\":{\"");
        stringBuilder.append(tag);
        stringBuilder.append("\":");
        stringBuilder.append(cmd);
        stringBuilder.append("}}");
        Log.i(TAG, "sendCommand: " + stringBuilder.toString());
        openApi.contralDevice(did, stringBuilder.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if (callback != null) {
                    if (response.code() == 200) {
                        Log.i(TAG, "onResponse: " + response.isSuccessful());
                        callback.onSuccess("on success " + response.body());
                    } else {
                        try {
                            callback.onFailure(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (callback != null) {
                    callback.onFailure(t.getMessage());

                }
            }
        });
    }

    public void registNewUser(){

    }



    public void getCmdHistory(String did, long st, final RequestCallback callback) {
        openApi.getCmdHistory(did, "cmd", (st - 3600) + "", st + "", "0", "20").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.i(TAG, "onResponse: getCmdHistory");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(TAG, "onFailure:getCmdHistory ");
            }
        });
    }

    public interface RequestCallback<T> {
        void onSuccess(T t);

        void onFailure(String msg);
    }

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        }
        catch (Exception ex) {

        }
        return resultString;
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }


    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private final static String[] hexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

}
