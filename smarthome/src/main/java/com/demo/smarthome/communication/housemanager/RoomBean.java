package com.demo.smarthome.communication.housemanager;

/**
 * Created by liukun on 2016/3/29.
 */
public class RoomBean {
    private String name;
    private String remark;
    private Integer temperature;
    private Integer humidity;
    private Integer airquality;
    private String time;

    public void setName(String n)
    {
        this.name = n;
    }
    public void setRemark(String rm)
    {
        this.remark = rm;
    }
    public void setTemperature(Integer t)
    {
        this.temperature = t;
    }
    public void setHumidity(Integer h)
    {
        this.humidity = h;
    }
    public void setAirquality(Integer a)
    {
        this.airquality = a;
    }
    public void setTime(String t)
    {
        this.time = t;
    }

    public String getName(){return this.name;}
    public String getRemark(){return this.remark;}
    public Integer getTemperature(){return this.temperature;}
    public Integer getHumidity(){return this.humidity;}
    public Integer getAirquality(){return this.airquality;}
    public String getTime(){return this.time;}
}

