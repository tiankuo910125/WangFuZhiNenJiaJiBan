package com.demo.smarthome.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.demo.smarthome.R;
import com.demo.smarthome.ui.base.CircleProgressView;
import com.demo.smarthome.ui.base.LedCharactorView;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;

import java.util.List;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomEnvironmentStatusView extends RoomControlBaseView {
    private Context mContext;
    private LayoutInflater mInflater;

    private LedCharactorView mTemperatureLedCharactorView;
    private LedCharactorView mHumidityLedCharactorView;
    private LedCharactorView mAirqualityLedCharactorView;
    private CircleProgressView mTemperatureCircleProgressView;
    private CircleProgressView mHumidityCircleProgressView;
    private CircleProgressView mAirqualityCircleProgressView;
    private String TAG = "RESView";

    public RoomEnvironmentStatusView(Context context, List<RoomInfo> roominfo, int position) {
        super(context);
        mContext = context;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_info_frame_adapter, this);

        mTemperatureLedCharactorView = (LedCharactorView)findViewById(R.id.temperature_led_value);
        mHumidityLedCharactorView = (LedCharactorView)findViewById(R.id.humidity_led_value);
        mAirqualityLedCharactorView = (LedCharactorView)findViewById(R.id.airquality_led_value);

        mTemperatureCircleProgressView = (CircleProgressView)findViewById(R.id.temperature_progress);
        mHumidityCircleProgressView = (CircleProgressView)findViewById(R.id.humidity_progress);
        mAirqualityCircleProgressView = (CircleProgressView)findViewById(R.id.airquality_progress);

        setUpdateData(roominfo,position);
    }

    public void getUpDate(List<RoomInfo> roominfo, int position){
        setUpdateData(roominfo,position);
    }

    public void setUpdateData(List<RoomInfo> info, int position) {
        RoomInfo roominfo = info.get(position);
        if (roominfo.roomName.equals("-1")){//室内显示平均值
            int temperature=0,humidity=0,pm25=0;
            int temperatureNum=0,humidityNum=0,pm25Num=0;
            for (int i=0;i<info.size();i++){
                if (i==position)
                    continue;

                if (info.get(i).temperature_o_device.size()>0) {
                    temperature += Float.parseFloat(String.valueOf(info.get(i).temperature_o_device.get(0).value));
                    temperatureNum++;
                }
                if (info.get(i).humidity_o_device.size()>0) {
                    humidity += Float.parseFloat(String.valueOf(info.get(i).humidity_o_device.get(0).value));
                    humidityNum++;
                }
                if (info.get(i).pm25_o_device.size()>0) {
                    pm25 += Float.parseFloat(String.valueOf(info.get(i).pm25_o_device.get(0).value));
                    pm25Num++;
                }
            }
            temperature=temperature/10;
            humidity = humidity/10;
            mTemperatureLedCharactorView.setValue(String.valueOf(temperature/temperatureNum));
            mTemperatureCircleProgressView.setValue(temperature/(info.size()-1));
            mHumidityLedCharactorView.setValue(String.valueOf(humidity / humidityNum));
            mHumidityCircleProgressView.setValue(humidity/(info.size()-1));
            mAirqualityLedCharactorView.setValue(String.valueOf(pm25 / pm25Num));
            mAirqualityCircleProgressView.setValue( pm25 / (info.size() - 1));
        }else {
            if (roominfo.temperature_o_device.size() > 0) {
                mTemperatureLedCharactorView.setValue(String.valueOf((int) Float.parseFloat(String.valueOf(roominfo.temperature_o_device.get(0).value)) / 10));

                RoomInfo.DeviceInfo deviceInfo=roominfo.temperature_o_device.get(0);
                Log.i(TAG,deviceInfo.name+" VALUE = "+deviceInfo.value);

                mTemperatureCircleProgressView.setValue((int) Float.parseFloat(String.valueOf(roominfo.temperature_o_device.get(0).value)) / 10);
            } else {
                mTemperatureLedCharactorView.setValue("-");
                mTemperatureCircleProgressView.setValue(0);
            }
            if (roominfo.humidity_o_device.size() > 0) {
                RoomInfo.DeviceInfo deviceInfo=roominfo.humidity_o_device.get(0);
                Log.i(TAG,deviceInfo.name+" VALUE = "+deviceInfo.value);
                mHumidityLedCharactorView.setValue(String.valueOf((int) Float.parseFloat(String.valueOf(roominfo.humidity_o_device.get(0).value)) / 10));
                mHumidityCircleProgressView.setValue((int) Float.parseFloat(String.valueOf(roominfo.humidity_o_device.get(0).value)) / 10);
            } else {
                mHumidityLedCharactorView.setValue("-");
                mHumidityCircleProgressView.setValue(0);
            }
            if (roominfo.pm25_o_device.size() > 0) {
                RoomInfo.DeviceInfo deviceInfo=roominfo.pm25_o_device.get(0);
                Log.i(TAG,deviceInfo.name+" VALUE = "+deviceInfo.value);
                mAirqualityLedCharactorView.setValue(String.valueOf((int) Float.parseFloat(String.valueOf(roominfo.pm25_o_device.get(0).value))));
                mAirqualityCircleProgressView.setValue((int) Float.parseFloat(String.valueOf(roominfo.pm25_o_device.get(0).value)));
            } else {
                mAirqualityLedCharactorView.setValue("-");
                mAirqualityCircleProgressView.setValue(0);
            }
        }
    }
}
