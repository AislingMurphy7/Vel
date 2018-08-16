package com.example.user.vel;

/*
    This class is an extendable class for the VehicleList class
 */

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class VehiclePostID
{
    @Exclude
    ////ID is passed to this class
    public String VehiclePostID;

    //ID will be retrieved and returned back to the point of usage
    public <T extends VehiclePostID> T withId(@NonNull final String id)
    {
        this.VehiclePostID = id;
        return (T) this;
    }//End <T()
}//End PartsPostID()
