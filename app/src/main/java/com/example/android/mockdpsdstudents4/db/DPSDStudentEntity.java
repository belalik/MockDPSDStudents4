package com.example.android.mockdpsdstudents4.db;

import java.time.LocalDate;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DPSDStudentEntity {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    private static int index = 1;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "birth_date")
    private LocalDate birthDate;

    @ColumnInfo(name = "sex")
    private int sex;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "year_of_entry")
    private int yearOfEntry;

    @ColumnInfo(name = "all_but_dissertation")
    private boolean allButDissertation;

    @ColumnInfo(name = "shortlisted")
    private boolean shortlisted;



}
