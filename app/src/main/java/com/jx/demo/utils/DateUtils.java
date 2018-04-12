package com.jx.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String dateFormatYMD = "yyyy-MM-dd";
    public static final String dateFormatHMS = "HH:mm:ss";

    /*
     * 将年月日转换为时间戳
     */
    public static long dateToStamp(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTimeInMillis();
    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        long ts = 0;
        try {
            Date date = simpleDateFormat.parse(s);
            ts = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ts;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long lt, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }
}
