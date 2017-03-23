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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Manometr view
 */

public class Manometer extends View implements View.OnTouchListener {

    private float pressurePsi = 0;

    private float arrowAngle = 0;

    private float cX;
    private float cY;

    private int sideSize;
    private final int startAngle = 135;
    private final int sweepAngle = 270;

    private final int padding = 100;

    private Paint p;
    private RectF rectF;

    private Path pathHatch;
    private Path pathText;
    private Path pathArrow;


    private Matrix matrixHatch;
    private Matrix matrixArrow;

    private MainActivity mainActivity;

    public int getSideSize() {
        return sideSize;
    }

    public Manometer(Context context) {
        super(context);

        mainActivity =(MainActivity) context;

        setArrowAngle(0);

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

        cX = sideSize/2;
        cY = sideSize/2;

        p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setTextSize(60);

        rectF = new RectF(padding, padding, sideSize - padding, sideSize - padding);
        pathHatch = new Path();
        pathText = new Path();
        pathArrow = new Path();
        matrixHatch = new Matrix();
        matrixArrow = new Matrix();

        this.setOnTouchListener(this);

        setFocusable(true);
    }

    protected void onDraw(Canvas canvas) {

        // paint background
        //canvas.drawColor(Color.CYAN);

        // рисуем дугу баров
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

        int angle = -135; // Стартовый угол, цифры начинаем рисовать отсюда
        int angleDigitRotate = 135; // Угол для повората цифр
        for(int i=0; i<=6; i++){

            // рисуем основные штрихи баров
            matrixHatch.reset();
            matrixHatch.setRotate(angle, sideSize/2, sideSize/2);
            pathHatch.transform(matrixHatch);
            canvas.drawPath(pathHatch, p);

            angle = 45; //Начиная с первой итерации цикла сдвигаемся на 45 градусов


            // Рисуем цифру давления в барах
            // относительно x y будем крутить цифры чтобы выровнять их по вертикали
            int radius = sideSize / 2 - padding + 2*hatchSize;
            float x = (float) Math.cos(Math.toRadians(angleDigitRotate)) * (radius) + sideSize / 2;
            float y = (float) Math.sin(Math.toRadians(angleDigitRotate)) * (radius) + sideSize / 2;
            canvas.drawText(String.valueOf(i), x, y, p);
            angleDigitRotate += 45;


        }

        // draw arrow
        int centralRadius = 10;
        int shiftArrow = 40;
        p.setColor(Color.GREEN);

        pathArrow.reset();
        pathArrow.addCircle(sideSize/2, sideSize/2, centralRadius, Path.Direction.CW);
        pathArrow.moveTo(sideSize/2, sideSize/2 + shiftArrow);
        pathArrow.lineTo(sideSize/2, padding + shiftArrow);

        matrixArrow.reset();
        matrixArrow.setRotate(arrowAngle, sideSize/2, sideSize/2);
        pathArrow.transform(matrixArrow);

        canvas.drawPath(pathArrow, p);


        //TODO Убрать бардак, привести в порядок свойства и методы  класса, приблизить их к реалиям и логике
        //TODO создание path вынести из onDraw, в onDraw оставить только прорисовку paths




    }

    /**
     *
     * @param arrowAngle   реальный угол отклонения стрелки от 0 до 270
     */
    public void setArrowAngle(float arrowAngle)
    {
        //Вставим тут проверку заданного угла, если угол уходит за границы опустим на ноль или зашалим манометр

        if(arrowAngle < 0)
        {
            arrowAngle = 0;
        } else if (arrowAngle > 270)
        {
            arrowAngle = 270;
        }

        // получим и сохраним текущее давление по углу
        // 1 bar = 14,5038 psi
        this.pressurePsi = (float) (arrowAngle * 6 * 14.5038 / 270);

        // Сохраним угол с поправкой для отображения
        this.arrowAngle = arrowAngle - 135;
    }

    public void setBarPressure(float bar)
    {
        if(bar < 0)
        {
            bar = 0;
        }
        else if(bar > 6)
        {
            bar = 6;
        }

        //пересчитываем в градусы исходя из пропорции 0 - 0, 6 - 270
        setArrowAngle(bar*270/6);
        invalidate();
    }

    public void setPsiPressure(float psi)
    {
        setBarPressure((float) (psi * 0.0689476));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        //setArrowAngle(Utils.angleByTouchCoordinates(x, y, cX, cY));
        setBarViaAngle(Utils.angleByTouchCoordinates(x, y, cX, cY));
        invalidate();

        return true;
    }

    private void setBarViaAngle(float angle) {
        setArrowAngle(angle);
        mainActivity.setPressure(this.pressurePsi);
    }
}


// arrowAngle - -135 - 135