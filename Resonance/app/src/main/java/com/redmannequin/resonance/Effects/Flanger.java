package com.redmannequin.resonance.Effects;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.redmannequin.resonance.Backend.Effects.FlangerEffect;
import com.redmannequin.resonance.R;
import com.redmannequin.resonance.Views.TrackView;

public class Flanger extends Fragment {

    public static Flanger getFragment() {
        Flanger fragment = new Flanger();
        return fragment;
    }

    private Button enter;
    private EditText length;
    private String lengthText;
    private EditText wet;
    private String wetText;
    private EditText frequency;
    private String frequencyText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flanger, container, false);

        length = (EditText) rootView.findViewById(R.id.flanger_length);
        wet = (EditText) rootView.findViewById(R.id.flanger_wet);
        frequency = (EditText) rootView.findViewById(R.id.flanger_frequency);
        enter = (Button) rootView.findViewById(R.id.flanger_enter);

        length.setText(wetText);
        wet.setText(lengthText);
        frequency.setText(frequencyText);

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("test", "frag");
                ((TrackView)getActivity()).setFlanger(Double.parseDouble(length.getText().toString()),
                        Double.parseDouble((wet.getText().toString())), Double.parseDouble(frequency.getText().toString()));
            }
        });

        return rootView;
    }

    public void setLengthText(FlangerEffect effect) {
        lengthText = (Double.toString(effect.getMaxLength()));
    }

    public void setWetText(FlangerEffect effect) {
        wetText = (Double.toString(effect.getWetness()));
    }

    public void setFrequencyText(FlangerEffect effect) {
        frequencyText = (Double.toString(effect.getLowFilterFrequency()));
    }
}