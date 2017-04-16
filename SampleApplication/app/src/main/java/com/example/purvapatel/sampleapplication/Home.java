package com.example.purvapatel.sampleapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import static com.example.purvapatel.sampleapplication.R.id.text;

/**
 * Created by purvapatel on 4/12/17.
 */

public class Home extends Fragment {

    ToggleButton toggleButton;
    TextView temperature, brightness, motion;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_home, container, false);
        toggleButton = (ToggleButton) view.findViewById(R.id.btn);
        temperature = (TextView) view.findViewById(R.id.outputTemprature);
        brightness = (TextView) view.findViewById(R.id.outputBrightness);
        motion = (TextView) view.findViewById(R.id.outputMotion);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                //text.setText("Status: " + isChecked);
            }
        });

        return view;

        //return getActivity().getLayoutInflater().inflate(R.layout.activity_home, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");
    }
}
