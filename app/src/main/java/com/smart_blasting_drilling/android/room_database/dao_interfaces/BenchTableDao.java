package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseBenchTableEntity;

import java.util.List;

@Dao
public interface BenchTableDao {

    @Insert
    void insertProject(ResponseBenchTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseBenchTableEntity WHERE benchCode = :benchCode)")
    Boolean isExistProject(int benchCode);

    @Query("SELECT * FROM ResponseBenchTableEntity")
    List<ResponseBenchTableEntity> getAllBladesProject();

    @Query("DELETE FROM ResponseBenchTableEntity WHERE benchCode = :benchCode")
    void deleteProjectById(int benchCode);

    @Query("DELETE FROM ResponseBenchTableEntity")
    void deleteAllProject();

    @Update
    void updateProject(ResponseBenchTableEntity data);

}
