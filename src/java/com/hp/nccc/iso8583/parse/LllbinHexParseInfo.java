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

public class LllbinHexParseInfo extends FieldParseInfo {

	public LllbinHexParseInfo() {
		super(ISOType.LLLBINHEX, 0);
	}

	@Override
	public <T> ISOValue<?> parse(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLLBINHEX field %d pos %d", field, pos), pos, field);
		} else if (pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient LLLBINHEX header field %d", field), pos, field);
		}
		final int l = decodeHexLength(buf, pos, 3);
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLBINHEX length %d field %d pos %d", l, field, pos),
					pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin LLLBINHEX field %d, pos %d requires %d, only %d available", field, pos, l,
					buf.length - pos + 1), pos, field);
		}
		byte[] _v = new byte[l];
		System.arraycopy(buf, pos + 2, _v, 0, l);
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
			writeLengthHexHeader(value.getLength(), outs, 3);
			outs.write((byte[]) value.getValue());
			missing = value.getLength() - ((byte[]) value.getValue()).length;
		} else if (value.getValue() instanceof ISO8583Fields) {
			byte[] _v = this.writeNestFields((ISO8583Fields) value.getValue(), reference, vo, (String[])custom);
			writeLengthHexHeader(_v.length, outs, 3);
			outs.write(_v);
		} else {
			writeLengthHexHeader(value.getLength(), outs, 3);
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
	public <T> ISOValue<?> parseString(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLBINHEX field %d pos %d",
                    field, pos), pos, field);
		} else if (pos+3 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient LLLBINHEX header field %d",
                    field), pos, field);
		}
		final int l = decodeStringHexLength(buf, pos, 3);
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLBINHEX length %d field %d pos %d",
                    l, field, pos), pos, field);
		} else if (l+pos+3 > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for LLLBINHEX field %d, pos %d", field, pos), pos, field);
		}
		byte[] binval = l == 0 ? new byte[0] : CommonFunction.hexDecode(new String(buf, pos + 3, l));
		if (isNested(reference, (String[])custom)) {
			return new ISOValue<ISO8583Fields>(type, parseStringNestFields(binval, reference, vo, isTraceEnable, (String[])custom), binval.length, getDescription(), true);
		} else {
			return new ISOValue<byte[]>(type, binval, binval.length, getDescription(), false);
		}
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		writeHexStringLengthHeader(value.getLength(), outs, 3);
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
