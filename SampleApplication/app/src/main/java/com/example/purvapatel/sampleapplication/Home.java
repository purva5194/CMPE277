package com.example.purvapatel.sampleapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.purvapatel.sampleapplication.Supporting_Files.AppConfig;
import com.example.purvapatel.sampleapplication.Supporting_Files.MyTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.purvapatel.sampleapplication.R.id.text;

/**
 * Created by purvapatel on 4/12/17.
 */

public class Home extends Fragment {

    ToggleButton toggleButton;
    TextView temperature, brightness, motion;
    String token;
    Boolean led;
    String BASE_URL = "https://packers-backend.herokuapp.com";
    public MyTask myTask;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = getActivity().getLayoutInflater().inflate(R.layout.activity_home, container, false);
        toggleButton = (ToggleButton) view.findViewById(R.id.btn);
        temperature = (TextView) view.findViewById(R.id.outputTemprature);
        brightness = (TextView) view.findViewById(R.id.outputBrightness);
        motion = (TextView) view.findViewById(R.id.outputMotion);
        token = (String) getActivity().getIntent().getSerializableExtra("token");
        myTask = new MyTask(view.getContext());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                //text.setText("Status: " + isChecked);

                //test
                if (toggleButton.isChecked()) {
                    led = Boolean.valueOf(toggleButton.getTextOn().toString());
                    android.util.Log.d("led", String.valueOf(led));
                }
                else {
                    led = Boolean.valueOf(toggleButton.getTextOff().toString());
                    android.util.Log.d("led", String.valueOf(led));
                }
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(BASE_URL) //Setting the Root URL
                        .build();

                AppConfig.led api = adapter.create(AppConfig.led.class);
                api.ledData(
                        token,
                        led,
                        new Callback<Response>() {
                            @Override
                            public void success(Response result, Response response) {

                                try {

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                                    String resp;
                                    resp = reader.readLine();
                                    android.util.Log.d("success", "" + resp);

                                    JSONObject jObj = new JSONObject(resp);
                                    int success = jObj.getInt("success");

                                    if(success == 1){
                                        Toast.makeText(getActivity(), "led is "+led, Toast.LENGTH_SHORT).show();;
                                    } else{
                                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (IOException e) {
                                    android.util.Log.d("Exception", e.toString());
                                } catch (JSONException e) {
                                    Log.d("JsonException", e.toString());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        });
        onbuttonSensorData(view);
        myTask.execute();

        //fetch data from the database
        return view;

        //return getActivity().getLayoutInflater().inflate(R.layout.activity_home, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sensorValues();
            handler.postDelayed(this, 1000);
        }
    };

    public void onbuttonSensorData(View v){
        handler.postDelayed(runnable, 1000);
    }

    /*public void ledSwtich(View v){

        if (toggleButton.isChecked()) {
            led = Boolean.valueOf(toggleButton.getTextOn().toString());
            android.util.Log.d("led", String.valueOf(led));
        }
        else {
            led = Boolean.valueOf(toggleButton.getTextOff().toString());
            android.util.Log.d("led", String.valueOf(led));
        }
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL) //Setting the Root URL
                .build();

        AppConfig.led api = adapter.create(AppConfig.led.class);
        api.ledData(
                token,
                led,
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {

                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            String resp;
                            resp = reader.readLine();
                            android.util.Log.d("success", "" + resp);

                            JSONObject jObj = new JSONObject(resp);
                            int success = jObj.getInt("success");

                            if(success == 1){
                                Toast.makeText(getActivity(), "led is "+led, Toast.LENGTH_SHORT).show();;
                            } else{
                                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                            }


                        } catch (IOException e) {
                            android.util.Log.d("Exception", e.toString());
                        } catch (JSONException e) {
                            Log.d("JsonException", e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }*/

    public void sensorValues(){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL) //Setting the Root URL
                .build();

        AppConfig.sensor api = adapter.create(AppConfig.sensor.class);
        api.sensorData(new Callback<Response>() {
                           @Override
                           public void success(Response result, Response response) {

                               try {

                                   BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                                   String resp;
                                   resp = reader.readLine();
                                   android.util.Log.d("success", "" + resp);

                                   JSONObject jObj = new JSONObject(resp);
                                   int success = jObj.getInt("success");

                                   if(success == 1){
                                       //Toast.makeText(getApplicationContext(), "led is "+led, Toast.LENGTH_SHORT).show();;

                                       JSONArray obj = jObj.getJSONArray("obj");
                                       JSONObject values = (JSONObject) obj.get(0);
                                       String temperatureValue = values.getString("temperature");
                                       String brightnessValue = values.getString("brightness");
                                       String motionValue = values.getString("motion");
                                       android.util.Log.d("temperature", "" + temperatureValue);
                                       temperature.setText(temperatureValue + " " + (char) 0x00B0);
                                       brightness.setText(brightnessValue);
                                       motion.setText(motionValue);



                                   } else{
                                       // Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                   }


                               } catch (IOException e) {
                                   android.util.Log.d("Exception", e.toString());
                               } catch (JSONException e) {
                                   android.util.Log.d("JsonException", e.toString());
                               }
                           }

                           @Override
                           public void failure(RetrofitError error) {
                               Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                           }
                       }
        );
    }
}
