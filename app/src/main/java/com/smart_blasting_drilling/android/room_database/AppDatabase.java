package com.smart_blasting_drilling.android.room_database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.smart_blasting_drilling.android.room_database.dao_interfaces.Project2DBladesDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.Project3DBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.Project2DBladesEntity;
import com.smart_blasting_drilling.android.room_database.entities.Project3DBladesEntity;

@Database(entities = {Project2DBladesEntity.class, Project3DBladesEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract Project2DBladesDao project2DBladesDao();
    public abstract Project3DBladesDao project3DBladesDao();
}
