package com.demo.smarthome.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.demo.smarthome.R;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.ui.adapter.CurtainItemAdapter;
import com.demo.smarthome.ui.adapter.WindowItemAdapter;
import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomControlWindowView extends RoomControlBaseView {
    private String TAG = "RoomControlWindowView";
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private NotScrollListView mWindowListView;
    private NotScrollListView mCurtainListView;

    private WindowItemAdapter mWindowItemAdapter;
    private CurtainItemAdapter mCurtainItemAdapter;

    public RoomControlWindowView(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_window_view, this);

        mWindowListView = (NotScrollListView) findViewById(R.id.window_list_view);
        mWindowItemAdapter = new WindowItemAdapter(mContext, mRoomInfo);
        mWindowListView.setAdapter(mWindowItemAdapter);

        mCurtainListView = (NotScrollListView) findViewById(R.id.curtain_list_view);
        mCurtainItemAdapter = new CurtainItemAdapter(mContext, mRoomInfo);
        mCurtainListView.setAdapter(mCurtainItemAdapter);
        gson = new Gson();
    }

    private Gson gson;

    @Override
    public void setReceiveData(final ConcurrentHashMap<String, Object> receiveData) {
        super.setReceiveData(receiveData);
        Tasks.executeInBackground(mContext, new BackgroundWork<RoomInfo>() {
                    @Override
                    public RoomInfo doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        /*开窗器 */
                        for (RoomInfo.DeviceInfo info : mRoomInfo.window_device) {
                            if (info.tag == null || info.tag.isEmpty()) {
                        /*两点控制  可能得情况是
                             1、只有 open 节点 执行开关操作  对 open 节点 写 0-1 可执行开关
                             2、open 节点与 close 节点 同时作用 open 1 开启 open 0 暂停开启 close 1 关闭 close 暂停关闭
                             直接用 device value 来区分 本地进行编码
                             */
                                String openTag = null;
                                String closeTag = null;

                                JsonObject object = gson.fromJson(info.descrip, JsonObject.class);
                                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                                    if (entry.getKey().contains("open")) {
                                        openTag = entry.getKey();
                                    }
                                    if (entry.getKey().contains("close")) {
                                        closeTag = entry.getKey();
                                    }
                                }
                                int open;
                                int close;

                                open = getValue(receiveData.get(openTag));
                                close = getValue(receiveData.get(closeTag));

                                Log.i(TAG, "doInBackground: 两点开窗器 " + openTag + " " + open);
                                Log.i(TAG, "doInBackground: 两点开窗器 " + closeTag + " " + close);
                                /*这是为了避免 机智云上传 boolean 类型  true false 0 1 不定的情况*/

                                if (open == 1) {//两点控制状态下 开启执行
                                    info.value = WindowItemAdapter.WINDOW_DOUBLE_OPEN;
                                } else if (close == 1) {//两点控制下 关闭执行 开启关闭互斥
                                    info.value = WindowItemAdapter.WINDOW_DOUBLE_CLOSE;
                                } else {//两个状态都不是1 系统处在暂停状态
                                    info.value = WindowItemAdapter.WINDOW_DOUBLE_PAUSE;
                                }
                            } else {
                                //如果是单点控制 开-关   1-0 可能有 暂停状态
                                Log.i(TAG, "doInBackground: 单点开窗器 tag  " + "tag" + info.tag + "  " + receiveData.get(info.tag));

                                if (getValue(receiveData.get(info.tag)) == 1) {
                                    info.value = WindowItemAdapter.WINDOW_SINGLE_OPEN;
                                } else {
                                    info.value = WindowItemAdapter.WINDOW_SINGLE_CLOSE;
                                }
                            }
                        }
                        /*窗帘*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.curtain_device) {
                            //如果是两点控制
                            if (info.tag == null || info.tag.isEmpty()) {
                        /*两点控制  可能得情况是
                             1、只有 open 节点 执行开关操作  对 open 节点 写 0-1 可执行开关
                             2、open 节点与 close 节点 同时作用 open 1 开启 open 0 暂停开启 close 1 关闭 close 暂停关闭
                             直接用 device value 来区分 本地进行编码
                             */
                                String openTag = null;
                                String closeTag = null;
                                JsonObject object = gson.fromJson(info.descrip, JsonObject.class);
                                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                                    if (entry.getKey().contains("open")) {
                                        openTag = entry.getKey();
                                    }
                                    if (entry.getKey().contains("close")) {
                                        closeTag = entry.getKey();
                                    }
                                }
                                int open;
                                int close;
                                open = getValue(receiveData.get(openTag));
                                close = getValue(receiveData.get(closeTag));
                                Log.i(TAG, "doInBackground: 两点控制设备窗帘 open 节点 " + open);
                                Log.i(TAG, "doInBackground: 两点控制设备窗帘 close 节点 " + close);
                                if (open == 1) {//两点控制状态下 开启执行
                                    info.value = CurtainItemAdapter.CURTAIN_DOUBLE_OPEN;
                                } else if (close == 1) {//两点控制下 关闭执行 开启关闭互斥
                                    info.value = CurtainItemAdapter.CURTAIN_DOUBLE_CLOSE;
                                } else {//两个状态都不是1 系统处在暂停状态
                                    info.value = CurtainItemAdapter.CURTAIN_DOUBLE_PAUSE;
                                }
                            } else {
                                //如果是单点控制 开-关   1-0 3?可能有 暂停状态
                                Log.i(TAG, "doInBackground: 单点开窗器 tag  " + "tag" + info.tag + "  " + receiveData.get(info.tag));
                                if (getValue(receiveData.get(info.tag)) == 1) {
                                    info.value = CurtainItemAdapter.CURTAIN_SINGLE_OPEN;
                                } else {
                                    info.value = CurtainItemAdapter.CURTAIN_SINGLE_CLOSE;
                                }
                            }
                        }
                        return mRoomInfo;
                    }
                }, new Completion<RoomInfo>() {

                    @Override
                    public void onSuccess(Context context, RoomInfo result) {
                        //TODO:更新界面和相关数值
                        mWindowItemAdapter.notifyDataSetChanged();
                        mCurtainItemAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }
        );
    }
}
