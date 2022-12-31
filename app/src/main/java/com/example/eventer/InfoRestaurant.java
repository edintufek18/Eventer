package com.example.eventer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.example.eventer.databinding.InfoRestaurantBinding;



public class InfoRestaurant extends Fragment{




    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.info_restaurant,container,false);
        FrameLayout frameLayout = root.findViewById(R.id.frameLayout);



        return root;
    }
}
