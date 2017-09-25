package com.demo.smarthome.communication.jsonbean;

/**
 * Created by wangdongyang on 2017/5/4.
 */

public class OutAccessToken {

    private AccessToken data;
    private String code;
    private String msg;

    public AccessToken getData() {
        return data;
    }

    public void setData(AccessToken data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "OutAccessToken{" +
                "data=" + data +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
