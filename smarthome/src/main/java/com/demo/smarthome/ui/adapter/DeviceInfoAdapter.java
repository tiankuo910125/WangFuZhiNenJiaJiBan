package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.smarthome.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liukun on 2016/2/23.
 */
public class DeviceInfoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Map<String,String>> mDeviceinfo;

    public DeviceInfoAdapter(Context context,ArrayList<Map<String,String>> deviceinfo)
    {
        mContext=context;
        mInflater = LayoutInflater.from(mContext);
        mDeviceinfo = deviceinfo;
    }

    @Override
    public int getCount() {
        return mDeviceinfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceinfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final TextView detailItem;
        final TextView detailValue;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.device_detail_list_adapter, null);
        }
        detailItem = (TextView) convertView.findViewById(R.id.device_detail_item);
        detailValue = (TextView) convertView.findViewById(R.id.device_detail_value);

        Iterator<Map.Entry <String, String>> it = mDeviceinfo.get(position).entrySet().iterator();
        Map.Entry <String, String> entry = it.next();
        detailItem.setText(entry.getKey().toString());
        detailValue.setText(entry.getValue().toString());

        return convertView;
    }
}
