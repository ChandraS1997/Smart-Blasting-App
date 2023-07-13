package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.ResponseBenchTableEntity;

import java.util.List;

@Dao
public interface BenchTableDao {

    @Insert
    void insertProject(ResponseBenchTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseBenchTableEntity)")
    Boolean isExistProject();

    @Query("SELECT * FROM ResponseBenchTableEntity")
    List<ResponseBenchTableEntity> getAllBladesProject();

    @Query("SELECT * FROM ResponseBenchTableEntity WHERE id=:id")
    ResponseBenchTableEntity getAllBladesProject(int id);

    @Query("DELETE FROM ResponseBenchTableEntity")
    void deleteProjectById();

    @Query("DELETE FROM ResponseBenchTableEntity")
    void deleteAllProject();

    @Query("UPDATE ResponseBenchTableEntity Set BenchData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(ResponseBenchTableEntity data);

}
