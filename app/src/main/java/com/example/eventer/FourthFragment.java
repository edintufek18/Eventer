package com.example.eventer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventer.databinding.FragmentFourthBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class FourthFragment extends Fragment{
    private FragmentFourthBinding binding;
    private RequestQueue requestQueue;
    private String url = "https://tilenkelc.eu/Eventer/api/categories";
    private String categID;

    private String token = "";


    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFourthBinding.inflate(inflater, container, false);

        token = ((GlobalClass) getActivity().getApplication()).getToken();
        System.out.println("new" + token);
        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        categID = ((GlobalClass) getActivity().getApplication()).getCategory();
        prikaziKategorije();



        return binding.getRoot();


    }

    // GET REQUEST KATEGOTIJE

    public void prikaziKategorije() {

        JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
        requestQueue.add(request);

    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<String> data = new ArrayList<>();


            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String category_image = object.getString("category_image");
                    String description = object.getString("description");


                    categID = id;
                    //System.out.println(categID);
                    data.add(id + " " + name + " " + "https://tilenkelc.eu/Eventer/public"  + category_image + " " + description);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            System.out.println(Arrays.toString(data.toArray()));
        }

    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };










    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("restoran.json"); // your file name
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray array = obj.getJSONArray("restaurants");
            HashMap<String, String> list;
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                String name = o.getString("name");
                String seats = o.getString("seats");
                list = new HashMap<>();
                list.put("name",name);
                list.put("seats",seats);
                arrayList.add(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FourthFragment.this)
                        .navigate(R.id.action_FourthFragment_to_MasiRezervacija);
            }
        });

        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FourthFragment.this)
                        .navigate(R.id.action_FourthFragment_to_MasiRezervacija);
            }
        });

        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FourthFragment.this)
                        .navigate(R.id.action_FourthFragment_to_MasiRezervacija);
            }
        });

        /*binding.buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FourthFragment.this)
                        .navigate(R.id.action_FourthFragment_to_InfoRestaurant);
            }
        });*/
    }

}

