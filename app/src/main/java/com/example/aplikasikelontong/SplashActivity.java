package com.example.aplikasikelontong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //make full screen
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);
            //getSupportActionBar().hide();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent iLogin = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(iLogin);
                    finish();
                }
            }, 3000);
        }
}
