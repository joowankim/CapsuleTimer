package com.example.knight.a2018_mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
 * @brief
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class FragmentTriple extends Fragment {

    private LineChart lineChart;

    public FragmentTriple() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_triple, container, false);

        ArrayList<Integer[]> Day = ((showGraph)getActivity()).day;
        ArrayList<Integer[]> Time = ((showGraph)getActivity()).time;

        lineChart = (LineChart) view.findViewById(R.id.chart);

        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();

        int i = 0;
        while (i < Day.size()) {
            entries1.add(new Entry(Day.get(i)[2], (float)Time.get(i)[0] + (float)Time.get(i)[1]/60));
            i++;
            if(i < Day.size() && Day.get(i) != null && Day.get(i-1)[2] == Day.get(i)[2]) {
                entries2.add(new Entry(Day.get(i)[2], (float)Time.get(i)[0] + (float)Time.get(i)[1]/60));
                i++;
                if(i < Day.size() && Day.get(i-1)[2] == Day.get(i)[2]) {
                    entries3.add(new Entry(Day.get(i)[2], (float)Time.get(i)[0] + (float)Time.get(i)[1]/60));
                    i++;
                }
            }
        }

        LineDataSet lineDataSet1 = new LineDataSet(entries1, "1회 복용");
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setCircleRadius(6);
        lineDataSet1.setCircleColor(getResources().getColor(R.color.once));
        lineDataSet1.setCircleColorHole(Color.BLUE);
        lineDataSet1.setColor(getResources().getColor(R.color.once));
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setDrawCircles(true);

        LineDataSet lineDataSet2 = new LineDataSet(entries2, "2회 복용");
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleColor(getResources().getColor(R.color.twice));
        lineDataSet2.setCircleColorHole(Color.RED);
        lineDataSet2.setColor(getResources().getColor(R.color.twice));
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);

        LineDataSet lineDataSet3 = new LineDataSet(entries3, "3회 복용");
        lineDataSet3.setLineWidth(2);
        lineDataSet3.setCircleRadius(6);
        lineDataSet3.setCircleColor(getResources().getColor(R.color.triple));
        lineDataSet3.setCircleColorHole(Color.RED);
        lineDataSet3.setColor(getResources().getColor(R.color.triple));
        lineDataSet3.setDrawCircleHole(true);
        lineDataSet3.setDrawCircles(true);

        LineData lineData = null;

        if (entries1.size() > 0 && entries2.size() > 0 && entries3.size() > 0)
            lineData = new LineData(lineDataSet1, lineDataSet2, lineDataSet3);
        else if (entries1.size() > 0 && entries2.size() > 0)
            lineData = new LineData(lineDataSet1, lineDataSet2);
        else if (entries1.size() > 0)
            lineData = new LineData(lineDataSet1);
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
