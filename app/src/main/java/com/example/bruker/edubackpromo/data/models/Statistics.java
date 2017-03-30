package com.example.bruker.edubackpromo.data.models;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Statistics {

    public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public String måloppnåelse;
    ArrayList<String> info = new ArrayList<>();
    ArrayList<String> pc = new ArrayList<>();
    ArrayList<String> svar = new ArrayList<>();
    String percentageText1,percentageText2,percentageText3;
    int none =0, some = 0, all = 0;

    public Statistics(){
    }

    public ArrayList<String> getFeedbackArray(String week, String fagkode, final String læringsmål){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(fagkode).child("Week").child(week).child("LGStatistics").child(læringsmål);
        mDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String cvalue = child.getValue().toString();
                    svar.add(cvalue);
                    int str = svar.size();
                    String item = "";
                }
                for (int i=0; i<svar.size();i++){
                    String infor = svar.get(i);
                    info.add(infor);
                }
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        return svar;
    }

    public ArrayList<String> returnPercentages(String fagkode, String week, String lg){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(fagkode).child("Week").child(week).child("LGStatistics").child(lg);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int answers = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                switch (dataSnapshot.getValue(String.class)){
                    case "Ingen forståelse":
                        none++;
                    case "Delvis forståelse":
                        some++;
                    case "Full forståelse":
                        all++;
                }
                double number1 = (100*(none/(none+some+all)));
                percentageText1 = number1 + " %";
                double number2 = (100*(some/(none+some+all)));
                percentageText2 =number2 + " %";
                double number3 = (100*(all/(none+some+all)));
                percentageText3 = number3 + " %";
                pc.add(percentageText1);
                pc.add(percentageText2);
                pc.add(percentageText3);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });


        int size = pc.size();
        String pc1 = pc.get(0);
        String pc2 = pc.get(1);
        String pc3 = pc.get(2);

        return pc;


    }

}
