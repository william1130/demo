/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 *
 */
public class TimeParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public TimeParseInfo() {
		super(ISOType.TIME, 6);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin TIME field %d pos %d",
                    field, pos), pos, field);
		} else if (pos+3 > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for bin TIME field %d, pos %d", field, pos), pos, field);
		}
		int[] tens = new int[3];
		int start = 0;
		for (int i = pos; i < pos + 3; i++) {
			tens[start++] = (((buf[i] & 0xf0) >> 4) * 10) + (buf[i] & 0x0f);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, tens[0]);
		cal.set(Calendar.MINUTE, tens[1]);
		cal.set(Calendar.SECOND, tens[2]);
		return new ISOValue<Date>(type, cal.getTime(), 6, getDescription(), false);
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		byte[] buf = new byte[length / 2];
		CommonFunction.Bcd.encode(value.toString(), buf);
		outs.write(buf);
		return;
	}
	
	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid TIME field %d pos %d",
                    field, pos), pos, field);
		} else if (pos+6 > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for TIME field %d, pos %d", field, pos), pos, field);
		}
		Calendar cal = Calendar.getInstance();
        if (forceStringDecoding) {
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.MINUTE, Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.SECOND, Integer.parseInt(new String(buf, pos+4, 2, getCharacterEncoding()), 10));
        } else {
            cal.set(Calendar.HOUR_OF_DAY, ((buf[pos] - 48) * 10) + buf[pos + 1] - 48);
            cal.set(Calendar.MINUTE, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48);
            cal.set(Calendar.SECOND, ((buf[pos + 4] - 48) * 10) + buf[pos + 5] - 48);
        }
		return new ISOValue<Date>(type, cal.getTime(), 6, getDescription(), false);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		outs.write(value.toString().getBytes());
		return;
	}

}
