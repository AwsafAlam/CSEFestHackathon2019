package com.example.android.e_learner.student;

public class Student {

    private String mobileNumber;
    private String name, institution;

    public Student(String mobileNumber, String name, String institution) {
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.institution = institution;
    }

    public Student() {
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getName() {
        return name;
    }

    public String getInstitution() {
        return institution;
    }
}
