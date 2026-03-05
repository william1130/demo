/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOCheckSum;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 * 
 */
public class CheckSumParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public CheckSumParseInfo(int len) {
		super(ISOType.CHECKSUM, len);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid CHECKSUM field %d position %d", field, pos), pos,
					field);
		}
		if (pos + length > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for CHECKSUM field %d of length %d, pos %d", field, length, pos), pos, field);
		}
		byte[] _v = new byte[length];
		System.arraycopy(buf, pos, _v, 0, length);
		ISOCheckSum isoCheckSum = new ISOCheckSum();
		if ( custom != null && isNested(this.reference , (String[])((Object[])custom)[1])) {
			byte[] data = new byte[length - 1];
			System.arraycopy(buf, pos, data, 0, length - 1);
			byte checkSum = CommonFunction.checkSum(data);
			if (getAlt() != null && !getAlt().isEmpty() && getAlt().equals("no check")) {
				isoCheckSum.setCheckSum(_v[length - 1]);
			} else {
				if (checkSum != _v[length - 1]) {
					throw new ISO8583ParseException(String.format(
							"invalid check sum field %d of value actual %s, expect %s", field,
							CommonFunction.hexEncode(_v[length - 1]), CommonFunction.hexEncode(checkSum)), pos, field);
				} else {
					isoCheckSum.setCheckSum(checkSum);
				}
			}
			isoCheckSum.setFields(parseNestFields(data, reference, vo, isTraceEnable, (String[])((Object[])custom)[1]));
			return new ISOValue<ISOCheckSum>(type, isoCheckSum, length, getDescription(), true);
		} else {
			if (getAlt() != null && !getAlt().isEmpty() && getAlt().equals("no check")) {
				isoCheckSum.setCheckSum(_v[length - 1]);
			} else {
				byte checkSum = CommonFunction.checkSum((byte[])((Object[])custom)[0]);
				if (checkSum != _v[length - 1]) {
					throw new ISO8583ParseException(String.format(
							"invalid check sum field %d of value actual %s, expect %s", field,
							CommonFunction.hexEncode(_v[length - 1]), CommonFunction.hexEncode(checkSum)), pos, field);
				} else {
					isoCheckSum.setCheckSum(checkSum);
				}
			}
			return new ISOValue<ISOCheckSum>(type, isoCheckSum, 1, getDescription(), false);
		}
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		int missing = 0;
		ISOCheckSum isoCheckSum = null;
		if (value.getValue() instanceof ISOCheckSum) {
			isoCheckSum = (ISOCheckSum) value.getValue();
		} else {
			missing = value.getLength();
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
			return;
		}

		if (isNested(this.reference , (String[])((Object[])custom)[1])) {
			byte[] data = writeNestFields(isoCheckSum.getFields(), reference, vo, (String[])((Object[])custom)[1]);
			outs.write(data);
			if (isoCheckSum.getCheckSum() == 0x00
					|| (getAlt() != null && !getAlt().isEmpty() && getAlt().equals("no check"))) {
				outs.write(CommonFunction.checkSum(data));
			} else {
				outs.write(isoCheckSum.getCheckSum());
			}
		} else {
			if (isoCheckSum.getCheckSum() == 0x00) {
				outs.write(CommonFunction.checkSum((byte[])((Object[])custom)[0]));
			} else {
				outs.write(isoCheckSum.getCheckSum());
			}
		}
	}
	
	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable) throws ISO8583ParseException,
			UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid CHECKSUM field %d position %d", field, pos), pos,
					field);
		}
		if (pos + length > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for CHECKSUM field %d of length %d, pos %d", field, length, pos), pos, field);
		}
		byte[] _v = new byte[length];
		System.arraycopy(buf, pos, _v, 0, length);
		ISOCheckSum isoCheckSum = new ISOCheckSum();
		if (isNested(this.reference , (String[])((Object[])custom)[1])) {
			byte[] data = new byte[length - 1];
			System.arraycopy(buf, pos, data, 0, length - 1);
			byte checkSum = CommonFunction.checkSum(data);
			if (getAlt() != null && !getAlt().isEmpty() && getAlt().equals("no check")) {
				isoCheckSum.setCheckSum(_v[length - 1]);
			} else {
				if (checkSum != _v[length - 1]) {
					throw new ISO8583ParseException(String.format(
							"invalid check sum field %d of value actual %s, expect %s", field,
							CommonFunction.hexEncode(_v[length - 1]), CommonFunction.hexEncode(checkSum)), pos, field);
				} else {
					isoCheckSum.setCheckSum(checkSum);
				}
			}
			isoCheckSum.setFields(parseStringNestFields(data, reference, vo, isTraceEnable, (String[])((Object[])custom)[1]));
			return new ISOValue<ISOCheckSum>(type, isoCheckSum, length, getDescription(), true);
		} else {
			byte checkSum = CommonFunction.checkSum((byte[])((Object[])custom)[0]);
			if (checkSum != _v[length - 1]) {
				throw new ISO8583ParseException(String.format(
						"invalid check sum field %d of value actual %s, expect %s", field,
						CommonFunction.hexEncode(_v[length - 1]), CommonFunction.hexEncode(checkSum)), pos, field);
			} else {
				isoCheckSum.setCheckSum(checkSum);
			}
			return new ISOValue<ISOCheckSum>(type, isoCheckSum, 1, getDescription(), false);
		}
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		int missing = 0;
		ISOCheckSum isoCheckSum = null;
		if (value.getValue() instanceof ISOCheckSum) {
			isoCheckSum = (ISOCheckSum) value.getValue();
		} else {
			missing = value.getLength();
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
			return;
		}

		if (isNested(this.reference , (String[])((Object[])custom)[1])) {
			byte[] data = writeNestFields(isoCheckSum.getFields(), reference, vo, (String[])((Object[])custom)[1]);
			outs.write(data);
			if (isoCheckSum.getCheckSum() == 0x00
					|| (getAlt() != null && !getAlt().isEmpty() && getAlt().equals("no check"))) {
				outs.write(CommonFunction.checkSum(data));
			} else {
				outs.write(isoCheckSum.getCheckSum());
			}
		} else {
			if (isoCheckSum.getCheckSum() == 0x00) {
				outs.write(CommonFunction.checkSum((byte[])((Object[])custom)[0]));
			} else {
				outs.write(isoCheckSum.getCheckSum());
			}
		}
	}

}
