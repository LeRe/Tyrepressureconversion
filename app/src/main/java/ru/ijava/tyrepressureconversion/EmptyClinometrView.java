package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by levchenko on 21.04.2017.
 */
public class EmptyClinometrView extends View {
    private Paint p;
    protected int sideSize = 0;

    public EmptyClinometrView(Context context, AttributeSet attrs) {
        super(context, attrs);

        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        View parent = (View) this.getParent();
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();

        if (parentWidth >= parentHeight) {
            this.sideSize = parentWidth / 2;
        } else {
            this.sideSize = parentHeight / 2;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.height = this.sideSize;
        params.width = this.sideSize;
        this.setLayoutParams(params);

        canvas.drawCircle(sideSize/2, sideSize/2,sideSize/2,p);

    }

    protected int getSideSize()
    {
        return this.sideSize;
    }

}
