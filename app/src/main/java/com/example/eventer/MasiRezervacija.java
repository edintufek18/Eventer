package com.example.eventer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventer.databinding.MasiRezervacijaBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MasiRezervacija extends Fragment {
    private @NonNull
    MasiRezervacijaBinding binding;
    private String url = "https://tilenkelc.eu/Eventer/api/category/products/1";
    private RequestQueue requestQueue;
    private String token;


    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        token = ((GlobalClass) getActivity().getApplication()).getToken();
        binding = MasiRezervacijaBinding.inflate(inflater, container, false);
        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        //prikaziProdukte();

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int table = 1; table < 13; table++) {
            int idView = getResources().getIdentifier("Table" + table, "id", getContext().getPackageName());
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

    public void prikaziProdukte() {

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<String> data = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");

                        data.add(id + " " + name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("REST error", error.getMessage());
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", " Bearer {" + token +"}");
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(req);
    }

}



