package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.InitiatingDeviceDataEntity;

import java.util.List;

@Dao
public interface InitiatingDeviceDao {

    @Insert
    void insertItem(InitiatingDeviceDataEntity data);

    @Query("SELECT EXISTS(SELECT * FROM InitiatingDeviceDataEntity WHERE DesignId = :id)")
    Boolean isExistItem(String id);

    @Query("SELECT * FROM InitiatingDeviceDataEntity")
    List<InitiatingDeviceDataEntity> getAllEntityDataList();

    @Query("SELECT * FROM InitiatingDeviceDataEntity WHERE DesignId = :designId")
    InitiatingDeviceDataEntity getSingleItemEntity(String designId);

    @Query("DELETE FROM InitiatingDeviceDataEntity WHERE DesignId = :designId")
    void deleteItemById(String designId);

    @Query("DELETE FROM InitiatingDeviceDataEntity")
    void deleteAllItem();

    @Query("UPDATE InitiatingDeviceDataEntity SET InitiatingDeviceData=:data WHERE DesignId = :id")
    void updateItem(String id, String data);

    @Update
    void updateItem(InitiatingDeviceDataEntity data);

}
