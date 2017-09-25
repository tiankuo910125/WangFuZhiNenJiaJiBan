package com.demo.smarthome.communication.weather;

import com.demo.smarthome.base.exception.NetException;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.JsonUtils;
import com.demo.smarthome.base.utils.PreferenceUtil;

/**
 * Created by liukun on 2016/2/25.
 */
public class WeatherUtilsImpl {
    private String TAG="WeatherUtilsImpl";


    private static class WeatherUtilsImplHolder {
        static final WeatherUtilsImpl instance = new WeatherUtilsImpl();
    }

    /**
     * 私有的构造函数
     */
    private WeatherUtilsImpl() {

    }

    public static WeatherUtilsImpl getInstance() {
        return WeatherUtilsImplHolder.instance;
    }

    public String getWeather(String cityname) throws NetException {
        String result="";
        if (cityname.contains("市"))
        {
            cityname = cityname.substring(0,cityname.indexOf("市"));
        }

        String url = new String("https://free-api.heweather.com/v5/weather?city="+cityname+"&key="+ Constant.HeWeatherApiKey);
        result = JsonUtils.getJsonByGet(url);
        //delete {"HeWeather data service 3.0":[   && ]}
        result = result.substring(result.indexOf("[")+1);
        result = result.substring(0,result.lastIndexOf("]"));
        PreferenceUtil.putString(Constant.HouseSystemManager.WEATHER, result);
        return  result;
    }

}
