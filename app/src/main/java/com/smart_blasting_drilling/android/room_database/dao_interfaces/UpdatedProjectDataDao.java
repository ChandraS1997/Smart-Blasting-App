package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.UpdatedProjectDetailEntity;

import java.util.List;

@Dao
public interface UpdatedProjectDataDao {

    @Insert
    void insertItem(UpdatedProjectDetailEntity data);

    @Query("SELECT EXISTS(SELECT * FROM UpdatedProjectDetailEntity WHERE ProjectId = :ProjectId)")
    Boolean isExistItem(String ProjectId);

    @Query("SELECT * FROM UpdatedProjectDetailEntity")
    List<UpdatedProjectDetailEntity> getAllEntityDataList();

    @Query("SELECT * FROM UpdatedProjectDetailEntity WHERE ProjectId = :desginId")
    UpdatedProjectDetailEntity getSingleItemEntity(String desginId);

    @Query("DELETE FROM UpdatedProjectDetailEntity WHERE ProjectId = :desginId")
    void deleteItemById(String desginId);

    @Query("DELETE FROM UpdatedProjectDetailEntity")
    void deleteAllItem();

    @Query("UPDATE UpdatedProjectDetailEntity SET data=:data WHERE ProjectId = :ProjectId")
    void updateItem(String ProjectId, String data);

    @Update
    void updateItem(UpdatedProjectDetailEntity data);

}
