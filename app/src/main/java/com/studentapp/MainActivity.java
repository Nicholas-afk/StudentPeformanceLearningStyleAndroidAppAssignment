package com.studentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HashMap<Student, Integer> studentRankMap;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all UI components and data
        initializeViews();
        loadStudentData();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);
        Button chartButton = findViewById(R.id.chartButton);

        searchButton.setOnClickListener(v -> searchStudent());
        chartButton.setOnClickListener(v -> openChartScreen());
    }

    private void loadStudentData() {
        // Parse CSV file and create student-rank mapping
        studentRankMap = CSVParser.parseStudentsCSV(this, "students.csv");
        if (studentRankMap.isEmpty()) {
            Toast.makeText(this, "Error: No student data loaded", Toast.LENGTH_LONG).show();
        }
    }

    private void setupRecyclerView() {
        // Display top 10 performing students in RecyclerView
        List<Student> topStudents = getTopPerformers();
        adapter = new StudentAdapter(topStudents, this::openStudentDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        findViewById(R.id.searchButton).setOnClickListener(v -> searchStudent());
        findViewById(R.id.chartButton).setOnClickListener(v -> openChartScreen());
    }

    private List<Student> getTopPerformers() {
        // Sort all students by exam score and return top 10
        List<Student> allStudents = new ArrayList<>(studentRankMap.keySet());
        allStudents.sort(Comparator.comparingInt(Student::getExamScore).reversed());
        int limit = Math.min(10, allStudents.size());
        return allStudents.subList(0, limit);
    }

    private void searchStudent() {
        String searchId = searchEditText.getText().toString().trim();
        if (searchId.isEmpty()) {
            Toast.makeText(this, "Please enter a student ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Find student by ID using stream API
        Student foundStudent = studentRankMap.keySet().stream()
                .filter(student -> student.getId().equals(searchId))
                .findFirst()
                .orElse(null);

        if (foundStudent != null) {
            openStudentDetails(foundStudent);
        } else {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openStudentDetails(Student student) {
        // Navigate to student details screen with student ID
        Intent intent = new Intent(this, StudentDetailsActivity.class);
        intent.putExtra("student_id", student.getId());
        startActivity(intent);
    }

    private void openChartScreen() {
        startActivity(new Intent(this, ChartActivity.class));
    }

    public HashMap<Student, Integer> getStudentRankMap() {
        return studentRankMap;
    }
}
