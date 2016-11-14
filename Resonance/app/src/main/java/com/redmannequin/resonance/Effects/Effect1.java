package com.redmannequin.resonance.Effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.redmannequin.resonance.R;
import com.redmannequin.resonance.Views.TrackView;

public class Effect1 extends Fragment {

    double value;

    public Effect1() {
        value = 0.0;
    }

    public static Effect1 getFragment() {
        Effect1 fragment = new Effect1();
        return fragment;
    }

    private Button enter;
    private EditText effect1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.effect1, container, false);

        effect1 = (EditText) rootView.findViewById(R.id.delay);
        enter = (Button) rootView.findViewById(R.id.delay_enter);

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((TrackView)getActivity()).setDelay(Double.parseDouble(effect1.getText().toString()));
            }
        });

        return rootView;
    }
}
