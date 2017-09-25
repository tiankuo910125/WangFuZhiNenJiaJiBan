package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.ui.model.RoomInfo;
import com.demo.smarthome.R;

/**
 * Created by liukun on 2016/3/17.
 */
public class AdjustLampItemAdapter extends BaseAdapter {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private ImageButton mAdjustLampSwitch;

    public static final int ADJUST_LAMP_ON = 87;
    public static final int ADJUST_LAMP_OFF = 86;
    public static final int ADJUST_LAMP_FLASH = 88;


    public AdjustLampItemAdapter(Context context, RoomInfo roomInfo) {
        super();
        mContext = context;
        mRoomInfo = roomInfo;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mRoomInfo.adjust_lighting_device.size();
    }

    @Override
    public RoomInfo.DeviceInfo getItem(int position) {
        return mRoomInfo.adjust_lighting_device.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.room_control_lamp_adjust_adapter_view, null);
        }
        final RoomInfo.DeviceInfo deviceInfo=mRoomInfo.adjust_lighting_device.get(position);
        ((TextView)convertView.findViewById(R.id.adjust_lamp_name)).setText(mRoomInfo.adjust_lighting_device.get(position).name);
        final ImageButton mAdjustLampSwitch = (ImageButton)convertView.findViewById(R.id.adjust_lamp_control_switch);
        final TextView adjust_lamp_state = (TextView) convertView.findViewById(R.id.adjust_lamp_state);
        if (Integer.parseInt(mRoomInfo.adjust_lighting_device.get(position).value.toString()) > ADJUST_LAMP_OFF){
            mAdjustLampSwitch.setBackgroundResource(R.drawable.switch_on_1);
            adjust_lamp_state.setText("开");
        }else{
            mAdjustLampSwitch.setBackgroundResource(R.drawable.switch_off_1);
            adjust_lamp_state.setText("关");
        }
        mAdjustLampSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(mRoomInfo.adjust_lighting_device.get(position).value.toString()) > ADJUST_LAMP_OFF) {
                    mAdjustLampSwitch.setBackgroundResource(R.drawable.switch_off_1);
                    adjust_lamp_state.setText("关");
                    //TODO:发送灯光控制指令
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            mRoomInfo.adjust_lighting_device.get(position).tag, ADJUST_LAMP_OFF);
                } else {
                    mAdjustLampSwitch.setBackgroundResource(R.drawable.switch_on_1);
                    adjust_lamp_state.setText("开");
                    //TODO:发送灯光控制指令
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            mRoomInfo.adjust_lighting_device.get(position).tag, ADJUST_LAMP_ON);
                }
            }
        });

        SeekBar mLightingSeekBar = (SeekBar)convertView.findViewById(R.id.lighting_seekbar);
        mLightingSeekBar.setProgress(0);
        mLightingSeekBar.setEnabled(false);

        SeekBar mColorSeekBar = (SeekBar)convertView.findViewById(R.id.color_seekbar);
        mColorSeekBar.setProgress(0);
        mColorSeekBar.setEnabled(false);
        return convertView;
    }

}
