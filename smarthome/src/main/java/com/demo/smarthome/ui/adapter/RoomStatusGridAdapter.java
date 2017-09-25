package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.ui.model.RoomInfo;

import java.util.List;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomStatusGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<RoomInfo> mRoomInfo;
    private LayoutInflater mInflater;
    private int mCheckedItem = 0;
    private final String defaultValue = "--";

    private final String TAG = "RoomStatusGridAdapter";

    public RoomStatusGridAdapter(Context context, List<RoomInfo> roominfo) {
        super();
        mContext = context;
        mRoomInfo = roominfo;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mRoomInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.all_rooms_air_status_grid_adapter, null);
        }
        TextView mRoomname = (TextView) convertView.findViewById(R.id.room_name);
        TextView mTemperature = (TextView) convertView.findViewById(R.id.temperature);
        TextView mHumidity = (TextView) convertView.findViewById(R.id.humidity);
        TextView mPm25 = (TextView) convertView.findViewById(R.id.pm25);
        RoomInfo item = mRoomInfo.get(position);

        /*设置 房间名称*/
        String[] sourceStrArray = item.roomName.split("--");
        if (sourceStrArray != null && sourceStrArray.length == 2) {
            mRoomname.setText(sourceStrArray[1]);
        } else {
            mRoomname.setText("");
        }
        /*设置 温度值*/
        if (item.temperature_o_device.size() > 0) {
            Object temvalue = item.temperature_o_device.get(0).value;
            setValuesForTv(mTemperature, temvalue, "℃", 10);
        } else {
            mTemperature.setText(defaultValue);
        }
        /*设置湿度值*/
        if (item.humidity_o_device.size() > 0) {
            Object humValue = item.humidity_o_device.get(0).value;
            setValuesForTv(mHumidity, humValue, "%", 10);
        } else {
            mHumidity.setText(defaultValue);
        }
        /*设置 pm2.5 的值*/
        if (item.pm25_o_device.size() > 0) {
            Object pmValue = item.pm25_o_device.get(0).value;
            setValuesForTv(mPm25, pmValue, "", 1);
        } else {
            mPm25.setText(defaultValue);
        }

//        if (item.roomName.equals("-1")) {  //室内显示平均值
//            float temperature = 0, humidity = 0, pm25 = 0;
//            int temperatureNum = 0, humidityNum = 0, pm25Num = 0;
//            for (int i = 0; i < mRoomInfo.size(); i++) {
//                if (i == position)
//                    continue;
//                if (mRoomInfo.get(i).temperature_o_device.size() > 0) {
//                    temperature += Float.parseFloat(String.valueOf(mRoomInfo.get(i).temperature_o_device.get(0).value));
//                    temperatureNum++;
//                }
//                if (mRoomInfo.get(i).humidity_o_device.size() > 0) {
//                    humidity += Float.parseFloat(String.valueOf(mRoomInfo.get(i).humidity_o_device.get(0).value));
//                    humidityNum++;
//                }
//                if (mRoomInfo.get(i).pm25_o_device.size() > 0) {
//                    pm25 += Float.parseFloat(String.valueOf(mRoomInfo.get(i).pm25_o_device.get(0).value));
//                    pm25Num++;
//                }
//            }
//            temperature = temperature / 10;
//            humidity = humidity / 10;
//            mTemperature.setText(String.format("%1.1f", temperature / temperatureNum));
//            mHumidity.setText(String.format("%1.1f", humidity / humidityNum));
//            mPm25.setText(String.format("%1.1f", pm25 / pm25Num));
//        }
        if (mCheckedItem == position)
            (convertView.findViewById(R.id.room_info_frame)).setBackgroundResource(R.drawable.text_hightlight_bg);
        else
            (convertView.findViewById(R.id.room_info_frame)).setBackgroundResource(0);
        return convertView;
    }

    private void setValuesForTv(TextView mTemperature, Object value, String unit, int radio) {
        String valueStr = String.valueOf(value);
        String tempValue = String.format("%1.1f", Float.valueOf(valueStr) / radio);
        mTemperature.setText(tempValue + unit);
//        Log.i(TAG, "setValuesForTv: " + valueStr + " unit " + unit);
    }

    public void setCheckedBackground(int position, boolean b) {
        if (b)
            mCheckedItem = position;
    }
}
