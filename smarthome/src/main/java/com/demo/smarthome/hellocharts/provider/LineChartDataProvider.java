package com.demo.smarthome.hellocharts.provider;

import com.demo.smarthome.hellocharts.model.LineChartData;

public interface LineChartDataProvider {

    public LineChartData getLineChartData();

    public void setLineChartData(LineChartData data);

}
