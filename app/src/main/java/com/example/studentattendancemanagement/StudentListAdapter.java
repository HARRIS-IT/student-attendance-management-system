package com.example.studentattendancemanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendancemanagement.model.Student;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private final List<Student> studentList;

    public StudentListAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentName.setText(student.getName());
        String details = "Roll: " + student.getRollNumber() + " | Dept: " + student.getDepartment();
        holder.tvStudentDetails.setText(details);

        if (student.isPresent()) {
            holder.rbPresent.setChecked(true);
        } else {
            holder.rbAbsent.setChecked(true);
        }

        holder.rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            student.setPresent(checkedId == R.id.rbPresent);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentDetails;
        RadioGroup rgStatus;
        RadioButton rbPresent, rbAbsent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentDetails = itemView.findViewById(R.id.tvStudentDetails);
            rgStatus = itemView.findViewById(R.id.rgAttendanceStatus);
            rbPresent = itemView.findViewById(R.id.rbPresent);
            rbAbsent = itemView.findViewById(R.id.rbAbsent);
        }
    }
}
