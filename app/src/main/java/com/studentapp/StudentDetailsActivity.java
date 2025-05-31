package com.studentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class StudentDetailsActivity extends AppCompatActivity {
    private TextView studentIdText, ageText, genderText, studyHoursText,
            assignmentRateText, examScoreText, correlationText;
    private Student currentStudent;
    private HashMap<Student, Integer> studentRankMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadData();
        displayStudentDetails();
        calculateCorrelation();
    }

    private void initViews() {
        studentIdText = findViewById(R.id.studentIdText);
        ageText = findViewById(R.id.ageText);
        genderText = findViewById(R.id.genderText);
        studyHoursText = findViewById(R.id.studyHoursText);
        assignmentRateText = findViewById(R.id.assignmentRateText);
        examScoreText = findViewById(R.id.examScoreText);
        correlationText = findViewById(R.id.correlationText);
    }

    private void loadData() {
        String studentId = getIntent().getStringExtra("student_id");
        studentRankMap = CSVParser.parseStudentsCSV(this, "students.csv");

        for (Student student : studentRankMap.keySet()) {
            if (student.getId().equals(studentId)) {
                currentStudent = student;
                break;
            }
        }
    }

    private void displayStudentDetails() {
        if (currentStudent != null) {
            studentIdText.setText("Student ID: " + currentStudent.getId());
            ageText.setText("Age: " + currentStudent.getAge());
            genderText.setText("Gender: " + currentStudent.getGender());
            studyHoursText.setText("Study Hours/Week: " + currentStudent.getStudyHoursPerWeek());
            assignmentRateText.setText("Assignment Completion Rate: " + currentStudent.getAssignmentCompletionRate() + "%");
            examScoreText.setText("Exam Score: " + currentStudent.getExamScore());
        }
    }

    private void calculateCorrelation() {
        List<Student> allStudents = new ArrayList<>(studentRankMap.keySet());

        if (allStudents.size() < 2) {
            correlationText.setText("Correlation: Not enough data");
            return;
        }

        double meanStudyHours = 0, meanExamScore = 0;

        // Calculate means
        for (Student student : allStudents) {
            meanStudyHours += student.getStudyHoursPerWeek();
            meanExamScore += student.getExamScore();
        }
        meanStudyHours /= allStudents.size();
        meanExamScore /= allStudents.size();

        double numerator = 0, sumSquaredDiffX = 0, sumSquaredDiffY = 0;

        // Calculate correlation coefficient
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
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
