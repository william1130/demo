/**
 * 
 */
package com.hp.nccc.iso8583.exception;

import java.text.ParseException;

import com.hp.nccc.iso8583.core.ISO8583TraceInfo;

/**
 * @author hsiehes
 *
 */
public class ISO8583ParseException extends ParseException {
	
	private int fieldNo = -1;
	private int rootFieldNo = -1;

	public ISO8583ParseException(String arg0, int arg1) {
		super(arg0, arg1);
	}
	
	public ISO8583ParseException(String arg0, int arg1, int fieldNo) {
		super(arg0, arg1);
		this.fieldNo = fieldNo;
	}
	
	public int getRootFieldNo() {
		return rootFieldNo;
	}

	public void setRootFieldNo(int rootFieldNo) {
		this.rootFieldNo = rootFieldNo;
	}

	public int getFieldNo(){
		return fieldNo;
	}
	
	public void setFieldNo(int fieldNo){
		this.fieldNo = fieldNo;
	}

	private static final long serialVersionUID = 7999030904303168169L;
	
	private ISO8583TraceInfo traceInfo;

	public ISO8583TraceInfo getTraceInfo() {
		return traceInfo;
	}

	public void setTraceInfo(ISO8583TraceInfo traceInfo) {
		this.traceInfo = traceInfo;
	}

}
