package com.apps.timemanagmentapp.Activities;

import static com.apps.timemanagmentapp.Activities.RegisterActivity.mainurl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apps.timemanagmentapp.R;
import com.apps.timemanagmentapp.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
binding.btnRegister.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        doLogin();
    }
});
binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
});
    }

    public void doLogin() {

        if (binding.edLoginEmail.getText() == null ||
                binding.edLoginPass.getText() == null) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
        } else {
            String url =mainurl+"App/Verify?email="+binding.edLoginEmail.getText()+"&pass="+binding.edLoginPass.getText();
            Log.d("urlCreate", url);

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    SharedPreferences saveinfo=getSharedPreferences("savelogin",MODE_PRIVATE);
                    SharedPreferences.Editor edit=saveinfo.edit();
                    try {
                        edit.putInt("id",response.getInt("id"));
                        edit.putString("name",response.getString("name"));
                        edit.apply();
                        Toast.makeText(getApplicationContext(), "good to go", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
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