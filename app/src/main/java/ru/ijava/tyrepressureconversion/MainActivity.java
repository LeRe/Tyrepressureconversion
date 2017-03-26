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
import android.widget.LinearLayout;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private TextWatcher barTextWatcher;
    private TextWatcher psiTextWatcher;
    private Manometer manometr;

    private EditText editBar = null;
    private EditText editPsi = null;

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

        //Создаем и добавляем виджет манометра на экран
        LinearLayout manometrPlace = (LinearLayout) findViewById(R.id.manometrView);
        this.manometr = new Manometer(this);
        manometrPlace.setLayoutParams(new LinearLayout.LayoutParams(manometr.getSideSize(),manometr.getSideSize()));
        manometrPlace.addView(manometr);

        editBar = (EditText) findViewById(R.id.editBar);
        editPsi = (EditText) findViewById(R.id.editPsi);


        //Обработчик изменения поля для значения давления в bar
        barTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editPsi.removeTextChangedListener(psiTextWatcher);

                float bar;
                if(s.length() == 0)
                {
                    editBar.setText( String.format(Locale.getDefault(), "%d", (0)) );
                    bar = 0;
                }
                else
                {
                    bar = Float.valueOf(s.toString());
                }

                editPsi.setText( String.format(Locale.getDefault(), "%2.2f", Utils.bar2psi(bar)) );
                manometr.setPressure(bar, false);

                editPsi.addTextChangedListener(psiTextWatcher);
            }
        };


        //Обработчик изменения поля для значения давления в psi
        psiTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                float psi;

                editBar.removeTextChangedListener(barTextWatcher);
                if(s.length() == 0)
                {
                    editPsi.setText( String.format(Locale.getDefault(), "%d", 0) );
                    psi = 0;
                }
                else
                {
                    psi = Float.valueOf(s.toString());
                }

                editBar.setText( String.format(Locale.getDefault(), "%2.2f", Utils.psi2bar(psi)) );
                manometr.setPressure(psi, true);

                editBar.addTextChangedListener(barTextWatcher);
            }
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

    /**
     * Помещает заданное значение давления в поля bar и psi
     *
     * @param psi значение давления в psi
     */
    public void updateTextFields(float psi)
    {
        editBar.removeTextChangedListener(barTextWatcher);
        editPsi.removeTextChangedListener(psiTextWatcher);

        float bar = (float) (psi / 14.5038);
        editBar.setText(String.format(Locale.getDefault(), "%2.2f", bar));
        editPsi.setText(String.format(Locale.getDefault(), "%2.2f", psi));

        editBar.addTextChangedListener(barTextWatcher);
        editPsi.addTextChangedListener(psiTextWatcher);
    }
}
