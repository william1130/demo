package proj.nccc.atsLog.batch.util;

import org.apache.commons.lang3.StringUtils;


public class CommUtil {

	public CommUtil() {

	}
	public static String fillChar(String orgStr,int totalLength,String align,String fillChar) 
	{
		if("LEFT".equalsIgnoreCase(align))
			return MyStringUtils.rightPad(orgStr, totalLength, fillChar);
		else if("RIGHT".equalsIgnoreCase(align))
			return MyStringUtils.leftPad(orgStr, totalLength, fillChar);
		else if("CENTER".equalsIgnoreCase(align))
			return StringUtils.center(orgStr, totalLength, fillChar);
		else
			return orgStr;
	}
	
	/**
	 * translate to 9,999,999
	 * @param value
	 * @param format
	 * @return
	 */
	public static String numberFormat(long value, String format) {
		if (format == null) {
			format = "#,###";
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat(format);  
		return df.format(value);
	}
		
	
	/**
	 * value1/value2 : 取小數後兩位
	 * @param value1
	 * @param value2
	 * @return
	 * @throws Exception 
	 */
	public static String divideAndFormat(long value1, long value2) throws Exception {
		if (value2 != 0 && value1 != 0) {
//			double f = value1 / value2;
			double f = Arith.div(value1, value2, 2);
			java.text.DecimalFormat df = new java.text.DecimalFormat("###.00");  
			return MyStringUtils.leftPad(df.format(Arith.round(f, 2)), 6, "0");
		}else {
			return "000.00";
		}
	}
	
	public static String divideAndFormat(double value1, long value2, int range) throws Exception {
		if (value2 != 0 && value1 > 0.0) {
//			double f = value1 / value2;
			double f = Arith.div(value1, value2, 2);
			java.text.DecimalFormat df = new java.text.DecimalFormat("###.00");  
			return MyStringUtils.leftPad(df.format(Arith.round(f, range)), 6, "0");
		}else {
			return "000.00";
		}
	}
	
	
	/**
	 * @param value
	 * @param scale 小數點後幾位
	 * @param format : default "###.00"
	 * @return
	 */
	public static String formatToDoubleString(double value, String format) {
		if (format == null) {
			format = "##0.00";
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat(format);  
		return df.format(value);
	}
	
	/**
	 * 於字串間隔加符號
	 * @param data
	 * @param len
	 * @param symbol
	 * @return
	 */
	public static String formatWithSymbol(String data, int len, String symbol) {
		if (data == null || data.length() <= len) {
			return data;
		}
		
		String ret = "";
		for(int i = 0 ; i < data.length() ; i += len) {
			if (i+len >= data.length()) {
				ret += data.substring(i);
				break;
			}
			ret += data.substring(i, i+len) + symbol;
		}
		return ret;
	}
	
	public static void main(String[] args) {
		System.out.println(CommUtil.formatWithSymbol("12345678", 2, ":"));
		System.out.println(CommUtil.formatWithSymbol("1", 2, ":"));
		System.out.println(CommUtil.formatWithSymbol("12", 2, ":"));
		System.out.println(CommUtil.formatWithSymbol("123", 2, ":"));
		System.out.println(CommUtil.formatWithSymbol("1234", 2, ":"));
		System.out.println(CommUtil.formatWithSymbol("1234567", 2, ":"));
		System.out.println(CommUtil.formatWithSymbol("中文34567", 2, ":"));
		
	}
}
