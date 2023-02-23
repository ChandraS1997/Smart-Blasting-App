package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.InitiatingDeviceDataEntity;

import java.util.List;

@Dao
public interface InitiatingDeviceDao {

    @Insert
    void insertItem(InitiatingDeviceDataEntity data);

    @Query("SELECT EXISTS(SELECT * FROM InitiatingDeviceDataEntity WHERE id = :id)")
    Boolean isExistItem(int id);

    @Query("SELECT * FROM InitiatingDeviceDataEntity")
    List<InitiatingDeviceDataEntity> getAllEntityDataList();

    @Query("SELECT * FROM InitiatingDeviceDataEntity")
    InitiatingDeviceDataEntity getSingleItemEntity();

    @Query("DELETE FROM InitiatingDeviceDataEntity")
    void deleteItemById();

    @Query("DELETE FROM InitiatingDeviceDataEntity")
    void deleteAllItem();

    @Query("UPDATE InitiatingDeviceDataEntity SET InitiatingDeviceData=:data WHERE id = :id")
    void updateItem(int id, String data);

    @Update
    void updateItem(InitiatingDeviceDataEntity data);

}
