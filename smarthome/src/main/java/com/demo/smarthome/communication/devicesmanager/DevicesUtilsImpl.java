package com.demo.smarthome.communication.devicesmanager;

import com.demo.smarthome.base.exception.NetException;

/**
 * Created by liukun on 2016/3/28.
 */
public class DevicesUtilsImpl {
    private String TAG="DevicesUtilsImpl";


    private static class DevicesUtilsImplHolder {
        static final DevicesUtilsImpl instance = new DevicesUtilsImpl();
    }

    /**
     * 私有的构造函数
     */
    private DevicesUtilsImpl() {

    }

    public static DevicesUtilsImpl getInstance() {
        return DevicesUtilsImplHolder.instance;
    }

    public String getDeviceList(String key) throws NetException {
        return null;
    }

    public String getDeviceDetail(String key,String deviceID) throws NetException {
        return null;
    }

}
