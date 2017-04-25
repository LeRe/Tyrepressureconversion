package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by rele on 4/23/17.
 */

public class RollClinometrView extends EmptyClinometrView {
    private Paint p;

    public RollClinometrView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        p = new Paint();
        p.setColor(Color.RED);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawLine(0,getSideSize()/2,getSideSize(),getSideSize()/2,p);
    }
}
