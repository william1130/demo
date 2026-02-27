package com.hp.nccc.iso8583.core;

import java.io.Serializable;
import java.util.Map;
/**
 * 
 */

/**
 * @author hsiehes
 *
 */
public class ISO8583Fields implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1207139411723327255L;
	private ISOValue<?>[] fields = new ISOValue<?>[129];
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the stored value in the field, without converting or formatting
	 * it.
	 * 
	 * @param field
	 *            The field number. fields go from 1 to 128.
	 */
	public <T> T getObjectValue(int field) {
		@SuppressWarnings("unchecked")
		ISOValue<T> v = (ISOValue<T>)fields[field];
		return v == null ? null : v.getValue();
	}

	/** Returns the ISOValue for the specified field. */
	@SuppressWarnings("unchecked")
	public <T> ISOValue<T> getField(int field) {
		return (ISOValue<T>)fields[field];
	}

	/**
	 * Stored the field in the specified index. The first field is the secondary
	 * bitmap and has index 1, so the first valid value for index must be 2.
	 * 
	 * @return The receiver (useful for setting several fields in sequence).
	 */
	public ISO8583Fields setField(int index, ISOValue<?> field) {
		if (index < 1 || index > 128) {
			throw new IndexOutOfBoundsException(
					"Field index must be between 1 and 128");
		}
		fields[index] = field;
		return this;
	}

	/** Convenience method for setting several fields in one call. */
	public ISO8583Fields setFields(Map<Integer, ISOValue<?>> values) {
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
	 *            The field number (1 to 128)
	 * @param value
	 *            The value to be stored.
	 * @param t
	 *            The ISO type.
	 * @param length
	 *            The length of the field, used for ALPHA and NUMERIC values
	 *            only, ignored with any other type.
	 * @return The receiver (useful for setting several values in sequence).
	 */
	public <T> ISO8583Fields setValue(int index, T value, ISOType t, int length, String desc) {
		if (index < 1 || index > 128) {
			throw new IndexOutOfBoundsException(
					"Field index must be between 1 and 128");
		}
		if (value == null) {
			fields[index] = null;
		} else {
			ISOValue<T> v = null;
			if (t.needsLength()) {
				v = new ISOValue<T>(t, value, length, desc);
			} else {
				v = new ISOValue<T>(t, value, desc);
			}
			fields[index] = v;
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
	public <T> ISO8583Fields updateValue(int index, T value, String desc) {
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
		return fields[idx] != null;
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
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		boolean first=true;
		for(int i=1;i<129;i++){
			if(hasField(i)){
				if(!first){
					sb.append("\n");
				}
				ISOValue<?> value = getField(i);
				sb.append("[").append(value.toString()).append("] //").append(value.getDescription());
				first = false;
			}
		}
		return sb.toString();
	}
	
	public int getLength(){
		return getLength(true);
	}
	
	public int getLength(boolean isBinary){
		int length = 0;
		for(ISOValue<?> val : fields){
			if(val != null){
				length += val.getLength();
				if(isBinary){
					if (val.getType() == ISOType.LLVAR || val.getType() == ISOType.LLBIN
							|| val.getType() == ISOType.LLZ) {
						length++;
					} else if (val.getType() == ISOType.LLLVAR || val.getType() == ISOType.LLLBIN
							|| val.getType() == ISOType.LLLTABLES || val.getType() == ISOType.LLLCHIP
							|| val.getType() == ISOType.SMARTPAYCHIP || val.getType() == ISOType.SMARTPAYTAG) {
						length += 2;
					}
				}else{
					if (val.getType() == ISOType.LLVAR || val.getType() == ISOType.LLBIN
							|| val.getType() == ISOType.LLZ) {
						length += 2;
					} else if (val.getType() == ISOType.LLLVAR || val.getType() == ISOType.LLLBIN
							|| val.getType() == ISOType.LLLTOKENS || val.getType() == ISOType.LLLCHIP 
							|| val.getType() == ISOType.LLLBITMAPVAR) {
						length += 3;
					} else if (val.getType() == ISOType.LLLLTOKENS) {
						length += 4;
					}
				}
				
			}
		}
		return length;
	}
}
