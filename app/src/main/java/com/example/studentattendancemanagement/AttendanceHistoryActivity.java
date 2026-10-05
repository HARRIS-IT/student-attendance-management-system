package com.example.studentattendancemanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendancemanagement.model.AttendanceRecord;
import com.example.studentattendancemanagement.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class AttendanceHistoryActivity extends AppCompatActivity {

    private Spinner spinnerSubjectFilter;
    private TextView tvEmptyHistory;
    private RecyclerView rvAttendanceHistory;

    private DatabaseHelper dbHelper;
    private long studentId = -1;
    private List<Subject> subjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        dbHelper = DatabaseHelper.getInstance(this);

        studentId = getIntent().getLongExtra("STUDENT_ID", -1);

        spinnerSubjectFilter = findViewById(R.id.spinnerSubjectFilter);
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory);
        rvAttendanceHistory = findViewById(R.id.rvAttendanceHistory);

        rvAttendanceHistory.setLayoutManager(new LinearLayoutManager(this));

        setupSpinner();
    }

    private void setupSpinner() {
        subjectList = new ArrayList<>();
        // Option 0: All Subjects
        subjectList.add(new Subject(-1, "All Subjects", "All"));
        subjectList.addAll(dbHelper.getAllSubjects());

        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjectFilter.setAdapter(adapter);

        spinnerSubjectFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject selected = subjectList.get(position);
                loadHistory(selected.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadHistory(-1);
            }
        });
    }

    private void loadHistory(long subjectIdFilter) {
        List<AttendanceRecord> history = dbHelper.getAttendanceHistory(studentId, subjectIdFilter);

        if (history.isEmpty()) {
            tvEmptyHistory.setVisibility(View.VISIBLE);
            rvAttendanceHistory.setVisibility(View.GONE);
        } else {
            tvEmptyHistory.setVisibility(View.GONE);
            rvAttendanceHistory.setVisibility(View.VISIBLE);
            AttendanceHistoryAdapter adapter = new AttendanceHistoryAdapter(history);
            rvAttendanceHistory.setAdapter(adapter);
        }
    }
}
