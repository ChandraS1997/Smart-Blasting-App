package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillMaterialEntity;

import java.util.List;

@Dao
public interface ResponseDrillMaterialDao {

    @Insert
    void insertItem(ResponseDrillMaterialEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseDrillMaterialEntity)")
    Boolean isExistItem();

    @Query("SELECT * FROM ResponseDrillMaterialEntity")
    List<ResponseDrillMaterialEntity> getAllEntityDataList();

    @Query("SELECT * FROM ResponseDrillMaterialEntity")
    ResponseDrillMaterialEntity getSingleItemEntity();

    @Query("DELETE FROM ResponseDrillMaterialEntity")
    void deleteItemById();

    @Query("DELETE FROM ResponseDrillMaterialEntity")
    void deleteAllItem();

    @Query("UPDATE ResponseDrillMaterialEntity SET DrillMaterial=:data WHERE id = :id")
    void updateItem(int id, String data);

    @Update
    void updateItem(ResponseDrillMaterialEntity data);

}
