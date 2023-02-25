package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseMineTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.TldDataEntity;

import java.util.List;

@Dao
public interface TldDataDao {

    @Insert
    void insertProject(TldDataEntity data);

    /*@Query("SELECT * FROM TldDataEntity")
    Boolean isExistProject();*/

    @Query("SELECT * FROM TldDataEntity")
    List<TldDataEntity> getAllBladesProject();

    @Query("DELETE FROM TldDataEntity")
    void deleteProjectById();

    @Query("DELETE FROM TldDataEntity")
    void deleteAllProject();

    @Query("UPDATE TldDataEntity Set TldData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(TldDataEntity data);

}
