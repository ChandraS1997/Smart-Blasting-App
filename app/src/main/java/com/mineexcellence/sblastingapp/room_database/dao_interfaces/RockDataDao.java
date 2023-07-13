package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.RockDataEntity;

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

    @Query("UPDATE RockDataEntity Set RockData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(RockDataEntity data);

}
