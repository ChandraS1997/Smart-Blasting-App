package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.ResponseMineTableEntity;

import java.util.List;

@Dao
public interface MineTableDao {

    @Insert
    void insertProject(ResponseMineTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseMineTableEntity)")
    Boolean isExistProject();

    @Query("SELECT * FROM ResponseMineTableEntity")
    List<ResponseMineTableEntity> getAllBladesProject();

    @Query("DELETE FROM ResponseMineTableEntity")
    void deleteProjectById();

    @Query("DELETE FROM ResponseMineTableEntity")
    void deleteAllProject();

    @Query("UPDATE ResponseMineTableEntity Set MineData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(ResponseMineTableEntity data);

}
