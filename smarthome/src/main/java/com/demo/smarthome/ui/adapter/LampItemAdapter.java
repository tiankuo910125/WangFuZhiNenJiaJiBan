package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.ui.model.RoomInfo;

/**
 * Created by liukun on 2016/3/17.
 */
public class LampItemAdapter extends BaseAdapter {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;
    private final String TAG = "LampItemAdapter";

    public LampItemAdapter(Context context, RoomInfo roomInfo) {
        super();
        mContext = context;
        mRoomInfo = roomInfo;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mRoomInfo.lighting_device.size();
    }

    @Override
    public RoomInfo.DeviceInfo getItem(int position) {
        return mRoomInfo.lighting_device.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.room_control_lamp_adapter_view, null);
        }
        final RoomInfo.DeviceInfo deviceInfo = mRoomInfo.lighting_device.get(position);
        final TextView lampName = ((TextView) convertView.findViewById(R.id.lamp_name));
        final TextView lampState = ((TextView) convertView.findViewById(R.id.lamp_state));
        final ImageButton lightingIcon = ((ImageButton) convertView.findViewById(R.id.lamp_control_switch));

        lampName.setText(deviceInfo.name);
        String valueStr = String.valueOf(deviceInfo.value);
        if (valueStr.contains("false")) {
            lampState.setText("关");
            lightingIcon.setImageResource(R.drawable.switch_off);
        } else if (valueStr.contains("true")) {
            lampState.setText("开");
            lightingIcon.setImageResource(R.drawable.switch_on);
        } else {
            if (Integer.valueOf(valueStr) == 1) {//如果是开启状态
                lampState.setText("开");
                lightingIcon.setImageResource(R.drawable.switch_on);
            } else {//如果是关闭状态
                lampState.setText("关");
                lightingIcon.setImageResource(R.drawable.switch_off);
            }
        }


        lightingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueStr = String.valueOf(deviceInfo.value);
                if (Integer.valueOf(valueStr) == 1) {//如果是开启状态 执行关闭
                    lampState.setText("关");
                    deviceInfo.value = 0;
                    lightingIcon.setImageResource(R.drawable.switch_off);
                } else {//如果是关闭状态
                    lampState.setText("开");
                    deviceInfo.value = 1;
                    lightingIcon.setImageResource(R.drawable.switch_on);
                }
                CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, deviceInfo.value);
            }
        });

        return convertView;
    }
}
