package com.example.bruker.edubackpromo.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.data.models.Fag;
import com.example.anna.eduback2.data.models.ViewHolder;

import java.util.List;

/**
 * Created by Bruker on 28.02.2017.
 */

public class CustomAdapter extends ArrayAdapter<Fag>{


    OnDeleteClickedListener mListener;

    public CustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CustomAdapter(Context context, int resource, List<Fag> items,OnDeleteClickedListener listener) {
        super(context, resource, items);
        mListener = listener;
    }

    //Overrider getView metoden. Den som definerer hvordan objektene i listen vil se ut.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mainViewholder = null;
        //Tilknytter viewet vi skal konvertere til en lokal variabel.
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlistrow, null);
        }
        //Variabel posisjonen blir sendt inn som et argument.
        //P referer til det aktuelle objektet. Det vi har trykket på.
        final Fag p = getItem(position);

        // Her referer jeg til alle textViewene
        // Disse TextViewsene er lagd i XML filen som defineres som ListView layouten
        if (p != null) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) v.findViewById(R.id.id);
            viewHolder.button = (Button) v.findViewById(R.id.delete_button);
            v.setTag(viewHolder);

            mainViewholder = (ViewHolder) v.getTag();
            //Sjekker om hver av TextViewene er null, hvis de ikke er det setter vi tekst.
            if (mainViewholder.title != null) {
                mainViewholder.title.setText(p.getName());
            }

            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDeleteReady(p.getCode());
                }
            });
        }
        //Returnerer viewen til aktiviteten
        return v;
    }

    public interface OnDeleteClickedListener{
        void onDeleteReady(String code);
    }
}