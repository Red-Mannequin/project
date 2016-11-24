package com.redmannequin.resonance.Effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redmannequin.resonance.R;

public class PitchShift extends Fragment {

    public PitchShift() { }

    public static PitchShift getFragment() {
        PitchShift fragment = new PitchShift();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pitch_shift, container, false);
        return rootView;
    }
}
