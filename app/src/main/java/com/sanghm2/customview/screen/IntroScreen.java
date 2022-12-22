package com.sanghm2.customview.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sanghm2.customview.R;

import java.util.Objects;

public class IntroScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        final Handler handler = new Handler()  ;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(IntroScreen.this , LoginActivity.class));
            }
        },3000);
    }
}