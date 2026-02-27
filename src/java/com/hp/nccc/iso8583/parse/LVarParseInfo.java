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
public class LVarParseInfo extends FieldParseInfo {
	/**
	 * @param t
	 * @param len
	 */
	public LVarParseInfo(int length) {
		super(ISOType.LVAR, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LVAR field %d position %d", field, pos), pos, field);
		}
		Object[] objs = (Object[]) custom;
		int length = (Integer) objs[0];
		if (pos + length > buf.length) {
			throw new ISO8583ParseException(
					String.format("Insufficient data for LVAR field %d of length %d, pos %d", field, length, pos), pos,
					field);
		}
		String s = new String(buf, pos, length, getCharacterEncoding());
		if (isNested(reference, (String[]) objs[1])) {
			return new ISOValue<ISO8583Fields>(type, parseNestFields(s.getBytes(getCharacterEncoding()), reference, vo,
					isTraceEnable, (String[]) objs[1]), length, getDescription(), true);
		} else {
			return new ISOValue<String>(type, s, length, getDescription(), false);
		}
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		int length = this.length == 0 ? varLength : this.length;
		writeLengthHeader(value.getLength(), outs, length);
		if (value.getValue() instanceof ISO8583Fields) {
			byte[] _v = this.writeNestFields((ISO8583Fields) value.getValue(), reference, vo, (String[]) custom);
			outs.write(_v);
		} else {
			outs.write(value.getCharacterEncoding() == null ? value.toString().getBytes()
					: value.toString().getBytes(value.getCharacterEncoding()));
		}
	}

	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LVAR field %d %d", field, pos), pos, field);
		}
		final int l = "byte".equals(this.getAlt()) ? decodeStringLength(buf, pos, length) * 2
				: decodeStringLength(buf, pos, length);
		int count = 1;
		Object[] objs = (Object[]) custom;
		if (objs[0] != null && objs[0] instanceof Integer) {
			int totLength = (Integer) objs[0];
			if ((totLength % 2 != 0 && "odd".equals(this.getFillIn()))
					|| (totLength % 2 == 0 && "even".equals(this.getFillIn()))) {
				if (" ".equals(new String(buf, "BIG5").substring(totLength - 1, totLength))) {
					totLength--;
				}
			}
			count = (totLength - length - pos) / l;
			if ((totLength - length - pos) % l != 0) {
				throw new ISO8583ParseException(
						String.format("Invalid LVAR length %d totLegth %d l %d, field %d pos %d", length, totLength, l,
								field, pos),
						pos, field);
			}
		}
		if (l < 0) {
			throw new ISO8583ParseException(
					String.format("Invalid LVAR length %d, field %d pos %d", length, field, pos), pos, field);
		} else if (length + pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient data for LVAR field %d, pos %d", field, pos),
					pos, field);
		}

		String[] _vs = new String[count];
		for (int i = 0; i < count; i++) {
			String _v;
			try {
				_v = l == 0 ? "" : new String(buf, pos + length + (i * l), l, getCharacterEncoding());
			} catch (IndexOutOfBoundsException ex) {
				throw new ISO8583ParseException(
						String.format("Insufficient data for LVAR header, field %d pos %d", field, pos), pos, field);
			}
			_vs[i] = _v;
		}
		if ("byte".equals(this.getAlt())) {
			return new ISOValue<String>(type, _vs[0], length, getDescription(), false);
		} else {
			return new ISOValue<String[]>(type, _vs, length, getDescription(), false);
		}
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		int length = this.length == 0 ? varLength : this.length;
		if (value.getValue() instanceof String[]) {
			String[] _vs = (String[]) value.getValue();
			String lString = String.format("%0" + length + "d", (value.getLength() - length) / _vs.length);
			outs.write(lString.getBytes());
			for (String s : _vs) {
				outs.write(s.getBytes(getCharacterEncoding()));
			}
		} else if (value.getValue() instanceof String) {
			String _vs = value.toString();
			String lString = String.format("%0" + length + "d", _vs.length() / 2);
			outs.write(lString.getBytes());
			outs.write(_vs.getBytes(getCharacterEncoding()));
		} else {
			outs.write(value.getCharacterEncoding() == null ? value.toString().getBytes()
					: value.toString().getBytes(value.getCharacterEncoding()));
		}
	}
}
