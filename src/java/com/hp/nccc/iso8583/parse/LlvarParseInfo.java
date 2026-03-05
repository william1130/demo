/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 * 
 */
public class LlvarParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public LlvarParseInfo() {
		super(ISOType.LLVAR, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		
		int length = decodeLength(buf, pos, 2);
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLVAR field %d pos %d", field, pos), pos, field);
		} else if ( length != 0 && (pos+2 > buf.length)) {
			throw new ISO8583ParseException(String.format("Insufficient data for bin LLVAR header, field %d pos %d",
					field, pos), pos, field);
		}
//		length = decodeLength(buf, pos, 2);
		if (length < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLVAR length %d, field %d pos %d", length,
					field, pos), pos, field);
		}
		if (length + pos + 1 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient data for bin LLVAR field %d, pos %d", field,
					pos), pos, field);
		}
		String s = new String(buf, pos + 1, length, getCharacterEncoding());
		if (isNested(reference, (String[])custom)) {
			return new ISOValue<ISO8583Fields>(type, parseNestFields(s.getBytes(getCharacterEncoding()), reference, vo, isTraceEnable, (String[])custom),
					length, getDescription(), true);
		} else {
			return new ISOValue<String>(type, s, length, getDescription(), false);
		}
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		
		if (value.getValue() instanceof ISO8583Fields) {
			byte[] _v = this.writeNestFields((ISO8583Fields) value.getValue(), reference, vo, (String[])custom);
			writeLengthHeader(_v.length, outs, 2);
			outs.write(_v);
		} else {
			writeLengthHeader(value.getLength(), outs, 2);
			outs.write(value.getCharacterEncoding() == null ? value.toString().getBytes() : value.toString().getBytes(
					value.getCharacterEncoding()));
		}
	}
	
	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		int length = decodeStringLength(buf, pos, 2);
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLVAR field %d %d", field, pos), pos, field);
		} else if (length != 0 && (pos+2 > buf.length)) {
			throw new ISO8583ParseException(String.format("Insufficient data for LLVAR header, pos %d", pos), pos, field);
		}
//		length = decodeStringLength(buf, pos, 2);
		if (length < 0) {
			throw new ISO8583ParseException(String.format(
                    "Invalid LLVAR length %d, field %d pos %d", length, field, pos), pos, field);
		} else if (length+pos+2 > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for LLVAR field %d, pos %d", field, pos), pos, field);
		}
		String _v;
        try {
            _v = length == 0 ? "" : new String(buf, pos + 2, length, getCharacterEncoding());
        } catch (IndexOutOfBoundsException ex) {
            throw new ISO8583ParseException(String.format(
                    "Insufficient data for LLVAR header, field %d pos %d", field, pos), pos, field);
        }
		//This is new: if the String's length is different from the specified length in the buffer,
		//there are probably some extended characters. So we create a String from the rest of the buffer,
		//and then cut it to the specified length.
		if (_v.length() != length) {
			_v = new String(buf, pos + 2, buf.length-pos-2, getCharacterEncoding()).substring(0, length);
		}
		if (isNested(reference, (String[])custom)) {
			return new ISOValue<ISO8583Fields>(type, parseNestFields(_v.getBytes(getCharacterEncoding()), reference, vo, isTraceEnable, (String[])custom),
					length, getDescription(), true);
		} else {
			return new ISOValue<String>(type, _v, length, getDescription(), false);
		}
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		writeStringLengthHeader(value.getLength(), outs, 2);
		if (value.getValue() instanceof ISO8583Fields) {
			int missing = 0;
			byte[] _v = this.writeNestFields((ISO8583Fields) value.getValue(), reference, vo, (String[])custom);
			outs.write(_v);
			missing = value.getLength() - (_v).length;
			for (int i = 0; i < missing; i++) {
				outs.write(String.valueOf(0).getBytes());
			}
		} else {
			outs.write(value.getCharacterEncoding() == null ? value.toString().getBytes() : value.toString().getBytes(
					value.getCharacterEncoding()));
		}
	}

}
