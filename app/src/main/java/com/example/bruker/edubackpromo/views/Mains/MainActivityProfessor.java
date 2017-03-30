package com.example.bruker.edubackpromo.views.Mains;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.views.Login.LoginActivity;
import com.example.anna.eduback2.views.Stats.StudentGoalsActivity;
import com.example.anna.eduback2.views.Subject.SubjectViewActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityProfessor extends AppCompatActivity {

    Button Profesor_goals;
    Button Professor_addsubject;
    Button Professor_schedule;
    Button Professor_settings;
    Button LoggOffButton;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor2);
        auth = FirebaseAuth.getInstance();

        Profesor_goals = (Button)findViewById(R.id.Professor_goals);
        Professor_addsubject =(Button)findViewById(R.id.GoToSubjectViewButton);
        Professor_schedule = (Button)findViewById(R.id.Professor_schedule);
        Professor_settings = (Button) findViewById(R.id.Professor_settings);
        LoggOffButton = (Button) findViewById(R.id.LogoffbuttonProfessor);

        Profesor_goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToStudentGoals = new Intent(getApplicationContext(), StudentGoalsActivity.class);
                startActivity(goToStudentGoals);
            }
        });
        Professor_addsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSubjectView = new Intent(getApplicationContext(), SubjectViewActivity.class);
                startActivity(goToSubjectView);
            }
        });

        LoggOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                toast();
                Intent LogingPage = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(LogingPage);


            }
        });
    }
        public void schedule(View view){
            Intent scheduleIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ntnu.1024.no"));
            startActivity(scheduleIntent);
        }

    public void toast(){
        Toast.makeText(this, "Logged off", Toast.LENGTH_SHORT).show();
    }
}
