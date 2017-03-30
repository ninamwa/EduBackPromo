package com.example.bruker.edubackpromo.data.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.anna.eduback2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;
    private HashMap<String, List<String>> infomap;
//    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public ArrayList<String> svar = new ArrayList<String>();
    String percentage = "";

    protected int maincount = 0, count0 = 0, count1 = 0, count2 = 0;

    protected double percentage0, percentage1,percentage2;
    protected String fagkode,week;


    public ExpandableListAdapter (Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap){
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
            convertView=inflater.inflate(R.layout.learning_goals_list_group, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.learning_goals_listitems, null);
        }
        final TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem0);
        txtListChild.setText(childText);

        String leg = getGroup(groupPosition).toString();
        List<String> svar = infomap.get(leg);
        String percentageText;
        int lav = 0, mid = 0, høy = 0;
        if (infomap.containsKey(leg)){
            for (String s : svar) {
                if (s.equals("Ingen forståelse")) {
                    lav++;
                } else if (s.equals("Delvis forståelse")) {
                    mid++;
                } else if (s.equals("Full forståelse")) {
                    høy++;
                }
            }
            double perc;
            switch (childText){
                case "Ingen forståelse":
                    perc = (100*lav)/(lav+mid+høy);
                    break;

                case "Delvis forståelse":
                    perc = (100*mid)/(lav+mid+høy);
                    break;
                case "Full forståelse":
                    perc = (100*høy)/(lav+mid+høy);
                    break;
                default:
                    perc = 0.0;
            }
            percentageText = perc + " %";
            final TextView percentage = (TextView) convertView.findViewById(R.id.prosentageTextView0);
            percentage.setText(percentageText);
            final TextView totalsubs = (TextView) convertView.findViewById(R.id.tvTotals);
            int tot = lav+mid+høy;
            totalsubs.setText("Answers: " + tot);
        }
        else{
            String noEvaluation = "No evaluation";
            final TextView percentage = (TextView) convertView.findViewById(R.id.prosentageTextView0);
            percentage.setText(noEvaluation);
            final TextView totalsubs = (TextView) convertView.findViewById(R.id.tvTotals);
            totalsubs.setText("");
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setFagkode(String fagkode1){
        this.fagkode = fagkode1;
    }
    public void setWeek(String week1){
        this.week = week1;
    }
    public void setLGInfo( HashMap<String, List<String>> infomap){this.infomap = infomap;}
    public void setFagName(View convertView,String fagName){
        final TextView title = (TextView) convertView.findViewById(R.id.titleTextViewLG);
        title.setText(fagName);
    }
}
