package com.studentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private HashMap<Student, Integer> studentRankMap;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private EditText searchEditText;
    private Button searchButton, chartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        loadData();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        chartButton = findViewById(R.id.chartButton);
    }

    private void loadData() {
        studentRankMap = CSVParser.parseStudentsCSV(this, "students.csv");
    }

    private void setupRecyclerView() {
        List<Student> topStudents = getTopPerformers();
        adapter = new StudentAdapter(topStudents, new StudentAdapter.OnStudentClickListener() {
            @Override
            public void onStudentClick(Student student) {
                openStudentDetails(student);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStudent();
            }
        });

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChartScreen();
            }
        });
    }

    private List<Student> getTopPerformers() {
        List<Student> allStudents = new ArrayList<>(studentRankMap.keySet());
        Collections.sort(allStudents, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Integer.compare(s2.getExamScore(), s1.getExamScore());
            }
        });

        // Return top 10 or all if less than 10
        int limit = Math.min(10, allStudents.size());
        return allStudents.subList(0, limit);
    }

    private void searchStudent() {
        String searchId = searchEditText.getText().toString().trim();
        if (searchId.isEmpty()) {
            Toast.makeText(this, "Please enter a student ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Student foundStudent = null;
        for (Student student : studentRankMap.keySet()) {
            if (student.getId().equals(searchId)) {
                foundStudent = student;
                break;
            }
        }

        if (foundStudent != null) {
            openStudentDetails(foundStudent);
        } else {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openStudentDetails(Student student) {
        Intent intent = new Intent(this, StudentDetailsActivity.class);
        intent.putExtra("student_id", student.getId());
        startActivity(intent);
    }

    private void openChartScreen() {
        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }

    public HashMap<Student, Integer> getStudentRankMap() {
        return studentRankMap;
    }
}
