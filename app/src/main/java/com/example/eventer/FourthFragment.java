package com.example.eventer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eventer.databinding.FragmentFourthBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private void ShowDetail() {
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.categories_layout);
        System.out.println(ll.getChildCount());
        String tmp = "https://tilenkelc.eu/Eventer/public/category_images/1_Restavracija_Halo.jpg";

        for(int i = 10; i > 0; i--){
            ImageView imageView = new ImageView(getActivity());
            //imageView.setImageResource(R.drawable.rest);

            //Glide.with(getActivity()).load(new File("").diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            Glide.with(getActivity())
                    .load(tmp)
                    .into(imageView);

            //imageView.setImageBitmap(getBitmapFromURL("https://tilenkelc.eu/Eventer/public/category_images/1_Restavracija_Halo.jpg"));
            ll.addView(imageView);
        }

        System.out.println("asdasd");

        /*for (int id : idList) {
            // Create LinearLayout
            LinearLayout ll = new LinearLayout(getActivity());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            // Create TextView for the item label
            TextView txtLabel = new TextView(getActivity());
            txtLabel.setText(getString(id) + ": ");
            //		txtLabel.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
            ll.addView(txtLabel);

            // Create TextView for the item content
            TextView txtContent = new TextView(getActivity());
            txtContent.setText(getString(id));
            txtContent.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
            //		txtLabel.setTextAppearance(getApplicationContext(), android.R.attr.textAppearanceMedium);
            ll.addView(txtContent);
            //Add button to LinearLayout defined in XML
            llv.addView(ll);
        }*/
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

        ShowDetail();

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

