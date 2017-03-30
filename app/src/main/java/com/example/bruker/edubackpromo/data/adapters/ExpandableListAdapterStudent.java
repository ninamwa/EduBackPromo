package com.example.bruker.edubackpromo.data.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.eduback2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterStudent extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;
    private DatabaseReference mDatabase;

    protected String fagkode, week;

    public ExpandableListAdapterStudent (Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap){
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.learning_goals_student_list_group, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.explListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.learning_goals_student_listitems, null);
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.explListItem);
        txtListChild.setText(childText);


        RadioButton rbutt = (RadioButton) convertView.findViewById(R.id.rbutt);

        final View view = convertView;
        rbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sende avgårde 0,1 eller 2 avhengig av hvilken knapp som har blitt trykket på

                if(moreThanOne(groupPosition, view)){
                    Toast.makeText(view.getContext(), "Cannot choose more than one option!", Toast.LENGTH_LONG).show();
                }

                String stat = "";
                if (childText.equals("Ingen forståelse")){
                    stat = "Ingen forståelse";
                } else if (childText.equals("Delvis forståelse")){
                    stat = "Delvis forståelse";
                } else if (childText.equals("Full forståelse")){
                    stat = "Full forståelse";
                }
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String userID = firebaseUser.getUid();                      // Få UserID
                String lg = getGroup(groupPosition).toString();             // Få læringsmål
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Studentfag").child(fagkode).child("Week").child(week).child("LGStatistics");
                mDatabase.child(lg).child(userID).setValue(stat);
                // https://www.youtube.com/watch?v=NtUiB0p4v90&t=218s
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setFagkode(String fagkode){
        this.fagkode = fagkode;
    }
    public void setWeek(String week){
        this.week = week;
    }
    public void setFagName(View convertView,String fagName){
        final TextView title = (TextView) convertView.findViewById(R.id.textViewLearningGoal);
        title.setText(fagName);
    }

    public boolean moreThanOne(int grouppos, View view) {
        int noClicked = 0;
        for (int i = 0; i < getChildrenCount(grouppos); i++) {
            Object child = getChild(grouppos, i);
            RadioButton radio = (RadioButton) view.findViewById(R.id.rbutt);
            if (radio.isChecked()) {
                noClicked++;
            }
        }
        if (noClicked > 1) {
            return true;
        } else {
            return false;
        }
    }
}