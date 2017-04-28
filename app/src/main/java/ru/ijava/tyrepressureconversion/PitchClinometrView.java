package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import static android.R.attr.path;

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
