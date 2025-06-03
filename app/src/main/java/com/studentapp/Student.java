package com.studentapp;

import java.util.Objects;

public class Student {
    private String id;
    private int age;
    private String gender;
    private int studyHoursPerWeek;
    private String preferredLearningStyle;
    private int onlineCoursesCompleted;
    private int assignmentCompletionRate;
    private int examScore;
    private int attendanceRate;

    public Student() {}

    // Constructor with validation through setters
    public Student(String id, int age, String gender, int studyHoursPerWeek,
                   String preferredLearningStyle, int onlineCoursesCompleted,
                   int assignmentCompletionRate, int examScore, int attendanceRate) {
        setId(id);
        setAge(age);
        setGender(gender);
        setStudyHoursPerWeek(studyHoursPerWeek);
        setPreferredLearningStyle(preferredLearningStyle);
        setOnlineCoursesCompleted(onlineCoursesCompleted);
        setAssignmentCompletionRate(assignmentCompletionRate);
        setExamScore(examScore);
        setAttendanceRate(attendanceRate);
    }

    // Standard getters
    public String getId() { return id; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public int getStudyHoursPerWeek() { return studyHoursPerWeek; }
    public String getPreferredLearningStyle() { return preferredLearningStyle; }
    public int getOnlineCoursesCompleted() { return onlineCoursesCompleted; }
    public int getAssignmentCompletionRate() { return assignmentCompletionRate; }
    public int getExamScore() { return examScore; }
    public int getAttendanceRate() { return attendanceRate; }

    // Validated setters with input sanitization
    public void setId(String id) {
        this.id = (id != null && !id.trim().isEmpty()) ? id.trim() : this.id;
    }

    public void setAge(int age) {
        this.age = (age > 0 && age < 120) ? age : this.age; // Reasonable age range
    }

    public void setGender(String gender) {
        this.gender = (gender != null && !gender.trim().isEmpty()) ? gender.trim() : this.gender;
    }

    public void setStudyHoursPerWeek(int studyHoursPerWeek) {
        // Max 168 hours per week (24*7)
        this.studyHoursPerWeek = (studyHoursPerWeek >= 0 && studyHoursPerWeek <= 168) ? studyHoursPerWeek : this.studyHoursPerWeek;
    }

    public void setPreferredLearningStyle(String preferredLearningStyle) {
        this.preferredLearningStyle = (preferredLearningStyle != null && !preferredLearningStyle.trim().isEmpty()) ?
                preferredLearningStyle.trim() : this.preferredLearningStyle;
    }

    public void setOnlineCoursesCompleted(int onlineCoursesCompleted) {
        this.onlineCoursesCompleted = Math.max(0, onlineCoursesCompleted); // Non-negative values only
    }

    public void setAssignmentCompletionRate(int assignmentCompletionRate) {
        this.assignmentCompletionRate = Math.max(0, Math.min(100, assignmentCompletionRate)); // 0-100% range
    }

    public void setExamScore(int examScore) {
        this.examScore = Math.max(0, Math.min(100, examScore)); // 0-100 score range
    }

    public void setAttendanceRate(int attendanceRate) {
        this.attendanceRate = Math.max(0, Math.min(100, attendanceRate)); // 0-100% range
    }

    // Equality based on student ID only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', examScore=%d}", id, examScore);
    }
}
