package ru.ijava.tyrepressureconversion;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

/**
 * Created by levchenko on 21.04.2017.
 */
public class ClinometrActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private float[] rotationMatrix;
    private float[] accelData;
    private float[] magnetData;
    private float[] orientationData;

    private int xyAngleRoll = 0;
    private int xzAnglePitch = 0;

    private RollClinometrView rollClinometrView;
    private PitchClinometrView pitchClinometrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinometr);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rollClinometrView = (RollClinometrView) findViewById(R.id.rollClinometrView);
        pitchClinometrView = (PitchClinometrView) findViewById(R.id.pitchClinometrView);

        //ConstraintLayout clinometrLayout = (ConstraintLayout) findViewById(R.id.layout_clinometr);
        //clinometrLayout.addView(new RollClinometrView(this));

        mSensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);

        rotationMatrix = new float[16];
        accelData = new float[3];
        magnetData = new float[3];
        orientationData = new float[3];

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(MenuApp.makeAction(this, item))
        {
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final int type = event.sensor.getType();
        if(type == Sensor.TYPE_ACCELEROMETER)
        {
            accelData = event.values.clone();
        }

        if(type == Sensor.TYPE_MAGNETIC_FIELD)
        {
            magnetData = event.values.clone();
        }

        SensorManager.getRotationMatrix(rotationMatrix, null, accelData, magnetData);
        SensorManager.getOrientation(rotationMatrix, orientationData);

        //xy - roll
        //xz - pitch

        xyAngleRoll = (int) Math.round(Math.toDegrees(orientationData[0]));
        xzAnglePitch = (int) Math.round(Math.toDegrees(orientationData[1]));

        rollClinometrView.setAngle(xyAngleRoll);
        pitchClinometrView.setAngle(xzAnglePitch);

        //Log.i("RELE","------------------------------------");
        //Log.i("RELE", "xy == " + String.valueOf(Math.round(Math.toDegrees(orientationData[0]))));
        //Log.i("RELE", "xz == " + String.valueOf(Math.round(Math.toDegrees(orientationData[1]))));
        //Log.i("RELE", "zy == " + String.valueOf(Math.round(Math.toDegrees(orientationData[2]))));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
