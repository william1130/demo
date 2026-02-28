
package proj.nccc.atsLog.batch.util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MyStringUtils {
	private final static String ENCODING = "MS950";

	/**
	 * 左靠右補字元 : 支持中文
	 * @param value
	 * @param maxLength
	 * @param padString
	 * @return
	 * @throws Exception 
	 */
	public static String rightPad(String value, int maxLength, String padString) {
		return MyStringUtils.paddingUtil(value, maxLength, padString, false);
	}	
	/**
	 * 右靠左補字元 : 支持中文
	 * @param value
	 * @param maxLength
	 * @param padString
	 * @return
	 * @throws Exception 
	 */
	public static String leftPad(String value, int maxLength, String padString) {
		return MyStringUtils.paddingUtil(value, maxLength, padString, true);
	}	
	
	/**
	 * 補字元
	 * @param value
	 * @param maxLength
	 * @param padString
	 * @param leftPadding
	 * @return
	 * @throws Exception 
	 */
	public static String paddingUtil(String value, int maxLength, String padString, boolean leftPadding) {
		String ret = "";
		
		try {
			if (StringUtils.isEmpty(value)) {
				return StringUtils.rightPad(padString, maxLength).substring(0, maxLength);
			}
			byte[] valueByte = value.getBytes(ENCODING);
			if (value.length() == valueByte.length) {
				// ----------------------------------
				// -- no chinese char
				if (leftPadding) {
					return StringUtils.leftPad(value, maxLength, padString);
				}else {
					return StringUtils.rightPad(value, maxLength, padString);
				}
			}
			if (valueByte.length == maxLength) {
				return value;
			}else if (valueByte.length > maxLength) {
				byte[] subByte = Arrays.copyOfRange(valueByte, 0, maxLength);
				return new String(subByte, ENCODING);
			}else {
				int startIdx = valueByte.length;
				if (!leftPadding) {
					// -------------------------
					// -- 左靠右補字元
					ret = value;
				}else {
					// -- 右靠
					ret = "";
				}
				for(int i = startIdx ; i < maxLength ; i ++) {
					ret += padString;
				}
				if (leftPadding) {
					// -- 右靠左補字元
					ret += value;
				}
			}
			
		}catch(Exception x) {
			log.error(x.getMessage(), x);
			ret = value;
		}
		
		return ret;
	}

	
	public static void main(String[] args) throws Exception {
		System.out.println(MyStringUtils.rightPad("0123456789012345678901234567890123456789012345678901234567890123456789", 13, " "));
		System.out.println(MyStringUtils.rightPad("*****       人工強制授權報表     ", 50, "$"));
		System.out.println(MyStringUtils.rightPad("0123456789012345678901234567", 50, "$"));
		System.out.println(MyStringUtils.leftPad("*****       人工強制授權報表     ", 50, "$"));
		System.out.println(MyStringUtils.leftPad("0123456789012345678901234567", 50, "$"));
		
		System.out.println(MyStringUtils.rightPad("*****       人工強制授權報表     ", 33, "$"));
		System.out.println(MyStringUtils.rightPad("*****       人工強制授權報表     ", 20, "$"));
		
		
	}
}