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

        // Open CSV file from assets folder
        try (InputStream inputStream = context.getAssets().open(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Parse CSV fields (expecting at least 10 columns)
                String[] fields = line.split(",");
                if (fields.length >= 10) {
                    try {
                        Student student = new Student(
                                fields[0].trim(),                    // name
                                Integer.parseInt(fields[1].trim()),  // age
                                fields[2].trim(),                    // gender
                                Integer.parseInt(fields[3].trim()),  // grade level
                                fields[4].trim(),                    // learning style
                                Integer.parseInt(fields[5].trim()),  // study hours
                                Integer.parseInt(fields[7].trim()),  // exam score
                                Integer.parseInt(fields[8].trim()),  // attendance
                                Integer.parseInt(fields[9].trim())   // participation
                        );
                        students.add(student);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing line: " + line, e);
                    }
                }
            }

            // Sort students by exam score (highest first) for ranking
            students.sort(Comparator.comparingInt(Student::getExamScore).reversed());

            // Assign ranks based on sorted order
            for (int i = 0; i < students.size(); i++) {
                studentRankMap.put(students.get(i), i + 1);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error reading CSV file: " + fileName, e);
        }

        return studentRankMap;
    }
}
