package com.example.bruker.edubackpromo.views.Subject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.views.Mains.WeekListActivity;
import com.example.anna.eduback2.views.Stats.StudentGoalsActivity;

public class SubjectPageActivity extends AppCompatActivity {

    TextView fagNavnTextView;
    TextView lærerTextView;
    TextView eksamensdatoTextView;

    Button ButtonGoToLearningGoal;
    Button ButtonGoTOMySubjectGoals;
    String fagName;
    String lærernavn;
    String eksamensdato;
    String fagkode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjectpage);

        fagNavnTextView = (TextView) findViewById(R.id.fagNavnTextView);
        lærerTextView = (TextView) findViewById(R.id.LærerTextView);
        eksamensdatoTextView = (TextView) findViewById(R.id.eksamensdatoTextView);
        ButtonGoToLearningGoal = (Button) findViewById(R.id.ButtonGoToWeekList);
        ButtonGoTOMySubjectGoals = (Button) findViewById(R.id.ButtonDIV);

        ButtonGoToLearningGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToWeekList = new Intent(getBaseContext(), WeekListActivity.class);
                moveToWeekList.putExtra("Fag", fagName);
                moveToWeekList.putExtra("Fagkode", fagkode);
                startActivity(moveToWeekList);
            }
        });
        ButtonGoTOMySubjectGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToSubjectGoals = new Intent(getBaseContext(), StudentGoalsActivity.class);
                moveToSubjectGoals.putExtra("Fag",fagName);
                startActivity(moveToSubjectGoals);
            }
        });


        Intent moveToDetailIntent = this.getIntent();
        fagName = moveToDetailIntent.getExtras().getString("Fag");
        lærernavn = moveToDetailIntent.getExtras().getString("Lærer");
        eksamensdato = moveToDetailIntent.getExtras().getString("Eksamen");
        fagkode = moveToDetailIntent.getExtras().getString("Fagkode");

        fagNavnTextView.setText(fagName);
        lærerTextView.setText("Foreleser(e) i faget: " + lærernavn);
        eksamensdatoTextView.setText("Eksamensdato: " + eksamensdato);
    }

    }


