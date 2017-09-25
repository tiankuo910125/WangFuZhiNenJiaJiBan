package com.demo.smarthome.hellocharts.formatter;

import com.demo.smarthome.hellocharts.model.BubbleValue;

public interface BubbleChartValueFormatter {

    public int formatChartValue(char[] formattedValue, BubbleValue value);
}
