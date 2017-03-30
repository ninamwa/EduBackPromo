package com.example.bruker.edubackpromo.views.Stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.anna.eduback2.R;

import java.util.ArrayList;


public class AddLearningGoalActivity extends AppCompatActivity implements View.OnClickListener{

    EditText addLearningGoal, addWeekNumber;
    Button submitButton;
    String newLG, fagkode, week;
    ArrayList<String> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_learning_goal);

        submitButton = (Button) findViewById(R.id.bsubmit);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.5));
        addLearningGoal = (EditText) findViewById(R.id.addLGEditText);

        Intent intent = getIntent();
        if(intent.hasExtra("Fagkode")){
            fagkode = intent.getStringExtra("Fagkode");
        }
        if(intent.hasExtra("Uke")){
            week = intent.getStringExtra("Uke");
        }

        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bsubmit:
                newLG = addLearningGoal.getText().toString();
                Intent tilbake = new Intent(getApplicationContext(), LearningGoals.class);
                tilbake.putExtra("NewLG", newLG);
                tilbake.putExtra("Fagkode", fagkode);
                tilbake.putExtra("Uke", week);
                startActivity(tilbake);
        }
    }
}