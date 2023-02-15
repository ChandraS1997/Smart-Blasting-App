package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;

import java.util.List;

@Dao
public interface PitTableDao {

    @Insert
    void insertProject(ResponsePitTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponsePitTableEntity WHERE pitCode = :pitCode)")
    Boolean isExistProject(int pitCode);

    @Query("SELECT * FROM ResponsePitTableEntity")
    List<ResponsePitTableEntity> getAllBladesProject();

    @Query("DELETE FROM ResponsePitTableEntity WHERE pitCode = :pitCode")
    void deleteProjectById(int pitCode);

    @Query("DELETE FROM ResponsePitTableEntity")
    void deleteAllProject();

    @Update
    void updateProject(ResponsePitTableEntity data);

}
