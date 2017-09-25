package com.demo.smarthome.communication.jsonbean.ginseng;

/**
 * Created by wangdongyang on 17/1/23.
 */
public class BindCodeGinseng {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BindCodeGinseng{" +
                "code='" + code + '\'' +
                '}';
    }
}
