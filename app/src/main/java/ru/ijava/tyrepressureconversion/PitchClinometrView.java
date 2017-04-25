package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by levchenko on 24.04.2017.
 */

public class PitchClinometrView extends EmptyClinometrView {
    private Paint p;

    public PitchClinometrView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        p = new Paint();
        p.setColor(Color.RED);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawLine(getSideSize()/2,0,getSideSize()/2,getSideSize(),p);
    }







}
