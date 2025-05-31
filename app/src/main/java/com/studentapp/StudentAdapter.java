package com.studentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<Student> students;
    private OnStudentClickListener listener;

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    public StudentAdapter(List<Student> students, OnStudentClickListener listener) {
        this.students = students;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.bind(student, listener);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentIdText, examScoreText, rankText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentIdText = itemView.findViewById(R.id.studentIdText);
            examScoreText = itemView.findViewById(R.id.examScoreText);
            rankText = itemView.findViewById(R.id.rankText);
        }

        void bind(Student student, OnStudentClickListener listener) {
            studentIdText.setText("ID: " + student.getId());
            examScoreText.setText("Score: " + student.getExamScore());
            rankText.setText("Rank: " + (getAdapterPosition() + 1));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStudentClick(student);
                }
            });
        }
    }
}
