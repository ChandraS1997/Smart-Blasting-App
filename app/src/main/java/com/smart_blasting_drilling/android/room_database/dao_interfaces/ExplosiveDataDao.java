package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ExplosiveDataEntity;

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

    @Update
    void updateProject(ExplosiveDataEntity data);

}
