package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.BlastCodeEntity;

import java.util.List;

@Dao
public interface BlastCodeDao {

    @Insert
    void insertItem(BlastCodeEntity data);

    @Query("SELECT EXISTS(SELECT * FROM BlastCodeEntity)")
    Boolean isExistItem();

    @Query("SELECT EXISTS(SELECT * FROM BlastCodeEntity WHERE DesignId = :id)")
    Boolean isExistItem(String id);

    @Query("SELECT * FROM BlastCodeEntity")
    List<BlastCodeEntity> getAllEntityDataList();

    @Query("SELECT * FROM BlastCodeEntity WHERE id = :id")
    BlastCodeEntity getSingleItemEntity(int id);

    @Query("SELECT * FROM BlastCodeEntity WHERE DesignId = :id")
    BlastCodeEntity getSingleItemEntityByDesignId(String id);

    @Query("DELETE FROM BlastCodeEntity")
    void deleteAllItem();

    @Query("DELETE FROM BlastCodeEntity WHERE DesignId = :id")
    void deleteSingleItem(String id);

    @Query("UPDATE BlastCodeEntity SET BlastCode=:data WHERE DesignId = :id")
    void updateItem(String id, String data);

    @Update
    void updateItem(BlastCodeEntity data);

}
