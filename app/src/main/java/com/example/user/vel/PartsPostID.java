package com.example.user.vel;

/*
    This class is an extendable class for the PartsList class
 */

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class PartsPostID
{
    @Exclude
    //ID is passed to this class
    public String PartsPostID;

    //ID will be retrieved and returned back to the point of usage
    public <T extends PartsPostID> T withId(@NonNull final String id)
    {
        this.PartsPostID = id;
        return (T) this;
    }//End <T()
}//End PartsPostID()
