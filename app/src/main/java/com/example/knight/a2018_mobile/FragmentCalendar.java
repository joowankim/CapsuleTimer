package com.example.knight.a2018_mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentCalendar extends Fragment {

    private MaterialCalendarView calendarView;

    private ImageView green;
    private ImageView yellow;
    private ImageView red;
    private ImageView gray;

    private TextView greenText;
    private TextView yellowText;
    private TextView redText;
    private TextView grayText;

    public FragmentCalendar() {
        // Required empty public constructor
    }

    List dates = new ArrayList();
    List times = new ArrayList();

    int numOfTaking = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        // selected date's color change
        calendarView.setSelectionColor(Color.parseColor("#00BCD4"));
        // start and end of calendar
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        // decorate weekend and today
        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new oneDayDecorator());

        green = (ImageView) view.findViewById(R.id.green);
        yellow = (ImageView) view.findViewById(R.id.yellow);
        red = (ImageView) view.findViewById(R.id.red);
        gray = (ImageView) view.findViewById(R.id.gray);

        greenText = (TextView) view.findViewById(R.id.greenText);
        yellowText = (TextView) view.findViewById(R.id.yellowText);
        redText = (TextView) view.findViewById(R.id.redText);
        grayText = (TextView) view.findViewById(R.id.grayText);

        // decorate days of taking medicine
        String date = "20180512";
        int time = 1;

        switch(numOfTaking) {// 각 case에 루프 추가해서 모든 약먹은 날짜에 대해 decorate 해야함
            case 1:
                calendarView.addDecorator(new takingDecoratorSingle(date, time, getActivity()));
                greenText.setText("복용 완료");
                yellow.setVisibility(View.INVISIBLE);
                yellowText.setVisibility(View.INVISIBLE);
                red.setVisibility(View.INVISIBLE);
                redText.setVisibility(View.INVISIBLE);
                gray.setVisibility(View.INVISIBLE);
                grayText.setVisibility(View.INVISIBLE);
                break;
            case 2:
                calendarView.addDecorator(new takingDecoratorDouble(date, time, getActivity()));
                greenText.setText("복용 완료");
                yellowText.setText("1회 복용");
                red.setVisibility(View.INVISIBLE);
                redText.setVisibility(View.INVISIBLE);
                gray.setVisibility(View.INVISIBLE);
                grayText.setVisibility(View.INVISIBLE);
                break;
            case 3:
                calendarView.addDecorator(new takingDecoratorTriple(date, time, getActivity()));
                greenText.setText("복용 완료");
                yellowText.setText("2회 복용");
                redText.setText("1회 복용");
                gray.setVisibility(View.INVISIBLE);
                grayText.setVisibility(View.INVISIBLE);
                break;
            case 4:
                calendarView.addDecorator(new takingDecoratorQuad(date, time, getActivity()));
                greenText.setText("복용 완료");
                yellowText.setText("3회 복용");
                redText.setText("2회 복용");
                grayText.setText("1회 복용");
                break;
        }



        return view;
    }

}
