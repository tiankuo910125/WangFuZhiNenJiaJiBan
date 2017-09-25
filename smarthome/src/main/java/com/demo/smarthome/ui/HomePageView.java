package com.demo.smarthome.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.demo.smarthome.OutsideWeatherActivity;
import com.demo.smarthome.R;
import com.demo.smarthome.RoomControlActivity;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.NetServerCommunicationImpl;
import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.tools.GizwitHolders;
import com.demo.smarthome.ui.adapter.AdjustLampItemAdapter;
import com.demo.smarthome.ui.adapter.CurtainItemAdapter;
import com.demo.smarthome.ui.adapter.RoomStatusGridAdapter;
import com.demo.smarthome.ui.model.RoomInfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import aprivate.oo.gizwitopenapi.GizwitOpenAPI;
import aprivate.oo.gizwitopenapi.response.DeviceData;

import static java.lang.String.valueOf;

/**
 * Created by liukun on 2016/2/15.
 */
public class HomePageView extends RelativeLayout {
    private boolean flg;

    private String TAG = "HomePageView";
    private Context mContext;
    private BDLocation mBDLocation;
    private List<RoomInfo> mRoomInfo;
    private List<RoomEnvironmentStatusView> mRoomEnvironmentViewList;

    private ViewPager mRoomEnvironmentViewPager;
    private RoomEnvironmentStatusViewAdapter mRoomEnvironmentStatusViewAdapter;
    private TextView mRoomTitle;
    private ImageButton mPrevRoomBtn;
    private ImageButton mNextRoomBtn;
    private TextView all_room_security_status_text;
    private TextView all_room_light_status_text;

    private GridView mRoomsStatusGrid;
    private RoomStatusGridAdapter mRoomStatusGridAdapter;

    private CheckBox mCurtianBtn;
    private CheckBox mAirQualityBtn;
    private CheckBox mLightBtn;
    private CheckBox mTemperatureBtn;
    private CheckBox mHumidityBtn;
    private CheckBox mSecurityBtn;

    private ImageButton mHomeBtn;
    private ImageButton mLeaveBtn;


    private TextView control_title_air;
    private TextView control_title_light;
    private TextView control_title_curtain;
    private TextView control_title_temperature;
    private TextView control_title_humidity;
    private TextView control_title_security;

    public static final int REQUEST_HOME_INIT_VIEW = 100;
    public static final int COMMAND_LOCATION_REFRESH = 101;


    private SwipeRefreshLayout swipeRefreshLayout;


    private Handler mHandler = new Handler() {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "recieve msg is " + msg.what);
            String result;
            Bundle data;
            switch (msg.what) {
                case REQUEST_HOME_INIT_VIEW: {
                    initUIFrame();
                    break;
                }
                case NetServerCommunicationImpl.MSG_LOCATION_INFO_CALLBACK: {
                    result = PreferenceUtil.getString(Constant.HouseSystemManager.LOCATION);
                    Log.d(TAG, "location result is " + result);
                    mBDLocation = GsonTools.getBean(result, BDLocation.class);
                    Log.d(TAG, "city is " + mBDLocation.getCity());
                    PreferenceUtil.putString(Constant.HouseSystemManager.CITY, mBDLocation.getCity());
                    PreferenceUtil.putString(Constant.HouseSystemManager.ADDRESS, mBDLocation.getAddrStr());
                    mHandler.sendEmptyMessage(COMMAND_LOCATION_REFRESH);
                    break;
                }
                case COMMAND_LOCATION_REFRESH: {
                    break;
                }
            }
            Log.d(TAG, "leave handler");
        }
    };

    public HomePageView(Context context, List<RoomInfo> roomInfo) {
        super(context);
        gson = new Gson();
        mRoomInfo = roomInfo;
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_viewpage, this);

        mRoomEnvironmentViewList = new ArrayList<>();
        mRoomEnvironmentViewPager = (ViewPager) findViewById(R.id.room_info_frame);
        mRoomEnvironmentStatusViewAdapter = new RoomEnvironmentStatusViewAdapter();
        mRoomEnvironmentViewPager.setAdapter(mRoomEnvironmentStatusViewAdapter);
        mRoomEnvironmentViewPager.addOnPageChangeListener(mPagerChangeListener);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh: ");
                reGetState();
            }
        });

        mRoomsStatusGrid = (GridView) findViewById(R.id.all_room_air_status_grid);
        mRoomStatusGridAdapter = new RoomStatusGridAdapter(mContext, mRoomInfo);
        mRoomsStatusGrid.setAdapter(mRoomStatusGridAdapter);
        int size = mRoomInfo.size();
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources().getDisplayMetrics();
        float density = dm.density;
        int allWidth = (int) (60 * size * density);
        int itemWidth = (int) (50 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.FILL_PARENT);
        mRoomsStatusGrid.setLayoutParams(params);
        mRoomsStatusGrid.setColumnWidth(itemWidth);
        mRoomsStatusGrid.setHorizontalSpacing(10);
        mRoomsStatusGrid.setStretchMode(GridView.NO_STRETCH);
        mRoomsStatusGrid.setNumColumns(size);
        mRoomsStatusGrid.setOnItemClickListener(mGridItemClickListener);

        control_title_air = (TextView) findViewById(R.id.control_title_air);
        control_title_light = (TextView) findViewById(R.id.control_title_light);
        control_title_curtain = (TextView) findViewById(R.id.control_title_curtain);
        control_title_temperature = (TextView) findViewById(R.id.control_title_temperature);
        control_title_humidity = (TextView) findViewById(R.id.control_title_humnity);
        control_title_security = (TextView) findViewById(R.id.control_title_security);

        mRoomTitle = (TextView) findViewById(R.id.room_name);
        mPrevRoomBtn = (ImageButton) findViewById(R.id.room_prev_btn);
        mNextRoomBtn = (ImageButton) findViewById(R.id.room_next_btn);
        mPrevRoomBtn.setOnClickListener(mImageButtonClickListner);
        mNextRoomBtn.setOnClickListener(mImageButtonClickListner);
        mHomeBtn = (ImageButton) findViewById(R.id.at_home_btn);
        mLeaveBtn = (ImageButton) findViewById(R.id.outside_btn);
        all_room_security_status_text = (TextView) findViewById(R.id.all_room_security_status_text);
        all_room_light_status_text = (TextView) findViewById(R.id.all_room_light_status_text);

        mCurtianBtn = (CheckBox) findViewById(R.id.curtain_btn);
        mAirQualityBtn = (CheckBox) findViewById(R.id.air_btn);
        mLightBtn = (CheckBox) findViewById(R.id.light_btn);
        mTemperatureBtn = (CheckBox) findViewById(R.id.temperature_btn);
        mHumidityBtn = (CheckBox) findViewById(R.id.humidity_btn);
        mSecurityBtn = (CheckBox) findViewById(R.id.security_btn);
        mCurtianBtn.setOnClickListener(mImageButtonClickListner);
        mAirQualityBtn.setOnClickListener(mImageButtonClickListner);
        mLightBtn.setOnClickListener(mImageButtonClickListner);
        mTemperatureBtn.setOnClickListener(mImageButtonClickListner);
        mHumidityBtn.setOnClickListener(mImageButtonClickListner);
        mSecurityBtn.setOnClickListener(mImageButtonClickListner);

        mHomeBtn.setOnClickListener(mImageButtonClickListner);
        mLeaveBtn.setOnClickListener(mImageButtonClickListner);
        findViewById(R.id.weather_prev_btn).setOnClickListener(mImageButtonClickListner);
        findViewById(R.id.room_control_next_btn).setOnClickListener(mImageButtonClickListner);

        mHandler.sendEmptyMessage(REQUEST_HOME_INIT_VIEW);
//        setButton(); //这个方法设置了 一键按钮的可见性
    }

    private void setButton() {
        int tagAir = 0;
        int tagCurtian = 0;
        int tagTemp = 0;
        int taghum = 0;
        int tagSecurity = 0;
        int tagLight = 0;

        for (RoomInfo roomInfo : mRoomInfo) {
            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.akeytemperaturedevice) {
                Log.i("a123", "setButton: temp " + tagTemp);
                tagTemp++;
            }
            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.akeyhumiditydevice) {
                Log.i("a123", "setButton: temp " + tagTemp);
                taghum++;
            }
            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.electronicPurifier_device) {
                Log.i("a123", "setButton: air " + tagAir);
                tagAir++;
            }
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.akeywindowcurtaindevice){
//                tagCurtian++;
//            }
            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.akeywindowcurtaindevice) {
                Log.i("a123", "setButton:  curtian " + tagCurtian);
                tagCurtian++;
            }
            if (PreferenceUtil.getString(Constant.SP_MONITOR) != null && !PreferenceUtil.getString(Constant.SP_MONITOR).equals("")) {
                Log.i("a123", "setButton: security " + tagSecurity);
                tagSecurity++;
            }
            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.akeyligntingdevice) {
                Log.i("a123", "setButton: light " + tagLight);
                tagLight++;
            }
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.adjust_lighting_device){
//                tagLight++;
//            }

//            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.akeytemperaturedevice) {
//
//                tagTemp++;
//            }
//            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.akeyhumiditydevice) {
//                tagTemp++;
//            }
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.temperature_i_device){
//                tagTemp++;
//            }
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.humidity_i_device){
//                tagTemp++;
//            }

            //--------------------------------------------------------------------------------------
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.electronicPurifier_device){
//                tagAir++;
//            }
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.curtain_device){
//                tagCurtian++;
//            }
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.window_device){
//                tagCurtian++;
//            }
//            if(PreferenceUtil.getString(Constant.SP_MONITOR)!=null && !PreferenceUtil.getString(Constant.SP_MONITOR).equals("")){
//                tagSecurity++;
//            }
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.lighting_device){
//                tagLight++;
//            }
//            for(RoomInfo.DeviceInfo deviceInfo : roomInfo.adjust_lighting_device){
//                tagLight++;
//            }
        }

        if (tagAir > 0) {
            mAirQualityBtn.setVisibility(View.VISIBLE);
            control_title_air.setVisibility(View.VISIBLE);
        } else {
            //原来是gone
            mAirQualityBtn.setVisibility(View.VISIBLE);
            control_title_air.setVisibility(View.VISIBLE);
        }
        if (tagCurtian > 0) {
            mCurtianBtn.setVisibility(View.VISIBLE);
            control_title_curtain.setVisibility(View.VISIBLE);
        } else {
            mCurtianBtn.setVisibility(View.VISIBLE);
            control_title_curtain.setVisibility(View.VISIBLE);
        }
        if (tagTemp > 0) {
            mTemperatureBtn.setVisibility(View.VISIBLE);
            control_title_temperature.setVisibility(View.VISIBLE);
        } else {
            mTemperatureBtn.setVisibility(View.VISIBLE);
            control_title_temperature.setVisibility(View.VISIBLE);
        }
        if (tagSecurity > 0) {//一键布放
            mSecurityBtn.setVisibility(View.VISIBLE);
            control_title_security.setVisibility(View.VISIBLE);
        } else {
            mSecurityBtn.setVisibility(View.VISIBLE);
            control_title_security.setVisibility(View.VISIBLE);
        }
        if (tagLight > 0) {
            mLightBtn.setVisibility(View.VISIBLE);
            control_title_light.setVisibility(View.VISIBLE);
        } else {
            mLightBtn.setVisibility(View.VISIBLE);
            control_title_light.setVisibility(View.VISIBLE);
        }


        mAirQualityBtn.setVisibility((GizwitHolders.getInstance().getAkeyAirDevice() == null) ? INVISIBLE : VISIBLE);
        control_title_air.setVisibility((GizwitHolders.getInstance().getAkeyAirDevice() == null) ? INVISIBLE : VISIBLE);

        mTemperatureBtn.setVisibility((GizwitHolders.getInstance().getAkeyTemDevice() == null) ? INVISIBLE : VISIBLE);
        control_title_temperature.setVisibility((GizwitHolders.getInstance().getAkeyTemDevice() == null) ? INVISIBLE : VISIBLE);

        mHumidityBtn.setVisibility((GizwitHolders.getInstance().getAkeyHumDevice() == null) ? INVISIBLE : VISIBLE);
        control_title_humidity.setVisibility((GizwitHolders.getInstance().getAkeyHumDevice() == null) ? INVISIBLE : VISIBLE);

    }


    public void upDate(List<RoomInfo> roomInfo) {
        Log.i(TAG, "upDate: ");
        mRoomInfo = roomInfo;
        mRoomEnvironmentStatusViewAdapter.notifyDataSetChanged();
        mRoomStatusGridAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("ResourceType")
    private boolean initUIFrame() {
        Log.i(TAG, "initUIFrame: ");
        for (int i = 0; i < mRoomInfo.size(); i++) {
            RoomEnvironmentStatusView item = new RoomEnvironmentStatusView(mContext, mRoomInfo, i);
            mRoomEnvironmentViewList.add(item);
            mRoomEnvironmentStatusViewAdapter.notifyDataSetChanged();
        }
        return true;
    }


    ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mRoomInfo.get(position).roomName.equals("-1")) {
                mRoomTitle.setText("室内状态");
            } else {
                String str = mRoomInfo.get(position).roomName;
                String[] sourceStrArray = str.split("--");
                if (sourceStrArray != null && sourceStrArray.length == 2) {
                    mRoomTitle.setText(sourceStrArray[1] + "状态");
                } else {
                    mRoomTitle.setText("室内状态");
                }
            }

            if (position == 0) {
                mPrevRoomBtn.setClickable(false);
            } else if (position == (mRoomEnvironmentViewList.size() - 1)) {
                mNextRoomBtn.setClickable(false);
            } else {
                mPrevRoomBtn.setClickable(true);
                mNextRoomBtn.setClickable(true);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void reGetState() {
        String did = GizwitHolders.getInstance().getDid();
        Log.i(TAG, "reGetState: " + did);
        GizwitOpenAPI.getInstance().getDeviceData(did, new GizwitOpenAPI.RequestCallback<DeviceData>() {
            @Override
            public void onSuccess(DeviceData deviceData) {
//                deviceData.getAttr().
                swipeRefreshLayout.setRefreshing(false);
                Log.i(TAG, "onSuccess: ");
                ConcurrentHashMap<String, Object> stateMap = new ConcurrentHashMap<String, Object>();
                for (Map.Entry<String, JsonElement> entry : deviceData.getAttr().entrySet()) {
                    stateMap.put(entry.getKey(), entry.getValue());
                }
                setReceiveData(stateMap);
                mRoomEnvironmentStatusViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                Log.i(TAG, "onFailure: " + msg);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    public void setReceiveData(final ConcurrentHashMap<String, Object> dataMap) {

        Tasks.executeInBackground(mContext, new BackgroundWork<List<RoomInfo>>() {
                    @Override
                    public List<RoomInfo> doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        //设置一键空气净化状态
                        refreshValue(GizwitHolders.getInstance().getAkeyAirDevice(), dataMap);
                        //设置一键照明状态
                        refreshValue(GizwitHolders.getInstance().getAkeyLightDevice(), dataMap);
                        //设置一键窗与窗帘状态
                        refreshValue(GizwitHolders.getInstance().getAkeyWindowDevice(), dataMap);
                        //设置一键温度控制状态
                        refreshValue(GizwitHolders.getInstance().getAkeyTemDevice(), dataMap);
                        //设置一键湿度控制状态
                        refreshValue(GizwitHolders.getInstance().getAkeyHumDevice(), dataMap);
                        //设置一键布防控制状态
                        refreshValue(GizwitHolders.getInstance().getAkeySecDevice(), dataMap);
                        //设置一键离家状态
                        refreshValue(GizwitHolders.getInstance().getAkeyHomeDevice(), dataMap);
                        //设置一键回家状态
                        refreshValue(GizwitHolders.getInstance().getAkeyLeaveDevice(), dataMap);
                        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
                        //设置各个类型设备的状态
                        for (RoomInfo roomInfo : mRoomInfo) {
                            Log.i(TAG, "doInBackground: set room infor " + roomInfo.roomName);
                            //房间内的温度设备
                            for (RoomInfo.DeviceInfo info : roomInfo.temperature_o_device) {
                                Log.i(TAG, "doInBackground: 设置 温度传感器值");
                                refreshValue(info, dataMap);
                            }
                            //房间内的湿度设备
                            for (RoomInfo.DeviceInfo info : roomInfo.humidity_o_device) {
                                Log.i(TAG, "doInBackground: 设置 湿度传感器值");
                                refreshValue(info, dataMap);
                            }
                            //房间内的灯设备
                            for (RoomInfo.DeviceInfo info : roomInfo.lighting_device) {
                                Log.i(TAG, "doInBackground: 设置 室内 灯状态");
                                refreshValue(info, dataMap);
                            }
                            for (RoomInfo.DeviceInfo info : roomInfo.adjust_lighting_device) {
                                Log.i(TAG, "doInBackground: 设置 室内 渐变灯状态");
                                refreshValue(info, dataMap);
                            }
                            //房间内 PM 2.5传感器
                            for (RoomInfo.DeviceInfo info : roomInfo.pm25_o_device) {
                                Log.i(TAG, "doInBackground: 设置室内 pm2.5 值");
                                refreshValue(info, dataMap);
                            }
                            //设置净化器状态
                        }
                        return mRoomInfo;
                    }
                }, new Completion<List<RoomInfo>>() {

                    @Override
                    public void onSuccess(Context context, List<RoomInfo> result) {
                        //TODO:更新界面和相关数值
                        swipeRefreshLayout.setRefreshing(false);
                        mRoomStatusGridAdapter.notifyDataSetChanged();
                        int curIndex = mRoomEnvironmentViewPager.getCurrentItem();//获取当前显示的room 索引值
                        RoomEnvironmentStatusView currentStausview = mRoomEnvironmentViewList.get(curIndex);
                        currentStausview.setUpdateData(result, curIndex);
                        String tem = null;
                        int lightCount = 0;

                        for (RoomInfo roomInfo : result) {
                            Log.e(TAG,"roominfo  result == " + result);
                            //判断有多少盏灯开启
                            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.adjust_lighting_device) {
                                if(getValue(deviceInfo.value) == 1){
                                    lightCount++;
                                }
                                Log.e(TAG,"deviceInfo1  result == " + deviceInfo + "count=="+ lightCount);
                            }
                            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.lighting_device) {
                                if(getValue(deviceInfo.value) == 1){
                                    lightCount++;
                                }
                                Log.e(TAG,"deviceInfo2  result == " + deviceInfo + "count=="+ lightCount);
                            }
                            //判断安全模式
                            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.alert_device) {
                                if(getValue(deviceInfo.value) == 1){
                                    all_room_security_status_text.setText("检测到异常状态");
                                    all_room_security_status_text.setTextColor(Color.parseColor(String.valueOf(R.color.juhuang)));
                                }
//                                Log.e(TAG,"deviceInfo1  result == " + deviceInfo + "count=="+ lightCount);
                            }

                        }
                        all_room_security_status_text.setText("未检测到异常状态");
                        all_room_light_status_text.setText("共有" + lightCount + "盏灯开启");

                        if (GizwitHolders.getInstance().getAkeyTemDevice() != null) {
                            tem = valueOf(GizwitHolders.getInstance().getAkeyTemDevice().value);
                            setButtonState(tem, mTemperatureBtn);
                        }
                        if (GizwitHolders.getInstance().getAkeyAirDevice() != null) {
                            tem = valueOf(GizwitHolders.getInstance().getAkeyAirDevice().value);
                            setButtonState(tem, mAirQualityBtn);
                        }

                        if (GizwitHolders.getInstance().getAkeyHumDevice() != null) {
                            tem = valueOf(GizwitHolders.getInstance().getAkeyHumDevice().value);
                            setButtonState(tem, mHumidityBtn);
                        }
                        if (GizwitHolders.getInstance().getAkeyLightDevice() != null) {
                            tem = valueOf(GizwitHolders.getInstance().getAkeyLightDevice().value);
                            setButtonState(tem, mLightBtn);
                        }
                        if (GizwitHolders.getInstance().getAkeyWindowDevice() != null) {
                            tem = valueOf(GizwitHolders.getInstance().getAkeyWindowDevice().value);
                            setButtonState(tem, mCurtianBtn);
                        }
                        if (GizwitHolders.getInstance().getAkeySecDevice() != null) {
                            tem = valueOf(GizwitHolders.getInstance().getAkeySecDevice().value);
                            setButtonState(tem, mSecurityBtn);
                        }

                        if (GizwitHolders.getInstance().getAkeyHomeDevice() != null) {
                            tem = valueOf(GizwitHolders.getInstance().getAkeyHomeDevice().value);
                            int val = 0;
                            if (tem.equalsIgnoreCase("true")) {
                                val = 1;
                            } else if (tem.equalsIgnoreCase("false")) {
                                val = 0;
                            } else {
                                val = Integer.valueOf(tem);
                            }
                            ((CheckBox) findViewById(R.id.at_home_select)).setChecked(val == 1);
//                            ((CheckBox) findViewById(R.id.outside_select)).setChecked(val == 0);
                        }
                        if (GizwitHolders.getInstance().getAkeyLeaveDevice() != null) {
                            tem = valueOf(GizwitHolders.getInstance().getAkeyLeaveDevice().value);
                            int val = 0;
                            if (tem.equalsIgnoreCase("true")) {
                                val = 1;
                            } else if (tem.equalsIgnoreCase("false")) {
                                val = 0;
                            } else {
                                val = Integer.valueOf(tem);
                            }
//                            ((CheckBox) findViewById(R.id.at_home_select)).setChecked(val == 0);
                            ((CheckBox) findViewById(R.id.outside_select)).setChecked(val == 1);
                        }

                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }
        );
    }

    private void setButtonState(String tem, CheckBox box) {
        if (tem.equalsIgnoreCase("true")) {
            box.setChecked(true);
        } else if (tem.equalsIgnoreCase("false")) {
            box.setChecked(false);
        } else {
            box.setChecked(Integer.valueOf(tem) == 1);
        }
    }


    private void refreshValue(RoomInfo.DeviceInfo info, ConcurrentHashMap<String, Object> dataMap) {
        if (info != null) {

            if (dataMap.containsKey(info.tag)) {
                info.value = dataMap.get(info.tag);
                Log.i(TAG, "refreshValue: 设备名称 " + info.name + "  tag : " + info.tag + " value " + info.value + " id : " + info.category);
            }
        }
    }

    private View.OnClickListener mImageButtonClickListner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.room_prev_btn: { //点击 切换 room 左箭头
                    int position = mRoomEnvironmentViewPager.getCurrentItem();
                    if (position > 0)
                        mRoomEnvironmentViewPager.setCurrentItem(position - 1, true);
                    break;
                }
                case R.id.room_next_btn: { //点击 切换 room 右箭头
                    int position = mRoomEnvironmentViewPager.getCurrentItem();
                    if (position < (mRoomEnvironmentViewList.size() - 1))
                        mRoomEnvironmentViewPager.setCurrentItem(position + 1, true);
                    break;
                }
                case R.id.air_btn: { //---------------------一键空气净化策略
                    Log.i(TAG, "点击: air_btn");
                    //首先这是 ui 反馈 需要在主线程
                    final RoomInfo.DeviceInfo deviceInfo = GizwitHolders.getInstance().getAkeyAirDevice();
                    if (checkFounction(deviceInfo)) {
                        //判断当前值

                        final String valueStr = valueOf(deviceInfo.value);
                        if (Integer.valueOf(valueStr) == 1) {// 如果 当前状态是 开启的  执行关闭动作
                            for (RoomInfo roomInfo : mRoomInfo) {
                                for (RoomInfo.DeviceInfo info : roomInfo.airdevice) {
                                    CmdCenter.getInstance(mContext).cWrite(null, info.tag, 0);
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            mAirQualityBtn.setChecked(false);
                            mAirQualityBtn.setBackgroundResource(R.drawable.air_btn_off);
                            Toast.makeText(getContext(), "已关闭", Toast.LENGTH_SHORT).show();
                            deviceInfo.value = 0;

                        } else {
                            for (RoomInfo roomInfo : mRoomInfo) {
                                for (RoomInfo.DeviceInfo info : roomInfo.airdevice) {
                                    CmdCenter.getInstance(mContext).cWrite(null, info.tag, 1);
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            mAirQualityBtn.setChecked(true);
                            mAirQualityBtn.setBackgroundResource(R.drawable.air_btn_on);
                            Toast.makeText(getContext(), "已开启", Toast.LENGTH_SHORT).show();
                            deviceInfo.value = 1;

                        }
                        String did = GizwitHolders.getInstance().getDid();
                        GizwitOpenAPI.getInstance().sendCommand(did, deviceInfo.tag, valueOf(deviceInfo.value), new GizwitOpenAPI.RequestCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.i(TAG, "onSuccess: 一键空净 成功开启" + valueOf(o));
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: 一键空净 " + msg);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag + "_helper", 1);
                    }
                    break;
                }
                case R.id.curtain_btn: { //点击窗帘 整体开关按钮
                    Log.i(TAG, "点击: curtain_btn");
                    //首先这是 ui 反馈 需要在主线程
                    RoomInfo.DeviceInfo deviceInfo = GizwitHolders.getInstance().getAkeyWindowDevice();
                    if (checkFounction(deviceInfo)) {
                        String valueStr = valueOf(deviceInfo.value);
                        if (Integer.valueOf(valueStr) == 1) {// 如果 当前状态是 开启的  执行关闭动作
                            mCurtianBtn.setChecked(false);
                            mCurtianBtn.setBackgroundResource(R.drawable.curtain_btn_off);
                            deviceInfo.value = 0;
                        } else {
                            mCurtianBtn.setChecked(true);
                            mCurtianBtn.setBackgroundResource(R.drawable.curtain_btn_on);
                            deviceInfo.value = 1;
                        }
                        String did = GizwitHolders.getInstance().getDid();
                        GizwitOpenAPI.getInstance().sendCommand(did, deviceInfo.tag, valueOf(deviceInfo.value), new GizwitOpenAPI.RequestCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.i(TAG, "onSuccess: 一键窗帘 成功开启");
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: 一键窗帘 " + msg);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag + "_helper", 1);
                        break;
                    }
                }
                case R.id.light_btn: {
                    Log.i(TAG, "onClick: light close");
                    final RoomInfo.DeviceInfo device = GizwitHolders.getInstance().getAkeyLightDevice();
                    if (checkFounction(device)) {

                        String valueStr = valueOf(device.value);
                        if (Integer.valueOf(valueStr) == 1) {// 如果 当前状态是 开启的  执行关闭动作
                            mLightBtn.setChecked(false);
                            mLightBtn.setBackgroundResource(R.drawable.light_btn_off);
                            device.value = 0;
                        } else {
                            mLightBtn.setChecked(true);
                            mLightBtn.setBackgroundResource(R.drawable.light_btn_on);
                            device.value = 1;
                        }
                        String did = GizwitHolders.getInstance().getDid();


                        GizwitOpenAPI.getInstance().sendCommand(did, device.tag, valueOf(device.value), new GizwitOpenAPI.RequestCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.i(TAG, "onSuccess: 一键灯光 成功开启");
                                CmdCenter.getInstance(mContext).cWrite(null, device.tag + "_helper", 1);
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: 一键灯光 " + msg);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                }
                case R.id.temperature_btn: { //温度主要就是控制空调的开关
                    Log.i(TAG, "点击: temperature_btn");
                       /*策略操作*/
                    RoomInfo.DeviceInfo device = GizwitHolders.getInstance().getAkeyTemDevice();
                    if (checkFounction(device)) {

                        String valueStr = valueOf(device.value);
                        if (Integer.valueOf(valueStr) == 1) {// 如果 当前状态是 开启的  执行关闭动作
                            mTemperatureBtn.setChecked(false);
                            mTemperatureBtn.setBackgroundResource(R.drawable.temperature_btn_off);
                            device.value = 0;
                        } else {
                            mTemperatureBtn.setChecked(true);
                            mTemperatureBtn.setBackgroundResource(R.drawable.temperature_btn_on);
                            device.value = 1;
                        }
                        String did = GizwitHolders.getInstance().getDid();
                        GizwitOpenAPI.getInstance().sendCommand(did, device.tag, valueOf(device.value), new GizwitOpenAPI.RequestCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.i(TAG, "onSuccess: 一键温度 成功开启");
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: 一键温度 " + msg);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CmdCenter.getInstance(mContext).cWrite(null, device.tag + "_helper", 1);

                    }
                    break;
                }
                case R.id.humidity_btn: {
                    Log.i(TAG, "点击: humidity_btn");
                    RoomInfo.DeviceInfo device = GizwitHolders.getInstance().getAkeyHumDevice();
                    if (checkFounction(device)) {
                        String valueStr = valueOf(device.value);
                        if (Integer.valueOf(valueStr) == 1) {// 如果 当前状态是 开启的  执行关闭动作
                            mHumidityBtn.setChecked(false);
                            mHumidityBtn.setBackgroundResource(R.drawable.humidity_btn_off);
                            device.value = 0;
                        } else {
                            mHumidityBtn.setChecked(true);
                            mHumidityBtn.setBackgroundResource(R.drawable.humidity_btn_on);
                            device.value = 1;
                        }
                        String did = GizwitHolders.getInstance().getDid();
                        GizwitOpenAPI.getInstance().sendCommand(did, device.tag, valueOf(device.value), new GizwitOpenAPI.RequestCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.i(TAG, "onSuccess: 一键湿度 成功开启");
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: 一键湿度 " + msg);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CmdCenter.getInstance(mContext).cWrite(null, device.tag + "_helper", 1);
                    }
                    break;
                }
                case R.id.security_btn: {
                    Log.i(TAG, "点击: security_btn");
                    RoomInfo.DeviceInfo device = GizwitHolders.getInstance().getAkeySecDevice();
                    if (checkFounction(device)) {
                        String valueStr = valueOf(device.value);
                        if (Integer.valueOf(valueStr) == 1) {// 如果 当前状态是 开启的  执行关闭动作
                            mSecurityBtn.setChecked(false);
                            mSecurityBtn.setBackgroundResource(R.drawable.security_btn_off);
                            device.value = 0;
                        } else {
                            mSecurityBtn.setChecked(true);
                            mSecurityBtn.setBackgroundResource(R.drawable.security_btn_on);
                            device.value = 1;
                        }
                        String did = GizwitHolders.getInstance().getDid();
                        GizwitOpenAPI.getInstance().sendCommand(did, device.tag, valueOf(device.value), new GizwitOpenAPI.RequestCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.i(TAG, "onSuccess: 布防 成功开启");
                            }

                            @Override
                            public void onFailure(String msg) {
                                Log.i(TAG, "onFailure: 布防  " + msg);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CmdCenter.getInstance(mContext).cWrite(null, device.tag + "_helper", 1);
                    }
                    break;
                }
                case R.id.at_home_btn: {
                    //TODO:发送回家指令
                    /*ui*/
                    ((CheckBox) findViewById(R.id.at_home_select)).setChecked(true);
                    ((CheckBox) findViewById(R.id.outside_select)).setChecked(false);


                    RoomInfo.DeviceInfo leavedevice = GizwitHolders.getInstance().getAkeyLeaveDevice();
                    RoomInfo.DeviceInfo homedevice = GizwitHolders.getInstance().getAkeyHomeDevice();

                    if (checkFounction(leavedevice) && checkFounction(homedevice)) {
                        ((CheckBox) findViewById(R.id.at_home_select)).setChecked(true);
                        homedevice.value = 1;
                        leavedevice.value = 0;
                        //发送 回家离家 节点
                        CmdCenter.getInstance(mContext).cWrite(null, homedevice.tag, homedevice.value);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CmdCenter.getInstance(mContext).cWrite(null, leavedevice.tag, leavedevice.value);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //发送回家辅助节点
                        CmdCenter.getInstance(mContext).cWrite(null, homedevice.tag + "_helper", 1);
                    }
                    break;
                }
                case R.id.outside_btn: {
                    //TODO:发送离家指令
                    /*UI 选择红点的设置*/
                    ((CheckBox) findViewById(R.id.at_home_select)).setChecked(false);
                    ((CheckBox) findViewById(R.id.outside_select)).setChecked(true);

                    /*内存模型的数值设置*/
                    RoomInfo.DeviceInfo leavedevice = GizwitHolders.getInstance().getAkeyLeaveDevice();
                    RoomInfo.DeviceInfo homedevice = GizwitHolders.getInstance().getAkeyHomeDevice();

                    if (checkFounction(leavedevice) && checkFounction(homedevice)) {
                        ((CheckBox) findViewById(R.id.outside_select)).setChecked(true);
                        leavedevice.value = 1;
                        homedevice.value = 0;
                        //发送 回家离家的策略命令
                        CmdCenter.getInstance(mContext).cWrite(null, leavedevice.tag, leavedevice.value);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CmdCenter.getInstance(mContext).cWrite(null, homedevice.tag, homedevice.value);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //发送离家辅助节点
                        CmdCenter.getInstance(mContext).cWrite(null, leavedevice.tag + "_helper", 1);
                    }
                    break;
                }
                case R.id.weather_prev_btn: {
                    Intent mIntent = new Intent(mContext, OutsideWeatherActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    mContext.startActivity(mIntent);
                    break;
                }
                case R.id.room_control_next_btn: {
                    Intent mIntent = new Intent(mContext, RoomControlActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                    mIntent.putExtra("RoomInfo", (Serializable) mRoomInfo);
                    mContext.startActivity(mIntent);
                    break;
                }
            }
        }
    };

    private boolean checkFounction(RoomInfo.DeviceInfo device) {
        if (device == null || device.tag == null) {
            Toast.makeText(mContext, "该功能未开通!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Gson gson;

    /**
     * 关闭所有窗帘  采用单台控制方式
     */
    private void closeAllCurtain(RoomInfo item) {
        if (item.akeywindowcurtaindevice.size() > 0) {//检查 策略节点是否存在
            for (int i = 0; i < item.akeywindowcurtaindevice.size(); i++) {
                CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                        item.akeywindowcurtaindevice.get(i).tag, CurtainItemAdapter.CURTAIN_CLOSE);//0 CurtainItemAdapter.CURTAIN_CLOSE
                Log.i(TAG, "onClick: 一键窗帘控制关" + item.akeywindowcurtaindevice.get(i).tag);
                try {
                    Thread.sleep(80);
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            item.akeywindowcurtaindevice.get(i).tag + "_helper", 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        } else {
            if (item.curtain_device.size() > 0) {
                for (int i = 0; i < item.curtain_device.size(); i++) {
                    String descrip = item.curtain_device.get(i).descrip;
                    JsonObject object = gson.fromJson(descrip, JsonObject.class);
                    String exidOpen = valueOf(object.get("open"));
                    exidOpen = exidOpen.replaceAll("\"", "");
                    String exidClose = valueOf(object.get("close"));
                    exidClose = exidClose.replaceAll("\"", "");
            /*停止开的动作*/
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            exidOpen, 0);
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void openAllLight(RoomInfo item) {
        if (item.akeyligntingdevice.size() > 0) {
            for (int i = 0; i < item.akeyligntingdevice.size(); i++) {
                //开灯
                CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                        item.akeyligntingdevice.get(i).tag, 1);
                try {
                    Thread.sleep(80);
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            item.akeyligntingdevice.get(i).tag + "_helper", 1);
                    Log.i(TAG, "onClick: " + item.akeyligntingdevice.get(i).tag);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (item.lighting_device.size() > 0) {
                for (int i = 0; i < item.lighting_device.size(); i++) {
                    //开灯
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            item.lighting_device.get(i).tag, true);
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (item.adjust_lighting_device.size() > 0) {
                for (int i = 0; i < item.adjust_lighting_device.size(); i++) {
                    //开灯
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            item.adjust_lighting_device.get(i).tag, AdjustLampItemAdapter.ADJUST_LAMP_ON);
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void openAllCurtain(RoomInfo item) {
        if (item.akeywindowcurtaindevice.size() > 0) {//策略模式
            for (int i = 0; i < item.akeywindowcurtaindevice.size(); i++) {
                CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                        item.akeywindowcurtaindevice.get(i).tag, CurtainItemAdapter.CURTAIN_OPEN);//1  CurtainItemAdapter.CURTAIN_OPEN
                Log.i(TAG, "onClick: 一键窗帘控制开 " + item.akeywindowcurtaindevice.get(i).tag);
                try {
                    Thread.sleep(80);
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            item.akeywindowcurtaindevice.get(i).tag + "_helper", 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {//旧版本操作模式
            if (item.curtain_device.size() > 0) {
                for (int i = 0; i < item.curtain_device.size(); i++) {
                    String descrip = item.curtain_device.get(i).descrip;
                    JsonObject object = gson.fromJson(descrip, JsonObject.class);
                    String exidOpen = valueOf(object.get("open"));
                    exidOpen = exidOpen.replaceAll("\"", "");
                    String exidClose = valueOf(object.get("close"));
                    exidClose = exidClose.replaceAll("\"", "");
            /*停止开的动作*/
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            exidOpen, 1);
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*三个 策略同时开启的时候  关闭空气净化策略 执行多次 pm2.5操作*/
    private void setAllAdajustLight(RoomInfo item) {
    }

    private void setAlllight(RoomInfo item) {
        if (item.akeyligntingdevice.size() > 0) {//策略模式
            for (int i = 0; i < item.akeyligntingdevice.size(); i++) {
                //关灯
                CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                        item.akeyligntingdevice.get(i).tag, 0);
                try {
                    Thread.sleep(80);
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            item.akeyligntingdevice.get(i).tag + "_helper", 1);
                    Log.i(TAG, "onClick: " + item.akeyligntingdevice.get(i).tag);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (item.lighting_device.size() > 0) {
                for (int i = 0; i < item.lighting_device.size(); i++) {
                    //关灯
                    CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                            item.lighting_device.get(i).tag, false);
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private AdapterView.OnItemClickListener mGridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mRoomStatusGridAdapter.setCheckedBackground(position, true);
            mRoomStatusGridAdapter.notifyDataSetChanged();
            mRoomEnvironmentViewPager.setCurrentItem(position, true);
            ((RoomEnvironmentStatusView) mRoomEnvironmentStatusViewAdapter.getItem(position)).setUpdateData(mRoomInfo, position);
        }
    };

    public class RoomEnvironmentStatusViewAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mRoomEnvironmentViewList.size();
        }

        public Object getItem(int position) {
            return mRoomEnvironmentViewList.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).removeView(mRoomEnvironmentViewList.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).addView(mRoomEnvironmentViewList.get(arg1));
            return mRoomEnvironmentViewList.get(arg1);
        }
    }


    public int getValue(Object value) {
        if (value == null) {
            return 0;
        }
        String valueStr = String.valueOf(value);
        if (valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("false")) {//如果是 boolean 类型的返回值 转换为 0 1
            if (valueStr.equalsIgnoreCase("true")) {
                return 1;
            } else {
                return 0;
            }
        } else {//如果不是 boolean 应该是 int 类型了
            int result = 0;
            try {
                result = Integer.parseInt(valueStr);
            } catch (NumberFormatException e) {
                Log.i(TAG, "checkValueFormate: 数据点 数据类型无法解析  " + valueStr);
                result=0;
            }
            return result;
        }
    }


}
