package com.example.pcadministrator.fitnesstracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/** @author Lea Celiku */
public class MainActivity extends AppCompatActivity implements SensorEventListener{

    long mLastStopTime;
    private SensorManager sensorManager;
    private TextView info;
    boolean activityRunning;
    private long steps;
    private static Button button_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Chronometer myChronometer = (Chronometer)findViewById(R.id.chronometer);
        Button buttonStart = (Button)findViewById(R.id.button_start);
        Button buttonPause = (Button)findViewById(R.id.button_pause);
        Button buttonEnd = (Button)findViewById(R.id.button_end);
        Button buttonSave=(Button)findViewById(R.id.button_save);
        info = (TextView)findViewById(R.id.text_info);
        info.setText("Information");
        mLastStopTime=0;
        activityRunning =true;
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Button button_db = (Button)findViewById(R.id.button_history);

        buttonStart.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
        public void onClick(View v) {
                        if (mLastStopTime==0)
               myChronometer.setBase(SystemClock.elapsedRealtime());
                        else {
               long intervalOnPause=(SystemClock.elapsedRealtime()- mLastStopTime);
               myChronometer.setBase(myChronometer.getBase()+intervalOnPause);
               }
                        myChronometer.start();

        }
        });

        buttonPause.setOnClickListener(
                new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myChronometer.stop();
                mLastStopTime = SystemClock.elapsedRealtime();
            }});

        buttonEnd.setOnClickListener(
                new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myChronometer.setBase(SystemClock.elapsedRealtime());
                myChronometer.stop();
                onStop(getBaseContext());
            }});

        buttonSave.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FitnessDB fitnessDB=new FitnessDB(getBaseContext());
                        fitnessDB.savingStepCounter((int)steps, convertToCalories(steps), convertToCalories(steps));
                        Log.i("StepCounterService","Information saved to database");
                        steps=0; //reset

                    }
                }
        );

        button_db.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent("com.example.pcadministrator.fitnesstracking.DatabaseViewer");
                        startActivity(intent);
                    }
                });
    }
    /**
     *Handles the event when the sensor stops.
     */
    public void onStop(Context context){
        sensorManager=(SensorManager)context.getSystemService(Activity.SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
        Log.i("StepCounterService","Sensor has stopped");
    }
    /**
     * Handles the event when sensor is able to work or not.
     */
    protected void onResume(){
        super.onResume();
        activityRunning = true;
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor != null){
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        } else {
            Toast.makeText(this, "Step sensor is not available", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onPause(){
        super.onPause();
        activityRunning = false;
    }

    /**
     * Method that displays the information (steps, distance and calories) in the info TextView.
     */
    @Override
    public void onSensorChanged(SensorEvent event){
       if (activityRunning){
            info = (TextView)findViewById(R.id.text_info);
            steps = (int) event.values[0];
            info.setText("Steps: " + String.valueOf(event.values[0])+ "\nCalories: " +String.valueOf(convertToCalories(steps)
                    + "\nDistance: " + String.valueOf(convertToDistance(steps))));
        }
    }
    /**
     * Method that calculates the calories and return this value.
     */
    private float convertToCalories(long steps){
        return (float) (steps*0.025);

    }
    /**
     * Method that calculates the distance and returns this value.
     */
    private float convertToDistance(long steps){

        return (float) (steps/2000);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        
    }
}
