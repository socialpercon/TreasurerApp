package com.sora.treasurer.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sora.treasurer.database.entities.GainEntity;

import java.util.List;

/**
 * Created by Sora on 22/04/2018.
 */

@Dao
public interface GainDao {
    @Query("Select * from gains")
    List<GainEntity> findAll();

    @Query("Select * from gains where GainID = :GainID")
    List<GainEntity> findByID(long GainID);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    long CreateGain(GainEntity gainEntity);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    int UpdateGain(GainEntity gainEntity);
}
