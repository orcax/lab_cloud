package com.prj.util;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("deprecation")
public class DateUtils {

    /**
     * 转换Long为日期
     * @param ldate
     * @return
     */
    public static Date getDateFromLong(Long ldate) {
        if (ldate != null) {
            return new Date(ldate);
        }
        return null;
    }

	/**
	 * 返回时段：上午、下午、晚上
	 * */
	public static String getTimePeriod(Date dt)
	{
		if (dt == null) {
			return "";
		}
		String ret = new String();
		
		int hour = dt.getHours();
		
		if(hour < 12) {
			ret = "上午";
		} else if(hour < 18) {
			ret = "下午";
		} else {
			ret = "晚上";
		}
		
		return ret;
	}
	

	/**
	 * 将字符串 "YY-MM-DD"和"hh-mm-ss/hh:mm:ss" 转换为日期
	 * */
	public static Date getDateFromString(String strDate, String strTime)
	{
		Calendar cal = Calendar.getInstance();
		
		String[] arrDate = strDate.split("-");
		String[] arrTime = strTime.split("[:-]");
		
		cal.set(Integer.parseInt(arrDate[0]), Integer.parseInt(arrDate[1])-1, Integer.parseInt(arrDate[2]), 
				Integer.parseInt(arrTime[0]), Integer.parseInt(arrTime[1]), Integer.parseInt(arrTime[1]));
		
		return cal.getTime();
	}
	
	/**
	 * 将字符串 "YY-MM-DD hh-mm-ss/hh:mm:ss" 转换为日期
	 * */
	public static Date getDateTimeFromString(String date)
	{
		String[] strs = date.split(" ");
		return getDateFromString(strs[0], strs[1]);
	}
	
//	/**
//	 * 将字符串 "YY-MM-DD hh:mm:ss" 转换为日期
//	 * */
//	public static Date getDateFormString(String dateString) {
//		Date ret = null;
//		
//		String temp[] = dateString.split("[ ]");
//		ret = DateUtils.getDateFormString(temp[0], temp[1]);
//		
//		return ret;
//	}

	
	/**
	 * 格式化时间，返回 YYYY-MM-DD hh:mm:ss
	 * */
	public static String formatDateTime(Date dt)
	{
		if (dt == null) {
			return "";
		}
		String ret = new String();
		
		ret = formatDate(dt) + " " + formatTime(dt);
		
		return ret;
	}

	/**
	 * 格式化时间，返回 hh:mm:ss
	 * */
	public static String formatTime(Date dt)
	{
		if (dt == null) {
			return "";
		}
		String ret = new String();
		
		ret = String.valueOf(dt.getHours()) + ":"
		+ String.valueOf(dt.getMinutes()) + ":"
		+ String.valueOf(dt.getSeconds());
		
		return ret;
	}
	
	/**
	 * 格式化日期，返回 YYYY-MM-DD
	 * */
	public static String formatDate(Date dt)
	{
		if (dt == null) {
			return "";
		}
		String ret = new String();
		
		ret = String.valueOf(dt.getYear()+1900) + "-"
		+ String.valueOf(dt.getMonth()+1) + "-"
		+ String.valueOf(dt.getDate());
		
		return ret;
	}
	
	public static String getCurrentDateString() {
		return getDateString(Calendar.getInstance());
	}
	
	public static String getDateString(Calendar cal) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(cal.get(Calendar.YEAR));
		buffer.append('-');
		buffer.append(cal.get(Calendar.MONTH) + 1);
		buffer.append('-');
		buffer.append(cal.get(Calendar.DAY_OF_MONTH));
		return buffer.toString();
	}
	
	/**
	 * 将字符串 "YY-MM-DD" 转换为日期
	 * */
	public static Date getDateFromString(String dateString) {
		Date ret = null;
		
		String[] tmp = dateString.split("-");
		if (tmp.length == 3) {
			Calendar c = Calendar.getInstance();
			c.set(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])-1, Integer.parseInt(tmp[2]),
					0,0,0);
			ret = c.getTime();
		}
		
		return ret;
	}
	
	public static long timeStr2Long(String str) {
		String[] numStrs = str.split(":");
		int h,m,s;
		h = Integer.valueOf(numStrs[0]);
		m = Integer.valueOf(numStrs[1]);
		s = Integer.valueOf(numStrs[2]);
		return (h*3600+m*60+s)*1000;
	}
	
	public static long getCurrentDateLong() {
		long l = new Date().getTime();
		return l-l%86400000-28800000;
	}
}
