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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        int numOfTaking = ((showGraph)getActivity()).times;

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
        ArrayList<Integer[]> Day = ((showGraph)getActivity()).day;
        int time;   // 몇번 먹었는지

        switch(numOfTaking) {
            case 1:
                for(int i=0; i<Day.size(); i++) {
                    time = 0;
                    for(int j=i; j < Day.size() && Day.get(i)[2] == Day.get(j)[2]; j++) time++;
                    calendarView.addDecorator(new takingDecoratorSingle(Day.get(i-1)[0], Day.get(i-1)[1], Day.get(i-1)[2], time, getActivity()));
                    i += (time - 1);
                }
                greenText.setText("복용 완료");
                yellow.setVisibility(View.INVISIBLE);
                yellowText.setVisibility(View.INVISIBLE);
                red.setVisibility(View.INVISIBLE);
                redText.setVisibility(View.INVISIBLE);
                gray.setVisibility(View.INVISIBLE);
                grayText.setVisibility(View.INVISIBLE);
                break;
            case 2:
                for(int i=0; i<Day.size(); i++) {
                    time = 0;
                    for(int j=i; j < Day.size() && Day.get(i)[2] == Day.get(j)[2]; j++) time++;
                    calendarView.addDecorator(new takingDecoratorDouble(Day.get(i-1)[0], Day.get(i-1)[1], Day.get(i-1)[2], time, getActivity()));
                    i += (time - 1);
                }
                greenText.setText("복용 완료");
                yellowText.setText("1회 복용");
                red.setVisibility(View.INVISIBLE);
                redText.setVisibility(View.INVISIBLE);
                gray.setVisibility(View.INVISIBLE);
                grayText.setVisibility(View.INVISIBLE);
                break;
            case 3:
                for(int i=0; i<Day.size(); i++) {
                    time = 0;
                    for(int j=i; j < Day.size() && Day.get(i)[2] == Day.get(j)[2]; j++) time++;
                    calendarView.addDecorator(new takingDecoratorTriple(Day.get(i-1)[0], Day.get(i-1)[1], Day.get(i-1)[2], time, getActivity()));
                    i += (time - 1);
                }
                greenText.setText("복용 완료");
                yellowText.setText("2회 복용");
                redText.setText("1회 복용");
                gray.setVisibility(View.INVISIBLE);
                grayText.setVisibility(View.INVISIBLE);
                break;
            case 4:
                for(int i=0; i<Day.size(); i++) {
                    time = 0;
                    for(int j=i; j < Day.size() && Day.get(i)[2] == Day.get(j)[2]; j++) time++;
                    calendarView.addDecorator(new takingDecoratorQuad(Day.get(i)[0], Day.get(i)[1], Day.get(i)[2], time, getActivity()));
                    i += (time - 1);
                }
                greenText.setText("복용 완료");
                yellowText.setText("3회 복용");
                redText.setText("2회 복용");
                grayText.setText("1회 복용");
                break;
        }

        return view;
    }

}
