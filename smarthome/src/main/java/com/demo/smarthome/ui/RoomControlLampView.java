package com.demo.smarthome.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.demo.smarthome.R;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.ui.adapter.AdjustLampItemAdapter;
import com.demo.smarthome.ui.adapter.LampItemAdapter;
import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomControlLampView extends RoomControlBaseView {
    private String TAG = "RoomControlLampView";
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private NotScrollListView mLampListView;
    private NotScrollListView mAdjustLampListView;

    private LampItemAdapter mLampItemAdapter;
    private AdjustLampItemAdapter mAdjustLampItemAdapter;


    public RoomControlLampView(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_lamp_view, this);

        mLampListView = (NotScrollListView) findViewById(R.id.lamp_list_view);
        mLampItemAdapter = new LampItemAdapter(mContext, mRoomInfo);
        mLampListView.setAdapter(mLampItemAdapter);

        mAdjustLampListView = (NotScrollListView) findViewById(R.id.adjust_lamp_list_view);
        mAdjustLampItemAdapter = new AdjustLampItemAdapter(mContext, mRoomInfo);
        mAdjustLampListView.setAdapter(mAdjustLampItemAdapter);
    }

    @Override
    public void setReceiveData(final ConcurrentHashMap<String, Object> receiveData) {
        super.setReceiveData(receiveData);
        Tasks.executeInBackground(mContext, new BackgroundWork<RoomInfo>() {
                    @Override
                    public RoomInfo doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        //开关类灯
                        for (RoomInfo.DeviceInfo info : mRoomInfo.lighting_device) {
                            Log.i(TAG, "doInBackground: 设置灯状态 "+info.name +" value "+info.value);
                            if (receiveData.containsKey(info.tag)) {
                                info.value = checkValueFormate(info, receiveData);
                            }else {
                                info.value = 0;
                            }
                        }
                        //可变类灯
                        for (RoomInfo.DeviceInfo info : mRoomInfo.adjust_lighting_device) {
                            Log.i(TAG, "doInBackground: 设置灯状态 "+info.tag +" value "+info.value);
                            if (receiveData.containsKey(info.tag)) {
                                info.value = checkValueFormate(info, receiveData);
                            }else {
                                info.value = 0;
                            }
                        }
                        return mRoomInfo;
                    }
                }, new Completion<RoomInfo>() {

                    @Override
                    public void onSuccess(Context context, RoomInfo result) {
                        //TODO:更新界面和相关数值
                        mAdjustLampItemAdapter.notifyDataSetChanged();
                        mLampItemAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }
        );
    }
}
