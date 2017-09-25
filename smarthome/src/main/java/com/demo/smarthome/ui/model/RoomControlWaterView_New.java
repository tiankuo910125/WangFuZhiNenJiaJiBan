package com.demo.smarthome.ui.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;

import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.base.utils.view_util.CustomListView;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.R;
import com.demo.smarthome.ui.base.LedCharactorView;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomControlWaterView_New extends RoomControlBaseView {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;
    private CustomListView customListView;

    private ImageButton mWaterSwitch;
    private ImageButton mGasSwitch;
    private ImageButton mHeaterSwitch;
    private ImageButton mWaterPurifierSwitch;
    private LedCharactorView mEnergyConsumed;


    public RoomControlWaterView_New(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_water_view, this);

    }


    @Override
    public void setReceiveData(final ConcurrentHashMap<String, Object> receiveData) {
        super.setReceiveData(receiveData);
        Tasks.executeInBackground(mContext, new BackgroundWork<RoomInfo>() {
                    @Override
                    public RoomInfo doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        ConcurrentHashMap<String, Object> statuMap = new ConcurrentHashMap<String, Object>();// 设备状态数据
                        String jsonString = receiveData.get("data").toString();
                        JSONObject receive = new JSONObject(jsonString);
                        Iterator actions = receive.keys();
                        while (actions.hasNext()) {
                            String param = actions.next().toString();
                            //   Log.d("revjson", "action=" + action);
                            // 忽略特殊部分
                            if (param.equals("cmd") || param.equals("qos") || param.equals("seq") || param.equals("version")) {
                                continue;
                            }
                            Object value = receive.get(param);
                            //Log.d("RoomEnvViewPager", "param=" + param+ "value=" + value);
                            statuMap.put(param, value);
                        }
                        //TODO:获取数值

                        if (mRoomInfo.water_control_device.size() > 0) {
                            for (int i = 0; i < mRoomInfo.water_control_device.size(); i++) {
                                mRoomInfo.water_control_device.get(i).value = (statuMap.get(mRoomInfo.water_control_device.get(i).tag) == null)
                                        ? mRoomInfo.water_control_device.get(i).value :
                                        Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.water_control_device.get(i).tag)));
                                if (Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.water_control_device.get(i).tag))))
                                    PreferenceUtil.putBoolean(mRoomInfo.roomName + "water_switch", true);
                            }
                        }

                        if (mRoomInfo.gas_control_device.size() > 0) {
                            for (int i = 0; i < mRoomInfo.gas_control_device.size(); i++) {
                                mRoomInfo.gas_control_device.get(i).value = (statuMap.get(mRoomInfo.gas_control_device.get(i).tag) == null)
                                        ? mRoomInfo.gas_control_device.get(i).value :
                                        Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.gas_control_device.get(i).tag)));
                                if (Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.gas_control_device.get(i).tag))))
                                    PreferenceUtil.putBoolean(mRoomInfo.roomName + "gas_switch", true);
                            }
                        }

                        if (mRoomInfo.heater_control_device.size() > 0) {
                            for (int i = 0; i < mRoomInfo.heater_control_device.size(); i++) {
                                mRoomInfo.heater_control_device.get(i).value = (statuMap.get(mRoomInfo.heater_control_device.get(i).tag) == null)
                                        ? mRoomInfo.heater_control_device.get(i).value :
                                        Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.heater_control_device.get(i).tag)));
                                if (Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.heater_control_device.get(i).tag))))
                                    PreferenceUtil.putBoolean(mRoomInfo.roomName + "heater_switch", true);
                            }
                        }

                        if (mRoomInfo.water_purifier_control_device.size() > 0) {
                            for (int i = 0; i < mRoomInfo.water_purifier_control_device.size(); i++) {
                                mRoomInfo.water_purifier_control_device.get(i).value = (statuMap.get(mRoomInfo.water_purifier_control_device.get(i).tag) == null)
                                        ? mRoomInfo.water_purifier_control_device.get(i).value :
                                        Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.water_purifier_control_device.get(i).tag)));
                                if (Boolean.parseBoolean(String.valueOf(statuMap.get(mRoomInfo.water_purifier_control_device.get(i).tag))))
                                    PreferenceUtil.putBoolean(mRoomInfo.roomName + "water_purifier_switch", true);
                            }
                        }

                        if (mRoomInfo.energy_control_device.size() > 0) {
                            for (int i = 0; i < mRoomInfo.energy_control_device.size(); i++) {
                                mRoomInfo.energy_control_device.get(i).value = (statuMap.get(mRoomInfo.energy_control_device.get(i).tag) == null)
                                        ? mRoomInfo.energy_control_device.get(i).value :
                                        Float.parseFloat(String.valueOf(statuMap.get(mRoomInfo.energy_control_device.get(i).tag)));
                                PreferenceUtil.putString(mRoomInfo.energy_control_device.get(0).tag,  String.valueOf(mRoomInfo.energy_control_device.get(i).value));
                            }
                        }

                        return mRoomInfo;
                    }
                }, new Completion<RoomInfo>() {

                    @Override
                    public void onSuccess(Context context, RoomInfo result) {
                        //TODO:更新界面和相关数值
                        if (mRoomInfo.water_control_device.size() > 0) {
                            mWaterSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "water_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
                        }
                        if (mRoomInfo.gas_control_device.size() > 0) {
                            mGasSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "gas_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
                        }
                        if (mRoomInfo.heater_control_device.size() > 0) {
                            mHeaterSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "heater_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
                        }
                        if (mRoomInfo.water_purifier_control_device.size() > 0) {
                            mWaterPurifierSwitch.setBackgroundResource(PreferenceUtil.getBoolean(mRoomInfo.roomName + "water_purifier_switch", false) ? R.drawable.switch_on : R.drawable.switch_off);
                        }
                        if (mRoomInfo.energy_control_device.size()>0){
                            mEnergyConsumed.setValue(PreferenceUtil.getString(mRoomInfo.energy_control_device.get(0).tag,"0"));
                        }
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }
        );
    }
}
