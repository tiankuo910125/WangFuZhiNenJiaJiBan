package com.demo.smarthome;

import android.support.multidex.MultiDexApplication;

import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.tools.ConfigFile;
import com.facebook.drawee.backends.pipeline.Fresco;

import aprivate.oo.gizwitopenapi.GizwitOpenAPI;

/**
 * Created by liukun on 2016/3/7.
 */
public class MainApplication extends MultiDexApplication {//低版本 64k bug

    public static MainApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

//        File dirFirstFolder = new File("/sdcard/wangfu/imageloader/Cache");// 方法一：直接使用字符串，如果是安装在存储卡上面，则需要使用sdcard2，但是需要确认是否有存储卡
//        if (!dirFirstFolder.exists()) { //如果该文件夹不存在，则进行创建
//            dirFirstFolder.mkdirs();//创建文件夹
//        }

        PreferenceUtil.setContext(mContext);
        Fresco.initialize(getApplicationContext());
        GizwitOpenAPI.init("401a1b5f1b69466a953a7602fe99b8bc");
        ConfigFile.init(mContext);// 配置文件
////        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
//        //启动推送服务
//        // 初始化sdk,传入appId,登录机智云官方网站查看产品信息获得 AppID
//        GizWifiSDK.sharedInstance().startWithAppID(getApplicationContext(),
//                Constant.GizwitzConfigs.APPID);
//        // 设定日志打印级别,日志保存文件名，是否在后台打印数据.
//        GizWifiSDK.sharedInstance().setLogLevel(GizLogPrintLevel.GizLogPrintNone);
//        Fresco.initialize(this);
//        // 添加https证书
//        try {
//            String[]  certFiles=this.getAssets().list("certs");
//            if (certFiles != null) {
//                for (String cert:certFiles) {
//                    InputStream is = getAssets().open("certs/" + cert);
//                    HttpsHelper.addCertificate(is); // 这里将证书读取出来，，放在配置中byte[]里
//                }
//            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
