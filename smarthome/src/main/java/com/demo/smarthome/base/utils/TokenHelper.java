package com.demo.smarthome.base.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TokenHelper {

    private static final String PATH_SD = Environment.getExternalStorageDirectory().getPath();
    private static final String PATH_TOKEN_IPC = PATH_SD + "/Token/IPC.txt";
    private static final String PATH_TOKEN_SENSORTREE = PATH_SD + "/Token/SensorTree.txt";

    private static final String TOEKN_IPC = "484c9637c8454f39ba22da87a4e618fb";
    private static final String TOKEN_SENSORTREE = "5a1f43b8ff2449b88b5b465b6957994f";

    public static String getIPCToken() {
        String str = "";

        File file = new File(PATH_TOKEN_IPC);

        try {

            FileInputStream in = new FileInputStream(file);

            // size 为字串的长度 ，这里一次�?读完

            int size = in.available();

            byte[] buffer = new byte[size];

            in.read(buffer);

            in.close();

            str = new String(buffer, "GB2312");

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }
        if (str == null || "".equals(str)) {
            str = TOEKN_IPC;
        }

        return str;
    }

    public static String getSensorTreeToken() {
        String str = "";

        File file = new File(PATH_TOKEN_SENSORTREE);

        try {

            FileInputStream in = new FileInputStream(file);

            // size 为字串的长度 ，这里一次�?读完

            int size = in.available();

            byte[] buffer = new byte[size];

            in.read(buffer);

            in.close();

            str = new String(buffer, "GB2312");

        } catch (IOException e) {

            e.printStackTrace();

        }

        if (str == null || "".equals(str)) {
            str = TOKEN_SENSORTREE;
        }

        return str;
    }

}
