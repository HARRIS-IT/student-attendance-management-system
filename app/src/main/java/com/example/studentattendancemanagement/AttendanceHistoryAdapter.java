package com.example.studentattendancemanagement;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendancemanagement.model.AttendanceRecord;

import java.util.List;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.ViewHolder> {

    private final List<AttendanceRecord> historyList;

    public AttendanceHistoryAdapter(List<AttendanceRecord> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord record = historyList.get(position);
        holder.tvHistorySubject.setText(record.getSubjectName());
        String dateStr = "Date: " + record.getDate();
        holder.tvHistoryDate.setText(dateStr);

        if ("PRESENT".equalsIgnoreCase(record.getStatus())) {
            holder.tvHistoryStatus.setText("PRESENT");
            holder.tvHistoryStatus.setTextColor(Color.parseColor("#15803D"));
            holder.tvHistoryStatus.setBackgroundColor(Color.parseColor("#DCFCE7"));
        } else {
            holder.tvHistoryStatus.setText("ABSENT");
            holder.tvHistoryStatus.setTextColor(Color.parseColor("#B91C1C"));
            holder.tvHistoryStatus.setBackgroundColor(Color.parseColor("#FEE2E2"));
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistorySubject, tvHistoryDate, tvHistoryStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistorySubject = itemView.findViewById(R.id.tvHistorySubject);
            tvHistoryDate = itemView.findViewById(R.id.tvHistoryDate);
            tvHistoryStatus = itemView.findViewById(R.id.tvHistoryStatus);
        }
    }
}
