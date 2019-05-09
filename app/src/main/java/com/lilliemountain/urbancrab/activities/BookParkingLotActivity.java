package com.lilliemountain.urbancrab.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lilliemountain.urbancrab.R;
import com.lilliemountain.urbancrab.model.ParkingLotWithDistance;
import com.lilliemountain.urbancrab.model.ParkingLotWithDistanceAndTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookParkingLotActivity extends AppCompatActivity {
    ParkingLotWithDistance parkingLotWithDistance;
    TextView locationName,address,distance,fare;
    ImageView openMap;
    RadioGroup radioGroup;
    Double base_price;
    Double fareDouble;
    FirebaseDatabase database;
    DatabaseReference booking;
    String bookingperiod="30 mins";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_parking_lot);
        parkingLotWithDistance=getIntent().getParcelableExtra("scenekyahai");
        locationName=findViewById(R.id.locationName);
        address=findViewById(R.id.address);
        distance=findViewById(R.id.distance);
        fare=findViewById(R.id.fare);
        radioGroup=findViewById(R.id.radioGroup);
        openMap=findViewById(R.id.openMap);

        locationName.setText(parkingLotWithDistance.getParkingLot().getName());
        address.setText(parkingLotWithDistance.getParkingLot().getAddress());
        final String distances=parkingLotWithDistance.getDistance()+"";

        distance.setText(distances+" km");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ek:
                        base_price=1.0;
                        fareDouble=base_price*Double.parseDouble(distances);
                        fare.setText("₹"+fareDouble+"/-");
                        bookingperiod="1 hour";
                        break;
                    case R.id.tees:
                        base_price=0.9;
                        fareDouble=base_price*Double.parseDouble(distances);
                        fare.setText("₹"+fareDouble+"/-");
                        bookingperiod="30 mins";
                        break;
                    case R.id.pandra:
                        base_price=0.43;
                        fareDouble=base_price*Double.parseDouble(distances);
                        fare.setText("₹"+fareDouble+"/-");
                        bookingperiod="15 mins";
                        break;
                }
            }
        });
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SimpleDateFormat dateFormat=new SimpleDateFormat("dd-mmm-yyyy hh:mm");
                Calendar calendar= Calendar.getInstance();
                Date date=calendar.getTime();
                String formattedDate=dateFormat.format(date);
                 parkingLotWithDistanceAndTime=new ParkingLotWithDistanceAndTime(parkingLotWithDistance,formattedDate,bookingperiod);
                database=FirebaseDatabase.getInstance();
                booking=database.getReference("booking");
                DatabaseReference userID=booking.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                userID.push().setValue(parkingLotWithDistanceAndTime).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(v.getContext(),"Parking lot booked!",Toast.LENGTH_LONG).show();
                        //TODO STARTACTIVITY FOR MY BOOKINGS
                        startActivity(new Intent(BookParkingLotActivity.this,MyBookingsActivity.class));

                    }
                });
            }
        });
    }
    ParkingLotWithDistanceAndTime parkingLotWithDistanceAndTime;
}
