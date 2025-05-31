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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {
    private BarChart barChart;
    private HashMap<Student, Integer> studentRankMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learning Style Performance");
        }

        barChart = findViewById(R.id.barChart);
        studentRankMap = CSVParser.parseStudentsCSV(this, "students.csv");

        setupChart();
    }

    private void setupChart() {
        Map<String, List<Integer>> learningStyleScores = new HashMap<>();

        // Group scores by learning style
        for (Student student : studentRankMap.keySet()) {
            String style = student.getPreferredLearningStyle();
            if (!learningStyleScores.containsKey(style)) {
                learningStyleScores.put(style, new ArrayList<>());
            }
            learningStyleScores.get(style).add(student.getExamScore());
        }

        // Calculate averages and create chart entries
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, List<Integer>> entry : learningStyleScores.entrySet()) {
            String style = entry.getKey();
            List<Integer> scores = entry.getValue();

            double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);

            entries.add(new BarEntry(index, (float) average));
            labels.add(style);
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Average Exam Scores by Learning Style");

        // Set colors
        int[] colors = {
                Color.rgb(255, 102, 102), // Light Red
                Color.rgb(102, 178, 255), // Light Blue
                Color.rgb(255, 204, 102), // Light Orange
                Color.rgb(153, 204, 153)  // Light Green
        };
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);

        // Configure chart
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        // Configure X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setDrawGridLines(false);

        // Configure Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(12f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Configure legend
        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);

        barChart.animateY(1000);
        barChart.invalidate();
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
