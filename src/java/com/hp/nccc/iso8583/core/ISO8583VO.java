/**
 * 
 */
package com.hp.nccc.iso8583.core;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Map;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.common.ISO8583Context.MTIClass;
import com.hp.nccc.iso8583.common.ISO8583Context.MTIMode;
import com.hp.nccc.iso8583.common.ISO8583Context.MTIModeSimple;

/**
 * @author hsiehes
 * 
 */
public class ISO8583VO implements Serializable {
	private String iso8583Type;
	
	public String getIso8583Type() {
		return iso8583Type;
	}

	public void setIso8583Type(String iso8583Type) {
		this.iso8583Type = iso8583Type;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 927630066401822520L;
	
	private String id = "default";
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	private String isoHeader;
	public String getIsoHeader() {
		return isoHeader;
	}

	public void setIsoHeader(String isoHeader) {
		this.isoHeader = isoHeader;
	}

	public boolean isTPDU(){
		return tpdu != null;
	}
	
	private int length;
	private MTI mti = new MTI();
	private TPDU tpdu;
	private ISO8583Fields fields = new ISO8583Fields();
	private String encoding = System.getProperty("file.encoding");
	private String desc = System.getProperty("file.desc");

	/** Sets the encoding to use. */
	public void setCharacterEncoding(String value) {
		if (value == null) {
			throw new IllegalArgumentException("Cannot set null encoding.");
		}
		encoding = value;
	}

	
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Returns the character encoding for Strings inside the message. Default is
	 * taken from the file.encoding system property.
	 */
	public String getCharacterEncoding() {
		return encoding;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public MTI getMti() {
		return mti;
	}

	public void setMti(MTI mti) {
		this.mti = mti;
	}

	public TPDU getTpdu() {
		return tpdu;
	}

	public void setTpdu(TPDU tpdu) {
		this.tpdu = tpdu;
	}

	public BitSet getBitMap() {
		return bitMap;
	}

	public void setBitMap(BitSet bitMap) {
		this.bitMap = bitMap;
	}
	
	public void createNewMTI(){
		this.mti = new MTI();
	}

	public class MTI implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1496256881976861457L;
		private MTIClass mtic;
		private MTIMode mtim;

		public MTIClass getMtic() {
			return mtic;
		}

		public void setMtic(MTIClass mtic) {
			this.mtic = mtic;
		}

		public MTIMode getMtim() {
			return mtim;
		}

		public void setMtim(MTIMode mtim) {
			this.mtim = mtim;
		}

		public byte[] toBytes() {
			return new byte[] { MTIClass.toByte(mtic), MTIMode.toByte(mtim) };
		}
		
		public MTIMode getResponse(){
			switch(mtim){
			case Interactive:
				return MTIMode.Interactive_Response;
			case Interactive_Response:
				return MTIMode.Interactive_Response;
			case Non_Interactive_Advice:
				return MTIMode.Non_Interactive_Advice_Response;
			case Non_Interactive_Advice_Response:
				return MTIMode.Non_Interactive_Advice_Response;
			case Advice_Repeat:
				return MTIMode.Non_Interactive_Advice_Response;
			case Issuer_Reconciliation_Advice:
				return MTIMode.Issuer_Reconciliation_Advice_Response;
			case Issuer_Reconciliation_Advice_Repeat:
				return MTIMode.Issuer_Reconciliation_Advice_Response;
			case Issuer_Reconciliation_Advice_Response:
				return MTIMode.Issuer_Reconciliation_Advice_Response;
			case Issuer_Reconciliation_Request:
				return MTIMode.Issuer_Reconciliation_Request_Response;
			case Issuer_Reconciliation_Request_Response:
				return MTIMode.Issuer_Reconciliation_Request_Response;
			default:
				return MTIMode.Interactive_Response;
			}
		}
		
		public String toString(){
			String mticS = "";
			if(mtic != null){
				mticS = mtic.toString();
			}
			String mtimS = "";
			if(mtim != null){
				mtimS = mtim.toString();
			}
			return mticS + mtimS;
		}
		
		public MTIModeSimple getMtimSimple(){
			return MTIModeSimple.convertMTIModeSimple(mtim);
		}
	}
	
	public void createNewTPDU(){
		this.tpdu = new TPDU();
	}

	public class TPDU implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2355796431745659203L;
		private byte ID;
		private byte[] destinationAddr;
		private byte[] originationAddr;

		public byte getID() {
			return ID;
		}

		public void setID(byte iD) {
			ID = iD;
		}

		public byte[] getDestinationAddr() {
			return destinationAddr;
		}

		public void setDestinationAddr(byte[] destinationAddr) {
			this.destinationAddr = destinationAddr;
		}

		public byte[] getOriginationAddr() {
			return originationAddr;
		}

		public void setOriginationAddr(byte[] originationAddr) {
			this.originationAddr = originationAddr;
		}

		public byte[] toBytes() {
			return ByteBuffer.allocate(5).put(ID).put(destinationAddr)
					.put(originationAddr).array();
		}
		
		public String toString(){
			return CommonFunction.bytesToHex(toBytes(), " ");
		}
	}

	private BitSet bitMap;

	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Returns the stored value in the field, without converting or formatting
	 * it.
	 * 
	 * @param field
	 *            The field number. 1 is the secondary bitmap and is not
	 *            returned as such; real fields go from 2 to 128.
	 */
	public <T> T getObjectValue(int field) {
		return fields.getObjectValue(field);
	}

	/** Returns the ISOValue for the specified field. First real field is 2. */
	public <T> ISOValue<T> getField(int field) {
		return fields.getField(field);
	}

	/**
	 * Stored the field in the specified index. The first field is the secondary
	 * bitmap and has index 1, so the first valid value for index must be 2.
	 * 
	 * @return The receiver (useful for setting several fields in sequence).
	 */
	public ISO8583VO setField(int index, ISOValue<?> field) {
//		if (index < 2 || index > 128) {
//			throw new IndexOutOfBoundsException(
//					"Field index must be between 2 and 128");
//		}
		if (field != null) {
			field.setCharacterEncoding(encoding);
		}
		fields.setField(index, field);
		return this;
	}

	/** Convenience method for setting several fields in one call. */
	public ISO8583VO setFields(Map<Integer, ISOValue<?>> values) {
		for (Map.Entry<Integer, ISOValue<?>> e : values.entrySet()) {
			setField(e.getKey(), e.getValue());
		}
		return this;
	}

	/**
	 * Sets the specified value in the specified field, creating an ISOValue
	 * internally.
	 * 
	 * @param index
	 *            The field number (2 to 128)
	 * @param value
	 *            The value to be stored.
	 * @param t
	 *            The ISO type.
	 * @param length
	 *            The length of the field, used for ALPHA and NUMERIC values
	 *            only, ignored with any other type.
	 * @return The receiver (useful for setting several values in sequence).
	 */
	public <T> ISO8583VO setValue(int index, T value, ISOType t, int length, String desc) {
		if (index < 2 || index > 128) {
			throw new IndexOutOfBoundsException(
					"Field index must be between 2 and 128");
		}
		if (value == null) {
			fields.setField(index, null);
		} else {
			ISOValue<T> v = null;
			if (t.needsLength()) {
				v = new ISOValue<T>(t, value, length, desc);
			} else {
				v = new ISOValue<T>(t, value, desc);
			}
			v.setCharacterEncoding(encoding);
			fields.setField(index, v);
		}
		return this;
	}

	/**
	 * A convenience method to set new values in fields that already contain
	 * values. The field's type, length and custom encoder are taken from the
	 * current value. This method can only be used with fields that have been
	 * previously set, usually from a template in the MessageFactory.
	 * 
	 * @param index
	 *            The field's index
	 * @param value
	 *            The new value to be set in that field.
	 * @return The message itself.
	 * @throws IllegalArgumentException
	 *             if there is no current field at the specified index.
	 */
	public <T> ISO8583VO updateValue(int index, T value, String desc) {
		ISOValue<T> current = getField(index);
		if (current == null) {
			throw new IllegalArgumentException(
					"Value-only field setter can only be used on existing fields");
		} else {
			setValue(index, value, current.getType(), current.getLength(), desc);
			getField(index)
					.setCharacterEncoding(current.getCharacterEncoding());
		}
		return this;
	}

	/**
	 * Returns true is the message has a value in the specified field.
	 * 
	 * @param idx
	 *            The field number.
	 */
	public boolean hasField(int idx) {
		return fields.hasField(idx);
	}

	// These are for Groovy compat
	/**
	 * Sets the specified value in the specified field, just like
	 * {@link #setField(int, ISOValue)}.
	 */
	public <T> void putAt(int i, ISOValue<T> v) {
		setField(i, v);
	}

	/**
	 * Returns the ISOValue in the specified field, just like
	 * {@link #getField(int)}.
	 */
	public <T> ISOValue<T> getAt(int i) {
		return getField(i);
	}

	// These are for Scala compat
	/**
	 * Sets the specified value in the specified field, just like
	 * {@link #setField(int, ISOValue)}.
	 */
	public <T> void update(int i, ISOValue<T> v) {
		setField(i, v);
	}

	/**
	 * Returns the ISOValue in the specified field, just like
	 * {@link #getField(int)}.
	 */
	public <T> ISOValue<T> apply(int i) {
		return getField(i);
	}

	/**
	 * Copies the specified fields from the other message into the recipient. If
	 * a specified field is not present in the source message it is simply
	 * ignored.
	 */
	public void copyFieldsFrom(ISO8583VO src, int... idx) {
		for (int i : idx) {
			ISOValue<Object> v = src.getField(i);
			if (v != null) {
				setValue(i, v.getValue(), v.getType(), v.getLength(), v.getDescription());
			}
		}
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("length:[%d]", this.getLength())).append("\n");
		if (this.getMti() != null) {
			sb.append(
					String.format("message type:[%s]",
							CommonFunction.bytesToHex(this.getMti().toBytes()))).append("\n");
		}

		for (int i = 1; i <= 128; i++) {
			if (this.hasField(i)) {
				String s = "";
				ISOValue<?> v = this.getField(i);
				s = v.toString();
				sb.append(String.format("Field%d-[%s] //%s", i, s, v.getDescription()))
						.append("\n");
			}
		}
		return sb.toString();
	}
}
