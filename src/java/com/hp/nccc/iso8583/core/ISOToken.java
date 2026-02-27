/**
 * 
 */
package com.hp.nccc.iso8583.core;

import java.io.Serializable;

/**
 * @author hsiehes
 *
 */
public class ISOToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5431053580898194107L;
	private String tknId;
	private int lgth;
	private ISO8583Fields fields = new ISO8583Fields();
	private String description;
	public String getTknId() {
		return tknId;
	}
	public void setTknId(String tknId) {
		this.tknId = tknId;
	}
	public int getLgth() {
		if(lgth == 0 && fields != null){
			return fields.getLength(false);
		}
		return lgth;
	}
	public void setLgth(int lgth) {
		this.lgth = lgth;
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
}
