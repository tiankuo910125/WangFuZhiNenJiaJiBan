package com.demo.smarthome.base.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.view.KeyEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DialogUtils {

    private static SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取当前日期
     * 
     * @return
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return sf2.format(calendar.getTime());
    }

    public static void showMessageDialog(String message, Context context) {
        showMessageDialog("提示", message, context);
    }

    public static void showMessageDialog(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static ProgressDialog showProgressDialog(Context context, final AsyncTask asyncTask) {
        return showProgressDialog(context, "正在加载数据…�?", asyncTask);
    }

    public static ProgressDialog showProgressDialog(Context context, final AsyncTask asyncTask, boolean cancelable) {
        return showProgressDialog(context, "正在加载数据…�?", asyncTask, true);
    }

    public static ProgressDialog showProgressDialog(Context context, String message, final AsyncTask asyncTask) {
        return showProgressDialog(context, message, asyncTask, true);
    }

    public static ProgressDialog showProgressDialog(Context context, String message, final AsyncTask asyncTask, boolean cancelable) {
        ProgressDialog pg = new ProgressDialog(context);
        pg.setMessage(message);
        pg.setCanceledOnTouchOutside(false);
        pg.setCancelable(cancelable);
        pg.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                        asyncTask.cancel(true);
                        dialog.cancel();
                    }
                }
                return false;
            }
        });
        return pg;
    }

}
