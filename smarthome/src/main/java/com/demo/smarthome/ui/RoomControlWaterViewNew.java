package com.demo.smarthome.ui;

import android.content.Context;
import android.view.LayoutInflater;

import com.demo.smarthome.R;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.base.utils.view_util.CustomListView;
import com.demo.smarthome.ui.adapter.WaterItemAdapter;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomControlWaterViewNew extends RoomControlBaseView {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private CustomListView listView;
    private WaterItemAdapter adapter;

    public RoomControlWaterViewNew(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_water_view_new, this);

        listView = (CustomListView) findViewById(R.id.water_list);
        adapter = new WaterItemAdapter(context, mRoomInfo);
        listView.setAdapter(adapter);
    }

    @Override
    public void setReceiveData(final ConcurrentHashMap<String, Object> receiveData) {
        super.setReceiveData(receiveData);
        Tasks.executeInBackground(mContext, new BackgroundWork<RoomInfo>() {
                    @Override
                    public RoomInfo doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        for (RoomInfo.DeviceInfo info : mRoomInfo.water_device) {
                            if (receiveData.containsKey(info.tag)) {
                                info.value = checkValueFormate(info, receiveData);
                            }
                        }
                        return mRoomInfo;
                    }
                }, new Completion<RoomInfo>() {

                    @Override
                    public void onSuccess(Context context, RoomInfo result) {
                        //TODO:更新界面和相关数值
                        mRoomInfo = result;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }
        );
    }
}
