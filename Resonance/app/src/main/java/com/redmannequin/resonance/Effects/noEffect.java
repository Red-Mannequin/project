package com.redmannequin.resonance.Effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redmannequin.resonance.R;

public class noEffect extends Fragment {

    public noEffect() { }

    public static noEffect getFragment() {
        noEffect fragment = new noEffect();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.no_effects, container, false);
        return rootView;
    }
}