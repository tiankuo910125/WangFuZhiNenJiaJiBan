package com.demo.smarthome.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.demo.smarthome.R;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.ui.base.LedCharactorView;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomControlWaterView extends RoomControlBaseView {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private ImageButton mWaterSwitch;
    private ImageButton mGasSwitch;
    private ImageButton mHeaterSwitch;
    private ImageButton mWaterPurifierSwitch;
    private LedCharactorView mEnergyConsumed;
    private String TAG = "RoomControlWaterView";


    public RoomControlWaterView(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_water_view, this);

        mWaterSwitch = (ImageButton)findViewById(R.id.water_control_switch);
        if (mRoomInfo.water_control_device.size()>0) {
            mWaterSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "water_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
            mWaterSwitch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //控制总水阀
                    for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.water_control_device) {
                        if (getValue(deviceInfo.value)==1) {
                            deviceInfo.value = 0;
                            mWaterSwitch.setBackgroundResource(R.drawable.switch_on);
                        }else {
                            deviceInfo.value = 1;
                            mWaterSwitch.setBackgroundResource(R.drawable.switch_off);
                        }
                    }
//                    if (PreferenceUtil.getBoolean(mRoomInfo.roomName + "water_switch", false)) {
//                        mWaterSwitch.setBackgroundResource(R.drawable.switch_off);
//                        PreferenceUtil.putBoolean(mRoomInfo.roomName + "water_switch", false);
//                        //TODO:发送水阀控制关闭命令给网关
//                        for (int i = 0; i < mRoomInfo.water_control_device.size(); i++) {
//                            CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
//                                    mRoomInfo.water_control_device.get(i).tag, false);
//                        }
//                    } else {
//                        mWaterSwitch.setBackgroundResource(R.drawable.switch_on);
//                        PreferenceUtil.putBoolean(mRoomInfo.roomName + "water_switch", true);
//                        //TODO:发送水阀控制开启命令给网关
//                        for (int i = 0; i < mRoomInfo.water_control_device.size(); i++) {
//                            CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
//                                    mRoomInfo.water_control_device.get(i).tag, true);
//                        }
//                    }
                }
            });
        }else{
            findViewById(R.id.water_frame).setVisibility(GONE);
        }

        mGasSwitch = (ImageButton)findViewById(R.id.gas_control_switch);
        if (mRoomInfo.gas_control_device.size()>0) {
            mGasSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "gas_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
            mGasSwitch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //控制燃气阀
                    for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.gas_control_device) {
                        if (getValue(deviceInfo.value)==1) {
                            deviceInfo.value = 0;
                            mWaterSwitch.setBackgroundResource(R.drawable.switch_on);
                        }else {
                            deviceInfo.value = 1;
                            mWaterSwitch.setBackgroundResource(R.drawable.switch_off);
                        }
                    }
//                    if (PreferenceUtil.getBoolean(mRoomInfo.roomName + "gas_switch", false)) {
//                        mGasSwitch.setBackgroundResource(R.drawable.switch_off);
//                        PreferenceUtil.putBoolean(mRoomInfo.roomName + "gas_switch", false);
//                        //TODO:发送水阀控制关闭命令给网关
//                        for (int i = 0; i < mRoomInfo.gas_control_device.size(); i++) {
//                            CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
//                                    mRoomInfo.gas_control_device.get(i).tag, false);
//                        }
//                    } else {
//                        mGasSwitch.setBackgroundResource(R.drawable.switch_on);
//                        PreferenceUtil.putBoolean(mRoomInfo.roomName + "gas_switch", true);
//                        //TODO:发送水阀控制开启命令给网关
//                        for (int i = 0; i < mRoomInfo.gas_control_device.size(); i++) {
//                            CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
//                                    mRoomInfo.gas_control_device.get(i).tag, true);
//                        }
//                    }
                }
            });
        }else {
            findViewById(R.id.gas_frame).setVisibility(GONE);
        }

        mHeaterSwitch=(ImageButton)findViewById(R.id.heating_control_switch);
        if (mRoomInfo.heater_control_device.size()>0) {
            mHeaterSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "heater_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
            mHeaterSwitch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //控制暖气阀
                    for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.heater_control_device) {
                        if (getValue(deviceInfo.value)==1) {
                            deviceInfo.value = 0;
                            mWaterSwitch.setBackgroundResource(R.drawable.switch_on);
                        }else {
                            deviceInfo.value = 1;
                            mWaterSwitch.setBackgroundResource(R.drawable.switch_off);
                        }
                    }
//                    if (PreferenceUtil.getBoolean(mRoomInfo.roomName + "heater_switch", false)) {
//                        mHeaterSwitch.setBackgroundResource(R.drawable.switch_off);
//                        PreferenceUtil.putBoolean(mRoomInfo.roomName + "heater_switch", false);
//                        //TODO:发送暖气控制关闭命令给网关
//                        for (int i = 0; i < mRoomInfo.heater_control_device.size(); i++) {
//                            CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
//                                    mRoomInfo.heater_control_device.get(i).tag, false);
//                        }
//                    } else {
//                        mHeaterSwitch.setBackgroundResource(R.drawable.switch_on);
//                        PreferenceUtil.putBoolean(mRoomInfo.roomName + "heater_switch", true);
//                        //TODO:发送暖气控制开启命令给网关
//                        for (int i = 0; i < mRoomInfo.heater_control_device.size(); i++) {
//                            CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
//                                    mRoomInfo.heater_control_device.get(i).tag, true);
//                        }
//                    }
                }
            });
        }else
        {
            findViewById(R.id.heating_frame).setVisibility(GONE);
        }

        mWaterPurifierSwitch=(ImageButton)findViewById(R.id.water_purifier_control_switch);
        if (mRoomInfo.water_purifier_control_device.size()>0) {
            mWaterPurifierSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "water_purifier_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
            mWaterPurifierSwitch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //控制净水阀
                    for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.water_purifier_control_device) {
                        if (getValue(deviceInfo.value)==1) {
                            deviceInfo.value = 0;
                            mWaterSwitch.setBackgroundResource(R.drawable.switch_on);
                        }else {
                            deviceInfo.value = 1;
                            mWaterSwitch.setBackgroundResource(R.drawable.switch_off);
                        }
                    }
//                    if (PreferenceUtil.getBoolean(mRoomInfo.roomName + "water_purifier_switch", false)) {
//                        mWaterPurifierSwitch.setBackgroundResource(R.drawable.switch_off);
//                        PreferenceUtil.putBoolean(mRoomInfo.roomName + "water_purifier_switch", false);
//                        //TODO:发送净水控制关闭命令给网关
//                        for (int i = 0; i < mRoomInfo.water_purifier_control_device.size(); i++) {
//                            CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
//                                    mRoomInfo.water_purifier_control_device.get(i).tag, false);
//                        }
//                    } else {
//                        mWaterPurifierSwitch.setBackgroundResource(R.drawable.switch_on);
//                        PreferenceUtil.putBoolean(mRoomInfo.roomName + "water_purifier_switch", true);
//                        //TODO:发送净水控制开启命令给网关
//                        for (int i = 0; i < mRoomInfo.water_purifier_control_device.size(); i++) {
//                            CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
//                                    mRoomInfo.water_purifier_control_device.get(i).tag, true);
//                        }
//                    }
                }
            });
        }else
        {
            findViewById(R.id.water_purifier_frame).setVisibility(GONE);
        }

        mEnergyConsumed = (LedCharactorView)findViewById(R.id.energy_consumed_num);
        if (mRoomInfo.energy_control_device.size()>0){
            //控制电
            mEnergyConsumed.setValue(getValue(mRoomInfo.energy_control_device.get(0).value) + "");
        }else {
            findViewById(R.id.elec_frame).setVisibility(GONE);
        }
    }


    @Override
    public void setReceiveData(final ConcurrentHashMap<String, Object> receiveData) {
        super.setReceiveData(receiveData);
        Tasks.executeInBackground(mContext, new BackgroundWork<RoomInfo>() {
                    @Override
                    public RoomInfo doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        ConcurrentHashMap<String, Object> statuMap = new ConcurrentHashMap<String, Object>();// 设备状态数据
//                        String jsonString = receiveData.get("data").toString();
//                        JSONObject receive = new JSONObject(jsonString);
//                        Iterator actions = receive.keys();
//                        while (actions.hasNext()) {
//                            String param = actions.next().toString();
                            //   Log.d("revjson", "action=" + action);
                            // 忽略特殊部分
//                            if (param.equals("cmd") || param.equals("qos") || param.equals("seq") || param.equals("version")) {
//                                continue;
//                            }
//                            Object value = receive.get(param);
//                            //Log.d("RoomEnvViewPager", "param=" + param+ "value=" + value);
//                            statuMap.put(param, value);
//                        }
                        //TODO:获取数值
                        //总水阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.water_control_device) {
                            checkValueFormate(deviceInfo,receiveData);
                        }
                        //燃气阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.gas_control_device) {
                            checkValueFormate(deviceInfo,receiveData);
                        }
                        //暖气阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.heater_control_device) {
                            checkValueFormate(deviceInfo,receiveData);
                        }
                        //净水阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.water_purifier_control_device) {
                            checkValueFormate(deviceInfo,receiveData);
                        }
                        //电
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.energy_control_device) {
                            checkValueFormate(deviceInfo,receiveData);
                        }
//                        if (mRoomInfo.water_control_device.size() > 0) {
//                            for (int i = 0; i < mRoomInfo.water_control_device.size(); i++) {
//                                mRoomInfo.water_control_device.get(i).value = (statuMap.get(mRoomInfo.water_control_device.get(i).tag) == null)
//                                        ? mRoomInfo.water_control_device.get(i).value :
//                                        Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.water_control_device.get(i).tag)));
//                                if (Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.water_control_device.get(i).tag))))
//                                    PreferenceUtil.putBoolean(mRoomInfo.roomName + "water_switch", true);
//                            }
//                        }
//
//                        if (mRoomInfo.gas_control_device.size() > 0) {
//                            for (int i = 0; i < mRoomInfo.gas_control_device.size(); i++) {
//                                mRoomInfo.gas_control_device.get(i).value = (statuMap.get(mRoomInfo.gas_control_device.get(i).tag) == null)
//                                        ? mRoomInfo.gas_control_device.get(i).value :
//                                        Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.gas_control_device.get(i).tag)));
//                                if (Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.gas_control_device.get(i).tag))))
//                                    PreferenceUtil.putBoolean(mRoomInfo.roomName + "gas_switch", true);
//                            }
//                        }
//
//                        if (mRoomInfo.heater_control_device.size() > 0) {
//                            for (int i = 0; i < mRoomInfo.heater_control_device.size(); i++) {
//                                mRoomInfo.heater_control_device.get(i).value = (statuMap.get(mRoomInfo.heater_control_device.get(i).tag) == null)
//                                        ? mRoomInfo.heater_control_device.get(i).value :
//                                        Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.heater_control_device.get(i).tag)));
//                                if (Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.heater_control_device.get(i).tag))))
//                                    PreferenceUtil.putBoolean(mRoomInfo.roomName + "heater_switch", true);
//                            }
//                        }
//
//                        if (mRoomInfo.water_purifier_control_device.size() > 0) {
//                            for (int i = 0; i < mRoomInfo.water_purifier_control_device.size(); i++) {
//                                mRoomInfo.water_purifier_control_device.get(i).value = (statuMap.get(mRoomInfo.water_purifier_control_device.get(i).tag) == null)
//                                        ? mRoomInfo.water_purifier_control_device.get(i).value :
//                                        Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.water_purifier_control_device.get(i).tag)));
//                                if (Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.water_purifier_control_device.get(i).tag))))
//                                    PreferenceUtil.putBoolean(mRoomInfo.roomName + "water_purifier_switch", true);
//                            }
//                        }
//
//                        if (mRoomInfo.energy_control_device.size() > 0) {
//                            for (int i = 0; i < mRoomInfo.energy_control_device.size(); i++) {
//                                mRoomInfo.energy_control_device.get(i).value = (statuMap.get(mRoomInfo.energy_control_device.get(i).tag) == null)
//                                        ? mRoomInfo.energy_control_device.get(i).value :
//                                        Float.parseFloat(String.valueOf(statuMap.get(mRoomInfo.energy_control_device.get(i).tag)));
//                                PreferenceUtil.putString(mRoomInfo.energy_control_device.get(0).tag,  String.valueOf(mRoomInfo.energy_control_device.get(i).value));
//                            }
//                        }
                        return mRoomInfo;
                    }
                }, new Completion<RoomInfo>() {

                    @Override
                    public void onSuccess(Context context, RoomInfo result) {
                        //TODO:更新界面和相关数值
                        //总水阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.water_control_device) {
                            Log.e(TAG,"deviceInfo.value == " + getValue(deviceInfo.value));
                            if (getValue(deviceInfo.value)==1) {
                                mWaterSwitch.setBackgroundResource(R.drawable.switch_on);
                            }else {
                                mWaterSwitch.setBackgroundResource(R.drawable.switch_off);
                            }
                        }
                        //燃气阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.gas_control_device) {
                            if (getValue(deviceInfo.value)==1) {
                                mGasSwitch.setBackgroundResource(R.drawable.switch_on);
                            }else {
                                mGasSwitch.setBackgroundResource(R.drawable.switch_off);
                            }
                        }
                        //暖气阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.heater_control_device) {
                            if (getValue(deviceInfo.value)==1) {
                                mHeaterSwitch.setBackgroundResource(R.drawable.switch_on);
                            }else {
                                mHeaterSwitch.setBackgroundResource(R.drawable.switch_off);
                            }
                        }
                        //净水阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.water_purifier_control_device) {
                            if (getValue(deviceInfo.value)==1) {
                                mWaterPurifierSwitch.setBackgroundResource(R.drawable.switch_on);
                            }else {
                                mWaterPurifierSwitch.setBackgroundResource(R.drawable.switch_off);
                            }
                        }
                        //电阀
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.energy_control_device) {
                                mEnergyConsumed.setValue(getValue(deviceInfo.value) + "");
                        }

//                        if (mRoomInfo.water_control_device.size() > 0) {
//                            mWaterSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "water_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
//                        }
//                        if (mRoomInfo.gas_control_device.size() > 0) {
//                            mGasSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "gas_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
//                        }
//                        if (mRoomInfo.heater_control_device.size() > 0) {
//                            mHeaterSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "heater_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
//                        }
//                        if (mRoomInfo.water_purifier_control_device.size() > 0) {
//                            mWaterPurifierSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "water_purifier_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
//                        }
//                        if (mRoomInfo.energy_control_device.size()>0){
//                            mEnergyConsumed.setValue(PreferenceUtil.getString(mRoomInfo.energy_control_device.get(0).tag,"0"));
//                        }
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }
        );
    }
}
