package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.UpdateProjectBladesEntity;

import java.util.List;

@Dao
public interface UpdateProjectBladesDao {
    @Insert
    void insertProject(UpdateProjectBladesEntity data);

    @Query("SELECT EXISTS(SELECT * FROM UpdateProjectBladesEntity WHERE designId = :designId AND RowId = :rowId AND HoleId = :holeId)")
    Boolean isExistProject(String designId, int rowId, int holeId);

    @Query("SELECT EXISTS(SELECT * FROM UpdateProjectBladesEntity WHERE designId = :designId)")
    Boolean isExistProject(String designId);

    @Query("SELECT * FROM UpdateProjectBladesEntity")
    List<UpdateProjectBladesEntity> getAllBladesProject();

    @Query("SELECT * FROM UpdateProjectBladesEntity WHERE designId = :designId AND RowId = :rowId AND HoleId = :holeId")
    UpdateProjectBladesEntity getAllBladesProject(String designId, int rowId, int holeId);

    @Query("DELETE FROM UpdateProjectBladesEntity WHERE designId = :designId")
    void deleteProjectById(String designId);

    @Query("DELETE FROM UpdateProjectBladesEntity")
    void deleteAllProject();

    @Query("UPDATE UpdateProjectBladesEntity Set BladesData=:data WHERE designId=:id")
    void updateProject(String id, String data);

    @Update
    void updateProject(UpdateProjectBladesEntity data);

}
