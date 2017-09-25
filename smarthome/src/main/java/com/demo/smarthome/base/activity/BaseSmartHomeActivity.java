package com.demo.smarthome.base.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.base.utils.ActivityHistorys;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.DialogUtils;
import com.demo.smarthome.base.utils.NotificationUtils;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.communication.devicesmanager.gizwits.SettingManager;
import com.demo.smarthome.ui.model.RoomInfo;
import com.gizwits.gizwifisdk.api.GizUserInfo;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiGroup;
import com.gizwits.gizwifisdk.api.GizWifiSSID;
import com.gizwits.gizwifisdk.enumration.GizEventType;
import com.gizwits.gizwifisdk.enumration.GizScheduleStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Created by liukun on 2016/3/18.
 */
public class BaseSmartHomeActivity extends AppCompatActivity {

    private String TAG = "BaseSmartHomeActivity";
    public boolean isExit = false;

    /**
     * 设备列表.
     */
    protected static List<GizWifiDevice> deviceslist = new ArrayList<GizWifiDevice>();

    /** 绑定列表 */
    protected static List<GizWifiDevice> bindlist = new ArrayList<GizWifiDevice>();

    public static List<GizWifiGroup> grouplist = new ArrayList<GizWifiGroup>();

    /**
     * 指令管理器.
     */
    public CmdCenter mCenter;

    /**
     * SharePreference处理类.
     */
    public SettingManager setmanager;

    /** 当前操作的设备 */
    public static GizWifiDevice mXpgWifiDevice;

    public static List<RoomInfo.DeviceInfo> mAlertDevice;

    public static Map<String,Long> mAlertRecord;
    /**
     * GizWifiDeviceListener
     * <p/>
     * 设备属性监听器。 设备连接断开、获取绑定参数、获取设备信息、控制和接受设备信息相关.
     */
    protected GizWifiDeviceListener deviceListener = new GizWifiDeviceListener() {

        @Override
        public void didExitProductionTesting(GizWifiErrorCode result, GizWifiDevice device) {
            BaseSmartHomeActivity.this.didExitProductionTesting(result, device);
        }

        @Override
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
            BaseSmartHomeActivity.this.didReceiveData(result, device, dataMap, sn);
        }

        @Override
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            BaseSmartHomeActivity.this.didSetSubscribe(result, device, isSubscribed);
        }

        @Override
        public void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, String> hardwareInfo) {
            BaseSmartHomeActivity.this.didGetHardwareInfo(result, device, hardwareInfo);
        }

        @Override
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            BaseSmartHomeActivity.this.didUpdateNetStatus(device, netStatus);
        }

        @Override
        public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
            BaseSmartHomeActivity.this.didSetCustomInfo(result, device);
        }

    };

    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {

    }

    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, String> hardwareInfo) {

    }

    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {

    }

    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {

    }

    protected void didExitProductionTesting(GizWifiErrorCode result, GizWifiDevice device) {

    }

    /**
     * GizWifiSDKListener
     * <p/>
     * sdk监听器。 配置设备上线、注册登录用户、搜索发现设备、用户绑定和解绑设备相关.
     */
    private GizWifiSDKListener sdkListener = new GizWifiSDKListener() {

        @Override
        public void didGetCurrentCloudService(GizWifiErrorCode result, ConcurrentHashMap<String, String> cloudServiceInfo) {
            BaseSmartHomeActivity.this.didGetCurrentCloudService(result, cloudServiceInfo);
        }

        @Override
        public void didGetUserInfo(GizWifiErrorCode result, GizUserInfo userInfo) {
            BaseSmartHomeActivity.this.didGetUserInfo(result, userInfo);
        }

        @Override
        public void didDisableLAN(GizWifiErrorCode result) {
            BaseSmartHomeActivity.this.didDisableLAN(result);
        }

        @Override
        public void didChannelIDBind(GizWifiErrorCode result) {
            BaseSmartHomeActivity.this.didChannelIDBind(result);
        }

        @Override
        public void didChannelIDUnBind(GizWifiErrorCode result) {
            BaseSmartHomeActivity.this.didChannelIDUnBind(result);
        }

        @Override
        public void didCreateScheduler(GizWifiErrorCode result, String sid) {
            BaseSmartHomeActivity.this.didCreateScheduler(result, sid);
        }

        @Override
        public void didDeleteScheduler(GizWifiErrorCode result) {
            BaseSmartHomeActivity.this.didDeleteScheduler(result);
        }


        @Override
        public void didGetSchedulerStatus(GizWifiErrorCode result, String sid, String datetime, GizScheduleStatus status, ConcurrentHashMap<String, Boolean> statusDetail) {
            BaseSmartHomeActivity.this.didGetSchedulerStatus(result, sid, datetime, status, statusDetail);
        }

        @Override
        public void didBindDevice(GizWifiErrorCode result, String did) {
            BaseSmartHomeActivity.this.didBindDevice(result, did);
        }

        @Override
        public void didUnbindDevice(GizWifiErrorCode result, String did) {
            BaseSmartHomeActivity.this.didUnbindDevice(result, did);
        }

        @Override
        public void didChangeUserInfo(GizWifiErrorCode result) {
            BaseSmartHomeActivity.this.didChangeUserInfo(result);
        }

        @Override
        public void didTransAnonymousUser(GizWifiErrorCode result) {
            BaseSmartHomeActivity.this.didTransAnonymousUser(result);
        }

        @Override
        public void didUpdateProduct(GizWifiErrorCode result, String productKey, String productUI) {
            BaseSmartHomeActivity.this.didUpdateProduct(result, productKey, productUI);
        }

        @Override
        public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
            BaseSmartHomeActivity.this.didDiscovered(result, deviceList);
        }


        @Override
        public void didUserLogin(GizWifiErrorCode result, String uid, String token) {
            BaseSmartHomeActivity.this.didUserLogin(result, uid, token);
        }

        @Override
        public void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID, String eventMessage) {
            BaseSmartHomeActivity.this.didNotifyEvent(eventType, eventSource, eventID, eventMessage);
        }

        @Override
        public void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {
            BaseSmartHomeActivity.this.didSetDeviceOnboarding(result, mac, did, productKey);
        }

        @Override
        public void didGetSSIDList(GizWifiErrorCode result, List<GizWifiSSID> ssidInfoList) {
            BaseSmartHomeActivity.this.didGetSSIDList(result, ssidInfoList);
        }

        @Override
        public void didGetCaptchaCode(GizWifiErrorCode result, String token, String captchaId, String captchaURL) {
            BaseSmartHomeActivity.this.didGetCaptchaCode(result, token, captchaId, captchaURL);
        }

        @Override
        public void didVerifyPhoneSMSCode(GizWifiErrorCode result) {
            BaseSmartHomeActivity.this.didVerifyPhoneSMSCode(result);
        }

        @Override
        public void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
            BaseSmartHomeActivity.this.didRegisterUser(result, uid, token);
        }

        @Override
        public void didChangeUserPassword(GizWifiErrorCode result) {
            BaseSmartHomeActivity.this.didChangeUserPassword(result);
        }

        @Override
        public void didGetGroups(GizWifiErrorCode result, List<GizWifiGroup> groupList) {
            BaseSmartHomeActivity.this.didGetGroups(result, groupList);
        }

    };

    protected void didGetUserInfo(GizWifiErrorCode result, GizUserInfo userInfo) {

    }

    protected void didDisableLAN(GizWifiErrorCode result) {

    }

    protected void didChannelIDBind(GizWifiErrorCode result) {

    }

    protected void didChannelIDUnBind(GizWifiErrorCode result) {

    }

    protected void didCreateScheduler(GizWifiErrorCode result, String sid) {

    }

    protected void didDeleteScheduler(GizWifiErrorCode result) {

    }


    protected void didGetSchedulerStatus(GizWifiErrorCode result, String sid, String datetime, GizScheduleStatus status, ConcurrentHashMap<String, Boolean> statusDetail) {

    }

    protected void didChangeUserInfo(GizWifiErrorCode result) {

    }

    protected void didTransAnonymousUser(GizWifiErrorCode result) {

    }

    protected void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID, String eventMessage) {

    }

    protected void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {

    }

    protected void didGetCaptchaCode(GizWifiErrorCode result, String token, String captchaId, String captchaURL) {

    }

    protected void didVerifyPhoneSMSCode(GizWifiErrorCode result) {

    }

    protected void didGetCurrentCloudService(GizWifiErrorCode result, ConcurrentHashMap<String, String> cloudServiceInfo) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setmanager = new SettingManager(getApplicationContext());
        mCenter = CmdCenter.getInstance(getApplicationContext());
        // 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
        mCenter.getGizWifiSDK().setListener(sdkListener);
        Log.d(TAG, "getGizWifiSDK().setListener success");
        // 把activity推入历史栈，退出app后清除历史栈，避免造成内存溢出
        ActivityHistorys.put(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onUserLeaveHint() {
        PreferenceUtil.putBoolean(Constant.LockScreenManager.PASSLOCKSCREENCHECK, false);
        super.onUserLeaveHint();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
        mCenter.getGizWifiSDK().setListener(sdkListener);
        Log.d(TAG, "getGizWifiSDK().setListener success");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "@@@@@@@@@@@@@@@@fall in BaseSmartHomeActivity onActivityResult@@@@@@@@@@@@@@@@@@@@@@@");
    }


    protected void didGetGroups(GizWifiErrorCode result, List<GizWifiGroup> groupList) {
        // TODO Auto-generated method stub

    }


    /**
     * 用户登陆回调接口.
     *

     */
    protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {

    }

    /**
     * 设备解除绑定回调接口.
     *
     */
    protected void didUnbindDevice(GizWifiErrorCode result, String did) {

    }


    /**
     * 注册用户结果回调接口.
     *
     */
    protected void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
        // TODO Auto-generated method stub

    }

    /**
     * 获取ssid列表回调接口.
     */
    protected void didGetSSIDList(GizWifiErrorCode result, List<GizWifiSSID> ssidInfoList) {
        // TODO Auto-generated method stub

    }

    /**
     * 搜索设备回调接口.
     */
    protected void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
        // TODO Auto-generated method stub

    }


    /**
     * 更换密码回调接口.
     *
     */
    protected void didChangeUserPassword(GizWifiErrorCode result) {
        // TODO Auto-generated method stub
    }


    /**
     * 绑定设备结果回调.
     *
     */
    protected void didBindDevice(GizWifiErrorCode result, String did) {

    }

    /**
     * 接收指令回调
     * <p/>
     * sdk接收到模块传入的数据回调该接口.
     *
     * @param device
     *            设备对象
     * @param dataMap
     *            json数据表
     * @param result
     *            状态代码
     */
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,final  ConcurrentHashMap<String, Object> dataMap, int sn) {
        Context context = BaseSmartHomeActivity.this;
        if (dataMap != null) {
            //TODO: Alerts和faults信息出发Notification的通知
            Tasks.executeInBackground(context, new BackgroundWork<Object>() {
                @Override
                public Object doInBackground() throws Exception {
                    List<String> notificatoinList = new ArrayList<String>();
                    JSONObject jsonObject;
                    String alertsJson = null;
                    String faultsJson = null;
                    if (dataMap.get("alerts") != null)
                        alertsJson = dataMap.get("alerts").toString();
                    if (dataMap.get("faults") != null)
                        faultsJson = dataMap.get("faults").toString();
//                    if (alertsJson != null && alertsJson.length()>0) //存在警告信息
//                    {
//                        Log.d(TAG,"alerts is "+alertsJson);
//                        try {
//                            jsonObject = new JSONObject(alertsJson);
//                            ContentResolver contentresolver=  BaseSmartHomeActivity.this.getContentResolver();
//                            Uri msgUri= Uri.parse("content://DataContentProvider/message");
//                            for (int i = 0; i < jsonObject.length(); i++) {
//                                //jsonObject.put(jsonObject.names().getString(0),true);
//                                if (jsonObject.getBoolean(jsonObject.names().getString(i)) != false) {
//                                    String name=null;
//                                    for (int j=0;j<mAlertDevice.size();j++){
//                                        Log.d(TAG,"alerts device name  is "+mAlertDevice.get(j).name);
//                                        if(mAlertDevice.get(j).tag.equals(jsonObject.names().getString(i))){
//                                            name = mAlertDevice.get(j).name;
//                                        }
//                                    }
//                                    name = (name==null)?jsonObject.names().getString(i):name;
//                                    if (mAlertRecord.get(name) == null || (System.currentTimeMillis()-mAlertRecord.get(name))>200000) {
//                                        notificatoinList.add(name + " "
//                                                + "触发告警！\r\n");
//                                        //TODO:写入Message数据库
//                                        ContentValues values = new ContentValues();
//                                        values.put("title", name);
//                                        values.put("content", "触发告警！");
//                                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
//                                        String date = sDateFormat.format(new java.util.Date());
//                                        values.put("time", date);
//                                        values.put("type", Constant.MessageType.ALERT);
//                                        contentresolver.insert(msgUri, values);
//                                        mAlertRecord.put(name, System.currentTimeMillis());
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    if (faultsJson != null && faultsJson.length()>0) //存在设备故障信息
                    {
                        Log.d(TAG,"faults is "+faultsJson);
                        try {
                            jsonObject = new JSONObject(faultsJson);
                            ContentResolver contentresolver=  BaseSmartHomeActivity.this.getContentResolver();
                            Uri msgUri= Uri.parse("content://DataContentProvider/message");
                            for (int i = 0; i < jsonObject.length(); i++) {
                                if (jsonObject.getInt(jsonObject.names().getString(i)) != 0) {
                                    notificatoinList.add(jsonObject.names().getString(i) + " "
                                            + jsonObject.getInt(jsonObject.names().getString(i)) + "\r\n");
                                    //TODO:写入Message数据库
                                    ContentValues values = new ContentValues();
                                    values.put("title",jsonObject.names().getString(i));
                                    values.put("content",jsonObject.getInt(jsonObject.names().getString(i)));
                                    SimpleDateFormat sDateFormat    =   new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                                    String    date    =    sDateFormat.format(new    java.util.Date());
                                    values.put("time",date);
                                    values.put("type", Constant.MessageType.FAULT);
                                    contentresolver.insert(msgUri, values);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return notificatoinList;
                }
            }, new Completion<Object>() {
                @Override
                public void onSuccess(Context context, Object result) {
                    List<String> notificationList = (List<String>)result;
                    for (int i=0;i<notificationList.size();i++)
                    {
                        NotificationUtils.generateNotification(context,notificationList.get(i),notificationList.get(i),null);
                        DialogUtils.showMessageDialog(notificationList.get(i), BaseSmartHomeActivity.this);
                    }
                }

                @Override
                public void onError(Context context, Exception e) {

                }
            });
        }

    }


    protected void didUpdateProduct(GizWifiErrorCode result, String productKey, String productUI) {
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
