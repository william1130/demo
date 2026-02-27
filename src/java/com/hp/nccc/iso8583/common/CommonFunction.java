/**
 * 
 */
package com.hp.nccc.iso8583.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author hsiehes
 * 
 */
public class CommonFunction {
	public static int getHexLength(byte[] bytes, int startIndex, int byteLengths){
		int totLen = 0;
		for (int i = startIndex; i < startIndex + byteLengths; i++) {
			int b = 0;
			b = bytes[i];
			totLen = totLen * 256 + (b & 0xFF);
		}
		return totLen;
	}
	
	public static int getHexLength(byte[] bytes){
		return getHexLength(bytes, 0, bytes.length);
	}
	
	public static String writeHexLength(int length, int digits){
		StringBuffer hex = new StringBuffer();
		int by = length;
		while(true){
			int mod = length%16;
			by = (int)(length/16);
			hex.insert(0, intToHex(mod));
			if(by == 0){
				break;
			}
			length = by;
		}
		int hl = hex.length();
		for(int i = 0; i < digits*2 - hl; i++){
			hex.insert(0, "0");
		}
		return hex.toString();
	}
	
	public static void main(String[] args){
		System.out.println(writeHexLength(999, 2));
	}
	
	public static String getTimeString(long msec) {
		SimpleDateFormat format = new SimpleDateFormat(
				CommonContext.TIME_FORMATE);
		format.setTimeZone(TimeZone.getTimeZone("GMT-0"));
		return format.format(new Date(msec));
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String intToHex(int value){
		if(value >= 0 && value < 16){
			return String.valueOf(hexArray[value]);
		}else{
			return intToHex(value/16) + intToHex(value%16);
		}
	}

	public static String bytesToHex(byte[] bytes) {
		return hexEncode(bytes, 0, bytes.length);
	}

	public static String bytesToHex(byte[] bytes, String split, int start, int length) {
		if(bytes == null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i = start; i < start + length; i++){
			sb.append(hexEncode(bytes[i])).append(split);
		}
		return sb.toString();
	}
	
	public static String bytesToHex(byte[] bytes, String split) {
		return bytesToHex(bytes, split, 0 , bytes.length);
	}
	
	public static String hexEncode(byte value){
		char[] chars = new char[2];
		int holder = 0;
		holder = (value & 0xf0) >> 4;
		chars[0] = hexArray[holder];
		holder = value & 0x0f;
		chars[1] = hexArray[holder];
		return new String(chars);
	}

	public static String hexEncode(byte[] buffer, int start, int length) {
		if (buffer.length == 0) {
			return "";
		}
		int holder = 0;
		char[] chars = new char[length * 2];
		int pos = -1;
		for (int i = start; i < start + length; i++) {
			holder = (buffer[i] & 0xf0) >> 4;
			chars[++pos * 2] = hexArray[holder];
			holder = buffer[i] & 0x0f;
			chars[(pos * 2) + 1] = hexArray[holder];
		}
		return new String(chars);
	}

	public static byte[] hexDecode(String hex) {
		// A null string returns an empty array
		if (hex == null || hex.length() == 0) {
			return new byte[0];
		} else if (hex.length() < 3) {
			return new byte[] { (byte) (Integer.parseInt(hex, 16) & 0xff) };
		}
		// Adjust accordingly for odd-length strings
		int count = hex.length();
		int nibble = 0;
		if (count % 2 != 0) {
			count++;
			nibble = 1;
		}
		byte[] buf = new byte[count / 2];
		char c = 0;
		int holder = 0;
		int pos = 0;
		for (int i = 0; i < buf.length; i++) {
			for (int z = 0; z < 2 && pos < hex.length(); z++) {
				c = hex.charAt(pos++);
				if (c >= 'A' && c <= 'F') {
					c -= 55;
				} else if (c >= '0' && c <= '9') {
					c -= 48;
				} else if (c >= 'a' && c <= 'f') {
					c -= 87;
				}
				if (nibble == 0) {
					holder = c << 4;
				} else {
					holder |= c;
					buf[i] = (byte) holder;
				}
				nibble = 1 - nibble;
			}
		}
		return buf;
	}
	
	public static byte checkSum(byte[] bytes) {
		byte sum = 0;
		for (byte b : bytes) {
			sum ^= b;
		}
		return sum;
	}

	public static final class Bcd {

		private Bcd() {
		}

		/**
		 * Decodes a BCD-encoded number as a long.
		 * 
		 * @param buf
		 *            The byte buffer containing the BCD data.
		 * @param pos
		 *            The starting position in the buffer.
		 * @param length
		 *            The number of DIGITS (not bytes) to read.
		 */
		public static long decodeToLong(byte[] buf, int pos, int length)
				throws IndexOutOfBoundsException {
			if (length > 18) {
				throw new IndexOutOfBoundsException(
						"Buffer too big to decode as long");
			}
			long l = 0;
			long power = 1L;
			for (int i = pos + (length / 2) + (length % 2) - 1; i >= pos; i--) {
				l += (buf[i] & 0x0f) * power;
				power *= 10L;
				l += ((buf[i] & 0xf0) >> 4) * power;
				power *= 10L;
			}
			return l;
		}

		/**
		 * Encode the value as BCD and put it in the buffer. The buffer must be
		 * big enough to store the digits in the original value (half the length
		 * of the string).
		 */
		public static void encode(String value, byte[] buf) {
			int charpos = 0; // char where we start
			int bufpos = 0;
			if (value.length() % 2 == 1) {
				// for odd lengths we encode just the first digit in the first
				// byte
				buf[0] = (byte) (value.charAt(0) - 48);
				charpos = 1;
				bufpos = 1;
			}
			// encode the rest of the string
			while (charpos < value.length()) {
				buf[bufpos] = (byte) (((value.charAt(charpos) - 48) << 4) | (value
						.charAt(charpos + 1) - 48));
				charpos += 2;
				bufpos++;
			}
		}

		/**
		 * Decodes a BCD-encoded number as a BigInteger.
		 * 
		 * @param buf
		 *            The byte buffer containing the BCD data.
		 * @param pos
		 *            The starting position in the buffer.
		 * @param length
		 *            The number of DIGITS (not bytes) to read.
		 */
		public static BigInteger decodeToBigInteger(byte[] buf, int pos,
				int length) throws IndexOutOfBoundsException {
			char[] digits = new char[length];
			int start = 0;
			for (int i = pos; i < pos + (length / 2) + (length % 2); i++) {
				digits[start++] = (char) (((buf[i] & 0xf0) >> 4) + 48);
				digits[start++] = (char) ((buf[i] & 0x0f) + 48);
			}
			return new BigInteger(new String(digits));
		}

		public static String decodeToHexString(byte[] buf, int pos, int length) {
			int cnt = length % 2 == 0 ? length / 2 : length / 2 + 1;
			int offset = pos + 1;
			String dest = "", lByte = "", rByte = "";
			for (int i = 0; i < cnt; i++) {
				int cvt = buf[offset];
				if (cvt < 0) {
					cvt += 256;
				}
				lByte = Integer.toHexString(cvt / 16);
				lByte = lByte.toUpperCase();
				rByte = Integer.toHexString(cvt % 16);
				rByte = rByte.toUpperCase();
				dest = dest + lByte + rByte;
				offset++;
			}
			return dest;
		}

	}
	
	public static List<String> getResourceFiles(String path) throws IOException {
		List<String> filenames = new ArrayList<>();

		try (InputStream in = getResourceAsStream(path);
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String resource;

			while ((resource = br.readLine()) != null) {
				filenames.add(resource);
			}
		}

		return filenames;
	}

	public static InputStream getResourceAsStream(String resource) {
		return getContextClassLoader().getResourceAsStream(resource);

//		return in == null ? getClass().getResourceAsStream(resource) : in;
	}

	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static String hexToUtf8String(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}

		return new String(data, StandardCharsets.UTF_8);
	}
}
