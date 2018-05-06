package com.example.knight.a2018_mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;



public class FragmentDouble extends Fragment {

    private LineChart lineChart;

    public FragmentDouble() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single, container, false);

        lineChart = (LineChart) view.findViewById(R.id.chart);

        // x is date, y is time that user takes medicine
        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(1, 1));
        entries1.add(new Entry(2, 2));
        entries1.add(new Entry(3, 0));
        entries1.add(new Entry(4, 4));
        entries1.add(new Entry(5, 3));

        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(1, 3));
        entries2.add(new Entry(2, 2));
        entries2.add(new Entry(3, 1));
        entries2.add(new Entry(4, 5));
        entries2.add(new Entry(5, 0));

        LineDataSet lineDataSet1 = new LineDataSet(entries1, "속성명1");
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setCircleRadius(6);
        lineDataSet1.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet1.setCircleColorHole(Color.BLUE);
        lineDataSet1.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setDrawCircles(true);

        LineDataSet lineDataSet2 = new LineDataSet(entries2, "속성명2");
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleColor(Color.parseColor("#FF00B4DC"));
        lineDataSet2.setCircleColorHole(Color.RED);
        lineDataSet2.setColor(Color.parseColor("#FF00B4DC"));
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);

        LineData lineData = new LineData(lineDataSet1, lineDataSet2);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

        return view;
    }

}