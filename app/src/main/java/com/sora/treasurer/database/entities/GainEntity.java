package com.sora.treasurer.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.sora.treasurer.utils.Util;

/**
 * Created by Sora on 22/04/2018.
 */

@Entity(tableName = "gains", indices = {@Index(value = "GainID", unique = true)})
public class GainEntity {
    @PrimaryKey(autoGenerate = true)
    private long GainID;
    private double GainValue;
    private String GainDescription;
    private long CreatedBy;
    private long DateCreated;
    private long DateModified;

    public GainEntity(double GainValue, String GainDescription, long CreatedBy) {
        this.GainValue = GainValue;
        this.GainDescription = GainDescription;
        this.CreatedBy = CreatedBy;
        this.DateCreated = Util.getCurrentDateTimeInMillis();
        this.DateModified = Util.getCurrentDateTimeInMillis();
    }

    public long getGainID() {
        return this.GainID;
    }
    public void setGainID(long GainID){
        this.GainID = GainID;
    }

    public double getGainValue() {
        return this.GainValue;
    }
    public void setGainValue(long GainValue){
        this.GainValue = GainValue;
    }

    public String getGainDescription() {
        return this.GainDescription;
    }
    public void setGainDescription(String GainDescription){
        this.GainDescription = GainDescription;
    }
    public long getCreatedBy() {
        return this.CreatedBy;
    }
    public void setCreatedBy(long CreatedBy){
        this.CreatedBy = CreatedBy;
    }
    public long getDateCreated() {
        return this.DateCreated;
    }
    public void setDateCreated(long DateCreated){
        this.DateCreated = DateCreated;
    }
    public long getDateModified() {
        return this.DateModified;
    }
    public void setDateModified(long DateModified){
        this.DateModified = DateModified;
    }
}
