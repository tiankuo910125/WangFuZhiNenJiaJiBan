package com.demo.smarthome.communication.usermanager;

import com.arcsoft.closeli.Log;
import com.demo.smarthome.base.utils.HttpsHelper;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.base.utils.Constant;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liukun on 2016/3/28.
 */
public class RegisterUtilsImpl {
    private String TAG="RegisterUtilsImpl";


    private static class RegisterUtilsImplHolder {
        static final RegisterUtilsImpl instance = new RegisterUtilsImpl();
    }

    /**
     * 私有的构造函数
     */
    private RegisterUtilsImpl() {

    }

    public static RegisterUtilsImpl getInstance() {
        return RegisterUtilsImplHolder.instance;
    }

    public String getVertifyCode(String mobile) {
        String result="";
        String url= String.format(Constant.HttpURL.Accout.httpVertifyCodeURL, mobile);
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, "");
            Request request = new Request.Builder()
                .url(url)
                .header("Content-Type","application/json")
                .addHeader("Accept","application/json")
                .addHeader("App-Key","1234567890")
                .addHeader("App-OS","Android")
                .addHeader("App-OS-Model","6.0")
                .addHeader("App-OS-SDK","23")
                .addHeader("App-OS-Release","6.0")
                .addHeader("App-Package-Name","SmartHome")
                .addHeader("App-Version-Name","1.0.0")
                .addHeader("App-Version-Code","12")
                .addHeader("App-Install-Time","143565456")
                .addHeader("App-Update-Time","143565456")
                .addHeader("App-UDID","14356545645")
                .post(body)
                .build();
            Log.d(TAG,"Request content is "+ request);
        Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getResetVertifyCode(String mobile) {
        String result="";
        String url= String.format(Constant.HttpURL.Accout.httpResetCodeURL, mobile);
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, "");
            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("App-Key","1234567890")
                    .addHeader("App-OS","Android")
                    .addHeader("App-OS-Model","6.0")
                    .addHeader("App-OS-SDK","23")
                    .addHeader("App-OS-Release","6.0")
                    .addHeader("App-Package-Name","SmartHome")
                    .addHeader("App-Version-Name","1.0.0")
                    .addHeader("App-Version-Code","12")
                    .addHeader("App-Install-Time","143565456")
                    .addHeader("App-Update-Time","143565456")
                    .addHeader("App-UDID","14356545645")
                    .post(body)
                    .build();
            Log.d(TAG,"Request content is "+ request);
            Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String registerUser(String username,String password, String vertifyCode) {

        String result="";
        String url= String.format(Constant.HttpURL.Accout.httpSignUpURL);
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            RequestBody body = new FormBody.Builder()
                    .add("mobile", username)//添加键值对
                    .add("password", password)
                    .add("verify_code",vertifyCode)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("App-Key","1234567890")
                    .addHeader("App-OS","Android")
                    .addHeader("App-OS-Model","6.0")
                    .addHeader("App-OS-SDK","23")
                    .addHeader("App-OS-Release","6.0")
                    .addHeader("App-Package-Name","SmartHome")
                    .addHeader("App-Version-Name","1.0.0")
                    .addHeader("App-Version-Code","12")
                    .addHeader("App-Install-Time","143565456")
                    .addHeader("App-Update-Time","143565456")
                    .addHeader("App-UDID","14356545645")
                    .post(body)
                    .build();
            Log.d(TAG,"Request content is "+ request);
            Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;}

    public String resetPassword(String username,String password, String vertifyCode) {

        String result="";
        String url= String.format(Constant.HttpURL.Accout.httpResetPasswordURL);
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            RequestBody body = new FormBody.Builder()
                    .add("mobile", username)//添加键值对
                    .add("password", password)
                    .add("verify_code",vertifyCode)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("App-Key","1234567890")
                    .addHeader("App-OS","Android")
                    .addHeader("App-OS-Model","6.0")
                    .addHeader("App-OS-SDK","23")
                    .addHeader("App-OS-Release","6.0")
                    .addHeader("App-Package-Name","SmartHome")
                    .addHeader("App-Version-Name","1.0.0")
                    .addHeader("App-Version-Code","12")
                    .addHeader("App-Install-Time","143565456")
                    .addHeader("App-Update-Time","143565456")
                    .addHeader("App-UDID","14356545645")
                    .post(body)
                    .build();
            Log.d(TAG,"Request content is "+ request);
            Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;}

    public String updateUserInfo(String username,String mobilenum,String email) { return null;}

    public String updateUserHeadIcon(byte[] data) { return null;}

    public String registerRouter(String routerinfo) {
        String result="";
        String url= String.format(Constant.HttpURL.Accout.httpScanURL+"?accessToken=%s", PreferenceUtil.getString("accessToken","null"));
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            RequestBody body = new FormBody.Builder()
                    .add("code", routerinfo)//添加键值对
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("App-Key","1234567890")
                    .addHeader("App-OS","Android")
                    .addHeader("App-OS-Model","6.0")
                    .addHeader("App-OS-SDK","23")
                    .addHeader("App-OS-Release","6.0")
                    .addHeader("App-Package-Name","SmartHome")
                    .addHeader("App-Version-Name","1.0.0")
                    .addHeader("App-Version-Code","12")
                    .addHeader("App-Install-Time","143565456")
                    .addHeader("App-Update-Time","143565456")
                    .addHeader("App-UDID","14356545645")
                    .post(body)
                    .build();
            Log.d(TAG,"Request content is "+ request);
            Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;}

    public String getAdministratorQRCode(int houseID){
        String result="";
        String url= String.format(Constant.HttpURL.Accout.httpQrcodeURL+"?accessToken=%s",houseID,PreferenceUtil.getString("accessToken","null"));
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("App-Key","1234567890")
                    .addHeader("App-OS","Android")
                    .addHeader("App-OS-Model","6.0")
                    .addHeader("App-OS-SDK","23")
                    .addHeader("App-OS-Release","6.0")
                    .addHeader("App-Package-Name","SmartHome")
                    .addHeader("App-Version-Name","1.0.0")
                    .addHeader("App-Version-Code","12")
                    .addHeader("App-Install-Time","143565456")
                    .addHeader("App-Update-Time","143565456")
                    .addHeader("App-UDID","14356545645")
                    .build();
            Log.d(TAG,"Request content is "+ request);
            Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;}

    public String refreshAdministratorQRCode(int houseID){
        String result="";
        String url= String.format(Constant.HttpURL.Accout.httpQrcodeURL+"?accessToken=%s",houseID,PreferenceUtil.getString("accessToken","null"));
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            RequestBody body = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("App-Key","1234567890")
                    .addHeader("App-OS","Android")
                    .addHeader("App-OS-Model","6.0")
                    .addHeader("App-OS-SDK","23")
                    .addHeader("App-OS-Release","6.0")
                    .addHeader("App-Package-Name","SmartHome")
                    .addHeader("App-Version-Name","1.0.0")
                    .addHeader("App-Version-Code","12")
                    .addHeader("App-Install-Time","143565456")
                    .addHeader("App-Update-Time","143565456")
                    .addHeader("App-UDID","14356545645")
                    .put(body)
                    .build();
            Log.d(TAG,"Request content is "+ request);
            Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;}

}
