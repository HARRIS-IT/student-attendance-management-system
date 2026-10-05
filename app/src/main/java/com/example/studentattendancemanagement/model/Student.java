package com.example.studentattendancemanagement.model;

public class Student {
    private long id;
    private String name;
    private String rollNumber;
    private String department;
    private long userId;
    private boolean isPresent = true; // Helper property for attendance marking

    public Student(long id, String name, String rollNumber, String department, long userId) {
        this.id = id;
        this.name = name;
        this.rollNumber = rollNumber;
        this.department = department;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
