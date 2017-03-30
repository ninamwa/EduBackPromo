package com.example.bruker.edubackpromo.views.Stats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.views.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LearningGoalsStudentActivity2 extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private DatabaseReference mRefFeed;
    private DatabaseReference mRefStud;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ExpandableListView explistview;
    private com.example.anna.eduback2.data.adapters.ExpandableListAdapterStudent listAdapterStudent;
    private String valueone, valuetwo, valuethree, valuefour;

    private ArrayList<String> listDataHeaderStudent = new ArrayList<>();
    private HashMap<String, List<String>> listHashStudent = new HashMap<>();
    private String subjectcode, week;
    private int stat;
    private String feedback;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_goals_student);

        final Button bAttendance = (Button) findViewById(R.id.bAttendance);
        final Button bFeedback = (Button) findViewById(R.id.bFeedback);
        TextView titleTextView = (TextView) findViewById(R.id.textViewLearningGoal);

        // SET EXP.LISTVIEW ADAPTER
        explistview = (ExpandableListView) findViewById(R.id.expListViewStudent);
        listAdapterStudent = new com.example.anna.eduback2.data.adapters.ExpandableListAdapterStudent(this, listDataHeaderStudent, listHashStudent);
        explistview.setAdapter(listAdapterStudent);
        // GET INTENTS
        Intent intent = getIntent();
        if(intent.hasExtra("Fagkode")){
            subjectcode = intent.getExtras().getString("Fagkode").trim();
            listAdapterStudent.setFagkode(subjectcode);
        }
        if (intent.hasExtra("Uke")){
            week = intent.getStringExtra("Uke");
            listAdapterStudent.setWeek(week);
        }
        if (intent.hasExtra("Fag")) {
            String subjectName = intent.getStringExtra("Fag");
            listAdapterStudent.setFagName(titleTextView, subjectName);
        }

        bAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendencewindow();
                bAttendance.setEnabled(false);
            }
        });

        bFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackwindow();
                bFeedback.setEnabled(false);
            }
        });

        // KONTAKT MED DATABASEN, HENTE UT LÆRINGSMÅL
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(subjectcode).child("Week").child(week).child("Learning Goals");
/*        mDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
                listAdapterStudent.notifyDataSetChanged();
                printExpLView();
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });     */
 /*       mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
                listAdapterStudent.notifyDataSetChanged();
                printExpLView();
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });    */
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
                listAdapterStudent.notifyDataSetChanged();
                printExpLView();
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

    }
    // PRINTE UT ARRAY I EXPANDABLELISTVIEW
    private void printExpLView(){
        for (String item : listDataHeaderStudent) {
            List<String> nyttlaringsmal = new ArrayList<>();
            nyttlaringsmal.add("Ingen forståelse");
            nyttlaringsmal.add("Delvis forståelse");
            nyttlaringsmal.add("Full forståelse");
            listHashStudent.put(item, nyttlaringsmal);
        }
    }

    // BEHANDLE DATA SOM ER TATT UT AV DATABASE
    private void fetchData(DataSnapshot dataSnapshot){
        for (DataSnapshot lg : dataSnapshot.getChildren()) {
            String lgItem = lg.getValue(String.class);
            listDataHeaderStudent.add(lgItem);
            listAdapterStudent.notifyDataSetChanged();
        } listAdapterStudent.notifyDataSetChanged();
    }

    public void attendencewindow() {


        mRef = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        firebaseUser = firebaseAuth.getCurrentUser();
        mRefStud = FirebaseDatabase.getInstance().getReference();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupview = layoutInflater.inflate(R.layout.activity_checkpagelayout, null);
        popupview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        final PopupWindow popupWindow = new PopupWindow(popupview, popupview.getMeasuredWidth(), popupview.getMeasuredHeight(), true);
        popupWindow.showAtLocation(popupview, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();

        Button btnSendFeedbakc = (Button) popupview.findViewById(R.id.btnFeedBack);
        Button btnCanel = (Button) popupview.findViewById(R.id.btnCancel);


        final RadioGroup question1 = (RadioGroup) popupview.findViewById(R.id.Questionone);
        question1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (question1.getCheckedRadioButtonId()){
                    case R.id.checkBoxStudent1YES:
                        valueone = "1";
                        break;
                    case R.id.checkBoxStudent1NO:
                        valueone = "0";
                        break;
                }
            }
        });

        final RadioGroup question2 = (RadioGroup) popupview.findViewById(R.id.Questiontwo);
        question2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (question2.getCheckedRadioButtonId()){
                    case R.id.checkBoxStudent2YES:
                        valuetwo = "1";
                        break;
                    case R.id.checkBoxStudent2NO:
                        valuetwo = "0";
                        break;

                }
            }
        });

        final RadioGroup question3 = (RadioGroup) popupview.findViewById(R.id.Questionthree);
        question3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (question3.getCheckedRadioButtonId()){
                    case R.id.checkBoxStudent3YES:
                        valuethree = "1";
                        break;
                    case R.id.checkBoxStudent3NO:
                        valuethree = "0";
                        break;
                }
            }
        });

        final RadioGroup question4 = (RadioGroup) popupview.findViewById(R.id.Questionfour);
        question4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (question4.getCheckedRadioButtonId()){
                    case R.id.checkBoxStudent4YES:
                        valuefour = "1";
                        break;
                    case R.id.checkBoxStudent4NO:
                        valuefour = "0";
                }
            }
        });




        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Please leave feedback later");
                popupWindow.dismiss();
            }
        });

        btnSendFeedbakc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mRef.child("Studentfag").child(subjectcode).child("Week").child(week).child("Questions").child("Question1").push().setValue(valueone);
                mRef.child("Studentfag").child(subjectcode).child("Week").child(week).child("Questions").child("Question2").push().setValue(valuetwo);
                mRef.child("Studentfag").child(subjectcode).child("Week").child(week).child("Questions").child("Question3").push().setValue(valuethree);
                mRef.child("Studentfag").child(subjectcode).child("Week").child(week).child("Questions").child("Question4").push().setValue(valuefour);
                // Disse setter student
                mRefStud.child("Users").child(firebaseUser.getUid()).child("Stats").child(subjectcode).child("Questions1").push().setValue(valueone);
                mRefStud.child("Users").child(firebaseUser.getUid()).child("Feedback").child("Questions").child("Questions2").push().setValue(valuetwo);
                mRefStud.child("Users").child(firebaseUser.getUid()).child("Feedback").child("Questions").child("Questions3").push().setValue(valuethree);
                mRefStud.child("Users").child(firebaseUser.getUid()).child("Feedback").child("Questions").child("Questions4").push().setValue(valuefour);

                toast("Thanks for your feedback");
                popupWindow.dismiss();
            }


        });

    }
    public void toast(String tekst){
        Toast.makeText(this, tekst, Toast.LENGTH_SHORT).show();
    }


    public void feedbackwindow(){
        mRefFeed = FirebaseDatabase.getInstance().getReference();
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupview1 = layoutInflater.inflate(R.layout.activity_feedback, null);
        popupview1.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        final PopupWindow popupWindow1 = new PopupWindow(popupview1, popupview1.getMeasuredWidth(), popupview1.getMeasuredHeight(), true);
        popupWindow1.showAtLocation(popupview1, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow1.setOutsideTouchable(true);
        popupWindow1.setIgnoreCheekPress();

        final Button btnSendFeedbakc = (Button) popupview1.findViewById(R.id.bsubmit);
        final EditText edtFeedback = (EditText) popupview1.findViewById(R.id.addFeedbackEditText);

        btnSendFeedbakc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefFeed.child("Studentfag").child(subjectcode).child("Week").child(week).child("FeedBacks").push().setValue(edtFeedback.getText().toString());
                toast("Thanks for your feedback");
                popupWindow1.dismiss();







            }
        });


    }

}