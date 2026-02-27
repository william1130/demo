/*
 * MyDateUtil.java
 *
 * Created on 2008年5月9日, 下午 3:07
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.beans;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Stephen Lin
 * @versionType $Revision$
 */
public class MyDateUtil {

	public static final DateTimeFormatter FULL_DATETIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public static final DateTimeFormatter FULL_DATETIME_2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	public static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
	public static final DateTimeFormatter HHMMSS = DateTimeFormatter.ofPattern("HHmmss");

	public MyDateUtil() {

	}

	public Date toDateYYYYMMDD(String yyyymmdd) {
		// log.info("sDate="+yyyymmdd);
		try {
			int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
			int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
			int dd = Integer.parseInt(yyyymmdd.substring(6, 8));
			if (mm > 12 || mm <= 0 || dd > 31 || dd <= 0)
				return null;
			Calendar cal = Calendar.getInstance();
			cal.set(yyyy, mm - 1, dd, 0, 0, 0);
			// log.info("sDate="+cal.getTime());
			return cal.getTime();
		} catch (Exception cc) {
			// log.info("myDate convert exception :["+yyyymmdd+"]"+cc);
			return null;
		}
	}

	public Date getDateBeforeAfterByDay(Date currentDate, int iDay, String sType) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		if (sType.equals("AFTER"))
			cal.add(Calendar.DATE, iDay);
		else if (sType.equals("BEFORE"))
			cal.add(Calendar.DATE, iDay * -1);
		return cal.getTime();
	}

	/**
	 * Format Date and Time
	 *
	 * @param date
	 * @param formatter
	 * 
	 * @return String
	 */
	public static String toString(Date date, DateTimeFormatter formatter) {
		if (date == null || formatter == null) {
			return null;
		}
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(formatter);
	}

	/**
	 * Get System Date and Time
	 * 
	 * @return String
	 */
	public static String getSysDateTime(DateTimeFormatter formatter) {
		if (formatter == null) {
			return null;
		}
		return toString(new Date(), formatter);
	}
}