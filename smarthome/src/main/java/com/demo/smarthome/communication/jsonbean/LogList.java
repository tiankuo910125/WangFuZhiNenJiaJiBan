package com.demo.smarthome.communication.jsonbean;

import java.util.List;

/**
 * Created by wangdongyang on 17/3/10.
 */
public class LogList {

    private String month;
    private String day;
    private String week;
    private List<String> log;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }
}
