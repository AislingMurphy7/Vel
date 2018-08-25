package com.example.user.vel;

/*
    This class handles the information that is stored/pulled
    from FireBase. Once the program can make a link between itself
    and FireBase based off the names of the variables below there should
    be no issue of pulling data from the database
 */

public class VehicleLog
{
    //Variables
    private String Make;
    private String Model;
    private String Reg;
    private String Engine;
    private String Password;
    private String Confirmed_Password;
    private String Image;

    //Constructor
    public VehicleLog(String make, String model, String reg, String image, String engine, String password, String confirmed_Password)
    {
        Make = make;
        Model = model;
        Reg = reg;
        Engine = engine;
        Image = image;
        Password = password;
        Confirmed_Password = confirmed_Password;
    }//End VehicleLog()

    //Empty constructor()
    public VehicleLog()
    {

    }//End VehicleLog()

    //Getters and Setters
    public String getEngine()
    {
        return Engine;
    }//End getEngine()

    public void setEngine(String engine)
    {
        Engine = engine;
    }//End setEngine()

    public String getImage()
    {
        return Image;
    }//End getImage()

    public void setImage(String image)
    {
        Image = image;
    }//End setImage()

    public String getMake()
    {
        return Make;
    }//End getMake()

    public void setMake(String make)
    {
        Make = make;
    }//End setMake()

    public String getModel()
    {
        return Model;
    }//End getModel()

    public void setModel(String model)
    {
        Model = model;
    }//End setModel()

    public String getReg()
    {
        return Reg;
    }//End getReg()

    public void setReg(String reg)
    {
        Reg = reg;
    }//End setReg()

    public String getPassword()
    {
        return Password;
    }//End getPassword()

    public void setPassword(String password)
    {
        Password = password;
    }//End setPassword()

    public String getConfirmed_Password()
    {
        return Confirmed_Password;
    }//End getConfirmed_Password()

    public void setConfirmed_Password(String confirmed_Password)
    {
        Confirmed_Password = confirmed_Password;
    }//End setConfirmed_Password()
}//End VehicleLog
