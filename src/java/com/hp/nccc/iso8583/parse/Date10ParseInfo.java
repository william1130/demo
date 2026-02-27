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
public class Date10ParseInfo extends FieldParseInfo {

	private static final long FUTURE_TOLERANCE;

	static {
		FUTURE_TOLERANCE = Long.parseLong(System.getProperty("j8583.future.tolerance", "900000"));
	}

	/**
	 * @param t
	 * @param len
	 */
	public Date10ParseInfo() {
		super(ISOType.DATE10, 10);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid DATE10 field %d position %d", field, pos), pos,
					field);
		}
		if (pos + 5 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient data for DATE10 field %d, pos %d", field, pos),
					pos, field);
		}
		int[] tens = new int[5];
		int start = 0;
		for (int i = pos; i < pos + tens.length; i++) {
			tens[start++] = (((buf[i] & 0xf0) >> 4) * 10) + (buf[i] & 0x0f);
		}
		if (tens[0] < 1 || tens[0] > 12 || tens[1] < 1 || tens[1] > 31 || tens[2] < 0 || tens[2] > 23 || tens[3] < 0
				|| tens[3] > 59 || tens[4] < 0 || tens[4] > 59) {
			throw new ISO8583ParseException(String.format("Insufficient data to parse binary DATE10 field %d pos %d",
					field, pos), pos, field);
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("MMddhhmmss");
			try {

				formatter.parse("" + (tens[0] - 1) + "" + tens[1] + "" + tens[2] + "" + tens[3] + "" + tens[4]);
			} catch (ParseException e) {
				throw new ISO8583ParseException(String.format(
						"Insufficient data to parse binary DATE10 field %d pos %d", field, pos), pos, field);
			}
		}
		Calendar cal = Calendar.getInstance();
		//A SimpleDateFormat in the case of dates won't help because of the missing data
		//we have to use the current date for reference and change what comes in the buffer
		//Set the month in the date
		cal.set(Calendar.MONTH, tens[0] - 1);
		cal.set(Calendar.DATE, tens[1]);
		cal.set(Calendar.HOUR_OF_DAY, tens[2]);
		cal.set(Calendar.MINUTE, tens[3]);
		cal.set(Calendar.SECOND, tens[4]);
		cal.set(Calendar.MILLISECOND, 0);
		adjustWithFutureTolerance(cal);
		return new ISOValue<Date>(type, cal.getTime(), 10 ,getDescription(), false);
	}

	public static void adjustWithFutureTolerance(Calendar cal) {
		//We need to handle a small tolerance into the future (a couple of minutes)
		long now = System.currentTimeMillis();
		long then = cal.getTimeInMillis();
		if (then > now && then - now > FUTURE_TOLERANCE) {
			cal.add(Calendar.YEAR, -1);
		}
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
			throw new ISO8583ParseException(String.format("Invalid DATE10 field %d position %d",
                    field, pos), pos, field);
		}
		if (pos+10 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient data for DATE10 field %d, pos %d",
                    field, pos), pos, field);
		}
		//A SimpleDateFormat in the case of dates won't help because of the missing data
		//we have to use the current date for reference and change what comes in the buffer
		Calendar cal = Calendar.getInstance();
		//Set the month in the date
        if (forceStringDecoding) {
            cal.set(Calendar.MONTH, Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10)-1);
            cal.set(Calendar.DATE, Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new String(buf, pos+4, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.MINUTE, Integer.parseInt(new String(buf, pos+6, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.SECOND, Integer.parseInt(new String(buf, pos+8, 2, getCharacterEncoding()), 10));
        } else {
            cal.set(Calendar.MONTH, ((buf[pos] - 48) * 10) + buf[pos + 1] - 49);
            cal.set(Calendar.DATE, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48);
            cal.set(Calendar.HOUR_OF_DAY, ((buf[pos + 4] - 48) * 10) + buf[pos + 5] - 48);
            cal.set(Calendar.MINUTE, ((buf[pos + 6] - 48) * 10) + buf[pos + 7] - 48);
            cal.set(Calendar.SECOND, ((buf[pos + 8] - 48) * 10) + buf[pos + 9] - 48);
        }
        cal.set(Calendar.MILLISECOND,0);
		adjustWithFutureTolerance(cal);
		return new ISOValue<Date>(type, cal.getTime(), 10,getDescription(), false);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		outs.write(value.toString().getBytes());
		return;
	}

}
