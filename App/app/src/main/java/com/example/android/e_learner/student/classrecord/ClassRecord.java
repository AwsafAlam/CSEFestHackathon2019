package com.example.android.e_learner.student.classrecord;

public class ClassRecord {

    private String className, teacherName, duration;
    private float classRating;

    public ClassRecord(String className, String teacherName, String duration, float classRating) {
        this.className = className;
        this.teacherName = teacherName;
        this.duration = duration;
        this.classRating = classRating;
    }

    public String getClassName() {
        return className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getDuration() {
        return duration;
    }

    public float getClassRating() {
        return classRating;
    }
}
