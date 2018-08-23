package com.example.user.vel;

/*
    This class handles the information that is stored/pulled
    from FireBase. Once the program can make a link between itself
    and FireBase based off the names of the variables below there should
    be no issue of pulling data from the database
 */

public class VehicleLog extends VehiclePostID
{
    //Variables
    private String make, model, reg, image_thumb, image_url, user_id, password, con_password;

    //Empty constructor
    public VehicleLog()
    {

    }//End PostLog()

    //Constructor
    public VehicleLog(String make, String model, String reg, String image_thumb, String image_url, String user_id, String password, String con_password)
    {
        this.model = model;
        this.make = make;
        this.reg = reg;
        this.image_thumb = image_thumb;
        this.image_url = image_url;
        this.user_id = user_id;
        this.password = password;
        this.con_password = con_password;
    }//End PostLog()

    //Getters and setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCon_password() {
        return con_password;
    }

    public void setCon_password(String con_password) {
        this.con_password = con_password;
    }

    public String getMake()
    {
        return make;
    }//End getMake()

    public String getModel()
    {
        return model;
    }//End getModel()

    public String getReg()
    {
        return reg;
    }//End getReg()

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

}//End VehicleLog
