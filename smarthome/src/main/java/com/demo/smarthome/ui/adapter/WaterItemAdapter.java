package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.ui.model.RoomInfo;

/**
 * Created by liukun on 2016/3/17.
 */
public class WaterItemAdapter extends BaseAdapter {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;


    private String TAG="WaterItemAdapter";
    public WaterItemAdapter(Context context, RoomInfo roomInfo) {
        super();
        mContext = context;
        mRoomInfo = roomInfo;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mRoomInfo.water_device.size();
    }

    @Override
    public RoomInfo.DeviceInfo getItem(int position) {
        return mRoomInfo.water_device.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.room_water_item, null);
        }
        final ImageView water_icon = (ImageView) convertView.findViewById(R.id.water_icon);
        final TextView water_name = (TextView) convertView.findViewById(R.id.water_name);
        final ImageButton water_control_switch = (ImageButton) convertView.findViewById(R.id.water_control_switch);
        final TextView energy_consumed_num = (TextView) convertView.findViewById(R.id.energy_consumed_num);


        water_name.setText(mRoomInfo.water_device.get(position).name);
        if (mRoomInfo.water_device.get(position).category == 1330) {
            energy_consumed_num.setVisibility(View.VISIBLE);
            water_control_switch.setVisibility(View.GONE);
            water_icon.setImageResource(R.drawable.stic_elec_nomal);
            java.text.DecimalFormat df = new java.text.DecimalFormat(".00");//使用向下兼容版本....
//            DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = df.format(Float.parseFloat(mRoomInfo.water_device.get(position).value.toString()));
            energy_consumed_num.setText(p);
        } else {
            energy_consumed_num.setVisibility(View.GONE);
            water_control_switch.setVisibility(View.VISIBLE);
            switch (mRoomInfo.water_device.get(position).category) {
                case 1300:
                    water_icon.setImageResource(R.drawable.stic_water_nomal);
                    break;
                case 1310:
                    water_icon.setImageResource(R.drawable.stic_water_nomal);
                    break;
                case 1320:
                    water_icon.setImageResource(R.drawable.stic_gas_nomal);
                    break;
                case 1340:
                    water_icon.setImageResource(R.drawable.stic_gas_nomal);
                    break;
            }

            String valueStr = String.valueOf(mRoomInfo.water_device.get(position).value);
            if (valueStr.contains("true")) {
                water_control_switch.setImageResource(R.drawable.switch_on);
            } else if (valueStr.contains("false")) {
                water_control_switch.setImageResource(R.drawable.switch_off);
            } else {
                if (Integer.valueOf(valueStr) == 1) {
                    water_control_switch.setImageResource(R.drawable.switch_on);
                } else {
                    water_control_switch.setImageResource(R.drawable.switch_off);
                }
            }

        }


        water_control_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomInfo.DeviceInfo deviceInfo = mRoomInfo.water_device.get(position);
                String valueStr = String.valueOf(deviceInfo.value);
                boolean flag = false;
                if (valueStr.equalsIgnoreCase("true")) {
                    flag = false;
                } else if (valueStr.equalsIgnoreCase("false")) {
                    flag = true;
                } else {
                    if (Integer.parseInt(valueStr) == 1) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                }

                if (flag) {
                    water_control_switch.setImageResource(R.drawable.switch_on);
                    deviceInfo.value = 1;
                } else {
                    water_control_switch.setImageResource(R.drawable.switch_off);
                    deviceInfo.value = 0;
                }

                //TODO:向网关发送灯光操纵命令
                Log.i(TAG, "onClick: SEND cmd "+deviceInfo.tag+ " +   "+ deviceInfo.value);
                CmdCenter.getInstance(mContext).cWrite(CmdCenter.getInstance(mContext).getXpgWifiDevice(),
                        mRoomInfo.water_device.get(position).tag, flag ? 1 : 0);
            }
        });

        return convertView;
    }
}
