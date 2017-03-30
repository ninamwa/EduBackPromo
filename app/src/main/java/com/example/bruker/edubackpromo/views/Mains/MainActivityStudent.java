package com.example.bruker.edubackpromo.views.Mains;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.views.Login.LoginActivity;
import com.example.anna.eduback2.views.Stats.OverAllStats;
import com.example.anna.eduback2.views.Stats.StudentGoalsActivity;
import com.example.anna.eduback2.views.Subject.StudentSubjectPageViewActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityStudent extends AppCompatActivity {
    Button Student_goals;
    Button Student_addsubject;
    Button Student_schedule;
    Button Student_overallstats;
    Button Loggoff;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        auth = FirebaseAuth.getInstance();


        Student_goals = (Button)findViewById(R.id.Student_goals);
        Student_addsubject =(Button)findViewById(R.id.Student_addsubject);
        Student_schedule = (Button)findViewById(R.id.Student_schedule);
        Student_overallstats = (Button) findViewById(R.id.buttonOverAllStats);
        Loggoff = (Button) findViewById(R.id.buttonLogoff);

        Student_goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToStudentGoals = new Intent(getApplicationContext(), StudentGoalsActivity.class);
                startActivity(goToStudentGoals);
            }
        });
        Student_addsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSubject = new Intent(getApplicationContext(), StudentSubjectPageViewActivity.class);
                startActivity(goToSubject);
            }
        });

        Student_overallstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToStats = new Intent(getApplicationContext(), OverAllStats.class);
                startActivity(goToStats);
            }
        });

        Loggoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent logoff = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoff);

            }
        });


    }
    public void scheduleS(View view){
        Intent scheduleIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ntnu.1024.no"));
        startActivity(scheduleIntent);
    }
}
