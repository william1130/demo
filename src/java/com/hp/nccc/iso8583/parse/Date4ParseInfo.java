/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class Date4ParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public Date4ParseInfo() {
		super(ISOType.DATE4, 4);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		int[] tens = new int[2];
		int start = 0;
		if (buf.length - pos < 2) {
			throw new ISO8583ParseException(String.format("Insufficient data to parse binary DATE4 field %d pos %d",
					field, pos), pos, field);
		}
		for (int i = pos; i < pos + tens.length; i++) {
			tens[start++] = (((buf[i] & 0xf0) >> 4) * 10) + (buf[i] & 0x0f);
		}
		if (tens[0] < 1 || tens[0] > 12 || tens[1] < 1 || tens[1] > 31) {
			throw new ISO8583ParseException(String.format("error date format to parse binary DATE4 value[%d%d] field %d pos %d", tens[0], tens[1],
					field, pos), pos, field);
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("MMdd");
			try {

				formatter.parse(String.format("%02d%02d", (tens[0] - 1),tens[1]));
			} catch (ParseException e) {
				throw new ISO8583ParseException(String.format(
						"Insufficient data to parse binary DATE4 field %d pos %d", field, pos), pos, field);
			}
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		//Set the month in the date
		cal.set(Calendar.MONTH, tens[0] - 1);
		cal.set(Calendar.DATE, tens[1]);
		cal.set(Calendar.MILLISECOND, 0);
		Date10ParseInfo.adjustWithFutureTolerance(cal);
		return new ISOValue<Date>(type, cal.getTime(), 4, getDescription(), false);
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		byte[] buf = new byte[length / 2];
		CommonFunction.Bcd.encode(value.toString(), buf);
		outs.write(buf);
		return;
	}
	
	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid DATE4 field %d position %d",
                    field, pos), pos, field);
		}
		if (pos+4 > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for DATE4 field %d, pos %d", field, pos), pos, field);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//Set the month in the date
        if (forceStringDecoding) {
            cal.set(Calendar.MONTH, Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10)-1);
            cal.set(Calendar.DATE, Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10));
        } else {
            cal.set(Calendar.MONTH, ((buf[pos] - 48) * 10) + buf[pos + 1] - 49);
            cal.set(Calendar.DATE, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48);
        }
		Date10ParseInfo.adjustWithFutureTolerance(cal);
		return new ISOValue<Date>(type, cal.getTime(), 4, getDescription(), false);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		outs.write(value.toString().getBytes());
		return;
	}

}
