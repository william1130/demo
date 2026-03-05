package com.hp.nccc.iso8583.parse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583FieldTraceInfo;
import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583TraceInfo;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOTable;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

public class LllTablesParseInfo extends FieldParseInfo {

	/**
	 * @param t
	 * @param len
	 */
	public LllTablesParseInfo() {
		super(ISOType.LLLTABLES, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.nccc.iso8583.parse.FieldParseInfo#parse(int, byte[], int)
	 */
	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLTABLES field %d pos %d", field, pos), pos, field);
		} else if (pos + 3 > buf.length) {
			throw new ISO8583ParseException(
					String.format("Insufficient data for bin LLLTABLES header, field %d pos %d", field, pos), pos,
					field);
		}
		final int l = decodeLength(buf, pos, 3);

		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLTABLES length %d field %d pos %d", l, field, pos),
					pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(
					String.format("Insufficient data for LLLTABLES field %d, pos %d requires %d, only %d available",
							field, pos, l, buf.length - pos + 1),
					pos, field);
		}
		byte[] data = new byte[l];
		int length = l;
		System.arraycopy(buf, pos + 2, data, 0, l);
		List<ISOTable> subDatas;
		Set<String> tableIds = this.mapProvider.getMap().keySet();
		Map<String, String> lengthTypeMap = this.mapProvider.getLengthTypeMap();

		int offset = 0;
		String[] conditions = (String[]) custom;
		try {
			subDatas = new Vector<ISOTable>();
			while (length > offset) {
				//there are two way parse table function, depend on 1 digit table id or 2 digit table id
				String tableId = new String(data, offset, 1);// table id

				int sublength = 0;
				int offsetLen = 1;

				if (tableIds.contains(tableId)) {
					offsetLen = 1;
					if(lengthTypeMap.get(tableId) != null && lengthTypeMap.get(tableId).length() >=3 ) {
						offset += 1;
						
						String[] lengthTypeAry = lengthTypeMap.get(tableId).split("-");
						int tableLen = Integer.parseInt(lengthTypeAry[0]);
						offsetLen = (int) Math.ceil(tableLen/2.0);						
						sublength = decodeHexLength(data, offset, tableLen);
						offset += offsetLen;
					} else {
						offset += 1;
						sublength = (int) CommonFunction.Bcd.decodeToLong(data, offset, offsetLen*2);
						offset += offsetLen;
					}
					
					//1 digit
					//offset += 1;
					//sublength = (int) (data[offset] & 0xff);
					//offset += 1;
				} else {
					//2 digit
					tableId = new String(data, offset, 2);// table id
					if (tableIds.contains(tableId)) {
						offset += 2;
						sublength = (int) CommonFunction.Bcd.decodeToLong(data, offset, 3);
						offset += 2;
					} else {
						//4 digit
						tableId = new String(data, offset, 4);// table id
						offset += 4;
						sublength = (int) CommonFunction.Bcd.decodeToLong(data, offset, 4);
						offset += 2;
					}
				}

				byte[] subData = new byte[sublength];
				System.arraycopy(data, offset, subData, 0, sublength);
				offset += sublength;
				Map<Integer, FieldParseInfo> fpis = this.mapProvider.getFieldsParseInfo(tableId, conditions);
				
				if (fpis == null) {	
					throw new ISO8583ParseException(String.format(
							"Insufficient data for LLLTABLES field %d, pos %d requires table id %s defind in configuration",
							field, pos, tableId), pos, field);
				}
				//calculate fields number by length
				ISOTable table = new ISOTable();
				table.setId(tableId);
				table.setLength(sublength);
				table.setFields(this.parseNestFields(subData, tableId, vo, isTraceEnable, conditions));
				table.setDescription(this.mapProvider.getDesc(tableId, conditions));
				subDatas.add(table);
			}
		} catch (ISO8583ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new ISO8583ParseException(String.format("parse data for LLLTABLES field %d, pos %d exception:[%s]",
					field, pos, e.getMessage()), pos, field);
		}
		return new ISOValue<List<ISOTable>>(type, subDatas, length, description, true);
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		//		writeLengthHeader(value.getLength(), outs, 3);
		@SuppressWarnings("unchecked")
		List<ISOTable> subDatas = (List<ISOTable>) value.getValue();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int missing = 0;
		if (subDatas == null || subDatas.size() == 0) {
			missing = value.getLength();
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
			return;
		}

		String[] conditions = (String[]) custom;
		byte[] result = null;
		boolean is4Digi = false;
		Map<String, String> lengthTypeMap = this.mapProvider.getLengthTypeMap();
		for (ISOTable table : subDatas) {
			String tableId = table.getId();
			byte[] data = writeNestFields(table.getFields(), tableId, vo, conditions);			
			int length = data.length;
			try {
				buffer.write(tableId.getBytes());
				if (tableId.length() == 1) {
					
					if(lengthTypeMap.get(tableId) != null && lengthTypeMap.get(tableId).length() >=3 ) {
						String[] lengthTypeAry = lengthTypeMap.get(tableId).split("-");
						int tableLen = Integer.parseInt(lengthTypeAry[0]);
						if(lengthTypeAry[1].equalsIgnoreCase("H")) {
							writeLengthHexHeader(length, buffer, tableLen);
						} else {
							writeLengthHeader(length, buffer, tableLen);
						}
					} else {
						buffer.write((byte) (table.getLength() & 0xff));
					}
				} else if(tableId.length() == 2){
					writeLengthHeader(length, buffer, 3);
				} else{
					writeLengthHeader(length, buffer, 4);
					is4Digi = true;
				}
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
		int length = result.length;
		writeLengthHeader(length, outs, is4Digi?4:3);
		outs.write(result);
	}

	@Override
	public <T> ISOValue<?> parseString(int field, byte[] buf, int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLTABLES field %d pos %d", field, pos), pos, field);
		} else if (pos + 3 > buf.length) {
			throw new ISO8583ParseException(
					String.format("Insufficient data for bin LLLTABLES header, field %d pos %d", field, pos), pos,
					field);
		}
		final int l = ((buf[pos] & 0x0f) * 100) + (((buf[pos + 1] & 0xf0) >> 4) * 10) + (buf[pos + 1] & 0x0f);
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLTABLES length %d field %d pos %d", l, field, pos),
					pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(
					String.format("Insufficient data for LLLTABLES field %d, pos %d requires %d, only %d available",
							field, pos, l, buf.length - pos + 1),
					pos, field);
		}
		byte[] data = new byte[l];
		int length = l;
		System.arraycopy(buf, pos + 2, data, 0, l);
		List<ISOTable> subDatas;
		String[] tableIds = reference.split(",");
		String[] conditions = (String[]) custom;
		int offset = 0;
		try {
			subDatas = new Vector<ISOTable>();
			while (length > offset) {
				//there are two way parse table function, depend on 1 digit table id or 2 digit table id
				String tableId = new String(data, offset, 1);// table id
				int sublength = 0;
				if (arrayContains(tableIds, tableId)) {
					//1 digit
					offset += 1;
					sublength = (int) (data[offset] & 0xff);
					offset += 1;
				} else {
					//2 digit
					tableId = new String(data, offset, 2);// table id
					offset += 2;
					sublength = (int) CommonFunction.Bcd.decodeToLong(data, offset, 3);
					offset += 2;
				}

				byte[] subData = new byte[sublength];
				System.arraycopy(data, offset, subData, 0, sublength);
				offset += sublength;
				Map<Integer, FieldParseInfo> fpis = this.mapProvider.getFieldsParseInfo(tableId, conditions);
				if (fpis == null) {
					throw new ISO8583ParseException(String.format(
							"Insufficient data for LLLTABLES field %d, pos %d requires table id %s defind in configuration",
							field, pos, tableId), pos, field);
				}
				//calculate fields number by length
				ISOTable table = new ISOTable();
				table.setId(tableId);
				table.setLength(sublength);
				table.setFields(this.parseNestFields(subData, tableId, vo, isTraceEnable, conditions));
				table.setDescription(this.mapProvider.getDesc(tableId, conditions));
				subDatas.add(table);
			}
		} catch (ISO8583ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new ISO8583ParseException(String.format("parse data for LLLTABLES field %d, pos %d exception:[%s]",
					field, pos, e.getMessage()), pos, field);
		}
		return new ISOValue<List<ISOTable>>(type, subDatas, length, description, true);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		@SuppressWarnings("unchecked")
		List<ISOTable> subDatas = (List<ISOTable>) value.getValue();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int missing = 0;
		if (subDatas == null || subDatas.size() == 0) {
			missing = value.getLength();
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
			return;
		}

		byte[] result = null;
		String[] conditions = (String[]) custom;
		for (ISOTable table : subDatas) {
			String tableId = table.getId();
			byte[] data = writeNestFields(table.getFields(), tableId, vo, conditions);
			int length = data.length;
			try {
				buffer.write(tableId.getBytes());
				if (tableId.length() == 1) {
					buffer.write((byte) (tableId.length() & 0xff));
				} else {
					writeStringLengthHeader(length, buffer, 3);
				}
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
		outs.write(result);
	}

	@Override
	public ISO8583Fields parseNestFields(byte[] data, String id, ISO8583VO vo, boolean isTraceEnable,
			String... conditions) throws ISO8583ParseException, UnsupportedEncodingException {
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
			ISOValue<?> val;
			ISO8583FieldTraceInfo field = null;
			if (isTraceEnable) {
				field = new ISO8583FieldTraceInfo();
				field.setBinary(true);
				field.setStartPos(pos);
				field.setConfigParseId(this.getMapProvider().getFieldsParseKey(id, conditions));
				field.setDesc(fpi.getDescription());
				field.setField(i);
			}
			try {
				if (fpi.getType() == ISOType.CHECKSUM) {
					if (fpi.isNested(fpi.getReference(), conditions)) {
						val = fpi.parse(i, data, pos, vo, new Object[] { null, conditions }, isTraceEnable);
					} else {
						String s = fpi.getAlt();
						if (s != null && s.equals("no check")) {
							val = fpi.parse(i, data, pos, vo, null, isTraceEnable);
						} else {
							String[] nums = s.split("-");
							byte[] subData = getCheckSumBytes(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]),
									fpis, index, data);
							val = fpi.parse(i, data, pos, vo, new Object[] { subData, conditions }, isTraceEnable);
						}
					}
				} else if (fpi.getType() == ISOType.LBINARY || fpi.getType() == ISOType.LVAR) {
					int calLength = data.length;
					for (Integer j : index) {
						if (i == j) {
							continue;
						}
						calLength -= fpis.get(j).getLength();
					}
					val = fpi.parse(i, data, pos, vo, new Object[] { calLength, conditions }, isTraceEnable);
				} else {
					val = fpi.parse(i, data, pos, vo, conditions, isTraceEnable);
				}
				fields.setField(i, val);
				if (val != null) {
					if (val.getType() == ISOType.NUMERIC || val.getType() == ISOType.DATE10
							|| val.getType() == ISOType.DATE4 || val.getType() == ISOType.DATE_EXP
							|| val.getType() == ISOType.AMOUNT || val.getType() == ISOType.TIME) {
						pos += (val.getLength() / 2) + (val.getLength() % 2);
					} else {
						pos += val.getLength();
					}
					if (val.getType() == ISOType.LLVAR || val.getType() == ISOType.LLBIN
							|| val.getType() == ISOType.LLZ) {
						pos++;
					} else if (val.getType() == ISOType.LLLVAR || val.getType() == ISOType.LLLBIN
							|| val.getType() == ISOType.LLLTABLES) {
						pos += 2;
					}
				}
			} catch (ISO8583ParseException e) {
				if (isTraceEnable) {
					ISO8583TraceInfo info = proessPos(data, traceInfos, field);
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
					ISO8583TraceInfo info = proessPos(data, traceInfos, field);
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

	private ISO8583TraceInfo proessPos(byte[] data, List<ISO8583FieldTraceInfo> traceInfos,
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

	@Override
	public byte[] writeNestFields(ISO8583Fields fields, String id, ISO8583VO vo, String... conditions)
			throws IOException {
		Map<Integer, FieldParseInfo> fpis = this.getMapProvider().getFieldsParseInfo(id, conditions);
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
				} else {
					try {
						fpi.write(v, bout, vo, conditions);
					} catch (IOException ex) {
						// should never happen, writing to a ByteArrayOutputStream
					}
				}
			}
		}
		return bout.toByteArray();
	}

	private boolean arrayContains(String[] array, String contain) {
		if (array == null) {
			return false;
		}
		for (String s : array) {
			if (s.equals(contain))
				return true;
		}
		return false;
	}
}
