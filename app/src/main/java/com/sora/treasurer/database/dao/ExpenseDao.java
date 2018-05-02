package com.sora.treasurer.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.database.entities.GainEntity;

import java.util.List;

/**
 * Created by Sora on 22/04/2018.
 */

@Dao
public interface ExpenseDao {
    @Query("Select * from expenses order by DateCreated desc")
    List<ExpenseEntity> findAll();

    @Query("Select * from expenses where ExpenseServerID is null order by DateCreated desc")
    List<ExpenseEntity> findAllWithoutServerID();

    @Query("Select * from expenses where ExpenseID = :ExpenseID")
    List<ExpenseEntity> findByID(long ExpenseID);

    @Query("Select distinct ExpenseID from expenses order by ExpenseID desc")
    List<Long> findAllExpenseIDs();

    @Query("Select * from expenses where ExpenseID in (:ExpenseIDs)")
    List<ExpenseEntity> findAllByIds(List<Long> ExpenseIDs);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    long CreateExpense(ExpenseEntity expenseEntity);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int UpdateExpense(ExpenseEntity expenseEntity);

    @Query("Delete from expenses where ExpenseID = :ExpenseID")
    void deleteByID(long ExpenseID);
}
