package com.example.studentattendancemanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studentattendancemanagement.model.AttendanceRecord;
import com.example.studentattendancemanagement.model.Student;
import com.example.studentattendancemanagement.model.Subject;
import com.example.studentattendancemanagement.model.SubjectAttendanceSummary;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "attendance_mgmt.db";
    private static final int DATABASE_VERSION = 3;

    // Tables
    public static final String TABLE_USERS = "users";
    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_SUBJECTS = "subjects";
    public static final String TABLE_ATTENDANCE = "attendance";

    // Users Columns
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role"; // TEACHER or STUDENT

    // Students Columns
    public static final String COL_STUDENT_ID = "id";
    public static final String COL_STUDENT_NAME = "name";
    public static final String COL_ROLL_NUMBER = "roll_number";
    public static final String COL_DEPARTMENT = "department";
    public static final String COL_STUDENT_USER_ID = "user_id";

    // Subjects Columns
    public static final String COL_SUBJECT_ID = "id";
    public static final String COL_SUBJECT_NAME = "subject_name";
    public static final String COL_SUBJECT_DEPT = "department";

    // Attendance Columns
    public static final String COL_ATT_ID = "id";
    public static final String COL_ATT_STUDENT_ID = "student_id";
    public static final String COL_ATT_SUBJECT_ID = "subject_id";
    public static final String COL_ATT_DATE = "date";
    public static final String COL_ATT_STATUS = "status"; // PRESENT or ABSENT

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";

        String CREATE_STUDENTS_TABLE = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COL_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_NAME + " TEXT, " +
                COL_ROLL_NUMBER + " TEXT UNIQUE, " +
                COL_DEPARTMENT + " TEXT, " +
                COL_STUDENT_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_STUDENT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";

        String CREATE_SUBJECTS_TABLE = "CREATE TABLE " + TABLE_SUBJECTS + " (" +
                COL_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SUBJECT_NAME + " TEXT, " +
                COL_SUBJECT_DEPT + " TEXT)";

        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                COL_ATT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ATT_STUDENT_ID + " INTEGER, " +
                COL_ATT_SUBJECT_ID + " INTEGER, " +
                COL_ATT_DATE + " TEXT, " +
                COL_ATT_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COL_ATT_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COL_STUDENT_ID + "), " +
                "FOREIGN KEY(" + COL_ATT_SUBJECT_ID + ") REFERENCES " + TABLE_SUBJECTS + "(" + COL_SUBJECT_ID + "))";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_STUDENTS_TABLE);
        db.execSQL(CREATE_SUBJECTS_TABLE);
        db.execSQL(CREATE_ATTENDANCE_TABLE);

        // Seed default Teacher
        ContentValues teacherValues = new ContentValues();
        teacherValues.put(COL_USERNAME, "teacher");
        teacherValues.put(COL_PASSWORD, "password123");
        teacherValues.put(COL_ROLE, "TEACHER");
        db.insert(TABLE_USERS, null, teacherValues);

        // Seed second Teacher
        ContentValues teacher2Values = new ContentValues();
        teacher2Values.put(COL_USERNAME, "ranganathan");
        teacher2Values.put(COL_PASSWORD, "teacher123");
        teacher2Values.put(COL_ROLE, "TEACHER");
        db.insert(TABLE_USERS, null, teacher2Values);

        // Seed default Subjects
        seedDefaultSubjects(db);

        // Seed 15 Students across multiple departments with Tamil Nadu names
        String[][] sampleStudents = {
                {"Karthik Kumar", "TN001", "Computer Science", "karthik", "123"},
                {"Anitha Suresh", "TN002", "Computer Science", "anitha", "123"},
                {"Bala Krishnan", "TN003", "Computer Science", "bala", "123"},
                {"Deepa Ramesh", "TN004", "Information Technology", "deepa", "123"},
                {"Ezhil Vendan", "TN005", "Information Technology", "ezhil", "123"},
                {"Fathima Beevi", "TN006", "Electronics & Comm", "fathima", "123"},
                {"Ganesan Murugan", "TN007", "Electronics & Comm", "ganesan", "123"},
                {"Harini Sundar", "TN008", "Electrical & Elec", "harini", "123"},
                {"Ilango Kumar", "TN009", "Electrical & Elec", "ilango", "123"},
                {"Jothi Selvam", "TN010", "Mechanical", "jothi", "123"},
                {"Kavitha Nathan", "TN011", "Mechanical", "kavitha", "123"},
                {"Lakshmanan Pillai", "TN012", "Civil", "lakshmanan", "123"},
                {"Malathi Devi", "TN013", "Civil", "malathi", "123"},
                {"Nithya Rajan", "TN014", "AI & Data Science", "nithya", "123"},
                {"Prabhu Deva", "TN015", "AI & Data Science", "prabhu", "123"}
        };

        for (int i = 0; i < sampleStudents.length; i++) {
            String name = sampleStudents[i][0];
            String roll = sampleStudents[i][1];
            String dept = sampleStudents[i][2];
            String uname = sampleStudents[i][3];
            String upass = sampleStudents[i][4];

            ContentValues uValues = new ContentValues();
            uValues.put(COL_USERNAME, uname);
            uValues.put(COL_PASSWORD, upass);
            uValues.put(COL_ROLE, "STUDENT");
            long uId = db.insert(TABLE_USERS, null, uValues);

            if (uId != -1) {
                ContentValues sValues = new ContentValues();
                sValues.put(COL_STUDENT_NAME, name);
                sValues.put(COL_ROLL_NUMBER, roll);
                sValues.put(COL_DEPARTMENT, dept);
                sValues.put(COL_STUDENT_USER_ID, uId);
                long sId = db.insert(TABLE_STUDENTS, null, sValues);

                String[] dates = {"2025-05-01", "2025-05-02", "2025-05-03", "2025-05-04", "2025-05-05"};
                for (int d = 0; d < dates.length; d++) {
                    for (long subjId = 1; subjId <= 5; subjId++) {
                        boolean isPresent;
                        if (i == 0 || i == 1) {
                            // Karthik & Anitha: 40% attendance (triggers low attendance alert < 75%)
                            isPresent = (d < 2);
                        } else {
                            // Others: 80% attendance
                            isPresent = (d != 2);
                        }
                        ContentValues attValues = new ContentValues();
                        attValues.put(COL_ATT_STUDENT_ID, sId);
                        attValues.put(COL_ATT_SUBJECT_ID, subjId);
                        attValues.put(COL_ATT_DATE, dates[d]);
                        attValues.put(COL_ATT_STATUS, isPresent ? "PRESENT" : "ABSENT");
                        db.insert(TABLE_ATTENDANCE, null, attValues);
                    }
                }
            }
        }
    }

    private void seedDefaultSubjects(SQLiteDatabase db) {
        String[] subjects = {"Mathematics", "Computer Science", "Physics", "Chemistry", "English"};
        for (String subj : subjects) {
            ContentValues values = new ContentValues();
            values.put(COL_SUBJECT_NAME, subj);
            values.put(COL_SUBJECT_DEPT, "General");
            db.insert(TABLE_SUBJECTS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- User & Auth ---

    public UserAuthResult authenticateUser(String identifier, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String idStr = identifier != null ? identifier.trim() : "";
        String passStr = password != null ? password.trim() : "";

        Cursor cursor;
        if ("STUDENT".equals(role)) {
            String query = "SELECT u." + COL_USER_ID + ", u." + COL_ROLE +
                    " FROM " + TABLE_USERS + " u" +
                    " LEFT JOIN " + TABLE_STUDENTS + " s ON u." + COL_USER_ID + " = s." + COL_STUDENT_USER_ID +
                    " WHERE (u." + COL_USERNAME + " = ? OR s." + COL_ROLL_NUMBER + " = ?) AND u." + COL_PASSWORD + " = ? AND u." + COL_ROLE + " = ?";
            cursor = db.rawQuery(query, new String[]{idStr, idStr, passStr, role});
        } else {
            cursor = db.query(TABLE_USERS,
                    new String[]{COL_USER_ID, COL_ROLE},
                    COL_USERNAME + "=? AND " + COL_PASSWORD + "=? AND " + COL_ROLE + "=?",
                    new String[]{idStr, passStr, role},
                    null, null, null);
        }

        if (cursor != null && cursor.moveToFirst()) {
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_USER_ID));
            cursor.close();

            long studentId = -1;
            if ("STUDENT".equals(role)) {
                studentId = getStudentIdByUserId(userId);
            }
            return new UserAuthResult(true, userId, studentId, role);
        }

        if (cursor != null) {
            cursor.close();
        }
        return new UserAuthResult(false, -1, -1, role);
    }

    public static class UserAuthResult {
        public final boolean success;
        public final long userId;
        public final long studentId;
        public final String role;

        public UserAuthResult(boolean success, long userId, long studentId, String role) {
            this.success = success;
            this.userId = userId;
            this.studentId = studentId;
            this.role = role;
        }
    }

    public long getStudentIdByUserId(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS,
                new String[]{COL_STUDENT_ID},
                COL_STUDENT_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        long studentId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            studentId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_ID));
            cursor.close();
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return studentId;
    }

    // --- Student Registration ---

    public long registerStudent(String name, String rollNumber, String department, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Check username unique
            Cursor uCursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                    COL_USERNAME + "=?", new String[]{username.trim()}, null, null, null);
            if (uCursor != null && uCursor.getCount() > 0) {
                uCursor.close();
                return -2; // Username exists
            }
            if (uCursor != null) uCursor.close();

            // Check roll number unique
            Cursor rCursor = db.query(TABLE_STUDENTS, new String[]{COL_STUDENT_ID},
                    COL_ROLL_NUMBER + "=?", new String[]{rollNumber.trim()}, null, null, null);
            if (rCursor != null && rCursor.getCount() > 0) {
                rCursor.close();
                return -3; // Roll number exists
            }
            if (rCursor != null) rCursor.close();

            // Insert User
            ContentValues userValues = new ContentValues();
            userValues.put(COL_USERNAME, username.trim());
            userValues.put(COL_PASSWORD, password.trim());
            userValues.put(COL_ROLE, "STUDENT");
            long userId = db.insert(TABLE_USERS, null, userValues);

            if (userId == -1) return -1;

            // Insert Student
            ContentValues studentValues = new ContentValues();
            studentValues.put(COL_STUDENT_NAME, name.trim());
            studentValues.put(COL_ROLL_NUMBER, rollNumber.trim());
            studentValues.put(COL_DEPARTMENT, department.trim());
            studentValues.put(COL_STUDENT_USER_ID, userId);

            long studentId = db.insert(TABLE_STUDENTS, null, studentValues);
            if (studentId != -1) {
                db.setTransactionSuccessful();
            }
            return studentId;
        } finally {
            db.endTransaction();
        }
    }

    public Student getStudentById(long studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS,
                new String[]{COL_STUDENT_ID, COL_STUDENT_NAME, COL_ROLL_NUMBER, COL_DEPARTMENT, COL_STUDENT_USER_ID},
                COL_STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Student student = new Student(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLL_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTMENT)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_USER_ID))
            );
            cursor.close();
            return student;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, null, null, null, null, COL_STUDENT_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Student s = new Student(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLL_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTMENT)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_USER_ID))
                );
                list.add(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
        return list;
    }

    public List<String> getAllDepartments() {
        List<String> list = new ArrayList<>();
        list.add("All Departments");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_STUDENTS, new String[]{COL_DEPARTMENT}, null, null, null, null, COL_DEPARTMENT + " ASC", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String dept = cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTMENT));
                if (dept != null && !dept.isEmpty() && !list.contains(dept)) {
                    list.add(dept);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
        return list;
    }

    public List<Student> getStudentsByDepartment(String department) {
        if (department == null || department.equals("All Departments") || department.isEmpty()) {
            return getAllStudents();
        }
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, COL_DEPARTMENT + "=?", new String[]{department}, null, null, COL_STUDENT_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Student s = new Student(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLL_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTMENT)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_USER_ID))
                );
                list.add(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
        return list;
    }

    // --- Subjects ---

    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SUBJECTS, null, null, null, null, null, COL_SUBJECT_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Subject s = new Subject(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_SUBJECT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBJECT_DEPT))
                );
                list.add(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
        return list;
    }

    public long addSubject(String subjectName, String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SUBJECT_NAME, subjectName.trim());
        values.put(COL_SUBJECT_DEPT, department.trim());
        return db.insert(TABLE_SUBJECTS, null, values);
    }

    // --- Attendance Operations ---

    public boolean markAttendance(long studentId, long subjectId, String date, boolean isPresent) {
        SQLiteDatabase db = this.getWritableDatabase();
        String statusStr = isPresent ? "PRESENT" : "ABSENT";

        // Check if record exists for student + subject + date
        Cursor cursor = db.query(TABLE_ATTENDANCE,
                new String[]{COL_ATT_ID},
                COL_ATT_STUDENT_ID + "=? AND " + COL_ATT_SUBJECT_ID + "=? AND " + COL_ATT_DATE + "=?",
                new String[]{String.valueOf(studentId), String.valueOf(subjectId), date},
                null, null, null);

        ContentValues values = new ContentValues();
        values.put(COL_ATT_STUDENT_ID, studentId);
        values.put(COL_ATT_SUBJECT_ID, subjectId);
        values.put(COL_ATT_DATE, date);
        values.put(COL_ATT_STATUS, statusStr);

        long result;
        if (cursor != null && cursor.moveToFirst()) {
            long attId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ATT_ID));
            cursor.close();
            result = db.update(TABLE_ATTENDANCE, values, COL_ATT_ID + "=?", new String[]{String.valueOf(attId)});
        } else {
            if (cursor != null) cursor.close();
            result = db.insert(TABLE_ATTENDANCE, null, values);
        }
        return result != -1;
    }

    public List<SubjectAttendanceSummary> getSubjectAttendanceSummaries(long studentId) {
        List<SubjectAttendanceSummary> summaries = new ArrayList<>();
        List<Subject> subjects = getAllSubjects();

        SQLiteDatabase db = this.getReadableDatabase();

        for (Subject subject : subjects) {
            String query = "SELECT " +
                    "COUNT(*) as total, " +
                    "SUM(CASE WHEN " + COL_ATT_STATUS + " = 'PRESENT' THEN 1 ELSE 0 END) as present_count, " +
                    "SUM(CASE WHEN " + COL_ATT_STATUS + " = 'ABSENT' THEN 1 ELSE 0 END) as absent_count " +
                    "FROM " + TABLE_ATTENDANCE + " WHERE " +
                    COL_ATT_STUDENT_ID + " = ? AND " + COL_ATT_SUBJECT_ID + " = ?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId), String.valueOf(subject.getId())});
            if (cursor != null && cursor.moveToFirst()) {
                int total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
                int present = cursor.getInt(cursor.getColumnIndexOrThrow("present_count"));
                int absent = cursor.getInt(cursor.getColumnIndexOrThrow("absent_count"));

                summaries.add(new SubjectAttendanceSummary(subject.getSubjectName(), total, present, absent));
                cursor.close();
            } else {
                if (cursor != null) cursor.close();
                summaries.add(new SubjectAttendanceSummary(subject.getSubjectName(), 0, 0, 0));
            }
        }
        return summaries;
    }

    public OverallStats getOverallStatsForStudent(long studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                "COUNT(*) as total, " +
                "SUM(CASE WHEN " + COL_ATT_STATUS + " = 'PRESENT' THEN 1 ELSE 0 END) as present_count, " +
                "SUM(CASE WHEN " + COL_ATT_STATUS + " = 'ABSENT' THEN 1 ELSE 0 END) as absent_count " +
                "FROM " + TABLE_ATTENDANCE + " WHERE " + COL_ATT_STUDENT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});
        int total = 0, present = 0, absent = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            present = cursor.getInt(cursor.getColumnIndexOrThrow("present_count"));
            absent = cursor.getInt(cursor.getColumnIndexOrThrow("absent_count"));
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }

        return new OverallStats(total, present, absent);
    }

    public static class OverallStats {
        public final int totalClasses;
        public final int presentCount;
        public final int absentCount;

        public OverallStats(int totalClasses, int presentCount, int absentCount) {
            this.totalClasses = totalClasses;
            this.presentCount = presentCount;
            this.absentCount = absentCount;
        }

        public double getPercentage() {
            if (totalClasses == 0) return 0.0;
            return (presentCount * 100.0) / totalClasses;
        }
    }

    public List<AttendanceRecord> getAttendanceHistory(long studentId, long subjectIdFilter) {
        List<AttendanceRecord> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT a." + COL_ATT_ID + ", a." + COL_ATT_STUDENT_ID + ", a." + COL_ATT_SUBJECT_ID +
                ", s." + COL_SUBJECT_NAME + ", a." + COL_ATT_DATE + ", a." + COL_ATT_STATUS +
                " FROM " + TABLE_ATTENDANCE + " a" +
                " JOIN " + TABLE_SUBJECTS + " s ON a." + COL_ATT_SUBJECT_ID + " = s." + COL_SUBJECT_ID +
                " WHERE a." + COL_ATT_STUDENT_ID + " = ?";

        List<String> args = new ArrayList<>();
        args.add(String.valueOf(studentId));

        if (subjectIdFilter > 0) {
            query += " AND a." + COL_ATT_SUBJECT_ID + " = ?";
            args.add(String.valueOf(subjectIdFilter));
        }

        query += " ORDER BY a." + COL_ATT_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, args.toArray(new String[0]));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                AttendanceRecord rec = new AttendanceRecord(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_ATT_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_ATT_STUDENT_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_ATT_SUBJECT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_STATUS))
                );
                list.add(rec);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
        return list;
    }

    public List<StudentLowAttendanceInfo> getLowAttendanceStudents(double thresholdPercentage) {
        List<StudentLowAttendanceInfo> list = new ArrayList<>();
        List<Student> students = getAllStudents();

        for (Student s : students) {
            OverallStats stats = getOverallStatsForStudent(s.getId());
            if (stats.totalClasses > 0 && stats.getPercentage() < thresholdPercentage) {
                list.add(new StudentLowAttendanceInfo(s, stats));
            }
        }
        return list;
    }

    public static class StudentLowAttendanceInfo {
        public final Student student;
        public final OverallStats stats;

        public StudentLowAttendanceInfo(Student student, OverallStats stats) {
            this.student = student;
            this.stats = stats;
        }
    }
}
