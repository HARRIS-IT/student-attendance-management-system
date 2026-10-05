package com.example.studentattendancemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

public class TeacherDashboardActivity extends AppCompatActivity {

    private MaterialCardView cardMarkAttendance, cardRegisterStudent;
    private Button btnLogout;
    private LinearLayout llLowAttendanceContainer;
    private TextView tvLowAttendanceNotice;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        dbHelper = DatabaseHelper.getInstance(this);

        cardMarkAttendance = findViewById(R.id.cardMarkAttendance);
        cardRegisterStudent = findViewById(R.id.cardRegisterStudent);
        btnLogout = findViewById(R.id.btnLogout);
        llLowAttendanceContainer = findViewById(R.id.llLowAttendanceContainer);
        tvLowAttendanceNotice = findViewById(R.id.tvLowAttendanceNotice);

        cardMarkAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, MarkAttendanceActivity.class);
            startActivity(intent);
        });

        cardRegisterStudent.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, RegisterStudentActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLowAttendanceAlerts();
    }

    private void loadLowAttendanceAlerts() {
        List<DatabaseHelper.StudentLowAttendanceInfo> lowList = dbHelper.getLowAttendanceStudents(75.0);

        if (lowList.isEmpty()) {
            tvLowAttendanceNotice.setVisibility(View.VISIBLE);
            tvLowAttendanceNotice.setText("No students below 75% attendance threshold.");
            // Remove any dynamically added views
            int childCount = llLowAttendanceContainer.getChildCount();
            if (childCount > 1) {
                llLowAttendanceContainer.removeViews(1, childCount - 1);
            }
        } else {
            tvLowAttendanceNotice.setVisibility(View.GONE);
            int childCount = llLowAttendanceContainer.getChildCount();
            if (childCount > 1) {
                llLowAttendanceContainer.removeViews(1, childCount - 1);
            }

            for (DatabaseHelper.StudentLowAttendanceInfo info : lowList) {
                TextView tv = new TextView(this);
                String text = String.format(Locale.getDefault(), "⚠️ %s (%s, %s) - %.1f%% (%d/%d Present)",
                        info.student.getName(),
                        info.student.getRollNumber(),
                        info.student.getDepartment(),
                        info.stats.getPercentage(),
                        info.stats.presentCount,
                        info.stats.totalClasses);
                tv.setText(text);
                tv.setTextSize(14f);
                tv.setTextColor(0xFF991B1B);
                tv.setPadding(0, 8, 0, 8);
                llLowAttendanceContainer.addView(tv);
            }
        }
    }
}
