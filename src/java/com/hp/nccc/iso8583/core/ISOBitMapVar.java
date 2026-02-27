package com.hp.nccc.iso8583.core;

import java.io.Serializable;
import java.util.BitSet;

public class ISOBitMapVar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2399208717894042694L;
	private BitSet bitMap;
	private int length;
	private ISO8583Fields fields = new ISO8583Fields();
	private String description;
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public ISO8583Fields getFields() {
		return fields;
	}
	public void setFields(ISO8583Fields fields) {
		this.fields = fields;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public BitSet getBitMap() {
		return bitMap;
	}
	public void setBitMap(BitSet bitMap) {
		this.bitMap = bitMap;
	}
	public String toString(){
		return fields.toString();
	}
}
