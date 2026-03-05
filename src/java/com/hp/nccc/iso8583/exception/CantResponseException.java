/**
 * 
 */
package com.hp.nccc.iso8583.exception;

/**
 * @author hsiehes
 *
 */
public class CantResponseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7128181297671818747L;

	/**
	 * @param arg0
	 */
	public CantResponseException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public CantResponseException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CantResponseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
