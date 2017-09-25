package com.demo.smarthome;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.EventBus_Account;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.NetServerCommunicationImpl;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.qrcode.CaptureActivity;
import com.demo.smarthome.tools.GizwitHolders;
import com.demo.smarthome.ui.adapter.HouseManagementAdapter;
import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.ui.model.RoomInfo;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import aprivate.oo.gizwitopenapi.response.BindingList;
import checkpermissions.PermissionsActivity;
import checkpermissions.PermissionsChecker;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by liukun on 2016/3/12.
 */
public class HouseManagementActivity extends BaseSmartHomeActivity implements View.OnClickListener {
    private static final int CAMERA_OK = 1;
    MyActivityManager mam = MyActivityManager.getInstance();
    private String TAG = "HouseManagement";
    private Context mContext;
    private Toolbar mToolbar;
    private NotScrollListView mHouseList;
    private HouseManagementAdapter mHouseAdapter;
    private RelativeLayout mScanBtn;

    private TextView mContentTextView;
    private UserProfileBean userProfileBean;
    public static HouseManagementActivity houseManagementActivity;
    private PermissionsChecker mPermissionsChecker;

    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kProgressHUD = AppUtils.getLoadingView(this);

        setContentView(R.layout.house_management_activity);
        mam.pushOneActivity(HouseManagementActivity.this);
        EventBus.getDefault().register(this);
        houseManagementActivity = this;
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.house_management_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContentTextView = (TextView) findViewById(R.id.no_content_alert_text);
        mHouseList = (NotScrollListView) findViewById(R.id.house_list);

        userProfileBean = new UserProfileBean();
//        userProfileBean = GsonTools.getBean(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
        userProfileBean = GizwitHolders.getInstance().getUserProfileBean();
        if (userProfileBean != null) {
            if (userProfileBean.getHouses().size() > 0) {
                mHouseAdapter = new HouseManagementAdapter(mContext, userProfileBean.getHouses());
                /*切换房屋的监听*/
                mHouseAdapter.setSwithcHouseCallback(new HouseManagementAdapter.SwithcHouseCallback() {
                    @Override
                    public void onSwitchHouse(int houseId) {
                        //点击切换房屋完成
                        kProgressHUD.show();
                        Log.i(TAG, "onSwitchHouse: ");
                        UserProfileBean userProfileBean = GizwitHolders.getInstance().getUserProfileBean();
                        if (userProfileBean == null) {
                            userProfileBean = GsonTools.getBean(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                        }
                        ArrayList<Houses> houseList = (ArrayList<Houses>) GizwitHolders.getInstance().getUserProfileBean().getHouses();
                        String productKey = null;
                        /*获取 目标房屋网关的 productkey*/
                        for (Houses house : houseList) {
                            if (houseId == house.getId()) {
                                productKey = house.getGateways().get(0).getExtid();
                                break;
                            }
                        }

                        /*获取机智云设备中符合 productkey 的设备 */
                        for (BindingList.DevicesBean devicesBean : GizwitHolders.getInstance().getBindingList().getDevices()) {
                            if (devicesBean.getProduct_key().equals(productKey)) {
                                GizwitHolders.getInstance().setProductKey(productKey);
                                /*roominfo 配置的过程比较耗时 */
                                GizwitHolders.getInstance().setListInfo(mRoomInfo, mAlertDevice, mAlertRecord, userProfileBean, new GizwitHolders.RoomInfoInitCallback() {
                                    @Override
                                    public void onInited(List<RoomInfo> roomInfos) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (kProgressHUD.isShowing()) {
                                                    kProgressHUD.dismiss();
                                                }
                                            }
                                        });

                                        Intent intent = getIntent();
//                                intent.putExtra("RoomInfo", (Serializable) mRoomInfo);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                                return;
                            }
                        }
                    }
                });
                mHouseList.setAdapter(mHouseAdapter);
            } else {
                mContentTextView.setVisibility(View.VISIBLE);
            }
        } else {
            mContentTextView.setVisibility(View.VISIBLE);
        }


        mScanBtn = (RelativeLayout) findViewById(R.id.add_house_btn);
        mScanBtn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        if (kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
        super.onDestroy();
        mam.popOneActivity(HouseManagementActivity.this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    public void saveHouseInfo() {
        //NetServerCommunicationImpl.getInstance(mContext).updateHouseInfo(mHandler,PreferenceUtil.getString(Constant.RouterManager.ROUTERINFO),item);
    }

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_house_btn: {
                //先判断相机是否打开状态
                Log.i(TAG, "onClick: ");
                //检查 当前权限
                if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(HouseManagementActivity.this, Manifest.permission.CAMERA)) {
                    toCaptureActivity();
                } else {//请求授权
                    String[] permissions = {Manifest.permission.CAMERA};
                    ActivityCompat.requestPermissions(HouseManagementActivity.this, permissions, 0x01);
                }


//                mPermissionsChecker = new PermissionsChecker(HouseManagementActivity.this);//权限申请
//                if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
//                    startPermissionsActivity();
//                }
//                Intent intent = new Intent(mContext, CaptureActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                startActivity(intent);
                break;
            }
        }
    }

    private void toCaptureActivity() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        startActivityForResult(intent,REQUESTCODE);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(HouseManagementActivity.this, REQUEST_CODE, PERMISSIONS);
    }

    private static final int REQUEST_CODE = 0; // 请求码
    private  static final int REQUESTCODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0x01 && grantResults.length > 0) {
            switch (grantResults[0]) {
                case PackageManager.PERMISSION_GRANTED:
                    toCaptureActivity();
                    break;
                case PackageManager.PERMISSION_DENIED:

                    break;

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }else if (resultCode == 2) {
            if (requestCode == 1) {
                Log.e(TAG,"这里跑了吗？");
//              mHouseAdapter.notifyDataSetChanged();
                if(mHouseAdapter == null) {
//                    mHouseAdapter = new HouseManagementAdapter(mContext, userProfileBean.getHouses());
//                    mHouseList.setAdapter(mHouseAdapter);
                    setResult(3,getIntent());
                    finish();
                }else {
                    mHouseAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetServerCommunicationImpl.MSG_UPDATE_HOUSE_INFO_CALLBACK: {
                    Bundle data = msg.getData();
                    String result = data.getString("result");
                    break;
                }
            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getEventBus(EventBus_Account message) {
        int tag = message.tag;
        switch (tag) {
            case Constant.EVENT_BUS_MODIFY_PERSONAL_HOUSE:
                userProfileBean = GsonTools.getBean(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                if (userProfileBean != null) {
                    if (userProfileBean.getHouses().size() > 0) {
                        mHouseAdapter = new HouseManagementAdapter(mContext, userProfileBean.getHouses());
                        mHouseList.setAdapter(mHouseAdapter);
                    } else {
                        mContentTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    mContentTextView.setVisibility(View.VISIBLE);
                }
        }
    }

    private ArrayList<RoomInfo> mRoomInfo;




//    private void initRoomInfo() {
//        mRoomInfo = new ArrayList<>();
//        mAlertDevice = new ArrayList<>();
//        mAlertRecord = new HashMap<>();
//        //TODO:获取房间信息
//        for (int i = 0; i < userProfileBean.getHouses().size(); i++) {
//            if (userProfileBean.getHouses().get(i).getId() == PreferenceUtil.getInt("default_house")) {
//                for (int j = 0; j < userProfileBean.getHouses().get(i).getRooms().size(); j++) {
//                    RoomInfo temp = new RoomInfo();
//                    Rooms room = userProfileBean.getHouses().get(i).getRooms().get(j);
//                    temp.roomName = room.getName();
//                    Log.i(TAG, "initRoomInfo:  get categories  " + room.getName());
//                    for (int k = 0; k < room.getCategories().size(); k++) {
//                        //temperature
//                        Log.i(TAG, "initRoomInfo: " + room.getCategories().get(k).getName());
//                        if (room.getCategories().get(k).getName().equals("temperature")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  temperature");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.temperature_o_device.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo: get temperature : " + temp.temperature_o_device.size());
//                        }
//                        //humidity
//                        if (room.getCategories().get(k).getName().equals("humidity")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  humidity");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.humidity_o_device.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo: humidity " + temp.humidity_o_device.size());
//                        }
//                        //pm25
//                        if (room.getCategories().get(k).getName().equals("pm25")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  pm25");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.pm25_o_device.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo:  pm25 " + temp.pm25_o_device.size());
//                        }
//
//
//                        //co2
//                        if (room.getCategories().get(k).getName().equals("co2")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  co2");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.co2_o_device.add(device);
//                            }
//                        }
//                        //hcho
//                        if (room.getCategories().get(k).getName().equals("HCHO")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  hcho");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.hcho_o_device.add(device);
//                            }
//                        }
//                        //voc
//                        if (room.getCategories().get(k).getName().equals("voc")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  voc");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.voc_o_device.add(device);
//                            }
//                        }
//                        //-------------------------------------新增加的-------------------------------------------------------------
//                        //一键空气净化
//                        if (room.getCategories().get(k).getId() == 1500) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1500");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                Log.i(TAG, "initRoomInfo: name-->" + device.name);
//                                temp.akeyairdevice.add(device);
//                            }
//                        }
//
//                        //airdevice
//                        if (room.getCategories().get(k).getId() == 1470) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1470");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                Log.i(TAG, "initRoomInfo: name-->" + device.name);
//                                temp.airdevice.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo: categoriese  airdevice " + temp.airdevice.size());
//                        }
//
//                        //akeyligntingdevice
//                        if (room.getCategories().get(k).getId() == 1480) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1480");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                Log.i(TAG, "initRoomInfo: name-->" + device.name);
//                                temp.akeyligntingdevice.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo: categoriese  light " + temp.akeyligntingdevice.size());
//                        }
//                        //akeywindowcurtaindevice
//                        if (room.getCategories().get(k).getId() == 1490) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1490");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                Log.i(TAG, "initRoomInfo: name-->" + device.name);
//                                temp.akeywindowcurtaindevice.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo: categoriese  1490 window  " + temp.akeywindowcurtaindevice.size());
//                        }
//
//                        //akeytemperaturedevice---有温度设备
//                        if (room.getCategories().get(k).getId() == 1550) {
//                            Log.i("1550", "initRoomInfo:  get categories  1550");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                Log.i("1550", "initRoomInfo: name-->" + device.name);
//                                temp.akeytemperaturedevice.add(device);
//                            }
//                        }
//                        //akeyhumiditydevice---有湿度设备
//                        if (room.getCategories().get(k).getId() == 1560) {
//                            Log.i("1560", "initRoomInfo:  get categories  1560");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                Log.i(TAG, "initRoomInfo: name-->" + device.name);
//                                temp.akeyhumiditydevice.add(device);
//                            }
//                        }
//                        //---------------------------------------------------------------------------------------------------------
//                        //pressure
//                        if (room.getCategories().get(k).getName().equals("pressure")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  pressure");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.press_o_device.add(device);
//                            }
//                        }
//                        //temperature_input
//                        if (room.getCategories().get(k).getName().equals("temperature_input")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  temperatureinput");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.temperature_i_device.add(device);
//                            }
//                        }
//                        //humidity_input
//                        if (room.getCategories().get(k).getName().equals("humidity_input")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  humidityinput");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.humidity_i_device.add(device);
//                            }
//                        }
//                        //pm25_input
//                        if (room.getCategories().get(k).getName().equals("pm25_input")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  pm25 input");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.pm25_i_device.add(device);
//                            }
//                        }
//                        //co2_input
//                        if (room.getCategories().get(k).getName().equals("co2_input")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  co2 input");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.co2_i_device.add(device);
//                            }
//                        }
//                        //voc_input
//                        if (room.getCategories().get(k).getName().equals("voc_input")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  voc input ");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.voc_i_device.add(device);
//                            }
//                        }
//                        //HCHO_input
//                        if (room.getCategories().get(k).getName().equals("HCHO_input")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  hcho input");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = 0;
//                                temp.hcho_i_device.add(device);
//                            }
//                        }
//                        //window
//                        if (room.getCategories().get(k).getName().equals("window")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  window");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = WindowItemAdapter.WINDOW_PAUSE;
//                                temp.window_device.add(device);
//                            }
//                        }
//                        //lighting
//                        if (room.getCategories().get(k).getName().equals("lamp")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  lamp");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = false;
//                                temp.lighting_device.add(device);
//                            }
//                        }
//                        //curtain
//                        if (room.getCategories().get(k).getName().equals("curtain")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  curtain");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = CurtainItemAdapter.CURTAIN_PAUSE;
//                                temp.curtain_device.add(device);
//                            }
//                        }
//                        //adjust_lamp
//                        if (room.getCategories().get(k).getName().equals("adjust_lamp")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  adjust lamp");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.value = AdjustLampItemAdapter.ADJUST_LAMP_OFF;
//                                temp.adjust_lighting_device.add(device);
//                            }
//                        }
//                        //security
//                        if (room.getCategories().get(k).getId() == 1280) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1280");
////                        if (room.getCategories().get(k).getName().equals("camera")) {
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.feature = devices.get(m).getFeature();
//                                temp.camera_device.add(device);
//                            }
//                        }
//                        if (room.getCategories().get(k).getName().equals("alert")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  alert");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.alert_device.add(device);
//                                mAlertDevice.add(device);
//                            }
//                        }
//                        //water
//                        if (room.getCategories().get(k).getName().equals("water_control")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  water control");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.water_control_device.add(device);
//                            }
//                        }
//                        if (room.getCategories().get(k).getName().equals("water_purifier")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  water purifier");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.water_purifier_control_device.add(device);
//                            }
//                        }
//                        if (room.getCategories().get(k).getName().equals("gas_control")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  gas control");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.gas_control_device.add(device);
//                            }
//                        }
//                        if (room.getCategories().get(k).getName().equals("energy_control")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  energy control");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = 0;
//                                temp.energy_control_device.add(device);
//                            }
//                        }
//                        if (room.getCategories().get(k).getName().equals("heater_control")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  heater control");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.heater_control_device.add(device);
//                            }
//                        }
//                        //fan
//                        if (room.getCategories().get(k).getName().equals("fan")) {
//                            Log.i(TAG, "initRoomInfo:  get categories  fan");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.fan_device.add(device);
//                            }
//                        }
//
//                        if (room.getCategories().get(k).getId() == 1450) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1450");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.electronicPurifier_device.add(device);
//                            }
//                        }
//
//
//                        if (room.getCategories().get(k).getId() == 1370) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1370");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.conditioner_device.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo: categoriese  1370 " + temp.conditioner_device.size());
//                        }
//
//                        if (room.getCategories().get(k).getId() == 1440) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1440");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.conditioner_pattern_device.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo: categoriese  140 " + temp.conditioner_pattern_device.size());
//                        }
//
//                        //房屋的空气净化
//                        if (room.getCategories().get(k).getId() == 1520) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1520");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                device.value = false;
//                                temp.roomAirdevice.add(device);
//                            }
//                            Log.i(TAG, "initRoomInfo: categoriese  1520 " + temp.roomAirdevice.size());
//                        }
//                        if (room.getCategories().get(k).getId() == 1380) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1380");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                PreferenceUtil.putString(Constant.SP_GOHOME, devices.get(m).getExtid());
//                            }
//                        }
//                        if (room.getCategories().get(k).getId() == 1390) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1390");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                PreferenceUtil.putString(Constant.SP_OUTHOME, devices.get(m).getExtid());
//                            }
//                        }
//                        if (room.getCategories().get(k).getId() == 1400) {
//                            Log.i(TAG, "initRoomInfo:  get categories  1400");
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                PreferenceUtil.putString(Constant.SP_MONITOR, devices.get(m).getExtid());
//                            }
//                        }
//
//                        if (room.getCategories().get(k).getId() == 1300
//                                || room.getCategories().get(k).getId() == 1310
//                                || room.getCategories().get(k).getId() == 1320
//                                || room.getCategories().get(k).getId() == 1330
//                                || room.getCategories().get(k).getId() == 1340) {
//                            List<Devices> devices = room.getCategories().get(k).getDevices();
//                            for (int m = 0; m < devices.size(); m++) {
//                                RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
//                                device.category = devices.get(m).getCategory();
//                                device.name = devices.get(m).getName();
//                                device.tag = devices.get(m).getExtid();
//                                device.room = room.getName();
//                                if (room.getCategories().get(k).getId() == 1330) {
//                                    device.value = 0.0;
//                                } else {
//                                    device.value = false;
//                                }
//                                temp.water_device.add(device);
//                            }
//                        }
//                         /*保存 一键空气净化设备*/
//                        if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_CLEANER) {
//                            Devices devices = room.getCategories().get(k).getDevices().get(0);
//                            RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
//                            deviceInfo.category = GizwitHolders.EVENT_ID_ONEKEY_CLEANER;
//                            deviceInfo.category = devices.getCategory();
//                            deviceInfo.name = devices.getName();
//                            deviceInfo.tag = devices.getExtid();
//                            deviceInfo.room = room.getName();
//                            GizwitHolders.getInstance().setAkeyAirDevice(deviceInfo);
//                        }
//
//                        /*保存 一键温度控制设备*/
//                        if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_TEMPERATURE) {
//                            Devices devices = room.getCategories().get(k).getDevices().get(0);
//                            RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
//                            deviceInfo.category = GizwitHolders.EVENT_ID_ONEKEY_TEMPERATURE;
//                            deviceInfo.category = devices.getCategory();
//                            deviceInfo.name = devices.getName();
//                            deviceInfo.tag = devices.getExtid();
//                            deviceInfo.room = room.getName();
//                            GizwitHolders.getInstance().setAkeyTemDevice(deviceInfo);
//                        }
//                        /*保存 一键湿度控制设备*/
//                        if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_HUMIDITY) {
//                            Devices devices = room.getCategories().get(k).getDevices().get(0);
//                            RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
//                            deviceInfo.category = GizwitHolders.EVENT_ID_ONEKEY_HUMIDITY;
//                            deviceInfo.category = devices.getCategory();
//                            deviceInfo.name = devices.getName();
//                            deviceInfo.tag = devices.getExtid();
//                            deviceInfo.room = room.getName();
//                            GizwitHolders.getInstance().setAkeyHumDevice(deviceInfo);
//                        }
//
//                    }
//                    mRoomInfo.add(temp);
//                }
//            }
//        }
////    }


}
