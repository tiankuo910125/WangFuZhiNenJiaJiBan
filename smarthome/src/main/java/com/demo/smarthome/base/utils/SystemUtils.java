package com.demo.smarthome.base.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SystemUtils {
    public static void hideSoftwareKeyboard(Activity activity, View view) {
        try {
            if (view == null) {
                view = activity.getWindow().getDecorView();
            }

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DateFormat getDateFormat(final Context context) {
        return android.text.format.DateFormat.getDateFormat(context);
    }

    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm:ss z");
    }
}