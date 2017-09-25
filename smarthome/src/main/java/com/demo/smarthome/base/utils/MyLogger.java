package com.demo.smarthome.base.utils;

import android.util.Log;

/**
 * Created by link on 16-3-7.
 */
public class MyLogger {

    private final String TAG = "sh";
    private String mClassName = null;

    public MyLogger(String className) {
        mClassName = className;
    }

    public void v(String msg) {
      Log.v(TAG, "[" + mClassName + "@" + Thread.currentThread() + "]: " + msg);
    }

    public void d(String msg) {
        Log.d(TAG, "[" + mClassName + "@" + Thread.currentThread() + "]: " + msg);
    }

    public void e(String msg) {
        Log.e(TAG, "[" + mClassName + "@" + Thread.currentThread() + "]: " + msg);
    }
}
