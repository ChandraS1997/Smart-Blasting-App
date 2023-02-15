package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseZoneTableEntity;

import java.util.List;

@Dao
public interface ZoneTableDao {

    @Insert
    void insertProject(ResponseZoneTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseZoneTableEntity WHERE zoneCode = :zoneCode)")
    Boolean isExistProject(int zoneCode);

    @Query("SELECT * FROM ResponseZoneTableEntity")
    List<ResponseZoneTableEntity> getAllBladesProject();

    @Query("DELETE FROM ResponseZoneTableEntity WHERE zoneCode = :zoneCode")
    void deleteProjectById(int zoneCode);

    @Query("DELETE FROM ResponseZoneTableEntity")
    void deleteAllProject();

    @Update
    void updateProject(ResponseZoneTableEntity data);

}
