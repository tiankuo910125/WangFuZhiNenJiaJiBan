package com.demo.smarthome.hellocharts.formatter;


import com.demo.smarthome.hellocharts.model.PointValue;

public interface LineChartValueFormatter {

    public int formatChartValue(char[] formattedValue, PointValue value);
}
