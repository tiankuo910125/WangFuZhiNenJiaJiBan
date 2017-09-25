package com.demo.smarthome.communication.jsonbean.sub;

/**
 * Created by cmcc on 2017-01-04.
 */


public class Gateways {
    private int id;

    private String uuid;

    private String name;

    private String title;

    private String extid;

    private String manufactory;

    private String model;

    private String brand;

    private String production_date;

    private String energy_consumption;

    private String feature;

    private int status;

    private String description;

    private String comment;

    private String holiday;

    private String gateway_ip;

    private String port;

    private String ip;

    private String ipv6;

    private String product_key;

    private int created_at;

    private int updated_at;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setUuid(String uuid){
        this.uuid = uuid;
    }
    public String getUuid(){
        return this.uuid;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setExtid(String extid){
        this.extid = extid;
    }
    public String getExtid(){
        return this.extid;
    }
    public void setManufactory(String manufactory){
        this.manufactory = manufactory;
    }
    public String getManufactory(){
        return this.manufactory;
    }
    public void setModel(String model){
        this.model = model;
    }
    public String getModel(){
        return this.model;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setProduction_date(String production_date){
        this.production_date = production_date;
    }
    public String getProduction_date(){
        return this.production_date;
    }
    public void setEnergy_consumption(String energy_consumption){
        this.energy_consumption = energy_consumption;
    }
    public String getEnergy_consumption(){
        return this.energy_consumption;
    }
    public void setFeature(String feature){
        this.feature = feature;
    }
    public String getFeature(){
        return this.feature;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public String getComment(){
        return this.comment;
    }
    public void setHoliday(String holiday){
        this.holiday = holiday;
    }
    public String getHoliday(){
        return this.holiday;
    }
    public void setGateway_ip(String gateway_ip){
        this.gateway_ip = gateway_ip;
    }
    public String getGateway_ip(){
        return this.gateway_ip;
    }
    public void setPort(String port){
        this.port = port;
    }
    public String getPort(){
        return this.port;
    }
    public void setIp(String ip){
        this.ip = ip;
    }
    public String getIp(){
        return this.ip;
    }
    public void setIpv6(String ipv6){
        this.ipv6 = ipv6;
    }
    public String getIpv6(){
        return this.ipv6;
    }
    public void setCreated_at(int created_at){
        this.created_at = created_at;
    }
    public int getCreated_at(){
        return this.created_at;
    }
    public void setUpdated_at(int updated_at){
        this.updated_at = updated_at;
    }
    public int getUpdated_at(){
        return this.updated_at;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    @Override
    public String toString() {
        return "Gateways{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", extid='" + extid + '\'' +
                ", manufactory='" + manufactory + '\'' +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", production_date='" + production_date + '\'' +
                ", energy_consumption='" + energy_consumption + '\'' +
                ", feature='" + feature + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", comment='" + comment + '\'' +
                ", holiday='" + holiday + '\'' +
                ", gateway_ip='" + gateway_ip + '\'' +
                ", port='" + port + '\'' +
                ", ip='" + ip + '\'' +
                ", ipv6='" + ipv6 + '\'' +
                ", product_key='" + product_key + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
