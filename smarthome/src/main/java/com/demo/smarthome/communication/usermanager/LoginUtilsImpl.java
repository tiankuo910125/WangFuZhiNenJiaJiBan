package com.demo.smarthome.communication.usermanager;

import com.demo.smarthome.base.utils.HttpsHelper;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.base.utils.Constant;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liukun on 2016/3/28.
 */
public class LoginUtilsImpl {
    private String TAG="LoginUtilsImpl";

    public static UserProfileBean mUserProfileBean = null;

    private static class LoginUtilsImplHolder {
        static final LoginUtilsImpl instance = new LoginUtilsImpl();
    }

    /**
     * 私有的构造函数
     */
    private LoginUtilsImpl() {
        if (mUserProfileBean == null){
            mUserProfileBean = new UserProfileBean();
        }
    }

    public static LoginUtilsImpl getInstance() {
        return LoginUtilsImplHolder.instance;
    }

    public String login(String username, String password){
        String result="";
        String url= String.format(Constant.HttpURL.Accout.httpSignInURL);
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            RequestBody body = new FormBody.Builder()
                    .add("mobile", username)//添加键值对
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("Authorization","1234")
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

            Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    public String modifyPassword(String password, String password_new , String password_confirm){
        String result="";
        String url= Constant.HttpURL.Accout.httpPasswordURL+"?accessToken="+ PreferenceUtil.getString("accessToken","null");
        System.out.println("---------url--------"+url);
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            RequestBody body = new FormBody.Builder()
                    .add("password", password)//添加键值对
                    .add("password_new", password_new)
                    .add("password_confirm",password_confirm)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type","application/json")
                    .addHeader("Accept","application/json")
                    .addHeader("Authorization","1234")
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

            Response response = null;

            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }




    public String logout(String username, String password){
        return null;
    }
}
