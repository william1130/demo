package com.hp.nccc.iso8583.core;

import java.io.Serializable;
import java.util.List;

import com.hp.nccc.iso8583.common.CommonFunction;

public class PoskitzTag implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5474060277116030802L;
	private List<ISOChip> tags;
	private int length;
	private String id;
	private byte crc;
	public byte getCrc() {
		return crc;
	}
	public void setCrc(byte crc) {
		this.crc = crc;
	}
	public List<ISOChip> getTags() {
		return tags;
	}
	public void setTags(List<ISOChip> tags) {
		this.tags = tags;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("id[").append(this.getId()).append("]");
		sb.append("\n");
		sb.append("length[").append(this.getLength()).append("]");
		sb.append("\n");
		for(ISOChip chip: this.getTags()){
			sb.append("tag[").append(CommonFunction.bytesToHex(chip.getTag(), " ")).append("]");
			sb.append("data[").append(CommonFunction.bytesToHex(chip.getData(), " ")).append("]");
			sb.append(" //").append(chip.getDesc());
			sb.append("\n");
		}
		sb.append("crc[").append(CommonFunction.hexEncode(this.getCrc())).append("]");
		sb.append("\n");
		return sb.toString();
	}
}
