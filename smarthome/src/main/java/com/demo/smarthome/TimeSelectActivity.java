package com.demo.smarthome;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.ui.adapter.ConfigResultListAdapter;
import com.demo.smarthome.ui.adapter.RepeatWeekdaytemAdapter;
import com.demo.smarthome.ui.base.WheelView;
import com.demo.smarthome.ui.model.ConfigInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeSelectActivity extends BaseSmartHomeActivity {
    private String TAG ="TimeSelectActivity";
    MyActivityManager mam = MyActivityManager.getInstance();

    private Toolbar mToolbar;

    private WheelView mValueView;
    private WheelView mHourView;
    private WheelView mMinuteView;

    private TextView mTitle;
    private TextView mValueName;

    private ListView mWeekdaySeletedList;
    private RepeatWeekdaytemAdapter mRepeatWeekdaytemAdapter;
    private List<Boolean>  mSelectedListValue;

    public static final int TYPE_TEMPERATURE=1;
    public static final int TYPE_HUMIDITY=2;
    public static final int TYPE_AIRQUALITY=3;
    private int mType=TYPE_TEMPERATURE;
    private int mCurrentHour = 3;
    private int mCurrentMinute = 3;
    private int mCurrentValue = 3;

    private ConfigInfo mConfigInfo;
    private int mPosition=-1;

    public final String[] TEMPERATURE = new String[]{
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32","关闭",
    };

    public final String[] HUMIDITY = new String[]{
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80",
            "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "关闭",
    };

    public final String[] AIRQUALITY = new String[]{
            "极优", "优", "良", "关闭",
    };

    public final String[] HOURS = new String[]{
            "00","01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
    };

    public final String[] MINUTES = new String[]{
            "00","01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_select);
        mam.pushOneActivity(TimeSelectActivity.this);

        Gson gson = new Gson();
        try {
            mConfigInfo = gson.fromJson(PreferenceUtil.getString("ConfigInfo", ""), ConfigInfo.class);
        }catch (NullPointerException e){
            e.printStackTrace();
            mConfigInfo = new ConfigInfo();
        }
        if (mConfigInfo == null) mConfigInfo = new ConfigInfo();

        mPosition = getIntent().getIntExtra("position",-1);
        mType = getIntent().getIntExtra("Type",TYPE_TEMPERATURE);
        int selected=getIntent().getIntExtra("weekday_repeat",0);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = (TextView)findViewById(R.id.toolbar_title);
        mValueName = (TextView)findViewById(R.id.setting_item_name);
        if (mType == TYPE_TEMPERATURE) {
            mTitle.setText("添加温度分时设置");
            mValueName.setText("温度设置");
        }
        else if(mType == TYPE_HUMIDITY) {
            mTitle.setText("添加湿度分时设置");
            mValueName.setText("湿度设置");
        }
        else{
            mTitle.setText("添加空气分时设置");
            mValueName.setText("空气设置");
        }

        mSelectedListValue = new ArrayList<>();
        for (int i=0; i<7;i++)
        {
            if(((selected >> i)&(0x01)) == 1){
                mSelectedListValue.add(true);
            }else
                mSelectedListValue.add(false);
        }

        mWeekdaySeletedList = (ListView)findViewById(R.id.weekday_repeat_list);
        mRepeatWeekdaytemAdapter = new RepeatWeekdaytemAdapter(this,mSelectedListValue);
        mWeekdaySeletedList.setAdapter(mRepeatWeekdaytemAdapter);

        mHourView = (WheelView) findViewById(R.id.hour);
        mMinuteView = (WheelView) findViewById(R.id.minutes);
        mValueView = (WheelView)findViewById(R.id.setting_item_value);

        mHourView.setOffset(3);
        mHourView.setSeletion(3);
        mHourView.setItems(Arrays.asList(HOURS));
        mHourView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "hour selectedIndex: " + selectedIndex + ", item: " + item);

                mCurrentHour = selectedIndex - 3;
            }
        });

        mMinuteView.setOffset(3);
        mMinuteView.setSeletion(3);
        mMinuteView.setItems(Arrays.asList(MINUTES));
        mMinuteView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG,"minute selectedIndex: " + selectedIndex + ", item: " + item);

                mCurrentMinute = selectedIndex - 3;
            }
        });

        mValueView.setOffset(3);
        mValueView.setSeletion(3);
        if (mType == TYPE_TEMPERATURE)
            mValueView.setItems(Arrays.asList(TEMPERATURE));
        else if (mType == TYPE_HUMIDITY)
            mValueView.setItems(Arrays.asList(HUMIDITY));
        else
            mValueView.setItems(Arrays.asList(AIRQUALITY));

        mValueView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "value selectedIndex: " + selectedIndex + ", item: " + item);

                mCurrentValue = selectedIndex - 3;
            }
        });

        findViewById(R.id.confirm_btn).setOnClickListener(mOnClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.confirm_btn:
                {
                    save();
                    finish();
                    break;
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        save();
        finish();
    }

    private void save() {
        Log.d(TAG, "save: hour: " + mCurrentHour + ", minute: " + mCurrentMinute);

        int selected=0;
        for (int j=(mRepeatWeekdaytemAdapter.getCount()-1);j>=0;j--)
        {
            int v = (boolean)mRepeatWeekdaytemAdapter.getItem(j)?1:0;
            selected = (selected << 1) | v;
        }

        String value;
        if (mType == TYPE_TEMPERATURE)
            value = TEMPERATURE[mCurrentValue];
        else if(mType == TYPE_HUMIDITY)
            value = HUMIDITY[mCurrentValue];
        else
            value = AIRQUALITY[mCurrentValue];


        if (mType == ConfigResultListAdapter.TYPE_TEMPERATURE){
            ConfigInfo.Temperature temp = mConfigInfo.new Temperature();
            temp.repeat = selected;
            if (value.equals("关闭")) {
                temp.temperature_value = "-";
            }else {
                temp.temperature_value = value;
            }
            temp.hour = HOURS[mCurrentHour];
            temp.minute = MINUTES[mCurrentMinute];
            if(mPosition == -1)
                mConfigInfo.temperatureList.add(temp);
            else if (mPosition>=0)
                mConfigInfo.temperatureList.set(mPosition,temp);

        }else  if (mType == ConfigResultListAdapter.TYPE_HUMIDITY){
            ConfigInfo.Humidity temp = mConfigInfo.new Humidity();
            temp.repeat = selected;
            if (value.equals("关闭")) {
                temp.humidity_value = "-";
            }else {
                temp.humidity_value = value;
            }
            temp.hour = HOURS[mCurrentHour];
            temp.minute = MINUTES[mCurrentMinute];
            if(mPosition == -1)
                mConfigInfo.humidityList.add(temp);
            else if (mPosition>=0)
                mConfigInfo.humidityList.set(mPosition,temp);

        }else if (mType == ConfigResultListAdapter.TYPE_AIRQUALITY){
            ConfigInfo.Airquality temp = mConfigInfo.new Airquality();
            temp.repeat = selected;
            if (value.equals("关闭")) {
                temp.aircontrol = "关闭";
            }else {
                temp.aircontrol = value;
            }
            temp.hour = HOURS[mCurrentHour];
            temp.minute = MINUTES[mCurrentMinute];
            if(mPosition == -1)
                mConfigInfo.airqualityList.add(temp);
            else if (mPosition>=0)
                mConfigInfo.airqualityList.set(mPosition,temp);
        }
        PreferenceUtil.putString("ConfigInfo", GsonTools.GsonToString(mConfigInfo));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(TimeSelectActivity.this);
    }
}
