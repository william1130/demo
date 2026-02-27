/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 * 
 */
public class LlbinHexParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public LlbinHexParseInfo() {
		super(ISOType.LLBINHEX, 0);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLBINHEX field %d position %d", field, pos), pos,
					field);
		} else if (pos + 1 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient bin LLBINHEX header field %d", field), pos, field);
		}
		final int l = decodeHexLength(buf, pos, 2);
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLBINHEX length %d pos %d", length, pos), pos,
					field);
		}
		if (l + pos + 1 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin LLBINHEX field %d, pos %d: need %d, only %d available", field, pos, l,
					buf.length), pos, field);
		}
		byte[] _v = new byte[l];
		System.arraycopy(buf, pos + 1, _v, 0, l);
		if (isNested(reference, (String[])custom)) {
			return new ISOValue<ISO8583Fields>(type, parseNestFields(_v, reference, vo, isTraceEnable, (String[])custom), l, getDescription(), true);
		} else {
			return new ISOValue<byte[]>(type, _v, l, getDescription(), false);
		}

	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		int missing = 0;
		if (value.getValue() instanceof byte[]) {
			writeLengthHexHeader(value.getLength(), outs, 2);
			outs.write((byte[]) value.getValue());
			missing = value.getLength() - ((byte[]) value.getValue()).length;
		} else if (value.getValue() instanceof ISO8583Fields) {
			byte[] _v = this.writeNestFields((ISO8583Fields) value.getValue(), reference, vo, (String[])custom);
			writeLengthHexHeader(_v.length, outs, 2);
			outs.write(_v);
		} else {
			writeLengthHexHeader(value.getLength(), outs, 2);
			byte[] binval = CommonFunction.hexDecode(value.getValue().toString());
			outs.write(binval);
			missing = value.getLength() - binval.length;
		}
		if (type == ISOType.BINARY && missing > 0) {
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
		}
	}
	
	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLBINHEX field %d position %d",
                    field, pos), pos, field);
		} else if (pos+2 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient LLBINHEX header field %d",
                    field), pos, field);
		}
		int length = decodeStringHexLength(buf, pos, 2);
		if (length < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLBINHEX field %d length %d pos %d",
                    field, length, pos), pos, field);
		}
		if (length+pos+2 > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for LLBINHEX field %d, pos %d (LEN states '%s')",
                    field, pos, new String(buf, pos, 2)), pos, field);
		}
		byte[] binval = length == 0 ? new byte[0] : CommonFunction.hexDecode(new String(buf, pos + 2, length));
		if (isNested(reference, (String[])custom)) {
			return new ISOValue<ISO8583Fields>(type, parseStringNestFields(binval, reference, vo, isTraceEnable, (String[])custom), binval.length, getDescription(), true);
		} else {
			return new ISOValue<byte[]>(type, binval, binval.length, getDescription(), false);
		}
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		writeHexStringLengthHeader(value.getLength(), outs, 2);
		int missing = 0;
		int length = this.length == 0 ? varLength : this.length;
		if (value.getValue() instanceof byte[]) {
			outs.write((byte[]) value.getValue());
			missing = value.getLength() - ((byte[]) value.getValue()).length;
		} else if (value.getValue() instanceof ISO8583Fields) {
			byte[] _v = this.writeNestFields((ISO8583Fields) value.getValue(), reference, vo, (String[])custom);
			outs.write(_v);
			missing = length - (_v).length;
		} else {
			byte[] binval = CommonFunction.hexDecode(value.getValue().toString());
			outs.write(binval);
			missing = value.getLength() - binval.length;
		}
		if (type == ISOType.BINARY && missing > 0) {
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
		}
	}

}
