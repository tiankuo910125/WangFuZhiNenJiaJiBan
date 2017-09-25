package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.ui.model.RoomInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by liukun on 2016/3/17.
 */
public class WindowItemAdapter extends BaseAdapter {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private final String TAG = "WindowItemAdapter";

    /*窗帘 开启状态 编码  分为 单点 和多点*/
    public static final int WINDOW_SINGLE_CLOSE = 0;
    public static final int WINDOW_SINGLE_OPEN = 1;

    /*这部分是根据 两点控制的 两个节点状态自定义的*/
    public static final int WINDOW_DOUBLE_CLOSE = -2;
    public static final int WINDOW_DOUBLE_OPEN = -1;
    public static final int WINDOW_DOUBLE_PAUSE = -3;


    public static final int WINDOW_OPEN = 0;

    public static final int WINDOW_CLOSE = 1;
    public static final int WINDOW_PAUSE = 2;

    Gson gson = new Gson();

    public WindowItemAdapter(Context context, RoomInfo roomInfo) {
        super();
        mContext = context;
        mRoomInfo = roomInfo;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mRoomInfo.window_device.size();
    }

    @Override
    public RoomInfo.DeviceInfo getItem(int position) {
        return mRoomInfo.window_device.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.room_control_window_adapter_view, null);
        }
        ((TextView) convertView.findViewById(R.id.window_name)).setText(mRoomInfo.window_device.get(position).name);
        final CheckBox mWindowOn = (CheckBox) convertView.findViewById(R.id.window_control_on);
        final CheckBox mWindowOff = (CheckBox) convertView.findViewById(R.id.window_control_off);
        //获取 控制方式 bug~~~~~~~~~~~~~~~~~
        final RoomInfo.DeviceInfo deviceInfo = mRoomInfo.window_device.get(position);
        String valueStr = String.valueOf(deviceInfo.value);
        final int value = Integer.valueOf(valueStr);
        //根据 节点状态 设置 checkbox 的状态
        setState(mWindowOn, mWindowOff, value);
        Log.i(TAG, "getView: value " + value);
        //设置 点击监听
        mWindowOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击开窗帘按钮
                String valueStr = String.valueOf(deviceInfo.value);
                Integer val = Integer.valueOf(valueStr);
                Log.i(TAG, "onClick: 开窗 " + valueStr);
                switch (val) {
                    case WINDOW_SINGLE_CLOSE://如果是单点控制的关状态 开启
                        deviceInfo.value = WINDOW_SINGLE_OPEN;
                        break;
                    case WINDOW_SINGLE_OPEN://如果是单点控制的 开状态 不做处理
                        break;
                    case WINDOW_DOUBLE_OPEN://如果是 两点控制 的开状态 转为暂定状态
                        deviceInfo.value = WINDOW_DOUBLE_PAUSE;
                        break;
                    case WINDOW_DOUBLE_CLOSE://如果是 两点控制的 关状态 切换关为开 这里的命令 发两条 想停止 关节点 再打开 开节点
                        deviceInfo.value = WINDOW_DOUBLE_OPEN;
                        break;
                    case WINDOW_DOUBLE_PAUSE://如果是 两点控制 暂停状态 直接开启
                        deviceInfo.value = WINDOW_DOUBLE_OPEN;
                        break;
                }
                String newVal=String.valueOf(deviceInfo.value);
                setState(mWindowOn, mWindowOff,Integer.valueOf(newVal));
                sendCmd(deviceInfo);
            }
        });
        mWindowOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueStr = String.valueOf(deviceInfo.value);
                Log.i(TAG, "onClick: 关窗 " + valueStr);
                Integer val = Integer.valueOf(valueStr);
                //点击关窗/窗帘按钮
                switch (val) {
                    case WINDOW_SINGLE_CLOSE://如果是单点控制的关状态 不处理
                        Log.i(TAG, "onClick:  关闭开关  当前值  SINGLECLOSE " +"目标值 singleclose");
                        break;
                    case WINDOW_SINGLE_OPEN://如果是单点控制的 开状态 执行关
                        deviceInfo.value = WINDOW_SINGLE_CLOSE;
                        break;
                    case WINDOW_DOUBLE_OPEN://如果是 两点控制 的开状态 切换 到关状态 这里的命令 发两条 先停止 开节点 再打开 关节点
                        deviceInfo.value = WINDOW_DOUBLE_CLOSE;
                        break;
                    case WINDOW_DOUBLE_CLOSE://如果是 两点控制的 关状态 切换暂停
                        deviceInfo.value = WINDOW_DOUBLE_PAUSE;
                        break;
                    case WINDOW_DOUBLE_PAUSE://如果是 两点控制 暂停状态 直接 执行关闭
                        deviceInfo.value = WINDOW_DOUBLE_CLOSE;
                        break;
                }
                String newVal=String.valueOf(deviceInfo.value);
                setState(mWindowOn, mWindowOff,Integer.valueOf(newVal));
                sendCmd(deviceInfo);
            }
        });

        return convertView;
    }

    private void sendCmd(RoomInfo.DeviceInfo deviceInfo) {
        String tag = null;
        int val = Integer.valueOf(String.valueOf(deviceInfo.value));
        switch (val) {
            case WINDOW_SINGLE_CLOSE:// 单点 停 tag有值
            case WINDOW_SINGLE_OPEN://单点控制 开
                tag = deviceInfo.tag;
                CmdCenter.getInstance(mContext).cWrite(null, tag, deviceInfo.value);
                break;
            case WINDOW_DOUBLE_OPEN: {//两点控制 开
                String descrip = deviceInfo.descrip;
                JsonObject object = gson.fromJson(descrip, JsonObject.class);
                try {
                    String openTag = object.get("open").getAsString();
                    String closeTag = object.get("close").getAsString();
                    CmdCenter.getInstance(mContext).cWrite(null, closeTag, 0);
                    CmdCenter.getInstance(mContext).cWrite(null, openTag, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case WINDOW_DOUBLE_CLOSE: {//两点控制 关
                String descrip = deviceInfo.descrip;
                JsonObject object = gson.fromJson(descrip, JsonObject.class);
                try {
                    String openTag = object.get("open").getAsString();
                    String closeTag = object.get("close").getAsString();
                    CmdCenter.getInstance(mContext).cWrite(null, openTag, 0);
                    CmdCenter.getInstance(mContext).cWrite(null, closeTag, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case WINDOW_DOUBLE_PAUSE: {//两点控制 暂停
                String descrip = deviceInfo.descrip;
                JsonObject object = gson.fromJson(descrip, JsonObject.class);
                try {
                    String openTag = object.get("open").getAsString();
                    String closeTag = object.get("close").getAsString();
                    CmdCenter.getInstance(mContext).cWrite(null, closeTag, 0);
                    CmdCenter.getInstance(mContext).cWrite(null, openTag, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void setState(CheckBox mWindowOpen, CheckBox mWindowClose, int value) {
        switch (value) {
            case WINDOW_SINGLE_CLOSE://单点控制 开状态？ 还是关状态 只有两个状态 按钮有一个必选
                mWindowOpen.setChecked(false);
                mWindowClose.setChecked(true);
                break;
            case WINDOW_SINGLE_OPEN://单点控制 关状态？  还是开状态
                mWindowOpen.setChecked(true);
                mWindowClose.setChecked(false);
                break;
            case WINDOW_DOUBLE_OPEN://两点控制 开启执行  有三个状态 按钮最多选一个
                mWindowOpen.setChecked(true);
                mWindowClose.setChecked(false);
                break;
            case WINDOW_DOUBLE_CLOSE://两点控制 关闭执行
                mWindowOpen.setChecked(false);
                mWindowClose.setChecked(true);
                break;
            case WINDOW_DOUBLE_PAUSE://两点控制 暂停状态
                mWindowOpen.setChecked(false);
                mWindowClose.setChecked(false);
                break;
        }
    }
}
