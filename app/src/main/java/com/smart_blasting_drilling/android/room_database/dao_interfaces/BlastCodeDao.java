package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;

import java.util.List;

@Dao
public interface BlastCodeDao {

    @Insert
    void insertItem(BlastCodeEntity data);

    @Query("SELECT EXISTS(SELECT * FROM BlastCodeEntity)")
    Boolean isExistItem();

    @Query("SELECT * FROM BlastCodeEntity")
    List<BlastCodeEntity> getAllEntityDataList();

    @Query("SELECT * FROM BlastCodeEntity WHERE id = :id")
    BlastCodeEntity getSingleItemEntity(int id);

    @Query("SELECT * FROM BlastCodeEntity WHERE DesignId = :id")
    BlastCodeEntity getSingleItemEntityByDesignId(String id);

    @Query("DELETE FROM BlastCodeEntity")
    void deleteAllItem();

    @Query("UPDATE BlastCodeEntity SET BlastCode=:data WHERE DesignId = :id")
    void updateItem(String id, String data);

    @Update
    void updateItem(BlastCodeEntity data);

}
