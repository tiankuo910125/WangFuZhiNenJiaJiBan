package com.demo.smarthome.communication.devicesmanager.gizwits;

import java.util.List;

/**
 * Created by liukun on 2016/4/22.
 */
public class GizwitsDeviceBean {
    private String name;

    private String packetVersion;

    private String protocolType;

    private String product_key;

    private List<Entities> entities ;

    private Ui ui;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setPacketVersion(String packetVersion){
        this.packetVersion = packetVersion;
    }
    public String getPacketVersion(){
        return this.packetVersion;
    }
    public void setProtocolType(String protocolType){
        this.protocolType = protocolType;
    }
    public String getProtocolType(){
        return this.protocolType;
    }
    public void setProduct_key(String product_key){
        this.product_key = product_key;
    }
    public String getProduct_key(){
        return this.product_key;
    }
    public void setEntities(List<Entities> entities){
        this.entities = entities;
    }
    public List<Entities> getEntities(){
        return this.entities;
    }
    public void setUi(Ui ui){
        this.ui = ui;
    }
    public Ui getUi(){
        return this.ui;
    }




    public class Object {
        private String action;

        private List<String> bind ;

        private String perm;

        public void setAction(String action){
            this.action = action;
        }
        public String getAction(){
            return this.action;
        }
        public void setBind(List<String> bind){
            this.bind = bind;
        }
        public List<String> getBind(){
            return this.bind;
        }
        public void setPerm(String perm){
            this.perm = perm;
        }
        public String getPerm(){
            return this.perm;
        }

    }

    public class Elements {
        private boolean boolValue;

        private Object object;

        private String type;

        private String key;

        private String title;

        public void setBoolValue(boolean boolValue){
            this.boolValue = boolValue;
        }
        public boolean getBoolValue(){
            return this.boolValue;
        }
        public void setObject(Object object){
            this.object = object;
        }
        public Object getObject(){
            return this.object;
        }
        public void setType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }
        public void setKey(String key){
            this.key = key;
        }
        public String getKey(){
            return this.key;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }

    }

    public class Sections {
        private List<Elements> elements ;

        public void setElements(List<Elements> elements){
            this.elements = elements;
        }
        public List<Elements> getElements(){
            return this.elements;
        }

    }

    public class Ui {
        private Object object;

        private List<Sections> sections ;

        public void setObject(Object object){
            this.object = object;
        }
        public Object getObject(){
            return this.object;
        }
        public void setSections(List<Sections> sections){
            this.sections = sections;
        }
        public List<Sections> getSections(){
            return this.sections;
        }

        public class Object {
            private int version;

            private boolean showEditButton;

            public void setVersion(int version){
                this.version = version;
            }
            public int getVersion(){
                return this.version;
            }
            public void setShowEditButton(boolean showEditButton){
                this.showEditButton = showEditButton;
            }
            public boolean getShowEditButton(){
                return this.showEditButton;
            }

        }
    }

    public class Position {
        private int byte_offset;

        private String unit;

        private int len;

        private int bit_offset;

        public void setByte_offset(int byte_offset){
            this.byte_offset = byte_offset;
        }
        public int getByte_offset(){
            return this.byte_offset;
        }
        public void setUnit(String unit){
            this.unit = unit;
        }
        public String getUnit(){
            return this.unit;
        }
        public void setLen(int len){
            this.len = len;
        }
        public int getLen(){
            return this.len;
        }
        public void setBit_offset(int bit_offset){
            this.bit_offset = bit_offset;
        }
        public int getBit_offset(){
            return this.bit_offset;
        }

    }

    public class Attrs {
        private String display_name;

        private String name;

        private String data_type;

        private Position position;

        private String type;

        private int id;

        private String desc;

        public void setDisplay_name(String display_name){
            this.display_name = display_name;
        }
        public String getDisplay_name(){
            return this.display_name;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setData_type(String data_type){
            this.data_type = data_type;
        }
        public String getData_type(){
            return this.data_type;
        }
        public void setPosition(Position position){
            this.position = position;
        }
        public Position getPosition(){
            return this.position;
        }
        public void setType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setDesc(String desc){
            this.desc = desc;
        }
        public String getDesc(){
            return this.desc;
        }

    }

    public class Entities {
        private String display_name;

        private List<Attrs> attrs ;

        private String name;

        private int id;

        public void setDisplay_name(String display_name){
            this.display_name = display_name;
        }
        public String getDisplay_name(){
            return this.display_name;
        }
        public void setAttrs(List<Attrs> attrs){
            this.attrs = attrs;
        }
        public List<Attrs> getAttrs(){
            return this.attrs;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }

    }
}
