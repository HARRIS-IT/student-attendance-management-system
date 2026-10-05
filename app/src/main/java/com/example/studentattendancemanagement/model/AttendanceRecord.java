package com.example.studentattendancemanagement.model;

public class AttendanceRecord {
    private final long id;
    private final long studentId;
    private final long subjectId;
    private final String subjectName;
    private final String date;
    private final String status; // "PRESENT" or "ABSENT"

    public AttendanceRecord(long id, long studentId, long subjectId, String subjectName, String date, String status) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.date = date;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public long getStudentId() {
        return studentId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
