/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
public class ZipParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public ZipParseInfo() {
		super(ISOType.ZIP, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0 || pos >= buf.length) {
			throw new ISO8583ParseException(String.format("Invalid CHECKSUM field %d position %d", field, pos), pos,
					field);
		}

		int l = buf.length - pos;
		int length = l;

		byte[] _v = new byte[length];
		System.arraycopy(buf, pos, _v, 0, length);

		try {
			byte[] unZipData = processUnZip(_v);
			return new ISOValue<ISO8583Fields>(type,
					parseNestFields(unZipData, reference, vo, isTraceEnable, (String[]) ((Object[]) custom)), unZipData.length,
					getDescription(), true);
		} catch (IOException e) {
			throw new ISO8583ParseException(
					String.format("invalid upzip field %d of value %s", field, CommonFunction.bytesToHex(_v)), pos,
					field);
		}
	}

	private byte[] processUnZip(byte[] zipData) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
		GZIPInputStream gzis = new GZIPInputStream(bais);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
        while ((len = gzis.read(buffer)) > 0) {
        	baos.write(buffer, 0, len);
        }
 
        gzis.close();
        
        byte[] data = baos.toByteArray();
        baos.close();
		return data;
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		byte[] subs = writeNestFields((ISO8583Fields)value.getValue(), this.getReference(), vo, (String[]) custom);
		byte[] zipData = processZip(subs);
		outs.write(zipData);
	}
	
	private byte[] processZip(byte[] data) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzos = new GZIPOutputStream(baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		int byteNo1;
		byte[] b1 = new byte[64];
		while ((byteNo1 = bais.read(b1)) > 0) {
			gzos.write(b1, 0, byteNo1);
		}
		gzos.finish();
		bais.close();
		byte[] output =  baos.toByteArray();
    	gzos.close();
		return output;
	}

	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException {
		//not implement yet
		return null;
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		//not implement yet
	}

}
