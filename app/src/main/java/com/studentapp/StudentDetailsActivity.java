package com.studentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentDetailsActivity extends AppCompatActivity {
    private TextView studentIdText, ageText, genderText, studyHoursText;
    private TextView assignmentRateText, examScoreText, correlationText;
    private Student currentStudent;
    private HashMap<Student, Integer> studentRankMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        // Setup action bar with back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Student Details");
        }

        // Initialize UI and load data for selected student
        initializeViews();
        loadStudentData();
        displayStudentDetails();
        calculateCorrelation();
    }

    private void initializeViews() {
        studentIdText = findViewById(R.id.studentIdText);
        ageText = findViewById(R.id.ageText);
        genderText = findViewById(R.id.genderText);
        studyHoursText = findViewById(R.id.studyHoursText);
        assignmentRateText = findViewById(R.id.assignmentRateText);
        examScoreText = findViewById(R.id.examScoreText);
        correlationText = findViewById(R.id.correlationText);
    }

    private void loadStudentData() {
        // Get student ID from intent and find matching student
        String studentId = getIntent().getStringExtra("student_id");
        studentRankMap = CSVParser.parseStudentsCSV(this, "students.csv");

        currentStudent = studentRankMap.keySet().stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    private void displayStudentDetails() {
        // Populate UI with student information
        if (currentStudent != null) {
            studentIdText.setText(String.format("Student ID: %s", currentStudent.getId()));
            ageText.setText(String.format("Age: %d", currentStudent.getAge()));
            genderText.setText(String.format("Gender: %s", currentStudent.getGender()));
            studyHoursText.setText(String.format("Study Hours/Week: %d", currentStudent.getStudyHoursPerWeek()));
            assignmentRateText.setText(String.format("Assignment Completion Rate: %d%%", currentStudent.getAssignmentCompletionRate()));
            examScoreText.setText(String.format("Exam Score: %d", currentStudent.getExamScore()));
        }
    }

    private void calculateCorrelation() {
        // Calculate Pearson correlation between study hours and exam scores
        List<Student> allStudents = new ArrayList<>(studentRankMap.keySet());
        if (allStudents.size() < 2) {
            correlationText.setText("Correlation: Not enough data");
            return;
        }

        // Calculate means for both variables
        double meanStudyHours = allStudents.stream()
                .mapToDouble(Student::getStudyHoursPerWeek)
                .average().orElse(0.0);

        double meanExamScore = allStudents.stream()
                .mapToDouble(Student::getExamScore)
                .average().orElse(0.0);

        // Calculate correlation using Pearson formula
        double numerator = 0, sumSquaredDiffX = 0, sumSquaredDiffY = 0;

        for (Student student : allStudents) {
            double diffX = student.getStudyHoursPerWeek() - meanStudyHours;
            double diffY = student.getExamScore() - meanExamScore;
            numerator += diffX * diffY;
            sumSquaredDiffX += diffX * diffX;
            sumSquaredDiffY += diffY * diffY;
        }

        double denominator = Math.sqrt(sumSquaredDiffX * sumSquaredDiffY);
        double correlation = denominator != 0 ? numerator / denominator : 0;

        correlationText.setText(String.format("Correlation (Study Hours vs Exam Score): %.3f", correlation));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button press
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
