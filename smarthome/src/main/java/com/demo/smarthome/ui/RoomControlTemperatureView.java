package com.demo.smarthome.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.smarthome.R;
import com.demo.smarthome.RoomControlActivity;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.hellocharts.model.Axis;
import com.demo.smarthome.hellocharts.model.Line;
import com.demo.smarthome.hellocharts.model.LineChartData;
import com.demo.smarthome.hellocharts.model.PointValue;
import com.demo.smarthome.hellocharts.model.ValueShape;
import com.demo.smarthome.hellocharts.model.Viewport;
import com.demo.smarthome.hellocharts.view.LineChartView;
import com.demo.smarthome.ui.base.CircleSeekButton;
import com.demo.smarthome.ui.base.LedCharactorView;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by liukun on 2016/3/17.
 */
public class RoomControlTemperatureView extends RoomControlBaseView {
    private String TAG = "RoomControlTemperature";
    private Context mContext;
    private FragmentManager mFragmentManager;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;
    private LineChartView mLineChartView;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 1;
    private int numberOfPoints = 24;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    public static final int COLOR_DIMGRAY = Color.parseColor("#4682b4");
    public static final int COLOR_GRAY = Color.parseColor("#4169e1");
    public static final int[] COLORS = new int[]{COLOR_DIMGRAY, COLOR_GRAY};

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = true;
    private boolean hasLabelForSelected = true;

    private String axisXName = "";
    private String axisYName = "";


    private TextView mCurrentTemp;
    private TextView mCurrentHumidity;
    private ImageButton mTempSwitch;
    private ImageButton mHumiditySwitch;

    private CircleSeekButton mTempCircleSeekBtn;
    private CircleSeekButton mHumidityCircleSeekBtn;

    private LedCharactorView mTemperaureSettingValue;
    private LedCharactorView mHumiditySettingValue;

    private CheckBox mWarmModeBtn;
    private CheckBox mCoolModeBtn;

    private static final int TEMPERATURE_VALUE_CHANGED = 0x01;
    private static final int HUMIDITY_VALUE_CHANGED = 0x02;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int value = data.getInt("value");
            switch (msg.what) {
                case TEMPERATURE_VALUE_CHANGED: {
                    //TODO:发送温度变化命令给网关
                    for (int i = 0; i < mRoomInfo.temperature_i_device.size(); i++) {
                        CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                                mRoomInfo.temperature_i_device.get(i).tag, value * 10);
                        PreferenceUtil.putInt(mRoomInfo.temperature_i_device.get(i).tag, value);
                    }
                    break;
                }
                case HUMIDITY_VALUE_CHANGED: {
                    //TODO:发送温度变化命令给网关
                    for (int i = 0; i < mRoomInfo.humidity_i_device.size(); i++) {
                        CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext.getApplicationContext()).getXpgWifiDevice(),
                                mRoomInfo.humidity_i_device.get(i).tag, value * 10);
                        PreferenceUtil.putInt(mRoomInfo.humidity_i_device.get(i).tag, value);
                    }
                    break;
                }
            }
        }
    };

    public RoomControlTemperatureView(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_temperature_view, this);

        mCurrentTemp = (TextView) findViewById(R.id.temperature_stat);
        mCurrentHumidity = (TextView) findViewById(R.id.humidity_stat);
        mTempCircleSeekBtn = (CircleSeekButton) findViewById(R.id.temperature_circle_btn);
        mHumidityCircleSeekBtn = (CircleSeekButton) findViewById(R.id.humidity_circle_btn);
        mTempSwitch = (ImageButton) findViewById(R.id.temperature_model_switch);
        mHumiditySwitch = (ImageButton) findViewById(R.id.humidity_model_switch);

        mTemperaureSettingValue = (LedCharactorView) findViewById(R.id.temperature_setting_value);
        mTemperaureSettingValue.setValue(String.valueOf(mTempCircleSeekBtn.getValue()) + "C");
        mHumiditySettingValue = (LedCharactorView) findViewById(R.id.humidity_setting_value);
        mHumiditySettingValue.setValue(String.valueOf(mHumidityCircleSeekBtn.getValue()) + "%");
        mWarmModeBtn = (CheckBox) findViewById(R.id.warm_btn);
        mCoolModeBtn = (CheckBox) findViewById(R.id.cool_btn);

        mWarmModeBtn.setChecked(false);
        mCoolModeBtn.setChecked(false);
        mHumiditySwitch.setImageResource(R.drawable.switch_off);
        mTempSwitch.setImageResource(R.drawable.switch_off);
        mHumidityCircleSeekBtn.setValue(0);
        mTempCircleSeekBtn.setValue(0);
        mTempCircleSeekBtn.setMinValue(16);
        mTempCircleSeekBtn.setMaxValue(36);
        mHumidityCircleSeekBtn.setMinValue(0);
        mHumidityCircleSeekBtn.setMaxValue(100);


        mTempCircleSeekBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    /*防止 滑动冲突*/
                    Log.i(TAG, "onTouch: action down");
                    mTempCircleSeekBtn.setParentViewDisallowInterceptTouchEvent(
                            ((ScrollView) ((RoomControlActivity) mContext).findViewById(R.id.scroll_view)), true);

                }
                /*设置 UI 数值*/
                int value = mTempCircleSeekBtn.getValue();
                mTemperaureSettingValue.setValue(value + "℃");

                /*确认设定数值后发送命令*/
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i(TAG, "onTouch: action up");
                    mTempCircleSeekBtn.setParentViewDisallowInterceptTouchEvent(
                            ((ScrollView) ((RoomControlActivity) mContext).findViewById(R.id.scroll_view)), false);
                    for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_i_device) {
                        info.value = value * 10;
                        CmdCenter.getInstance(mContext).cWrite(null, info.tag, value * 10);
                    }
                }
                return false;
            }
        });

//
//        mTempCircleSeekBtn.setOnSeekbarChangeListener(new CircleSeekButton.OnValueChangedListener() {
//            @Override
//            public void OnValueChanged(int value) {
//                //发送 设定数值
//                mTemperaureSettingValue.setValue(value + "℃");
//                Log.i(TAG, "OnValueChanged: temperature value " + value);
//                for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_i_device) {
//                    info.value = value * 10;
//                    CmdCenter.getInstance(mContext).cWrite(null, info.tag, value * 10);
//                }
//            }
//        });

        mHumidityCircleSeekBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    /*防止 滑动冲突*/
                    Log.i(TAG, "onTouch: action down");
                    mHumidityCircleSeekBtn.setParentViewDisallowInterceptTouchEvent(
                            ((ScrollView) ((RoomControlActivity) mContext).findViewById(R.id.scroll_view)), true);

                }
                /*设置 UI 数值*/
                int value = mHumidityCircleSeekBtn.getValue();
                mHumiditySettingValue.setValue(value + "%");

                /*确认设定数值后发送命令*/
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i(TAG, "onTouch: action up");
                    mHumidityCircleSeekBtn.setParentViewDisallowInterceptTouchEvent(
                            ((ScrollView) ((RoomControlActivity) mContext).findViewById(R.id.scroll_view)), false);
                    for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_i_device) {
                        info.value = value * 10;
                        CmdCenter.getInstance(mContext).cWrite(null, info.tag, value * 10);
                    }
                }
                return false;
            }
        });
//        mHumidityCircleSeekBtn.setOnSeekbarChangeListener(new CircleSeekButton.OnValueChangedListener() {
//            @Override
//            public void OnValueChanged(int value) {
//                //发送 设定数值
//                mHumiditySettingValue.setValue(value + "%");
//                Log.i(TAG, "OnValueChanged: humidity value  " + value);
//                for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_i_device) {
//                    info.value = value * 10;
//                    CmdCenter.getInstance(mContext).cWrite(null, info.tag, value * 10);
//                }
//            }
//        });
        temHumListener();
        airModeSet();
        mLineChartView = (LineChartView) findViewById(R.id.temp_histroy_data_chart);
        generateValues();
        generateData();
        // Disable viewpirt recalculations, see toggleCubic() method for more info.
        mLineChartView.setViewportCalculationEnabled(false);
        resetViewport();
    }

    private void temHumListener() {
        mTempSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: temp " + mRoomInfo.temperature_switch.size());
                //判断控制功能是否存在----有数据节点、设备控制节点则按钮可控
                //有数据节点但设备控制节点不存在或都不存在时按钮不可控，提示功能未开通
                if (!checkFunction(mRoomInfo.temperature_switch) || !checkFunction(mRoomInfo.temperature_i_device)){
                    Toast.makeText(mContext, "该功能未开通", Toast.LENGTH_SHORT).show();
                    return;}
                for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_switch) {
                    Log.i(TAG, "onClick: temp " + (getValue(info.value) == 1));
                    Log.i(TAG, "温度开关: temp tag" + info.tag);
                    if (getValue(info.value) == 1) {//如果是开启状态
                        info.value = 0;//关闭
                        mTempSwitch.setImageResource(R.drawable.switch_off);//按钮 UI
                        mTempCircleSeekBtn.setEnabled(false);
                        mTemperaureSettingValue.setValue(0 + "℃");
                        mTempCircleSeekBtn.setValue(0);
                        //只对 目标温度进行设置 这里实际需要 判断空调模式
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.temperature_i_device) {
                            CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 998);//设置 温度目标温度为最高
                        }
                        //对开关节点进行设置
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.temperature_switch) {
                            CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 0);//设置 开关为关
                        }
                    } else {
                        info.value = 1;
                        mTempSwitch.setImageResource(R.drawable.switch_on);
                        mTempCircleSeekBtn.setEnabled(true);
                        mTemperaureSettingValue.setValue(16 + "℃");
                        mTempCircleSeekBtn.setValue(16);

                        //只对 目标温度进行设置 这里实际需要 判断空调模式
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.temperature_i_device) {
                            CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 160);//设置 温度目标温度为较低
                        }
                        //对开关节点进行设置
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.temperature_switch) {
                            CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 1);//设置 开关为开
                        }
                    }
                }
            }
        });

        mHumiditySwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: humidity" + mRoomInfo.humidity_switch.size());
                if (!checkFunction(mRoomInfo.humidity_switch) || !checkFunction(mRoomInfo.humidity_i_device)){
                    Toast.makeText(mContext, "该功能未开通", Toast.LENGTH_SHORT).show();
                    return;}
                for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_switch) {
                    Log.i(TAG, "onClick: humidity " + (getValue(info.value) == 1));
                    if (getValue(info.value) == 1) {//如果是开启状态
                        info.value = 0;//关闭
                        mHumidityCircleSeekBtn.setEnabled(false);
                        mHumiditySettingValue.setValue(0 + "%");
                        mHumidityCircleSeekBtn.setValue(0);
                        //对目标 湿度进行设置  关闭时 应设置为0
                        mHumiditySwitch.setImageResource(R.drawable.switch_off);//按钮 UI
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.temperature_i_device) {
                            CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 0);//设置 湿度目标为0%
                        }
                        //对开关节点进行设置
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.humidity_switch) {
                            CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 0);//设置 开关为关
                        }
                    } else {
                        info.value = 1;
                        mHumidityCircleSeekBtn.setEnabled(true);
                        mHumiditySettingValue.setValue(50 + "%");
                        mHumidityCircleSeekBtn.setValue(50);
                        mHumiditySwitch.setImageResource(R.drawable.switch_on);
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.humidity_i_device) {
                            CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 500);//设置 湿度目标为50%
                        }
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.humidity_switch) {
                            CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 1);//设置 开关为关
                        }

                    }
                }
            }
        });
    }

    private boolean checkFunction(List list) {
        if(list== null || list.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 对空调模式进行设置
     * 两个模式互斥
     */
    private void airModeSet() {
//        判断 目前模型状态
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.warm_btn:
                        mWarmModeBtn.setChecked(true);
                        mCoolModeBtn.setChecked(false);
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.conditioner_pattern_device) {
                            if (getValue(deviceInfo.value) == 0) {//如果现在是 制冷模式
                                deviceInfo.value = 1;
                                CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 1);
                            }
                        }
                        break;
                    case R.id.cool_btn:
                        mWarmModeBtn.setChecked(false);
                        mCoolModeBtn.setChecked(true);
                        for (RoomInfo.DeviceInfo deviceInfo : mRoomInfo.conditioner_pattern_device) {
                            if (getValue(deviceInfo.value) == 1) {//如果现在是 制冷模式
                                deviceInfo.value = 0;
                                CmdCenter.getInstance(mContext).cWrite(null, deviceInfo.tag, 0);
                            }
                        }
                        break;
                }
            }
        };
        mWarmModeBtn.setOnClickListener(listener);
        mCoolModeBtn.setOnClickListener(listener);
    }


    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 40f;
            }
        }
    }

    public void resetViewport(int bottom, int top) {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.bottom = bottom;
        v.top = top;
        v.left = 0;
        v.right = 24;
        mLineChartView.setMaximumViewport(v);
        mLineChartView.setCurrentViewport(v);
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = 50;
        v.left = 0;
        v.right = 24;
        mLineChartView.setMaximumViewport(v);
        mLineChartView.setCurrentViewport(v);
    }

    private void generateData() {
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }
            Line line = new Line(values);
            line.setColor(COLORS[i]).setStrokeWidth(1);
            line.setShape(shape);
            line.setPointRadius(2);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }
        data = new LineChartData(lines);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(axisXName);
                axisY.setName(axisYName);
            }
            data.setAxisXBottom(axisY);
            data.setAxisYLeft(axisX);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChartView.setLineChartData(data);
        mLineChartView.setInteractive(true);
        mLineChartView.setScrollEnabled(false);
        mLineChartView.setZoomEnabled(false);
    }

    @Override
    public void setReceiveData(final ConcurrentHashMap<String, Object> receiveData) {
        super.setReceiveData(receiveData);
        Tasks.executeInBackground(mContext, new BackgroundWork<RoomInfo>() {
                    @Override
                    public RoomInfo doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        Log.i(TAG, "doInBackground: " + TAG);
                        for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_i_device) {
                            info.value = checkValueFormate(info, receiveData);
                        }
                        /*室内当前温度  显示文内实际温度*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_o_device) {
                            info.value = checkValueFormate(info, receiveData);
                        }
                           /*室内当前 湿度 显示室内设定湿度*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_i_device) {
                            info.value = checkValueFormate(info, receiveData);
                        }
                        /*室内当前 湿度 显示室内实际湿度*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_o_device) {
                            info.value = checkValueFormate(info, receiveData);
                        }
                        /*室内空调机器 的开关状态 显示为 温度开关*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_switch) {
                            info.value = checkValueFormate(info, receiveData);
                        }
                        /*这个不知道干什么用的 初步估计为 空调 的制冷制热模式*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.conditioner_pattern_device) {
                            info.value = checkValueFormate(info, receiveData);
                        }
                        /*温度开关 内存赋值 */
                        for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_switch) {
                            info.value = checkValueFormate(info, receiveData);
                        }
                        /*湿度开关 内存赋值*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_switch) {
                            info.value = checkValueFormate(info, receiveData);
                        }
                        return mRoomInfo;
                    }
                }, new Completion<RoomInfo>() {
                    @Override
                    public void onSuccess(Context context, RoomInfo result) {
                        // TODO: 2017/9/5  ui 的变化
                        Log.i(TAG, "onSuccess: " + TAG);
                        //两个标志位 后面 对旋钮赋值时判断是否需要赋值操作
                        boolean temSwitch = false;
                        boolean humSwitch = false;
                        // 温度开关
                        for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_switch) {
                            Log.i("check111", "温度开关onSuccess: " + info.value);
                            if (getValue(info.value) == 1) {
                                temSwitch = true;
                                mTempSwitch.setImageResource(R.drawable.switch_on);
                            } else {
                                temSwitch = false;
                                mTempSwitch.setImageResource(R.drawable.switch_off);
                            }
                        }

                        //湿度开关
                        for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_switch) {
                            Log.i("check111", "湿度开关onSuccess: " + info.value);
                            info.value = checkValueFormate(info, receiveData);
                            if (getValue(info.value) == 1) {//
                                humSwitch = true;
                                mHumiditySwitch.setImageResource(R.drawable.switch_on);
                                mHumidityCircleSeekBtn.setEnabled(true);
                            } else {
                                humSwitch = false;
                                mHumiditySwitch.setImageResource(R.drawable.switch_off);
                                mHumidityCircleSeekBtn.setEnabled(false);
                            }
                        }
                        //温度示数
                        for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_o_device) {
                            Log.i("check111", "温度示数onSuccess: " + info.value);
                            String valueStr = String.valueOf(info.value);
                            float temValue = Integer.valueOf(valueStr);
                            mCurrentTemp.setText((int) (temValue / 10 + 0.5f) + "℃");
                        }
                        if (temSwitch) {//如果 温度开启
                            //温度旋钮
                            for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_i_device) {
                                Log.i(TAG, "温度旋钮onSuccess: " + info.value);
                                String valueStr = String.valueOf(info.value);
                                float temValue = Integer.valueOf(valueStr);
                                mTempCircleSeekBtn.setValue((int) (temValue / 10 + 0.5f));//旋钮示数
                                mTemperaureSettingValue.setValue((int) (temValue / 10 + 0.5f) + "℃");//led 示数
                                mTempCircleSeekBtn.setEnabled(true);
                            }
                        } else {
                            for (RoomInfo.DeviceInfo info : mRoomInfo.temperature_i_device) {
                                Log.i(TAG, "温度旋钮示数onSuccess: " + info.value);
                                mTempCircleSeekBtn.setValue(0);//旋钮示数
                                mTemperaureSettingValue.setValue("0℃");//led 示数
                                mTempCircleSeekBtn.setEnabled(false);
                            }
                        }
                        //湿度示数
                        for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_o_device) {
                            Log.i("check111", "onSuccess: " + info.value);

                            String valueStr = String.valueOf(info.value);
                            float value = Integer.valueOf(valueStr);
                            mCurrentHumidity.setText((int) (value / 10 + 0.5f) + "%");
                        }
                        if (humSwitch) {
                            //湿度旋钮
                            mHumidityCircleSeekBtn.setEnabled(true);
                            for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_i_device) {
                                Log.i(TAG, "onSuccess: " + info.value);
                                String valueStr = String.valueOf(info.value);
                                float humValue = Integer.valueOf(valueStr);
                                mHumidityCircleSeekBtn.setValue((int) (humValue / 10 + 0.5f));
                                mHumiditySettingValue.setValue((int) (humValue / 10 + 0.5f) + "%");
                            }
                        } else {
                            mHumidityCircleSeekBtn.setEnabled(false);
                            for (RoomInfo.DeviceInfo info : mRoomInfo.humidity_i_device) {
                                Log.i(TAG, "onSuccess: " + info.value);
                                mHumidityCircleSeekBtn.setValue(0);
                                mHumiditySettingValue.setValue("0%");
                            }
                        }
                        //空调模式
                        if (temSwitch) {
                            for (RoomInfo.DeviceInfo info : mRoomInfo.conditioner_pattern_device) {
                                Log.i("check111", "onSuccess: " + info.value);
                                info.value = checkValueFormate(info, receiveData);
                                if (getValue(info.value) == 0) {
                                    mCoolModeBtn.setChecked(true);
                                    mWarmModeBtn.setChecked(false);
                                } else {
                                    mCoolModeBtn.setChecked(false);
                                    mWarmModeBtn.setChecked(true);
                                }
                            }
                        } else {
                            mCoolModeBtn.setChecked(false);
                            mWarmModeBtn.setChecked(false);
                        }
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                    }
                }
        );
    }

//    private Object  checkValueFormate(RoomInfo.DeviceInfo info, ConcurrentHashMap<String, Object> receiveData) {
//        String value= String.valueOf(receiveData.get(info.tag));
//        if (value.contains("true")) {
//            return 1;
//        }else if(value.contains("false")){
//            return 0;
//        }else {
//            return info.tag;
//        }
//    }
}
