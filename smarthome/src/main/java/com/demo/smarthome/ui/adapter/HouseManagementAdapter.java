package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.ui.ModifyActivity;
import com.demo.smarthome.ui.base.NotScrollListView;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

/**
 * Created by liukun on 2016/3/15.
 */
public class HouseManagementAdapter extends BaseAdapter {
    private Gson gson = new Gson();
    private KProgressHUD kProgressHUD;
    private String TAG = "HouseManagementAdapter";

    private Context mContext;
    private List<Houses> mHouseList;
    private int mDefaultHouse;
    private boolean bEditMode = false;
    private LayoutInflater mInflater;


    public HouseManagementAdapter(Context context, List<Houses> house) {
        super();
        mContext = context;
        mHouseList = house;
        mInflater = LayoutInflater.from(mContext);
        kProgressHUD = new KProgressHUD(mContext);
    }

    @Override
    public int getCount() {
        return mHouseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHouseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater
                    .inflate(R.layout.house_list_adapter, null);
        }

        //TODO:从读取房屋和房间信息
        final Houses houseData = mHouseList.get(position);

        //更新UI
        final TextView houseName = (TextView) convertView.findViewById(R.id.house_name);
        TextView houseState = (TextView) convertView.findViewById(R.id.house_state);
        final TextView houseAddr = (TextView) convertView.findViewById(R.id.house_address);

        houseName.setText(houseData.getName());
        houseName.setFocusable(false);
        houseName.setEnabled(false);
        /*UI 显示  当前选择的房屋*/
        if (houseData.getId() == PreferenceUtil.getInt("default_house")) {
            houseState.setText("当前\n显示");
            houseState.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
        } else {
            houseState.setText("设为\n当前");
            houseState.setTextColor(mContext.getResources().getColor(R.color.grown_black));
        }
        houseState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtil.getInt("default_house") == houseData.getId()) {
                    //如果是当前房屋 不做处理
                    return;
                }
                //修改 UI数据 数据修改完成后才通知 UI 变更
                PreferenceUtil.putInt("default_house", houseData.getId());//更改 默认房屋的 id
                PreferenceUtil.putString(Constant.HouseSystemManager.CITY,houseData.getCity());// 设置 天气请求的城市
                PreferenceUtil.putString("product_city",houseData.getCity());//设置显示 的城市
                PreferenceUtil.putString("product_address",houseData.getAddress());//设置显示的 地址
                notifyDataSetChanged();
                //回调
                if (swithcHouseCallback != null) {
                    swithcHouseCallback.onSwitchHouse(houseData.getId());
                }

            }
        });
        houseAddr.setText(houseData.getAddress());
        convertView.findViewById(R.id.house_name_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mHouseNameEditBtn onClicked");
                Intent intent = new Intent();
                intent.setClass(mContext, ModifyActivity.class);
                intent.putExtra("TAG", 1);
                intent.putExtra("ID", houseData.getId() + "");
                mContext.startActivity(intent);
            }
        });

        NotScrollListView mRoomList = (NotScrollListView) convertView.findViewById(R.id.room_list);
        HouseInfoAdapter mHouseInfoAdapter = new HouseInfoAdapter(mContext, houseData.getRooms(), houseData.getName());
        mRoomList.setAdapter(mHouseInfoAdapter);

        return convertView;
    }

    private SwithcHouseCallback swithcHouseCallback;

    public void setSwithcHouseCallback(SwithcHouseCallback swithcHouseCallback) {
        this.swithcHouseCallback = swithcHouseCallback;
    }

    public interface SwithcHouseCallback {
        void onSwitchHouse(int houseId);
    }
}
