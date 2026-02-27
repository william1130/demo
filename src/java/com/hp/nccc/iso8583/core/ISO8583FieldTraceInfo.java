package com.hp.nccc.iso8583.core;

import java.io.Serializable;

import com.hp.nccc.iso8583.common.CommonFunction;

public class ISO8583FieldTraceInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1926621422077188117L;
	private int startPos;
	private int endPos;
	private byte[] byteData;
	private ISOValue<?> value;
	private String desc;
	private String configParseId;
	private String superConfigParseId;
	private int field;
	private int superField;
	private boolean isBinary;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("i[%s] n[%d] p[%d-%d] c[%s] d[%s] v[%s]", configParseId, field, startPos, endPos,
				isBinary?CommonFunction.bytesToHex(byteData):new String(byteData), desc, value==null?"":value.toString()));
		return sb.toString();
	}

	public boolean isBinary() {
		return isBinary;
	}

	public void setBinary(boolean isBinary) {
		this.isBinary = isBinary;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public ISOValue<?> getValue() {
		return value;
	}

	public void setValue(ISOValue<?> value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getConfigParseId() {
		return configParseId;
	}

	public void setConfigParseId(String configParseId) {
		this.configParseId = configParseId;
	}

	public String getSuperConfigParseId() {
		return superConfigParseId;
	}

	public void setSuperConfigParseId(String superConfigParseId) {
		this.superConfigParseId = superConfigParseId;
	}

	public int getField() {
		return field;
	}

	public void setField(int field) {
		this.field = field;
	}

	public int getSuperField() {
		return superField;
	}

	public void setSuperField(int superField) {
		this.superField = superField;
	}
}
