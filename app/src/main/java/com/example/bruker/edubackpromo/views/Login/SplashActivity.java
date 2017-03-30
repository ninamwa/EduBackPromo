package com.example.bruker.edubackpromo.views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.anna.eduback2.data.local.PreferenceHelper;
import com.example.anna.eduback2.views.Mains.MainActivityProfessor;
import com.example.anna.eduback2.views.Mains.MainActivityStudent;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
        Intent i;
        if (fireBaseAuth.getCurrentUser() == null){
            i = new Intent(this, RegisterActivity.class);
        } else {
            // We have a logged in user
            PreferenceHelper helper = new PreferenceHelper(this);
            if(helper.getIsStudent()){
                i = new Intent(this, MainActivityStudent.class);
            } else {
                i = new Intent(this, MainActivityProfessor.class);
            }
        }
        startActivity(i);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        finish();
    }
}
