package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.ExplosiveDataEntity;

import java.util.List;

@Dao
public interface ExplosiveDataDao {

    @Insert
    void insertProject(ExplosiveDataEntity data);

   /* @Query("SELECT * FROM ExplosiveDataEntity")
    Boolean isExistProject();*/

    @Query("SELECT * FROM ExplosiveDataEntity")
    List<ExplosiveDataEntity> getAllBladesProject();

    @Query("DELETE FROM ExplosiveDataEntity")
    void deleteProjectById();

    @Query("DELETE FROM ExplosiveDataEntity")
    void deleteAllProject();

    @Query("UPDATE ExplosiveDataEntity Set ExplosiveData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(ExplosiveDataEntity data);

}
