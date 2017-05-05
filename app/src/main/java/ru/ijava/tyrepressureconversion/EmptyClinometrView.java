package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by levchenko on 21.04.2017.
 */
public class EmptyClinometrView extends View {
    private float angle = 0;

    private Paint paint;

    private Path pathLine;
    private Matrix transformingMatrix;

    private int sideSize = 0;

    private final static int PADDING = 30;

    private Rect textBounds = new Rect();

    public EmptyClinometrView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        int strokeWidth = 5;
        paint.setStrokeWidth(strokeWidth);
        int textSize = 60;
        paint.setTextSize(textSize);

        pathLine = new Path();
        transformingMatrix = new Matrix();

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

        canvas.drawCircle(sideSize/2, sideSize/2,sideSize/2 - PADDING, paint);

        pathLine.reset();
        pathLine.moveTo(0 + PADDING,getSideSize()/2);
        pathLine.lineTo(getSideSize() - PADDING, getSideSize()/2);

        transformingMatrix.reset();
        transformingMatrix.setRotate(getAngle(), getSideSize()/2, getSideSize()/2);
        pathLine.transform(transformingMatrix);

        canvas.drawPath(pathLine, paint);

        paint.getTextBounds(String.valueOf(angle), 0, String.valueOf(angle).length(), textBounds);
        canvas.drawText(String.valueOf(angle), getSideSize()/2 - textBounds.width() / 2, getSideSize() * 2 / 3 + textBounds.height() / 2, paint);

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        invalidate();
    }

    public int getSideSize()
    {
        return this.sideSize;
    }
}
