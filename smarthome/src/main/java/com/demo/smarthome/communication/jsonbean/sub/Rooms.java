package com.demo.smarthome.communication.jsonbean.sub;

import java.util.List;

/**
 * Created by cmcc on 2017-01-04.
 */
public class Rooms {
    private int id;

    private String name;

    private int status;

    private List<Categories> categories ;

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
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setCategories(List<Categories> categories){
        this.categories = categories;
    }
    public List<Categories> getCategories(){
        return this.categories;
    }

}
