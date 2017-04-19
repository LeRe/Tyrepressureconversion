package ru.ijava.tyrepressureconversion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Manometr view
 */
public class Manometer extends View implements View.OnTouchListener {

    private float pressurePsi = 0;  // Текущее давление манометра

    private float arrowAngle = -135;   // Текущий угол отклонения стрелки манометра  для матрицы поворота изменяется от  -135 до 135, с поправкой для первоночального отклонения на ноль

    private float cX;   // Координата центра манометра по оси Х
    private float cY;   // Координата центра манометра по оси У
    private int sideSize;   // Размер стороны Виджета

    private final int PADDING = 100;  // отступ от физических границ виджета до шкалы(элементов виджета)

    private final int SW_SMALL = 5;    // SW - stroke width
    private final int SW_MEDIUM = 10;
    private final int SW_BIG = 15;
    private final int TEXT_SIZE = 60;

    // for pressure gauge needle
    private float[][] pointsArrow = new float[6][2];
    private final float C_X;
    private final float C_Y;
    private final float L;
    private final float L1;
    private final float W;
    private final float C_RAD_BIG;
    private final float C_RAD_SMALL;

    private Paint p;

    private Path pathHatch; // патч для штрихов
    private Path pathArrow; // патч для стрелки

    private Matrix matrixHatch; // Матрица для работы со штрихами
    private Matrix matrixArrow; // Матрица для поморота стрелки

    private MainActivity mainActivity; //Ссылка на активность в который размещен виджет манометра для передачи значений давления в текстовые поля


    /**
     *  Возвращает длинну стороны виджета    // На кой я это сделал предстоит посмотреть ))))
     * @return длинна стороны виджета
     */
    public int getSideSize() {
        return sideSize;
    }

    public Manometer(Context context) {
        super(context);

        mainActivity =(MainActivity) context;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //Определяемся с длиной стороны виджета и координатами центра, относительно которых будем рисовать манометр
        if(height <= width)
        {
            this.sideSize = height;
        }
        else
        {
            this.sideSize = width;
        }
        this.cX = sideSize/2;
        this.cY = sideSize/2;

        // готовимся к рисованию
        p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(SW_SMALL);
        p.setTextSize(TEXT_SIZE);

        //создаем патчи и преобразующие матрицы
        pathHatch = new Path();
        pathArrow = new Path();
        matrixHatch = new Matrix();
        matrixArrow = new Matrix();

        // будем обрабатывать касания
        this.setOnTouchListener(this);

        C_X = sideSize/2;
        C_Y = sideSize/2;
        L = C_Y;
        L1 = L / 3;
        W = L1 / 6;
        C_RAD_BIG = (float) (W/1.2);
        C_RAD_SMALL = W/2;
        initNedle();
    }

    private void initNedle()
    {
        pointsArrow[0][0] = C_X - W / 2;
        pointsArrow[0][1] = C_Y + L1 / 2;

        pointsArrow[1][0] = C_X + W / 2;
        pointsArrow[1][1] = C_Y + L1 / 2;

        pointsArrow[2][0] = C_X - W / 2;
        pointsArrow[2][1] = C_Y - L1 / 2;

        pointsArrow[3][0] = C_X;
        pointsArrow[3][1] = C_Y - L1 / 2 - (L - L1);

        pointsArrow[4][0] = C_X + W / 2;
        pointsArrow[4][1] = C_Y - L1 / 2;

        pointsArrow[5][0] = C_X - W / 2;
        pointsArrow[5][1] = C_Y + L1 / 2;

    }

    /**
     * Draw manometer background
     * @param canvas canvas
     */
    private void drawBackGround(Canvas canvas) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(C_X, C_Y, C_X, p);

        Rect textRect = new Rect();
        float x;
        float y;
        final float SHIFT = (float) 0.8;

        String psi = "PSI";
        p.setColor(Color.RED);
        p.getTextBounds(psi, 0, psi.length(),textRect);
        x = sideSize / 2 - textRect.width()/2;
        y = sideSize/2 + sideSize/4 - textRect.height() * SHIFT;
        canvas.drawText("PSI", x, y, p);

        String bar = "BAR";
        p.setColor(Color.BLACK);
        p.getTextBounds(bar, 0, bar.length(), textRect);
        x = sideSize / 2 - textRect.width()/2;
        y = sideSize/2 + sideSize/4 + textRect.height() * SHIFT;
        canvas.drawText(bar, x, y, p);
    }

    //TODO Переделать подписи к шкалам через p.getTextBounds(), поубирать костыли

    private void drawBarArc(Canvas canvas)
    {
        final int START_ANGLE = 135;
        final int SWEEP_ANGLE = 270;
        final int BIG_HATCH_SIZE = 30;
        final int SMALL_HATCH_SIZE = BIG_HATCH_SIZE / 2;
        final float SCALE = (float) 1;

        // рисуем дугу баров
        p.setColor(Color.BLACK);
        p.setStrokeWidth(SW_SMALL);
        p.setStyle(Paint.Style.STROKE);

        // Для рисования дуги шкалы
        RectF rectF = new RectF(PADDING * SCALE, PADDING * SCALE, sideSize - PADDING * SCALE, sideSize - PADDING * SCALE);
        canvas.drawArc(rectF, START_ANGLE, SWEEP_ANGLE, false, p);

        //draw small hatch
        pathHatch.reset();
        pathHatch.moveTo(sideSize/2, PADDING * SCALE - SMALL_HATCH_SIZE);
        pathHatch.lineTo(sideSize/2, PADDING * SCALE);

        int angle = - 135; // Стартовый угол, штрихи начинаем рисовать отсюда
        for(int i = 0; i <= 6 * 5; i++)
        {
            matrixHatch.reset();
            matrixHatch.setRotate(angle, sideSize/2, sideSize/2);
            pathHatch.transform(matrixHatch);
            canvas.drawPath(pathHatch, p);
            if(i == 0) angle = 9;
        }

        //draw BIG hatch рисуем большие штрихи
        pathHatch.reset();
        pathHatch.moveTo(sideSize/2, PADDING * SCALE - BIG_HATCH_SIZE);
        pathHatch.lineTo(sideSize/2, PADDING * SCALE);

        // Расставляем цифры баров
        angle = -135; // Стартовый угол, цифры начинаем рисовать отсюда
        int angleDigitRotate = 135; // Угол для повората цифр
        for(int i=0; i<=6; i++){
            // рисуем основные штрихи баров
            matrixHatch.reset();
            matrixHatch.setRotate(angle, sideSize/2, sideSize/2);
            pathHatch.transform(matrixHatch);
            p.setStrokeWidth(SW_MEDIUM);
            canvas.drawPath(pathHatch, p);

            if(i == 0) angle = 45; //Начиная с первой итерации цикла сдвигаемся на 45 градусов

            // Рисуем цифру давления в барах
            // относительно x y будем крутить цифры чтобы выровнять их по вертикали
            float radius = sideSize / 2 - PADDING * SCALE + 2 * BIG_HATCH_SIZE;
            int shift = (i + 2) * 3;
            float x = (float) Math.cos(Math.toRadians(angleDigitRotate)) * (radius) + sideSize / 2 - shift;
            float y = (float) Math.sin(Math.toRadians(angleDigitRotate)) * (radius) + sideSize / 2 + shift;
            p.setStrokeWidth(SW_SMALL);
            canvas.drawText(String.valueOf(i), x, y, p); // x,y - расчитанные координаты места где рисуем цифру давления
            angleDigitRotate += 45;
        }
    }

    private void drawPsiArc(Canvas canvas)
    {
        final int START_ANGLE = 135;
        final int SWEEP_ANGLE = 270;
        final int BIG_HATCH_SIZE = 30;
        final int SMALL_HATCH_SIZE = BIG_HATCH_SIZE / 2;

        final float SCALE = (float) 1.3;

        // рисуем дугу баров
        p.setColor(Color.RED);
        p.setStrokeWidth(SW_SMALL);
        p.setStyle(Paint.Style.STROKE);

        // Для рисования дуги шкалы
        RectF rectF = new RectF(PADDING * SCALE, PADDING * SCALE, sideSize - PADDING * SCALE, sideSize - PADDING * SCALE);
        canvas.drawArc(rectF, START_ANGLE, SWEEP_ANGLE, false, p);

        //draw small hatch
        pathHatch.reset();
        pathHatch.moveTo(sideSize/2, PADDING * SCALE);
        pathHatch.lineTo(sideSize/2, PADDING * SCALE + SMALL_HATCH_SIZE);

        float angle = - 135; // Стартовый угол, штрихи начинаем рисовать отсюда
        for(int i = 0; i <= 87; i++)
        {
            matrixHatch.reset();
            matrixHatch.setRotate(angle, sideSize/2, sideSize/2);
            pathHatch.transform(matrixHatch);
            canvas.drawPath(pathHatch, p);

            if(i == 0) angle = (float) 3.1;
        }

        //draw BIG hatch рисуем большие штрихи
        pathHatch.reset();
        pathHatch.moveTo(sideSize/2, PADDING * SCALE);
        pathHatch.lineTo(sideSize/2, PADDING * SCALE + BIG_HATCH_SIZE);

        // Расставляем цифры баров
        angle = -135; // Стартовый угол, цифры начинаем рисовать отсюда
        int angleDigitRotate = 135; // Угол для повората цифр
        for(int i=0; i<=87; i++){
            // рисуем основные штрихи баров
            if(i % 10 == 0)
            {
                matrixHatch.reset();
                matrixHatch.setRotate(angle, sideSize/2, sideSize/2);
                pathHatch.transform(matrixHatch);
                p.setStrokeWidth(SW_MEDIUM);
                canvas.drawPath(pathHatch, p);

                // Рисуем цифру давления в PSI
                // относительно x y будем крутить цифры чтобы выровнять их по вертикали
                float radius = sideSize / 2 - PADDING * SCALE - 2 * BIG_HATCH_SIZE;
                int shift = i/3 + 20;
                float x = (float) Math.cos(Math.toRadians(angleDigitRotate)) * (radius) + sideSize / 2 - shift;
                float y = (float) Math.sin(Math.toRadians(angleDigitRotate)) * (radius) + sideSize / 2 + shift;
                p.setStrokeWidth(SW_SMALL);
                canvas.drawText(String.valueOf(i), x, y, p); // x,y - расчитанные координаты места где рисуем цифру давления
                angleDigitRotate += 3.1 * 10;
            }

            if(i == 0) angle = (float) 3.1 * 10; //Начиная с первой итерации цикла сдвигаемся на 31 градус
        }
    }

    public void drawArrow(Canvas canvas)
    {
        // draw the pressure gauge needle
        // path in which we draw the arrow, TODO Move from this method to the initialization method
        pathArrow.reset();
        pathArrow.moveTo(pointsArrow[0][0], pointsArrow[0][1]);
        for(int i = 1; i <= 5; i++)
        {
            pathArrow.lineTo(pointsArrow[i][0], pointsArrow[i][1]);
        }

        // Rotate arrow on specified angle
        matrixArrow.reset();
        matrixArrow.setRotate(arrowAngle, sideSize/2, sideSize/2);
        pathArrow.transform(matrixArrow);

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(SW_SMALL);
        canvas.drawPath(pathArrow, p);

        canvas.drawCircle(C_X, C_Y, C_RAD_BIG, p);
        p.setColor(Color.WHITE);
        canvas.drawCircle(C_X, C_Y, C_RAD_SMALL, p);
    }

    protected void onDraw(Canvas canvas) {
        drawBackGround(canvas);
        drawBarArc(canvas);
        drawPsiArc(canvas);
        drawArrow(canvas);
    }

    /**
     *  Получает в качестве параметра угол отклонения стрелки  в градусах и сохраняет его в свойство объекта
     *  Так же вычесляет давление соответствующее углу и сохраняет его в соответствующее  свойство объекта TODO вот эту хрень надо поправить, неправильно это в одном методе делать
     *  TODO -135 135 только при рисовании надо оставить хранить углы только в отклонениях стрелки от  нулевого значения на шкале
     *
     * @param arrowAngle   реальный угол отклонения стрелки от 0 до 270
     */
    public void setArrowAngle(float arrowAngle)
    {
        //Вставим тут проверку заданного угла, если угол уходит за границы опустим на ноль или зашкалим манометр on 270 degree
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

    /**
     *  Задает манометру значение давления в bar или PSI.
     *  Давление данное в качестве параметра заносится в свойство объекта манометр.
     *
     *
     * @param pressure значение давления
     * @param inPsi true если давление  в PSI, false если давление в bar
     */
    public void setPressure(float pressure, boolean inPsi)
    {
        if(!inPsi)
        {
            pressure = (float) (pressure * 14.5038);
        }

        if(pressure < 0)
        {
            pressure = 0;
        }
        else if(pressure > 6 * 14.5038)
        {
            pressure = (float) (6 * 14.5038);
        }

        this.pressurePsi = pressure;

        //пересчитываем в градусы исходя из пропорции 0 - 0, 6 - 270
        setArrowAngle((float)(pressure*270/6/14.5038));
        invalidate();
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

        setArrowAngle( Utils.angleByTouchCoordinates(x, y, cX, cY) );
        mainActivity.updateTextFields( this.pressurePsi );
        invalidate();

        return true;
    }
}