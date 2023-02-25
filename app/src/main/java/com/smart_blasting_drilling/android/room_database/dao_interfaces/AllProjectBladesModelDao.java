package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;

import java.util.List;

@Dao
public interface AllProjectBladesModelDao {

    @Insert
    void insertItem(AllProjectBladesModelEntity data);

    @Query("SELECT EXISTS(SELECT * FROM AllProjectBladesModelEntity WHERE DesignId= :designId)")
    Boolean isExistItem(String designId);

    @Query("SELECT * FROM AllProjectBladesModelEntity")
    List<AllProjectBladesModelEntity> getAllEntityDataList();

    @Query("SELECT * FROM AllProjectBladesModelEntity WHERE DesignId= :designId")
    AllProjectBladesModelEntity getSingleItemEntity(String designId);

    @Query("DELETE FROM AllProjectBladesModelEntity")
    void deleteItemById();

    @Query("DELETE FROM AllProjectBladesModelEntity")
    void deleteAllItem();

    @Query("UPDATE AllProjectBladesModelEntity SET Blades=:data, ProjectCode=:projectCode WHERE DesignId = :id")
    void updateItem(String id, String projectCode, String data);

    @Update
    void updateItem(AllProjectBladesModelEntity data);

}
