package com.lilliemountain.urbancrab.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.urbancrab.R;
import com.lilliemountain.urbancrab.adapter.ParkingLotWithDistanceAndTimeAdapter;
import com.lilliemountain.urbancrab.model.ParkingLotWithDistanceAndTime;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference booking,userId;
    List<ParkingLotWithDistanceAndTime> lot=new ArrayList<>();
    ParkingLotWithDistanceAndTimeAdapter pLWDATA;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database=FirebaseDatabase.getInstance();
        booking=database.getReference("booking");
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            userId=booking.child(uid);
            userId.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lot.clear();
                    for (DataSnapshot dx:
                         dataSnapshot.getChildren()) {
                        lot.add(dx.getValue(ParkingLotWithDistanceAndTime.class));
                    }
                    pLWDATA=new ParkingLotWithDistanceAndTimeAdapter(lot);
                    recyclerView.setAdapter(pLWDATA);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
