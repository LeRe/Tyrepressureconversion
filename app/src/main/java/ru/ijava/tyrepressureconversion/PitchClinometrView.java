package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by levchenko on 24.04.2017.
 */

public class PitchClinometrView extends EmptyClinometrView {
    public PitchClinometrView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setAngle(-20);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }







}
