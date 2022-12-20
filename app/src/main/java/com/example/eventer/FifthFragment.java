package com.example.eventer;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eventer.databinding.FragmentFifthBinding;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;
import android.widget.Toast;
import java.util.Date;


public class FifthFragment extends Fragment{


    private FragmentFifthBinding binding;
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFifthBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    DayScrollDatePicker datePicker;
    String selectDate;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState,ViewGroup container) {
        AppCompatActivity compActivity = new AppCompatActivity();
        super.onViewCreated(view, savedInstanceState);

        compActivity.setContentView(R.layout.fragment_fifth);
        datePicker = compActivity.findViewById(R.id.date_picker_scroll_day_month);
        datePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                selectDate = date.toString();


            }
        });

    }

}
