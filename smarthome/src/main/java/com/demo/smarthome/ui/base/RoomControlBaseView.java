package com.demo.smarthome.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.demo.smarthome.ui.model.RoomInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/11/22.
 */
public class RoomControlBaseView extends RelativeLayout {
    public RoomControlBaseView(Context context) {
        super(context);
    }

    public RoomControlBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomControlBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onDestroy() {

    }

    public void setReceiveData(ConcurrentHashMap<String, Object> dataMap) {
    }

    private final String TAG = "CheckValue";

    public int getValue(Object value) {
        if (value == null) {
            return 0;
        }
        String valueStr = String.valueOf(value);
        if (valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("false")) {//如果是 boolean 类型的返回值 转换为 0 1
            if (valueStr.equalsIgnoreCase("true")) {
                return 1;
            } else {
                return 0;
            }
        } else {//如果不是 boolean 应该是 int 类型了
            int result = 0;
            try {
                result = Integer.parseInt(valueStr);
            } catch (NumberFormatException e) {
                Log.i(TAG, "checkValueFormate: 数据点 数据类型无法解析  " + valueStr);
                result=0;
            }
            return result;
        }
    }

    public Object checkValueFormate(RoomInfo.DeviceInfo info, ConcurrentHashMap<String, Object> receiveData) {
        //检查 是否含有这个 tag
        if (!receiveData.containsKey(info.tag)) {
            Log.i(TAG, "checkValueFormate: NOT CONTAINED   tag is " + info.tag);
            return 0;
        }

        //检查 数据点的值是否合法
        Object value = receiveData.get(info.tag);
        if (value == null) {
            return 0;
        }
        //检查 值 是否是 boolean 类型
        String valueStr = String.valueOf(value);
        if (valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("false")) {//如果是 boolean 类型的返回值 转换为 0 1
            if (valueStr.equalsIgnoreCase("true")) {
                return 1;
            } else {
                return 0;
            }
        } else {//如果不是 boolean 应该是 int 类型了
            int result = 0;
            try {
                result = Integer.parseInt(valueStr);
            } catch (NumberFormatException e) {
                Log.i(TAG, "checkValueFormate: 数据点 数据类型无法解析  " + valueStr);
                result=0;
            }
            return result;
        }
    }

}
