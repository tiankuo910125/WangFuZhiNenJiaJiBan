package com.demo.smarthome.communication.jsonbean.ginseng;

/**
 * Created by wangdongyang on 17/3/9.
 */
public class LogListGinseng {

    private String product_key;
    private int device_id;
    private int page;


    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
