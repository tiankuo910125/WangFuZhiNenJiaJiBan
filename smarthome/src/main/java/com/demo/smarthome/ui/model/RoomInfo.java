package com.demo.smarthome.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liukun on 2016/9/14.
 */
public class RoomInfo implements Serializable {
    public String roomName = "";
    public String roomRemark = "";

    //environment
    public List<DeviceInfo> temperature_o_device;
    public List<DeviceInfo> temperature_i_device;
    public List<DeviceInfo> temperature_switch;
    public List<DeviceInfo> humidity_o_device;
    public List<DeviceInfo> humidity_i_device;
    public List<DeviceInfo> humidity_switch;


    public List<DeviceInfo> co2_o_device;
    public List<DeviceInfo> co2_i_device;
    public List<DeviceInfo> pm25_o_device;
    public List<DeviceInfo> pm25_i_device;
    public List<DeviceInfo> hcho_o_device;
    public List<DeviceInfo> hcho_i_device;
    public List<DeviceInfo> voc_o_device;
    public List<DeviceInfo> voc_i_device;
    public List<DeviceInfo> press_o_device;
    //lighting
    public List<DeviceInfo> lighting_device;
    public List<DeviceInfo> adjust_lighting_device;
    public List<DeviceInfo> curtain_device;
    public List<DeviceInfo> window_device;

    //security
    public List<DeviceInfo> camera_device;
    public List<DeviceInfo> alert_device;

    //water
    public List<DeviceInfo> water_control_device;
    public List<DeviceInfo> gas_control_device;
    public List<DeviceInfo> water_purifier_control_device;
    public List<DeviceInfo> heater_control_device;
    public List<DeviceInfo> energy_control_device;

    //fan
    public List<DeviceInfo> fan_device;

    //ElectronicPurifier
    public List<DeviceInfo> electronicPurifier_device;

    public List<DeviceInfo> conditioner_device;//空调
    public List<DeviceInfo> conditioner_pattern_device;//空调模式

    public List<DeviceInfo> water_device;//水电气

    //------------------------新增加的--------------------------------------------------------------
    //一键空气净化策略
    public List<DeviceInfo> akeyairdevice;          //一键空气净化策略
    public List<DeviceInfo> airdevice;              //空气净化策略
    public List<DeviceInfo> akeyligntingdevice;     //一键照明控制
    public List<DeviceInfo> akeywindowcurtaindevice;//一键窗与窗帘
    //public List<DeviceInfo> akeysafedevice;       //一键布放控制
    public List<DeviceInfo> akeytemperaturedevice; //一键温度控制
    public List<DeviceInfo> akeyhumiditydevice;    //一键湿度控制
    public List<DeviceInfo> roomAirdevice;          //房屋空气净化

    public RoomInfo() {
        temperature_o_device = new ArrayList<>();
        temperature_i_device = new ArrayList<>();
        humidity_o_device = new ArrayList<>();
        humidity_i_device = new ArrayList<>();
        co2_o_device = new ArrayList<>();
        co2_i_device = new ArrayList<>();
        pm25_o_device = new ArrayList<>();
        pm25_i_device = new ArrayList<>();
        hcho_o_device = new ArrayList<>();
        voc_o_device = new ArrayList<>();
        hcho_i_device = new ArrayList<>();
        voc_i_device = new ArrayList<>();
        press_o_device = new ArrayList<>();

        lighting_device = new ArrayList<>();
        adjust_lighting_device = new ArrayList<>();
        curtain_device = new ArrayList<>();
        window_device = new ArrayList<>();

        camera_device = new ArrayList<>();
        alert_device = new ArrayList<>();
        water_control_device = new ArrayList<>();
        gas_control_device = new ArrayList<>();
        water_purifier_control_device = new ArrayList<>();
        heater_control_device = new ArrayList<>();
        energy_control_device = new ArrayList<>();
        fan_device = new ArrayList<>();
        electronicPurifier_device = new ArrayList<>();

        conditioner_device = new ArrayList<>();
        conditioner_pattern_device = new ArrayList<>();
        water_device = new ArrayList<>();

        //新增加的 一键空气净化策略
        akeyairdevice = new ArrayList<>();
        airdevice = new ArrayList<>();
        akeyligntingdevice = new ArrayList<>();
        akeywindowcurtaindevice = new ArrayList<>();
        //akeysafedevice=new ArrayList<>();
        akeytemperaturedevice = new ArrayList<>();
        akeyhumiditydevice = new ArrayList<>();
        roomAirdevice = new ArrayList<>();
        temperature_switch=new ArrayList<>();
        humidity_switch=new ArrayList<>();
    }

    public static class DeviceInfo implements Serializable {
        public int category;
        public String name;
        public String tag;
        public String room;
        public String open;
        public String close;
        public Object value=0;
        public String feature;
        public String descrip;

    }

    public List<DeviceInfo> getTemperature_switch() {
        return temperature_switch;
    }

    public void setTemperature_switch(List<DeviceInfo> temperature_switch) {
        this.temperature_switch = temperature_switch;
    }

    public List<DeviceInfo> getHumidity_switch() {
        return humidity_switch;
    }

    public void setHumidity_switch(List<DeviceInfo> humidity_switch) {
        this.humidity_switch = humidity_switch;
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "roomName='" + roomName + '\'' +
                ", roomRemark='" + roomRemark + '\'' +
                ", temperature_o_device=" + temperature_o_device +
                ", temperature_i_device=" + temperature_i_device +
                ", humidity_o_device=" + humidity_o_device +
                ", humidity_i_device=" + humidity_i_device +
                ", co2_o_device=" + co2_o_device +
                ", co2_i_device=" + co2_i_device +
                ", pm25_o_device=" + pm25_o_device +
                ", pm25_i_device=" + pm25_i_device +
                ", hcho_o_device=" + hcho_o_device +
                ", hcho_i_device=" + hcho_i_device +
                ", voc_o_device=" + voc_o_device +
                ", voc_i_device=" + voc_i_device +
                ", press_o_device=" + press_o_device +
                ", lighting_device=" + lighting_device +
                ", adjust_lighting_device=" + adjust_lighting_device +
                ", curtain_device=" + curtain_device +
                ", window_device=" + window_device +
                ", camera_device=" + camera_device +
                ", alert_device=" + alert_device +
                ", water_control_device=" + water_control_device +
                ", gas_control_device=" + gas_control_device +
                ", water_purifier_control_device=" + water_purifier_control_device +
                ", heater_control_device=" + heater_control_device +
                ", energy_control_device=" + energy_control_device +
                ", fan_device=" + fan_device +
                '}';
    }
}
