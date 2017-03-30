package com.example.bruker.edubackpromo.views.Subject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.data.adapters.CustomAdapter;
import com.example.anna.eduback2.data.models.Fag;
import com.example.anna.eduback2.data.tasks.JSONTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SubjectViewActivity extends AppCompatActivity implements JSONTask.OnJSONReadyListener , AdapterView.OnItemClickListener {

    private ArrayList<Fag> SubjectList = new ArrayList<>();
    private ArrayAdapter<Fag> adapter;
    private ArrayList<Fag> PossibleSubjects = new ArrayList<>();

    ListView FagListView;
    Button AddFagButton;
    EditText InsertFagEditText;
    String subjectCode;


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mDatasubject = FirebaseDatabase.getInstance().getReference().child("Studentfag");
    private DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Subjects");


    private CustomAdapter.OnDeleteClickedListener mListener =  new CustomAdapter.OnDeleteClickedListener() {
        @Override
        public void onDeleteReady(String code) {
            delete(code);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);

        FagListView = (ListView) findViewById(R.id.FagListView);
        AddFagButton = (Button) findViewById(R.id.AddFagButton);
        InsertFagEditText = (EditText) findViewById(R.id.InsertFag);
        InsertFagEditText.setHint("Enter subject you want to add");

        adapter = new CustomAdapter(this, R.layout.itemlistrow, SubjectList, mListener);
        FagListView.setAdapter(adapter);
        FagListView.setOnItemClickListener(this);
        AddFagButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                subjectCode = InsertFagEditText.getText().toString();
                new JSONTask(SubjectViewActivity.this).execute("http://www.ime.ntnu.no/api/course/" + subjectCode);
            }
        });


        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String code = dataSnapshot.getKey().toString();

                ArrayList<String> info = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    info.add(child.getValue().toString());
                }
                if (!code.equals("User info")) {
                    String exam = info.get(1);
                    String name = info.get(2);
                    String teacher = info.get(3);
                    Fag nyttFag = new Fag(code, name, exam, teacher);
                    SubjectList.add(nyttFag);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String code = dataSnapshot.getKey().toString();
                for (int i=0; i<SubjectList.size();i++){
                    if (SubjectList.get(i).getCode().equals(code)){
                        SubjectList.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            //Urelevante metoder for oss.
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mDatasubject.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String code = dataSnapshot.getKey().toString();
                ArrayList<String> info = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    info.add(child.getValue().toString());
                }
                String exam = info.get(1);
                String name = info.get(2);
                String teacher = info.get(3);
                Fag possiblesubject = new Fag(code, name, exam, teacher);
                PossibleSubjects.add(possiblesubject);
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent moveToDetailIntent = new Intent(getBaseContext(), SubjectPageActivity.class);
        moveToDetailIntent.putExtra("Fag", SubjectList.get(position).getName());
        moveToDetailIntent.putExtra("Eksamen", SubjectList.get(position).getExamdate());
        moveToDetailIntent.putExtra("Lærer", SubjectList.get(position).getTeachername());
        moveToDetailIntent.putExtra("Fagkode", SubjectList.get(position).getCode()); //getCode
        startActivity(moveToDetailIntent);
    }

    @Override
    public void onJsonReady(final Fag nyttFag) {
        boolean add = true;
        if (nyttFag == null) {
            Toast.makeText(this, "Subjectcode is not valid", Toast.LENGTH_LONG).show();
            add = false;
        }

        if(add){
        for(int i =0; i<SubjectList.size(); i++){
            if(SubjectList.get(i).getCode().equals(nyttFag.getCode())){
                Toast.makeText(this, "Subject is already in list", Toast.LENGTH_LONG).show();
                add = false;
            }}
        }

        if (add) {
            Add(nyttFag);
            Toast.makeText(this, "Subject added to list", Toast.LENGTH_LONG).show();
        }
    }


    boolean exist = false;
    private void Add(Fag nyttFag) {
        String code = nyttFag.getCode();
        mDatabase.child("Users").child(firebaseUser.getUid()).child("Subjects").child(code).setValue(nyttFag);
        for(int i=0; i<PossibleSubjects.size(); i++){
            if(PossibleSubjects.get(i).getCode().equals(code)){
                exist = true;
            }
        }
        if(!(exist)){
            mDatabase.child("Studentfag").child(code).setValue(nyttFag);
        }
    }

    public void delete(String code){
        mDatabase.child("Users").child(firebaseUser.getUid()).child("Subjects").child(code).removeValue();
        //Slette herfra også?
        //mDatabase.child("Studentfag").child(code).removeValue();
    }
}
