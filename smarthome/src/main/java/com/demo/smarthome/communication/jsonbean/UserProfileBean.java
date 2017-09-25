package com.demo.smarthome.communication.jsonbean;

import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.communication.jsonbean.sub.Oauth;
import com.demo.smarthome.communication.jsonbean.sub.Info;
import com.demo.smarthome.communication.jsonbean.sub.Profile;

import java.util.List;

/**
 * Created by cmcc on 2017-01-04.
 */

public class UserProfileBean {
    private Profile profile;

    private Info info;

    private List<Houses> houses ;

    private Oauth oauth;

    public void setProfile(Profile profile){
        this.profile = profile;
    }
    public Profile getProfile(){
        return this.profile;
    }
    public void setInfo(Info info){
        this.info = info;
    }
    public Info getInfo(){
        return this.info;
    }
    public void setHouses(List<Houses> houses){
        this.houses = houses;
    }
    public List<Houses> getHouses(){
        return this.houses;
    }
    public void setOauth(Oauth oauth){
        this.oauth = oauth;
    }
    public Oauth getOauth(){
        return this.oauth;
    }
}




