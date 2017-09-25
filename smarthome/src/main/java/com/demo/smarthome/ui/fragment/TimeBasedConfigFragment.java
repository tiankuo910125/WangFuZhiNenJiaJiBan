package com.demo.smarthome.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.demo.smarthome.ui.adapter.ConfigResultListAdapter;
import com.google.gson.Gson;
import com.demo.smarthome.R;
import com.demo.smarthome.TimeSelectActivity;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.ui.model.ConfigInfo;

@SuppressLint("ValidFragment")
public class TimeBasedConfigFragment extends Fragment{
    private String TAG = "TimeBasedConfigFragment";
    private Context mContext;

    private NotScrollListView mItemList;
    private ConfigResultListAdapter mConfigResultListAdapter;

    private TextView mTitle;
    private ImageButton mEditBtn;
    private ImageButton mAddBtn;
    private TextView mEditBtnName;

    private boolean bEditMode = false;

    private int mType;
    private ConfigInfo mConfigInfo;

    public TimeBasedConfigFragment(ConfigInfo configInfo, int configType) {
        mConfigInfo = configInfo;
        mType = configType;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config_timebased, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Gson gson = new Gson();
        try {
            mConfigInfo = gson.fromJson(PreferenceUtil.getString("ConfigInfo", ""), ConfigInfo.class);
        }catch (NullPointerException e){
            e.printStackTrace();
            mConfigInfo = new ConfigInfo();
        }
        if (mConfigInfo == null) mConfigInfo = new ConfigInfo();
        mConfigResultListAdapter = new ConfigResultListAdapter(mContext,mConfigInfo,mType);
        mItemList.setAdapter(mConfigResultListAdapter);

        if (mConfigResultListAdapter.getCount() == 0){
            this.getView().findViewById(R.id.tips).setVisibility(View.VISIBLE);
        }else {
            this.getView().findViewById(R.id.tips).setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTitle = (TextView)this.getView().findViewById(R.id.title);
        switch (mType){
            case ConfigResultListAdapter.TYPE_TEMPERATURE:{
                mTitle.setText("按时间开启温度设置");
                break;
            }
            case ConfigResultListAdapter.TYPE_HUMIDITY:{
                mTitle.setText("按时间开启湿度设置");
                break;
            }
            case ConfigResultListAdapter.TYPE_AIRQUALITY:{
                mTitle.setText("按时间开启空气设置");
                break;
            }
        }

        mItemList = (NotScrollListView)this.getView().findViewById(R.id.config_temp_list);


        mEditBtn = (ImageButton)this.getView().findViewById(R.id.btn_1);
        mEditBtnName = (TextView)this.getView().findViewById(R.id.btn_1_name);
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bEditMode){
                    bEditMode = true;
                    mConfigResultListAdapter.setEditMode(true);
                    mConfigResultListAdapter.notifyDataSetChanged();
                    mEditBtn.setBackgroundResource(R.drawable.ok_btn);
                    mEditBtnName.setText("完成");
                }else {
                    bEditMode = false;
                    mConfigResultListAdapter.setEditMode(false);
                    mConfigResultListAdapter.notifyDataSetChanged();
                    if (mConfigResultListAdapter.getCount() == 0){
                        getActivity().findViewById(R.id.tips).setVisibility(View.VISIBLE);
                    }else {
                        getActivity().findViewById(R.id.tips).setVisibility(View.GONE);
                    }
                    mEditBtn.setBackgroundResource(R.drawable.edit_btn);
                    mEditBtnName.setText("编辑");
                }
            }
        });

        mAddBtn = (ImageButton)this.getView().findViewById(R.id.btn_2);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, TimeSelectActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                mIntent.putExtra("Type", mType);
                mIntent.putExtra("position", -1);
                startActivity(mIntent);
            }
        });
    }

}