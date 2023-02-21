package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseFileDetailsTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseTypeTableEntity;

import java.util.List;

@Dao
public interface FileTypeTableDao {

    @Insert
    void insertProject(ResponseFileDetailsTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseFileDetailsTableEntity)")
    Boolean isExistProject();

    @Query("SELECT * FROM ResponseFileDetailsTableEntity")
    List<ResponseFileDetailsTableEntity> getAllBladesProject();

    @Query("DELETE FROM ResponseFileDetailsTableEntity")
    void deleteProjectById();

    @Query("DELETE FROM ResponseFileDetailsTableEntity")
    void deleteAllProject();

    @Query("UPDATE ResponseFileDetailsTableEntity Set FileData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(ResponseFileDetailsTableEntity data);

}
