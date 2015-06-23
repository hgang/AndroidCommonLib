package priv.heg.lib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class TimeHelper {
	public static SimpleDateFormat formatter_a = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	public static SimpleDateFormat formatter_b = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public static SimpleDateFormat formatter_c = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat formatter_d = new SimpleDateFormat("MM-dd");
	public static SimpleDateFormat formatter_e = new SimpleDateFormat(
			"yyyy��MM��dd�� HH:mm");
	public static SimpleDateFormat formatter_f = new SimpleDateFormat("MM��dd��");
	public static SimpleDateFormat formatter_g = new SimpleDateFormat(
			"HH:mm:ss");
	public static SimpleDateFormat formatter_h = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat formatter_day = new SimpleDateFormat(
			"yyyy_MM_dd HH:mm:ss");

	
	public static long getTimeOffsetMonth(long timestamp) {
		return timestamp - 30*24*60*60*1000L;
	} 
	public static long getTimeOffsetWeek(long timestamp) {
		return timestamp - 7*24*60*60*1000L;
	} 
	public static String getDate(long timestamp) {
		String date = formatter_a.format(new java.util.Date(timestamp));
		return date;
	} 
	public static String getDate_(long timestamp) {
		String date = formatter_day.format(new java.util.Date(timestamp));
		return date;
	} 
	public static int getDay(long timestamp) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(timestamp);
		return ca.get(Calendar.DAY_OF_MONTH);
	} 
	
	public static String getTodayString() {
		return formatter_c.format(new Date());
	}
	public static String getMonth(){
		return formatter_f.format(new Date());
	}
	public static String getTomorrowString() {
		Date d = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return formatter_c.format(c.getTime());
	}

	public static long[] getTodaySection() {
		long[] times = new long[3];
		Date d = new Date();
		times[0] = d.getTime();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		times[1] = c.getTimeInMillis();
		times[2] = times[1] + 24 * 3600 * 1000;
		return times;
	}

	public static String getTodayTime() {
		return formatter_a.format(new Date());
	}
	public static String getDetailTime(String time){
		return formatter_e.format(new Date(Long.parseLong(time)));
	}

	public static String getNowTime() {
		return formatter_h.format(new Date());
	}

	public static String getAMPM() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
	}

	public static String getHourTime(String start, String duration) {
		long time = Long.parseLong(start) + Long.parseLong(duration);
		return formatter_h.format(new Date(time));
	}

	public static String getEventTime(String start, String duration) {
		long s = Long.parseLong(start);
		long e = Long.parseLong(start) + Long.parseLong(duration);
		return formatter_h.format(new Date(s)) + " - "
				+ formatter_h.format(new Date(e));
	}

	public static String getEventTime(long s, long d) {
		return formatter_h.format(new Date(s)) + " - "
				+ formatter_h.format(new Date(s+d));
	}

	public static String getHourTime(long start, long duration) {
		long time = start + duration;
		return formatter_h.format(new Date(time));
	}

	public static String getHourTime(String start) {
		long time = Long.parseLong(start);
		return formatter_h.format(new Date(time));
	}

	public static String getTimeShiftTimeString(Date date) {
		Date currentTime;
		if (date == null) {
			currentTime = new Date();
		} else {
			currentTime = date;
		}
		return formatter_b.format(currentTime);
	}

	public static String getTimeShiftTimeString(String start, String duration) {
		try {
			Date d = formatter_b.parse(start);
			Date end = new Date(d.getTime() + Integer.parseInt(duration) * 1000);
			return getTimeShiftTimeString(end);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return getTimeShiftTimeString(null);
	}
}
