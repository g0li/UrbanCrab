package com.lilliemountain.urbancrab.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilliemountain.urbancrab.R;
import com.lilliemountain.urbancrab.activities.BookParkingLotActivity;
import com.lilliemountain.urbancrab.model.ParkingLot;
import com.lilliemountain.urbancrab.model.ParkingLotWithDistance;
import com.lilliemountain.urbancrab.model.ParkingLotWithDistanceAndTime;

import java.util.List;

public class ParkingLotAdapter extends RecyclerView.Adapter<ParkingLotAdapter.ParkingLotHolder> {
    List<ParkingLotWithDistance> parkingLotList;

    public ParkingLotAdapter(List<ParkingLotWithDistance> parkingLotList) {
        this.parkingLotList = parkingLotList;
    }

    @NonNull
    @Override
    public ParkingLotHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_parking,viewGroup,false);
        return new ParkingLotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingLotHolder parkingLotHolder, int i) {
        parkingLotHolder.locationName.setText(parkingLotList.get(i).getParkingLot().getName());
        parkingLotHolder.address.setText(parkingLotList.get(i).getParkingLot().getAddress());
        String distances=parkingLotList.get(i).getDistance()+"";

        parkingLotHolder.distance.setText(distances.substring(0,4)+"km");
        parkingLotHolder.parkingLotWithDistance=parkingLotList.get(i);
    }

    @Override
    public int getItemCount() {
        return parkingLotList.size();
    }

    public class ParkingLotHolder extends RecyclerView.ViewHolder {
        TextView locationName,address,distance;
        ParkingLotWithDistance parkingLotWithDistance;
        public ParkingLotHolder(@NonNull View itemView) {
            super(itemView);
            locationName=itemView.findViewById(R.id.locationName);
            address=itemView.findViewById(R.id.address);
            distance=itemView.findViewById(R.id.distance);
            itemView.findViewById(R.id.book).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO BOOK PARKING
                    v.getContext().startActivity(new Intent(v.getContext(), BookParkingLotActivity.class).putExtra("scenekyahai",parkingLotWithDistance));
                }
            });
        }
    }
}
