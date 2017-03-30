package com.example.bruker.edubackpromo.views.Stats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.anna.eduback2.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAttendanceActivity extends AppCompatActivity {

    private HashMap<String, List<String>> hashmap = new HashMap<>();
    private String subjectcode, week;
    PieChart pChartA1, pChartA2, pChartV, pChartT;
    private int qno =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_attendance);

        pChartA1 = (PieChart) findViewById(R.id.pieChartA1);
        pChartA2 = (PieChart) findViewById(R.id.pieChartA2);
        pChartV = (PieChart) findViewById(R.id.pieChartV);
        pChartT = (PieChart) findViewById(R.id.pieChartT);

        // Her lager vi statistikk for svar på spørsmålene

        // Hente ut svar på Qs i hashmap:
        Intent intent = getIntent();
        if(intent.hasExtra("subjectCode")){
            subjectcode = intent.getStringExtra("subjectCode");
        }
        if(intent.hasExtra("week")){
            week = intent.getStringExtra("week");
        }


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(subjectcode).child("Week").child(week).child("Questions");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                qno++;
                List<String> subs = new ArrayList<String>();
                String qnumber = dataSnapshot.getKey().toString();
                for (DataSnapshot answer : dataSnapshot.getChildren()){
                    String yesorno = answer.getValue().toString();
                    subs.add(yesorno);
                }
                hashmap.put(qnumber, subs);
                setPieChart(hashmap, qno);
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        pChartA1.setRotationEnabled(true);
        pChartA2.setRotationEnabled(true);
        pChartV.setRotationEnabled(true);
        pChartT.setRotationEnabled(true);

    }



    public void setPieChart(HashMap<String, List<String>> hashMap, int i){
        PieChart chart = (PieChart) findViewById(R.id.pieChartA1);
        String heading = "";
        switch (i){
            case 1:
                chart= (PieChart) findViewById(R.id.pieChartA1);
                heading ="Attendance Lecture 1";
                break;
            case 2:
                chart= (PieChart) findViewById(R.id.pieChartA2);
                heading ="Attendance Lecture 2";
                break;
            case 3:
                chart= (PieChart) findViewById(R.id.pieChartV);
                heading = "Watched on Video";
                break;
            case 4:
                chart= (PieChart) findViewById(R.id.pieChartT);
                heading = "Completed Weekly Task";
                break;
        }
        addData(hashMap, chart, i, heading);
    }

    public void addData(HashMap<String, List<String>> hashMap, PieChart chart, int i, String header) {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        String q = "Question" + i;
        float result = setPq(hashMap, q);

        yEntry.add(new PieEntry(result));
        yEntry.add(new PieEntry((100-result)));
        xEntry.add("Yes");
        xEntry.add("No");
        PieDataSet pieDataSet = new PieDataSet(yEntry,header);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(10);
        chart.setCenterText(header);
        chart.setHoleRadius(80);
        chart.setDrawEntryLabels(false);

        // Legg til farger
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        pieDataSet.setColors(colors);

        // Legg til Legend
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        PieData pieData= new PieData(pieDataSet);
        chart.setData(pieData);
        chart.invalidate();
    }

    public float setPq(HashMap<String, List<String>> hash, String q){
        List<String> answers = hash.get(q);
        int counter = 0;
        for (int j=0;j<answers.size();j++){
            if (answers.get(j).equals("1")){counter++;}
        } float participation = (100*counter)/answers.size();
        return participation;
    }
}
