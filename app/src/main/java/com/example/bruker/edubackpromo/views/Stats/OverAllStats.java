package com.example.bruker.edubackpromo.views.Stats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.anna.eduback2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverAllStats extends AppCompatActivity {

    private HashMap<String, List<String>> qs;
    private ArrayList<String> lgoppnåelse;
    TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_all_stats);

        // TRENGER:
        // UKENUMMER, FAGKODE,

        tv1 = (TextView) findViewById(R.id.tvAlloverstats1);
        tv2 = (TextView) findViewById(R.id.tvAlloverstats2);

//BRUKERINFO
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        final String userID = firebaseUser.getUid();


// Hente ut svar på Qs i hashmap:
        final String subjectcode = "TMA4100", week = "1";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(subjectcode).child("Week").child(week).child("Questions");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String qnumber = dataSnapshot.getKey().toString();
                List<String> subs = new ArrayList<String>();
                for (DataSnapshot answer : dataSnapshot.getChildren()){
                    String yesorno = answer.getValue().toString();
                    subs.add(yesorno);
                } qs.put(qnumber, subs);
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
        // Finne svar på læringsmål
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(subjectcode).child("Week").child(week).child("LGStatistics");
/*        mDatabase2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String leg = dataSnapshot.getKey().toString();
                for (DataSnapshot ok : dataSnapshot.getChildren()){
                    if (ok.getKey().equals(firebaseUser.getUid())){
                        String addIt = leg + ": " + ok.getValue().toString();
                        lgoppnåelse.add(addIt);
                    }
                }
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}

        });     */

        tv1.setText("HEllo");
        tv2.setText("Everybody");



    }
}
