package com.demo.smarthome.communication.jsonbean.sub;

import java.util.List;

/**
 * Created by cmcc on 2017-01-04.
 */
public class Categories {
    private int id;

    private String name;

    private List<Devices> devices ;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setDevices(List<Devices> devices){
        this.devices = devices;
    }
    public List<Devices> getDevices(){
        return this.devices;
    }

}
