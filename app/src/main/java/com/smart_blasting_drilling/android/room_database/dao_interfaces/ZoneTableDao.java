package com.smart_blasting_drilling.android.room_database.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smart_blasting_drilling.android.room_database.entities.ResponseMineTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseZoneTableEntity;

import java.util.List;

@Dao
public interface ZoneTableDao {

    @Insert
    void insertProject(ResponseZoneTableEntity data);

    @Query("SELECT EXISTS(SELECT * FROM ResponseZoneTableEntity)")
    Boolean isExistProject();

    @Query("SELECT * FROM ResponseZoneTableEntity")
    List<ResponseZoneTableEntity> getAllBladesProject();

    @Query("SELECT * FROM ResponseZoneTableEntity WHERE id=:id")
    ResponseZoneTableEntity getAllBladesProject(int id);

    @Query("DELETE FROM ResponseZoneTableEntity")
    void deleteProjectById();

    @Query("DELETE FROM ResponseZoneTableEntity")
    void deleteAllProject();

    @Query("UPDATE ResponseZoneTableEntity Set ZoneData=:data WHERE id=:id")
    void updateProject(int id, String data);

    @Update
    void updateProject(ResponseZoneTableEntity data);

}
