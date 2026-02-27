/**
 * 
 */
package com.hp.nccc.iso8583.core;

import com.hp.nccc.iso8583.common.CommonFunction;

/**
 * @author hsiehes
 * 
 */
public class ISOCheckSum {
	private ISO8583Fields fields;
	private byte checkSum;

	public ISO8583Fields getFields() {
		return fields;
	}

	public void setFields(ISO8583Fields fields) {
		this.fields = fields;
	}

	public byte getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(byte checkSum) {
		this.checkSum = checkSum;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (fields != null) {
			sb.append("[").append(CommonFunction.hexEncode(checkSum)).append("] //").append("check sum").append("\n");
			sb.append(fields.toString());
		} else {
			sb.append(CommonFunction.hexEncode(checkSum));
		}
		return sb.toString();
	}
}
