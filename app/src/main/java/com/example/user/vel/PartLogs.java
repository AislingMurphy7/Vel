package com.example.user.vel;


import java.util.Date;

public class PartLogs extends PartsPostID{

    private String desc, image_thumb, image_url, user_id;
    private Date timestamp;

    public PartLogs() {

    }

    public PartLogs(String desc, String image_thumb, String image_url, String user_id, Date timestamp) {
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.image_url = image_url;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDesc() {
        return desc;
    }


    public String getImage_thumb() {
        return image_thumb;
    }

    public String getImage_url() {
        return image_url;
    }


    public String getUser_id() {
        return user_id;
    }

}



