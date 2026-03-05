/**
 * 
 */
package com.hp.nccc.iso8583.exception;


/**
 * @author hsiehes
 *
 */
public class HeaderParseException extends ISO8583ParseException {
	
	public final static int FiledLength = -2;
	public final static int FiledTPDU = -3;
	public final static int FiledMTI = -4;
	public final static int FiledBITMAP = -5;

	/**
	 * 
	 */
	private static final long serialVersionUID = 9170111039011266477L;

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HeaderParseException(String arg0, int arg1, int Field ) {
		super(arg0, arg1);
		super.setFieldNo(Field);
	}

}
