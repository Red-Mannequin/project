package com.redmannequin.resonance.BackendTesting;

import android.os.Parcel;
import android.os.Parcelable;

public class TrackB implements Parcelable {

    private String name;

    public TrackB(String name) {
        this.name = name;
    }

    protected TrackB(Parcel in) {
        name = in.readString();
    }

    public static final Creator<TrackB> CREATOR = new Creator<TrackB>() {
        @Override
        public TrackB createFromParcel(Parcel in) {
            return new TrackB(in);
        }

        @Override
        public TrackB[] newArray(int size) {
            return new TrackB[size];
        }
    };

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
