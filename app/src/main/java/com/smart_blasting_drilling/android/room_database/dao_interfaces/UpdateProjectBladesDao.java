package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;

import java.util.List;

@Dao
public interface UpdateProjectBladesDao {
    @Insert
    void insertProject(UpdateProjectBladesEntity data);

    @Query("SELECT EXISTS(SELECT * FROM UpdateProjectBladesEntity WHERE designId = :designId)")
    Boolean isExistProject(String designId);

    @Query("SELECT * FROM UpdateProjectBladesEntity")
    List<UpdateProjectBladesEntity> getAllBladesProject();

    @Query("DELETE FROM UpdateProjectBladesEntity WHERE designId = :designId")
    void deleteProjectById(String designId);

    @Query("DELETE FROM UpdateProjectBladesEntity")
    void deleteAllProject();

    @Update
    void updateProject(UpdateProjectBladesEntity data);

}
