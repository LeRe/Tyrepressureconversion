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

}
