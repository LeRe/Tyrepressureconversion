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

    private float pressurePsi = 0;  // Текущее давление манометра

    private float arrowAngle = -135;   // Текущий угол отклонения стрелки манометра  для матрицы поворота изменяется от  -135 до 135, с поправкой для первоночального отклонения на ноль

    private float cX;   // Координата центра манометра по оси Х
    private float cY;   // Координата центра манометра по оси У

    private int sideSize;   // Размер стороны Виджета

    private final int startAngle = 135; // Стартовый угол ЧЕРТ ЗНАЕТ ДЛЯ ЧЕГО...
    private final int sweepAngle = 270; // Длина шкалы в градусах

    private final int padding = 100;  // отступ от физических границ фиджета до шкалы(элементов виджета)

    private Paint p;    //
    private RectF rectF;

    private Path pathHatch; // патч для штрихов
    private Path pathText; // патч для цифр  TODO Удалить не используется
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

        //TODO удалить нафик
        //setArrowAngle(0);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        //TODO delete next line
        //((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //Определяемся с длиной стороны виджета и координатами центра, относительно которых будем рисовать манометр
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

        // готовимся к рисованию
        p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setTextSize(60);

        // Для рисования дуги шкалы
        rectF = new RectF(padding, padding, sideSize - padding, sideSize - padding);

        //создаем патчи и преобразующие матрицы
        pathHatch = new Path();
        //pathText = new Path(); TODO удалить, не используется
        pathArrow = new Path();
        matrixHatch = new Matrix();
        matrixArrow = new Matrix();

        // будем обрабатывать касания
        this.setOnTouchListener(this);

        //TODO а нужна ли нам последующая строка? Похоже на наш функционал не влияет
        //setFocusable(true);
    }

    protected void onDraw(Canvas canvas) {

        // paint background
        //canvas.drawColor(Color.CYAN);

        // рисуем дугу баров
        p.setColor(Color.WHITE);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, p);

        //draw hatch рисуем штрихи
        int hatchSize = 30;
        pathHatch.reset();
        pathHatch.moveTo(sideSize/2, padding - hatchSize);
        pathHatch.lineTo(sideSize/2, padding);

        // Готовим патч для рисвания на нем текста
        // TODO удалить так как не используется
//        pathText.reset();
//        pathText.moveTo(sideSize/2, padding - hatchSize - hatchSize/4);
//        pathText.lineTo(sideSize/2+1, padding - hatchSize - hatchSize/4);

        // Расставляем цифры баров
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
            int radius = sideSize / 2 - padding + 2 * hatchSize;
            float x = (float) Math.cos(Math.toRadians(angleDigitRotate)) * (radius) + sideSize / 2;
            float y = (float) Math.sin(Math.toRadians(angleDigitRotate)) * (radius) + sideSize / 2;
            canvas.drawText(String.valueOf(i), x, y, p); // x,y - расчитанные координаты места где рисуем цифру давления
            angleDigitRotate += 45;
        }

        // draw the pressure gauge needle
        int centralRadius = 10;
        int shiftArrow = 40;
        p.setColor(Color.GREEN);

        // path in which we drawthe arrow, TODO Move from this method to the initialization method
        pathArrow.reset();
        pathArrow.addCircle(sideSize/2, sideSize/2, centralRadius, Path.Direction.CW);
        pathArrow.moveTo(sideSize/2, sideSize/2 + shiftArrow);
        pathArrow.lineTo(sideSize/2, padding + shiftArrow);

        // Rotate arrow on specified angle
        matrixArrow.reset();
        matrixArrow.setRotate(arrowAngle, sideSize/2, sideSize/2);
        pathArrow.transform(matrixArrow);

        canvas.drawPath(pathArrow, p);


        //TODO Убрать бардак, привести в порядок свойства и методы  класса, приблизить их к реалиям и логике
        //TODO создание path вынести из onDraw, в onDraw оставить только прорисовку paths

    }

    /**
     *  Получает в качестве параметра угол отклонения стрелки  в градусах и сохраняет его в свойство объекта
     *  Так же вычесляет давление соответствующее углу и сохраняет его в соответствующее  свойство объекта TODO вот эту хрень надо поправить, неправильно это в одном методе делать
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
     * Задает манометру значение давления в барах
     * давление после проверки ДОЛЖНО помещается в соответствующее свойство объекта TODO переделать!!! не пересчитывать в градусы, а сохранять давление в свойство
     *
     * @param bar давление в барах
     */
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

    /**
     * Задает манометру значение давления в psi
     * давление пересчитывается в бары уже существующим методом, TODO а надо бы наоборот, глядишь и точность через psi повысится
     *
     * @param psi давление в psi
     */
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


        setBarViaAngle(Utils.angleByTouchCoordinates(x, y, cX, cY));
        invalidate();

        return true;
    }

    /**
     *  Помещает угол в свойство манометра, вызывает метод главной активити для обновления давления в текстовых полях
     * @param angle угол охоже в градусах
     */
    private void setBarViaAngle(float angle) {
        setArrowAngle(angle);
        mainActivity.setPressure(this.pressurePsi);
    }
}           //TODO    NEXT STEP рисуем жизненный цикл программы: ввод пользователя - движение по методам - изменение свойств - изменение вида, проводим оптимизацию