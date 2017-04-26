package com.itpoints.njmetro.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.text.TextUtils;

/**
 * 时间工具类
 * 
 * @author peidongxu
 * 
 */
public class TimeUtils {

	/**
	 * 获取当前时间字符串
	 * 
	 * @param yyyy
	 *            -MM-dd HH:mm:ss 获取的当前的时间的格式
	 * @return
	 */
	public static String getCurrertData(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String formatStr = sdf.format(new Date());
		return formatStr;
	}

	/**
	 * 字符串转成时间戳
	 * 
	 * @param 格式yyyy
	 *            -MM-dd HH:mm:ss
	 * @param 字符
	 */
	public static long getDataUnix(String fotmat, String str) {
		long date = 0;
		if (str != null && !TextUtils.isEmpty(str)) {
			try {
				date = new java.text.SimpleDateFormat(fotmat).parse(str).getTime() / 1000;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return date;
	}

	/**
	 * 时间戳转换成字符窜
	 * 
	 * @param yyyy
	 *            -MM-dd HH:mm:ss
	 * @param 时间戳
	 */
	public static String getData(String fotmat, String l) {
		return new java.text.SimpleDateFormat(fotmat).format(new java.util.Date(Long.parseLong(l) * 1000));
	}

	/**
	 * 获取当前星期
	 * 
	 * @return
	 */
	public static String getCurrertWeek() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		return String.valueOf(c.get(Calendar.DAY_OF_WEEK) - 1);
	}

	/**
	 * 获取两个时间相差的分数
	 * 
	 * @param beginTime
	 *            开始的时间
	 * @param endTime
	 *            结束的时间
	 * @return 时间天数
	 */
	public static int getDateMillisecond(String beginTime, String endTime) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");// 输入日期的格式
		Date date1 = null;
		try {
			date1 = simpleDateFormat.parse(beginTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2 = null;
		try {
			date2 = simpleDateFormat.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);

		int day = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000 / 60);
		return day;
	}

	/**
	 * 获取两个时间相差的天数
	 * 
	 * @param beginTime
	 *            开始的时间
	 * @param endTime
	 *            结束的时间
	 * @return 时间天数
	 */
	public static String getDateAfter(String beginTime, long millisecond) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");// 输入日期的格式
		Date date = null;
		try {
			date = simpleDateFormat.parse(beginTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		c.add(Calendar.MINUTE, (int) millisecond);
		Date time = c.getTime();

		return converToString("HH:mm", time);
	}

	/**
	 * 时间戳转换成字符窜
	 * 
	 * @param yyyy
	 *            -MM-dd HH:mm:ss
	 * @param 时间戳
	 */
	public static String timeStampToStr(String fotmat, String l) {
		return new java.text.SimpleDateFormat(fotmat).format(new java.util.Date(Long.parseLong(l) * 1000));
	}

	// 把日期转为字符串
	public static String converToString(String format, Date date) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	// 格式化字符串日期
	public static String getFotmatData(String format, String strDate) {
		return getData("HH:mm", String.valueOf(getDataUnix(format, strDate)));
	}

	/**
	 * 格式化字符串日期
	 * 
	 * @param format_bef
	 *            格式化之前格式
	 * @param format_end
	 *            格式化之后格式
	 * @param strDate
	 *            格式的日期
	 * @return
	 */
	public static String getFotmatData(String format_bef, String format_end, String strDate) {
		return getData(format_end, String.valueOf(getDataUnix(format_bef, strDate)));
	}

}
