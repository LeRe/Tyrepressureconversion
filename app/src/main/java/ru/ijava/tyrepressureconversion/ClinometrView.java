package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by levchenko on 21.04.2017.
 */
public class ClinometrView extends View {
    private Paint p;
    private int width;
    private int height;

    public ClinometrView(Context context) {
        super(context);

        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.GREEN);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        this.width = this.getMeasuredWidth();
        this.height = this.getMeasuredHeight();

        canvas.drawCircle(width/2, height/2,width/4,p);

    }
}
