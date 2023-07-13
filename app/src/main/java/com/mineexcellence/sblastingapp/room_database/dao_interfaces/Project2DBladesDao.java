package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.Project2DBladesEntity;

import java.util.List;

@Dao
public interface Project2DBladesDao {

    @Insert
    void insertProject(Project2DBladesEntity data);

    @Query("SELECT EXISTS(SELECT * FROM Project2DBladesEntity WHERE designId = :designId)")
    Boolean isExistProject(String designId);

    @Query("SELECT * FROM Project2DBladesEntity")
    List<Project2DBladesEntity> getAllBladesProject();

    @Query("DELETE FROM Project2DBladesEntity WHERE designId = :designId")
    void deleteProjectById(String designId);

    @Query("DELETE FROM Project2DBladesEntity")
    void deleteAllProject();

    @Update
    void updateProject(Project2DBladesEntity data);

   /* @Query("UPDATE ResponseBladesRetrieveData SET first_name = :fname, last_name=:lname WHERE designId = :designId")
    void updateProjectById(String designId, String fname, String lname);*/
}
