package com.sora.treasurer.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.google.gson.JsonObject;
import com.sora.treasurer.enums.ExpenseTypes;
import com.sora.treasurer.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Sora on 22/04/2018.
 */

@Entity(tableName = "expenses", indices = {@Index(value = "ExpenseID", unique = true)})
public class ExpenseEntity {
    @PrimaryKey(autoGenerate = true)
    private long ExpenseID;
    private String ExpenseServerID;
    private int ExpenseType;
    private double ExpenseValue;
    private String ExpenseDescription;
    // TEMPORARY SOFT DELETE
    private boolean Active;
    // BOOLEAN TO CHECK IF DATA IS SYNCED WITH SERVER
    private boolean Updated;
    private long CreatedBy;
    private long DateCreated;
    private long DateModified;

    // MANUALLY CREATED
    public ExpenseEntity(double ExpenseValue, int ExpenseType,String ExpenseDescription, long CreatedBy) {
        this.ExpenseValue = ExpenseValue;
        this.ExpenseType = ExpenseType;
        this.ExpenseDescription = ExpenseDescription;
        this.CreatedBy = CreatedBy;
        this.DateCreated = Util.getCurrentDateTimeInMillis();
        this.DateModified = Util.getCurrentDateTimeInMillis();
        this.Active = true;
        this.Updated = false;
    }

    // DATA FROM SERVER
    public ExpenseEntity(ExpenseEntity expenseEntity) {
        this.ExpenseServerID = expenseEntity.ExpenseServerID;
        this.ExpenseValue = expenseEntity.ExpenseValue;
        this.ExpenseType = expenseEntity.ExpenseType;
        this.ExpenseDescription = expenseEntity.ExpenseDescription;
        this.CreatedBy = expenseEntity.CreatedBy;
        this.DateCreated = expenseEntity.DateCreated;
        this.DateModified = expenseEntity.DateModified;
        this.Active = expenseEntity.Active;
        this.Updated = true;
    }

    public long getExpenseID() {
        return this.ExpenseID;
    }
    public void setExpenseID(long ExpenseID){
        this.ExpenseID = ExpenseID;
    }

    public String getExpenseServerID() {
        return this.ExpenseServerID;
    }
    public void setExpenseServerID(String ExpenseServerID){
        this.ExpenseServerID = ExpenseServerID;
    }

    public int getExpenseType() {
        return this.ExpenseType;
    }
    public void setExpenseType(int ExpenseType){
        this.ExpenseType = ExpenseType;
    }

    public double getExpenseValue() {
        return this.ExpenseValue;
    }
    public void setExpenseValue(long ExpenseValue){
        this.ExpenseValue = ExpenseValue;
    }

    public String getExpenseDescription() {
        return this.ExpenseDescription;
    }
    public void setExpenseDescription(String ExpenseDescription){
        this.ExpenseDescription = ExpenseDescription;
    }
    public boolean getActive() {
        return this.Active;
    }
    public void setActive(boolean Active){
        this.Active = Active;
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
        return this.DateCreated;
    }
    public void setDateModified(long DateModified){
        this.DateModified = DateModified;
    }

    public boolean getUpdated() { return  this.Updated; }
    public void setUpdated(Boolean Updated) { this.Updated = Updated; }

    public void UpdateEntity(ExpenseEntity expenseEntity) {
        this.ExpenseServerID = expenseEntity.ExpenseServerID;
        this.ExpenseValue = expenseEntity.ExpenseValue;
        this.ExpenseType = expenseEntity.ExpenseType;
        this.ExpenseDescription = expenseEntity.ExpenseDescription;
        this.CreatedBy = expenseEntity.CreatedBy;
        this.DateCreated = expenseEntity.DateCreated;
        this.DateModified = expenseEntity.DateModified;
        this.Active = expenseEntity.Active;
        this.Updated = true;
    }

    public JSONObject getJsonObject() {
        final JSONObject obj = new JSONObject();
        try {
            obj.put("ExpenseServerID", getExpenseServerID());
            obj.put("ExpenseType", getExpenseType());
            obj.put("ExpenseValue", getExpenseValue());
            obj.put("ExpenseDescription", getExpenseDescription());
            obj.put("Active", getActive());
            obj.put("Updated", getUpdated());
            obj.put("CreatedBy", getCreatedBy());
            obj.put("DateCreated", getDateCreated());
            obj.put("DateModified", getDateModified());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public String toString() {
        return "{" +
                "ExpenseID: " + String.valueOf(this.ExpenseID) + "," +
                "ExpenseServerID: " + String.valueOf(this.ExpenseServerID) + "," +
                "ExpenseType: " + String.valueOf(this.ExpenseType) + "," +
                "ExpenseDescription: " + String.valueOf(this.ExpenseDescription) + "," +
                "Active: " + String.valueOf(this.Active) + "," +
                "CreatedBy: " + String.valueOf(this.CreatedBy) + "," +
                "DateCreated: " + String.valueOf(this.DateCreated) + "," +
                "}";
    }
}
