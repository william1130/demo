/**
 * 
 */
package com.hp.nccc.iso8583.core;

import java.io.Serializable;

import com.hp.nccc.iso8583.common.CommonFunction;

/**
 * @author hsiehes
 * 
 */
public class ISOChip implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3202757619558098120L;
	private byte[] tag;
	private int length;
	private byte[] data;
	private String desc;

	public byte[] getTag() {
		return tag;
	}

	public void setTag(byte[] tag) {
		this.tag = tag;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("tag[").append(CommonFunction.bytesToHex(this.getTag(), " ")).append("]");
		sb.append("data[").append(CommonFunction.bytesToHex(this.getData(), " ")).append("]");
		sb.append(" //").append(this.getDesc());
		return sb.toString();
	}
}
