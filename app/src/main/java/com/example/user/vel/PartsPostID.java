package com.example.user.vel;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class PartsPostID {

    @Exclude
    public String PartsPostID;

    public <T extends PartsPostID> T withId(@NonNull final String id) {
        this.PartsPostID = id;
        return (T) this;
    }
}
