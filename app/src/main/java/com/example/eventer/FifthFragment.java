package com.example.eventer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FifthFragment extends Fragment {

    private Button pickDateBtn;
    private TextView selectedDateTV;
    private TextView selectedTimeText;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String url = "https://tilenkelc.eu/Eventer/api/rent/full/";
    private RequestQueue requestQueue;
    private String token;
    private Spinner timeSpinner;
    private Button proceedBtn;

    private String subcategory_id;
    private String opens_at;
    private String closes_at;

    private Map<String, List<Integer>> taken = new HashMap<String, List<Integer>>();

    public String infoDate ="";
    public String infoHour = "";

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_fifth,container,false);

        pickDateBtn = root.findViewById(R.id.idBtnPickDate);
        selectedDateTV = root.findViewById(R.id.idTVSelectedDate);
        proceedBtn = root.findViewById(R.id.proceedBtn);
        selectedTimeText = root.findViewById(R.id.selectedTimeText);

        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        token = ((GlobalClass) getActivity().getApplication()).getToken();

        subcategory_id = ((GlobalClass) getActivity().getApplication()).getSubcategory();
        opens_at = ((GlobalClass) getActivity().getApplication()).getOpens_at();
        closes_at = ((GlobalClass) getActivity().getApplication()).getCloses_at();

        timeSpinner = root.findViewById(R.id.timeSpinner);
        timeSpinner.setVisibility(View.GONE);
        selectedTimeText.setVisibility(View.GONE);
        prikaziAvailable();

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog,
                        dateSetListener,year,month,day);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = requireActivity().getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                if(infoDate.isEmpty() || infoDate.equals("")) {
                    CharSequence text = "Please select a date first!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else if(infoHour.isEmpty() || infoHour.equals("")) {
                    CharSequence text = "Please select a time first!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else if(infoHour.equals("Select a time slot")) {
                    CharSequence text = "Select a valid time slot!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else{
                    ((GlobalClass) requireActivity().getApplication()).setDate(infoDate);
                    ((GlobalClass) requireActivity().getApplication()).setTime(infoHour);

                    NavHostFragment.findNavController(FifthFragment.this)
                            .navigate(R.id.action_FifthFragment_to_Bank);
                }
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                selectedDateTV.setText("Your current selected date is: " + day + "." + month + "." + year);

                infoDate = year + "-";
                if(month < 10){
                    infoDate += "0" + month + "-";
                }else{
                    infoDate += month + "-";
                }

                if(day < 10){
                    infoDate += "0" + day;
                }else{
                    infoDate += day;
                }

                addTime();
            }
        };
        return root;
    }

    public void clear(){
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, Collections.emptyList());
        timeSpinner.setAdapter(adapter);
    }

    public void addTime(){
        timeSpinner.setVisibility(View.VISIBLE);
        selectedTimeText.setVisibility(View.VISIBLE);
        clear();

        ArrayAdapter<String> adapter;

        int opens = Integer.parseInt(opens_at.split(":")[0]);
        int closes = Integer.parseInt(closes_at.split(":")[0]);

        List<String> list = new ArrayList<String>();
        list.add("Select a time slot");

        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Date date = new Date();
        int current_time = Integer.parseInt(formatter.format(date)) + 1;

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        String current_date = formatter.format(date);

        for(int current = opens; current < closes; current++){
            List<Integer> tmp = new ArrayList<>();

            if(taken.containsKey(infoDate)){
                tmp = taken.get(infoDate);
            }

            if(tmp.isEmpty() || !tmp.contains(current)){
                if(infoDate.equals(current_date)){
                    if(current > current_time){
                        String from = "";
                        if(current < 10){
                            from = "0" + String.valueOf(current);
                        }else{
                            from = String.valueOf(current);
                        }

                        String to = "";
                        if((current + 1) < 10){
                            to = "0" + String.valueOf(current + 1);
                        }else{
                            to = String.valueOf(current + 1);
                        }

                        String item = from + ":00 - " +  to + ":00";
                        list.add(item);
                    }
                }else{
                    String from = "";
                    if(current < 10){
                        from = "0" + String.valueOf(current);
                    }else{
                        from = String.valueOf(current);
                    }

                    String to = "";
                    if((current + 1) < 10){
                        to = "0" + String.valueOf(current + 1);
                    }else{
                        to = String.valueOf(current + 1);
                    }

                    String item = from + ":00 - " +  to + ":00";
                    list.add(item);
                }
            }
        }

        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
            android.R.layout.simple_spinner_dropdown_item, list);

        timeSpinner.setAdapter(adapter);

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                infoHour = selectedItem;
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
                // Nothing
            }
        });
    }

    /*public void prikaziAvailable() {
        JsonArrayRequest request = new JsonArrayRequest(url + subcategory_id, jsonArrayListener, errorListener);
        requestQueue.add(request);
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    String date = object.getString("date");
                    String time_from = object.getString("time_from");
                    String time_to = object.getString("time_to");

                    int from = Integer.parseInt(time_from.split(":")[0]);
                    int to = Integer.parseInt(time_to.split(":")[0]);

                    List<Integer> tmp = new ArrayList<Integer>();
                    for(int counter = from; counter < to; counter++){
                        tmp.add(counter);
                    }
                    taken.put(date, tmp);

                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            System.out.println(taken);
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };*/

    public void prikaziAvailable() {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url + subcategory_id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //ArrayList<String> data = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String date = object.getString("date");
                        String time_from = object.getString("time_from");
                        String time_to = object.getString("time_to");

                        int from = Integer.parseInt(time_from.split(":")[0]);
                        int to = Integer.parseInt(time_to.split(":")[0]);

                        List<Integer> tmp = new ArrayList<Integer>();
                        for(int counter = from; counter < to; counter++){
                            tmp.add(counter);
                        }
                        taken.put(date, tmp);

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
                headers.put("Authorization", " Bearer " + token.replace("\"", ""));
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(req);
    }
}
