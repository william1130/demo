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
public class VarParseInfo extends FieldParseInfo {
	
	/**
	 * @param t
	 * @param len
	 */
	public VarParseInfo(int length) {
		super(ISOType.VAR, length);
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
			throw new ISO8583ParseException(String.format("Invalid bin VAR field %d position %d", field, pos), pos,
					field);
		} 
		Object[] objs = (Object[])custom;
		int length = (Integer)objs[0];
		
		if (pos + length > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient data for bin %s field %d of length %d, pos %d",
					type, field, length, pos), pos, field);
		}
		try {
			return new ISOValue<String>(type, new String(buf, pos, length, getCharacterEncoding()), length,
					this.getDescription(), false);
		} catch (IndexOutOfBoundsException ex) {
			throw new ISO8583ParseException(String.format("Insufficient data for bin %s field %d of length %d, pos %d",
					type, field, length, pos), pos, field);
		}
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		String s = length == 0?value.toString().substring(0, varLength):value.toString().substring(0, length);
		outs.write(value.getCharacterEncoding() == null ? s.getBytes() : s.toString().getBytes(
				value.getCharacterEncoding()));
	}

	@Override
	public <T> ISOValue<?> parseString(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		Object[] objs = (Object[])custom;
		int length = this.length;
		if(objs.length >0 && objs[0] != null && objs[0] instanceof Integer ){
			int totLength = (Integer)objs[0];
			length = totLength;
		}
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid VAR field %d position %d", field, pos), pos, field);
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
		String s = value.toString();
		outs.write(value.getCharacterEncoding() == null ? s.getBytes() : s.toString().getBytes(
				value.getCharacterEncoding()));
	}

}
