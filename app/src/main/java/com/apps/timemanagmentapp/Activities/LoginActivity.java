package com.apps.timemanagmentapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.apps.timemanagmentapp.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}