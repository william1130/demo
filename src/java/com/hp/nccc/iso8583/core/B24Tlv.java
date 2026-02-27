/**
 * 
 */
package com.hp.nccc.iso8583.core;

import java.io.Serializable;

/**
 * @author hsiehes
 * 
 */
public class B24Tlv implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3202757619558098120L;
	private String tag;
	private int length;
	private String data;
	private String desc;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
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
		sb.append("tag[").append(this.getTag()).append("]");
		sb.append("data[").append(this.getData()).append("]");
		sb.append(" //").append(this.getDesc());
		return sb.toString();
	}
	
	public String toData(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.tag);
		sb.append(String.format("%03d", this.length));
		sb.append(this.data);
		return sb.toString();
	}
}
