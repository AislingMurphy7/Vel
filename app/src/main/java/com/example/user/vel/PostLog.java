package com.example.user.vel;


import java.util.Date;

public class PostLog extends PartsPostID
{

    private String desc, image_thumb, image_url, user_id;
    private Date timestamp;

    //Empty constructor
    public PostLog()
    {

    }//End PostLog()

    //Constructor
    public PostLog(String desc, String image_thumb, String image_url, String user_id, Date timestamp)
    {
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.image_url = image_url;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }//End PostLog()

    //Getters and setters
    public Date getTimestamp()
    {
        return timestamp;
    }//End getTimestamp()

    public String getDesc()
    {
        return desc;
    }//End getDesc()

    public String getImage_thumb()
    {
        return image_thumb;
    }//End getImage_thumb()

    public String getImage_url()
    {
        return image_url;
    }//End getImage_url()

    public String getUser_id()
    {
        return user_id;
    }//End getUser_id()
}//End PostLog



