package com.demo.smarthome.hellocharts.listener;


import com.demo.smarthome.hellocharts.model.SliceValue;

public interface PieChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int arcIndex, SliceValue value);

}
