package com.example.bruker.edubackpromo.views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.anna.eduback2.R;

public class SplashScreenActivity extends AppCompatActivity {

    protected boolean _active = true;
    protected int _splashTime = 4000; // time to display the splash screen in ms

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {

                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            };
        };
        splashTread.start();
    }
}

