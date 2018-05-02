package com.sora.treasurer.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.sora.treasurer.enums.ExpenseTypes;
import com.sora.treasurer.utils.Util;

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
    private long CreatedBy;
    private long DateCreated;
    private long DateModified;

    public ExpenseEntity(double ExpenseValue, int ExpenseType,String ExpenseDescription, long CreatedBy) {
        this.ExpenseValue = ExpenseValue;
        this.ExpenseType = ExpenseType;
        this.ExpenseDescription = ExpenseDescription;
        this.CreatedBy = CreatedBy;
        this.DateCreated = Util.getCurrentDateTimeInMillis();
        this.DateModified = Util.getCurrentDateTimeInMillis();
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
}
