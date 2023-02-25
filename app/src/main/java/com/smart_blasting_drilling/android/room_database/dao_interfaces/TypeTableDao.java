package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseMineTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseTypeTableEntity;

import java.util.List;

@Dao
public interface TypeTableDao {

    @Insert
    void insertProject(ResponseTypeTableEntity data);

    /*@Query("SELECT * FROM ResponseTypeTableEntity")
    Boolean isExistProject();*/

    @Query("SELECT * FROM ResponseTypeTableEntity")
    List<ResponseTypeTableEntity> getAllBladesProject();

    @Query("DELETE FROM ResponseTypeTableEntity")
    void deleteProjectById();

    @Query("DELETE FROM ResponseTypeTableEntity")
    void deleteAllProject();

    @Query("UPDATE ResponseTypeTableEntity Set TypeData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(ResponseTypeTableEntity data);

}
