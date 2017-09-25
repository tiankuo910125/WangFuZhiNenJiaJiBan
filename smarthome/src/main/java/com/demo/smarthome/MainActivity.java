package com.demo.smarthome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.EventBus_Account;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.camera.CameraListManager;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.tools.GizwitHolders;
import com.demo.smarthome.ui.HistoryJournalActivity;
import com.demo.smarthome.ui.HomePageView;
import com.demo.smarthome.ui.UserCenterActivity_Now;
import com.demo.smarthome.ui.model.RoomInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import aprivate.oo.gizwitopenapi.GizwitOpenAPI;
import aprivate.oo.gizwitopenapi.response.DeviceData;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by liukun on 2016/10/25.
 */
public class MainActivity extends BaseSmartHomeActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    MyActivityManager mam = MyActivityManager.getInstance();
    private Gson gson;

    public static MainActivity mainActivity;
    private String TAG = "MainActivity";
    private Context mContext;
    private SimpleDraweeView mUserHeaderIcon;
    private TextView mUserHeaderText;
    private HomePageView mHomePageView;
    private List<RoomInfo> mRoomInfo;
    private ProgressDialog proDialog;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mam.pushOneActivity(MainActivity.this);
        if (mainActivity == null) {
            mainActivity = MainActivity.this;
        }
        mContext = this;
        gson = new Gson();
        EventBus.getDefault().register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();


        mUserHeaderIcon = (SimpleDraweeView) this.findViewById(R.id.UserHeaderimageView);
        mUserHeaderText = (TextView) this.findViewById(R.id.UserHeadertextView);
        mUserHeaderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, UserCenterActivity_Now.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);
            }
        });

        Time t = new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。

        findViewById(R.id.nav_house_management).setOnClickListener(onNavigationItemClickListener);
        findViewById(R.id.nav_house_status).setOnClickListener(onNavigationItemClickListener);
        findViewById(R.id.nav_user).setOnClickListener(onNavigationItemClickListener);
        findViewById(R.id.nav_feedback).setOnClickListener(onNavigationItemClickListener);
        findViewById(R.id.nav_alert_record).setOnClickListener(onNavigationItemClickListener);
        findViewById(R.id.nav_device_log).setOnClickListener(onNavigationItemClickListener);
        findViewById(R.id.nav_message).setOnClickListener(onNavigationItemClickListener);
        findViewById(R.id.nav_room_control).setOnClickListener(onNavigationItemClickListener);

        roomInfoInit(getIntent());

        Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
        if (Constant.userProfileBean != null) {
            String imgUrl = Constant.userProfileBean.getProfile().getAvatarPath();
            if (imgUrl != null) {
                imgUrl = imgUrl.replace("https", "http");
                mUserHeaderIcon.setImageURI(imgUrl);
            }
        }

        String nameStr = Constant.userProfileBean.getProfile().getFirstName();
        if (nameStr == null || nameStr.equals("")) {
            mUserHeaderText.setText("火星人");
        } else {
            mUserHeaderText.setText(nameStr);
        }
    }

    private void roomInfoInit(Intent newData) {
        Log.i(TAG, "roomInfoInit: ");
//        mRoomInfo = (List<RoomInfo>) newData.getSerializableExtra("RoomInfo");
        mRoomInfo = GizwitHolders.getInstance().roomInfoList;
        mHomePageView = new HomePageView(this, mRoomInfo);
        mHomePageView.upDate(mRoomInfo);
        ((RelativeLayout) findViewById(R.id.main_viewpage)).addView(mHomePageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reGetState();
    }

    private void reGetState() {
        String did = GizwitHolders.getInstance().getDid();
        Log.i(TAG, "reGetState: did = " + did);
        GizwitOpenAPI.getInstance().getDeviceData(did, new GizwitOpenAPI.RequestCallback<DeviceData>() {
            @Override
            public void onSuccess(DeviceData deviceData) {
//                deviceData.getAttr().
                Log.i(TAG, "onSuccess: ");
                ConcurrentHashMap<String, Object> stateMap = new ConcurrentHashMap<String, Object>();
                for (Map.Entry<String, JsonElement> entry : deviceData.getAttr().entrySet()) {
                    stateMap.put(entry.getKey(), entry.getValue());
                }
                mHomePageView.setReceiveData(stateMap);
            }

            @Override
            public void onFailure(String msg) {
                Log.i(TAG, "onFailure: " + msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        CameraListManager.getInstance().logoutCamera();
        mam.popOneActivity(MainActivity.this);
        super.onDestroy();
    }

    long exitTime = 0;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
            return;
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(mContext, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return;
            }
            isExit = true;
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onNavigationItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.nav_message) {
                Intent mIntent = new Intent(mContext, HistoryJournalActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);
            }
            if (id == R.id.nav_room_control) {
                Intent mIntent = new Intent(mContext, ConfigActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);
            }
            if (id == R.id.nav_alert_record) {
                Intent mIntent = new Intent(mContext, AlertRecordActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);
            }
            if (id == R.id.nav_device_log) {
                Intent mIntent = new Intent(mContext, DeviceManagementActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);
            }
            if (id == R.id.nav_house_management) {
                Intent mIntent = new Intent(mContext, HouseManagementActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivityForResult(mIntent, 0x01);
            }
            if (id == R.id.nav_user) {
                Intent mIntent = new Intent(mContext, UserCenterActivity_Now.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);
            }
            if (id == R.id.nav_feedback) {
                Intent mIntent = new Intent(mContext, FeedBackNowActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: result ok");
            if (requestCode == 0x01) {//如果是 从房屋切换界面返回
                roomInfoInit(data);
            }
        } else {
            Log.d(TAG, "Data fresh failed");
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getEventBus(EventBus_Account message) {
        int tag = message.tag;
        Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
        switch (tag) {
            case Constant.EVENT_BUS_MODIFY_PERSONAL_INFORMATION:
                if (Constant.userProfileBean != null) {
                    String imgUrl = Constant.userProfileBean.getProfile().getAvatarPath();
                    if (imgUrl != null) {
                        imgUrl = imgUrl.replace("https", "http");
                        mUserHeaderIcon.setImageURI(imgUrl);
                    }
                }
                break;
            case Constant.EVENT_BUS_MODIFY_PERSONAL_HOUSE:
                Constant.userProfileBean = GsonTools.getBean(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                for (int i = 0; i < Constant.userProfileBean.getHouses().size(); i++) {
                    if (Constant.userProfileBean.getHouses().get(i).getId() == PreferenceUtil.getInt("default_house")) {
                        Houses houses = Constant.userProfileBean.getHouses().get(i);
                        for (int j = 0; j < houses.getRooms().size(); j++) {
                            mRoomInfo.get(j).roomName = houses.getRooms().get(j).getName();
                        }
                    }
                }
                mHomePageView.upDate(mRoomInfo);
                break;
            case Constant.EVENT_BUS_MODIFY_PERSONAL_REMARKS:
                Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                String nameStr = Constant.userProfileBean.getProfile().getFirstName();
                if (nameStr == null || nameStr.equals("")) {
                    mUserHeaderText.setText("火星人");
                } else {
                    mUserHeaderText.setText(nameStr);
                }
                break;
        }
    }

    private void goLogin() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

}
