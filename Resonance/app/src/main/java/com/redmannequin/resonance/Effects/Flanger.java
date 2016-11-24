package com.redmannequin.resonance.Effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redmannequin.resonance.R;

public class Flanger extends Fragment {

    public Flanger() { }

    public static Flanger getFragment() {
        Flanger fragment = new Flanger();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flanger, container, false);
        return rootView;
    }
}