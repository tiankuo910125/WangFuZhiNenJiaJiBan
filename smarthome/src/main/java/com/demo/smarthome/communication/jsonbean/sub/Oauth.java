package com.demo.smarthome.communication.jsonbean.sub;

/**
 * Created by cmcc on 2017-01-04.
 */
public class Oauth {
    private String accessToken;

    private int expiresIn;

    private String tokenType;

    private String scope;

    private String refreshToken;

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
    public String getAccessToken(){
        return this.accessToken;
    }
    public void setExpiresIn(int expiresIn){
        this.expiresIn = expiresIn;
    }
    public int getExpiresIn(){
        return this.expiresIn;
    }
    public void setTokenType(String tokenType){
        this.tokenType = tokenType;
    }
    public String getTokenType(){
        return this.tokenType;
    }
    public void setScope(String scope){
        this.scope = scope;
    }
    public String getScope(){
        return this.scope;
    }
    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public String getRefreshToken(){
        return this.refreshToken;
    }

}
