package ru.ijava.tyrepressureconversion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Manometr view
 */

public class Manometer extends View {
    private int sideSize;

    private Paint p = new Paint();
    private RectF rectF = new RectF(0, 0, sideSize, sideSize);

    public int getSideSize() {
        return sideSize;
    }

    public Manometer(Context context) {
        super(context);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        if(height <= width)
        {
            sideSize = height;
        }
        else
        {
            sideSize = width;
        }

        p = new Paint();
        rectF = new RectF(0, 0, sideSize, sideSize);


        setFocusable(true);
    }

    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.CYAN);

        // smooths
        p.setAntiAlias(true);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        // opacity
        //p.setAlpha(0x80); //

        canvas.drawOval(rectF, p);
        p.setColor(Color.BLACK);
        canvas.drawArc (rectF, 90, 45, true, p);
    }

}
