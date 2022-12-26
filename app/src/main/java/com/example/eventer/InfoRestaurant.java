package com.example.eventer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.example.eventer.databinding.InfoRestaurantBinding;

public class InfoRestaurant extends Fragment{
    private InfoRestaurantBinding binding;
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = InfoRestaurantBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }
}
