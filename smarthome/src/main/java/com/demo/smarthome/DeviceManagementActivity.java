package com.demo.smarthome;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.communication.jsonbean.sub.Rooms;
import com.demo.smarthome.tools.GizwitHolders;
import com.demo.smarthome.ui.OneRoomDeviceView;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/2/22.
 */
public class DeviceManagementActivity extends BaseSmartHomeActivity {

    private String TAG = "DeviceManagement";
    MyActivityManager mam = MyActivityManager.getInstance();
    private Context mContext;
    private LinearLayout mDeviceListFrame;
    private ArrayList<OneRoomDeviceView> mOneRoomDeviceViews;
    private UserProfileBean userProfileBean;
    private Rooms roomInfo;
    private Houses houseData;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    for (int i = 0; i < userProfileBean.getHouses().size(); i++) {
                        if (userProfileBean.getHouses().get(i).getId() == PreferenceUtil.getInt("default_house")) {
                            mOneRoomDeviceViews = new ArrayList<>();
                            houseData = userProfileBean.getHouses().get(i);
                            for (int j = 0; j < houseData.getRooms().size(); j++) {
                                roomInfo = houseData.getRooms().get(j);
                                OneRoomDeviceView v = new OneRoomDeviceView(mContext, roomInfo);
                                mOneRoomDeviceViews.add(v);
                                mDeviceListFrame.addView(v);

                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_management_activity);
        mam.pushOneActivity(DeviceManagementActivity.this);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.device_management_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDeviceListFrame = (LinearLayout) this.findViewById(R.id.device_list_frame);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO:获取设备信息
//                userProfileBean = new UserProfileBean();
//                userProfileBean = GsonTools.getBean(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                userProfileBean= GizwitHolders.getInstance().getUserProfileBean();
                Message message = new Message();
                message.what = 10;
                handler.sendMessage(message);

            }
        }).start();
        //TODO:刷新各个View数据
//        Log.d(TAG, "set XpgWifiDevice Listener in OnCreate");
//        mXpgWifiDevice.setListener(deviceListener);
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
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, final ConcurrentHashMap<String, Object> dataMap, int sn) {
        Log.d(TAG, "fall in didReceiveData in SmartLightingActivity and finishing state is " + isFinishing());
        if (isFinishing()) return;
        super.didReceiveData(result, device, dataMap, sn);
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS || result == GizWifiErrorCode.GIZ_SDK_RAW_DATA_TRANSMIT) {
            if(mOneRoomDeviceViews!=null){
                for (int i = 0; i < mOneRoomDeviceViews.size(); i++) {
                    mOneRoomDeviceViews.get(i).setUpdateData(dataMap);
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(DeviceManagementActivity.this);
    }
}
