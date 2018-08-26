package com.example.user.vel;

/*
    This class handles the information that is stored/pulled
    from FireBase. Once the program can make a link between itself
    and FireBase based off the names of the variables below there should
    be no issue of pulling data from the database
 */

public class

VehicleData
{
    //Variables
    private String deviceTime;
    private String coolantTemperature;
    private String engineLoad;
    private String engineRPM;
    private String intakeAirTemperature;
    private String massAirflowRate;
    private String throttlePosition;

    //Constructor
    public VehicleData(String  deviceTime, String coolantTemperature, String engineLoad, String engineRPM,
                       String intakeAirTemperature, String massAirflowRate, String throttlePosition)
    {

        this.deviceTime = deviceTime;
        this.coolantTemperature = coolantTemperature;
        this.engineLoad = engineLoad;
        this.engineRPM = engineRPM;
        this.intakeAirTemperature = intakeAirTemperature;
        this.massAirflowRate = massAirflowRate;
        this.throttlePosition = throttlePosition;
    }//End VehicleData()

    //Empty Constructor
    VehicleData()
    {}

    //Getters and setters
    public String getDeviceTime()
    {
        return deviceTime;
    }//End()

    public void setDeviceTime(String deviceTime)
    {
        this.deviceTime = deviceTime;
    }//End()

    public String getCoolantTemperature()
    {
        return coolantTemperature;
    }//End()

    public void setCoolantTemperature(String coolantTemperature)
    {
        this.coolantTemperature = coolantTemperature;
    }//End()

    public String getEngineLoad()
    {
        return engineLoad;
    }//End()

    public void setEngineLoad(String engineLoad)
    {
        this.engineLoad = engineLoad;
    }//End()

    public String getEngineRPM()
    {
        return engineRPM;
    }//End()

    public void setEngineRPM(String engineRPM)
    {
        this.engineRPM = engineRPM;
    }//End()

    public String getIntakeAirTemperature()
    {
        return intakeAirTemperature;
    }//End()

    public void setIntakeAirTemperature(String intakeAirTemperature)
    {
        this.intakeAirTemperature = intakeAirTemperature;
    }//End()

    public String getMassAirflowRate()
    {
        return massAirflowRate;
    }//End()

    public void setMassAirflowRate(String massAirflowRate)
    {
        this.massAirflowRate = massAirflowRate;
    }//End()

    public String getThrottlePosition()
    {
        return throttlePosition;
    }//End()

    public void setThrottlePosition(String throttlePosition)
    {
        this.throttlePosition = throttlePosition;
    }//End()
}//End VehicleData()
