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

/**
 * @brief one-line graph
 * @author Joo wan Kim
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class FragmentSingle extends Fragment {

    private LineChart lineChart;

    /**
     * @brief constructor for this
     */
    public FragmentSingle() {
        // Required empty public constructor
    }

    /**
     * @brief one-line graph
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return one-line graph view group
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single, container, false);

        // taking day and time
        ArrayList<Integer[]> Day = ((showGraph)getActivity()).day;
        ArrayList<Integer[]> Time = ((showGraph)getActivity()).time;

        lineChart = (LineChart) view.findViewById(R.id.chart);

        // make entry of taking time data
        List<Entry> entries = new ArrayList<>();
        int i = 0;
        while(i < Day.size()) {
            entries.add(new Entry(Day.get(i)[2], (float)Time.get(i)[0] + (float)Time.get(i)[1]/60));
            i++;
        }

        // set the taking time data
        LineDataSet lineDataSet1 = new LineDataSet(entries, "1회 복용");
        Module.lineSetting(lineDataSet1, getResources().getColor(R.color.once));

        // set the data
        LineData lineData = null;
        if (entries.size() > 0)
            lineData = new LineData(lineDataSet1);
        lineChart.setData(lineData);

        // draw x-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);

        // draw y-axis
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
