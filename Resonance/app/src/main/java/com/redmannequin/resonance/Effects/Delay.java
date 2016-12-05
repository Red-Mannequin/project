package com.redmannequin.resonance.Effects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.redmannequin.resonance.Backend.Effects.DelayEffect;
import com.redmannequin.resonance.R;
import com.redmannequin.resonance.Views.TrackView;

public class Delay extends Fragment {

    private Button enter;
    private EditText delay;
    private String delayText;
    private EditText decay;
    private String decayText;

    private int fragmentIndex;

    public static Delay getFragment() {
        Delay fragment = new Delay();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.delay, container, false);

        delay = (EditText) rootView.findViewById(R.id.delay);
        decay = (EditText) rootView.findViewById(R.id.decay);
        enter = (Button) rootView.findViewById(R.id.delay_enter);

        delay.setText(delayText);
        decay.setText(decayText);

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (enter.getText().equals("Apply")) {
                    ((TrackView) getActivity()).setDelay(Double.parseDouble(delay.getText().toString()), Double.parseDouble((decay.getText().toString())));
                    enter.setText("Disable");
                } else {
                    ((TrackView) getActivity()).toggleEffect(fragmentIndex);
                    enter.setText(enter.getText().equals("Disable") ? "Enable" : "Disable");
                }
            }
        });

        return rootView;
    }

    public void setDelayText(DelayEffect effect) {
        delayText = (Double.toString(effect.getDelay()));
    }

    public void setDecayText(DelayEffect effect) {
        decayText = (Double.toString(effect.getFactor()));
    }

    public void setFragmentIndex(int i) {
        fragmentIndex = i;
    }
}
