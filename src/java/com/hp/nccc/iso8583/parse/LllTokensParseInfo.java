/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.nccc.iso8583.core.ISO8583FieldTraceInfo;
import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583TraceInfo;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOToken;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 *
 */
public class LllTokensParseInfo extends FieldParseInfo {

	private static final Logger log = LogManager.getLogger(LllTokensParseInfo.class);

	/**
	 * @param t
	 * @param len
	 */
	public LllTokensParseInfo() {
		super(ISOType.LLLTOKENS, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		// undefined
		return null;
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		// undefined
	}

	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLTOKENS field %d pos %d", field, pos), pos,
					field);
		} else if (pos + 3 > buf.length) {
			throw new ISO8583ParseException(
					String.format("Insufficient data for LLLTOKENS header field %d pos %d", field, pos), pos,
					field);
		}
		int length = decodeStringLength(buf, pos, 3);
		if (length < 0) {
			throw new ISO8583ParseException(
					String.format("Invalid LLLTOKENS length %d field %d pos %d", length, field, pos), pos, field);
		} else if (length + pos + 3 > buf.length) {
			throw new ISO8583ParseException(
					String.format("Insufficient data for LLLTOKENS field %d, pos %d", field, pos), pos, field);
		}
		String _v;
		try {
			_v = length == 0 ? "" : new String(buf, pos + 3, length, getCharacterEncoding());
		} catch (IndexOutOfBoundsException ex) {
			throw new ISO8583ParseException(
					String.format("Insufficient data for LLLTOKENS header, field %d pos %d", field, pos), pos,
					field);
		}
		// This is new: if the String's length is different from the specified length in
		// the buffer,
		// there are probably some extended characters. So we create a String from the
		// rest of the buffer,
		// and then cut it to the specified length.
		if (_v.length() != length) {
			_v = new String(buf, pos + 3, buf.length - pos - 3, getCharacterEncoding()).substring(0, length);
		}

		List<ISOToken> subDatas;

		int offset = 0;
		// header token
		// Eye-catcher
		offset++;
		// User-FLD1
		offset++;
		// CNT
		int totCnt = Integer.parseInt(_v.substring(offset, offset + 5));
		offset += 5;
		// Length
		int lgth = Integer.parseInt(_v.substring(offset, offset + 5));
		offset += 5;
		if (length != lgth) {
			throw new ISO8583ParseException(
					String.format("LLLTokens length data error data[%d], length[%d]", lgth, length), pos, field);
		}
		String[] conditions = (String[]) custom;
		try {
			int count = 1;// include header
			subDatas = new Vector<ISOToken>();
			while (length > offset) {
				// token header
				// Eye-catcher
				offset++;
				// Filler
				offset++;
				// TKN-ID
				String tokenId = _v.substring(offset, offset + 2);// token id
				offset += 2;
				// token length
				int tokenLength = Integer.parseInt(_v.substring(offset, offset + 5));
				offset += 5;
				// Filler
				offset++;

				String tokenData = _v.substring(offset, offset + tokenLength);
				offset += tokenLength;
				ISOToken token = new ISOToken();
				token.setTknId(tokenId);
				// token.setLgth(tokenLength);

				Map<Integer, FieldParseInfo> fpis = this.getMapProvider().getFieldsParseInfo(tokenId, conditions);
				if (fpis == null) {
					log.warn(String.format(
							"Insufficient data for LLLTOKENS field %d, pos %d requires token id %s defind in configuration",
							field, pos, tokenId));
					String desc = String.format("unknow token[%s]", tokenId);
					ISO8583Fields fields = new ISO8583Fields();
					fields.setValue(1, tokenData, ISOType.ALPHA, tokenLength, desc);
					token.setFields(fields);
					token.setDescription(desc);
				} else {
					// calculate fields number by length
					// JTV - 20171012 - Seth
//						String fillIn = this.getMapProvider().getDesc(tokenId, tokenId, "fill-in");
//						if ((tokenLength % 2 != 0 && "odd".equals(fillIn))
//								|| (tokenLength % 2 == 0 && "even".equals(fillIn))) {
//							if (" ".equals(tokenData.substring(tokenLength - 1, tokenLength))) {
//								tokenData = tokenData.substring(0, tokenLength - 1);
//								tokenLength--;
//							}
//						}
					token.setFields(this.parseStringNestFields(tokenId, tokenData.getBytes(getCharacterEncoding()),
							tokenLength, vo, isTraceEnable, conditions));
					token.setDescription(this.getMapProvider().getDesc(tokenId, conditions));
				}

				subDatas.add(token);
				count++;
			}

			if (totCnt != count) {
				throw new ISO8583ParseException(
						String.format("LLLTokens totCnt data error totCnt[%d], count[%d]", totCnt, count), pos,
						field);
			}
		} catch (ISO8583ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new ISO8583ParseException(String
					.format("parse data for LLLTOKENS field %d, pos %d exception:[%s]", field, pos, e.getMessage()),
					pos, field);
		}

		return new ISOValue<List<ISOToken>>(type, subDatas, length, description, true);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
//		writeStringLengthHeader(value.getLength(), outs, 3);
//		//write header
//		//Eye-catcher
//		outs.write("&".getBytes());
//		//User-FLD1
//		outs.write(" ".getBytes());
//		@SuppressWarnings("unchecked")
//		List<ISOToken> subDatas = (List<ISOToken>) value.getValue();
//		//CNT
//		outs.write(String.format("%05d", (subDatas.size() + 1)).getBytes());//include header token 
//		//Length
//		outs.write(String.format("%05d", value.getLength()).getBytes());

		@SuppressWarnings("unchecked")
		List<ISOToken> subDatas = (List<ISOToken>) value.getValue();

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		byte[] result = null;
		String[] conditions = (String[]) custom;
		int totallen = 0;
		for (ISOToken token : subDatas) {
			String tokenId = token.getTknId();
			byte[] data = writeStringNestFields(token.getFields(), tokenId, vo, conditions);
			if (token.getLgth() <= 0) {
				String _v = new String(data, this.getCharacterEncoding());
				token.setLgth(_v.length());
			}
			try {
				// JTV - 20171012 - Seth
				String fillIn = this.getMapProvider().getDesc(tokenId, tokenId, "fill-in");
				if ((token.getLgth() % 2 == 0 && "odd".equals(fillIn))
						|| (token.getLgth() % 2 != 0 && "even".equals(fillIn))) {
					token.setLgth(token.getLgth() + 1);
					String _v = new String(data, this.getCharacterEncoding()).concat(" ");
					data = _v.getBytes(this.getCharacterEncoding());
				}
				// Eye-catcher
				buffer.write("!".getBytes());
				totallen++;
				// Filler
				buffer.write(" ".getBytes());
				totallen++;
				// TKN-ID
				buffer.write(tokenId.getBytes());
				totallen = totallen + 2;
				// Length
				buffer.write(String.format("%05d", token.getLgth()).getBytes());
				totallen = totallen + 5;
				totallen = totallen + token.getLgth();
				// Filler
				buffer.write(" ".getBytes());
				totallen++;
				// token data
				buffer.write(data);
			} catch (IOException e) {
				// can not happen
			}
		}
		result = buffer.toByteArray();
		try {
			buffer.close();
		} catch (IOException e) {
			// can not happen
		}
		// write header
		totallen = totallen + 1 + 1 + 5 + 5;
		writeStringLengthHeader(totallen, outs, 3);
		// Eye-catcher
		outs.write("&".getBytes());
		// User-FLD1
		outs.write(" ".getBytes());
		// CNT
		outs.write(String.format("%05d", (subDatas.size() + 1)).getBytes());// include header token
		// Length
		outs.write(String.format("%05d", totallen).getBytes());

		outs.write(result);
	}

	protected ISO8583Fields parseStringNestFields(String id, byte[] data, Object custom, ISO8583VO vo,
			boolean isTraceEnable, String... conditions) throws ISO8583ParseException, UnsupportedEncodingException {
		List<ISO8583FieldTraceInfo> traceInfos = null;
		if (isTraceEnable) {
			traceInfos = new ArrayList<ISO8583FieldTraceInfo>();
		}
		Map<Integer, FieldParseInfo> fpis = this.getMapProvider().getFieldsParseInfo(id, conditions);
		ISO8583Fields fields = new ISO8583Fields();
		List<Integer> index = new ArrayList<Integer>();
		index.addAll(fpis.keySet());
		Collections.sort(index);
		int pos = 0;
		for (Integer i : index) {
			FieldParseInfo fpi = fpis.get(i);
			ISO8583FieldTraceInfo field = null;
			if (isTraceEnable) {
				field = new ISO8583FieldTraceInfo();
				field.setBinary(false);
				field.setStartPos(pos);
				field.setConfigParseId(this.getMapProvider().getFieldsParseKey(id, conditions));
				field.setDesc(fpi.getDescription());
				field.setField(i);
			}
			try {
				fpi.setFillIn(this.getMapProvider().getDesc(id, id, "fill-in"));
				ISOValue<?> val = null;
				if (fpi.getType() == ISOType.LVAR || fpi.getType() == ISOType.VAR) {
					val = fpi.parseString(i, data, pos, vo, new Object[] { custom, conditions }, isTraceEnable);
				} else if (fpi.getType() == ISOType.B24TLV) {
					val = fpi.parseString(i, data, pos, vo, new Object[] { id, conditions }, isTraceEnable);
				} else {
					val = fpi.parseString(i, data, pos, vo, conditions, isTraceEnable);
				}
				fields.setField(i, val);

				pos += fpi.getLength();
				if (val.getType() == ISOType.LLVAR || val.getType() == ISOType.LLBIN || val.getType() == ISOType.LLZ) {
					pos += 2;
				} else if (val.getType() == ISOType.LLLVAR || val.getType() == ISOType.LLLBIN
						|| val.getType() == ISOType.LLLTABLES || val.getType() == ISOType.LLLCHIP) {
					pos += 3;
				} else if (val.getType() == ISOType.LVAR) {
					pos += fpi.getLength();
				}
			} catch (ISO8583ParseException e) {
				if (isTraceEnable) {
					ISO8583TraceInfo info = processTokenPos(data, traceInfos, field);
					ISO8583TraceInfo subInfo = e.getTraceInfo();
					if (subInfo != null) {
						for (ISO8583FieldTraceInfo subFieldInfo : subInfo.getTraceInfos()) {
							subFieldInfo.setSuperConfigParseId(this.getMapProvider().getFieldsParseKey(id, conditions));
							subFieldInfo.setSuperField(i);
						}
					}
					info.setTraceInfo(subInfo);
					e.setTraceInfo(info);
				}
				throw e;
			} catch (Exception e) {
				ISO8583ParseException ie = new ISO8583ParseException(
						String.format("exception [%s] field %d", e.getMessage(), i), pos, i);
				if (isTraceEnable) {
					ISO8583TraceInfo info = processTokenPos(data, traceInfos, field);
					ie.setTraceInfo(info);
				}
				throw ie;
			}
			if (isTraceEnable) {
				field.setEndPos(pos - 1);
				int length = field.getEndPos() - field.getStartPos() + 1;
				if (length > data.length - field.getStartPos()) {
					length = data.length - field.getStartPos();
				}
				byte[] tempByte = new byte[length];
				System.arraycopy(data, field.getStartPos(), tempByte, 0, length);
				field.setByteData(tempByte);
				field.setValue(fields.getField(i));
				traceInfos.add(field);
			}
			if (pos == data.length) {
				break;
			}
		}
		return fields;
	}

	private ISO8583TraceInfo processTokenPos(byte[] data, List<ISO8583FieldTraceInfo> traceInfos,
			ISO8583FieldTraceInfo field) {
		field.setEndPos(data.length - 1);
		int length = field.getEndPos() - field.getStartPos() + 1;
		if (length > data.length - field.getStartPos()) {
			length = data.length - field.getStartPos();
		}
		byte[] tempByte = new byte[length];
		System.arraycopy(data, field.getStartPos(), tempByte, 0, length);
		field.setByteData(tempByte);
		traceInfos.add(field);
		ISO8583TraceInfo info = new ISO8583TraceInfo();
		info.setTraceInfos(traceInfos);
		return info;
	}

	@Override
	public byte[] writeStringNestFields(ISO8583Fields fields, String id, ISO8583VO vo, String... conditions)
			throws IOException {
		Map<Integer, FieldParseInfo> fpis = this.getMapProvider().getFieldsParseInfo(id, conditions);
		if (fpis == null) {
			String data = fields.getField(1).toString();
			return data.getBytes();
		} else {
			List<Integer> index = new ArrayList<Integer>();
			index.addAll(fpis.keySet());
			Collections.sort(index);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			for (Integer i : index) {
				ISOValue<?> v = fields.getField(i);
				FieldParseInfo fpi = fpis.get(i);
				if (v != null) {
					try {
						fpi.writeString(v, bout, vo, conditions);
					} catch (IOException ex) {
						// should never happen, writing to a ByteArrayOutputStream
					}
				}
			}
			return bout.toByteArray();
		}
	}
}
