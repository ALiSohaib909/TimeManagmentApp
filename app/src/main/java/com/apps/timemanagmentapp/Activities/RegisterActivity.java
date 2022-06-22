package com.apps.timemanagmentapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apps.timemanagmentapp.R;
import com.apps.timemanagmentapp.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    StringBuilder builder = new StringBuilder();
    TextView textView;
    ActivityRegisterBinding binding;
     static String mainurl = "http://192.168.0.102/workScheduleApp/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        textView = findViewById(R.id.tv_login);

        String white = "Already have an account?";
        SpannableString whiteSpannable = new SpannableString(white);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, white.length(), 0);
        builder.append(whiteSpannable);

        String blue = "Login";
        SpannableString redSpannable = new SpannableString(blue);

        redSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, blue.length(), 0);
        builder.append(redSpannable);
        textView.setText(builder, TextView.BufferType.SPANNABLE);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addmeeting();
            }
        });
    }

    public void addmeeting() {

        if (binding.edName.getText() == null || binding.edRegisterEmail.getText() == null ||
                binding.edRegisterPass.getText() == null) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
        } else {
            String url = mainurl + "App/Save";
            Log.d("urlCreate", url);
            JSONObject Object = new JSONObject();
            try {
                Object.put("name", binding.edName.getText().toString());
                Object.put("email", binding.edRegisterEmail.getText().toString());
                Object.put("password", binding.edRegisterPass.getText().toString());

            } catch (Exception ex) {
                Log.d("error", ex.getMessage().toString());
            }
            Log.d("meetingObj", Object.toString());
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, url, Object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            );
            requestQueue.add(request);
        }
    }
}