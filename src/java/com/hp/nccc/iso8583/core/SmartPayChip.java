package com.hp.nccc.iso8583.core;

import java.io.Serializable;

public class SmartPayChip implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7495409193500681465L;
	private String serialNumber;
	private byte[] tac;
	private String mcc;
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public byte[] getTac() {
		return tac;
	}
	public void setTac(byte[] tac) {
		this.tac = tac;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	
}
