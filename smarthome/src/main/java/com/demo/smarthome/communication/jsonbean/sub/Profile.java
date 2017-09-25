package com.demo.smarthome.communication.jsonbean.sub;

/**
 * Created by cmcc on 2017-01-04.
 */
public class Profile {
    private String firstName;

    private String avatarPath;

    private String avatarBaseUrl;

    private String locale;

    private String gender;

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public void setAvatarPath(String avatarPath){
        this.avatarPath = avatarPath;
    }
    public String getAvatarPath(){
        return this.avatarPath;
    }
    public void setAvatarBaseUrl(String avatarBaseUrl){
        this.avatarBaseUrl = avatarBaseUrl;
    }
    public String getAvatarBaseUrl(){
        return this.avatarBaseUrl;
    }
    public void setLocale(String locale){
        this.locale = locale;
    }
    public String getLocale(){
        return this.locale;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return this.gender;
    }

}
