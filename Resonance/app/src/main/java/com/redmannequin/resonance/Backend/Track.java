package com.redmannequin.resonance.Backend;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

    private String name;

    public Track(String name) {
        this.name = name;
    }

    protected Track(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
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
