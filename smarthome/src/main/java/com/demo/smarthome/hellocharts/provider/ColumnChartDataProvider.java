package com.demo.smarthome.hellocharts.provider;

import com.demo.smarthome.hellocharts.model.ColumnChartData;

public interface ColumnChartDataProvider {

    public ColumnChartData getColumnChartData();

    public void setColumnChartData(ColumnChartData data);

}
