package com.demo.smarthome.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.demo.smarthome.R;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomControlFanView extends RoomControlBaseView {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private ImageButton mFanSwitch;


    public RoomControlFanView(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_fan_view, this);

        mFanSwitch = (ImageButton) findViewById(R.id.fan_control_switch);
        mFanSwitch.setImageResource(R.drawable.switch_off);
        mFanSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (RoomInfo.DeviceInfo info : mRoomInfo.fan_device) {
                    if (getValue(info.value) == 1) {
                        info.value = 0;
                        mFanSwitch.setImageResource(R.drawable.switch_off);
                    } else {
                        info.value = 1;
                        mFanSwitch.setImageResource(R.drawable.switch_on);
                    }
                    CmdCenter.getInstance(mContext).cWrite(null, info.tag, info.value);
                }
            }
        });
    }

    @Override
    public void setReceiveData(final ConcurrentHashMap<String, Object> receiveData) {
        super.setReceiveData(receiveData);
        Tasks.executeInBackground(mContext, new BackgroundWork<RoomInfo>() {
                    @Override
                    public RoomInfo doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        ConcurrentHashMap<String, Object> statuMap = new ConcurrentHashMap<String, Object>();// 设备状态数据
                        //TODO:获取数值

                        for (RoomInfo.DeviceInfo info : mRoomInfo.fan_device) {
                            info.value = checkValueFormate(info, receiveData);
                        }

                        return mRoomInfo;
                    }
                }, new Completion<RoomInfo>() {

                    @Override
                    public void onSuccess(Context context, RoomInfo result) {
                        //TODO:更新界面和相关数值
                        for (RoomInfo.DeviceInfo info : mRoomInfo.fan_device) {
                            if (getValue(info.value) == 1) {
                                mFanSwitch.setImageResource(R.drawable.switch_on);
                            } else {
                                mFanSwitch.setImageResource(R.drawable.switch_off);
                            }
                        }
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }
        );
    }

}
