package com.demo.smarthome.hellocharts.listener;


import com.demo.smarthome.hellocharts.model.PointValue;
import com.demo.smarthome.hellocharts.model.SubcolumnValue;

public interface ComboLineColumnChartOnValueSelectListener extends OnValueDeselectListener {

    public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

    public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value);

}
