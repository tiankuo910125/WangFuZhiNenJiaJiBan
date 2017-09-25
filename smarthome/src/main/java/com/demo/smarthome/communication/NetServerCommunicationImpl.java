package com.demo.smarthome.communication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.communication.weather.WeatherUtilsImpl;
import com.demo.smarthome.base.exception.NetException;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.communication.usermanager.RegisterUtilsImpl;

/**
 * Created by liukun on 2016/3/25.
 */
public class NetServerCommunicationImpl {
    private String TAG="NetServerCommunicationImpl";
    private Context mContext;
    private static NetServerCommunicationImpl instance = null;

    public static final int MSG_WEATHER_INFO_CALLBACK=5001;
    public static final int MSG_LOCATION_INFO_CALLBACK=5002;
    public static final int MSG_REGISTER_ROUTER_CALLBACK=5003;
    public static final int MSG_HOUSE_INFO_CALLBACK=5004;
    public static final int MSG_UPDATE_HOUSE_INFO_CALLBACK=5005;
    public static final int MSG_DEVICE_INFO_CALLBACK=5006;
    public static final int MSG_DEVICES_LIST_CALLBACK=5007;
    public static final int MSG_ROOM_STATE_CALLBACK=5008;

    public synchronized static NetServerCommunicationImpl getInstance(Context context) {
        if (instance == null) {
            instance = new NetServerCommunicationImpl(context);
        }
        return instance;
    }
    /**
     * 私有的构造函数
     */
    private NetServerCommunicationImpl(Context context) {
        mContext = context;
    }


    public void getWeatherData(final Handler handler, final String cityname) throws RemoteException {
        Tasks.executeInBackground(mContext, new BackgroundWork<String>() {
            @Override
            public String doInBackground() throws Exception {
                String result = "";
                try {
                    result = WeatherUtilsImpl.getInstance().getWeather(cityname);
                } catch (NetException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }, new Completion<String>() {
            @Override
            public void onSuccess(Context context, String result) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_WEATHER_INFO_CALLBACK;
                    Bundle data = new Bundle();
                    data.putString("result", result);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Context context, Exception e) {

            }
        });
    }


    public void registerRouter(final Handler handler, final String info) throws RemoteException{
        Tasks.executeInBackground(mContext, new BackgroundWork<String>() {
            @Override
            public String doInBackground() throws Exception {
                //发动到后台服务器验证并注册网关
                return RegisterUtilsImpl.getInstance().registerRouter(info);
            }
        }, new Completion<String>() {
            @Override
            public void onSuccess(Context context, String result) {
                if (result != null) {
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = MSG_REGISTER_ROUTER_CALLBACK;
                            Bundle data = new Bundle();
                            data.putString("result", result);
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (handler != null) {
                            handler.sendEmptyMessage(MSG_REGISTER_ROUTER_CALLBACK);
                        }
                    }
                }

            @Override
            public void onError(Context context, Exception e) {

            }
        });
    }






}
