package com.demo.smarthome.communication.jsonbean.sub;

import java.util.List;

/**
 * Created by cmcc on 2017-01-04.
 */


public class Houses {
    private int id;

    private String name;

    private String country;

    private String state;

    private String city;

    private String address;

    private int status;

    private String role;

    private List<Rooms> rooms ;

    private List<Gateways> gateways ;

    private List<UserHouses> userHouses ;

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
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
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
    public void setRooms(List<Rooms> rooms){
        this.rooms = rooms;
    }
    public List<Rooms> getRooms(){
        return this.rooms;
    }
    public void setGateways(List<Gateways> gateways){
        this.gateways = gateways;
    }
    public List<Gateways> getGateways(){
        return this.gateways;
    }
    public void setUserHouses(List<UserHouses> userHouses){
        this.userHouses = userHouses;
    }
    public List<UserHouses> getUserHouses(){
        return this.userHouses;
    }

    @Override
    public String toString() {
        return "Houses{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", role='" + role + '\'' +
                ", rooms=" + rooms +
                ", gateways=" + gateways +
                ", userHouses=" + userHouses +
                '}';
    }
}