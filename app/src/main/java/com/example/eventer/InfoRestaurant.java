package com.example.eventer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

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
import com.example.eventer.databinding.InfoRestaurantBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InfoRestaurant extends Fragment{
    private String url = "https://tilenkelc.eu/Eventer/api/user/rentals";
    private RequestQueue requestQueue;
    private String token;




    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.info_restaurant,container,false);
        FrameLayout frameLayout = root.findViewById(R.id.frameLayout);
        token = ((GlobalClass) getActivity().getApplication()).getToken();
        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        prikaziRezervacije();
        return root;
    }


    public void prikaziRezervacije() {

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //ArrayList<String> data = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String category_ime = object.getString("category_name");
                        String subcategories = object.getString("subcategories");
                        String date = object.getString("date");
                        String termin = object.getString("termin");
                        String status = object.getString("status");
                        String created_at = object.getString("created_at");


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
