package com.example.eventer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.eventer.databinding.FragmentSecondBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    public EditText emailLI;
    public EditText passwordLI;
    private Button sendLI;
    private RequestQueue requestQueue;
    private String urlLI = "https://tilenkelc.eu/Eventer/api/login";
    private String token = "";
    private boolean invalidLogin = false;

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View root = inflater.inflate(R.layout.fragment_second, container, false);
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());

        emailLI = (EditText) root.findViewById(R.id.editTextTextEmailAddress3);
        passwordLI = (EditText) root.findViewById(R.id.editTextTextPassword2);
        sendLI = (Button) root.findViewById(R.id.button_second);

        return root;
    }

    public void login(){
        Context context = requireActivity().getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        if(TextUtils.isEmpty(emailLI.getText().toString())) {
            CharSequence text = "Email input cannot be empty!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else if(TextUtils.isEmpty(passwordLI.getText().toString())){
            CharSequence text = "Password input cannot be empty!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else{
            try{
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", emailLI.getText());
                jsonBody.put("password", passwordLI.getText());


                final String mRequestBody = jsonBody.toString();

                System.out.println(mRequestBody);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLI, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(invalidLogin){
                            Context context = requireActivity().getApplicationContext();
                            int duration = Toast.LENGTH_LONG;
                            CharSequence text = "Invalid login details!";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }else{
                            if(!token.equals("")) {
                                ((GlobalClass) requireActivity().getApplication()).setToken(token);

                                NavHostFragment.findNavController(SecondFragment.this)
                                        .navigate(R.id.action_SecondFragment_to_FourthFragment);
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

                            if(responseString.contains("Invalid login details")){
                                invalidLogin = true;
                            }else{
                                invalidLogin = false;
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

        sendLI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}