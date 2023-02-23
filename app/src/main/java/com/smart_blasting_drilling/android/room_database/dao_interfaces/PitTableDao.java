package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseMineTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;

import java.util.List;

@Dao
public interface PitTableDao {

    @Insert
    void insertProject(ResponsePitTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponsePitTableEntity)")
    Boolean isExistProject();

    @Query("SELECT * FROM ResponsePitTableEntity")
    List<ResponsePitTableEntity> getAllBladesProject();

    @Query("SELECT * FROM ResponsePitTableEntity WHERE id=:id")
    ResponsePitTableEntity getAllBladesProject(int id);

    @Query("DELETE FROM ResponsePitTableEntity")
    void deleteProjectById();

    @Query("DELETE FROM ResponsePitTableEntity")
    void deleteAllProject();

    @Query("UPDATE ResponsePitTableEntity Set PitData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(ResponsePitTableEntity data);

}
