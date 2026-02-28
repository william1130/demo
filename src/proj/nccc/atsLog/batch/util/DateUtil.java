package proj.nccc.atsLog.batch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
			
	public static String dateToString(Date date){
		//目前時間
//		Date date = new Date();
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//進行轉換
		String dateString = sdf.format(date);
		return  dateString ; 
	}
	
	public static String dateToString(Date date, String format){
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		//進行轉換
		String dateString = sdf.format(date);
		return  dateString ; 
	}
	
	public static Date stringToDate(String dateString){
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//進行轉換
		Date date;
		try {
			date = sdf.parse(dateString);
			return date ; 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ; 
		}
//		System.out.println(date);
	}
	
	public static Date stringToDate(String dateString ,String format){
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		//進行轉換
		Date date;
		try {
			date = sdf.parse(dateString);
			return date ; 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ; 
		}
	}

	public static boolean checkIfCrossDay(String compareDateStr){
		if(!compareDateStr.equals(DateUtil.dateToString(new Date()))){
			return true ; 
		}
		return false ;
	}
	
	
	/**
	 * true: sendTimestamp + offsetTime) > timestamp
	 * offsetTime : ESunConfig.getOtpValidTime()
	 * @param strDate
	 * @param offsetTime
	 * @return
	 */
	public static boolean checkIfValid(String strDate, int offsetTime){
		//u_time  //20161117 162019 
		//yyyyMMddhmmss
 
		Date currentDate = new Date();
		long timestamp = currentDate.getTime() / 1000;
		
		Date sendDate = DateUtil.stringToDate(strDate, "yyyyMMddHHmmss") ; 
		long sendTimestamp =  sendDate.getTime() /1000 ; 
		
		if( (sendTimestamp + offsetTime) > timestamp ){
			return true ; 
		}
		return false ;
	}
	
	/*
	 * - 取得該月月底
	 * return : yyyymmdd
	 */
	public static String getMonthEnd(String yyyymm) {
		String strZ = null;
		boolean leap = false;
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
			leap = DateUtil.leapYear(x);
			if (leap) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		return yyyymm + strZ;
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
	
	/**
	 * 西元yyyymm 轉民國 yyy/mm
	 * @param yyyymm
	 * @param slash : include "/"
	 * @return
	 */
	public static String chineseYearMonth(String yyyymm, boolean slash) {
		return "" + (Integer.parseInt(yyyymm.substring(0, 4)) - 1911)
				+ (slash ? "/":"" )+ yyyymm.substring(4);
		
	}
	
	/**
	 * date 轉民國年
	 * @param date
	 * @param slash : include "/"
	 * @return
	 */
	public static String ChineseYYYMMDD(Date date, boolean slash) {
		
		// 0123456789
		// yyyy-MM-dd
		String d = dateToString(date);
		return "" + (Integer.parseInt(d.substring(0, 4)) - 1911)
		+ (slash ? "/":"" ) + d.substring(5, 7)
		+ (slash ? "/":"" ) + d.substring(8);
		
	}

}
