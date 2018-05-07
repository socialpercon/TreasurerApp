package com.sora.treasurer.adapters.adapter_models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.utils.DataService;
import com.sora.treasurer.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sora on 22/04/2018.
 */


public class ExpenseArrayAdapterEntity {
    private long ExpenseID;
    private String ExpenseServerID;
    private int ExpenseType;
    private double ExpenseValue;
    private String ExpenseDescription;
    private String ExpenseTags;
    private String ExpenseCategory;
    // TEMPORARY SOFT DELETE
    private boolean Active;
    // BOOLEAN TO CHECK IF DATA IS SYNCED WITH SERVER
    private boolean Updated;
    private long CreatedBy;
    private long DateCreated;
    private long DateModified;

    // FOR LAYOUT
    private boolean ShowDay;
    private boolean ShowMonthYear;

    // MANUALLY CREATED
    public ExpenseArrayAdapterEntity(double ExpenseValue, int ExpenseType, String ExpenseDescription, String ExpenseCategory, String ExpenseTags, long CreatedBy) {
        this.ExpenseValue = ExpenseValue;
        this.ExpenseType = ExpenseType;
        this.ExpenseDescription = ExpenseDescription;
        this.ExpenseCategory = ExpenseCategory;
        this.ExpenseTags = "";
        this.CreatedBy = CreatedBy;
        this.DateCreated = Util.getCurrentDateTimeInMillis();
        this.DateModified = Util.getCurrentDateTimeInMillis();
        this.Active = true;
        this.Updated = false;
    }

    // DATA FROM SERVER
    public ExpenseArrayAdapterEntity(ExpenseEntity expenseEntity, boolean ShowDay, boolean ShowMonthYear) {
        this.ExpenseID = expenseEntity.getExpenseID();
        this.ExpenseServerID = expenseEntity.getExpenseServerID();
        this.ExpenseValue = expenseEntity.getExpenseValue();
        this.ExpenseType = expenseEntity.getExpenseType();
        this.ExpenseDescription = expenseEntity.getExpenseDescription();
        if(expenseEntity.getExpenseCategory() == null) this.ExpenseCategory = DataService.CATEGORY_OTHERS;
        else this.ExpenseCategory = expenseEntity.getExpenseCategory();
        this.ExpenseTags = "";
        this.CreatedBy = expenseEntity.getCreatedBy();
        this.DateCreated = expenseEntity.getDateCreated();
        this.DateModified = expenseEntity.getDateModified();
        this.Active = expenseEntity.getActive();
        this.Updated = expenseEntity.getUpdated();
        this.ShowDay = ShowDay;
        this.ShowMonthYear = ShowMonthYear;
    }

    // Constructor to convert JSON object into a Java class instance
    public ExpenseArrayAdapterEntity(JSONObject object){
        try {
            this.ExpenseServerID = object.getString("ExpenseServerID");
            this.ExpenseValue = Double.valueOf(object.getString("ExpenseValue"));
            this.ExpenseType = Integer.valueOf(object.getString("ExpenseType"));
            this.ExpenseDescription = object.getString("ExpenseDescription");
            this.ExpenseCategory = object.getString("ExpenseCategory");
            this.ExpenseTags = object.getString("ExpenseTags");
            this.CreatedBy = Integer.valueOf(object.getString("CreatedBy"));
            this.DateCreated = Long.valueOf(object.getString("DateCreated"));
            this.DateModified = Long.valueOf(object.getString("DateModified"));
            this.Active = Boolean.valueOf(object.getString("Active"));
            this.Updated = Boolean.valueOf(object.getString("Updated"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void setExpenseServerID(String ExpenseServerID){this.ExpenseServerID = ExpenseServerID; }

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

    public String getExpenseCategory() {
        return this.ExpenseCategory;
    }
    public void setExpenseCategory(String ExpenseCategory){this.ExpenseCategory = ExpenseCategory; }

    public String getExpenseTags() {
        return this.ExpenseTags;
    }
    public void setExpenseTags(String ExpenseTags){this.ExpenseTags = ExpenseTags; }

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

    public boolean getShowDay() { return  this.ShowDay; }
    public void setShowDay(Boolean ShowDay) { this.ShowDay = ShowDay; }

    public boolean getShowMonthYear() { return  this.ShowMonthYear; }
    public void setShowMonthYear(Boolean ShowMonthYear) { this.ShowMonthYear = ShowMonthYear; }


    public JSONObject getJsonObject() {
        final JSONObject obj = new JSONObject();
        try {
            obj.put("ExpenseServerID", getExpenseServerID());
            obj.put("ExpenseType", getExpenseType());
            obj.put("ExpenseValue", getExpenseValue());
            obj.put("ExpenseDescription", getExpenseDescription());
            obj.put("ExpenseCategory",(getExpenseCategory() == null)? DataService.CATEGORY_OTHERS : getExpenseCategory());
            obj.put("Active", getActive());
            obj.put("Updated", getUpdated());
            obj.put("CreatedBy", getCreatedBy());
            obj.put("DateCreated", getDateCreated());
            obj.put("DateModified", getDateModified());
            obj.put("ShowDay", getShowDay());
            obj.put("ShowMonthYear", getShowMonthYear());

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
                "ExpenseCategory: "+ String.valueOf(this.ExpenseCategory) + "," +
                "ExpenseTags: " + String.valueOf(this.ExpenseTags) + "," +
                "Active: " + String.valueOf(this.Active) + "," +
                "CreatedBy: " + String.valueOf(this.CreatedBy) + "," +
                "DateCreated: " + String.valueOf(this.DateCreated) + "," +
                "}";
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<ExpenseArrayAdapterEntity> fromJson(JSONArray jsonObjects) {
        ArrayList<ExpenseArrayAdapterEntity> expenseArrayAdapterEntities = new ArrayList<ExpenseArrayAdapterEntity>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                expenseArrayAdapterEntities.add(new ExpenseArrayAdapterEntity(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return expenseArrayAdapterEntities;
    }

}
