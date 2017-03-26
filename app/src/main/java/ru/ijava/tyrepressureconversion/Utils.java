package ru.ijava.tyrepressureconversion;

/**
 *  Util class
 */

class Utils {

    static float bar2psi(float bar)
    {
        return (float) (bar * 14.50377);
    }

    static float psi2bar(float psi)
    {
        return (float) (psi * 0.06894757);
    }


    /**
     * Высчитывает угол положения стрелки манометра. Угол вычисляем в градусах 0 - 270
     * Перевод в -135 - 135 пусть выполнится в классе манометр. TODO -135 135 только при рисовании надо оставить хранить углы только в отклонениях стрелки от  нулевого значения на шкале
     *
     * @param x координата нажатия
     * @param y координата нажатия
     * @param cx координата центра относительно которого вращается стрелка
     * @param cy координата центра относительно которого вращается стрелка
     * @return угол положения стрелки манометра
     */
    static float angleByTouchCoordinates(float x, float y, float cx, float cy)
    {
        //angle гуляет от -135  - 0 бар
        // вертикально стоит == 0 градусов
        // и до 135 градусов, что соответствует углу 270 прибора или 6 бар
        float angle;
        final float zeroAngle = 0;

        if( x > cx && y < cy)
        {
            angle = (float) (135 + 180/Math.PI * Math.atan((x - cx)/(cy - y)));
            //angle = (float) (180/Math.PI * Math.atan((x - cx)/(cy - y)));
        }
        else if( x > cx && y > cy)
        {
            angle = (float) (135 + 90 + 180/Math.PI * Math.atan((y - cy)/(x - cx)));
            //angle = (float) (90 + 180/Math.PI * Math.atan((y - cy)/(x - cx)));
        }
        else if( x < cx && y > cy)
        {
            angle = (float) (135 -90 - 180/Math.PI * Math.atan((y - cy)/(cx - x)));
            //angle = (float) ( - 90 - 180/Math.PI * Math.atan((y - cy)/(cx - x)));
        }
        else if( x < cx && y < cy)
        {
            angle = (float) (135 - 180/Math.PI * Math.atan((cx - x)/(cy - y)));
            //angle = (float) ( - 180/Math.PI * Math.atan((cx - x)/(cy - y)));
        }
        else if(x == cx && y == cy)
        {
            angle = zeroAngle;
        }
        else if(x == cx && y < cy)
        {
            angle = 135;
        }
        else if(x > cx && y == cy)
        {
            angle = 135 + 90;
        }
        else if(x == cx && y > cy)
        {
            angle = 135 + 90 + 90;
        }
        else if(x < cx && y == cy)
        {
            angle = 135 - 90;
        }
        else
        {
            angle = 0;
        }

        return angle;
    }
}
