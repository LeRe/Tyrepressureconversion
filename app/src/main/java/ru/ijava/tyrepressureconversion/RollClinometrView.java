package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by rele on 4/23/17.
 */

public class RollClinometrView extends EmptyClinometrView {



    public RollClinometrView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setAngle(10);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}
