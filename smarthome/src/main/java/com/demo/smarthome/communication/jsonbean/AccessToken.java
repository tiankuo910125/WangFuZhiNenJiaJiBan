package com.demo.smarthome.communication.jsonbean;

/**
 * Created by wangdongyang on 2017/5/4.
 */

public class AccessToken {

    private String accessToken;
    private long expireTime;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}
