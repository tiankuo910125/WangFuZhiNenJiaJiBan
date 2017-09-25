package com.demo.smarthome;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.base.utils.StringUtils;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.DescriPtion;
import com.demo.smarthome.communication.jsonbean.OutAccessToken;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.communication.jsonbean.ginseng.LoginGinseng;
import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.communication.location.LocationUtilsImpl;
import com.demo.smarthome.myinterface.HttpInterface;
import com.demo.smarthome.tools.GizwitHolders;
import com.demo.smarthome.ui.model.RoomInfo;
import com.ezvizuikit.open.EZUIKit;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.videogo.openapi.EZOpenSDK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aprivate.oo.gizwitopenapi.GizwitOpenAPI;
import aprivate.oo.gizwitopenapi.response.BindingList;
import aprivate.oo.gizwitopenapi.response.LoginResponse;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by liukun on 2016/3/19.
 */
public class LoginActivity extends BaseSmartHomeActivity {
    MyActivityManager mam = MyActivityManager.getInstance();
    private Gson gson = new Gson();
    private String TAG = "LoginActivity";
    private Context mContext;
    private KProgressHUD kProgressHUD;
    private Button mLoginBtn;
    private EditText mUsername;
    private EditText mPassword;
    private Button mRegister;
    private TextView mForgotPwd;
    private String username;
    private String password;
    private static final int LOGIN_SERVER = 97;
    private static final int GET_LOCATION = 98;
    private static final int SETUP_UI = 99;
    private static final int GIZWITZ_USER_LOGIN = 100;
    private static final int SEARCH_ROUTER = 101;
    private static final int BIND_ROUTER = 102;
    private static final int LOGIN_DEVICE = 103;
    private static final int SETUP_PROFILE = 104;
    private static final int INIT_HOUSE_DEVICE_INFO = 105;
    private static final int FINISH_LOGIN = 106;
    private static final int TIME_OUT = 107;
    private static final int ERROR_STATE = 108;

    private int loginState = 0;
    private BDLocation mBDLocation;

    private List<RoomInfo> mRoomInfo = new ArrayList<>();

    private UserProfileBean userProfileBean;

    private ProgressDialog proDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GIZWITZ_USER_LOGIN:
                    loginGizwit();
                    break;
                case FINISH_LOGIN:
                    Log.i(TAG, "handleMessage: finished login");
                    PreferenceUtil.putBoolean(Constant.UserManager.LOGINSTATE, true);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    intent.putExtra("RoomInfo", (Serializable) mRoomInfo);
                    startActivity(intent);
                    finish();
                    break;

                case TIME_OUT: {
                    if (kProgressHUD.isShowing())
                        kProgressHUD.dismiss();
                    Toast.makeText(mContext, "登录设备超时", Toast.LENGTH_SHORT).show();
                    break;
                }
                case ERROR_STATE: {
                    if (kProgressHUD != null && kProgressHUD.isShowing())
                        kProgressHUD.dismiss();
                    Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mam.pushOneActivity(LoginActivity.this);
        mContext = this;
        loginState = 0;
        Log.d(TAG, "fall in LoginActivty");
        kProgressHUD = AppUtils.getLoadingView(this);
        setContentView(R.layout.login_activity);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            init();
            sdkInit();
        }
    }


    private void checkPermission() {
        String[] permissions = {
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.GET_TASKS,
                Manifest.permission.VIBRATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.EXPAND_STATUS_BAR,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CHANGE_CONFIGURATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
        };
        ActivityCompat.requestPermissions(this, permissions, 0x01);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x01) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
            }
            init();
            sdkInit();
        }
    }

    private void init() {
        mUsername = (EditText) findViewById(R.id.login_activity_username_edit);
        mPassword = (EditText) findViewById(R.id.login_activity_pwd_edit);
        mRegister = (Button) findViewById(R.id.login_activity_register_btn);
        mRegister.setOnClickListener(onClickListenerImpl);
        mForgotPwd = (TextView) findViewById(R.id.login_activity_forgot_pwd_btn);
        mForgotPwd.setOnClickListener(onClickListenerImpl);
        mLoginBtn = (Button) findViewById(R.id.login_activity_login_btn);
        mLoginBtn.setOnClickListener(onClickListenerImpl);
        //判断是否登录
        if (PreferenceUtil.getBoolean(Constant.UserManager.LOGINSTATE, false)) {
            //TODO:数据初始化
            //proDialog = android.app.ProgressDialog.show(mContext, null, "正在加载...");
            kProgressHUD.show();
            //向服务器提交用户名密码进行验证
            username = PreferenceUtil.getString(Constant.UserManager.USERNAME);
            password = PreferenceUtil.getString(Constant.UserManager.PASSWORD);
            mUsername.setText(username);
            mPassword.setText(password);
            getLogInData();

        } else {
            if (PreferenceUtil.getString(Constant.UserManager.USERNAME) != null && !PreferenceUtil.getString(Constant.UserManager.USERNAME).equals("")) {
                username = PreferenceUtil.getString(Constant.UserManager.USERNAME);
                mUsername.setText(username);
            }
        }

        int type = getIntent().getIntExtra("TYPE", -1);
        if (type == 403) {
            mUsername.setText(username);
            AppUtils.show(LoginActivity.this, getIntent().getStringExtra("TAG"));
        }
    }

    @Override
    public void onBackPressed() {
//        if (proDialog != null && proDialog.isShowing())
//            proDialog.dismiss();
        if (kProgressHUD != null && kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        LocationUtilsImpl.getInstance(mContext).disconnectLocationService();
        super.onDestroy();
        Log.d(TAG, "Destroy LoginActivty");
        mam.popOneActivity(LoginActivity.this);
    }

    private void initRoomInfo() {
        GizwitHolders.getInstance().setListInfo(mRoomInfo, mAlertDevice, mAlertRecord, userProfileBean, new GizwitHolders.RoomInfoInitCallback() {
            @Override
            public void onInited(List<RoomInfo> roomInfos) {
                getAccessToken();
            }
        });
//        getAccessToken();
    }

    private void sdkInit() {
        Log.i(TAG, "sdkInit: ");
        PreferenceUtil.setContext(getApplicationContext());
//        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
//        EZUIKit.setDebug(true);//开启debug模式
        EZOpenSDK.enableP2P(true);
        EZOpenSDK.initLib(getApplication(), "7630855c3e4444029e9424cf4156b1de", "");
        EZUIKit.initWithAppKey(getApplication(), "7630855c3e4444029e9424cf4156b1de");
        //启动推送服务
        // 初始化sdk,传入appId,登录机智云官方网站查看产品信息获得 AppID
//        GizWifiSDK.sharedInstance().startWithAppID(getApplicationContext(),
//                Constant.GizwitzConfigs.APPID);
        // 设定日志打印级别,日志保存文件名，是否在后台打印数据.
//        GizWifiSDK.sharedInstance().setLogLevel(GizLogPrintLevel.GizLogPrintNone);

    }

    private View.OnClickListener onClickListenerImpl = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_activity_login_btn: {
                    if (mUsername.length() > 0 || mPassword.length() > 0) {
                        if (kProgressHUD == null || !kProgressHUD.isShowing()) {
                            //TODO:数据初始化
                            kProgressHUD.show();
                        }
                        //TODO:服务器验证用户名密码
                        username = mUsername.getText().toString();
                        password = mPassword.getText().toString();
                        getLogInData();
                    } else {
                        Toast.makeText(mContext, getString(R.string.login_activity_login_error), Toast.LENGTH_SHORT).show();
                        mUsername.setText("");
                        mPassword.setText("");
                    }
                    break;
                }
                case R.id.login_activity_forgot_pwd_btn: {
                    Intent intent = new Intent(mContext, PasswordRecoveryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    startActivity(intent);
                    break;
                }
                case R.id.login_activity_register_btn: {
                    Intent intent = new Intent(mContext, RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    startActivity(intent);
                    break;
                }
            }
        }
    };


    /**
     * 登录请求
     * 登录自己的服务器
     */

    private void getLogInData() {
        LoginGinseng loginGinseng = new LoginGinseng();
        loginGinseng.setMobile(username);
        loginGinseng.setPassword(password);
        loginGinseng.setHouse_type("1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();
        HttpInterface httpInterface = retrofit.create(HttpInterface.class);
        Call<ResultBean> call = httpInterface.getLogIn(loginGinseng);
        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean r = response.body();
//                                //TODO: 验证  如果成功继续，如果失败重新登录

                Log.e(TAG,"登录的返回===" + r.getResult());

                if (r != null) {
                    Log.i(TAG, "onResponse: " + response.body());
                    if (r.getCode() != null && r.getCode() == Constant.HttpURL.ErrorCode.OK) {
                        PreferenceUtil.putString(Constant.UserManager.USERNAME, username);
                        PreferenceUtil.putString(Constant.UserManager.PASSWORD, password);
                        userProfileBean = new UserProfileBean();
                        userProfileBean = GsonTools.getBean(GsonTools.GsonToString(r.getResult()), UserProfileBean.class);

                        //保存token
                        PreferenceUtil.putString("accessToken", userProfileBean.getOauth().getAccessToken());
                        PreferenceUtil.putString("refreshToken", userProfileBean.getOauth().getRefreshToken());
                        PreferenceUtil.putString("tokenType", userProfileBean.getOauth().getTokenType());
                        PreferenceUtil.putInt("expiresIn", userProfileBean.getOauth().getExpiresIn());
                        /*这是保存的 用户信息*/
                        PreferenceUtil.putString("user_profile", GsonTools.GsonToString(r.getResult()));
//                        /*保存 到内存*/
//                        GizwitHolders.getInstance().setUserProfileBean(userProfileBean);
//                        if (userProfileBean.getHouses().size() <= 0) { //没有房间信息，需要扫描添加
//                            Intent intent = new Intent(mContext, HouseManagementActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                            startActivity(intent);
//                            return;
//                        } else { //有房间信息
//                            int defaultHouseID = PreferenceUtil.getInt("default_house", -1);
//                            Houses houses = null;
//                            int houseTag = 0;
//                            if (defaultHouseID == -1) {
//                                houses = userProfileBean.getHouses().get(0);
//                            } else {
//                                for (int i = 0; i < userProfileBean.getHouses().size(); i++) {
//                                    if (userProfileBean.getHouses().get(i).getId() == defaultHouseID) {
//                                        houses = userProfileBean.getHouses().get(i);
//                                        houseTag = i;
//                                    }
//                                }
//                                if (houses == null) {
//                                    houses = userProfileBean.getHouses().get(0);//houses = userProfileBean.getHouses().get(0);
//                                    houseTag = 0;
//                                }
//                            }
//                            GizwitHolders.getInstance().setProductKey(houses.getGateways().get(0).getExtid());
//
//                            PreferenceUtil.putInt("default_house", houses.getId());
//                            PreferenceUtil.putString("rolse", houses.getRole());
//
//                            PreferenceUtil.putString("product_city", houses.getCity());
//                            PreferenceUtil.putString("product_address", houses.getAddress());
//
//                            DescriPtion descriPtion = gson.fromJson(houses.getGateways().get(0).getDescription(), DescriPtion.class);
//                            PreferenceUtil.putString("product_key", descriPtion.getProductKey());
//                            PreferenceUtil.putString("productSecret", descriPtion.getProductSecret());
//                            PreferenceUtil.putString("mac", descriPtion.getMac());
//                            PreferenceUtil.putString("AppId", descriPtion.getAppId());
//                            PreferenceUtil.putString("AppSecret", descriPtion.getAppSecret());
//
//                            PreferenceUtil.putString(Constant.HouseSystemManager.CITY, houses.getCity());
//                            PreferenceUtil.putString(Constant.HouseSystemManager.ADDRESS, houses.getAddress());
//                            Set<String> tags = new HashSet<String>(); //tag--product_key
//                            for (Houses houses1 : userProfileBean.getHouses()) {
//                                tags.add( houses1.getGateways().get(0).getExtid());
//                            }
//
//                            JPushInterface.setAliasAndTags(LoginActivity.this, null, tags, new TagAliasCallback() {
//                                @Override
//                                public void gotResult(int i, String s, Set<String> set) {
//
//                                }
//                            });
//                        }
                        //TODO:登录机智云
                        mHandler.sendEmptyMessage(GIZWITZ_USER_LOGIN);
                    } else {
                        Toast.makeText(mContext, r.getMessage(), Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(ERROR_STATE);
                    }
                } else {
                    kProgressHUD.dismiss();
                    AppUtils.show(LoginActivity.this, "请求失败");
                }
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"为什么失败了？" + t.getMessage());
                Log.e(TAG,"为什么失败了？" + t.toString());
                mHandler.sendEmptyMessage(ERROR_STATE);
            }
        });

    }

    private void getAccessToken() {
        Log.i(TAG, "getAccessToken: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://open.ys7.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<OutAccessToken> call = httpInterface.getAccessToken("7630855c3e4444029e9424cf4156b1de", "f526b7ad9eedcfcf5c3e4461538f420d");

        call.enqueue(new Callback<OutAccessToken>() {
            @Override
            public void onResponse(Call<OutAccessToken> call, Response<OutAccessToken> response) {
                Log.i(TAG, "onResponse: getaccess token");
                if (TextUtils.equals("200", response.body().getCode())) {
                    String tocken = response.body().getData().getAccessToken();
                    Log.i(TAG, "onResponse: " + tocken);
                    EZOpenSDK.getInstance().setAccessToken(response.body().getData().getAccessToken());
                    EZUIKit.setAccessToken(response.body().getData().getAccessToken());
                } else {

                }
                if (kProgressHUD.isShowing())
                    kProgressHUD.dismiss();
                mHandler.sendEmptyMessage(FINISH_LOGIN);

            }

            @Override
            public void onFailure(Call<OutAccessToken> call, Throwable t) {
                Log.i(TAG, "onFailure: getaccess token");
                if (kProgressHUD.isShowing())
                    kProgressHUD.dismiss();
                mHandler.sendEmptyMessage(FINISH_LOGIN);
            }
        });
    }


    private void loginGizwit() {
        String un = PreferenceUtil.getString(Constant.UserManager.USERNAME);
        GizwitOpenAPI.getInstance().loginServer(StringUtils.MD5Encode(un)
                + Constant.GizwitzConfigs.UserNameEnd, Constant.GizwitzConfigs.Password, new GizwitOpenAPI.RequestCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Log.i(TAG, "onSuccess: login ");
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
                PreferenceUtil.putString("uid", loginResponse.getUid());
                PreferenceUtil.putString("token", loginResponse.getToken());


                 /*保存 到内存*/
                GizwitHolders.getInstance().setUserProfileBean(userProfileBean);
                if (userProfileBean.getHouses().size() <= 0) { //没有房间信息，需要扫描添加
                    Intent intent = new Intent(mContext, HouseManagementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    startActivityForResult(intent,11);
                    return;
                } else { //有房间信息
                    int defaultHouseID = PreferenceUtil.getInt("default_house", -1);
                    Houses houses = null;
                    int houseTag = 0;
                    if (defaultHouseID == -1) {
                        houses = userProfileBean.getHouses().get(0);
                    } else {
                        for (int i = 0; i < userProfileBean.getHouses().size(); i++) {
                            if (userProfileBean.getHouses().get(i).getId() == defaultHouseID) {
                                houses = userProfileBean.getHouses().get(i);
                                houseTag = i;
                            }
                        }
                        if (houses == null) {
                            houses = userProfileBean.getHouses().get(0);//houses = userProfileBean.getHouses().get(0);
                            houseTag = 0;
                        }
                    }


                    GizwitHolders.getInstance().setProductKey(houses.getGateways().get(0).getExtid());

                    PreferenceUtil.putInt("default_house", houses.getId());
                    PreferenceUtil.putString("rolse", houses.getRole());

                    PreferenceUtil.putString("product_city", houses.getCity());
                    PreferenceUtil.putString("product_address", houses.getAddress());

                    DescriPtion descriPtion = gson.fromJson(houses.getGateways().get(0).getDescription(), DescriPtion.class);
                    PreferenceUtil.putString("product_key", descriPtion.getProductKey());
                    PreferenceUtil.putString("productSecret", descriPtion.getProductSecret());
                    PreferenceUtil.putString("mac", descriPtion.getMac());
                    PreferenceUtil.putString("AppId", descriPtion.getAppId());
                    PreferenceUtil.putString("AppSecret", descriPtion.getAppSecret());

                    PreferenceUtil.putString(Constant.HouseSystemManager.CITY, houses.getCity());
                    PreferenceUtil.putString(Constant.HouseSystemManager.ADDRESS, houses.getAddress());
                    Set<String> tags = new HashSet<String>(); //tag--product_key
                    for (Houses houses1 : userProfileBean.getHouses()) {
                        tags.add(houses1.getGateways().get(0).getExtid());
                    }

                    JPushInterface.setAliasAndTags(LoginActivity.this, null, tags, new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {

                        }
                    });
                    getUserDevices();
                }

            }

            @Override
            public void onFailure(String msg) {
                Log.i(TAG, "onFailure: login");
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        });
    }

    private void getUserDevices() {
        GizwitOpenAPI.getInstance().getDeviceList(new GizwitOpenAPI.RequestCallback<BindingList>() {
            @Override
            public void onSuccess(BindingList bindingList) {

                /*
                通过userProfileBean获取房屋ID，当用户信息中的房屋ID和house列表中ID相匹配时，获取 productKey
                从机智云上获取绑定的房屋信息列表，当绑定列表中的productKey和用户房屋信息中的productKey相匹配时
                加载房屋信息
                 */

                UserProfileBean userProfileBean = GizwitHolders.getInstance().getUserProfileBean();
                if (userProfileBean == null) {
                    userProfileBean = GsonTools.getBean(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                }
                ArrayList<Houses> houseList = (ArrayList<Houses>) GizwitHolders.getInstance().getUserProfileBean().getHouses();
                String productKey = null;
                        /*获取 目标房屋网关的 productkey*/
//                int houseId = houseList.get(0).getId();
                  int houseId = userProfileBean.getHouses().get(0).getId();
                for (Houses house : houseList) {
                    if (houseId == house.getId()) {
                        productKey = house.getGateways().get(0).getExtid();
                        break;
                    }
                }

                Log.i(TAG, "onSuccess: get bindinglist");
//                    成功获取 机智云上的设备列表后 进行缓存
                    GizwitHolders.getInstance().setBindingList(bindingList);
//                     String GproductKey = GizwitHolders.getInstance().getProductKey();
                    Log.i(TAG, "onSuccess: 获取用户绑定设备 " + GizwitHolders.getInstance().getProductKey());
                    for (BindingList.DevicesBean bean : bindingList.getDevices()) {
                        if (bean.getProduct_key().equals(productKey)) {
                            GizwitHolders.getInstance().setProductKey(productKey);
                            GizwitHolders.getInstance().setDid(bean.getDid());
                            Log.i(TAG, "onSuccess: 获取用户绑定设备 did " + bean.getDid());
                            break;
                        }
                    }
                    //登录成功 开始初始化 房屋结构信息 准备跳转界面
                    initRoomInfo();
            }

            @Override
            public void onFailure(String msg) {
                Log.i(TAG, "onFailure: getUserDevcies  " + msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 3) {
            if(requestCode == 11) {
                Log.e(TAG,"登录的这里走了没有？？？？" );
                getUserDevices();

                }
        }
    }
}
