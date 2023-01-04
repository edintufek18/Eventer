package com.example.eventer;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventer.databinding.FragmentFirstBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Bank extends Fragment {
    private Button submitPayment;
    private TextView creditCardInput;
    private TextView ccvInput;
    private TextView validDateInput;
    private TextView amountField;

    private String token;
    private String subcategory_id;
    private String date;
    private String time;
    private String amount;

    private String url = "https://tilenkelc.eu/Eventer/api/rent/pay";
    private RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bank,container,false);

        submitPayment = root.findViewById(R.id.submitPaymentBtn);
        creditCardInput = root.findViewById(R.id.creditCardInput);
        ccvInput = root.findViewById(R.id.ccvInput);
        validDateInput = root.findViewById(R.id.validDateInput);
        amountField = root.findViewById(R.id.setAmountTextField);

        token = ((GlobalClass) getActivity().getApplication()).getToken();
        subcategory_id = ((GlobalClass) getActivity().getApplication()).getSubcategory();
        date = ((GlobalClass) getActivity().getApplication()).getDate();
        time = ((GlobalClass) getActivity().getApplication()).getTime();
        amount = ((GlobalClass) getActivity().getApplication()).getAmount();

        amountField.setText(amount + "€");

        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());

        submitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = requireActivity().getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                if(TextUtils.isEmpty(creditCardInput.getText().toString())) {
                    CharSequence text = "Credit card input cannot be empty!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else if(!creditCardInput.getText().toString().matches("([0-9]{4})-([0-9]{4})-([0-9]{4})-([0-9]{4})")){
                    CharSequence text = "Credit card input is not in a correct format (xxxx-xxxx-xxxx-xxxx)!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else if(TextUtils.isEmpty(ccvInput.getText().toString())){
                    CharSequence text = "CCV input cannot be empty!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else if(!ccvInput.getText().toString().matches("([0-9]{3})")){
                    CharSequence text = "CCV input is not in a correct format (xxx)!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else if(TextUtils.isEmpty(validDateInput.getText().toString())){
                    CharSequence text = "Expire date input cannot be empty!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else if(!validDateInput.getText().toString().matches("(0?[1-9]|1[012])[\\/]([0-9]{2})")){
                    CharSequence text = "Expire date input is not in a correct format (MM/YY)!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else{
                    pay();
                }
            }
        });

        return root;
    }

    public void pay(){
        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", subcategory_id);
            jsonBody.put("date", date);
            jsonBody.put("time_from", time.split(" - ")[0]);
            jsonBody.put("time_to", time.split(" - ")[1]);
            jsonBody.put("card_num", creditCardInput.getText());
            jsonBody.put("valid", validDateInput.getText());
            jsonBody.put("ccv", ccvInput.getText());

            final String mRequestBody = jsonBody.toString();

            System.out.println(mRequestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    /*if(invalidLogin){
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
                    }*/
                    System.out.println(response);
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
                        //headers.put("Authorization", token);
                        headers.put("Authorization", "Bearer" + " " + token);
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

                            /*if(responseString.contains("Invalid login details")){
                                invalidLogin = true;
                            }else{
                                invalidLogin = false;
                                token = responseString;
                            }*/
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
