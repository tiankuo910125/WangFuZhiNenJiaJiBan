package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.communication.jsonbean.sub.Devices;
import com.demo.smarthome.ui.JournalActivity;

import java.util.ArrayList;

/**
 * Created by liukun on 2016/2/22.
 */
public class DeviceListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private String mRoomName;
    private ArrayList<Devices> mAllDevices;
    private ArrayList<String> mDeviceState;
    public DeviceListAdapter(Context context, String room, ArrayList<Devices> devices, ArrayList<String> deviceState)
    {
        mContext=context;
        mInflater = LayoutInflater.from(mContext);
        mRoomName = room;
        mAllDevices = devices;
        mDeviceState = deviceState;
    }

    @Override
    public int getCount() {
        return mAllDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView deviceName;
        final TextView deviceState;
        ImageButton detailBtn;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.device_list_adapter, null);
        }

        deviceName = (TextView) convertView.findViewById(R.id.device_name);
        deviceState = (TextView) convertView.findViewById(R.id.device_state);
        detailBtn  = (ImageButton)convertView.findViewById(R.id.detail_btn);

        final Devices it = mAllDevices.get(position);
        deviceName.setText(it.getName());

        String state = mDeviceState.get(position);
        if (state.equals("true")) deviceState.setText("ON");
        else if (state.equals("false")) deviceState.setText("OFF");
        else deviceState.setText(state);

        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:跳转到设备详情页面
                Intent intent = new Intent();
                intent.setClass(mContext,JournalActivity.class);
                intent.putExtra("deviceid",it.getId());
                intent.putExtra("title",it.getName());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
