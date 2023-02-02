package com.smart_blasting_drilling.android.room_database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.Project2DBladesDao;

@Database(entities = {ResponseBladesRetrieveData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract Project2DBladesDao project2DBladesDao();
}
