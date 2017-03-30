package com.example.bruker.edubackpromo.data.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anna.eduback2.R;
import com.example.anna.eduback2.data.tasks.TasksTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewHolders extends RecyclerView.ViewHolder {
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public CheckBox markIcon;
    public TextView categoryTitle;
    public ImageView deleteIcon;
    private List<TasksTask> taskObject;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Student Goals");

    public RecyclerViewHolders(final View itemView, final List<TasksTask> taskObject) {
        super(itemView);
        this.taskObject = taskObject;
        categoryTitle = (TextView) itemView.findViewById(R.id.task_title);
        deleteIcon = (ImageView) itemView.findViewById(R.id.task_delete);

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = taskObject.get(getAdapterPosition()).getTask();
                mDatabase.child("Users").child(firebaseUser.getUid()).child("Student Goals").child(taskTitle).removeValue();
            }
        });
    }
}