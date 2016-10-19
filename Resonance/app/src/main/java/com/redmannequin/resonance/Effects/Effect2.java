package com.redmannequin.resonance.Effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redmannequin.resonance.R;

public class Effect2 extends Fragment {

    public Effect2() { }

    public static Effect2 getFragment() {
        Effect2 fragment = new Effect2();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.effect2, container, false);
        return rootView;
    }
}