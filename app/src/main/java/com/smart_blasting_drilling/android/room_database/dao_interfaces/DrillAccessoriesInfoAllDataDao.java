package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillAccessoriesInfoAllDataEntity;

import java.util.List;

@Dao
public interface DrillAccessoriesInfoAllDataDao {

    @Insert
    void insertProject(ResponseDrillAccessoriesInfoAllDataEntity data);

   /* @Query("SELECT * FROM ResponseDrillAccessoriesInfoAllDataEntity")
    Boolean isExistProject();*/

    @Query("SELECT * FROM ResponseDrillAccessoriesInfoAllDataEntity")
    List<ResponseDrillAccessoriesInfoAllDataEntity> getAllBladesProject();

    @Query("DELETE FROM ResponseDrillAccessoriesInfoAllDataEntity")
    void deleteProjectById();

    @Query("DELETE FROM ResponseDrillAccessoriesInfoAllDataEntity")
    void deleteAllProject();

    @Update
    void updateProject(ResponseDrillAccessoriesInfoAllDataEntity data);

}
