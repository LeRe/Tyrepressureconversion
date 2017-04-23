package ru.ijava.tyrepressureconversion;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by levchenko on 21.04.2017.
 */
public class ClinometrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinometr);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConstraintLayout clinometrLayout = (ConstraintLayout) findViewById(R.id.layout_clinometr);

        clinometrLayout.addView(new RollClinometrView(this));
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

}
