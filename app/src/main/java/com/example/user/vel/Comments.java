package com.example.user.vel;

/*
    This class handles the information that is stored/pulled
    from FireBase. Once the program can make a link between itself
    and FireBase based off the names of the variables below there should
    be no issue of pulling data from the database
 */

public class Comments
{
    //Variables
    private String message, user_id;

    //Empty Constructor
    public Comments()
    {

    }//End Comments

    //Constructor
    public Comments(String message, String user_id)
    {
        this.message = message;
        this.user_id = user_id;
    }//End Comments

    //Getters and Setters
    public String getMessage()
    {
        return message;
    }//End getMessage()

    public void setMessage(String message)
    {
        this.message = message;
    }//End SetMessage()

    public String getUser_id()
    {
        return user_id;
    }//End getUser_id()
}//End Comments
