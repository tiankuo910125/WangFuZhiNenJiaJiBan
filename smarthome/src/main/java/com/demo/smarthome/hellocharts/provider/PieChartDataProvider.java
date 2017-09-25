package com.demo.smarthome.hellocharts.provider;

import com.demo.smarthome.hellocharts.model.PieChartData;

public interface PieChartDataProvider {

    public PieChartData getPieChartData();

    public void setPieChartData(PieChartData data);

}
