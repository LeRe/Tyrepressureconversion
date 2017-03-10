package ru.ijava.tyrepressureconversion;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private TextWatcher barTextWatcher;
    private TextWatcher psiTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final EditText editBar = (EditText) findViewById(R.id.editBar);
        final EditText editPsi = (EditText) findViewById(R.id.editPsi);

        barTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editPsi.removeTextChangedListener(psiTextWatcher);
                //editPsi.setText(s);
                editPsi.setText(String.valueOf(Float.valueOf(s.toString()) * 14.50377));
                editPsi.addTextChangedListener(psiTextWatcher);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        psiTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editBar.removeTextChangedListener(barTextWatcher);
                //editBar.setText(s);
                editBar.setText(String.valueOf(Float.valueOf(s.toString()) *  0.06894757));
                editBar.addTextChangedListener(barTextWatcher);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editBar.addTextChangedListener(barTextWatcher);
        editPsi.addTextChangedListener(psiTextWatcher);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
