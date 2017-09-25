package com.demo.smarthome.communication.housemanager;

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
public class HouseUtilsImpl {
    private String TAG="HouseUtilsImpl";


    private static class HouseUtilsImplHolder {
        static final HouseUtilsImpl instance = new HouseUtilsImpl();
    }

    private HouseUtilsImpl() {}

    public static HouseUtilsImpl getInstance() {
        return HouseUtilsImplHolder.instance;
    }

    public String getHouseInfo(String name,int id){
        String result="";
        String url= String.format(Constant.HttpURL.House.httpHouseURL+"?accessToken=%s", id, PreferenceUtil.getString("accessToken","null"));
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
        return result;
    }

    public String updateHouseInfo(String name,int id){
        String result="";
        String url= String.format(Constant.HttpURL.House.httpHouseURL+"?accessToken=%s", id,PreferenceUtil.getString("accessToken","null"));
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = new FormBody.Builder()
                    .add("name", name)//添加键值对
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
        return result;
    }

    public String getRoomInfo(String name,int id){
        //TODO:向服务器更新RooomInfo
        String result="";
        String url= String.format(Constant.HttpURL.Room.httpRoomURL+"?accessToken=%s", id,PreferenceUtil.getString("accessToken","null"));
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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
        return result;
    }

    public String updateRoomInfo(String name,int id){
        //TODO:向服务器更新RooomInfo
        String result="";
        String url= String.format(Constant.HttpURL.Room.httpRoomURL+"?accessToken=%s", id,PreferenceUtil.getString("accessToken","null"));
        Log.d("updateRoomInfo","url is "+url);
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = new FormBody.Builder()
                    .add("name", name)//添加键值对
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
        return result;
    }



    public String getBindingHouse(String code){
        //TODO:向服务器更新RooomInfo
        String result="";
        String url = Constant.HttpURL.House.httpBindingHouse+"?accessToken="+PreferenceUtil.getString("accessToken","null");
//        String url= String.format(Constant.HttpURL.House.httpBindingHouse+"?accessToken=",PreferenceUtil.getString("accessToken","null"));
        Log.d("updateRoomInfo","url is "+url);
        OkHttpClient httpClient = HttpsHelper.createOkhttp();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = new FormBody.Builder()
                    .add("code", code)//添加键值对
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
        return result;
    }


}
