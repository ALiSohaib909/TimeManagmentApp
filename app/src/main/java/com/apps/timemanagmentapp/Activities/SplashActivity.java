package com.apps.timemanagmentapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.apps.timemanagmentapp.R;
import com.apps.timemanagmentapp.databinding.ActivitySplashBinding;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new Handler().postDelayed(() -> {
            Intent i = new Intent(
                    SplashActivity.this, RegisterActivity.class);
            startActivity(i);
        }, 3000);


    }
}