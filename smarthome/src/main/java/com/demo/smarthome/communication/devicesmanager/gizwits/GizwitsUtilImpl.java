package com.demo.smarthome.communication.devicesmanager.gizwits;

import android.content.Context;

/**
 * Created by liukun on 2016/4/18.
 */
public class GizwitsUtilImpl {
    private String TAG="GizwitsUtilImpl";

    private Context mContext;
    private static GizwitsUtilImpl instance = null;

    private static final String URL = "http://api.gizwits.com";
    private static final String USER_INFO = "/app/users";
    private static final String USER_LOGIN = "/app/login";
    private static final String APP_TOKEN = "/app/request_token";
    private static final String VERIFY_CODES = "/app/verify/codes";
    private static final String RESET_PASSWORD = "/app/reset_password";
    private static final String DATA_LATEST = "/app/devdata/%s/latest";
    private static final String BIND_DEVICE = "/app/bind_mac";
    private static final String BINDED_DEVICE = "/app/bindings";
    private static final String CONTROL_DEVICE = "/app/control/did";
    private static final String PROFILE_URL = "http://site.gizwits.com/v2/datapoint?product_key=%s&format=json";



    public synchronized static GizwitsUtilImpl getInstance(Context context) {
        if (instance == null) {
            instance = new GizwitsUtilImpl(context);
        }
        return instance;
    }
    /**
     * 开启gizwits服务
     */
    private GizwitsUtilImpl(Context context) {
        mContext = context;
    }


}
