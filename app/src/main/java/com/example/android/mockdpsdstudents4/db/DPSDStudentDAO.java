package com.example.android.mockdpsdstudents4.db;

import java.util.ArrayList;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DPSDStudentDAO {

    @Query("SELECT * FROM DPSDStudentEntity")
    ArrayList<DPSDStudentEntity> getAllDPSDEntities();

    @Insert
    void insertDPSDEntity (DPSDStudentEntity... entities);

    @Delete
    void delete(DPSDStudentEntity entity);
}
