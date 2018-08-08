package com.example.user.vel;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class VehiclePostID
{
    @Exclude
    public String VehiclePostID;

    public <T extends VehiclePostID> T withId(@NonNull final String id)
    {
        this.VehiclePostID = id;
        return (T) this;
    }//End <T()
}//End PartsPostID()
