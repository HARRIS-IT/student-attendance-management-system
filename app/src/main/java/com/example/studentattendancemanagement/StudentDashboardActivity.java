package com.example.studentattendancemanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendancemanagement.model.Student;
import com.example.studentattendancemanagement.model.SubjectAttendanceSummary;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

public class StudentDashboardActivity extends AppCompatActivity {

    private TextView tvStudentName, tvStudentDetails;
    private TextView tvTotalClasses, tvPresentClasses, tvAbsentClasses, tvOverallPercentage;
    private MaterialCardView cardLowAttendanceAlert;
    private TextView tvLowAttendanceMsg;
    private Button btnLogout, btnViewHistory;
    private RecyclerView rvSubjectSummary;

    private DatabaseHelper dbHelper;
    private long studentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        dbHelper = DatabaseHelper.getInstance(this);

        studentId = getIntent().getLongExtra("STUDENT_ID", -1);

        tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentDetails = findViewById(R.id.tvStudentDetails);

        tvTotalClasses = findViewById(R.id.tvTotalClasses);
        tvPresentClasses = findViewById(R.id.tvPresentClasses);
        tvAbsentClasses = findViewById(R.id.tvAbsentClasses);
        tvOverallPercentage = findViewById(R.id.tvOverallPercentage);

        cardLowAttendanceAlert = findViewById(R.id.cardLowAttendanceAlert);
        tvLowAttendanceMsg = findViewById(R.id.tvLowAttendanceMsg);

        btnLogout = findViewById(R.id.btnLogout);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        rvSubjectSummary = findViewById(R.id.rvSubjectSummary);

        rvSubjectSummary.setLayoutManager(new LinearLayoutManager(this));

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, AttendanceHistoryActivity.class);
            intent.putExtra("STUDENT_ID", studentId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentData();
    }

    private void loadStudentData() {
        if (studentId == -1) {
            Toast.makeText(this, "Student session invalid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Student student = dbHelper.getStudentById(studentId);
        if (student != null) {
            tvStudentName.setText(student.getName());
            String details = "Roll No: " + student.getRollNumber() + " | Dept: " + student.getDepartment();
            tvStudentDetails.setText(details);
        }

        // Overall Stats
        DatabaseHelper.OverallStats stats = dbHelper.getOverallStatsForStudent(studentId);
        tvTotalClasses.setText(String.valueOf(stats.totalClasses));
        tvPresentClasses.setText(String.valueOf(stats.presentCount));
        tvAbsentClasses.setText(String.valueOf(stats.absentCount));

        double overallPct = stats.getPercentage();
        tvOverallPercentage.setText(String.format(Locale.getDefault(), "%.1f%%", overallPct));

        if (stats.totalClasses > 0 && overallPct < 75.0) {
            cardLowAttendanceAlert.setVisibility(View.VISIBLE);
            String alertText = String.format(Locale.getDefault(),
                    "Your overall attendance is %.1f%% which is below the 75%% minimum required threshold!", overallPct);
            tvLowAttendanceMsg.setText(alertText);
            tvOverallPercentage.setTextColor(Color.parseColor("#DC2626"));
        } else {
            cardLowAttendanceAlert.setVisibility(View.GONE);
            tvOverallPercentage.setTextColor(Color.parseColor("#2563EB"));
        }

        // Subject Summaries
        List<SubjectAttendanceSummary> summaries = dbHelper.getSubjectAttendanceSummaries(studentId);
        SubjectSummaryAdapter adapter = new SubjectSummaryAdapter(summaries);
        rvSubjectSummary.setAdapter(adapter);
    }
}
