package proj.nccc.atsLog.batch.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Util {

	// 驗證字符串是否是數字
	public boolean isNumeric(String str) {

		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return new SimpleDateFormat("yyyyMMdd ").format(cal.getTime());
	}

	public static String getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		return new SimpleDateFormat("yyyyMMdd ").format(cal.getTime());
	}

	/**
	 * 將傳入之姓名保留第一個字, 後面加上'先生/小姐' 若傳入之值為null則傳回null
	 * 
	 * @param name
	 * @return
	 */
	public static String hideName(String name) {
		if (name != null) {
			return name.substring(0, 1) + " 先生/小姐";
		}
		return name;
	}

	/**
	 * 以byte的方式取得子字串, 若傳入之字串為空(null或空字串), 則傳回原字串, 若byte長度不足startByte所指之長度,
	 * 則傳回空字串("").
	 * 
	 * @param str
	 * @param startByte
	 * @param endByte
	 * @return
	 */
	public static String substringByByte(String str, int startByte, int endByte) {
		if (StringUtils.isBlank(str))
			return str;
		byte[] b = str.getBytes();
		if (startByte > b.length)
			return "";
		if (endByte > b.length)
			endByte = b.length;
		int len = endByte - startByte;
		String s1 = new String(b, startByte, len);
		return s1;
	}

	/**
	 * 遮罩卡號, 僅留前6後4碼為名碼, 中間6碼顯示 *.
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String maskCardNo(String cardNo) {
		if (cardNo != null) {
			StringBuffer sb = new StringBuffer();
			if (cardNo.length() >= 6) {
				sb.append(cardNo.substring(0, 6));
				sb.append("******");
				if (cardNo.length() > 12) {
					sb.append(cardNo.substring(12));
				}
			} else
				sb.append(cardNo);

			return sb.toString();
		}

		return cardNo;
	}

	/**
	 * 遮罩序號, 僅留前2後3碼為名碼, 中間6碼顯示 *.
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String maskCouponNo(String sn) {
		if (sn != null) {
			StringBuffer sb = new StringBuffer();
			int t = sn.length();
			if (sn.length() >= 5) {
				sb.append(sn.substring(0, 2));
				for (int j = 0; j < t - 5; j++)
					sb.append("*");
				sb.append(sn.substring(t - 3));
			} else
				sb.append(sn);
			return sb.toString();
		}
		return sn;
	}

	public static String filter0d0a(String s) {
		StringBuffer s2 = new StringBuffer();
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!(chars[i] == 13 || chars[i] == 10)) {
				s2.append(chars[i]);
			}
		}
		return s2.toString();
	}
	
	/**
	 * Email 是否合規
	 * @param email
	 * @return
	 */
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	/**
	 * -建立路徑白名單 : for > Path Manipulation: Zip Entry Overwrite
	 */
	private final static Map<String, String> pathCharWhiteList = new HashMap<String, String>();
	static {
		pathCharWhiteList.clear();
		for(int i = 65; i <= 90 ; i++) {
			// -- A - Z
			pathCharWhiteList.put("" + (char) i, "" + (char) i);
		}
		for(int i = 97; i <= 122 ; i++) {
			// -- a - z
			pathCharWhiteList.put("" + (char) i, "" + (char) i);
		}
		for(int i = 48; i <= 57 ; i++) {
			// -- 0 - 9
			pathCharWhiteList.put("" + (char) i, "" + (char) i);
		}
		pathCharWhiteList.put("-", "-");
		pathCharWhiteList.put("_", "_");
		pathCharWhiteList.put("/", "/");
		pathCharWhiteList.put("\\", "\\");
	}
	/**
	 * - valid file path
	 * @param filePath
	 * @return
	 */
	public static String validFilePath(String filePath) {
		String temp = "";
		log.debug("filePath (before): " + filePath);
		for(int i = 0 ; i < filePath.length() ; i++) {
			Character curChar = null;
			Character nextChar = null;
			try {
				curChar = filePath.charAt(i);
				nextChar = filePath.charAt(i + 1);
			}catch(Exception x) {}
			if (pathCharWhiteList.get("" + filePath.charAt(i)) != null 
					&& (curChar == '\\' || curChar == '/')) {
				temp += pathCharWhiteList.get("" + filePath.charAt(i));
			}else if (pathCharWhiteList.get("" + filePath.charAt(i)) != null 
					&& curChar != '.') {
				temp += pathCharWhiteList.get("" + filePath.charAt(i));
			}else if (pathCharWhiteList.get("" + filePath.charAt(i)) != null 
					&& curChar == '.' && nextChar != '.') {
				temp += pathCharWhiteList.get("" + filePath.charAt(i));
			}
		}
		log.debug("filePath (after): " + filePath);
		filePath = temp;
		return filePath;
	}
	
	/**
	 * 收費檔檔名
	 * @param currentDd
	 * @param includeZip : 是否含 .zip
	 * @return
	 */
	public static String getSettleFileName(String currentDd, boolean includeZip) {
//		String s = DateUtil.dateToString(new Date(), "yyMMdd");
		String s = "I9561088" + currentDd +"F.001";
		return includeZip ? s + ".zip" : s; 
	}
	

}
