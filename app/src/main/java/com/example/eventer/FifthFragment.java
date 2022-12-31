package com.example.eventer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.DateFormatSymbols;
import java.util.Calendar;


public class FifthFragment extends Fragment {







    private Button pickDateBtn;
    private TextView selectedDateTV;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    int tHour,tMinute;
    private TextView timePickers;
    public String infoDate ="";
    public String infoHour = "";
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_fifth,container,false);
        pickDateBtn = root.findViewById(R.id.idBtnPickDate);
        selectedDateTV = root.findViewById(R.id.idTVSelectedDate);
        timePickers = root.findViewById(R.id.timepickers);
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog,
                        dateSetListener,year,month,day);
                dialog.show();
            }
        });
        timePickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        tHour = hour;
                        tMinute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,tHour,tMinute);
                        timePickers.setText(DateFormat.format("hh:mm aa",calendar));
                        infoHour = DateFormat.format("hh:mm aa",calendar).toString();
                    }
                },12,0,false);
                timePickerDialog.show();
            }
        });



        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
//                Log.d(TAG,"on Date ")
                String date = day + " " + month + " " + year;
                selectedDateTV.setText(date);
                infoDate = date;

            }
        };

        return root;
    }


}
