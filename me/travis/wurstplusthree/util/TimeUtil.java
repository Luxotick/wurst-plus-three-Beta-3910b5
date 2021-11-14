/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util;

import java.util.Calendar;
import me.travis.wurstplusthree.util.Globals;

public class TimeUtil
implements Globals {
    public static int get_hour() {
        return Calendar.getInstance().get(11);
    }

    public static int get_day() {
        return Calendar.getInstance().get(5);
    }

    public static int get_month() {
        return Calendar.getInstance().get(2);
    }

    public static int get_year() {
        return Calendar.getInstance().get(1);
    }

    public static int get_minuite() {
        return Calendar.getInstance().get(12);
    }

    public static int get_second() {
        return Calendar.getInstance().get(13);
    }
}

