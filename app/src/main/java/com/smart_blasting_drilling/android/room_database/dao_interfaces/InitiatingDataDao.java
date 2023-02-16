package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.InitiatingDataEntity;

import java.util.List;

@Dao
public interface InitiatingDataDao {

    @Insert
    void insertProject(InitiatingDataEntity data);

    /*@Query("SELECT * FROM InitiatingDataEntity")
    Boolean isExistProject();*/

    @Query("SELECT * FROM InitiatingDataEntity")
    List<InitiatingDataEntity> getAllBladesProject();

    @Query("DELETE FROM InitiatingDataEntity")
    void deleteProjectById();

    @Query("DELETE FROM InitiatingDataEntity")
    void deleteAllProject();

    @Update
    void updateProject(InitiatingDataEntity data);

}
