package cn.cmy.sample.customvideoviewdemo;

import java.util.Formatter;
import java.util.Locale;

public class TimerUtil {

    public static String updateTimes(int millisecond) {
        if (millisecond <= 0 || millisecond >= 24 * 3600 * 1000) {
            return "00:00";
        }
        int totalSecond = millisecond / 1000;
        int hh = totalSecond / 3600;
        int mm = totalSecond / 60 % 60;
        int ss = totalSecond % 60;
        StringBuilder str = new StringBuilder();
        Formatter formatter = new Formatter(str, Locale.getDefault());
        if (hh > 0) {
            return formatter.format("%02d:%02d:%02d", hh, mm, ss).toString();
        } else {
            return formatter.format("%02d:%02d",mm,ss).toString();
        }
    }

}
