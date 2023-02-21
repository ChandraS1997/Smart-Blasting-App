package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;

import java.util.List;

@Dao
public interface ProjectHoleDetailRowColDao {

    @Insert
    void insertProject(ProjectHoleDetailRowColEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ProjectHoleDetailRowColEntity WHERE designId = :designId)")
    Boolean isExistProject(String designId);

    @Query("SELECT * FROM ProjectHoleDetailRowColEntity")
    List<ProjectHoleDetailRowColEntity> getAllBladesProjectList();

    @Query("SELECT * FROM ProjectHoleDetailRowColEntity WHERE designId = :designId")
    ProjectHoleDetailRowColEntity getAllBladesProject(String designId);

    @Query("DELETE FROM ProjectHoleDetailRowColEntity WHERE designId = :designId")
    void deleteProjectById(String designId);

    @Query("DELETE FROM ProjectHoleDetailRowColEntity")
    void deleteAllProject();

    @Query("UPDATE ProjectHoleDetailRowColEntity Set project_hole=:data, designId=:designId WHERE id=:id")
    void updateProject(int id, String designId, String data);

    @Update
    void updateProject(ProjectHoleDetailRowColEntity data);

}
