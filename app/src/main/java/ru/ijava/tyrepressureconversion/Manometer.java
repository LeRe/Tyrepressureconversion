package ru.ijava.tyrepressureconversion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Manometr view
 */

public class Manometer extends View {
    private int sideSize;
    private final int startAngle = 135;
    private final int sweepAngle = 270;

    private final int padding = 100;

    private Paint p;
    private RectF rectF;
    private Path pathHatch;
    private Path pathText;
    private Matrix matrixHatch;
    private Matrix matrixText;

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
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setTextSize(60);

        rectF = new RectF(padding, padding, sideSize - padding, sideSize - padding);
        pathHatch = new Path();
        pathText = new Path();
        matrixHatch = new Matrix();
        matrixText = new Matrix();

        setFocusable(true);
    }

    protected void onDraw(Canvas canvas) {
        // paint background
        //canvas.drawColor(Color.CYAN);

        //drawArc
        p.setColor(Color.WHITE);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, p);

        //draw hatch
        int hatchSize = 30;
        //canvas.drawLine(sideSize/2, 0 + padding, sideSize/2, sideSize/2 + hatchSize, p);
        pathHatch.reset();
        pathHatch.moveTo(sideSize/2, padding - hatchSize);
        pathHatch.lineTo(sideSize/2, padding);

        pathText.reset();
        pathText.moveTo(sideSize/2, padding - hatchSize - hatchSize/4);
        pathText.lineTo(sideSize/2+1, padding - hatchSize - hatchSize/4);

        int angle = -135;
        for(int i=0; i<=6; i++){
            matrixHatch.reset();
            matrixHatch.setRotate(angle, sideSize/2, sideSize/2);
            pathHatch.transform(matrixHatch);

            matrixText.reset();
            matrixText.setRotate(angle, sideSize/2, sideSize/2);
            matrixText.setSinCos((float) 0.1, (float) 0.9);

            pathText.transform(matrixText);
            canvas.drawPath(pathHatch, p);
            canvas.drawTextOnPath(String.valueOf(i), pathText, 0, 0, p);
            angle = 45;
        }


        // draw arrow
        int centralRadius = 10;
        int shiftArrow = 40;
        p.setColor(Color.RED);
        canvas.drawCircle(sideSize/2, sideSize/2, centralRadius, p);
        canvas.drawLine(sideSize/2, sideSize/2 + shiftArrow, sideSize/2, padding + shiftArrow, p);

    }

}
