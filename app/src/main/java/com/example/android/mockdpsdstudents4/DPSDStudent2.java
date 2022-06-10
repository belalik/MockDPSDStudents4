package com.example.android.mockdpsdstudents4;

import java.time.LocalDate;

public class DPSDStudent2 {

    private static int index=0;

    private String name;
    private LocalDate birthDate;
    private int sex;
    private int entryYear;
    private boolean abd;

    private String email;

    public DPSDStudent2(String name, LocalDate birthDate, int sex, int entryYear, boolean abd) {
        this.name = name;
        this.birthDate = birthDate;
        this.sex = sex;
        this.entryYear = entryYear;
        this.abd = abd;

        index++;

        createDefaultEmail();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.entryYear = entryYear;
    }

    public boolean isAbd() {
        return abd;
    }

    public void setAbd(boolean abd) {
        this.abd = abd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private void createDefaultEmail() {

        // dpsd19010@aegean.gr...

        String email = "dpsd"+
                this.entryYear%100+
                String.format("%03d", index)+
                "@aegean.gr";

        this.setEmail(email);
    }

    public int getAppropriateIcon() {
        if (this.sex == 1) {
            return R.drawable.man;
        }
        else {
            return R.drawable.woman;
        }
    }
}
