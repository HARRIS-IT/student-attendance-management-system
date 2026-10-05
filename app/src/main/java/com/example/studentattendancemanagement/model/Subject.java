package com.example.studentattendancemanagement.model;

public class Subject {
    private final long id;
    private final String subjectName;
    private final String department;

    public Subject(long id, String subjectName, String department) {
        this.id = id;
        this.subjectName = subjectName;
        this.department = department;
    }

    public long getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return subjectName;
    }
}
