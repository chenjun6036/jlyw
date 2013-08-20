package com.jlyw.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeFormatUtil {
	public static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 获取两个时间相差的天数的绝对值(>0)
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
		if (null == fDate || null == oDate) {
           return -1;
        }
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return Math.abs(day2 - day1);
    }
	/**
	 * 获取两个时间相差的天数的绝对值(>0)
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Timestamp fDate, Timestamp oDate) {
		if (null == fDate || null == oDate) {
           return -1;
        }
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return Math.abs(day2 - day1);
    }
	
}
