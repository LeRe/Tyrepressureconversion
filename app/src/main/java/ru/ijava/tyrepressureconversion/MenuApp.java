package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by rele on 4/23/17.
 */

public class MenuApp {

    public static boolean makeAction(Context context, MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_clinometer)
        {
            Intent intent = new Intent(context, ClinometrActivity.class);
            context.startActivity(intent);

            return true;
        }
        else if (id == R.id.action_pressure)
        {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

            return true;
        }
        else if(id == R.id.action_settings) {
            Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }


}
