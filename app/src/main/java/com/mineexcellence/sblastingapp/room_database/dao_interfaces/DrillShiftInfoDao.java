package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.DrillShiftInfoEntity;

import java.util.List;

@Dao
public interface DrillShiftInfoDao {

    @Insert
    void insertItem(DrillShiftInfoEntity data);

    @Query("SELECT EXISTS(SELECT * FROM DrillShiftInfoEntity)")
    Boolean isExistItem();

    @Query("SELECT * FROM DrillShiftInfoEntity")
    List<DrillShiftInfoEntity> getAllEntityDataList();

    @Query("SELECT * FROM DrillShiftInfoEntity")
    DrillShiftInfoEntity getSingleItemEntity();

    @Query("DELETE FROM DrillShiftInfoEntity")
    void deleteItemById();

    @Query("DELETE FROM DrillShiftInfoEntity")
    void deleteAllItem();

    @Query("UPDATE DrillShiftInfoEntity SET data=:data WHERE id = :id")
    void updateItem(int id, String data);

    @Update
    void updateItem(DrillShiftInfoEntity data);

}
