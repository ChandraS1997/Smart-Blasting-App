package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.RockDataEntity;

import java.util.List;

@Dao
public interface RockDataDao {

    @Insert
    void insertProject(RockDataEntity data);

   /* @Query("SELECT * FROM RockDataEntity")
    Boolean isExistProject();*/

    @Query("SELECT * FROM RockDataEntity")
    List<RockDataEntity> getAllBladesProject();

    @Query("DELETE FROM RockDataEntity")
    void deleteProjectById();

    @Query("DELETE FROM RockDataEntity")
    void deleteAllProject();

    @Update
    void updateProject(RockDataEntity data);

}
