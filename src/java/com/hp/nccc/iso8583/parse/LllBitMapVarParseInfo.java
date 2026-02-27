package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOBitMapVar;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

public class LllBitMapVarParseInfo extends FieldParseInfo {
	
	public LllBitMapVarParseInfo() {
		super(ISOType.LLLBITMAPVAR, 0);
	}

	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLTABLES field %d pos %d", field, pos), pos, field);
		} else if (pos + 3 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin LLLTABLES header, field %d pos %d", field, pos), pos, field);
		}
		final int l = decodeLength(buf, pos, 3);
		;
		if (l < 0) {
			throw new ISO8583ParseException(
					String.format("Invalid LLLTABLES length %d field %d pos %d", l, field, pos), pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for LLLTABLES field %d, pos %d requires %d, only %d available", field, pos, l,
					buf.length - pos + 1), pos, field);
		}
		byte[] data = new byte[l];
		int length = l;
		System.arraycopy(buf, pos + 2, data, 0, l);
		
		ISOBitMapVar var = new ISOBitMapVar();
		final BitSet bs = new BitSet(64);
		int position = 0;
		for (int i = 0; i < 8 ; i++) {
			int bit = 128;
			for (int b = 0; b < 8; b++) {
				bs.set(position++, (data[i] & bit) != 0);
				bit >>= 1;
			}
		}
		
		var.setBitMap(bs);
		
		byte[] nestData = new byte[l - 8];
		System.arraycopy(data, 8, nestData, 0, l - 8);

		ISO8583Fields result = parseNestFields(nestData, this.getReference(), vo, isTraceEnable, bs, (String[]) custom);
		
		var.setFields(result);
		
		return new ISOValue<ISOBitMapVar>(type, var, length, description, true);
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		writeLengthHeader(value.getLength(), outs, 3);
		ISOBitMapVar var = (ISOBitMapVar) value.getValue();
		// Bitmap
		BitSet bs = createBitmapBitSet(var.getFields());
		// Write bitmap to stream
		int pos = 128;
		int b = 0;
		for (int i = 0; i < bs.size(); i++) {
			if (bs.get(i)) {
				b |= pos;
			}
			pos >>= 1;
			if (pos == 0) {
				outs.write(b);
				pos = 128;
				b = 0;
			}
		}
		byte[] subData = writeNestFields(var.getFields(), this.getReference(), vo, (String[]) custom);
		outs.write(subData);
	}

	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		// TODO Auto-generated method stub

	}

}
