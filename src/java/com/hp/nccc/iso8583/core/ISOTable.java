/**
 * 
 */
package com.hp.nccc.iso8583.core;

import java.io.Serializable;

/**
 * @author hsiehes
 *
 */
public class ISOTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 53799006837591201L;
	private int length;
	private String id;
	private ISO8583Fields fields = new ISO8583Fields();
	private String description;
	private String lengthType;
	
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
	
	public String toString(){
		return fields.toString();
	}
	public String getLengthType() {
		return lengthType;
	}
	public void setLengthType(String lengthType) {
		this.lengthType = lengthType;
	}
}
