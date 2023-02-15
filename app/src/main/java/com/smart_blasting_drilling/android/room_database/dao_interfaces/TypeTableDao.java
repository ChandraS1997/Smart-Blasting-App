package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseTypeTableEntity;

import java.util.List;

@Dao
public interface TypeTableDao {

    @Insert
    void insertProject(ResponseTypeTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseTypeTableEntity WHERE typeId = :typeId)")
    Boolean isExistProject(int typeId);

    @Query("SELECT * FROM ResponseTypeTableEntity")
    List<ResponseTypeTableEntity> getAllBladesProject();

    @Query("DELETE FROM ResponseTypeTableEntity WHERE typeId = :typeId")
    void deleteProjectById(int typeId);

    @Query("DELETE FROM ResponseTypeTableEntity")
    void deleteAllProject();

    @Update
    void updateProject(ResponseTypeTableEntity data);

}
