package com.example.user.vel;

public class VehicleData
{
    private String deviceTime;
    private String coolantTemperature;
    private String engineLoad;
    private String engineRPM;
    private String intakeAirTemperature;
    private String massAirflowRate;
    private String throttlePosition;

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
    }

    VehicleData()
    {}

    public String getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }

    public String getCoolantTemperature() {
        return coolantTemperature;
    }

    public void setCoolantTemperature(String coolantTemperature) {
        this.coolantTemperature = coolantTemperature;
    }

    public String getEngineLoad() {
        return engineLoad;
    }

    public void setEngineLoad(String engineLoad) {
        this.engineLoad = engineLoad;
    }

    public String getEngineRPM() {
        return engineRPM;
    }

    public void setEngineRPM(String engineRPM) {
        this.engineRPM = engineRPM;
    }

    public String getIntakeAirTemperature() {
        return intakeAirTemperature;
    }

    public void setIntakeAirTemperature(String intakeAirTemperature) {
        this.intakeAirTemperature = intakeAirTemperature;
    }

    public String getMassAirflowRate() {
        return massAirflowRate;
    }

    public void setMassAirflowRate(String massAirflowRate) {
        this.massAirflowRate = massAirflowRate;
    }

    public String getThrottlePosition() {
        return throttlePosition;
    }

    public void setThrottlePosition(String throttlePosition) {
        this.throttlePosition = throttlePosition;
    }
}
