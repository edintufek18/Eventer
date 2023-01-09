package com.example.eventer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RentInfo extends Fragment{
    private String url = "https://tilenkelc.eu/Eventer/api/user/rent/";
    private String cancelUrl = "https://tilenkelc.eu/Eventer/api/rent/cancel";
    private RequestQueue requestQueue;
    private String token;
    private String rent_id;

    private TextView nameField;
    private TextView streetField;
    private TextView cityField;
    private TextView userNameField;
    private TextView emailField;
    private TextView mobileField;
    private TextView tableField;
    private TextView dateField;
    private TextView slotField;
    private TextView statusField;

    private Button cancel;


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.rent_info,container,false);
        token = ((GlobalClass) getActivity().getApplication()).getToken();
        rent_id = ((GlobalClass) getActivity().getApplication()).getRentId();
        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());

        nameField = root.findViewById(R.id.nameField);
        streetField = root.findViewById(R.id.streetField);
        cityField = root.findViewById(R.id.cityField);
        userNameField = root.findViewById(R.id.userNameField);
        emailField = root.findViewById(R.id.emailField);
        mobileField = root.findViewById(R.id.mobileField);
        tableField = root.findViewById(R.id.tableField);
        dateField = root.findViewById(R.id.dateField);
        slotField = root.findViewById(R.id.slotField);
        statusField = root.findViewById(R.id.statusField);
        cancel = root.findViewById(R.id.cancelReservation);
        cancel.setVisibility(View.GONE);

        prikaziRezervacijo();
        return root;
    }

    public void prikaziRezervacijo() {

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url + rent_id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);

                        String category_name = object.getString("category_name");
                        String category_street = object.getString("category_street");
                        String category_city = object.getString("category_city");

                        String customer_name = object.getString("customer_name") + " " + object.getString("customer_surname");
                        String customer_email = object.getString("customer_email");
                        String customer_phone_number = object.getString("customer_phone_number");

                        String subcategory = object.getString("subcategory");
                        String date = object.getString("date");
                        String slot = object.getString("termin");

                        String status = object.getString("status");

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

                        nameField.setText(category_name);
                        streetField.setText(category_street);
                        cityField.setText(category_city);

                        userNameField.setText(customer_name);
                        emailField.setText(customer_email);
                        mobileField.setText(customer_phone_number);

                        subcategory = subcategory.replace("[", "");
                        subcategory = subcategory.replace("]", "");
                        subcategory = subcategory.replace("\"", "");

                        tableField.setText(subcategory);
                        dateField.setText(date);
                        slotField.setText(slot);

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
                        LocalDateTime now = LocalDateTime.now();

                        String today = dtf.format(now);

                        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

                        date = slot.split(" - ")[0] + " " + date;

                        LocalDateTime start = LocalDateTime.parse(today, format);
                        LocalDateTime stop = LocalDateTime.parse(date, format);

                        if(start.isBefore(stop) && status.equals("Successfully paid")){
                            cancel.setVisibility(View.VISIBLE);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cancelReservation();
                                }
                            });
                        }

                        statusField.setText("Status: " +status);

                        System.out.println(response);

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

    public void cancelReservation(){
        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", rent_id);

            final String mRequestBody = jsonBody.toString();

            System.out.println(mRequestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, cancelUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObject = new JSONObject(response);
                        String status = jObject.getString("status");

                        if(status.equals("200")){
                            NavHostFragment.findNavController(RentInfo.this)
                                    .navigate(R.id.action_rent_info_self);
                        }else{
                            Context context = requireActivity().getApplicationContext();
                            int duration = Toast.LENGTH_LONG;

                            CharSequence text = "An error occured while canceling reservation!";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY",error.toString());
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer" + " " + token.replace("\"", ""));
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {

                        try {
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException e) {
                            new String(response.data);
                        }

                        System.out.println(responseString);

                    }
                    return super.parseNetworkResponse(response);
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };
            requestQueue.add(stringRequest);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
