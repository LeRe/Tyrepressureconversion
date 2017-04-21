package ru.ijava.tyrepressureconversion;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by levchenko on 21.04.2017.
 */
public class ClinometrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinometr);

        ConstraintLayout clinometrLayout = (ConstraintLayout) findViewById(R.id.layout_clinometr);

        clinometrLayout.addView(new ClinometrView(this));
    }
}
