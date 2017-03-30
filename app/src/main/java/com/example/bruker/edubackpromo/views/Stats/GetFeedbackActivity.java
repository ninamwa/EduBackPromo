package com.example.bruker.edubackpromo.views.Stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anna.eduback2.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GetFeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    String subjectCode, weekNumber;
    ArrayList<String> text = new ArrayList<>();
    Button bfinish;
    LinearLayout ll;
    final int N = 100;
    final TextView[] feedbacks = new TextView[N]; //save all feedbacks to later use

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_feedback);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .5));

        ll = (LinearLayout) findViewById(R.id.linearlayout);

        Intent intent = getIntent();
        if (intent.hasExtra("subjectCode")) {
            subjectCode = intent.getStringExtra("subjectCode");
            weekNumber = intent.getStringExtra("week");
        }

        final TextView rowTextViewNew = new TextView(this);
        rowTextViewNew.setText("Finnes dessverre ingen tilbakemeldinger enda");
        ll.addView(rowTextViewNew);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(subjectCode).child("Week").child(weekNumber);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.equals("FeedBacks")){
                    ll.removeAllViews();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                            text.add(child.getValue(String.class));
                    }
                    addToLayout();
                }
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

            bfinish = (Button) findViewById(R.id.bfinish);
            bfinish.setOnClickListener(this);
        }

        @Override
        public void onClick (View v){
            finish();
        }


    public void addToLayout() {
        for (int i = 0; i < text.size(); i++) {
            // create a new textview
            final TextView rowTextViewNew = new TextView(this);
            // set some properties of rowTextView or something
            rowTextViewNew.setText("Feedback: " + text.get(i).toString());
            // add the textview to the linearlayout
            ll.addView(rowTextViewNew);
            // save a reference to the textview for later
            feedbacks[i] = rowTextViewNew;
        }
    }
}
