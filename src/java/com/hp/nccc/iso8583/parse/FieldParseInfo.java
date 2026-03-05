/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.B24TlvParseInfos;
import com.hp.nccc.iso8583.core.ISO8583FieldTraceInfo;
import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583TraceInfo;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 * 
 */
public abstract class FieldParseInfo {

	protected ISOType type;
	protected final int length;
	protected String description;
	private String encoding = System.getProperty("file.encoding");
	protected boolean forceStringDecoding;
	protected String reference;
	protected ParseMapProvider mapProvider;
	protected B24TlvParseInfos sbuMapProvider;
	private String alt;
	private String fillIn;
	
	protected int varLength;
	
	public void setVarLength(int varLength) {
		this.varLength = varLength;
	}
	
	public int getVarLength() {
		return this.varLength;
	}

	/**
	 * Creates a new instance that parses a value of the specified type, with
	 * the specified length.
	 * The length is only useful for ALPHA and NUMERIC types.
	 * 
	 * @param t
	 *            The ISO type to be parsed.
	 * @param len
	 *            The length of the data to be read (useful only for ALPHA and
	 *            NUMERIC types).
	 */
	public FieldParseInfo(ISOType t, int len) {
		if (t == null) {
			throw new IllegalArgumentException("ISOType cannot be null");
		}
		type = t;
		length = len;
		description = "";
	}

	/**
	 * Creates a new instance that parses a value of the specified type, with
	 * the specified length.
	 * The length is only useful for ALPHA and NUMERIC types.
	 * 
	 * @param t
	 *            The ISO type to be parsed.
	 * @param len
	 *            The length of the data to be read (useful only for ALPHA and
	 *            NUMERIC types).
	 */
	public FieldParseInfo(ISOType t, int len, String desc) {
		if (t == null) {
			throw new IllegalArgumentException("ISOType cannot be null");
		}
		type = t;
		length = len;
		description = desc;
	}

	/**
	 * Specified whether length headers for variable-length fields in text mode
	 * should
	 * be decoded using proper string conversion with the character encoding.
	 * Default is false,
	 * which means use the old behavior of decoding as ASCII.
	 */
	public void setForceStringDecoding(boolean flag) {
		forceStringDecoding = flag;
	}

	public void setCharacterEncoding(String value) {
		encoding = value;
	}

	public String getCharacterEncoding() {
		return encoding;
	}

	/** Returns the specified length for the data to be parsed. */
	public int getLength() {
		return length;
	}
	
//	public void setLength(int length) {
//		this.length = length;
//	}

	public String getDescription () {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	/** Returns the data type for the data to be parsed. */
	public ISOType getType() {
		return type;
	}

	/**
	 * Parses the character data from the buffer and returns the
	 * ISOValue with the correct data type in it.
	 * 
	 * @param field
	 *            The field index, useful for error reporting.
	 * @param buf
	 *            The full ISO message buffer.
	 * @param pos
	 *            The starting position for the field data.
	 * @param custom
	 *            custom field.
	 */
	public abstract <T> ISOValue<?> parse(final int field, byte[] buf, int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException;

	public abstract <T> void write(ISOValue<T> value, final OutputStream outs, ISO8583VO vo, Object custom)
			throws IOException;

	public abstract <T> ISOValue<?> parseString(final int field, byte[] buf, int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException;

	public abstract <T> void writeString(ISOValue<T> value, final OutputStream outs, ISO8583VO vo, Object custom)
			throws IOException;

	/**
	 * Returns a new FieldParseInfo instance that can parse the specified type.
	 */
	public static FieldParseInfo getInstance(ISOType t, int len, String encoding, String alt, String desc, String ref) {
		FieldParseInfo fpi = null;
		switch (t) {
		case ALPHA:
			fpi = new AlphaParseInfo(len);
			break;
		case AMOUNT:
			fpi = new AmountParseInfo();
			break;
		case BINARY:
			fpi = new BinaryParseInfo(len);
			break;
		case CHINESE:
			fpi = new ChineseParseInfo(len);
			break;
		case DATE10:
			fpi = new Date10ParseInfo();
			break;
		case DATE4:
			fpi = new Date4ParseInfo();
			break;
		case DATE_EXP:
			break;
		case LLBIN:
			fpi = new LlbinParseInfo();
			break;
		case LLLBIN:
			fpi = new LllbinParseInfo();
			break;
		case LLLVAR:
			fpi = new LllvarParseInfo();
			break;
		case LLVAR:
			fpi = new LlvarParseInfo();
			break;
		case NUMERIC:
			fpi = new NumericParseInfo(len);
			break;
		case TIME:
			fpi = new TimeParseInfo();
			break;
		case LLZ:
			fpi = new LlzParseInfo();
			break;
		case LLLTABLES:
			fpi = new LllTablesParseInfo();
			break;
		case LLLCHIP:
			fpi = new LllChipParseInfo();
			break;
		case POSKITZTAG:
			fpi = new PoskitzTagParseInfo();
			break;
		case CHECKSUM:
			fpi = new CheckSumParseInfo(len);
			break;
		case LBINARY:
			fpi = new LBinaryParseInfo();
			break;
		case LLLTOKENS:
			fpi = new LllTokensParseInfo();
			break;
		case LLLLTOKENS:
			fpi = new LlllTokensParseInfo();
			break;
		case LVAR:
			fpi = new LVarParseInfo(len);
			break;
		case VAR:
			fpi = new VarParseInfo(len);
			break;
		case SMARTPAYCHIP:
			fpi = new SmartPayChipParseInfo();
			break;
		case SMARTPAYTAG:
			fpi = new SmartPayTagParseInfo();
			break;
		case REDEF:
			fpi = new RedefineParseInfo();
			break;
		case LLLBITMAPVAR:
			fpi = new LllBitMapVarParseInfo();
			break;
		case ZIP:
			fpi = new ZipParseInfo();
			break;
		case B24TLV:
			fpi = new B24TlvParseInfo();
			break;
		case EBCDIC:
			fpi = new EbcdicParseInfo(len);
			break;
		case LLBINHEX:
			fpi = new LlbinHexParseInfo();
			break;
		case LLLBINHEX:
			fpi = new LllbinHexParseInfo();
			break;
		default:
			throw new IllegalArgumentException(String.format("Cannot parse type %s", t));
		}

		fpi.setDescription(desc);
		fpi.setCharacterEncoding(encoding);
		fpi.setReference(ref);
		fpi.setAlt(alt);
		return fpi;
	}

	protected int decodeLength(byte[] buf, int pos, int digits) throws UnsupportedEncodingException {
		if (digits == 3) {
			return ( (((buf[pos] & 0xf0) >> 4) * 1000) + (buf[pos] & 0x0f) * 100) + (((buf[pos + 1] & 0xf0) >> 4) * 10) + (buf[pos + 1] & 0x0f);
		} else {
			return (((buf[pos] & 0xf0) >> 4) * 10) + (buf[pos] & 0x0f);
		}
	}
	
	protected int decodeHexLength(byte[] buf, int pos, int digits) throws UnsupportedEncodingException {
		if (digits >= 3) {
			return ( (((buf[pos] & 0xf0) >> 4) * 16 * 16 * 16) + (buf[pos] & 0x0f) * 16 * 16) + (((buf[pos + 1] & 0xf0) >> 4) * 16) + (buf[pos + 1] & 0x0f);
		} else {
			return (((buf[pos] & 0xf0) >> 4) * 16) + (buf[pos] & 0x0f);
		}
	}

	protected int decodeStringLength(byte[] buf, int pos, int digits) throws UnsupportedEncodingException {
		int l = Integer.parseInt(new String(buf, pos, digits));
		return l;
	}
	
	protected int decodeStringHexLength(byte[] buf, int pos, int digits) throws UnsupportedEncodingException {
		int l = Integer.parseInt(new String(buf, pos, digits), 16);
		
		return l;
	}

	protected void writeLengthHeader(final int l, final OutputStream outs, final int digits) throws IOException {
		if (digits == 4) {
			int l2 = l/100;
			outs.write((((l2 % 100) / 10) << 4) | (l2 % 10));
		}
		
		if (digits == 3) {
			outs.write(l / 100); // 00 to 09 automatically in BCD
		}
		// BCD encode the rest of the length
		outs.write((((l % 100) / 10) << 4) | (l % 10));
	}
	
	protected void writeLengthHexHeader(final int l, final OutputStream outs, final int digits) throws IOException {
		if (digits == 4) {
			int l2 = l/(16 * 16);
			outs.write((((l2 % (16 * 16)) / 16) << 4) | (l2 % 16));
		}
		
		if (digits == 3) {
			outs.write(l / (16 * 16)); 
		}
		// HEX encode the rest of the length
		outs.write((((l % (16 * 16)) / 16) << 4) | (l % 16));
	}

	protected void writeStringLengthHeader(final int l, final OutputStream outs, final int digits) throws IOException {
		if (digits == 4) {
			outs.write(String.valueOf(l / 1000).getBytes());
			outs.write(String.valueOf((l / 100) % 10).getBytes());
		}
		if (digits == 3) {
			outs.write(String.valueOf(l / 100).getBytes());
		}
		outs.write(String.format("%02d", l % 100).getBytes());
	}
	
	protected static void writeHexStringLengthHeader(final int l, final OutputStream outs, final int digits) throws IOException {
		String hexLen = CommonFunction.intToHex(l);
		String padding0 = "";
		for(int i = 0; i < digits; i++) {
			padding0 = padding0+"0";
		}
		outs.write( (padding0.substring(0, digits-hexLen.length())+hexLen).getBytes());
	}

	public ParseMapProvider getMapProvider() {
		return mapProvider;
	}

	public void setMapProvider(ParseMapProvider mapProvider) {
		this.mapProvider = mapProvider;
	}

	public B24TlvParseInfos getSbuMapProvider() {
		return sbuMapProvider;
	}

	public void setSbuMapProvider(B24TlvParseInfos sbuMapProvider) {
		this.sbuMapProvider = sbuMapProvider;
	}

	public boolean isNested(String id, String... conditions) {
		if (id == null) {
			return false;
		}
		if (this.mapProvider == null) {
			return false;
		}
		if (mapProvider.getFieldsParseInfo(id, conditions) == null) {
			return false;
		}
		return true;
	}

	public ISO8583Fields parseNestFields(byte[] data, String id, ISO8583VO vo, boolean isTraceEnable, BitSet bitmap,
			String... conditions) throws ISO8583ParseException, UnsupportedEncodingException {
		List<ISO8583FieldTraceInfo> traceInfos = null;
		if (isTraceEnable) {
			traceInfos = new ArrayList<ISO8583FieldTraceInfo>();
		}
		Map<Integer, FieldParseInfo> fpis = mapProvider.getFieldsParseInfo(id, conditions);
		ISO8583Fields fields = new ISO8583Fields();
		List<Integer> index = new ArrayList<Integer>();
		index.addAll(fpis.keySet());
		Collections.sort(index);
		int pos = 0;
		for (Integer i : index) {
			if(bitmap == null || bitmap.get(i-1)){
				FieldParseInfo fpi = fpis.get(i);
				ISO8583FieldTraceInfo field = null;
				if (isTraceEnable) {
					field = new ISO8583FieldTraceInfo();
					field.setBinary(true);
					field.setStartPos(pos);
					field.setConfigParseId(mapProvider.getFieldsParseKey(id, conditions));
					field.setDesc(fpi.getDescription());
					field.setField(i);
				}
				try {
					ISOValue<?> val = null;
					if (fpi.getType() == ISOType.CHECKSUM) {
						if (fpi.isNested(fpi.getReference(), conditions)) {
							val = fpi.parse(i, data, pos, vo, new Object[] { null, conditions }, isTraceEnable);
						} else {
							String s = fpi.getAlt();
							if (s != null && s.equals("no check")) {
								val = fpi.parse(i, data, pos, vo, null, isTraceEnable);
							} else {
								String[] nums = s.split("-");
								byte[] subData = getCheckSumBytes(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]), fpis,
										index, data);
								val = fpi.parse(i, data, pos, vo, new Object[] { subData, conditions }, isTraceEnable);
							}
						}
					} else {
						val = fpi.parse(i, data, pos, vo, conditions, isTraceEnable);
					}
					if (val != null) {
						if (val.getType() == ISOType.NUMERIC || val.getType() == ISOType.DATE10
								|| val.getType() == ISOType.DATE4 || val.getType() == ISOType.DATE_EXP
								|| val.getType() == ISOType.AMOUNT || val.getType() == ISOType.TIME) {
							pos += (val.getLength() / 2) + (val.getLength() % 2);
						} else {
							pos += val.getLength();
						}
						if (val.getType() == ISOType.LLVAR || val.getType() == ISOType.LLBIN || val.getType() == ISOType.LLBINHEX
								|| val.getType() == ISOType.LLZ) {
							pos++;
						} else if (val.getType() == ISOType.LLLVAR || val.getType() == ISOType.LLLBIN || val.getType() == ISOType.LLLBINHEX
								|| val.getType() == ISOType.LLLTABLES || val.getType() == ISOType.LLLCHIP
								|| val.getType() == ISOType.LLLBITMAPVAR) {
							pos += 2;
						}
					}
					fields.setField(i, val);
				} catch (ISO8583ParseException e) {
					if (isTraceEnable) {
						ISO8583TraceInfo info = processPos(data, traceInfos, field);
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
						ISO8583TraceInfo info = processPos(data, traceInfos, field);
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
		}
		return fields;
	}

	public ISO8583Fields parseNestFields(byte[] data, String id, ISO8583VO vo, boolean isTraceEnable,
			String... conditions) throws ISO8583ParseException, UnsupportedEncodingException {
		return parseNestFields(data, id, vo, isTraceEnable, null, conditions);
	}

	public ISO8583Fields parseStringNestFields(byte[] data, String id, ISO8583VO vo, boolean isTraceEnable,
			String... conditions) throws ISO8583ParseException, UnsupportedEncodingException {
		List<ISO8583FieldTraceInfo> traceInfos = null;
		if (isTraceEnable) {
			traceInfos = new ArrayList<ISO8583FieldTraceInfo>();
		}
		Map<Integer, FieldParseInfo> fpis = mapProvider.getFieldsParseInfo(id, conditions);
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
				field.setConfigParseId(mapProvider.getFieldsParseKey(id, conditions));
				field.setDesc(fpi.getDescription());
				field.setField(i);
			}
			try {
				ISOValue<?> val = fpi.parseString(i, data, pos, vo, conditions, isTraceEnable);
				fields.setField(i, val);

				pos += val.toString().getBytes(fpi.getCharacterEncoding()).length;
				if (val.getType() == ISOType.LLVAR || val.getType() == ISOType.LLBIN || val.getType() == ISOType.LLBINHEX || val.getType() == ISOType.LLZ) {
					pos += 2;
				} else if (val.getType() == ISOType.LLLVAR || val.getType() == ISOType.LLLBIN || val.getType() == ISOType.LLLBINHEX
						|| val.getType() == ISOType.LLLTOKENS) {
					pos += 3;
				} else if (val.getType() == ISOType.LLLLTOKENS) {
					pos += 4;
				}
			} catch (ISO8583ParseException e) {
				if (isTraceEnable) {
					ISO8583TraceInfo info = processPos(data, traceInfos, field);
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
					ISO8583TraceInfo info = processPos(data, traceInfos, field);
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

	private ISO8583TraceInfo processPos(byte[] data, List<ISO8583FieldTraceInfo> traceInfos,
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
		;
		info.setTraceInfos(traceInfos);
		return info;
	}

	public byte[] writeNestFields(ISO8583Fields fields, String id, ISO8583VO vo, String... conditions)
			throws IOException {
		Map<Integer, FieldParseInfo> fpis = mapProvider.getFieldsParseInfo(id, conditions);
		List<Integer> index = new ArrayList<Integer>();
		index.addAll(fpis.keySet());
		Collections.sort(index);

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for (Integer i : index) {
			ISOValue<?> v = fields.getField(i);
			FieldParseInfo fpi = fpis.get(i);
			if (v != null) {
				if (fpi.getType() == ISOType.CHECKSUM) {
					if (fpi.isNested(fpi.getReference(), conditions)) {
						fpi.write(v, bout, vo, new Object[] { null, conditions });
					} else {
						String s = fpi.getAlt();
						String[] nums = s.split("-");
						byte[] subData = getCheckSumBytes(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]), fpis,
								index, fields, vo, conditions);
						fpi.write(v, bout, vo, new Object[] { subData, conditions });
					}
				}else{
					try {
						fpi.write(v, bout, vo, conditions);
					} catch (IOException ex) {
						// should never happen, writing to a ByteArrayOutputStream
					}
				}
			}
		}
		byte[] data = bout.toByteArray();
		bout.close();
		return data;
	}

	public byte[] writeStringNestFields(ISO8583Fields fields, String id, ISO8583VO vo, String... conditions)
			throws IOException {
		Map<Integer, FieldParseInfo> fpis = mapProvider.getFieldsParseInfo(id, conditions);
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

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public byte[] getCheckSumBytes(int startNum, int endNum, Map<Integer, FieldParseInfo> fpis, List<Integer> index,
			byte[] data) {
		int pos = 0;
		int startPos = 0, endPos = 0;
		for (Integer i : index) {
			if (i == startNum) {
				startPos = pos;
			}
			FieldParseInfo fpi = fpis.get(i);
			if (fpi.getType() == ISOType.NUMERIC || fpi.getType() == ISOType.DATE10 || fpi.getType() == ISOType.DATE4
					|| fpi.getType() == ISOType.DATE_EXP || fpi.getType() == ISOType.AMOUNT
					|| fpi.getType() == ISOType.TIME) {
				pos += (fpi.getLength() / 2) + (fpi.getLength() % 2);
			} else {
				pos += fpi.getLength();
			}
			if (fpi.getType() == ISOType.LLVAR || fpi.getType() == ISOType.LLBIN || fpi.getType() == ISOType.LLZ) {
				pos++;
			} else if (fpi.getType() == ISOType.LLLVAR || fpi.getType() == ISOType.LLLBIN
					|| fpi.getType() == ISOType.LLLTABLES || fpi.getType() == ISOType.LLLCHIP) {
				pos += 2;
			}

			if (pos == data.length || i == endNum) {
				endPos = pos;
				break;
			}
		}

		byte[] subData = new byte[endPos - startPos];
		System.arraycopy(data, startPos, subData, 0, endPos - startPos);
		return subData;
	}

	public byte[] getCheckSumBytes(int startNum, int endNum, Map<Integer, FieldParseInfo> fpis, List<Integer> index,
			ISO8583Fields fields, ISO8583VO vo, String... conditions) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for (Integer i : index) {
			if (i < startNum) {
				continue;
			}
			ISOValue<?> v = fields.getField(i);
			FieldParseInfo fpi = fpis.get(i);
			try {
				fpi.write(v, bout, vo, conditions);
			} catch (IOException ex) {
				// should never happen, writing to a ByteArrayOutputStream
			}
			if (i == endNum) {
				break;
			}
		}
		return bout.toByteArray();
	}
	
	private boolean forceb2 = false;
	
	/** Creates a BitSet for the bitmap. */
	public BitSet createBitmapBitSet(ISO8583Fields m) {
		BitSet bs = new BitSet(forceb2 ? 128 : 64);
		for (int i = 2; i < 129; i++) {
			if (m.getField(i) != null) {
				bs.set(i - 1);
			}
		}
		if (forceb2) {
			bs.set(0);
		} else if (bs.length() > 64) {
			// Extend to 128 if needed
			BitSet b2 = new BitSet(128);
			b2.or(bs);
			bs = b2;
			bs.set(0);
		}
		return bs;
	}

	public boolean isForceb2() {
		return forceb2;
	}

	public void setForceb2(boolean forceb2) {
		this.forceb2 = forceb2;
	}

	public String getFillIn() {
		return fillIn;
	}

	public void setFillIn(String fillIn) {
		this.fillIn = fillIn;
	}

}
