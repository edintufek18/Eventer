package com.example.eventer;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventer.databinding.FragmentThirdBinding;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class ThirdFragment extends Fragment{
    private FragmentThirdBinding binding;

    public EditText ime;
    private EditText priimek;
    private EditText email;
    private EditText stevilka;
    private EditText geslo;
    private EditText gesloConfirm;
    // private EditText naslov;
    // private EditText kraj;
    // private  EditText posta;
    private Button send;

    private RequestQueue requestQueue;
    private String url = "https://tilenkelc.eu/Eventer/api/register";
    private String token = "";
    private boolean alreadyTaken = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View root = inflater.inflate(R.layout.fragment_third, container, false);
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        ime = (EditText) root.findViewById(R.id.Name);
        priimek = (EditText) root.findViewById(R.id.Surname);
        email = (EditText) root.findViewById(R.id.editTextTextEmailAddress3);
        stevilka = (EditText) root.findViewById(R.id.editTextPhone);
        geslo = (EditText) root.findViewById(R.id.editTextTextPassword2);
        gesloConfirm = (EditText) root.findViewById(R.id.editTextTextPassword);
        send = (Button) root.findViewById(R.id.buttonThird);

        return root;
    }


    public void registracija(){

        Context context = requireActivity().getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        if(TextUtils.isEmpty(ime.getText().toString())) {
            CharSequence text = "Name input cannot be empty!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else if(TextUtils.isEmpty(priimek.getText().toString())){
            CharSequence text = "Surname input cannot be empty!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else if(TextUtils.isEmpty(geslo.getText().toString())){
            CharSequence text = "Password input cannot be empty!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else if(geslo.getText().toString().length() < 8){
            CharSequence text = "Password has to have at least 8 characters!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else if(TextUtils.isEmpty(gesloConfirm.getText().toString())){
            CharSequence text = "Confirm password input cannot be empty!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else if(!geslo.getText().toString().equals(gesloConfirm.getText().toString())){
            CharSequence text = "Passwords do not match!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else if(TextUtils.isEmpty(email.getText().toString())){
            CharSequence text = "Email input cannot be empty!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else if(TextUtils.isEmpty(stevilka.getText().toString())){
            CharSequence text = "Phone number input cannot be empty!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else{
            try{
                JSONObject jsonBody = new JSONObject();

                jsonBody.put("name", ime.getText().toString());
                jsonBody.put("surname", priimek.getText());
                jsonBody.put("email", email.getText());
                jsonBody.put("phone_number", stevilka.getText());
                jsonBody.put("password", geslo.getText());

                final String mRequestBody = jsonBody.toString();

                //System.out.println(mRequestBody);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(alreadyTaken){
                            Context context = requireActivity().getApplicationContext();
                            int duration = Toast.LENGTH_LONG;
                            CharSequence text = "Email already taken!";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }else{
                            if(!token.equals("")) {
                                ((GlobalClass) requireActivity().getApplication()).setToken(token);

                                NavHostFragment.findNavController(ThirdFragment.this)
                                        .navigate(R.id.action_ThirdFragment_to_FourthFragment);
                            }
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

                            if(responseString.contains("zaseden")){
                                alreadyTaken = true;
                            }else{
                                alreadyTaken = false;
                                token = responseString;
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




    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registracija();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

