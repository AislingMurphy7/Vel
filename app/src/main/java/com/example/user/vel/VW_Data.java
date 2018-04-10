package com.example.user.vel;

public class VW_Data
{
    String Altitude;
    String Bearing;
    String  DEVICE_TIME;
    String  G_calibrated;
    String G_x;
    String G_y;
    String G_z;
    String Horizontal_Dilution_of_Precision;
    String Latitude;
    String Longitude;
    String Engine_Coolant_Temperature;
    String Engine_load;
    String Engine_RPM;
    String GPS_SPEED;
    String GPS_TIME;
    String Intake_Air_Temperature;
    String Mass_Air_Flow_Rate;
    String Throttle_Position_Manifold_Percentage;

    public VW_Data(String Altitude, String Bearing, String  DEVICE_TIME, String  G_calibrated, String G_x,
                   String G_y, String G_z, String  Horizontal_Dilution_of_Precision, String Latitude, String Longitude,
                   String Engine_Coolant_Temperature, String Engine_load, String Engine_RPM, String GPS_SPEED,
                   String GPS_TIME, String Intake_Air_Temperature, String Mass_Air_Flow_Rate,
                   String Throttle_Position_Manifold_Percentage)
    {
        this.Altitude = Altitude;
        this.Bearing = Bearing;
        this.DEVICE_TIME = DEVICE_TIME;
        this.G_calibrated = G_calibrated;
        this.G_x =G_x;
        this.G_y = G_y;
        this.G_z = G_z;
        this.Horizontal_Dilution_of_Precision = Horizontal_Dilution_of_Precision;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Engine_Coolant_Temperature = Engine_Coolant_Temperature;
        this.Engine_load = Engine_load;
        this.Engine_RPM = Engine_RPM;
        this.GPS_SPEED = GPS_SPEED;
        this.GPS_TIME = GPS_TIME;
        this.Intake_Air_Temperature = Intake_Air_Temperature;
        this.Mass_Air_Flow_Rate = Mass_Air_Flow_Rate;
        this.Throttle_Position_Manifold_Percentage = Throttle_Position_Manifold_Percentage;
    }

    VW_Data()
    {}
}
