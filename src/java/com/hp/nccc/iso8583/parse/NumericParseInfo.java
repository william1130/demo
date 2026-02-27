/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.common.CommonFunction.Bcd;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 *
 */
public class NumericParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public NumericParseInfo(int len) {
		super(ISOType.NUMERIC, len);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parseBinary(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin NUMERIC field %d pos %d",
                    field, pos), pos, field);
		} else if (pos+(length/2) > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for bin %s field %d of length %d, pos %d",
				type, field, length, pos), pos, field);
		}
		// Supper Processing Code contains text.
		// pos-1:buf no length prompt.
		String hexStr = CommonFunction.Bcd.decodeToHexString(buf, pos - 1, length);
		if (isNumeric(hexStr)) {
			// A long covers up to 18 digits
			if (length < 19) {
				return new ISOValue<Number>(ISOType.NUMERIC, CommonFunction.Bcd.decodeToLong(buf, pos, length), length,
						getDescription(), false);
			} else {
				// Use a BigInteger
				try {
					return new ISOValue<Number>(ISOType.NUMERIC, 
					Bcd.decodeToBigInteger(buf, pos, length), length,getDescription(), false);
				} catch (IndexOutOfBoundsException ex) {
					throw new ISO8583ParseException(String.format(
					"Insufficient data for bin %s field %d of length %d, pos %d", 
					type, field, length, pos), pos, field);
				}
			}
		} else {
			return new ISOValue<String>(ISOType.NUMERIC, hexStr, length, getDescription(), false);
		}
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		byte[] buf = new byte[(length / 2) + (length % 2)];
		CommonFunction.Bcd.encode(value.toString(), buf);
		outs.write(buf);
		return;
	}
	
	@Override
	public <T> ISOValue<?> parseString(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid ALPHA/NUM field %d position %d", field, pos), pos, field);
		} else if (pos + length > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient data for %s field %d of length %d, pos %d", type,
					field, length, pos), pos, field);
		}
		try {
			String _v = new String(buf, pos, length, getCharacterEncoding());
			if (_v.length() != length) {
				_v = new String(buf, pos, buf.length - pos, getCharacterEncoding()).substring(0, length);
			}
			return new ISOValue<String>(type, _v, length, this.getDescription(), false);
		} catch (StringIndexOutOfBoundsException ex) {
			throw new ISO8583ParseException(String.format("Insufficient data for %s field %d of length %d, pos %d", type,
					field, length, pos), pos, field);
		}
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		outs.write(value.toString().getBytes());
		return;
	}
	
	// Validate number
	public static boolean isNumeric(String str) {

		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
