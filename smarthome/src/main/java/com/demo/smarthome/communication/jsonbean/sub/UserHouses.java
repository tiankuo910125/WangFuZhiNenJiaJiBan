package com.demo.smarthome.communication.jsonbean.sub;

/**
 * Created by cmcc on 2017-01-04.
 */

public class UserHouses {
    private int user_id;

    private String username;

    private int house_id;

    private String name;

    private int status;

    private String role;

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }
    public int getUser_id(){
        return this.user_id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setHouse_id(int house_id){
        this.house_id = house_id;
    }
    public int getHouse_id(){
        return this.house_id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return this.role;
    }

}
