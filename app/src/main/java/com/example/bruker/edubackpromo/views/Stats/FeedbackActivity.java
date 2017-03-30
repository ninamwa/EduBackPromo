package com.example.bruker.edubackpromo.views.Stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anna.eduback2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {


    EditText addFeedback;
    Button submitButton;
    String feedback, subjectCode;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference mDatabase;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.5));

        submitButton = (Button) findViewById(R.id.bsubmit);
        addFeedback = (EditText) findViewById(R.id.addFeedbackEditText);

        Intent intent = getIntent();
        if(intent.hasExtra("subjectCode")){
            subjectCode = intent.getStringExtra("subjectCode");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(subjectCode).child("Feedbacks");
        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Subjects").child(subjectCode).child("Your Feedback");
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        feedback = addFeedback.getText().toString();
        Toast.makeText(this, "Thanks for your feedback!", Toast.LENGTH_LONG).show();
        mDatabase.push().setValue(feedback);
        mRef.push().setValue(feedback);


        finish();
        }
    }
