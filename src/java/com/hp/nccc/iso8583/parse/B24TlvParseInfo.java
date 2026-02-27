/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.hp.nccc.iso8583.core.B24Tlv;
import com.hp.nccc.iso8583.core.B24TlvParseInfos;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;
import com.hp.nccc.iso8583.core.ISOValue;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

/**
 * @author hsiehes
 * 
 */
public class B24TlvParseInfo extends FieldParseInfo {

	/** tlv parse info */
	private B24TlvParseInfos b24TlvParseInfo = new B24TlvParseInfos();

	public B24TlvParseInfos getB24TlvParseInfo() {
		return b24TlvParseInfo;
	}

	public void setB24TlvParseInfo(B24TlvParseInfos b24TlvParseInfo) {
		this.b24TlvParseInfo = b24TlvParseInfo;
	}

	public B24TlvParseInfo() {
		super(ISOType.B24TLV, 0);
	}

	@Override
	public <T> ISOValue<?> parse(int field, byte[] buf, int pos, ISO8583VO vo, Object custom, boolean isTraceEnable)
			throws ISO8583ParseException, UnsupportedEncodingException {
		//undefined
		return null;
	}

	@Override
	public <T> void write(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		//undefined
	}

	@Override
	public <T> ISOValue<?> parseString(final int field, final byte[] buf, final int pos, ISO8583VO vo, Object custom,
			boolean isTraceEnable) throws ISO8583ParseException, UnsupportedEncodingException {
		String _v = new String(buf, this.getCharacterEncoding());
		final int l = buf.length;
		List<B24Tlv> b24IsoChipList = new ArrayList<B24Tlv>();
		int offset = 0;
		while (true) {
			B24Tlv b24IsoChip = new B24Tlv();
			//tag name
			String tagName = _v.substring(offset, offset + 2);
			b24IsoChip.setTag(tagName);
			offset += 2;
			String id = ((Object[]) custom)[0].toString();
			B24TlvParseInfos.B24TlvFieldInfo info = b24TlvParseInfo.getB24TlvFieldInfo().get(id + "_" + tagName);
			//tag length
			int tokenLength = Integer.parseInt(_v.substring(offset, offset + 3));
			b24IsoChip.setLength(tokenLength);
			offset += 3;
			String tagValue = _v.substring(offset, offset + tokenLength);
			offset += tokenLength;
			b24IsoChip.setData(tagValue);
			if(info == null) {
				b24IsoChip.setDesc("Unknow Tag");
			} else {
				b24IsoChip.setDesc(info.getDesc());	
			}
			b24IsoChipList.add(b24IsoChip);
			if (offset == l || offset == (l-1)) {
				break;
			}
		}

		return new ISOValue<List<B24Tlv>>(type, b24IsoChipList, l, getDescription(), false);
	}

	@Override
	public <T> void writeString(ISOValue<T> value, OutputStream outs, ISO8583VO vo, Object custom) throws IOException {
		@SuppressWarnings("unchecked")
		List<B24Tlv> subDatas = (List<B24Tlv>) value.getValue();
		for (B24Tlv chip : subDatas) {
			//TAG
			outs.write(chip.getTag().getBytes());
			//LENGTH
			outs.write(String.format("%03d", chip.getLength()).getBytes());
			//TAG data
			outs.write(chip.getData().getBytes());
		}
	}

}
