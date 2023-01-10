package com.example.eventer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InfoRestaurant extends Fragment{
    private String url = "https://tilenkelc.eu/Eventer/api/user/rentals";
    private RequestQueue requestQueue;
    private String token;

    ArrayList<JSONObject> data = new ArrayList<>();
    private Button pastBtn;
    private Button futureBtn;
    private Button allBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.info_restaurant,container,false);
        token = ((GlobalClass) getActivity().getApplication()).getToken();
        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());

        pastBtn = root.findViewById(R.id.pastReservations);
        futureBtn = root.findViewById(R.id.futureReservations);
        allBtn = root.findViewById(R.id.allBtn);

        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.size() <= 0){
                    Context context = requireActivity().getApplicationContext();
                    int duration = Toast.LENGTH_LONG;

                    CharSequence text = "You have no reservations!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    clear();

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
                    LocalDateTime now = LocalDateTime.now();

                    String today = dtf.format(now);

                    for (int i = 0; i < data.size(); i++){
                        try {
                            JSONObject object = data.get(i);
                            String id = object.getString("id");
                            String category_ime = object.getString("category_name");
                            String termin = object.getString("termin");
                            String date = object.getString("date");
                            String date_check = termin.split(" - ")[0] + " " + object.getString("date");
                            String status = object.getString("status");

                            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

                            LocalDateTime start = LocalDateTime.parse(today, format);
                            LocalDateTime stop = LocalDateTime.parse(date_check, format);

                            if(start.isAfter(stop)){
                                addItem(id, termin, date, category_ime, status);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            }
        });

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.size() <= 0){
                    Context context = requireActivity().getApplicationContext();
                    int duration = Toast.LENGTH_LONG;

                    CharSequence text = "You have no reservations!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    clear();

                    for (int i = 0; i < data.size(); i++){
                        try {
                            JSONObject object = data.get(i);
                            String id = object.getString("id");
                            String category_ime = object.getString("category_name");
                            String termin = object.getString("termin");
                            String date_check = termin.split(" - ")[0] + " " + object.getString("date");
                            String status = object.getString("status");
                            String date = object.getString("date");

                            addItem(id, termin, date, category_ime, status);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            }
        });

        futureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.size() <= 0){
                    Context context = requireActivity().getApplicationContext();
                    int duration = Toast.LENGTH_LONG;

                    CharSequence text = "You have no reservations!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    clear();

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
                    LocalDateTime now = LocalDateTime.now();

                    String today = dtf.format(now);

                    for (int i = 0; i < data.size(); i++){
                        try {
                            JSONObject object = data.get(i);
                            String id = object.getString("id");
                            String category_ime = object.getString("category_name");
                            String termin = object.getString("termin");
                            String date_check = termin.split(" - ")[0] + " " + object.getString("date");
                            String date = object.getString("date");
                            String status = object.getString("status");

                            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

                            LocalDateTime start = LocalDateTime.parse(today, format);
                            LocalDateTime stop = LocalDateTime.parse(date_check, format);

                            if(start.isBefore(stop)){
                                addItem(id, termin, date, category_ime, status);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            }
        });

        prikaziRezervacije();
        return root;
    }

    public void clear(){
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.user_categories_layout);
        ll.removeAllViews();
    }

    private void addItem(String id, String termin, String date, String name, String status) {
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.user_categories_layout);

        switch(status){
            case "successfully_paid":
                status = "Successfully paid";
                break;
            case "canceled":
                status = "Canceled";
                break;
            case "pending":
                status = "Pending";
                break;
            case "in_progress":
                status = "In progress";
                break;
            case "completed":
                status = "Completed";
                break;
        }

        TextView textView = new TextView(getActivity());
        textView.setText("#"+ id + "\n" + name + "\n" + termin + " " + date + "\n" +status + "\n\n");
        textView.setTextSize(20);
        textView.setGravity(1);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GlobalClass) requireActivity().getApplication()).setRentId(id);

                NavHostFragment.findNavController(InfoRestaurant.this)
                        .navigate(R.id.action_InfoRestaurant_to_rent_info);
            }
        });

        ll.addView(textView);
    }

    private void showEmpty() {
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.user_categories_layout);

        TextView textView = new TextView(getActivity());
        textView.setText("You currently have no reservations");
        textView.setTextSize(20);
        textView.setGravity(1);

        ll.addView(textView);
    }



    public void prikaziRezervacije() {

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String id = object.getString("id");
                        String category_ime = object.getString("category_name");
                        String subcategories = object.getString("subcategories");
                        String date = object.getString("date");
                        String termin = object.getString("termin");
                        String status = object.getString("status");

                        addItem(id, termin, date, category_ime, status);
                        data.add(object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if(data.size() <= 0){
                    showEmpty();
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
                headers.put("Authorization", " Bearer " + token.replace("\"", ""));
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(req);
    }
}
