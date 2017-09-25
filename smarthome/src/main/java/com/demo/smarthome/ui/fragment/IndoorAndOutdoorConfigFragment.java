package com.demo.smarthome.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.smarthome.ui.model.ConfigInfo;
import com.demo.smarthome.R;

@SuppressLint("ValidFragment")
public class IndoorAndOutdoorConfigFragment extends Fragment{
    private String TAG = "IndoorAndOutdoorConfigFragment";
    private Context mContext;

    private ConfigInfo mConfigInfo;

    private ConfigButtonListFragment mIndoorConfigFrame;
    private ConfigButtonListFragment mOutdoorConfigFrame;

    public IndoorAndOutdoorConfigFragment(ConfigInfo configInfo) {
        super();
        mConfigInfo = configInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config_indoor_and_outdoor, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIndoorConfigFrame = new ConfigButtonListFragment(mConfigInfo,true);
        mOutdoorConfigFrame = new ConfigButtonListFragment(mConfigInfo,false);
        getFragmentManager().beginTransaction().add(R.id.inside_config_content, mIndoorConfigFrame).commit();
        getFragmentManager().beginTransaction().add(R.id.outside_config_content, mOutdoorConfigFrame).commit();
    }
}