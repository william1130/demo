
package proj.nccc.atsLog.batch.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyDateUtil {

	private static final String[] MONTH_NAMES = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };

	// format to Date
	public static Date toDate(String yyyymmddhhmmss) {
		// System.out.println("sDate="+sDate);
		if (yyyymmddhhmmss != null && yyyymmddhhmmss.length() >= 14) {
			try {
				int yyyy = Integer.parseInt(yyyymmddhhmmss.substring(0, 4));
				int mm = Integer.parseInt(yyyymmddhhmmss.substring(4, 6));
				int dd = Integer.parseInt(yyyymmddhhmmss.substring(6, 8));
				int hh = Integer.parseInt(yyyymmddhhmmss.substring(8, 10));
				int MM = Integer.parseInt(yyyymmddhhmmss.substring(10, 12));
				int ss = Integer.parseInt(yyyymmddhhmmss.substring(12, 14));
				Calendar cal = Calendar.getInstance();
				cal.set(yyyy, mm - 1, dd, hh, MM, ss);
				return cal.getTime();
			} catch (Exception cc) {
				// System.out.println("myDate convert exception :["+yyyymmddhhmmss+"]"+cc);
				return null;
			}
		} else
			return null;
	}
	// format to Date
	public static Date toDate2(String yyyymmdd_hhmmss) {
		// System.out.println("sDate="+sDate);
		if (yyyymmdd_hhmmss != null && yyyymmdd_hhmmss.length() >= 15) {
			try {
				int yyyy = Integer.parseInt(yyyymmdd_hhmmss.substring(0, 4));
				int mm = Integer.parseInt(yyyymmdd_hhmmss.substring(4, 6));
				int dd = Integer.parseInt(yyyymmdd_hhmmss.substring(6, 8));
				int hh = Integer.parseInt(yyyymmdd_hhmmss.substring(9, 11));
				int MM = Integer.parseInt(yyyymmdd_hhmmss.substring(11, 13));
				int ss = Integer.parseInt(yyyymmdd_hhmmss.substring(13, 15));
				Calendar cal = Calendar.getInstance();
				cal.set(yyyy, mm - 1, dd, hh, MM, ss);
				return cal.getTime();
			} catch (Exception cc) {
				// System.out.println("myDate convert exception :["+yyyymmddhhmmss+"]"+cc);
				return null;
			}
		} else
			return null;
	}

	public static Date toDateYYYYMMDD(String yyyymmdd) {
		// System.out.println("sDate="+yyyymmdd);
		try {
			int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
			int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
			int dd = Integer.parseInt(yyyymmdd.substring(6, 8));
			if (mm > 12 || mm <= 0 || dd > 31 || dd <= 0)
				return null;
			Calendar cal = Calendar.getInstance();
			cal.set(yyyy, mm - 1, dd, 0, 0, 0);
			// System.out.println("sDate="+cal.getTime());
			return cal.getTime();
		} catch (Exception cc) {
			// System.out.println("myDate convert exception :["+yyyymmdd+"]"+cc);
			return null;
		}
	}

	//            012345678901234
	// format to [20080125205018]
	public static String toDateString(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		StringBuffer reportingDate = new StringBuffer();
		reportingDate.append("");
		reportingDate.append(cal.get(Calendar.YEAR));
		reportingDate.append("");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.MONTH) + 1), 2, "0"));
		reportingDate.append("");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.DAY_OF_MONTH)), 2, "0"));
		// reportingDate.append("-");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.HOUR_OF_DAY)), 2, "0"));
		reportingDate.append("");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.MINUTE)), 2, "0"));
		reportingDate.append("");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.SECOND)), 2, "0"));
		/*
		 reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
		 .get(Calendar.MILLISECOND)), 3, "0"));
		 */
		return reportingDate.toString();
	}

	// format to [2008/01/25 20:50:18]
	public static String toDateString2(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		StringBuffer reportingDate = new StringBuffer();
		reportingDate.append("");
		reportingDate.append(cal.get(Calendar.YEAR));
		reportingDate.append("/");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.MONTH) + 1), 2, "0"));
		reportingDate.append("/");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.DAY_OF_MONTH)), 2, "0"));
		reportingDate.append(" ");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.HOUR_OF_DAY)), 2, "0"));
		reportingDate.append(":");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.MINUTE)), 2, "0"));
		reportingDate.append(":");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.SECOND)), 2, "0"));
		return reportingDate.toString();
	}
	public static String toDateString3(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		StringBuffer reportingDate = new StringBuffer();
		reportingDate.append("");
		reportingDate.append(cal.get(Calendar.YEAR));
		reportingDate.append("/");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.MONTH) + 1), 2, "0"));
		reportingDate.append("/");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.DAY_OF_MONTH)), 2, "0"));
		return reportingDate.toString();
	}
	public static Date getDateBeforeAfterByDay(Date currentDate, int iDay, String sType) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		if (sType.equals("AFTER"))
			cal.add(Calendar.DATE, iDay);
		else if (sType.equals("BEFORE"))
			cal.add(Calendar.DATE, iDay * -1);
		return cal.getTime();
	}
	public static Date getDateBeforeAfterByMinutes(Date currentDate, int minutes, String sType) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		if (sType.equals("AFTER"))
			cal.add(Calendar.MINUTE, minutes);
		else if (sType.equals("BEFORE"))
			cal.add(Calendar.MINUTE, minutes * -1);
		return cal.getTime();
	}

	// format to [200801] YYYYMM
	public static String yyyymm(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		StringBuffer reportingDate = new StringBuffer();
		reportingDate.append("");
		reportingDate.append(cal.get(Calendar.YEAR));
		reportingDate.append("");
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.MONTH) + 1), 2, "0"));
		return reportingDate.toString();
	}

	// format to [201801] YYYYMM, 取得上個月的yyyymm
	public static String lastYyyymm(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		StringBuffer reportingDate = new StringBuffer();
		if (cal.get(Calendar.MONTH) == 0) {
			reportingDate.append(cal.get(Calendar.YEAR) - 1);
			reportingDate.append("12");
		} else {
			reportingDate.append(cal.get(Calendar.YEAR));
			reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
					.get(Calendar.MONTH)), 2, "0"));
		}
		return reportingDate.toString();
	}

	// format to [201801] YYYYMM, 取得下個月的yyyymm
	public static String nextYyyymm(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		StringBuffer reportingDate = new StringBuffer();
		if (cal.get(Calendar.MONTH) == 11) {
			// DECEMBER
			reportingDate.append(cal.get(Calendar.YEAR) + 1);
			reportingDate.append("01");
		} else {
			reportingDate.append(cal.get(Calendar.YEAR));
			reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
					.get(Calendar.MONTH) + 1 + 1 ), 2, "0"));
		}
		return reportingDate.toString();
	}
	
	// format to [201801] YYYYMM, 取得去年此月的yyyymm
	public static String lastYearYYYYMM(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		StringBuffer reportingDate = new StringBuffer();
		reportingDate.append(cal.get(Calendar.YEAR) - 1);
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.MONTH) + 1), 2, "0"));
		return reportingDate.toString();
	}

	/**
	 * format to [20190101] YYYYMMDD
	 * @param processDate
	 * @return
	 * @throws Exception 
	 */
	public static String yyyymmdd(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		StringBuffer reportingDate = new StringBuffer();
		reportingDate.append(cal.get(Calendar.YEAR));
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.MONTH) + 1), 2, "0"));
		reportingDate.append(MyStringUtils.leftPad(String.valueOf(cal
				.get(Calendar.DAY_OF_MONTH)), 2, "0"));
		return reportingDate.toString();
	}
	
	public static String mmdd(Date processDate) throws Exception {
		return yyyymmdd(processDate).substring(4, 8);
	}
	public static String dd(Date processDate) throws Exception {
		return yyyymmdd(processDate).substring(6);
	}
	
	public static String yymmdd(String yyyymmdd) {
		return yyyymmdd.substring(2);
	}
	public static String mmddyy(String yymmdd){
		return yymmdd.substring(2)+yymmdd.substring(0, 2);
	}
	public static String yddd(String yyyymmdd) throws Exception{
		Date d=MyDateUtil.toDateYYYYMMDD(yyyymmdd);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int ddd=cal.get(Calendar.DAY_OF_YEAR);
		return yyyymmdd.substring(3, 4)+MyStringUtils.leftPad(ddd+"", 3, "0");
	}

	// is a vaild date ?
	public static boolean isDate(String yyyymmdd) {
		if (yyyymmdd.length() == 6)
			yyyymmdd += "01";
		if (MyDateUtil.toDateYYYYMMDD(yyyymmdd) == null)
			return false;
		else
			return true;
	}

	public static int toChineseYear(String yyyymmdd) {
		String s = yyyymmdd.substring(0, 4);

		int i = Integer.parseInt(s);
		return (i - 1911);
	}
	
	public static int toYear(String yyyymmdd) {
		String s = "0";
		if (yyyymmdd.length() >= 4)
			s = yyyymmdd.substring(0, 4);
		int i = Integer.parseInt(s);
		return i;
	}

	public static int toMonth(String yyyymmdd) {
		String s = "";
		if (yyyymmdd.length() >= 8)
			s = yyyymmdd.substring(4, 6);
		else if (yyyymmdd.length() == 6)
			s = yyyymmdd.substring(4);
		int i = Integer.parseInt(s);
		return i;
	}

	public static int toDay(String yyyymmdd) {
		String s = "";
		if (yyyymmdd.length() >= 8)
			s = yyyymmdd.substring(6, 8);
		int i = Integer.parseInt(s);
		return i;
	}
	public static Date getDateBeforeAfterByMonth(Date currentDate, int iMonth, String sType) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		if (sType.equals("AFTER"))
			cal.add(Calendar.MONTH, iMonth);
		else if (sType.equals("BEFORE"))
			cal.add(Calendar.MONTH, iMonth * -1);
		return cal.getTime();
	}

	/**
	 * 取得月份相差數 yyyymm2 - yyyymm1
	 * 
	 * @param yyyymm1
	 * @param yyyymm2
	 * @return
	 */
	public static int diffMonth(String yyyymm1, String yyyymm2) {

		try {

			int year1 = Integer.parseInt(yyyymm1.substring(0, 4));
			int year2 = Integer.parseInt(yyyymm2.substring(0, 4));
			int month1 = Integer.parseInt(yyyymm1.substring(4, 6));
			int month2 = Integer.parseInt(yyyymm2.substring(4, 6));

			return Math.abs((year2 * 12 + month2) - (year1 * 12 + month1)) + 1;
		} catch (Exception e) {

			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * @param yyyymm
	 * @return
	 */
	public static Date toDateYYYYMM(String yyyymm) {

		try {

			int yyyy = Integer.parseInt(yyyymm.substring(0, 4));
			int mm = Integer.parseInt(yyyymm.substring(4, 6));
			int dd = 1;

			if (mm > 12 || mm <= 0)
				return null;
			Calendar cal = Calendar.getInstance();
			cal.set(yyyy, mm - 1, dd, 0, 0, 0);

			return cal.getTime();
		} catch (Exception cc) {

			return null;
		}
	}

	public static String lastyyyymm(String fileDate) {
		// 0123456
		// yyyymm
		String yyyy = fileDate.substring(0, 4);
		String mm = fileDate.substring(4);
		if (fileDate.length() == 8) {
			mm = fileDate.substring(4, 6);
		}
		int iyyyy = 0;
		int imm = 0;
		try {
			iyyyy = Integer.parseInt(yyyy);
		} catch (Exception x) {
			return null;
		}
		try {
			imm = Integer.parseInt(mm);
		} catch (Exception x) {
			return null;
		}
		StringBuffer reportingDate = new StringBuffer();
		if (imm == 1) {
			reportingDate.append(iyyyy - 1);
			reportingDate.append("12");
		} else {
			imm--;
			reportingDate.append(yyyy);
			if (imm < 10)
				reportingDate.append("0" + imm);
			else
				reportingDate.append(imm);
		}
		return reportingDate.toString();
	}

	public static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0)
					leap = true;
				else
					leap = false;
			} else
				leap = true;
		} else
			leap = false;
		return leap;
	}

	/*
	 * 取得該月月底
	 * return : yyyymmdd
	 */
	public static String getMonthEndYYYYMMDD(String yyyymm) {
		String strZ = null;
		boolean leap = false;
		// 201010
		// 012345
		int x = Integer.parseInt(yyyymm.substring(0, 4));
		int y = Integer.parseInt(yyyymm.substring(4));
		if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10
				|| y == 12) {
			strZ = "31";
		}
		if (y == 4 || y == 6 || y == 9 || y == 11) {
			strZ = "30";
		}
		if (y == 2) {
			leap = leapYear(x);
			if (leap) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		return yyyymm + strZ;
	}

	public static String getEngMonth(int month) {

		int month1 = month - 1;

		if (month1 < 0 || month1 >= 12)
			return "";

		return MONTH_NAMES[month1];
	}
	   /**
     * 取得 currentDate的之前/後,的timeUnit (日/時/分)的n 
     * @param currentDate
     * @param value
     * @param beforeAfter
     * @param timeUnit
     * @return
     */
    public static Date nextTime(Date currentDate, int value,String beforeAfter,String timeUnit ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        if("BEFORE".equals(beforeAfter)) {
        	value=value*-1;
        }
        if("MONTH".equals(timeUnit)) {
            cal.add(Calendar.MONTH, value);
        }else if("DAY".equals(timeUnit)) {
            cal.add(Calendar.DATE, value);
        }else if("HOUR".equals(timeUnit)) {
            cal.add(Calendar.HOUR, value);
        }else if("MINUTE".equals(timeUnit)) {
            cal.add(Calendar.MINUTE, value);
        }
        
        return cal.getTime();
    }
	public static boolean isInRange(Date now,Date dateStart,Date dateEnd){
		return now.compareTo(dateStart)>=0 && now.compareTo(dateEnd)<=0;
	}
	
	public static long dateDiff(Date date1, Date date2, String itemType){
		long milliseconds1 = date1.getTime();
		long milliseconds2 = date2.getTime();
		long diff = milliseconds2 - milliseconds1;
		if("DAY".equals(itemType)||itemType==null)
			return  diff / (24 * 60 * 60 * 1000);
		else if("HOURS".equals(itemType))
			return diff / (60 * 60 * 1000);
		else if("MINUTES".equals(itemType))
			return diff / (60 * 1000);
		else if("SECONDS".equals(itemType))
			return diff / 1000;
		else 
			return diff ;
	}
	
	/**
	 * long time millis to String as format
	 * @param timeMillis
	 * @param format
	 * @return
	 */
	public static String formatToDateString(long timeMillis, String format) {
		if (format == null) {
			format = "yyyy/MM/dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
		Date transTime = new Date(timeMillis);
		return sdf.format(transTime);
	}
	
	
	public static void main(String[] args) {
		System.out.println(new Date());
		System.out.println(MyDateUtil.nextTime(new Date(), 13, "BEFORE", "MONTH"));
		System.out.println(MyDateUtil.nextTime(new Date(), 365, "AFTER", "DAY"));
		
	}
}