package com.example.studentattendancemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private RadioGroup rgRole;
    private RadioButton rbTeacher, rbStudent;
    private TextInputEditText etUsername, etPassword;
    private Button btnLogin, btnRegisterStudent;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = DatabaseHelper.getInstance(this);

        rgRole = findViewById(R.id.rgRole);
        rbTeacher = findViewById(R.id.rbTeacher);
        rbStudent = findViewById(R.id.rbStudent);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterStudent = findViewById(R.id.btnRegisterStudent);

        btnLogin.setOnClickListener(v -> performLogin());

        btnRegisterStudent.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterStudentActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        String username = etUsername.getText() != null ? etUsername.getText().toString() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        String role = rbTeacher.isChecked() ? "TEACHER" : "STUDENT";

        DatabaseHelper.UserAuthResult result = dbHelper.authenticateUser(username, password, role);

        if (result.success) {
            Toast.makeText(this, "Login Successful (" + role + ")", Toast.LENGTH_SHORT).show();
            if ("TEACHER".equals(role)) {
                Intent intent = new Intent(LoginActivity.this, TeacherDashboardActivity.class);
                intent.putExtra("USER_ID", result.userId);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, StudentDashboardActivity.class);
                intent.putExtra("STUDENT_ID", result.studentId);
                intent.putExtra("USER_ID", result.userId);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid credentials or role mismatch", Toast.LENGTH_SHORT).show();
        }
    }
}
