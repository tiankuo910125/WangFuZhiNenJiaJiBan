package com.demo.smarthome.communication.housemanager;

import java.util.List;

/**
 * Created by liukun on 2016/3/29.
 */
public class HouseBean {
    private List<House> house;

    public void setHouseList(List<House> h)
    {
        this.house = h;
    }

    public List<House> getHouseList()
    {
        return this.house;
    }

    public class House{
        private List<String> rooms ;

        private List<String> rooms_remark ;

        private String remark;

        private String name;

        private String address;

        public void setRooms(List<String> rooms){
            this.rooms = rooms;
        }
        public List<String> getRooms(){
            return this.rooms;
        }
        public void setRoomsRemark(List<String> rooms_remark){
            this.rooms_remark = rooms_remark;
        }
        public List<String> getRoomsRemark(){
            return this.rooms_remark;
        }
        public void setRemark(String remark){
            this.remark = remark;
        }
        public String getRemark(){
            return this.remark;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setAddress(String address){
            this.address = address;
        }
        public String getAddress(){
            return this.address;
        }
    }
}

