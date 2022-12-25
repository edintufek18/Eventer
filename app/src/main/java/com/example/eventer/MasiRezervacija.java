package com.example.eventer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eventer.databinding.MasiRezervacijaBinding;

public class MasiRezervacija extends Fragment{
    private @NonNull MasiRezervacijaBinding binding;
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = MasiRezervacijaBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int table = 1; table < 13; table++) {
            int idView = getResources().getIdentifier("Table" + table,"id",getContext().getPackageName());
            View eventView = view.findViewById(idView);
            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(MasiRezervacija.this)
                            .navigate(R.id.action_MasiRezervacija_to_FifthFragment);
                }
            });
            
        }
    }
}
