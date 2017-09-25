package com.demo.smarthome.ui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liukun on 2016/11/10.
 */
public class ConfigInfo {
    public InsideStatus insideStatus = new InsideStatus();
    public OutsideStatus outsideStatus  = new OutsideStatus();
    public List<Temperature> temperatureList= new ArrayList<>();
    public List<Humidity> humidityList= new ArrayList<>();
    public List<Airquality> airqualityList= new ArrayList<>();

    public ConfigInfo() {
        super();
    }

    public class Temperature{
        public String temperature_value="-";
        public String hour="-";
        public String minute="-";
        public int repeat;
        public boolean switch_onoff;
    }

    public class Humidity{
        public String humidity_value="-";
        public String hour="-";
        public String minute="-";
        public int repeat;
        public boolean switch_onoff;
    }

    public class Airquality{
        public String aircontrol="关闭";
        public String hour="-";
        public String minute="-";
        public int repeat;
        public boolean switch_onoff;
    }

    public class InsideStatus{
        public boolean temperature=false;
        public boolean air = false;
        public boolean lamp = false;
        public boolean water =false;
        public boolean fan = false;
        public boolean security = false;
    }

    public class OutsideStatus{
        public boolean temperature=false;
        public boolean air = false;
        public boolean lamp = false;
        public boolean water =false;
        public boolean fan = false;
        public boolean security = false;
    }
}
