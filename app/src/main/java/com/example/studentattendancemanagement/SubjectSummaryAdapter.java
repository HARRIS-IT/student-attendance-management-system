package com.example.studentattendancemanagement;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendancemanagement.model.SubjectAttendanceSummary;

import java.util.List;
import java.util.Locale;

public class SubjectSummaryAdapter extends RecyclerView.Adapter<SubjectSummaryAdapter.ViewHolder> {

    private final List<SubjectAttendanceSummary> summaryList;

    public SubjectSummaryAdapter(List<SubjectAttendanceSummary> summaryList) {
        this.summaryList = summaryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubjectAttendanceSummary summary = summaryList.get(position);
        holder.tvSubjectName.setText(summary.getSubjectName());

        double percentage = summary.getPercentage();
        holder.tvSubjectPercentage.setText(String.format(Locale.getDefault(), "%.1f%%", percentage));

        String details = String.format(Locale.getDefault(), "Total: %d | Present: %d | Absent: %d",
                summary.getTotalClasses(), summary.getPresentCount(), summary.getAbsentCount());
        holder.tvClassesSummary.setText(details);

        int progress = (int) Math.round(percentage);
        holder.pbSubjectAttendance.setProgress(progress);

        if (summary.getTotalClasses() == 0) {
            holder.tvSubjectPercentage.setTextColor(Color.parseColor("#64748B"));
            holder.pbSubjectAttendance.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#CBD5E1")));
        } else if (percentage < 75.0) {
            holder.tvSubjectPercentage.setTextColor(Color.parseColor("#DC2626")); // Red warning
            holder.pbSubjectAttendance.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#DC2626")));
        } else {
            holder.tvSubjectPercentage.setTextColor(Color.parseColor("#16A34A")); // Green
            holder.pbSubjectAttendance.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#16A34A")));
        }
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvSubjectPercentage, tvClassesSummary;
        ProgressBar pbSubjectAttendance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvSubjectPercentage = itemView.findViewById(R.id.tvSubjectPercentage);
            tvClassesSummary = itemView.findViewById(R.id.tvClassesSummary);
            pbSubjectAttendance = itemView.findViewById(R.id.pbSubjectAttendance);
        }
    }
}
