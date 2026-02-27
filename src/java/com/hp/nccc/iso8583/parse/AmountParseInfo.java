/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 *
 */
public class AmountParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public AmountParseInfo() {
		super(ISOType.AMOUNT, 12);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		char[] digits = new char[13];
		digits[10] = '.';
		int start = 0;
		for (int i = pos; i < pos + 6; i++) {
			digits[start++] = (char)(((buf[i] & 0xf0) >> 4) + 48);
			digits[start++] = (char)((buf[i] & 0x0f) + 48);
			if (start == 10) {
				start++;
			}
		}
		try {
			return new ISOValue<BigDecimal>(ISOType.AMOUNT, new BigDecimal(new String(digits)), 12, getDescription(), false);
		} catch (NumberFormatException ex) {
			throw new ISO8583ParseException(String.format("Cannot read amount '%s' field %d pos %d",
                    new String(digits), field, pos), pos, field);
        } catch (IndexOutOfBoundsException ex) {
            throw new ISO8583ParseException(String.format(
                    "Insufficient data for AMOUNT field %d, pos %d", field, pos), pos, field);
		}
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		byte[] buf = new byte[6];
		CommonFunction.Bcd.encode(value.toString(), buf);
		outs.write(buf);
	}
	
	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid AMOUNT field %d position %d",
                    field, pos), pos, field);
		}
		if (pos+12 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient data for AMOUNT field %d, pos %d",
                    field, pos), pos, field);
		}
		String c = new String(buf, pos, 12, getCharacterEncoding());
		try {
			return new ISOValue<BigDecimal>(type, new BigDecimal(c).movePointLeft(2), 12, getDescription(), false);
		} catch (NumberFormatException ex) {
			throw new ISO8583ParseException(String.format("Cannot read amount '%s' field %d pos %d",
                    c, field, pos), pos, field);
        } catch (IndexOutOfBoundsException ex) {
            throw new ISO8583ParseException(String.format(
                    "Insufficient data for AMOUNT field %d, pos %d", field, pos), pos, field);
		}
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		outs.write(value.toString().getBytes());
	}

}
