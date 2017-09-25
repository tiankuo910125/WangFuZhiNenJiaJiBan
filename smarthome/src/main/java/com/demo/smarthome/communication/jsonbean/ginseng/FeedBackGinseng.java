package com.demo.smarthome.communication.jsonbean.ginseng;

import java.util.List;

/**
 * Created by wangdongyang on 17/3/7.
 */
public class FeedBackGinseng {

    private String content;
    private List<ImgUrl> attaches;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ImgUrl> getAttaches() {
        return attaches;
    }

    public void setAttaches(List<ImgUrl> attaches) {
        this.attaches = attaches;
    }


//    private String content;
//    private String[] attaches;
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String[] getAttaches() {
//        return attaches;
//    }
//
//    public void setAttaches(String[] attaches) {
//        this.attaches = attaches;
//    }


}
