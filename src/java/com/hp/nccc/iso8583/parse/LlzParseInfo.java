/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 *
 */
public class LlzParseInfo extends FieldParseInfo {
	
	/**
	 * @param t
	 * @param len
	 */
	public LlzParseInfo() {
		super(ISOType.LLZ, 0);
	}

	/* (non-Javadoc)
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLZ field %d position %d",
                    field, pos), pos, field);
		} else if (pos+1 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient bin LLZ header field %d",
                    field), pos, field);
		}
		final int l = decodeLength(buf, pos, 2);
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLZ length %d pos %d", length, pos), pos, field);
		}
		if (l+pos+1 > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for bin LLZ field %d, pos %d: need %d, only %d available",
                    field, pos, l, buf.length), pos, field);
		}
		String dest = CommonFunction.Bcd.decodeToHexString(buf, pos, l);
		if(getAlt() != null && getAlt().startsWith("prefix")){
			 if ( (l % 2) != 0 )
			 { dest = dest.substring(1,dest.length()); }
		}else{
			if ( (l % 2) != 0 )
			 { dest = dest.substring(0,dest.length()-1); }
		}
		int length = dest.length() / 2 + dest.length() % 2;
		return new ISOValue<String>(type, dest, length, getDescription(), false);
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		boolean fixFlag = ((String)value.getValue()).length()%2 == 1;
		int fixLength = ((String)value.getValue()).length() + (fixFlag?1:0);
		if(fixFlag){
			fixLength = ((String)value.getValue()).length() + 1;
		}
		writeLengthHeader(((String)value.getValue()).length(), outs, 2);
		String fixS = "0";
		byte[] buf = null;
		if(fixFlag){
			if(getAlt() != null && getAlt().startsWith("prefix")){
				String[] tmp = getAlt().split(":");
				if(tmp.length > 1){
					fixS = tmp[1];
				}
				buf = CommonFunction.hexDecode(fixS + (String)value.getValue());
			}else{
				if(getAlt().startsWith("posfix")){
					String[] tmp = getAlt().split(":");
					if(tmp.length > 1){
						fixS = tmp[1];
					}
				}
				buf = CommonFunction.hexDecode((String)value.getValue() + fixS);
			}
		}else{
			buf = new byte[fixLength/2];
			buf = CommonFunction.hexDecode((String)value.getValue());
		}
		
		outs.write(buf);
	}
	
	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLZ field %d position %d",
                    field, pos), pos, field);
		} else if (pos+1 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient bin LLZ header field %d",
                    field), pos, field);
		}
		int length = decodeStringLength(buf, pos, 2);
		if (length < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLZ length %d pos %d", length, pos), pos, field);
		}
		if (length+pos+2 > buf.length) {
			throw new ISO8583ParseException(String.format(
                    "Insufficient data for bin LLZ field %d, pos %d: need %d, only %d available",
                    field, pos, length, buf.length), pos, field);
		}
		String dest = CommonFunction.Bcd.decodeToHexString(buf, pos+2, length);
//		if(getAlt() != null && getAlt().startsWith("prefix")){
//			 if ( (length % 2) != 0 )
//			 { dest = dest.substring(1,dest.length()); }
//		}else{
//			if ( (length % 2) != 0 )
//			 { dest = dest.substring(0,dest.length()-1); }
//		}
//		length = dest.length() / 2 + dest.length() % 2;
		return new ISOValue<String>(type, dest, length, getDescription(), false);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		writeStringLengthHeader(((String)value.getValue()).length(), outs, 2);
		byte[] buf = CommonFunction.hexDecode((String)value.getValue());		
		outs.write(buf);
	}
}
