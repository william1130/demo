/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 * 
 */
public class EbcdicParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public EbcdicParseInfo(int len) {
		super(ISOType.EBCDIC, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parseBinary(int, byte[],
	 * int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin EBCDIC field %d position %d", field, pos), pos,
					field);
		} else if (pos + length > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient data for bin %s field %d of length %d, pos %d",
					type, field, length, pos), pos, field);
		}
		try {
			return new ISOValue<String>(type, new String(buf, pos, length, "CP1047"), length,
					this.getDescription(), false);
		} catch (IndexOutOfBoundsException ex) {
			throw new ISO8583ParseException(String.format("Insufficient data for bin %s field %d of length %d, pos %d",
					type, field, length, pos), pos, field);
		}
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		String s = value.toString().substring(0, length);
		outs.write(s.toString().getBytes("CP1047"));
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
			String _v = new String(buf, pos, length, "CP1047");
			if (_v.length() != length) {
				_v = new String(buf, pos, buf.length - pos, "CP1047").substring(0, length);
			}
			return new ISOValue<String>(type, _v, length, this.getDescription(), false);
		} catch (StringIndexOutOfBoundsException ex) {
			throw new ISO8583ParseException(String.format("Insufficient data for %s field %d of length %d, pos %d", type,
					field, length, pos), pos, field);
		}
	}
	
	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		String s = value.toString().substring(0, length);
		outs.write(s.toString().getBytes("CP1047"));
	}

}
