package com.example.bruker.edubackpromo.views.Stats;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.anna.eduback2.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LearningGoals extends AppCompatActivity implements View.OnClickListener{

    private ExpandableListView listView;
    private com.example.anna.eduback2.data.adapters.ExpandableListAdapter listAdapter;
    private ArrayList<String> listDataHeader = new ArrayList<>();
    private ArrayList<String> feedBack = new ArrayList<>();
    private HashMap<String, List<String>> feddBackData = new HashMap<>();
    private HashMap<String, List<String>> listHash = new HashMap<>();
    Button addLG;
    TextView titleTextView, percentageTV;
    private String subjectName,subjectcode, newLG, week;

    private DatabaseReference mDatabase;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_goals);

        Button bAttendance = (Button) findViewById(R.id.bAttendance);
        Button bFeedback = (Button) findViewById(R.id.bFeedback);

        listView = (ExpandableListView) findViewById(R.id.expListViewProfessor);
        listAdapter = new com.example.anna.eduback2.data.adapters.ExpandableListAdapter(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);
        titleTextView = (TextView) findViewById(R.id.titleTextViewLG);

        addLG = (Button) findViewById(R.id.addButton);
        addLG.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent.hasExtra("Uke")){
            week = intent.getStringExtra("Uke");
            listAdapter.setWeek(week);
        }
        if (intent.hasExtra("Fag")) {
            subjectName = intent.getStringExtra("Fag");
            listAdapter.setFagName(titleTextView, subjectName);
        }
        if(intent.hasExtra("Fagkode")){
            subjectcode = intent.getExtras().getString("Fagkode").trim();
            listAdapter.setFagkode(subjectcode);
        }

        bAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToAttendanceIntent = new Intent(getBaseContext(), GetAttendanceActivity.class);
                moveToAttendanceIntent.putExtra("subjectCode", subjectcode);
                moveToAttendanceIntent.putExtra("week", week);
                startActivity(moveToAttendanceIntent);
            }
        });

        bFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToFeedbackIntent = new Intent(getBaseContext(), GetFeedbackActivity.class);
                moveToFeedbackIntent.putExtra("subjectCode", subjectcode);
                moveToFeedbackIntent.putExtra("week", week);
                startActivity(moveToFeedbackIntent);
            }
        });


        mref = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(subjectcode).child("Week").child(week).child("Learning Goals");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
                printExpLView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listDataHeader.clear();
                fetchData(dataSnapshot);
                printExpLView();
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
                printExpLView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
// Henter ut statistikk for hvert læringsmål i en hashmap
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Studentfag").child(subjectcode).child("Week").child(week).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String leg = dataSnapshot.getKey().toString();
                if (leg.equals("LGStatistics")) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        List<String> fb = new ArrayList<String>();
                        String learning = child.getKey();
                        for (DataSnapshot d : child.getChildren()) {
                            String key = d.getKey();
                            String value = d.getValue().toString();
                            fb.add(value);
                        } feddBackData.put(learning, fb);
                    }listAdapter.setLGInfo(feddBackData);
                }
            }


 /*           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String leg = dataSnapshot.getKey().toString();
                if (leg.equals("LGStatistics")) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        List<String> fb = new ArrayList<String>();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                           // for (DataSnapshot d : dataSnapshot.getChildren()){
                                String item = d.getValue().toString();
                                fb.add(item);
                            } feddBackData.put(leg, fb);listAdapter.setLGInfo(feddBackData);
                       // }
                    }
                }
            }  */
                //feedBack.clear();
           /*     List<String> fb = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String item = ds.getValue().toString();
                    fb.add(item);
                } feddBackData.put(leg, fb); listAdapter.setLGInfo(feddBackData); */


            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });


        if (intent.hasExtra("NewLG")) {
            newLG = intent.getStringExtra("NewLG");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Studentfag").child(subjectcode).child("Week").child(week).child("Learning Goals").child(newLG).setValue(newLG);
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addButton){
            Intent addLearningGoal = new Intent(getApplicationContext(), AddLearningGoalActivity.class);
            addLearningGoal.putExtra("Fagkode", subjectcode);
            addLearningGoal.putExtra("Uke", week);
            startActivity(addLearningGoal);
        }
    }

    private void printExpLView(){
        for (String item : listDataHeader){
            List<String> nyttlaringsmal = new ArrayList<>();
            nyttlaringsmal.add("Ingen forståelse");
            nyttlaringsmal.add("Delvis forståelse");
            nyttlaringsmal.add("Full forståelse");
            listHash.put(item, nyttlaringsmal);
        }
    }

    private void fetchData(DataSnapshot dataSnapshot){
        for (DataSnapshot lg : dataSnapshot.getChildren()) {
            String lgItem = lg.getValue().toString();
            listDataHeader.add(lgItem);
        } listAdapter.notifyDataSetChanged();
    }
}