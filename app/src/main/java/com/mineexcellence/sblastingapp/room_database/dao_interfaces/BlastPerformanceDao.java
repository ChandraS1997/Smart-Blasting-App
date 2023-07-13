package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.BlastPerformanceEntity;

import java.util.List;

@Dao
public interface BlastPerformanceDao {
    @Insert
    void insertItem(BlastPerformanceEntity data);

    @Query("SELECT EXISTS(SELECT * FROM BlastPerformanceEntity WHERE DesignId = :DesignId)")
    Boolean isExistItem(String DesignId);

    @Query("SELECT * FROM BlastPerformanceEntity")
    List<BlastPerformanceEntity> getAllEntityDataList();

    @Query("SELECT * FROM BlastPerformanceEntity WHERE DesignId = :designId")
    BlastPerformanceEntity getSingleItemEntity(String designId);

    @Query("DELETE FROM BlastPerformanceEntity WHERE DesignId = :designId")
    void deleteItemById(String designId);

    @Query("DELETE FROM BlastPerformanceEntity")
    void deleteAllItem();

    @Query("UPDATE BlastPerformanceEntity SET BlastPerformance=:data WHERE DesignId = :DesignId")
    void updateItem(String DesignId, String data);

    @Update
    void updateItem(BlastPerformanceEntity data);


}
