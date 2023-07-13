package com.mineexcellence.sblastingapp.room_database.dao_interfaces;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mineexcellence.sblastingapp.room_database.entities.Project3DBladesEntity;

import java.util.List;

@Dao
public interface Project3DBladesDao {

    @Insert(onConflict = REPLACE)
    void insertProject(Project3DBladesEntity data);

    @Query("SELECT EXISTS(SELECT * FROM Project3DBladesEntity WHERE designId = :designId)")
    Boolean isExistProject(String designId);

    @Query("SELECT * FROM Project3DBladesEntity")
    List<Project3DBladesEntity> getAllBladesProject();

    @Query("DELETE FROM Project3DBladesEntity WHERE designId = :designId")
    void deleteProjectById(String designId);

    @Update
    void updateProject(Project3DBladesEntity data);

   /* @Query("UPDATE ResponseBladesRetrieveData SET first_name = :fname, last_name=:lname WHERE designId = :designId")
    void updateProjectById(String designId, String fname, String lname);*/

}
