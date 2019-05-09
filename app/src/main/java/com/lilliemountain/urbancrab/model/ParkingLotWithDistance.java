package com.lilliemountain.urbancrab.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ParkingLotWithDistance implements Parcelable {
    ParkingLot parkingLot;
    Double distance;

    public ParkingLotWithDistance() {
    }

    protected ParkingLotWithDistance(Parcel in) {
        parkingLot = in.readParcelable(ParkingLot.class.getClassLoader());
        if (in.readByte() == 0) {
            distance = null;
        } else {
            distance = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(parkingLot, flags);
        if (distance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(distance);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParkingLotWithDistance> CREATOR = new Creator<ParkingLotWithDistance>() {
        @Override
        public ParkingLotWithDistance createFromParcel(Parcel in) {
            return new ParkingLotWithDistance(in);
        }

        @Override
        public ParkingLotWithDistance[] newArray(int size) {
            return new ParkingLotWithDistance[size];
        }
    };

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public ParkingLotWithDistance(ParkingLot parkingLot, Double distance) {
        this.parkingLot = parkingLot;
        this.distance = distance;
    }

}
