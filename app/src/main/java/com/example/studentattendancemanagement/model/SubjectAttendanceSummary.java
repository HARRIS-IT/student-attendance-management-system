package com.example.studentattendancemanagement.model;

public class SubjectAttendanceSummary {
    private final String subjectName;
    private final int totalClasses;
    private final int presentCount;
    private final int absentCount;

    public SubjectAttendanceSummary(String subjectName, int totalClasses, int presentCount, int absentCount) {
        this.subjectName = subjectName;
        this.totalClasses = totalClasses;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public double getPercentage() {
        if (totalClasses == 0) return 0.0;
        return (presentCount * 100.0) / totalClasses;
    }
}
