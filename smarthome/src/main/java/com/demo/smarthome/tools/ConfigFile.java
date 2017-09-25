package com.demo.smarthome.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhuxiaolong on 2017/9/7.
 * 配置信息持久化
 * 登录信息
 * 主要是房间信息  由于目前都保存在内存中 过于庞大
 * 分房屋进行保存
 * 实现主体 使用 sharedpreference
 */

public class ConfigFile {
    private static final String CONFIG_NAME_WF = "config_wangfu";
    private static final String CONFIG_NAME_GW = "config_gizwit";
    private SharedPreferences sharedPreferencesWF;
    private SharedPreferences sharedPreferencesGW;
    private static ConfigFile INSTANCE;

    public static void init(Context context) {
        INSTANCE = new ConfigFile(context);
    }

    public static ConfigFile getInstance() {
        return INSTANCE;
    }


    public ConfigFile(Context context) {
        //持有 机智云 配置文档
        sharedPreferencesGW = context.getSharedPreferences(CONFIG_NAME_GW, Context.MODE_PRIVATE);
        //持有 王府 配置文档
        sharedPreferencesWF = context.getSharedPreferences(CONFIG_NAME_WF, Context.MODE_PRIVATE);
    }


    public void saveHouse(int houseId, String houseJson) {
        sharedPreferencesWF.edit().putString(String.valueOf(houseId), houseJson).apply();
    }

    public String getHouse(int houseId) {
        return sharedPreferencesWF.getString(String.valueOf(houseId), null);
    }

    public void saveBindDevice(String productKey, String deviceJson) {
        sharedPreferencesGW.edit().putString(productKey, deviceJson).apply();
    }

    public String getBindDevice(String productKey) {
        return sharedPreferencesGW.getString(productKey, null);
    }
}
