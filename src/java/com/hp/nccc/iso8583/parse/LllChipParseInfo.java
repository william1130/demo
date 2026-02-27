/**
 * 
 */
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
import com.hp.nccc.iso8583.core.ISO8583Fields;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOChip;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 * 
 */
public class LllChipParseInfo extends FieldParseInfo {

	private static final Logger log = LogManager.getLogger(LllChipParseInfo.class);
	/** chip parse info */
	private ChipParseInfo chipParseInfo = new ChipParseInfo();

	public ChipParseInfo getChipParseInfo() {
		return chipParseInfo;
	}

	public void setChipParseInfo(ChipParseInfo chipParseInfo) {
		this.chipParseInfo = chipParseInfo;
	}

	public LllChipParseInfo() {
		super(ISOType.LLLCHIP, 0);
	}

	@Override
	public <T> ISOValue<?> parse(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLLCHIP field %d pos %d", field, pos), pos,
					field);
		} else if (pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient LLLCHIP header field %d", field), pos, field);
		}
		final int l = decodeLength(buf, pos, 3);
		;
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLCHIP length %d field %d pos %d", l, field, pos),
					pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin LLLCHIP field %d, pos %d requires %d, only %d available", field, pos, l,
					buf.length - pos + 1), pos, field);
		}
		byte[] _v = new byte[l];
		System.arraycopy(buf, pos + 2, _v, 0, l);
		List<ISOChip> chips = new ArrayList<ISOChip>();
		int index = 0;
		while (true) {
			ISOChip chip = new ISOChip();
			int tempLength = 1;
			String tempDesc = "";//undefined
			if (chipParseInfo.getDoubleBytesTags().contains(Byte.valueOf(_v[index]))
					|| (_v[index] & 0x10) == (byte) 0x10 && (_v[index] & 0x0f) == (byte) 0x0f) {
				if(chipParseInfo.getTripleBytesTags().contains(new String(CommonFunction.bytesToHex(new byte[]{_v[index], _v[index+1]})))){
					chip.setTag(new byte[] { _v[index++], _v[index++], _v[index++] });
					tempLength = 1;
				}else{
					chip.setTag(new byte[] { _v[index++], _v[index++] });
					tempLength = 1;
				}
			} else {
				chip.setTag(new byte[] { _v[index++] });
				tempLength = 1;
			}

			ChipParseInfo.ChipFieldInfo info = chipParseInfo.getChipFieldInfos().get(
					CommonFunction.bytesToHex(chip.getTag()));
			if (info == null) {
				log.warn(String.format("LLLCHIP chip defiend error ,can't find tag. tag id[%s]",
						CommonFunction.bytesToHex(chip.getTag())));
				tempDesc = String.format("undefined tag [%s]", CommonFunction.bytesToHex(chip.getTag()));
//				throw new ISO8583ParseException(String.format("LLLCHIP chip defiend error ,can't find tag. tag id[%s]",
//						CommonFunction.bytesToHex(chip.getTag())), pos, field);
			}else{
				tempLength = info.getLength();
				tempDesc = info.getDesc();
			}
			byte[] lengthBytes = new byte[tempLength];
			System.arraycopy(_v, index, lengthBytes, 0, tempLength);
			int chiplength = 0;
			for (int i = 0; i < tempLength; i++) {
				chiplength *= 10;
				chiplength += (int)lengthBytes[i];
			}
			chip.setLength(chiplength);
			index += tempLength;

			byte[] data = null;
			if(chip.getLength() != 0){
				data = new byte[chip.getLength()];
				System.arraycopy(_v, index, data, 0, chip.getLength());
			}
			chip.setData(data);
			index += chip.getLength();

			chip.setDesc(tempDesc);
			chips.add(chip);
			if (index == _v.length) {
				break;
			}
		}

		return new ISOValue<List<ISOChip>>(type, chips, l, getDescription(), false);
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
		if (type == ISOType.LLLCHIP && missing > 0) {
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
		}
	}

	@Override
	public <T> ISOValue<?> parseString(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ISO8583ParseException(String.format("Invalid bin LLLCHIP field %d pos %d", field, pos), pos,
					field);
		} else if (pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format("Insufficient LLLCHIP header field %d", field), pos, field);
		}
		final int l = ((buf[pos] & 0x0f) * 100) + (((buf[pos + 1] & 0xf0) >> 4) * 10) + (buf[pos + 1] & 0x0f);
		if (l < 0) {
			throw new ISO8583ParseException(String.format("Invalid LLLCHIP length %d field %d pos %d", l, field, pos),
					pos, field);
		}
		if (l + pos + 2 > buf.length) {
			throw new ISO8583ParseException(String.format(
					"Insufficient data for bin LLLCHIP field %d, pos %d requires %d, only %d available", field, pos, l,
					buf.length - pos + 1), pos, field);
		}
		byte[] _v = new byte[l];
		System.arraycopy(buf, pos + 2, _v, 0, l);

		List<ISOChip> chips = new ArrayList<ISOChip>();
		int index = 0;
		while (true) {
			ISOChip chip = new ISOChip();
			if (_v[index] == (byte) 0x5F || _v[index] == (byte) 0x9F || _v[index] == (byte) 0xDF) {
				//2byte tag
				chip.setTag(new byte[] { _v[index++], _v[index++] });
			} else {
				chip.setTag(new byte[] { _v[index++] });
			}
			chip.setLength(_v[index++] & 0xFF);
			byte[] data = new byte[chip.getLength()];
			System.arraycopy(_v, index, data, 0, chip.getLength());
			chip.setData(data);
			index += chip.getLength();
			chips.add(chip);
			if (index == _v.length) {
				break;
			}
		}

		return new ISOValue<List<ISOChip>>(type, chips, l, getDescription(), false);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		writeLengthHeader(value.getLength(), outs, 3);
		int missing = 0;
		int length = this.length == 0 ? varLength : this.length;
		if (value.getValue() instanceof byte[]) {
			outs.write((byte[]) value.getValue());
			missing = value.getLength() - ((byte[]) value.getValue()).length;
		} else if (value.getValue() instanceof ISO8583Fields) {
			byte[] _v = this.writeNestFields((ISO8583Fields) value.getValue(), reference, vo, (String[])custom);
			outs.write(_v);
			missing = length - (_v).length;
		} else {
			byte[] binval = CommonFunction.hexDecode(value.getValue().toString());
			outs.write(binval);
			missing = value.getLength() - binval.length;
		}
		if (type == ISOType.BINARY && missing > 0) {
			for (int i = 0; i < missing; i++) {
				outs.write(0);
			}
		}
	}
}
