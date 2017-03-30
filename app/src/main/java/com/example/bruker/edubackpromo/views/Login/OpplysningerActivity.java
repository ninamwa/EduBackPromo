package com.example.bruker.edubackpromo.views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.data.local.PreferenceHelper;
import com.example.anna.eduback2.data.models.User;
import com.example.anna.eduback2.views.Mains.MainActivityProfessor;
import com.example.anna.eduback2.views.Mains.MainActivityStudent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OpplysningerActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private EditText editTextFullName;
    private RadioButton radioButtonStudent, radioButtonProf;
    private Button buttonSaveInfo;
    private boolean student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opplysninger);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        firebaseUser = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        radioButtonStudent = (RadioButton) findViewById(R.id.checkBoxStudent);
        radioButtonProf = (RadioButton) findViewById(R.id.checkBoxProfessor);
        buttonSaveInfo = (Button) findViewById(R.id.buttonSaveInfo);

        buttonSaveInfo.setOnClickListener(this);
    }


    public void saveUserInformation() {
        String name = editTextFullName.getText().toString().trim();
           if (radioButtonProf.isChecked()) {
                student = false;
            } else {
                student = true;
            }
            User user = new User(student,name);
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            mDatabase.child("Users").child(firebaseUser.getUid()).child("User info").setValue(user);

            Toast.makeText(this, "Information Saved", Toast.LENGTH_LONG).show();
    }


    public void getSignedInUserProfile() {

        DatabaseReference reference = mDatabase;//.child("eduback-2feef");
        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) {
                    // Save if the user is student or prof in shared prefs.
                    PreferenceHelper helper = new PreferenceHelper(getBaseContext());
                    helper.setIsStudent(user.isStudent);
                    checkStudentOrProfessor(user);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Ups vis error
            }
        });



    }

    public void checkStudentOrProfessor(User user) {

        Intent i;
        if (user.isStudent) {
            i = new Intent(this, MainActivityStudent.class);
        } else {
            i = new Intent(this, MainActivityProfessor.class);
        }
        startActivity(i);


    }

    @Override
    public void onClick(View v) {
        saveUserInformation();
        if(radioButtonStudent.isChecked()){
                startActivity(new Intent(OpplysningerActivity.this, MainActivityStudent.class));
            }
            else {
                startActivity(new Intent(OpplysningerActivity.this, MainActivityProfessor.class));
        }

    }
}