package com.bumos.vgvee.terriblerecipesuggester;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity{

    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootReference,childReference;

    private Toolbar mTopToolbar;

    ArrayList<recipes> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recyclerView = findViewById(R.id.recyclerView3);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();
        childReference = rootReference.child(firebaseAuth.getCurrentUser().getUid());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar3);
        setSupportActionBar(mTopToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        childReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();

                for(DataSnapshot ds : dataSnapshots){
//                    Task currentTask = ds.getValue(Task.class);
//                    Log.e("TAG",""+currentTask.title);
                    recipes recipe = ds.getValue(recipes.class);
                    recipes.add(recipe);
                }
                adapter3 adapter = new adapter3(Main3Activity.this,recipes,childReference);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public static void removePost(Query query){
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("TAG", "onCancelled", databaseError.toException());
//            }
//        });
//    }
}
