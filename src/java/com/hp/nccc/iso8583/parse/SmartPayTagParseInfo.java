package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ChipParseInfo;
import com.hp.nccc.iso8583.core.ISO8583FieldTraceInfo;
import com.hp.nccc.iso8583.core.ISO8583TraceInfo;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOChip;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

public class SmartPayTagParseInfo extends FieldParseInfo {

	private static final Logger log = LogManager.getLogger(SmartPayTagParseInfo.class);
	
	/** chip parse info */
	private ChipParseInfo chipParseInfo = new ChipParseInfo();

	public ChipParseInfo getChipParseInfo() {
		return chipParseInfo;
	}

	public void setChipParseInfo(ChipParseInfo chipParseInfo) {
		this.chipParseInfo = chipParseInfo;
	}

	public SmartPayTagParseInfo() {
		super(ISOType.SMARTPAYTAG, 0);
	}

	@Override
	public <T> ISOValue<?> parse(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin SMARTPAYTAG field %d pos %d", field, pos), pos,
					field);
		} else if (pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient SMARTPAYTAG header field %d", field), pos, field);
		}
		final int l = decodeLength(buf, pos, 3);
		
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid SMARTPAYTAG length %d field %d pos %d", l, field, pos),
					pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin SMARTPAYTAG field %d, pos %d requires %d, only %d available", field, pos, l,
					buf.length - pos + 1), pos, field);
		}

		byte[] _v = new byte[l];
		System.arraycopy(buf, pos + 2, _v, 0, l);
		List<ISOChip> chips = new ArrayList<ISOChip>();
		boolean isEdc = getAlt() != null && !getAlt().isEmpty() && getAlt().equals("EDC");
		int index = 0;
		int i = 0;
		List<ISO8583FieldTraceInfo> traceInfos = null;
		if(isTraceEnable){
			traceInfos = new ArrayList<ISO8583FieldTraceInfo>();
		}
		while (true) {
			ISO8583FieldTraceInfo traceField = null;
			if(isTraceEnable){
				traceField = new ISO8583FieldTraceInfo();
				traceField.setBinary(true);
				traceField.setStartPos(pos);
			}
			ISOChip chip = new ISOChip();
			try {
				String tempDesc = "";//undefined
				if(isEdc){
					chip.setTag(new String(_v, index, 2).getBytes());
					index+=2;
					chip.setLength(CommonFunction.getHexLength(_v, index, 1));
					index+=1;
				}else{
					chip.setTag(CommonFunction.hexDecode(new String(_v, index, 2)));
					index+=2;
					chip.setLength(Integer.parseInt(new String(_v, index, 2)));
					index+=2;
				}
				
				byte[] data = new byte[chip.getLength()];
				System.arraycopy(_v, index, data, 0, chip.getLength());
				chip.setData(data);
				index+=chip.getLength();
				ChipParseInfo.ChipFieldInfo info = chipParseInfo.getChipFieldInfos().get(
						CommonFunction.bytesToHex(chip.getTag()));
				if (info == null) {
					log.warn(String.format("SMARTPAYTAG chip defiend error ,can't find tag. tag id[%s]",
							CommonFunction.bytesToHex(chip.getTag())));
					tempDesc = String.format("undefined tag [%s]", CommonFunction.bytesToHex(chip.getTag()));
				}else{
					tempDesc = info.getDesc();
				}
				chip.setDesc(tempDesc);
				chips.add(chip);
			} catch (Exception e) {
				ISO8583ParseException ie = new ISO8583ParseException(String.format("exception [%s] field %d", e.getMessage(), i), pos, i);
				if(isTraceEnable){
					ISO8583TraceInfo info = processPos(buf, traceInfos, traceField);
					ie.setTraceInfo(info);
				}
				throw ie;
			}
			i++;
			if(isTraceEnable){
				traceField.setConfigParseId(new String(chip.getTag()));
				traceField.setDesc(chip.getDesc());
				traceField.setField(i);
				traceField.setEndPos(pos-1);
				int length = traceField.getEndPos() - traceField.getStartPos() + 1;
				if(length > buf.length - traceField.getStartPos()){
					length = buf.length - traceField.getStartPos();
				}
				byte[] tempByte = new byte[length];
				System.arraycopy(buf, traceField.getStartPos(), tempByte, 0, length);
				traceField.setByteData(tempByte);
				traceField.setValue(new ISOValue<ISOChip>(type, chip, l, getDescription(), false));
				traceInfos.add(traceField);
			}
			if (index == _v.length) {
				break;
			}
		}

		return new ISOValue<List<ISOChip>>(type, chips, l, getDescription(), false);
	}
	
	private ISO8583TraceInfo processPos(byte[] data, List<ISO8583FieldTraceInfo> traceInfos, ISO8583FieldTraceInfo field) {
		field.setEndPos(data.length-1);
		int length = field.getEndPos() - field.getStartPos() + 1;
		if(length > data.length - field.getStartPos()){
			length = data.length - field.getStartPos();
		}
		byte[] tempByte = new byte[length];
		System.arraycopy(data, field.getStartPos(), tempByte, 0, length);
		field.setByteData(tempByte);
		traceInfos.add(field);
		ISO8583TraceInfo info = new ISO8583TraceInfo();;
		info.setTraceInfos(traceInfos);
		return info;
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		writeLengthHeader(value.getLength(), outs, 3);
		int missing = 0;
		if (value.getValue() instanceof byte[]) {
			outs.write((byte[]) value.getValue());
			missing = value.getLength() - ((byte[]) value.getValue()).length;
		} else if (value.getValue() instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<ISOChip> chips = (List<ISOChip>) value.getValue();
			boolean isEdc = getAlt() != null && !getAlt().isEmpty() && getAlt().equals("EDC");
			for(ISOChip chip:chips){
				if(isEdc){
					//tag name
					outs.write(chip.getTag());
					//tag length
					outs.write(CommonFunction.hexDecode(CommonFunction.writeHexLength(chip.getLength(), 1)));
					//value
					outs.write(chip.getData());
				}else{
					//tag name
					outs.write(CommonFunction.hexEncode(chip.getTag(), 0, 1).getBytes());
					//tag length
					outs.write(String.format("%02d", chip.getLength()).getBytes());
					//value
					outs.write(chip.getData());
				}
			}
		} else {
			byte[] binval = CommonFunction.hexDecode(value.getValue().toString());
			outs.write(binval);
			missing = value.getLength() - binval.length;
		}
		if (type == ISOType.SMARTPAYTAG && missing > 0) {
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
