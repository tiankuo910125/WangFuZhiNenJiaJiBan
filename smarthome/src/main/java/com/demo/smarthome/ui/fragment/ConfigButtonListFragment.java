package com.demo.smarthome.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.ui.model.ConfigInfo;
import com.demo.smarthome.R;

@SuppressLint("ValidFragment")
public class ConfigButtonListFragment extends Fragment {
    private String TAG = "ConfigButtonListFragment";
    private Context mContext;

    private ConfigInfo mConfigInfo;
    private boolean bInsideConfig = true;

    private TextView mTitleText;
    private LinearLayout mTemperatureBtn;
    private LinearLayout mAirBtn;
    private LinearLayout mLampBtn;
    private LinearLayout mWaterBtn;
    private LinearLayout mFanBtn;
    private LinearLayout mSecurityBtn;

    public ConfigButtonListFragment() {
    }

    //    public ConfigButtonListFragment(ConfigInfo configInfo) {
//        super();
//        mConfigInfo = configInfo;
//        bInsideConfig = true;
//
//    }
    public ConfigButtonListFragment(ConfigInfo configInfo, boolean inside) {
        super();
        mConfigInfo = configInfo;
        bInsideConfig = inside;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config_button_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitleText = (TextView) this.getView().findViewById(R.id.inside_title);
        mTemperatureBtn = (LinearLayout) this.getView().findViewById(R.id.temperature_btn);
        mAirBtn = (LinearLayout) this.getView().findViewById(R.id.airquality_btn);
        mLampBtn = (LinearLayout) this.getView().findViewById(R.id.lamp_btn);
        mWaterBtn = (LinearLayout) this.getView().findViewById(R.id.water_btn);
        mFanBtn = (LinearLayout) this.getView().findViewById(R.id.fan_btn);
        mSecurityBtn = (LinearLayout) this.getView().findViewById(R.id.security_btn);

        mTemperatureBtn.setOnClickListener(mOnClickListener);
        mAirBtn.setOnClickListener(mOnClickListener);
        mLampBtn.setOnClickListener(mOnClickListener);
        mWaterBtn.setOnClickListener(mOnClickListener);
        mFanBtn.setOnClickListener(mOnClickListener);
        mSecurityBtn.setOnClickListener(mOnClickListener);

        initView();
    }

    private void initView() {
        if (bInsideConfig) {
            if (mConfigInfo.insideStatus.temperature) {
                this.getView().findViewById(R.id.temperature_btn_icon).setBackgroundResource(R.drawable.temp_btn_selected);
                ((TextView) this.getView().findViewById(R.id.temperature_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.temperature_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.temperature_btn_icon).setBackgroundResource(R.drawable.temp_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.temperature_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.temperature_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.insideStatus.air) {
                this.getView().findViewById(R.id.airquality_btn_icon).setBackgroundResource(R.drawable.air_btn_selected);
                ((TextView) this.getView().findViewById(R.id.airquality_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.airquality_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.airquality_btn_icon).setBackgroundResource(R.drawable.air_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.airquality_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.airquality_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.insideStatus.lamp) {
                this.getView().findViewById(R.id.lamp_btn_icon).setBackgroundResource(R.drawable.light_btn_selected);
                ((TextView) this.getView().findViewById(R.id.lamp_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.lamp_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.lamp_btn_icon).setBackgroundResource(R.drawable.light_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.lamp_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.lamp_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.insideStatus.water) {
                this.getView().findViewById(R.id.water_btn_icon).setBackgroundResource(R.drawable.water_btn_selected);
                ((TextView) this.getView().findViewById(R.id.water_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.water_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.water_btn_icon).setBackgroundResource(R.drawable.water_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.water_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.water_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.insideStatus.fan) {
                this.getView().findViewById(R.id.fan_btn_icon).setBackgroundResource(R.drawable.fan_btn_selected);
                ((TextView) this.getView().findViewById(R.id.fan_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.fan_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.fan_btn_icon).setBackgroundResource(R.drawable.fan_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.fan_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.fan_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.insideStatus.security) {
                this.getView().findViewById(R.id.security_btn_icon).setBackgroundResource(R.drawable.temp_btn_selected);
                ((TextView) this.getView().findViewById(R.id.security_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.security_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.security_btn_icon).setBackgroundResource(R.drawable.security_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.security_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.security_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            mTitleText.setText("按动回家按钮时");
        } else {
            if (mConfigInfo.outsideStatus.temperature) {
                this.getView().findViewById(R.id.temperature_btn_icon).setBackgroundResource(R.drawable.temp_btn_selected);
                ((TextView) this.getView().findViewById(R.id.temperature_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.temperature_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.temperature_btn_icon).setBackgroundResource(R.drawable.temp_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.temperature_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.temperature_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.outsideStatus.air) {
                this.getView().findViewById(R.id.airquality_btn_icon).setBackgroundResource(R.drawable.air_btn_selected);
                ((TextView) this.getView().findViewById(R.id.airquality_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.airquality_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.airquality_btn_icon).setBackgroundResource(R.drawable.air_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.airquality_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.airquality_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.outsideStatus.lamp) {
                this.getView().findViewById(R.id.lamp_btn_icon).setBackgroundResource(R.drawable.light_btn_selected);
                ((TextView) this.getView().findViewById(R.id.lamp_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.lamp_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.lamp_btn_icon).setBackgroundResource(R.drawable.light_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.lamp_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.lamp_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.outsideStatus.water) {
                this.getView().findViewById(R.id.water_btn_icon).setBackgroundResource(R.drawable.water_btn_selected);
                ((TextView) this.getView().findViewById(R.id.water_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.water_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.water_btn_icon).setBackgroundResource(R.drawable.water_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.water_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.water_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.outsideStatus.fan) {
                this.getView().findViewById(R.id.fan_btn_icon).setBackgroundResource(R.drawable.fan_btn_selected);
                ((TextView) this.getView().findViewById(R.id.fan_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.fan_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.fan_btn_icon).setBackgroundResource(R.drawable.fan_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.fan_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.fan_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            if (mConfigInfo.outsideStatus.security) {
                this.getView().findViewById(R.id.security_btn_icon).setBackgroundResource(R.drawable.security_btn_selected);
                ((TextView) this.getView().findViewById(R.id.security_btn_text)).setText("AUTO");
                ((TextView) this.getView().findViewById(R.id.security_btn_text)).setTextColor(getResources().getColor(R.color.text_highlight));
            } else {
                this.getView().findViewById(R.id.security_btn_icon).setBackgroundResource(R.drawable.security_btn_nomal);
                ((TextView) this.getView().findViewById(R.id.security_btn_text)).setText("OFF");
                ((TextView) this.getView().findViewById(R.id.security_btn_text)).setTextColor(getResources().getColor(R.color.text_hint));
            }
            mTitleText.setText("按离家按钮时");
        }
    }

    private void updateView() {
        PreferenceUtil.putString("ConfigInfo", GsonTools.GsonToString(mConfigInfo));
        initView();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.temperature_btn: {
                    if (bInsideConfig) {
                        mConfigInfo.insideStatus.temperature = !mConfigInfo.insideStatus.temperature;
                    } else
                        mConfigInfo.outsideStatus.temperature = !mConfigInfo.outsideStatus.temperature;
                    break;
                }
                case R.id.airquality_btn: {
                    if (bInsideConfig) {
                        mConfigInfo.insideStatus.air = !mConfigInfo.insideStatus.air;
                    } else
                        mConfigInfo.outsideStatus.air = !mConfigInfo.outsideStatus.air;
                    break;
                }
                case R.id.lamp_btn: {
                    if (bInsideConfig) {
                        mConfigInfo.insideStatus.lamp = !mConfigInfo.insideStatus.lamp;
                    } else
                        mConfigInfo.outsideStatus.lamp = !mConfigInfo.outsideStatus.lamp;
                    break;
                }
                case R.id.water_btn: {
                    if (bInsideConfig) {
                        mConfigInfo.insideStatus.water = !mConfigInfo.insideStatus.water;
                    } else
                        mConfigInfo.outsideStatus.water = !mConfigInfo.outsideStatus.water;
                    break;
                }
                case R.id.fan_btn: {
                    if (bInsideConfig) {
                        mConfigInfo.insideStatus.fan = !mConfigInfo.insideStatus.fan;
                    } else
                        mConfigInfo.outsideStatus.fan = !mConfigInfo.outsideStatus.fan;
                    break;
                }
                case R.id.security_btn: {
                    if (bInsideConfig) {
                        mConfigInfo.insideStatus.security = !mConfigInfo.insideStatus.security;
                    } else
                        mConfigInfo.outsideStatus.security = !mConfigInfo.outsideStatus.security;
                    break;
                }
            }
            updateView();
        }
    };


}