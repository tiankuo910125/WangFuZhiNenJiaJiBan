package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.communication.weather.WeatherBean;

/**
 * Created by liukun on 2016/3/21.
 */
public class DailyForecastListItemAdapter extends BaseAdapter {
    private String TAG="DailyForecastListItemAdapter";
    private Context mContext;
    private WeatherBean mWeather;
    private LayoutInflater mInflater;
    public DailyForecastListItemAdapter(Context context, WeatherBean weather) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mWeather = weather;
    }


    @Override
    public int getCount() {
        //判断一下list的数量
        return mWeather.getDaily_forecast()==null ? 0 : mWeather.getDaily_forecast().size();
    }

    @Override
    public Object getItem(int position) {
        return mWeather.getDaily_forecast().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.daily_forecast_item_list_adapter,null);
        }
        ImageView mWeatherIcon = (ImageView)convertView.findViewById(R.id.weather_icon);
        TextView mWeekday = (TextView)convertView.findViewById(R.id.weekday);
        TextView mMaxValue = (TextView)convertView.findViewById(R.id.max_temp);
        TextView mMinValue = (TextView)convertView.findViewById(R.id.min_temp);
        if (mWeather.getDaily_forecast() != null) {
            mWeatherIcon.setBackgroundResource(Constant.getWeatherImg(mContext, Integer.parseInt(mWeather.getDaily_forecast().get(position).getCond().getCode_d())));
            mWeekday.setText(AppUtils.getWeek(mWeather.getDaily_forecast().get(position).getDate()));
            mMaxValue.setText("最高："+mWeather.getDaily_forecast().get(position).getTmp().getMax() + "℃");
            mMinValue.setText("最低："+mWeather.getDaily_forecast().get(position).getTmp().getMin() + "℃");

        }
        return convertView;
    }
}
