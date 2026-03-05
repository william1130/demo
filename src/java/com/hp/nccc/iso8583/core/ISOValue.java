/**
 * 
 */
package com.hp.nccc.iso8583.core;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.rits.cloning.Cloner;

/**
 * @author hsiehes
 * 
 */
public class ISOValue<T> implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3277963718559610264L;
	private ISOType type;
	private T value;
	private int length;
	private String encoding;
	private String description;
	private boolean nestedValue;

	/**
	 * Creates a new instance that stores the specified value as the specified
	 * type. Useful for storing LLVAR or LLLVAR types, as well as fixed-length
	 * value types like DATE10, DATE4, AMOUNT, etc.
	 * 
	 * @param t
	 *            the ISO type.
	 * @param value
	 *            The value to be stored.
	 */
	@SuppressWarnings("unchecked")
	public ISOValue(ISOType t, T value, String desc) {
		if (t.needsLength()) {
			throw new IllegalArgumentException(
					"Fixed-value types must use constructor that specifies length");
		}
		type = t;
		this.value = value;
		description = desc;
		this.nestedValue = false;
        if(value instanceof ISO8583Fields){ 
        	length = ((ISO8583Fields)value).getLength();
        } else if ((t == ISOType.LLLTOKENS || t == ISOType.LLLLTOKENS) && value instanceof List<?>) {
            if (length == 0) {
                length+=1;//Eye-catcher
                length+=1;//User-FLD1 
                length+=5;//CNT
                length+=5;//Lgth
                for(ISOToken token : (List<ISOToken>)value){
                    length+=1;//Eye-catcher
                    length+=1;//Filler
                    length+=2;//TKN-ID
                    length+=5;//Lgth
                    length+=1;//Filler
					// add by gail 20171011
					boolean isTlv = false;
					for (int i = 0; i < 128; i++) {
						if (token.getFields().hasField(i)) {
							if (token.getFields().getField(i).getType() == ISOType.B24TLV) {
								isTlv = true;
								int tlvLen = 0;
								for (B24Tlv tlv : (List<B24Tlv>) token.getFields().getField(i).getValue()) {
									tlvLen += 2;
									tlvLen += 3;
									tlvLen += tlv.getLength();
								}
								length += tlvLen;
							}else if (token.getFields().getField(i).getType() == ISOType.VAR) {
								isTlv = true;
								int tlvLen = 0;
								tlvLen += 2;
								tlvLen += 3;
								tlvLen += token.getFields().getField(i).getValue().toString().length();
								length += tlvLen;
							}
						}
					}
					if (!isTlv) {
						length += token.getFields().getLength();
					}
                }
           }
       }else if (t == ISOType.LVAR) {
			if(value instanceof String[]){
//				length += length;
				for(String s : (String[])value){
					length += s.length();
				}
			}else{
				if (length == 0) {
					length = value.toString().length();
				}else{
					length += value.toString().length();
				}
			}
		} else if (type == ISOType.LLVAR || type == ISOType.LLLVAR) {
			length = value.toString().length();
			if (t == ISOType.LLVAR && length > 99) {
				throw new IllegalArgumentException(
						"LLVAR can only hold values up to 99 chars");
			} else if (t == ISOType.LLLVAR && length > 999) {
				throw new IllegalArgumentException(
						"LLLVAR can only hold values up to 999 chars");
			}
		} else if (type == ISOType.LLBIN || type == ISOType.LLLBIN) {
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
			if (t == ISOType.LLBIN && length > 99) {
				throw new IllegalArgumentException(
						"LLBIN can only hold values up to 99 chars");
			} else if (t == ISOType.LLLBIN && length > 999) {
				throw new IllegalArgumentException(
						"LLLBIN can only hold values up to 999 chars");
			}
		} else if (type == ISOType.LLZ) {
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
			if (t == ISOType.LLZ && length > 99) {
				throw new IllegalArgumentException(
						"LLZ can only hold values up to 99 chars");
			}
		} else if (t == ISOType.LLLTABLES) {
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
//			if (t == ISOType.LLLTABLES && length > 999) {
//				throw new IllegalArgumentException(
//						"LLLTABLES can only hold values up to 999 chars");
//			}
		} else if (t == ISOType.LLLCHIP) {
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
			if (t == ISOType.LLLCHIP && length > 999) {
				throw new IllegalArgumentException(
						"LLLCHIP can only hold values up to 999 chars");
			}
		} else if (t == ISOType.LLLTOKENS || t == ISOType.LLLLTOKENS) {
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
			if (t == ISOType.LLLTOKENS && length > 999) {
				throw new IllegalArgumentException(
						"LLLTOKENS can only hold values up to 999 chars");
			}
			if (t == ISOType.LLLLTOKENS && length > 9999) {
				throw new IllegalArgumentException(
						"LLLLTOKENS can only hold values up to 9999 chars");
			}
		} else if(t == ISOType.POSKITZTAG){
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
		} else if(t == ISOType.SMARTPAYCHIP){
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else if(value instanceof SmartPayChip){
				SmartPayChip chip = ((SmartPayChip)value);
				length += 1;//sLen
				length += 8;//S/N
				length += 2;//tLen
				length += chip.getTac().length;//tac
				length += 1;//mLen
				length += 15;//mcc
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
		} else if(t == ISOType.SMARTPAYTAG){
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else if(value instanceof List<?>){
				List<ISOChip> chips = ((List<ISOChip>)value);
				for(ISOChip chip:chips){
					length += 2;//tag name
					length += 2;//tag length
					length += chip.getLength();//tag data
				}
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
		} else if (t == ISOType.LLLBITMAPVAR) {
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else {
				length += 8;
				int l = ((ISOBitMapVar)value).getFields().getLength();
				length += l / 2 + (l % 2);
			}
			if (t == ISOType.LLLBITMAPVAR && length > 999) {
				throw new IllegalArgumentException(
						"LLLBITMAPVAR can only hold values up to 999 chars");
			}
		} else if (type == ISOType.LLBINHEX || type == ISOType.LLLBINHEX) {
			if (value instanceof byte[]) {
				length = ((byte[]) value).length;
			} else {
				length = value.toString().length() / 2
						+ (value.toString().length() % 2);
			}
			if (t == ISOType.LLBINHEX && length > 255) {
				throw new IllegalArgumentException(
						"LLBIN can only hold values up to 255 chars");
			} else if (t == ISOType.LLLBINHEX && length > 4095) {
				throw new IllegalArgumentException(
						"LLLBIN can only hold values up to 4095 chars");
			}
		} else {
			length = type.getLength();
		}
	}

	/**
	 * Creates a new instance that stores the specified value as the specified
	 * type. Useful for storing fixed-length value types.
	 * 
	 * @param t
	 *            The ISO8583 type for this field.
	 * @param val
	 *            The value to store in the field.
	 * @param len
	 *            The length for the value.
	 */
	@SuppressWarnings("unchecked")
	public ISOValue(ISOType t, T val, int len, String desc) {
		type = t;
		value = val;
		length = len;
		description = desc;
		this.nestedValue = false;
		if (length == 0 && t.needsLength()) {
			throw new IllegalArgumentException(
					String.format(
							"Length must be greater than zero for type %s (value '%s')",
							t, val));
		} else if(val instanceof ISO8583Fields){ 
			if (len == 0) {
				length = ((ISO8583Fields)val).getLength();
			}
		} else if ((t == ISOType.LLLTOKENS || t == ISOType.LLLLTOKENS) && val instanceof List<?>) {
			if (len == 0) {
				length+=1;//Eye-catcher
				length+=1;//User-FLD1 
				length+=5;//CNT
				length+=5;//Lgth
				for(ISOToken token : (List<ISOToken>)val){
					length+=1;//Eye-catcher
					length+=1;//Filler
					length+=2;//TKN-ID
					length+=5;//Lgth
					length+=1;//Filler
					length+=token.getFields().getLength();
				}
			}
		} else if (t == ISOType.VAR) {
			if (len == 0) {
				length = val.toString().length();
			}else{
				length += val.toString().length();
			}
		} else if (t == ISOType.LVAR) {
			if(val instanceof String[]){
//				length += len;
				for(String s : (String[])val){
					length += s.length();
				}
			}else{
				if (len == 0) {
					length = val.toString().length();
				}else{
					length += val.toString().length();
				}
			}
		} else if (t == ISOType.LLVAR || t == ISOType.LLLVAR) {
			if (len == 0) {
				length = val.toString().length();
			}
			if (t == ISOType.LLVAR && length > 99) {
				throw new IllegalArgumentException(
						"LLVAR can only hold values up to 99 chars");
			} else if (t == ISOType.LLLVAR && length > 999) {
				throw new IllegalArgumentException(
						"LLLVAR can only hold values up to 999 chars");
			}
		} else if (t == ISOType.LLBIN || t == ISOType.LLLBIN) {
			if (len == 0) {
				length = ((byte[]) val).length;
			}
			if (t == ISOType.LLBIN && length > 99) {
				throw new IllegalArgumentException(
						"LLBIN can only hold values up to 99 chars");
			} else if (t == ISOType.LLLBIN && length > 999) {
				throw new IllegalArgumentException(
						"LLLBIN can only hold values up to 999 chars");
			}
		} else if (t == ISOType.LLLTABLES) {
			if (len == 0) {
				length = ((byte[]) val).length;
			}
//			if (t == ISOType.LLLTABLES && length > 999) {
//				throw new IllegalArgumentException(
//						"LLLTABLES can only hold values up to 999 chars");
//			}
		} else if (t == ISOType.LLLCHIP) {
			if (len == 0) {
				length = ((byte[]) val).length;
			}
			if (t == ISOType.LLLCHIP && length > 999) {
				throw new IllegalArgumentException(
						"LLLCHIP can only hold values up to 999 chars");
			}
		} else if (t == ISOType.LLZ) {
			if (len == 0) {
				length = ((String) val).length() / 2 + ((String) val).length()
						% 2;
			}
			if (t == ISOType.LLZ && length > 99) {
				throw new IllegalArgumentException(
						"LLZ can only hold values up to 99 chars");
			}
		} else if (t == ISOType.LLLTOKENS || t == ISOType.LLLLTOKENS) {
			if (len == 0) {
				length = ((byte[]) val).length;
			}
			if (t == ISOType.LLLTOKENS && length > 999) {
				throw new IllegalArgumentException(
						"LLLTOKENS can only hold values up to 999 chars");
			}
			if (t == ISOType.LLLLTOKENS && length > 9999) {
				throw new IllegalArgumentException(
						"LLLLTOKENS can only hold values up to 9999 chars");
			}
		} else if(t == ISOType.POSKITZTAG){
			if (len == 0) {
				length = ((byte[]) val).length;
			}
		} else if(t == ISOType.SMARTPAYCHIP){
			if (len == 0) {
				if(value instanceof SmartPayChip){
					SmartPayChip chip = ((SmartPayChip)value);
					length += 1;//sLen
					length += 8;//S/N
					length += 2;//tLen
					length += chip.getTac().length;//tac
					length += 1;//mLen
					length += 15;//mcc
				}else{
					length = ((byte[]) val).length;
				}
			}
		} else if (t == ISOType.LLLBITMAPVAR) {
			if (len == 0) {
				length = ((byte[]) val).length;
			}
			if (t == ISOType.LLLBITMAPVAR && length > 999) {
				throw new IllegalArgumentException(
						"LLLBITMAPVAR can only hold values up to 999 chars");
			}
		}else if (t == ISOType.LLBINHEX || t == ISOType.LLLBINHEX) {
			if (len == 0) {
				length = ((byte[]) val).length;
			}
			if (t == ISOType.LLBINHEX && length > 255) {
				throw new IllegalArgumentException(
						"LLBIN can only hold values up to 255 chars");
			} else if (t == ISOType.LLLBINHEX && length > 4095) {
				throw new IllegalArgumentException(
						"LLLBIN can only hold values up to 4095 chars");
			}
		}
	}
	
	/**
	 * Creates a new instance that stores the specified value as the specified
	 * type. Useful for storing fixed-length value types.
	 * 
	 * @param t
	 *            The ISO8583 type for this field.
	 * @param val
	 *            The value to store in the field.
	 * @param len
	 *            The length for the value.
	 */
	@SuppressWarnings("unchecked")
	public ISOValue(ISOType t, T val, int len, String desc, boolean isNested) {
		this.type = t;
		this.value = val;
		this.length = len;
		this.description = desc;
		this.nestedValue = isNested;
		if (length == 0 && t.needsLength()) {
			throw new IllegalArgumentException(
					String.format(
							"Length must be greater than zero for type %s (value '%s')",
							t, val));
		} else if(isNested && val instanceof ISO8583Fields){ 
			if (len == 0) {
				length = ((ISO8583Fields)val).getLength();
			}
		} else if ((t == ISOType.LLLTOKENS || t == ISOType.LLLLTOKENS) && val instanceof List<?>) {
			if (len == 0) {
				length+=1;//Eye-catcher
				length+=1;//User-FLD1 
				length+=5;//CNT
				length+=5;//Lgth
				for(ISOToken token : (List<ISOToken>)val){
					length+=1;//Eye-catcher
					length+=1;//Filler
					length+=2;//TKN-ID
					length+=5;//Lgth
					length+=1;//Filler
					length+=token.getFields().getLength();
				}
			}
		} else if (t == ISOType.LVAR) {
			if(val instanceof String[]){
				//length += len;
				for(String s : (String[])val){
					length += s.length();
				}
			}else{
				if (len == 0) {
					length = val.toString().length();
				}else{
					length += val.toString().length();
				}
			}
		}else if (t == ISOType.LLVAR || t == ISOType.LLLVAR) {
			if (len == 0) {
				length = val.toString().length();
			}
			if (t == ISOType.LLVAR && length > 99) {
				throw new IllegalArgumentException(
						"LLVAR can only hold values up to 99 chars");
			} else if (t == ISOType.LLLVAR && length > 999) {
				throw new IllegalArgumentException(
						"LLLVAR can only hold values up to 999 chars");
			}
		} else if (t == ISOType.LLBIN || t == ISOType.LLLBIN) {
			if (len == 0) {
				length = ((byte[]) val).length;
			}
			if (t == ISOType.LLBIN && length > 99) {
				throw new IllegalArgumentException(
						"LLBIN can only hold values up to 99 chars");
			} else if (t == ISOType.LLLBIN && length > 999) {
				throw new IllegalArgumentException(
						"LLLBIN can only hold values up to 999 chars");
			}
		} else if (t == ISOType.LLZ) {
			if (len == 0) {
				length = ((String) val).length() / 2 + ((String) val).length()
						% 2;
			}
			if (t == ISOType.LLZ && length > 99) {
				throw new IllegalArgumentException(
						"LLZ can only hold values up to 99 chars");
			}
		} else if (t == ISOType.LLBINHEX || t == ISOType.LLLBINHEX) {
			if (len == 0) {
				length = ((byte[]) val).length;
			}
			if (t == ISOType.LLBINHEX && length > 255) {
				throw new IllegalArgumentException(
						"LLBIN can only hold values up to 255 chars");
			} else if (t == ISOType.LLLBINHEX && length > 4095) {
				throw new IllegalArgumentException(
						"LLLBIN can only hold values up to 4095 chars");
			}
		}
	}

	public String getDescription() {
		return description;
	}

	public boolean isNestedValue() {
		return nestedValue;
	}

	/** Returns the ISO type to which the value must be formatted. */
	public ISOType getType() {
		return type;
	}

	/**
	 * Returns the length of the stored value, of the length of the formatted
	 * value in case of NUMERIC or ALPHA. It doesn't include the field length
	 * header in case of LLVAR or LLLVAR.
	 */
	public int getLength() {
		return length;
	}

	/** Returns the stored value without any conversion or formatting. */
	public T getValue() {
		return value;
	}

	public void setCharacterEncoding(String value) {
		encoding = value;
	}

	public String getCharacterEncoding() {
		return encoding;
	}

	/**
	 * Returns the formatted value as a String. The formatting depends on the
	 * type of the receiver.
	 */
	public String toString() {
		if (value == null) {
			return "ISOValue<null>";
		}
		if(isNestedValue()){
			if(type == ISOType.LLLTABLES){
				StringBuffer sb = new StringBuffer();
				sb.append("\n");
				@SuppressWarnings("unchecked")
				List<ISOTable> tables = (List<ISOTable>)value;
				for(ISOTable table: tables){
					sb.append("----------").append(table.getDescription()).append("----------\n");
					sb.append(table.toString());
					sb.append("\n");
				}
				return sb.toString();
			} else if(type == ISOType.LLLTOKENS || type == ISOType.LLLLTOKENS){
				StringBuffer sb = new StringBuffer();
				sb.append("\n");
				@SuppressWarnings("unchecked")
				List<ISOToken> tokens = (List<ISOToken>)value;
				for(ISOToken token: tokens){
					sb.append("----------").append(token.getDescription()).append("----------\n");
					sb.append(token.toString());
					sb.append("\n");
				}
				return sb.toString();
			} else {
				StringBuffer sb = new StringBuffer();
				sb.append("\n");
				sb.append("********").append(getDescription()).append("********\n");
				sb.append(value.toString());
				sb.append("\n");
				return sb.toString();
			}
		} else if (type == ISOType.NUMERIC || type == ISOType.AMOUNT) {
			if (type == ISOType.AMOUNT) {
				if (value instanceof BigDecimal) {
					return type.format((BigDecimal) value, 12);
				} else {
					return type.format(value.toString(), 12);
				}
			} else if (value instanceof Number) {
				return type.format(((Number) value).longValue(), length);
			} else {
				return type.format(value.toString(), length);
			}
		} else if (type == ISOType.ALPHA) {
			return type.format(value.toString(), length);
		} else if (type == ISOType.LLVAR || type == ISOType.LLLVAR) {
			return value.toString();
		} else if (value instanceof Date) {
			return type.format((Date) value);
		} else if (type == ISOType.BINARY) {
			if (value instanceof byte[]) {
				final byte[] _v = (byte[]) value;
				return type.format(CommonFunction.bytesToHex(_v), length * 2);
			} else {
				return type.format(value.toString(), length * 2);
			}
		} else if (type == ISOType.CHINESE) {
			if (value instanceof byte[]) {
				final byte[] _v = (byte[]) value;
				try {
					return new String(_v, getCharacterEncoding());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				return value.toString();
			}
		} else if (type == ISOType.LLBIN || type == ISOType.LLLBIN || type == ISOType.LLBINHEX || type == ISOType.LLLBINHEX) {
			if (value instanceof byte[]) {
				final byte[] _v = (byte[]) value;
				return CommonFunction.bytesToHex(_v, " ");
			} else {
				final String _s = value.toString();
				return (_s.length() % 2 == 1) ? String.format("0%s", _s) : _s;
			}
		} else if(type == ISOType.LBINARY){
			if (value instanceof byte[]) {
				final byte[] _v = (byte[]) value;
				return CommonFunction.bytesToHex(_v, " ");
			} else {
				final String _s = value.toString();
				return (_s.length() % 2 == 1) ? String.format("0%s", _s) : _s;
			}
		} else if (type == ISOType.LLZ) {
			return value.toString();
		} else if(type == ISOType.CHECKSUM){
			if (value instanceof byte[]) {
				final byte[] _v = (byte[]) value;
				return CommonFunction.hexEncode(_v[_v.length-1]);
			} else {
				return value.toString();
			}
		} else if(type == ISOType.LLLCHIP || type == ISOType.SMARTPAYTAG){
			StringBuffer sb = new StringBuffer();
			if(value instanceof List<?>){
				sb.append("\n");
				@SuppressWarnings("unchecked")
				List<ISOChip> chips = (List<ISOChip>)value;
				for(ISOChip chip: chips){
					sb.append("tag[").append(CommonFunction.bytesToHex(chip.getTag(), " ")).append("]");
					sb.append("data[").append(CommonFunction.bytesToHex(chip.getData(), " ")).append("]");
					sb.append(" //").append(chip.getDesc());
					sb.append("\n");
				}
			} else if(value instanceof ISOChip){
				ISOChip chip = (ISOChip)value;
				sb.append("tag[").append(CommonFunction.bytesToHex(chip.getTag(), " ")).append("]");
				sb.append("data[").append(CommonFunction.bytesToHex(chip.getData(), " ")).append("]");
				sb.append(" //").append(chip.getDesc());
				sb.append("\n");
			} else{
				sb.append(value.toString()).append("\n");
			}
			return sb.toString();
		} else if(type == ISOType.POSKITZTAG){
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			PoskitzTag poskitzTag = (PoskitzTag)value;
			sb.append("id[").append(poskitzTag.getId()).append("]");
			sb.append("\n");
			sb.append("length[").append(poskitzTag.getLength()).append("]");
			sb.append("\n");
			for(ISOChip chip: poskitzTag.getTags()){
				sb.append("tag[").append(CommonFunction.bytesToHex(chip.getTag(), " ")).append("]");
				sb.append("data[").append(CommonFunction.bytesToHex(chip.getData(), " ")).append("]");
				sb.append(" //").append(chip.getDesc());
				sb.append("\n");
			}
			sb.append("crc[").append(CommonFunction.hexEncode(poskitzTag.getCrc())).append("]");
			sb.append("\n");
			return sb.toString();
		} else if(type == ISOType.SMARTPAYCHIP){
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			SmartPayChip smartPayChip = (SmartPayChip)value;
			sb.append("S/N[").append(smartPayChip.getSerialNumber()).append("]");
			sb.append("\n");
			sb.append("TAC[").append(CommonFunction.bytesToHex(smartPayChip.getTac())).append("]");
			sb.append("\n");
			sb.append("MCC[").append(smartPayChip.getMcc()).append("]");
			sb.append("\n");
			return sb.toString();
		} else if(type == ISOType.LVAR){
			StringBuffer sb = new StringBuffer();
			if(value instanceof String){
				sb.append(value);
			}else{
				String[] _vs = (String[])value;
				int count = 1;
				for(String s:_vs){
					sb.append("value ").append(count);
					sb.append("[").append(s).append("]\n");
					count++;
				}
			}
			return sb.toString();
		} else if(type == ISOType.B24TLV){
			StringBuffer sb = new StringBuffer();
			if(value instanceof String){
				sb.append(value);
			}else if(value instanceof List<?>){
				@SuppressWarnings("unchecked")
				List<B24Tlv> b24tlvs = (List<B24Tlv>)value;
				for(B24Tlv tlv:b24tlvs) {
					sb.append(tlv).append("\n");
				}
			}
			return sb.toString();
		}
		return value.toString();
	}

	/** Returns a copy of the receiver that references the same value object. */
	public ISOValue<T> clone() {
//		try {
			return (ISOValue<T>) new Cloner().deepClone(new ISOValue<T>(type, value, length, description, nestedValue));
//			return (ISOValue<T>) super.clone();
//		} catch (CloneNotSupportedException ex) {
//			return null;
//		}
	}

	/**
	 * Returns true of the other object is also an ISOValue and has the same
	 * type and length, and if other.getValue().equals(getValue()) returns true.
	 */
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ISOValue<?>)) {
			return false;
		}
		ISOValue<?> comp = (ISOValue<?>) other;
		return (comp.getType() == getType()
				&& comp.getValue().equals(getValue()) && comp.getLength() == getLength());
	}

	@Override
	public int hashCode() {
		return value == null ? 0 : toString().hashCode();
	}
}
