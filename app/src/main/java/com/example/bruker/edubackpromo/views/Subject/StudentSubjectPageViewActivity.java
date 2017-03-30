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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StudentSubjectPageViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayList<Fag> StudentSubjectList = new ArrayList<>();
    private ArrayList<Fag> PossibleSubjects = new ArrayList<>();
    private ArrayAdapter<Fag> adapter;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag");
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Subjects");

    String userID;

    ListView StudentfagListView;
    Button Button_AddSubject;
    EditText EditText_SubjectCode;


    private CustomAdapter.OnDeleteClickedListener mListener =  new CustomAdapter.OnDeleteClickedListener() {
        @Override
        public void onDeleteReady(String code) {
            delete(code);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsubjectview);
        StudentfagListView = (ListView) findViewById(R.id.FagListStudent);
        Button_AddSubject = (Button) findViewById(R.id.AddStudentFagButton);
        EditText_SubjectCode = (EditText) findViewById(R.id.InsertStudentFag);
        adapter = new CustomAdapter(this, R.layout.itemlistrow, StudentSubjectList, mListener);
        StudentfagListView.setAdapter(adapter);
        StudentfagListView.setOnItemClickListener(this);
        Button_AddSubject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alreadyinlist(EditText_SubjectCode.getText().toString().toUpperCase());


            }
        });

        // Adder fag som allerede er i databasen

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String code = dataSnapshot.getKey().toString();

                ArrayList<String> info = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    info.add(child.getValue().toString());
                }
                    String exam = info.get(1);
                    String name = info.get(2);
                    String teacher = info.get(3);
                    Fag nyttFag = new Fag(code, name, exam, teacher);

                      StudentSubjectList.add(nyttFag);
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String code = dataSnapshot.getKey().toString();
                for (int i = 0; i< StudentSubjectList.size(); i++){
                    if (StudentSubjectList.get(i).getCode().equals(code)){
                        StudentSubjectList.remove(i);
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

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String code = dataSnapshot.getKey().toString();
                ArrayList<String> info = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    info.add(child.getValue().toString());
                }
                String exam = info.get(2);
                String name = info.get(3);
                String teacher = info.get(4);
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

     boolean add = false;
     boolean exist = false;

    public void alreadyinlist(String code) {
        for (int i = 0; i < StudentSubjectList.size(); i++) {
            if (StudentSubjectList.get(i).getCode().equals(code)) {
                Toast.makeText(this, "Subject already in list", Toast.LENGTH_SHORT).show();
                EditText_SubjectCode.setHint("");
                add = false;
                exist = true;
            }
        }

        for(int i = 0; i<PossibleSubjects.size(); i++){
            if(PossibleSubjects.get(i).getCode().equals(code)){
                add = true;
                exist = true;

            }
        }

        if(!(exist)){
            Toast.makeText(this, "Unvalid code", Toast.LENGTH_SHORT).show();
            EditText_SubjectCode.setHint("");
        }

        if(add){
            for(int i = 0; i<PossibleSubjects.size(); i++){
                if(PossibleSubjects.get(i).getCode().equals(code)){
                    mRef.child(code).setValue(PossibleSubjects.get(i));
                }
            }


        }
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent moveToDetailIntent = new Intent(getBaseContext(), SubjectPageActivity.class);
        moveToDetailIntent.putExtra("Fag", StudentSubjectList.get(position).getName());
        moveToDetailIntent.putExtra("Eksamen", StudentSubjectList.get(position).getExamdate());
        moveToDetailIntent.putExtra("Lærer", StudentSubjectList.get(position).getTeachername());
        moveToDetailIntent.putExtra("Fagkode", StudentSubjectList.get(position).getCode()); //getCode
        startActivity(moveToDetailIntent);
    }


    public void delete (String code){
        mRef.child(code).removeValue();
    }




}


