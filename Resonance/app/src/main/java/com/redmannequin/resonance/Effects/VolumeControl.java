package com.redmannequin.resonance.Effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redmannequin.resonance.R;

public class VolumeControl extends Fragment {

    public VolumeControl() { }

    public static VolumeControl getFragment() {
        VolumeControl fragment = new VolumeControl();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.volume_control, container, false);
        return rootView;
    }
}