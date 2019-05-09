package com.lilliemountain.urbancrab.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilliemountain.urbancrab.R;
import com.lilliemountain.urbancrab.model.ParkingLotWithDistanceAndTime;

import java.util.List;

public class ParkingLotWithDistanceAndTimeAdapter extends RecyclerView.Adapter<ParkingLotWithDistanceAndTimeAdapter.ParkingLotHolder> {
    List<ParkingLotWithDistanceAndTime> parkingLotList;
    String geoUri;
    public ParkingLotWithDistanceAndTimeAdapter(List<ParkingLotWithDistanceAndTime> parkingLotList) {
        this.parkingLotList = parkingLotList;
    }

    @NonNull
    @Override
    public ParkingLotHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_parking_with_distance_and_time,viewGroup,false);
        return new ParkingLotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingLotHolder parkingLotHolder, int i) {
        parkingLotHolder.locationName.setText(parkingLotList.get(i).getParkingLotWithDistance().getParkingLot().getName());
        parkingLotHolder.address.setText(parkingLotList.get(i).getParkingLotWithDistance().getParkingLot().getAddress());
        parkingLotHolder.distance.setText(parkingLotList.get(i).getParkingLotWithDistance().getDistance()+" km");
        parkingLotHolder.date.setText(parkingLotList.get(i).getDatetime());
        parkingLotHolder.bookingperiod.setText(parkingLotList.get(i).getBookingperiod());
         geoUri = "http://maps.google.com/maps?q=loc:" + parkingLotList.get(i).getParkingLotWithDistance().getParkingLot().getLat()
                + "," + parkingLotList.get(i).getParkingLotWithDistance().getParkingLot().getLng() + " (" + parkingLotList.get(i).getParkingLotWithDistance().getParkingLot().getName() + ")";
        parkingLotHolder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parkingLotList.size();
    }

    public class ParkingLotHolder extends RecyclerView.ViewHolder {
        TextView locationName,address,distance,date,bookingperiod;
        ImageView imageView2;
        public ParkingLotHolder(@NonNull View itemView) {
            super(itemView);
            locationName=itemView.findViewById(R.id.locationName);
            address=itemView.findViewById(R.id.address);
            distance=itemView.findViewById(R.id.distance);
            date=itemView.findViewById(R.id.date);
            bookingperiod=itemView.findViewById(R.id.bookingperiod);
            imageView2=itemView.findViewById(R.id.imageView2);
        }
    }
}
