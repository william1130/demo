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
public class BinaryParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public BinaryParseInfo(int len) {
		super(ISOType.BINARY, len);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
            throw new ISO8583ParseException(String.format("Invalid BINARY field %d position %d",
                      field, pos), pos, field);
        }
        if (pos+length > buf.length) {
            throw new ISO8583ParseException(String.format(
                      "Insufficient data for BINARY field %d of length %d, pos %d",
                field, length, pos), pos, field);
        }
		byte[] _v = new byte[length];
		System.arraycopy(buf, pos, _v, 0, length);
		if(isNested(this.reference , (String[])custom)){
			return new ISOValue<ISO8583Fields>(type, parseNestFields(_v, reference, vo, isTraceEnable, (String[])custom), length, getDescription(), true);
		}else{
			return new ISOValue<byte[]>(type, _v, length, getDescription(), false);
		}
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		int missing = 0;
		int length = this.length == 0 ? varLength : this.length;
		if (value.getValue() instanceof byte[]) {
			outs.write((byte[]) value.getValue());
			missing = length - ((byte[]) value.getValue()).length;
		} else if(value.getValue() instanceof ISO8583Fields){
			byte[] _v = this.writeNestFields((ISO8583Fields)value.getValue(), reference, vo, (String[])custom);
			outs.write(_v);
			missing = length - (_v).length;
		} else {
			byte[] binval = CommonFunction.hexDecode(value.getValue().toString());
			outs.write(binval);
			missing = length - binval.length;
		}
		if (type == ISOType.BINARY && missing > 0) {
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
		}
	}
	
	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid BINARY field %d position %d",
                    field, pos), pos, field);
		}
		if (pos+(length*2) > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for BINARY field %d of length %d, pos %d",
				field, length, pos), pos, field);
		}
		byte[] binval = CommonFunction.hexDecode(new String(buf, pos, length*2));
		if(isNested(this.reference , (String[])custom)){
			return new ISOValue<ISO8583Fields>(type, parseStringNestFields(binval, reference, vo, isTraceEnable, (String[])custom), length, getDescription(), true);
		}else{
			return new ISOValue<byte[]>(type, binval, binval.length, getDescription(), false);
		}
		
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		int missing = 0;
		int length = this.length == 0 ? varLength : this.length;
		if (value.getValue() instanceof byte[]) {
			outs.write((byte[]) value.getValue());
			missing = length - ((byte[]) value.getValue()).length;
		} else if(value.getValue() instanceof ISO8583Fields){
			byte[] _v = this.writeNestFields((ISO8583Fields)value.getValue(), reference, vo, (String[])custom);
			outs.write(_v);
			missing = length - (_v).length;
		} else {
			byte[] binval = CommonFunction.hexDecode(value.getValue().toString());
			outs.write(binval);
			missing = length - binval.length;
		}
		if (type == ISOType.BINARY && missing > 0) {
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
		}
	}
}
