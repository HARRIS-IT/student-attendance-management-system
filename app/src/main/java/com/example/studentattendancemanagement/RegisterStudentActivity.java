package com.example.studentattendancemanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterStudentActivity extends AppCompatActivity {

    private TextInputEditText etStudentName, etRollNumber, etDepartment, etUsername, etPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        dbHelper = DatabaseHelper.getInstance(this);

        etStudentName = findViewById(R.id.etStudentName);
        etRollNumber = findViewById(R.id.etRollNumber);
        etDepartment = findViewById(R.id.etDepartment);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String name = etStudentName.getText() != null ? etStudentName.getText().toString().trim() : "";
        String roll = etRollNumber.getText() != null ? etRollNumber.getText().toString().trim() : "";
        String dept = etDepartment.getText() != null ? etDepartment.getText().toString().trim() : "";
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(roll) || TextUtils.isEmpty(dept) ||
                TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.registerStudent(name, roll, dept, username, password);

        if (result > 0) {
            Toast.makeText(this, "Student registered successfully!", Toast.LENGTH_LONG).show();
            finish();
        } else if (result == -2) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        } else if (result == -3) {
            Toast.makeText(this, "Roll number already registered", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to register student", Toast.LENGTH_SHORT).show();
        }
    }
}
