package com.sudaotech.chatlibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Samuel on 2017/2/23 15:27.
 * Email:xuzhou40@gmail.com
 * Description:日期工具类
 */

public class DateUtil {
    /**
     * 格式化日期
     * 当天显示时间
     * 一周内显示星期
     * 一周外显示具体年月日
     *
     * @param time
     * @return
     */
    public static String getFormatDate(long time) {
        SimpleDateFormat simpleDateFormat;
        long intervalDays = getIntervalDays(time, new Date().getTime());
        if (intervalDays == 0) {
            simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else if (intervalDays > 0 && intervalDays < 7) {
            simpleDateFormat = new SimpleDateFormat("E", Locale.getDefault());
        } else {
            simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd", Locale.getDefault());
        }
        Date date = new Date(time);
        return simpleDateFormat.format(date);

    }

    /**
     * 判断两个日期的间隔天数
     *
     * @param startTime 起始日期
     * @param endTime   结束日期
     * @return
     */
    public static long getIntervalDays(long startTime, long endTime) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(new Date(startTime));
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(new Date(endTime));
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (1000 * 60 * 60 * 24);
    }
}
