package com.demo.smarthome;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.communication.NetServerCommunicationImpl;
import com.demo.smarthome.communication.weather.WeatherBean;
import com.demo.smarthome.ui.adapter.DailyForecastListItemAdapter;
import com.demo.smarthome.ui.base.CircleProgressView;
import com.demo.smarthome.ui.base.LedCharactorView;

import java.util.Calendar;

/**
 * Created by liukun on 2016/3/17.
 */
public class OutsideWeatherActivity extends BaseSmartHomeActivity {

    MyActivityManager mam = MyActivityManager.getInstance();
    private TextView city_name;
    private TextView house_address;

    private String TAF = "OutsideWeatherActivity";
    private String WEEKDAY[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六" };

    private Context mContext;
    private Toolbar mToolbar;

    private NotScrollListView mDailyforecastListView;
    private DailyForecastListItemAdapter mDailyForecastListItemAdapter;

    private LedCharactorView mTemperatureLedCharactorView;
    private LedCharactorView mHumidityLedCharactorView;
    private LedCharactorView mAirqualityLedCharactorView;
    private CircleProgressView mTemperatureCircleProgressView;
    private CircleProgressView mHumidityCircleProgressView;
    private CircleProgressView mAirqualityCircleProgressView;

    private TextView mTodayWeekday;
    private TextView mTodayTempMaxValue;
    private TextView mTodayTempMinValue;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result;
            Bundle data;
            WeatherBean mWeather;
            switch (msg.what) {
                case NetServerCommunicationImpl.MSG_WEATHER_INFO_CALLBACK: {
                    data = msg.getData();
                    result = (String) data.getString("result");
                    Log.i("TAG", "handleMessage: 天气返回的result-->" + result);
                    mWeather = GsonTools.getBean(result, WeatherBean.class); //报解析异常了.....
                    mDailyForecastListItemAdapter = new DailyForecastListItemAdapter(mContext, mWeather);
                    mDailyforecastListView.setAdapter(mDailyForecastListItemAdapter);
                    mTemperatureLedCharactorView.setValue(String.valueOf(mWeather.getNow().getTmp()));
                    mTemperatureCircleProgressView.setValue((int) Float.parseFloat(mWeather.getNow().getTmp()));

                    mHumidityLedCharactorView.setValue(String.valueOf(mWeather.getNow().getHum()));
                    mHumidityCircleProgressView.setValue((int) Float.parseFloat(mWeather.getNow().getHum()));

                    mAirqualityLedCharactorView.setValue(String.valueOf(mWeather.getAqi().getCity().getPm25()));
                    mAirqualityCircleProgressView.setValue((int) Float.parseFloat(mWeather.getAqi().getCity().getPm25()));

                    Calendar c = Calendar.getInstance();
                    c.getTime();
                    mTodayWeekday.setText(WEEKDAY[c.get(Calendar.DAY_OF_WEEK) - 1]);
                    mTodayTempMaxValue.setText("最高：" + mWeather.getDaily_forecast().get(0).getTmp().getMax() + "℃");
                    mTodayTempMinValue.setText("最低：" + mWeather.getDaily_forecast().get(0).getTmp().getMin() + "℃");
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_weather);
        mContext = this;
        mam.pushOneActivity(OutsideWeatherActivity.this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTemperatureLedCharactorView = (LedCharactorView) findViewById(R.id.temperature_led_value);
        mHumidityLedCharactorView = (LedCharactorView) findViewById(R.id.humidity_led_value);
        mAirqualityLedCharactorView = (LedCharactorView) findViewById(R.id.airquality_led_value);

        mTemperatureCircleProgressView = (CircleProgressView) findViewById(R.id.temperature_progress);
        mHumidityCircleProgressView = (CircleProgressView) findViewById(R.id.humidity_progress);
        mAirqualityCircleProgressView = (CircleProgressView) findViewById(R.id.airquality_progress);

        mTodayWeekday = (TextView) findViewById(R.id.today_weekday);
        mTodayTempMaxValue = (TextView) findViewById(R.id.max_temp);
        mTodayTempMinValue = (TextView) findViewById(R.id.min_temp);

        city_name = (TextView) findViewById(R.id.city_name);
        house_address = (TextView) findViewById(R.id.house_address);

        mDailyforecastListView = (NotScrollListView) findViewById(R.id.daily_forecast_list);

        city_name.setText(PreferenceUtil.getString("product_city"));
        house_address.setText(PreferenceUtil.getString("product_address"));


        Log.i("OutsideWeather", "onCreate: getweather city "+PreferenceUtil.getString(Constant.HouseSystemManager.CITY));

        //TODO:获取天气信息
        try {
            NetServerCommunicationImpl.getInstance(
                    this.getApplicationContext())
                    .getWeatherData(mHandler, PreferenceUtil.getString(Constant.HouseSystemManager.CITY));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(OutsideWeatherActivity.this);
    }
}
