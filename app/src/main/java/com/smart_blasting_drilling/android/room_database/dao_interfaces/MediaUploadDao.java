package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.MediaUploadEntity;

import java.util.List;

@Dao
public interface MediaUploadDao {

    @Insert
    void insertItem(MediaUploadEntity data);

    @Query("SELECT EXISTS(SELECT * FROM MediaUploadEntity WHERE ProjectId = :ProjectId)")
    Boolean isExistItem(String ProjectId);

    @Query("SELECT * FROM MediaUploadEntity")
    List<MediaUploadEntity> getAllEntityDataList();

    @Query("SELECT * FROM MediaUploadEntity WHERE ProjectId = :desginId")
    MediaUploadEntity getSingleItemEntity(String desginId);

    @Query("DELETE FROM MediaUploadEntity WHERE ProjectId = :designId")
    void deleteItemById(String designId);

    @Query("DELETE FROM MediaUploadEntity")
    void deleteAllItem();

    @Query("UPDATE MediaUploadEntity SET data=:data WHERE ProjectId = :ProjectId")
    void updateItem(String ProjectId, String data);

    @Update
    void updateItem(MediaUploadEntity data);

}
