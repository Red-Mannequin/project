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

import com.redmannequin.resonance.R;
import com.redmannequin.resonance.Views.TrackView;

public class Flanger extends Fragment {

    public static Flanger getFragment() {
        Flanger fragment = new Flanger();
        return fragment;
    }

    private Button enter;
    private EditText length;
    private EditText wet;
    private EditText frequency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flanger, container, false);

        length = (EditText) rootView.findViewById(R.id.flanger_length);
        wet = (EditText) rootView.findViewById(R.id.flanger_wet);
        frequency = (EditText) rootView.findViewById(R.id.flanger_frequency);
        enter = (Button) rootView.findViewById(R.id.flanger_enter);

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("test", "frag");
                ((TrackView)getActivity()).setFlanger(Double.parseDouble(length.getText().toString()),
                        Double.parseDouble((wet.getText().toString())), Double.parseDouble(frequency.getText().toString()));
            }
        });

        return rootView;
    }
}