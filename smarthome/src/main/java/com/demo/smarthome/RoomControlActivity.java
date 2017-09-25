package com.demo.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.tools.GizwitHolders;
import com.demo.smarthome.ui.RoomControlAirView;
import com.demo.smarthome.ui.RoomControlFanView;
import com.demo.smarthome.ui.RoomControlLampView;
import com.demo.smarthome.ui.RoomControlTemperatureView;
import com.demo.smarthome.ui.RoomControlWaterView;
import com.demo.smarthome.ui.RoomControlWindowView;
import com.demo.smarthome.ui.RoomYingshiVIew;
import com.demo.smarthome.ui.adapter.RoomNameHorizontalTabAdapter;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;
import com.ezvizuikit.open.EZUIPlayer;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import aprivate.oo.gizwitopenapi.GizwitOpenAPI;
import aprivate.oo.gizwitopenapi.response.DeviceData;

public class RoomControlActivity extends BaseSmartHomeActivity {
    MyActivityManager mam = MyActivityManager.getInstance();
    private int tag = 0;

    private String TAG = "RoomControlActivity";
    private Context mContext;
    private List<RoomInfo> mRoomInfo;

    private GridView mRoomSelectGrid;
    private LinearLayout mRoomControlFrame;
    private ScrollView mScrollView;
    private RoomNameHorizontalTabAdapter mRoomNameHorizontalTabAdapter;

    private final int CAMERA_VIEW = 0;
    private final int TEMPERATURE_VIEW = 1;
    private final int AIR_VIEW = 2;
    private final int LAMP_VIEW = 3;
    private final int WINDOW_VIEW = 4;
    private final int WATER_VIEW = 5;
    private final int FAN_VIEW = 6;

    private ImageButton mCameraBtn;
    private ImageButton mTemperatureBtn;
    private ImageButton mAirBtn;
    private ImageButton mLampBtn;
    private ImageButton mWindowBtn;
    private ImageButton mWaterBtn;
    private ImageButton mFanBtn;

    private EZUIPlayer player_ui;
    private ImageView player_bg;

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_room_control);
        mam.pushOneActivity(RoomControlActivity.this);
        mRoomInfo=GizwitHolders.getInstance().roomInfoList;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRoomControlFrame = (LinearLayout) findViewById(R.id.room_control_frame);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mCameraBtn = (ImageButton) findViewById(R.id.camera_table_icon);
        mTemperatureBtn = (ImageButton) findViewById(R.id.temp_table_icon);
        mAirBtn = (ImageButton) findViewById(R.id.air_table_icon);
        mLampBtn = (ImageButton) findViewById(R.id.light_table_icon);
        mWindowBtn = (ImageButton) findViewById(R.id.window_table_icon);
        mWaterBtn = (ImageButton) findViewById(R.id.water_table_icon);

        mCameraBtn.setOnClickListener(mOnClickListener);
        mTemperatureBtn.setOnClickListener(mOnClickListener);
        mAirBtn.setOnClickListener(mOnClickListener);
        mLampBtn.setOnClickListener(mOnClickListener);
        mWindowBtn.setOnClickListener(mOnClickListener);
        mWaterBtn.setOnClickListener(mOnClickListener);


        List<String> roomnames = new ArrayList<>();
        List<Integer> roomIndex = new ArrayList<>();
        for (int i = 0; i < mRoomInfo.size(); i++) {
            if (mRoomInfo.get(i).camera_device.size() > 0 || mRoomInfo.get(i).temperature_o_device.size() > 0
                    || mRoomInfo.get(i).humidity_o_device.size() > 0 || mRoomInfo.get(i).temperature_i_device.size() > 0
                    || mRoomInfo.get(i).humidity_i_device.size() > 0 || mRoomInfo.get(i).pm25_i_device.size() > 0 || mRoomInfo.get(i).pm25_o_device.size() > 0
                    || mRoomInfo.get(i).co2_i_device.size() > 0 || mRoomInfo.get(i).co2_o_device.size() > 0
                    || mRoomInfo.get(i).hcho_o_device.size() > 0 || mRoomInfo.get(i).voc_o_device.size() > 0
                    || mRoomInfo.get(i).lighting_device.size() > 0 || mRoomInfo.get(i).adjust_lighting_device.size() > 0
                    || mRoomInfo.get(i).window_device.size() > 0 || mRoomInfo.get(i).curtain_device.size() > 0
                    || mRoomInfo.get(i).water_control_device.size() > 0 || mRoomInfo.get(i).gas_control_device.size() > 0
                    || mRoomInfo.get(i).energy_control_device.size() > 0 || mRoomInfo.get(i).fan_device.size() > 0) {
                roomnames.add(mRoomInfo.get(i).roomRemark.length() > 0 ? mRoomInfo.get(i).roomRemark : mRoomInfo.get(i).roomName);
                roomIndex.add(i);
            }

        }

        mRoomSelectGrid = (GridView) findViewById(R.id.room_select_grid);
        mRoomNameHorizontalTabAdapter = new RoomNameHorizontalTabAdapter(mContext, roomnames, roomIndex);
        mRoomSelectGrid.setAdapter(mRoomNameHorizontalTabAdapter);
        int size = mRoomInfo.size();
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources().getDisplayMetrics();
        float density = dm.density;
        int allWidth = (int) (80 * size * density);
        int itemWidth = (int) (70 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.FILL_PARENT);
        mRoomSelectGrid.setLayoutParams(params);
        mRoomSelectGrid.setColumnWidth(itemWidth);
        mRoomSelectGrid.setHorizontalSpacing(10);
        mRoomSelectGrid.setStretchMode(GridView.NO_STRETCH);
        mRoomSelectGrid.setNumColumns(size);
        mRoomSelectGrid.setOnItemClickListener(mGridItemClickListener);
        mRoomNameHorizontalTabAdapter.setCheckedItem(0);

        initView(GizwitHolders.getInstance().roomInfoList.get(0));
//        initView(mRoomInfo.get(roomIndex.get(0)));

        //TODO:刷新各个View数据
        Log.d(TAG, "set XpgWifiDevice Listener in OnCreate");
    }

    private void getData() {
        final String did = GizwitHolders.getInstance().getDid();
        GizwitOpenAPI.getInstance().getDeviceData(did, new GizwitOpenAPI.RequestCallback<DeviceData>() {
            @Override
            public void onSuccess(DeviceData deviceData) {
                swipeRefreshLayout.setRefreshing(false);
                Log.i(TAG, "onSuccess:  did = "+deviceData.getDid());
                ConcurrentHashMap<String, Object> stateMap = new ConcurrentHashMap<String, Object>();
                for (Map.Entry<String, JsonElement> entry : deviceData.getAttr().entrySet()) {
                    stateMap.put(entry.getKey(), entry.getValue());
                    Log.i(TAG, "onSuccess: key - "+entry.getKey()+ " value - "+entry.getValue());
                }
                for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
                    ((RoomControlBaseView) mRoomControlFrame.getChildAt(i)).setReceiveData(stateMap);
                }
            }

            @Override
            public void onFailure(String msg) {
                Log.i(TAG, "onFailure: " + msg);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initView(RoomInfo roomInfo) {
        for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
            RoomControlBaseView view = (RoomControlBaseView) mRoomControlFrame.getChildAt(i);
            view.onDestroy();
        }
        mRoomControlFrame.removeAllViews();
        //Camera
//        if (roomInfo.camera_device.size()>0 && roomInfo.camera_device.get(0).value != null
//                && String.valueOf(roomInfo.camera_device.get(0).value).length()>0) {
        if (roomInfo.camera_device != null && roomInfo.camera_device.size() > 0) {
//            RoomControlCameraView cameraView = new RoomControlCameraView(mContext, roomInfo);
//            cameraView.setTag(CAMERA_VIEW);

            for (RoomInfo.DeviceInfo deviceInfo : roomInfo.camera_device) {
                RoomYingshiVIew roomYingshiVIew = new RoomYingshiVIew(mContext, roomInfo, deviceInfo.feature);
                roomYingshiVIew.setTag(CAMERA_VIEW);

                findViewById(R.id.camera_table_btn).setVisibility(View.VISIBLE);

                mRoomControlFrame.addView(roomYingshiVIew);
            }


        } else {
            findViewById(R.id.camera_table_btn).setVisibility(View.GONE);
        }
        //Temperature & humidity---温湿度
        if (roomInfo.temperature_o_device.size() > 0 || roomInfo.humidity_o_device.size() > 0 || roomInfo.temperature_i_device.size() > 0
                || roomInfo.humidity_i_device.size() > 0) {
            RoomControlTemperatureView temperatureView = new RoomControlTemperatureView(mContext, roomInfo);
            temperatureView.setTag(TEMPERATURE_VIEW);
            mRoomControlFrame.addView(temperatureView);
            findViewById(R.id.temp_table_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.temp_table_btn).setVisibility(View.GONE);
        }
        //Airquality----空气净化
//        if (roomInfo.roomAirdevice.size()>0){
//            RoomControlAirView airView = new RoomControlAirView(mContext,roomInfo);
//            airView.setTag(AIR_VIEW);
//            mRoomControlFrame.addView(airView);
//            findViewById(R.id.air_table_btn).setVisibility(View.VISIBLE);
//        }else {
//            findViewById(R.id.air_table_btn).setVisibility(View.GONE);
//        }
        if (roomInfo.pm25_i_device.size() > 0 || roomInfo.pm25_o_device.size() > 0
                || roomInfo.co2_i_device.size() > 0 || roomInfo.co2_o_device.size() > 0
                || roomInfo.hcho_o_device.size() > 0 || roomInfo.voc_o_device.size() > 0) {
            RoomControlAirView airView = new RoomControlAirView(mContext, roomInfo);
            airView.setTag(AIR_VIEW);
            mRoomControlFrame.addView(airView);
            findViewById(R.id.air_table_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.air_table_btn).setVisibility(View.GONE);
        }
        //Light
        if (roomInfo.lighting_device.size() > 0 || roomInfo.adjust_lighting_device.size() > 0) {
            RoomControlLampView lampView = new RoomControlLampView(mContext, roomInfo);
            lampView.setTag(LAMP_VIEW);
            mRoomControlFrame.addView(lampView);
            findViewById(R.id.light_table_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.light_table_btn).setVisibility(View.GONE);
        }
        //Window
        if (roomInfo.window_device.size() > 0 || roomInfo.curtain_device.size() > 0) {
            RoomControlWindowView windowView = new RoomControlWindowView(mContext, roomInfo);
            windowView.setTag(WINDOW_VIEW);
            mRoomControlFrame.addView(windowView);
            findViewById(R.id.window_table_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.window_table_btn).setVisibility(View.GONE);
        }
        //Water
        if (roomInfo.water_control_device.size()>0 ||  roomInfo.gas_control_device.size()>0
                || roomInfo.energy_control_device.size()>0){
            RoomControlWaterView waterView = new RoomControlWaterView(mContext,roomInfo);
            waterView.setTag(WATER_VIEW);
            mRoomControlFrame.addView(waterView);
            findViewById(R.id.water_table_btn).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.water_table_btn).setVisibility(View.GONE);
        }
//        if (roomInfo.water_device.size() > 0) {
//            RoomControlWaterViewNew waterView = new RoomControlWaterViewNew(mContext, roomInfo);
//            waterView.setTag(WATER_VIEW);
//            mRoomControlFrame.addView(waterView);
//            findViewById(R.id.water_table_btn).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.water_table_btn).setVisibility(View.GONE);
//        }


//        Fan
        if (roomInfo.fan_device.size()>0 ){
            RoomControlFanView fanView = new RoomControlFanView(mContext,roomInfo);
            fanView.setTag(FAN_VIEW);
            mRoomControlFrame.addView(fanView);
        }
        swipeRefreshLayout.setRefreshing(true);
        getData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(RoomControlActivity.this);
        RoomYingshiVIew.setDestory();
    }

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
        for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
            RoomControlBaseView view = (RoomControlBaseView) mRoomControlFrame.getChildAt(i);
            view.onDestroy();
        }
    }

    private AdapterView.OnItemClickListener mGridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mRoomNameHorizontalTabAdapter.setCheckedItem(position);
            tag = position;
            initView(mRoomInfo.get((int) mRoomNameHorizontalTabAdapter.getItemId(position)));
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.camera_table_icon: {
                    for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
                        View view = mRoomControlFrame.getChildAt(i);
                        if ((int) view.getTag() == CAMERA_VIEW)
                            mScrollView.scrollTo(0, (int) view.getY());
                    }
                    break;
                }
                case R.id.temp_table_icon: {
                    for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
                        View view = mRoomControlFrame.getChildAt(i);
                        if ((int) view.getTag() == TEMPERATURE_VIEW)
                            mScrollView.scrollTo(0, (int) view.getY());
                    }
                    break;
                }
                case R.id.air_table_icon: {
                    for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
                        View view = mRoomControlFrame.getChildAt(i);
                        if ((int) view.getTag() == AIR_VIEW)
                            mScrollView.scrollTo(0, (int) view.getY());
                    }
                    break;
                }
                case R.id.light_table_icon: {
                    for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
                        View view = mRoomControlFrame.getChildAt(i);
                        if ((int) view.getTag() == LAMP_VIEW)
                            mScrollView.scrollTo(0, (int) view.getY());
                    }
                    break;
                }
                case R.id.window_table_icon: {
                    for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
                        View view = mRoomControlFrame.getChildAt(i);
                        if ((int) view.getTag() == WINDOW_VIEW)
                            mScrollView.scrollTo(0, (int) view.getY());
                    }
                    break;
                }
                case R.id.water_table_icon: {
                    for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
                        View view = mRoomControlFrame.getChildAt(i);
                        if ((int) view.getTag() == WATER_VIEW)
                            mScrollView.scrollTo(0, (int) view.getY());
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
        Log.d(TAG, "fall in didReceiveData in RoomControlActivity and finishing state is " + isFinishing());
        if (isFinishing()) return;
        super.didReceiveData(result, device, dataMap, sn);
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS || result == GizWifiErrorCode.GIZ_SDK_RAW_DATA_TRANSMIT) {
            for (int i = 0; i < mRoomControlFrame.getChildCount(); i++) {
                ((RoomControlBaseView) mRoomControlFrame.getChildAt(i)).setReceiveData(dataMap);
            }
        } else {
            Log.d(TAG, "Data fresh failed");
        }
    }

    private void goLogin() {
        Intent intent = new Intent();
        intent.setClass(RoomControlActivity.this, LoginActivity.class);
        startActivity(intent);
        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.finish();
        }
        this.finish();
    }
}