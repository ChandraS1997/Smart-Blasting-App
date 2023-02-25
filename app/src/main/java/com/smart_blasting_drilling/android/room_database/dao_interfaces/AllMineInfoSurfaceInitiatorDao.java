package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.AllMineInfoSurfaceInitiatorEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillAccessoriesInfoAllDataEntity;

import java.util.List;

@Dao
public interface AllMineInfoSurfaceInitiatorDao {
    @Insert
    void insertProject(AllMineInfoSurfaceInitiatorEntity data);

    /*@Query("SELECT * FROM AllMineInfoSurfaceInitiatorEntity")
    Boolean isExistProject();*/

    @Query("SELECT * FROM AllMineInfoSurfaceInitiatorEntity")
    List<AllMineInfoSurfaceInitiatorEntity> getAllBladesProject();

    @Query("DELETE FROM AllMineInfoSurfaceInitiatorEntity")
    void deleteProjectById();

    @Query("DELETE FROM AllMineInfoSurfaceInitiatorEntity")
    void deleteAllProject();

    @Query("UPDATE AllMineInfoSurfaceInitiatorEntity Set MineInfoInitiator=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(AllMineInfoSurfaceInitiatorEntity data);
}
