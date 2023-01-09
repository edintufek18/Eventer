package com.example.eventer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.example.eventer.databinding.FragmentFourthBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class FourthFragment extends Fragment{
    private FragmentFourthBinding binding;
    private RequestQueue requestQueue;
    private String url = "https://tilenkelc.eu/Eventer/api/categories";

    private String token = "";

    private Button showReservations;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFourthBinding.inflate(inflater, container, false);

        token = ((GlobalClass) getActivity().getApplication()).getToken();
        showReservations = binding.getRoot().findViewById(R.id.showReservationsBtn);

        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        prikaziKategorije();

        showReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FourthFragment.this)
                        .navigate(R.id.action_FourthFragment_to_InfoRestaurant);
            }
        });

        return binding.getRoot();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    private void addItem(String id, String opens_at, String closes_at, String amount, String name, String image) {
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.categories_layout);
        System.out.println(ll.getChildCount());

        ImageView imageView = new ImageView(getActivity());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GlobalClass) requireActivity().getApplication()).setCategory(id);
                ((GlobalClass) requireActivity().getApplication()).setOpens_at(opens_at);
                ((GlobalClass) requireActivity().getApplication()).setCloses_at(closes_at);
                ((GlobalClass) requireActivity().getApplication()).setAmount(amount);

                NavHostFragment.findNavController(FourthFragment.this)
                        .navigate(R.id.action_FourthFragment_to_MasiRezervacija);
            }
        });

        Glide.with(getActivity())
                .load(image)
                .into(imageView);
        ll.addView(imageView);

        TextView textView = new TextView(getActivity());
        textView.setText(name + "\n\n");
        textView.setTextSize(30);
        textView.setGravity(1);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GlobalClass) requireActivity().getApplication()).setCategory(id);
                ((GlobalClass) requireActivity().getApplication()).setOpens_at(opens_at);
                ((GlobalClass) requireActivity().getApplication()).setCloses_at(closes_at);
                ((GlobalClass) requireActivity().getApplication()).setAmount(amount);

                NavHostFragment.findNavController(FourthFragment.this)
                        .navigate(R.id.action_FourthFragment_to_MasiRezervacija);
            }
        });

        ll.addView(textView);
    }

    // GET REQUEST KATEGOTIJE


    public void prikaziKategorije() {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //ArrayList<String> data = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        System.out.println(response.toString());
                        JSONObject object = response.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String opens_at = object.getString("opens_at");
                        String closes_at = object.getString("closes_at");
                        String amount = object.getString("amount");
                        String category_image = object.getString("category_image");
                        //String description = object.getString("description");

                        addItem(id, opens_at, closes_at, amount, name, "https://tilenkelc.eu/Eventer/public" + category_image);

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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}

