package com.demo.smarthome.hellocharts.formatter;

import com.demo.smarthome.hellocharts.model.SliceValue;

public interface PieChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SliceValue value);
}
