package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.ResponseDrillAccessoriesInfoAllDataEntity;

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

    @Query("UPDATE ResponseDrillAccessoriesInfoAllDataEntity Set FileData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(ResponseDrillAccessoriesInfoAllDataEntity data);

}
