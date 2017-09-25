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
import com.demo.smarthome.TimeSelectActivity;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.ui.model.ConfigInfo;

/**
 * Created by liukun on 2016/3/21.
 */
public class ConfigResultListAdapter extends BaseAdapter {
    private String TAG="MessageListAdapter";
    private Context mContext;

    public static final int TYPE_TEMPERATURE=1;
    public static final int TYPE_HUMIDITY=2;
    public static final int TYPE_AIRQUALITY=3;

    private String[] weekday={"周一","周二","周三","周四","周五","周六","周日"};

    private ConfigInfo mConfigInfo;
    private int mType;
    private LayoutInflater mInflater;
    private boolean bEdit= false;
    public ConfigResultListAdapter(Context context, ConfigInfo configInfo, int type) {
        super();
        mContext = context;
        mConfigInfo = configInfo;
        mType = type;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mType == TYPE_TEMPERATURE)
            return mConfigInfo.temperatureList.size();
        else if (mType == TYPE_HUMIDITY)
            return mConfigInfo.humidityList.size();
        else
            return mConfigInfo.airqualityList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mType == TYPE_TEMPERATURE)
            return mConfigInfo.temperatureList.get(position);
        else if (mType == TYPE_HUMIDITY)
            return mConfigInfo.humidityList.get(position);
        else
            return mConfigInfo.airqualityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.fragment_config_result_list_item,null);
        }
        ImageButton deleteBtn = (ImageButton)convertView.findViewById(R.id.delete_btn);
        final ImageButton switchBtn = (ImageButton)convertView.findViewById(R.id.switch_btn);
        ImageButton detailBtn = (ImageButton)convertView.findViewById(R.id.detail_btn);
        TextView value_text = (TextView)convertView.findViewById(R.id.value);
        TextView time_text = (TextView)convertView.findViewById(R.id.time);
        TextView repeat_text = (TextView)convertView.findViewById(R.id.day);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == TYPE_TEMPERATURE)
                    mConfigInfo.temperatureList.remove(position);
                else if (mType == TYPE_HUMIDITY)
                    mConfigInfo.humidityList.remove(position);
                else
                    mConfigInfo.airqualityList.remove(position);
                PreferenceUtil.putString("ConfigInfo", GsonTools.GsonToString(mConfigInfo));
                notifyDataSetChanged();
            }
        });
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bSwitch;
                if (mType == TYPE_TEMPERATURE) {
                    mConfigInfo.temperatureList.get(position).switch_onoff = !mConfigInfo.temperatureList.get(position).switch_onoff;
                    bSwitch = mConfigInfo.temperatureList.get(position).switch_onoff;
                }
                else if (mType == TYPE_HUMIDITY) {
                    mConfigInfo.humidityList.get(position).switch_onoff = !mConfigInfo.humidityList.get(position).switch_onoff;
                    bSwitch = mConfigInfo.humidityList.get(position).switch_onoff;
                }
                else {
                    mConfigInfo.airqualityList.get(position).switch_onoff = !mConfigInfo.airqualityList.get(position).switch_onoff;
                    bSwitch = mConfigInfo.airqualityList.get(position).switch_onoff;
                }
                PreferenceUtil.putString("ConfigInfo", GsonTools.GsonToString(mConfigInfo));
                if (bSwitch)
                    switchBtn.setBackgroundResource(R.drawable.switch_on_1);
                else
                    switchBtn.setBackgroundResource(R.drawable.switch_off_1);
            }
        });
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, TimeSelectActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION|Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.putExtra("Type", mType);
                mIntent.putExtra("position",position);
                mContext.startActivity(mIntent);
            }
        });

        if (bEdit){
            deleteBtn.setVisibility(View.VISIBLE);
            switchBtn.setVisibility(View.GONE);
            detailBtn.setVisibility(View.VISIBLE);
        }else {
            deleteBtn.setVisibility(View.GONE);
            switchBtn.setVisibility(View.VISIBLE);
            detailBtn.setVisibility(View.GONE);
        }

        if (mType == TYPE_TEMPERATURE) {
            value_text.setText(mConfigInfo.temperatureList.get(position).temperature_value);
            time_text.setText(mConfigInfo.temperatureList.get(position).hour+":"
            +mConfigInfo.temperatureList.get(position).minute);
            repeat_text.setText(getWeeklyRepeat(mConfigInfo.temperatureList.get(position).repeat));
        }
        else if (mType == TYPE_HUMIDITY){
            value_text.setText(mConfigInfo.humidityList.get(position).humidity_value);
            time_text.setText(mConfigInfo.humidityList.get(position).hour+":"
                    +mConfigInfo.humidityList.get(position).minute);
            repeat_text.setText(getWeeklyRepeat(mConfigInfo.humidityList.get(position).repeat));
        }
        else{
            value_text.setText(mConfigInfo.airqualityList.get(position).aircontrol);
            time_text.setText(mConfigInfo.airqualityList.get(position).hour+":"
                    +mConfigInfo.airqualityList.get(position).minute);
            repeat_text.setText(getWeeklyRepeat(mConfigInfo.airqualityList.get(position).repeat));
        }

        return convertView;
    }
    
    private String getWeeklyRepeat(int repeat) {
        String result="";
        for (int i=0;i<weekday.length;i++){
            if (((repeat >> i)&0x1)==1){
                result += weekday[i]+" ";
            }
        }
        return result;
    }

    public void setEditMode(boolean b){
        bEdit = b;
    }

    public boolean getEditMode(){
        return bEdit;
    }
}
