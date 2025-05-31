package com.studentapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CSVParser {
    private static final String TAG = "CSVParser";

    public static HashMap<Student, Integer> parseStudentsCSV(Context context, String fileName) {
        HashMap<Student, Integer> studentRankMap = new HashMap<>();
        List<Student> students = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                String[] fields = line.split(",");
                if (fields.length >= 9) {
                    try {
                        Student student = new Student(
                                fields[0].trim(), // id
                                Integer.parseInt(fields[1].trim()), // age
                                fields[2].trim(), // gender
                                Integer.parseInt(fields[3].trim()), // studyHoursPerWeek
                                fields[4].trim(), // preferredLearningStyle
                                Integer.parseInt(fields[5].trim()), // onlineCoursesCompleted
                                Integer.parseInt(fields[6].trim()), // assignmentCompletionRate
                                Integer.parseInt(fields[7].trim()), // examScore
                                Integer.parseInt(fields[8].trim())  // attendanceRate
                        );
                        students.add(student);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing line: " + line, e);
                    }
                }
            }
            reader.close();

            // Sort students by exam score in descending order
            Collections.sort(students, new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    return Integer.compare(s2.getExamScore(), s1.getExamScore());
                }
            });

            // Assign ranks based on sorted order
            for (int i = 0; i < students.size(); i++) {
                studentRankMap.put(students.get(i), i + 1);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error reading CSV file", e);
        }

        return studentRankMap;
    }
}
