package com.hp.nccc.iso8583.exception;

public class UnsupportMTIException extends HeaderParseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4546472996305513973L;

	public UnsupportMTIException(String arg0, int arg1) {
		super(arg0, arg1, HeaderParseException.FiledMTI);
	}

}
