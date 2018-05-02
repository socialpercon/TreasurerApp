package com.sora.treasurer.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.sora.treasurer.database.dao.ExpenseDao;
import com.sora.treasurer.database.dao.GainDao;
import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.database.entities.GainEntity;

/**
 * Created by Sora on 22/04/2018.
 */
@Database(entities = {ExpenseEntity.class, GainEntity.class }, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase INSTANCE;

    public abstract GainDao gainDao();
    public abstract ExpenseDao expenseDao();

    public static AppDatabase getDatabase(Context ctx) {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(ctx, AppDatabase.class, "treasurer_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }


    public static void destroyInstance(){ INSTANCE = null; }

    public String getDatabaseName(){ return INSTANCE.getDatabaseName(); }
}
