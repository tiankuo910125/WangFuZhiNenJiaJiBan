package com.demo.smarthome.communication.location;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.base.utils.Constant;

/**
 * Created by liukun on 2016/2/25.
 */
public class LocationUtilsImpl {

    private String TAG="LocationUtilsImpl";
    private Context mContext;

    private BDLocation mBDLocation;

    private Handler mHandler;
    private Message mMsg;

    private LocationClient mLocationClient = null;
    private BDLocationListener mLocationUtilsImplListener = new LocationUtilsImplListener();

    private static LocationUtilsImpl instance = null;

    public synchronized static LocationUtilsImpl getInstance(Context context) {
        if (instance == null) {
            instance = new LocationUtilsImpl(context);
        }
        return instance;
    }
    /**
     * 私有的构造函数
     */
    private LocationUtilsImpl(Context context) {
        mContext = context;
        mBDLocation = new BDLocation();
        mLocationClient = new LocationClient(mContext);     //声明LocationClient类
        mLocationClient.registerLocationListener( mLocationUtilsImplListener );    //注册监听函数
        initLocation();
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private class LocationUtilsImplListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mBDLocation = location;
                mMsg.obj = GsonTools.GsonToString(mBDLocation);
                PreferenceUtil.putString(Constant.HouseSystemManager.LOCATION, (String)mMsg.obj);
                mHandler.sendMessage(mMsg);
                mLocationClient.stop();
            }
        }

    };

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=0; //仅定位一次
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public void fetchLocationByCallBack(Handler handler, Message msg)
    {
        mHandler = handler;
        mMsg = msg;
        mLocationClient.start();
    }

    public void disconnectLocationService()
    {
        if (mLocationClient.isStarted())
        {
            mLocationClient.stop();
            Log.d(TAG,"disconnectLocationService, mLocationClient.stop();");
        }
    }
}
