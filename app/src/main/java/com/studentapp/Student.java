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

    public Student(String id, int age, String gender, int studyHoursPerWeek,
                   String preferredLearningStyle, int onlineCoursesCompleted,
                   int assignmentCompletionRate, int examScore, int attendanceRate) {
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.studyHoursPerWeek = studyHoursPerWeek;
        this.preferredLearningStyle = preferredLearningStyle;
        this.onlineCoursesCompleted = onlineCoursesCompleted;
        this.assignmentCompletionRate = assignmentCompletionRate;
        this.examScore = examScore;
        this.attendanceRate = attendanceRate;
    }

    // Getters
    public String getId() { return id; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public int getStudyHoursPerWeek() { return studyHoursPerWeek; }
    public String getPreferredLearningStyle() { return preferredLearningStyle; }
    public int getOnlineCoursesCompleted() { return onlineCoursesCompleted; }
    public int getAssignmentCompletionRate() { return assignmentCompletionRate; }
    public int getExamScore() { return examScore; }
    public int getAttendanceRate() { return attendanceRate; }

    // Setters with validation
    public void setId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.id = id;
        }
    }

    public void setAge(int age) {
        if (age > 0 && age < 120) {
            this.age = age;
        }
    }

    public void setGender(String gender) {
        if (gender != null && !gender.trim().isEmpty()) {
            this.gender = gender;
        }
    }

    public void setStudyHoursPerWeek(int studyHoursPerWeek) {
        if (studyHoursPerWeek >= 0 && studyHoursPerWeek <= 168) {
            this.studyHoursPerWeek = studyHoursPerWeek;
        }
    }

    public void setPreferredLearningStyle(String preferredLearningStyle) {
        if (preferredLearningStyle != null && !preferredLearningStyle.trim().isEmpty()) {
            this.preferredLearningStyle = preferredLearningStyle;
        }
    }

    public void setOnlineCoursesCompleted(int onlineCoursesCompleted) {
        if (onlineCoursesCompleted >= 0) {
            this.onlineCoursesCompleted = onlineCoursesCompleted;
        }
    }

    public void setAssignmentCompletionRate(int assignmentCompletionRate) {
        if (assignmentCompletionRate >= 0 && assignmentCompletionRate <= 100) {
            this.assignmentCompletionRate = assignmentCompletionRate;
        }
    }

    public void setExamScore(int examScore) {
        if (examScore >= 0 && examScore <= 100) {
            this.examScore = examScore;
        }
    }

    public void setAttendanceRate(int attendanceRate) {
        if (attendanceRate >= 0 && attendanceRate <= 100) {
            this.attendanceRate = attendanceRate;
        }
    }

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
        return "Student{" +
                "id='" + id + '\'' +
                ", examScore=" + examScore +
                '}';
    }
}
