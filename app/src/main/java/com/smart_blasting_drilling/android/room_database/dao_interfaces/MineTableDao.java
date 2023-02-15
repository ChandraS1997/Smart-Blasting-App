package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseMineTableEntity;

import java.util.List;

@Dao
public interface MineTableDao {

    @Insert
    void insertProject(ResponseMineTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseMineTableEntity WHERE mineCode = :mineCode)")
    Boolean isExistProject(int mineCode);

    @Query("SELECT * FROM ResponseMineTableEntity")
    List<ResponseMineTableEntity> getAllBladesProject();

    @Query("DELETE FROM ResponseMineTableEntity WHERE mineCode = :mineCode")
    void deleteProjectById(int mineCode);

    @Query("DELETE FROM ResponseMineTableEntity")
    void deleteAllProject();

    @Update
    void updateProject(ResponseMineTableEntity data);

}
