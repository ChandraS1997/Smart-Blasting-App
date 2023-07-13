package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.ResponseDrillMethodEntity;

import java.util.List;

@Dao
public interface ResponseDrillMethodDao {

    @Insert
    void insertItem(ResponseDrillMethodEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseDrillMethodEntity)")
    Boolean isExistItem();

    @Query("SELECT * FROM ResponseDrillMethodEntity")
    List<ResponseDrillMethodEntity> getAllEntityDataList();

    @Query("SELECT * FROM ResponseDrillMethodEntity")
    ResponseDrillMethodEntity getSingleItemEntity();

    @Query("DELETE FROM ResponseDrillMethodEntity")
    void deleteItemById();

    @Query("DELETE FROM ResponseDrillMethodEntity")
    void deleteAllItem();

    @Query("UPDATE ResponseDrillMethodEntity SET DrillMethod=:data WHERE id = :id")
    void updateItem(int id, String data);

    @Update
    void updateItem(ResponseDrillMethodEntity data);

}
