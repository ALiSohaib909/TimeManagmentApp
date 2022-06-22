package com.apps.timemanagmentapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.apps.timemanagmentapp.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    StringBuilder builder = new StringBuilder();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
    }
}