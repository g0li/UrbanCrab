package com.lilliemountain.urbancrab.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ParkingLotWithDistanceAndTime implements Parcelable {
    ParkingLotWithDistance parkingLotWithDistance;
    String datetime;

    public ParkingLotWithDistanceAndTime() {
    }

    protected ParkingLotWithDistanceAndTime(Parcel in) {
        parkingLotWithDistance = in.readParcelable(ParkingLotWithDistance.class.getClassLoader());
        datetime = in.readString();
        bookingperiod = in.readString();
    }

    public static final Creator<ParkingLotWithDistanceAndTime> CREATOR = new Creator<ParkingLotWithDistanceAndTime>() {
        @Override
        public ParkingLotWithDistanceAndTime createFromParcel(Parcel in) {
            return new ParkingLotWithDistanceAndTime(in);
        }

        @Override
        public ParkingLotWithDistanceAndTime[] newArray(int size) {
            return new ParkingLotWithDistanceAndTime[size];
        }
    };

    public ParkingLotWithDistance getParkingLotWithDistance() {
        return parkingLotWithDistance;
    }

    public void setParkingLotWithDistance(ParkingLotWithDistance parkingLotWithDistance) {
        this.parkingLotWithDistance = parkingLotWithDistance;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getBookingperiod() {
        return bookingperiod;
    }

    public void setBookingperiod(String bookingperiod) {
        this.bookingperiod = bookingperiod;
    }

    public ParkingLotWithDistanceAndTime(ParkingLotWithDistance parkingLotWithDistance, String datetime, String bookingperiod) {
        this.parkingLotWithDistance = parkingLotWithDistance;
        this.datetime = datetime;
        this.bookingperiod = bookingperiod;
    }

    String bookingperiod;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(parkingLotWithDistance, flags);
        dest.writeString(datetime);
        dest.writeString(bookingperiod);
    }
}
