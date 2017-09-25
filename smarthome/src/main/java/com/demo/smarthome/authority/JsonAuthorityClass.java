package com.demo.smarthome.authority;

/**
 * Created by liukun on 2016/9/5.
 */
public class JsonAuthorityClass {
    private String title;

    private boolean device;

    private boolean security;

    private boolean lighting;

    private boolean environment;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setDevice(boolean device){
        this.device = device;
    }
    public boolean getDevice(){
        return this.device;
    }
    public void setSecurity(boolean security){
        this.security = security;
    }
    public boolean getSecurity(){
        return this.security;
    }
    public void setLighting(boolean lighting){
        this.lighting = lighting;
    }
    public boolean getLighting(){
        return this.lighting;
    }
    public void setEnvironment(boolean environment){
        this.environment = environment;
    }
    public boolean getEnvironment(){
        return this.environment;
    }
}
