package com.hp.nccc.iso8583.parse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.nccc.iso8583.common.Bertlv;
import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ChipParseInfo;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOChip;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.core.PoskitzTag;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

public class PoskitzTagParseInfo extends FieldParseInfo {

	private static final Logger log = LogManager.getLogger(PoskitzTagParseInfo.class);
	/** chip parse info */
	private ChipParseInfo chipParseInfo = new ChipParseInfo();

	public ChipParseInfo getChipParseInfo() {
		return chipParseInfo;
	}

	public void setChipParseInfo(ChipParseInfo chipParseInfo) {
		this.chipParseInfo = chipParseInfo;
	}

	public PoskitzTagParseInfo() {
		super(ISOType.POSKITZTAG, 0);
	}

	@Override
	public <T> ISOValue<?> parse(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin POSKITZTAG field %d pos %d", field, pos), pos,
					field);
		} else if (pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient POSKITZTAG header field %d", field), pos,
					field);
		}
		int offset = 2;
		final int l = CommonFunction.getHexLength(buf, pos, offset);
		
		if (l < 0) {
			throw new ISO8583ParseException(
					String.format("Invalid POSKITZTAG length %d field %d pos %d", l, field, pos), pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin POSKITZTAG field %d, pos %d requires %d, only %d available", field, pos,
					l, buf.length - pos + 1), pos, field);
		}
		byte[] _v = new byte[l - 2];
		System.arraycopy(buf, pos + offset + 1, _v, 0, l - 2);//exclude id and crc

		PoskitzTag poskitzTag = new PoskitzTag();
		poskitzTag.setLength(l);
		poskitzTag.setId(CommonFunction.bytesToHex(buf, "", pos + offset, 1));
		offset += 1;

		Bertlv bertlv = new Bertlv();

		List<ISOChip> chips = bertlv.parseChip(_v);

		for (ISOChip chip : chips) {
			String tempDesc;
			ChipParseInfo.ChipFieldInfo info = chipParseInfo.getChipFieldInfos()
					.get(CommonFunction.bytesToHex(chip.getTag()));
			if (info == null) {
				log.warn(String.format("POSKITZTAG chip defiend error ,can't find tag. tag id[%s]",
						CommonFunction.bytesToHex(chip.getTag())));
				tempDesc = String.format("undefined tag [%s]", CommonFunction.bytesToHex(chip.getTag()));
			} else {
				tempDesc = info.getDesc();
			}
			chip.setDesc(tempDesc);
		}

		poskitzTag.setTags(chips);
		offset += _v.length;

		byte crc = buf[pos + offset];

		
		byte[] checkSumData = new byte[l - 1];
		System.arraycopy(buf, pos+2, checkSumData, 0, checkSumData.length);

		if (crc != CommonFunction.checkSum(checkSumData)) {
			throw new ISO8583ParseException("parse tag data check sum error", pos, field);
		}
		poskitzTag.setCrc(crc);
		offset += 1;

		return new ISOValue<PoskitzTag>(type, poskitzTag, offset, getDescription(), false);
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
			for (ISOChip chip : chips) {
				if (chip != null) {
					outs.write(chip.getTag());
					outs.write(chip.getLength());
					outs.write(chip.getData());
				}
			}
		} else {
			byte[] binval = CommonFunction.hexDecode(value.getValue().toString());
			outs.write(binval);
			missing = value.getLength() - binval.length;
		}
		if (type == ISOType.POSKITZTAG && missing > 0) {
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
		}
	}

	@Override
	public <T> ISOValue<?> parseString(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin POSKITZTAG field %d pos %d", field, pos), pos,
					field);
		}
		byte[] lengByte = CommonFunction.hexDecode(new String(buf, pos, 4));
		final int l = CommonFunction.getHexLength(lengByte);
		int offset = 4;
		if (l < 0) {
			throw new ISO8583ParseException(
					String.format("Invalid POSKITZTAG length %d field %d pos %d", l, field, pos), pos, field);
		}
		if (l * 2 + pos + 2 + 2 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin POSKITZTAG field %d, pos %d requires %d, only %d available", field, pos,
					l, buf.length - pos + 1), pos, field);
		}
		PoskitzTag poskitzTag = new PoskitzTag();
		poskitzTag.setLength(l);
		poskitzTag.setId(new String(buf, pos + offset, 2));
		offset += 2;
		byte[] _v = CommonFunction.hexDecode(new String(buf, pos + offset, (l - 2) * 2));//exclude id and crc

		Bertlv bertlv = new Bertlv();

		List<ISOChip> chips = bertlv.parseChip(_v);

		for (ISOChip chip : chips) {
			String tempDesc;
			ChipParseInfo.ChipFieldInfo info = chipParseInfo.getChipFieldInfos()
					.get(CommonFunction.bytesToHex(chip.getTag()));
			if (info == null) {
				log.warn(String.format("POSKITZTAG chip defiend error ,can't find tag. tag id[%s]",
						CommonFunction.bytesToHex(chip.getTag())));
				tempDesc = String.format("undefined tag [%s]", CommonFunction.bytesToHex(chip.getTag()));
			} else {
				tempDesc = info.getDesc();
			}
			chip.setDesc(tempDesc);
		}

		poskitzTag.setTags(chips);
		offset += _v.length * 2;

		byte crc = CommonFunction.hexDecode(new String(buf, pos + offset, 2))[0];

		byte[] checkSumData = CommonFunction.hexDecode(new String(buf, pos + 4, (l - 1) * 2));
		//System.arraycopy(buf, pos+2, checkSumData, 0, checkSumData.length);

		if (crc != CommonFunction.checkSum(checkSumData)) {
			throw new ISO8583ParseException("parse tag data check sum error", pos, field);
		}
		poskitzTag.setCrc(crc);
		offset += 2;

		return new ISOValue<PoskitzTag>(type, poskitzTag, offset, getDescription(), false);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		if (!(value.getValue() instanceof PoskitzTag)) {
			throw new RuntimeException("write tag data ISOValue type is not PoskitzTag");
		}

		PoskitzTag poskitzTag = (PoskitzTag) value.getValue();

		ByteArrayOutputStream aout = new ByteArrayOutputStream();
		aout.write(CommonFunction.hexDecode(poskitzTag.getId()));
		for (ISOChip chip : poskitzTag.getTags()) {
			if (chip != null) {
				aout.write(chip.getTag());
				aout.write(CommonFunction.hexDecode(CommonFunction.writeHexLength(chip.getLength(), 1)));
				aout.write(chip.getData());
			}
		}
		byte[] dataBytes = aout.toByteArray();

		int l = 0;
		if (poskitzTag.getLength() == 0) {
			l = dataBytes.length;
		} else {
			l = poskitzTag.getLength();
		}
		outs.write(CommonFunction.writeHexLength(l + 1, 2).getBytes());

		outs.write(CommonFunction.bytesToHex(dataBytes).getBytes());
		byte crc;
		if (poskitzTag.getCrc() == 0x00) {
			crc = CommonFunction.checkSum(dataBytes);
		} else {
			crc = poskitzTag.getCrc();
		}
		outs.write(CommonFunction.hexEncode(crc).getBytes());
	}

}
