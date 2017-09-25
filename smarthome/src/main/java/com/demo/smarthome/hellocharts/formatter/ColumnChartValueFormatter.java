package com.demo.smarthome.hellocharts.formatter;

import com.demo.smarthome.hellocharts.model.SubcolumnValue;

public interface ColumnChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SubcolumnValue value);

}
