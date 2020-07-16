package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

	/**
	 * 计算自然周期季度
	 *
	 * @param date
	 * @return
	 */
	public static Integer getNatureQuaterNum(Date date) {
		return (getDayNum(date) + 2) / 3;
	}

	/**
	 * 计算非自然周期季度
	 *
	 * @param date
	 * @return
	 */
	public static Integer getQuaterNum(int i) {
		if (i == 12) {
			return 4;
		}
		return (i + 3) / 3;
	}

	/**
	 * 通过时间秒毫秒数判断两个时间的间隔
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetweenDate(Date date1, Date date2) {
		return Math.abs(getDayNum(date1) - getDayNum(date2));
	}

	/**
	 * 每月第一天加n-1天
	 *
	 * @param n
	 * @return
	 */
	public static Date addNDay(Date date, Integer n) {
		Calendar cal = Calendar.getInstance();
		Date date1 = getFirstDayOfMonth(date);
		cal.setTime(date1);
		cal.add(Calendar.DAY_OF_MONTH, n - 1);
		return cal.getTime();
	}

	/**
	 * 当前日期加一天
	 *
	 * @param date
	 * @return
	 */
	public static Date addOneDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 当前日期加n天
	 *
	 * @param date
	 * @return
	 */
	public static Date addNDays(Date date, Integer interalDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, interalDays);
		return cal.getTime();
	}

	/**
	 * 当前日期加一个月
	 *
	 * @param cycleType
	 * @param           date:上一周期的endDate
	 * @return
	 */

	public static Date addOneMonth(Integer cycleType, Date date, Integer endDay) {
		Integer intervalDays;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Integer monthNum = getMonthNum(date);
		if (endDay == null) {
			cal.add(Calendar.MONTH, 1);
			return cal.getTime();
		} else if (endDay <= getDayNum(getLastDayOfMonth(date)) && monthNum == 2) {
			cal.add(Calendar.MONTH, 1);
			return cal.getTime();
		} else if (endDay > getDayNum(getLastDayOfMonth(date)) && monthNum == 2) {
			cal.add(Calendar.MONTH, 1);
			intervalDays = endDay - getDayNum(cal.getTime());
			return addNDays(cal.getTime(), intervalDays);
		} else if (endDay <= getDayNum(getLastDayOfMonth(date)) && monthNum != 2) {
			cal.add(Calendar.MONTH, 1);
			return cal.getTime();
		} else {
			cal.add(Calendar.MONTH, 1);
			intervalDays = endDay - getDayNum(cal.getTime());
			return addNDays(cal.getTime(), intervalDays);
		}
	}

	/**
	 * 获取当前日期的月份数
	 *
	 * @param date
	 * @return
	 */
	public static Integer getMonthNum(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前日期的天数
	 *
	 * @param date
	 * @return
	 */
	public static Integer getDayNum(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取某年第一天日期
	 *
	 * @param year 年份
	 * @return Date
	 */
	public static Date getFirstDayOfYear(Integer year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取当月第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 获取当月最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return cal.getTime();
	}

	/**
	 * 比较两个日期的大小
	 *
	 * @param date1, date2
	 * @return
	 */
	public static Boolean compareDate(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);

		int result = c1.compareTo(c2);
		if (result < 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * 比较两个日期的大小
	 *
	 * @param date1, date2
	 * @return
	 */
	public static Boolean compareEqualDate(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);

		int result = c1.compareTo(c2);
		if (result == 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	// 获取某年最后一天
	public static Date getYearLastDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	/**
	 * localDate转date
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date localDate2Date(String dateStr) {
		// 定义格式
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		// 字符串格式转为LocalDate格式
		LocalDateTime parse = LocalDateTime.parse(dateStr, dateTimeFormatter);
		// 获取时间地区ID
		ZoneId zoneId = ZoneId.systemDefault();
		// 转换为当地时间
		ZonedDateTime zonedDateTime = parse.atZone(zoneId);
		// 转为Date类型
		Date date = Date.from(zonedDateTime.toInstant());
		return date;
	}

	/**
	 * 判断是否是闰年
	 *
	 * @param year
	 * @return
	 */
	public static boolean judgeLeapYear(int year) {
		boolean flag = false;
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			flag = true;
			return flag;
		} else {
			return flag;
		}
	}

	public static Date getNextDate(Date startDate, Integer endDay) {
		Date endDate = null;
		Integer startDay = getDayNum(startDate);
		Integer intervalDays;
		if (startDay < endDay) {
			if (getMonthNum(startDate) == 2) {
				Integer lastDayOfFebInteger = getDayNum(getLastDayOfMonth(startDate));
				if (endDay <= lastDayOfFebInteger) {
					intervalDays = endDay - startDay;
					endDate = addNDays(startDate, intervalDays);
				} else {
					endDate = addNDays(getLastDayOfMonth(startDate), 0);
				}
			} else {
				if (endDay > getDayNum(getLastDayOfMonth(startDate))) {
					intervalDays = getDayNum(getLastDayOfMonth(startDate)) - startDay;
				} else {
					intervalDays = endDay - startDay;
				}
				endDate = addNDays(startDate, intervalDays);
			}
		} else {
			endDate = addNDays(getLastDayOfMonth(startDate), endDay);
		}
		return endDate;
	}

	public static Date getStringToDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateT = null;
		try {
			// 注意格式需要与上面一致，不然会出现异常
			dateT = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateT;
	}

	/**
	 * 获取上个月的每一天
	 */
	public static List<String> getMonthFullDay(){
		List<String> fullDayList = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // 设置为当前时间
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int j = 0; j <= (count-1);) {
			calendar.add(Calendar.DAY_OF_MONTH, j == 0 ? +0 : +1);
			j++;
			fullDayList.add(sdf.format(calendar.getTime()));
		}
		return fullDayList;
	}
	/**
	 * 获取所传入时间全月的每一天
	 */
	public static List<String> getCurrentFullDay(String businessDate){
		List<String> fullDayList = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = new Date();
		try {
			 parse = format.parse(businessDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse); // 设置为当前时间
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int j = 0; j <= (count-1);) {
			calendar.add(Calendar.DAY_OF_MONTH, j == 0 ? +0 : +1);
			j++;
			fullDayList.add(sdf.format(calendar.getTime()));
		}
		return fullDayList;
	}
	//获取昨天日期
	public static String getYesterday(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date d=cal.getTime();
		SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
		return sp.format(d);//获取昨天日期
	}

	public static void main(String[] args) {
		List<String> monthFullDay = getMonthFullDay();

		System.out.println(getNextDate(localDate2Date("2019/06/11 00:00:00"), 20));
	}

}
