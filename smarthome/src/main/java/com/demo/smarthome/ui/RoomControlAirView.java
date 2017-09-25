package com.demo.smarthome.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.RoomControlActivity;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.communication.devicesmanager.gizwits.CmdCenter;
import com.demo.smarthome.hellocharts.model.Axis;
import com.demo.smarthome.hellocharts.model.Line;
import com.demo.smarthome.hellocharts.model.LineChartData;
import com.demo.smarthome.hellocharts.model.PointValue;
import com.demo.smarthome.hellocharts.model.ValueShape;
import com.demo.smarthome.hellocharts.model.Viewport;
import com.demo.smarthome.hellocharts.view.LineChartView;
import com.demo.smarthome.ui.base.CircleSeekButton;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by liukun on 2016/3/17.
 * 2017.9.5 zhuxiaolong update
 */
public class RoomControlAirView extends RoomControlBaseView {
    private String TAG = "RoomControlAirView";
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private final static int AIR_POLICY_PERFECT = 1;
    private final static int AIR_POLICY_GOOD = 2;
    private final static int AIR_POLICY_NORMAL = 3;
    private final static int AIR_POLICY_UNENABLE = 4;
    private final static int AIR_POLICY_CLOSE = 0;


    private ImageButton mAirSwitch;
    private ImageView mAirStatus;
    private CircleSeekButton mAirCircleSeekBtn;

    private TextView mCurrentPm25;
    private TextView mCurrentCo2;
    private TextView mCurrentHCHO;
    private TextView mCurrentVOC;
    private TextView mCurrentPress;

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


    private static final int AIR_VALUE_CHANGED = 0x01;


    /*这是一个初始化的状态 不应该做获取数值的操作 只设定初值*/
    public RoomControlAirView(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_air_view, this);

        mAirCircleSeekBtn = (CircleSeekButton) findViewById(R.id.air_circle_btn);
        mAirCircleSeekBtn.setMinValue(0);
        mAirCircleSeekBtn.setMaxValue(40);
        mAirStatus = (ImageView) findViewById(R.id.air_status_image_view);

        mAirCircleSeekBtn.setValue(0);

        //旋钮的点击事件 监听用户调整旋钮并设置策略值
        setTouchCircleListener();
        mAirSwitch = (ImageButton) findViewById(R.id.air_control_switch);
        //开关的点击事件
        setAirSwitchListener();

        mCurrentPm25 = (TextView) findViewById(R.id.pm25_value);
        mCurrentCo2 = (TextView) findViewById(R.id.co2_value);
        mCurrentVOC = (TextView) findViewById(R.id.voc_value);
        mCurrentHCHO = (TextView) findViewById(R.id.hcho_value);
        mCurrentPress = (TextView) findViewById(R.id.press_value);

        mLineChartView = (LineChartView) findViewById(R.id.air_histroy_data_chart);
        // Generate some randome values.
        generateValues();
        generateData();
        // Disable viewpirt recalculations, see toggleCubic() method for more info.
        mLineChartView.setViewportCalculationEnabled(false);
        resetViewport();
    }

    private void setAirSwitchListener() {
        mAirSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前 开关状态
                boolean flag = false;
                RoomInfo.DeviceInfo airswitch = null;
                //开关节点
                for (RoomInfo.DeviceInfo info : mRoomInfo.roomAirdevice) {
                    String valueStr = String.valueOf(info.value);
                    airswitch = info;
                }
                flag = getValue(airswitch.value) == 1;
                Log.i(TAG, "onClick: " + flag);
                if (flag) {//如果 目前处在开启状态 执行关闭操作
                    // 设置空气净化策略 0
                    for (RoomInfo.DeviceInfo info : mRoomInfo.airdevice) {// 净化器设备 关闭
                        info.value = AIR_POLICY_CLOSE;
                        CmdCenter.getInstance(mContext).cWrite(null, info.tag, AIR_POLICY_CLOSE);
                        CmdCenter.getInstance(mContext).cWrite(null, info.tag + "_help", 1);
                    }
                    airswitch.value = 0;//开关状态 设置为 关
                    mAirSwitch.setImageResource(R.drawable.switch_off);
                    mAirCircleSeekBtn.setValue(0);
                    mAirCircleSeekBtn.setEnabled(false);
                    mAirStatus.setVisibility(INVISIBLE);
                    //发送开关节点 关命令
                    CmdCenter.getInstance(mContext).cWrite(null, airswitch.tag, 0);
                } else {
                    //设置空气净化策略 为极优状态  策略为
                    for (RoomInfo.DeviceInfo info : mRoomInfo.airdevice) {//净化器 设备
                        info.value = AIR_POLICY_PERFECT;
                        CmdCenter.getInstance(mContext).cWrite(null, info.tag, AIR_POLICY_PERFECT);
                        CmdCenter.getInstance(mContext).cWrite(null, info.tag + "_help", 1);
                    }
                    airswitch.value = 1;//开关 状态 设置为开
                    mAirSwitch.setImageResource(R.drawable.switch_on);
                    mAirCircleSeekBtn.setValue(10);
                    mAirCircleSeekBtn.setEnabled(true);
                    mAirStatus.setVisibility(VISIBLE);
                    mAirStatus.setImageResource(R.drawable.led_charator_perfect);

                    //发送开关节点 开命令
                    CmdCenter.getInstance(mContext).cWrite(null, airswitch.tag, 1);
                }
            }
        });
    }

    private void setCircleListener() {
    }


    private void setTouchCircleListener() {
        mAirCircleSeekBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//当触碰到 旋钮的时候  进制上下滑动
                    Log.i(TAG, "onTouch: action down");
                    mAirCircleSeekBtn.setParentViewDisallowInterceptTouchEvent(
                            ((ScrollView) ((RoomControlActivity) mContext).findViewById(R.id.scroll_view)), true);
                }
                /*根据旋钮随时更改 策略显示 ui 设置*/
                int value = mAirCircleSeekBtn.getValue();
                if (value == 0) {//旋钮关闭
                    mAirStatus.setVisibility(INVISIBLE);
                } else if (value >= 10 && value < 20) {//极优
                    mAirStatus.setVisibility(VISIBLE);
                    mAirStatus.setImageResource(R.drawable.led_charator_perfect);
                } else if (value >= 20 && value < 30) {//优
                    mAirStatus.setVisibility(VISIBLE);
                    mAirStatus.setImageResource(R.drawable.led_charator_good);
                } else if (value >= 30) {//普通
                    mAirStatus.setVisibility(VISIBLE);
                    mAirStatus.setImageResource(R.drawable.led_charator_nomal);
                }


                /*命令设置 只发送命令即可*/
                if (event.getAction() == MotionEvent.ACTION_UP) {//当 完成 滑动事件以后  重新开启 上下滑动监听 并获取当前设置的 净化策略
                    Log.i(TAG, "onTouch: up");
                    value = mAirCircleSeekBtn.getValue();
                    mAirCircleSeekBtn.setParentViewDisallowInterceptTouchEvent(
                            ((ScrollView) ((RoomControlActivity) mContext).findViewById(R.id.scroll_view)), false);
                    if (value == 0) {//旋钮关闭
                        sendAirCmd(AIR_POLICY_CLOSE);
                    } else if (value >= 10 && value < 20) {//极优
                        sendAirCmd(AIR_POLICY_PERFECT);
                    } else if (value >= 20 && value < 30) {//优
                        sendAirCmd(AIR_POLICY_GOOD);
                    } else if (value >= 30) {//普通
                        sendAirCmd(AIR_POLICY_NORMAL);
                    }
                }
                return false;
            }
        });
    }

    private void sendAirCmd(int value) {
        if (value == AIR_POLICY_NORMAL || value == AIR_POLICY_PERFECT
                || value == AIR_POLICY_GOOD || value == AIR_POLICY_CLOSE
                || value == AIR_POLICY_UNENABLE) {//判断是不是合法的命令

            for (RoomInfo.DeviceInfo info : mRoomInfo.airdevice) {
                CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext).getXpgWifiDevice(),
                        info.tag, value);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CmdCenter.getInstance(mContext.getApplicationContext()).cWrite(CmdCenter.getInstance(mContext).getXpgWifiDevice(),
                        info.tag + "_helper", 1);
                Log.i(TAG, "发送 空净策略 " + value);
            }
        }
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
                        //TODO:获取数值
                        //这里原来写的一键策略  但是这是单台控制 不应该用一键策略
                        /*空气净化策略*/
                        setDeviceValue(mRoomInfo.airdevice, receiveData);
                        /*pm25 传感器*/
                        setDeviceValue(mRoomInfo.pm25_o_device, receiveData);
                        /*二氧化碳传感器*/
                        setDeviceValue(mRoomInfo.co2_o_device, receiveData);
                        /*voc 传感器*/
                        setDeviceValue(mRoomInfo.voc_o_device, receiveData);
                        /*甲醛 传感器*/
                        setDeviceValue(mRoomInfo.hcho_o_device, receiveData);
                        /*气压 传感器*/
                        setDeviceValue(mRoomInfo.press_o_device, receiveData);
                        /*空气净化开关switch*/
                        setDeviceValue(mRoomInfo.roomAirdevice, receiveData);
                        /*电子净化器*/
                        setDeviceValue(mRoomInfo.electronicPurifier_device, receiveData);
                        /*这四个是 设置值 展示几乎不需要*/
                        return mRoomInfo;
                    }
                }, new Completion<RoomInfo>() {
                    @Override
                    public void onSuccess(Context context, RoomInfo result) {
                        /*设置 净化器开关状态*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.roomAirdevice) {
                            if (getValue(info.value) == 1) {
                                mAirSwitch.setImageResource(R.drawable.switch_on);
                                mAirCircleSeekBtn.setEnabled(true);
                            } else {
                                mAirSwitch.setImageResource(R.drawable.switch_off);
                                mAirCircleSeekBtn.setEnabled(false);// 开关没有打开的时候 不能旋转策略按钮
                            }
                        }
                        /*设置 空净策略 根据空气净化策略 也要判断 空净控制开关 这是策略代号 不是显示的数值*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.airdevice) {
                            switch (getValue(info.value)) {
                                case AIR_POLICY_CLOSE:
                                case AIR_POLICY_UNENABLE:
                                    mAirCircleSeekBtn.setValue(0);
                                    mAirStatus.setVisibility(INVISIBLE);
                                    mAirSwitch.setImageResource(R.drawable.switch_off);
                                    break;
                                case AIR_POLICY_NORMAL:
                                    mAirCircleSeekBtn.setValue(30);//显示值
                                    mAirStatus.setVisibility(VISIBLE);
                                    mAirStatus.setImageResource(R.drawable.led_charator_nomal);
                                    mAirSwitch.setImageResource(R.drawable.switch_on);
                                    break;
                                case AIR_POLICY_GOOD:
                                    mAirCircleSeekBtn.setValue(20);//显示值
                                    mAirStatus.setVisibility(VISIBLE);
                                    mAirStatus.setImageResource(R.drawable.led_charator_good);
                                    mAirSwitch.setImageResource(R.drawable.switch_on);
                                    break;
                                case AIR_POLICY_PERFECT:
                                    mAirCircleSeekBtn.setValue(10);//显示值
                                    mAirStatus.setVisibility(VISIBLE);
                                    mAirStatus.setImageResource(R.drawable.led_charator_perfect);
                                    mAirSwitch.setImageResource(R.drawable.switch_on);
                                    break;
                            }
                        }

                        /*设置PM传感器值*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.pm25_o_device) {
                            String pmValue = String.valueOf(info.value);
                            mCurrentPm25.setText(pmValue);
                        }
                        /*设置 二氧化碳*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.co2_o_device) {
                            String coValue = String.valueOf(info.value);
                            mCurrentCo2.setText(coValue);
                        }
                        /*设置voc传感器值*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.voc_o_device) {
                            String vocValue = String.valueOf(info.value);
                            mCurrentVOC.setText("" + ((float) getValue(info.value)) / 100);
                        }
                        /*设置  甲醛*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.hcho_o_device) {
                            String hchoValue = String.valueOf(info.value);
                            mCurrentHCHO.setText("" + ((float) getValue(info.value)) / 100);
                        }
                         /*设置  压差*/
                        for (RoomInfo.DeviceInfo info : mRoomInfo.press_o_device) {
                            String pressValue = String.valueOf(info.value);
                            mCurrentPress.setText(pressValue);
                        }
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }
        );
    }

    private void setDeviceValue(List<RoomInfo.DeviceInfo> deviceInfoList, ConcurrentHashMap<String, Object> receiveData) {
        for (RoomInfo.DeviceInfo info : deviceInfoList) {
            if (receiveData.containsKey(info.tag)) {
                Log.i(TAG, "setDeviceValue: tag " + info.tag + " value : " + receiveData.get(info.tag));
                info.value = checkValueFormate(info, receiveData);
            }
        }
    }
}
