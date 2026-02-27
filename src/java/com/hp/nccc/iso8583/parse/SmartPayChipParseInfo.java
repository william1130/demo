package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.core.SmartPayChip;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

public class SmartPayChipParseInfo extends FieldParseInfo {

	//private Logger log = Logger.getLogger(this.getClass());

	public SmartPayChipParseInfo() {
		super(ISOType.SMARTPAYCHIP, 0);
	}

	@Override
	public <T> ISOValue<?> parse(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin SMARTPAYCHIP field %d pos %d", field, pos), pos,
					field);
		} else if (pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient SMARTPAYCHIP header field %d", field), pos, field);
		}
		final int l = decodeLength(buf, pos, 3);
		
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid SMARTPAYCHIP length %d field %d pos %d", l, field, pos),
					pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin SMARTPAYCHIP field %d, pos %d requires %d, only %d available", field, pos, l,
					buf.length - pos + 1), pos, field);
		}
		int offset = 0;
		SmartPayChip smartPayChip = new SmartPayChip();
		byte[] _v = new byte[l];
		offset++;
		System.arraycopy(buf, pos + 2, _v, 0, l);
		//int sLen = CommonFunction.getHexLength(_v, 0, offset);
		long sLen = CommonFunction.Bcd.decodeToLong(_v, 0, 2);
		smartPayChip.setSerialNumber(new String(_v, offset, (int)sLen));
		offset += sLen;
		//int tLen = CommonFunction.getHexLength(_v, offset, 2);
		long tLen = CommonFunction.Bcd.decodeToLong(_v, offset, 4);
		offset += 2;
		byte[] tac = new byte[(int)tLen];
		System.arraycopy(_v, offset, tac, 0, (int)tLen);
		smartPayChip.setTac(tac);
		offset += tLen;
		long mLen = CommonFunction.Bcd.decodeToLong(_v, offset, 2);
		offset++;
		smartPayChip.setMcc(new String(_v, offset, (int)mLen));
		offset += mLen;

		return new ISOValue<SmartPayChip>(type, smartPayChip, l, getDescription(), false);
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		writeLengthHeader(value.getLength(), outs, 3);
		int missing = 0;
		if (value.getValue() instanceof byte[]) {
			outs.write((byte[]) value.getValue());
			missing = value.getLength() - ((byte[]) value.getValue()).length;
		} else if (value.getValue() instanceof SmartPayChip) {
			SmartPayChip chip = (SmartPayChip) value.getValue();
			//String sLen = CommonFunction.writeHexLength(chip.getSerialNumber().length(), 1);
			byte[] sLen = new byte[1];
			CommonFunction.Bcd.encode(String.format("%02d", chip.getSerialNumber().length()), sLen);
			outs.write(sLen);
			outs.write(chip.getSerialNumber().getBytes());
			//String tLen = CommonFunction.writeHexLength(chip.getTac().length, 2);
			byte[] tLen = new byte[2];
			CommonFunction.Bcd.encode(String.format("%04d", chip.getTac().length), tLen);
			outs.write(tLen);
			outs.write(chip.getTac());
			byte[] mLen = new byte[1];
			CommonFunction.Bcd.encode(String.format("%02d", chip.getMcc().length()), mLen);
			outs.write(mLen);
			outs.write(chip.getMcc().getBytes());
		} else {
			byte[] binval = CommonFunction.hexDecode(value.getValue().toString());
			outs.write(binval);
			missing = value.getLength() - binval.length;
		}
		if (type == ISOType.SMARTPAYCHIP && missing > 0) {
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
		}
	}

	@Override
	public <T> ISOValue<?> parseString(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		throw new RuntimeException("not implement yet");
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		throw new RuntimeException("not implement yet");
	}

}
