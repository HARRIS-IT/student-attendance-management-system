package com.example.studentattendancemanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendancemanagement.model.Student;
import com.example.studentattendancemanagement.model.Subject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MarkAttendanceActivity extends AppCompatActivity {

    private Spinner spinnerSubject, spinnerDepartment;
    private TextView tvSelectedDate;
    private Button btnSelectDate, btnSubmitAttendance;
    private RecyclerView rvStudents;

    private DatabaseHelper dbHelper;
    private StudentListAdapter adapter;
    private List<Subject> subjectList;
    private List<String> departmentList;
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        dbHelper = DatabaseHelper.getInstance(this);

        spinnerSubject = findViewById(R.id.spinnerSubject);
        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSubmitAttendance = findViewById(R.id.btnSubmitAttendance);
        rvStudents = findViewById(R.id.rvStudents);

        rvStudents.setLayoutManager(new LinearLayoutManager(this));

        updateDateLabel();

        btnSelectDate.setOnClickListener(v -> showDatePicker());

        loadSubjects();
        loadDepartments();

        btnSubmitAttendance.setOnClickListener(v -> submitAttendance());
    }

    private void updateDateLabel() {
        String dateStr = "Date: " + dateFormat.format(calendar.getTime());
        tvSelectedDate.setText(dateStr);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateLabel();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void loadSubjects() {
        subjectList = dbHelper.getAllSubjects();
        if (subjectList.isEmpty()) {
            Toast.makeText(this, "No subjects found.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter<Subject> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectList);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);
    }

    private void loadDepartments() {
        departmentList = dbHelper.getAllDepartments();
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentList);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(deptAdapter);

        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDept = departmentList.get(position);
                loadStudents(selectedDept);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadStudents("All Departments");
            }
        });
    }

    private void loadStudents(String department) {
        List<Student> studentList = dbHelper.getStudentsByDepartment(department);
        adapter = new StudentListAdapter(studentList);
        rvStudents.setAdapter(adapter);
    }

    private void submitAttendance() {
        if (subjectList == null || subjectList.isEmpty()) {
            Toast.makeText(this, "Please select a valid subject.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedSubjIndex = spinnerSubject.getSelectedItemPosition();
        if (selectedSubjIndex < 0 || selectedSubjIndex >= subjectList.size()) {
            Toast.makeText(this, "Invalid subject selection.", Toast.LENGTH_SHORT).show();
            return;
        }

        Subject selectedSubject = subjectList.get(selectedSubjIndex);
        String selectedDate = dateFormat.format(calendar.getTime());

        List<Student> studentsToSave = adapter.getStudentList();
        if (studentsToSave.isEmpty()) {
            Toast.makeText(this, "No students to mark attendance for.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean allSaved = true;
        for (Student s : studentsToSave) {
            boolean success = dbHelper.markAttendance(s.getId(), selectedSubject.getId(), selectedDate, s.isPresent());
            if (!success) {
                allSaved = false;
            }
        }

        if (allSaved) {
            Toast.makeText(this, "Attendance recorded successfully for " + selectedDate + "!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Some attendance entries failed to save.", Toast.LENGTH_SHORT).show();
        }
    }
}
