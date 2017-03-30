package com.example.bruker.edubackpromo.views.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.data.local.PreferenceHelper;
import com.example.anna.eduback2.data.models.User;
import com.example.anna.eduback2.views.Mains.MainActivityProfessor;
import com.example.anna.eduback2.views.Mains.MainActivityStudent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import butterknife.BindView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mfireBaseAuth;
    private DatabaseReference mDatabase;
    private String userID;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mfireBaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignIn){
            usersignin();

        }
        if(v==textViewSignUp){
            startActivity(new Intent(this, RegisterActivity.class));
        }


    }

    private void usersignin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage("Logging in. Please wait...");
        mProgressDialog.show();
        mfireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if(task.isSuccessful()){
                    getSignedInUserProfile();
                }
            }
        });
    }

    private void getSignedInUserProfile() {

        DatabaseReference reference = mDatabase;//.child("eduback-2feef");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        reference.child("Users").child(userID).child("User info").addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void checkStudentOrProfessor(User user) {

        Intent i;
        if (user.isStudent ) {
            i = new Intent(this, MainActivityStudent.class);
        } else {
            i = new Intent(this, MainActivityProfessor.class);
        }
        startActivity(i);


    }



}
