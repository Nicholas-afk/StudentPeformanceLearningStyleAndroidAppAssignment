package com.studentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartActivity extends AppCompatActivity {
    private BarChart barChart;
    private HashMap<Student, Integer> studentRankMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // Setup action bar with back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learning Style Performance");
        }

        // Initialize chart and load student data
        barChart = findViewById(R.id.barChart);
        studentRankMap = CSVParser.parseStudentsCSV(this, "students.csv");
        setupChart();
    }

    private void setupChart() {
        // Group students by learning style and collect their exam scores
        Map<String, List<Integer>> learningStyleScores = studentRankMap.keySet().stream()
                .collect(Collectors.groupingBy(
                        Student::getPreferredLearningStyle,
                        Collectors.mapping(Student::getExamScore, Collectors.toList())
                ));

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        // Calculate average scores for each learning style
        for (Map.Entry<String, List<Integer>> entry : learningStyleScores.entrySet()) {
            String style = entry.getKey();
            List<Integer> scores = entry.getValue();
            double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);

            entries.add(new BarEntry(index, (float) average));
            labels.add(style);
            index++;
        }

        // Create dataset with colors and styling
        BarDataSet dataSet = new BarDataSet(entries, "Average Exam Scores by Learning Style");

        int[] colors = {
                Color.rgb(52, 152, 219),
                Color.rgb(231, 76, 60),
                Color.rgb(46, 204, 113),
                Color.rgb(241, 196, 15)
        };
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);

        // Configure chart appearance and behavior
        configureChartLayout();
        configureAxes(labels);
        configureLegend();

        // Apply data and animate chart
        barChart.setData(barData);
        barChart.animateY(1200);
        barChart.invalidate();
    }

    private void configureChartLayout() {
        barChart.setExtraOffsets(16f, 16f, 16f, 40f);

        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setScaleEnabled(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
    }

    private void configureAxes(List<String> labels) {
        // Setup X-axis with learning style labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(Color.DKGRAY);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setLabelRotationAngle(-15f);

        // Setup Y-axis for score values (0-100)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setTextSize(12f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setAxisLineColor(Color.DKGRAY);
        leftAxis.setTextColor(Color.DKGRAY);
        leftAxis.setGranularity(10f);

        barChart.getAxisRight().setEnabled(false);
    }

    private void configureLegend() {
        Legend legend = barChart.getLegend();
        legend.setEnabled(true);

        legend.setDrawInside(false);

        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(12f);
        legend.setTextSize(11f);
        legend.setTextColor(Color.DKGRAY);
        legend.setXEntrySpace(8f);
        legend.setYEntrySpace(4f);
        legend.setFormToTextSpace(4f);

        legend.setYOffset(8f);
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
