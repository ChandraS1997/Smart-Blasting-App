package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;

import java.util.List;

@Dao
public interface Project2DBladesDao {

    @Insert
    void insertProject(ResponseBladesRetrieveData users);

    @Query("SELECT EXISTS(SELECT * FROM ResponseBladesRetrieveData WHERE designId = :designId)")
    Boolean isExistProject(String designId);

    @Query("SELECT * FROM ResponseBladesRetrieveData")
    List<ResponseBladesRetrieveData> getAllBladesProject();

    @Query("DELETE FROM ResponseBladesRetrieveData WHERE designId = :designId")
    void deleteProjectById(String designId);

   /* @Query("UPDATE ResponseBladesRetrieveData SET first_name = :fname, last_name=:lname WHERE designId = :designId")
    void updateProjectById(String designId, String fname, String lname);*/
}
