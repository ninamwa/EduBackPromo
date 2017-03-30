package com.example.bruker.edubackpromo.views.Mains;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.data.models.User;
import com.example.anna.eduback2.views.Stats.LearningGoals;
import com.example.anna.eduback2.views.Stats.LearningGoalsStudentActivity2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WeekListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<String> weekList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    ListView weekListView;
    Button AddWeekButton;
    EditText InsertWeekEditText;
    String weekNumber;
    String subjectName;
    String subjectCode;
    User user;

    DatabaseReference mDatabase;
    DatabaseReference mRef;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_list);

        Intent moveToDetailIntent = this.getIntent();
        subjectName = moveToDetailIntent.getExtras().getString("Fag");
        subjectCode = moveToDetailIntent.getExtras().getString("Fagkode");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(subjectCode).child("Week");
        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("User info");

        weekListView = (ListView) findViewById(R.id.WeekListView);
        AddWeekButton = (Button) findViewById(R.id.AddWeekButton);
        InsertWeekEditText = (EditText) findViewById(R.id.InsertWeek);

        String userID = firebaseUser.getUid();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user.isStudent){
                    View a = weekListView;
                    a.setMinimumHeight(80);
                    View b = AddWeekButton;
                    b.setVisibility(View.GONE);
                    View c = InsertWeekEditText;
                    c.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, weekList);
        weekListView.setAdapter(adapter);
        weekListView.setOnItemClickListener(this);
        AddWeekButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                weekNumber= InsertWeekEditText.getText().toString();
                mDatabase.child(weekNumber).child("id").setValue(weekNumber);
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String week = dataSnapshot.getKey().toString();
                    weekList.add("Week: " + week);
                    adapter.notifyDataSetChanged();
                }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            //Urelevante metoder for oss.
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Intent i;
        if(user.isStudent){
            i = new Intent(getBaseContext(), LearningGoalsStudentActivity2.class);
        } else {
            i = new Intent(getBaseContext(), LearningGoals.class);
        }
        i.putExtra("Fag", subjectName);
        i.putExtra("Fagkode", subjectCode);
        i.putExtra("Uke", weekList.get(position).toString().substring(6));
        startActivity(i);

        /*
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // User user = dataSnapshot.getValue(User.class);
                Intent i;
                if (user.isStudent ) {
                    i = new Intent(getBaseContext(), LearningGoalsStudentActivity2.class);
                } else {
                    i = new Intent(getBaseContext(), LearningGoals.class);
                }
                i.putExtra("Fag", subjectName);
                i.putExtra("Fagkode", subjectCode);
                i.putExtra("Uke", weekList.get(position).toString().substring(6));
                startActivity(i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

    }
}