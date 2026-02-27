/**
 * 
 */
package com.hp.nccc.iso8583.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hsiehes
 *
 */
public enum ISOType {

	/** A fixed-length numeric value. It is zero-filled to the left. */
	NUMERIC(true, 0),
	/** A fixed-length alphanumeric value. It is filled with spaces to the right. */
	ALPHA(true, 0),
	/** A variable length alphanumeric value with a 2-digit header length. */
	LLVAR(false, 0),
	/** A variable length alphanumeric value with a 3-digit header length. */
	LLLVAR(false, 0),
	/** A date in format MMddHHmmss */
	DATE10(false, 10),
	/** A date in format MMdd */
	DATE4(false, 4),
	/** A date in format yyMM */
	DATE_EXP(false, 4),
	/** Time of day in format HHmmss */
	TIME(false, 6),
	/** An amount, expressed in cents with a fixed length of 12. */
	AMOUNT(false, 12),
	/** Similar to ALPHA but holds byte arrays instead of strings. */
	BINARY(true, 0),
	/** Similar to ALPHA but holds byte arrays instead of strings. */
	CHINESE(true, 0),
	/** Similar to LLVAR but holds byte arrays instead of strings. */
	LLBIN(false, 0),
	/** Similar to LLLVAR but holds byte arrays instead of strings. */
	LLLBIN(false, 0),
	
	/** Similar to LLBIN but holds bit arrays instead of strings. */
	LLZ(false, 0),
	
	/** Sub tables used for NCCC  */
	LLLTABLES(false, 0),
	
	/** EMV Chip Data used for NCCC  */
	LLLCHIP(false, 0),
	
	/** EMV Chip Data used for NCCC  */
	POSKITZTAG(false, 0),
	
	/** If ISO8583 need calculate check sum , use it. It's type must include LLLTables*/
	CHECKSUM(true, 0),
	
	/** If LLLTables sub-table filed need calculate length*/
	LBINARY(false, 0),
	
	/** tokens used for Base24  */
	LLLTOKENS(false, 0),
	
	/** tokens used for Base24  */
	LLLLTOKENS(false, 0),
	
	/** If LLLTOKENS token filed need calculate length*/
	LVAR(true, 0),
	
	SMARTPAYCHIP(false, 0),
	
	SMARTPAYTAG(false, 0),
	
	REDEF(false, 0),
	
	LLLBITMAPVAR(false, 0),
	
	ZIP(false, 0),
	
	B24TLV(false, 0),
	
	EBCDIC(true, 0),
	
	VAR(false, 0),
	
	LLBINHEX(false, 0),
	
	LLLBINHEX(false, 0)
	;

	private boolean needsLen;
	private int length;

	ISOType(boolean flag, int l) {
		needsLen = flag;
		length = l;
	}

	/** Returns true if the type needs a specified length. */
	public boolean needsLength() {
		return needsLen;
	}

	/** Returns the length of the type if it's always fixed, or 0 if it's variable. */
	public int getLength() {
		return length;
	}

	/** Formats a Date if the receiver is DATE10, DATE4, DATE_EXP or TIME; throws an exception
	 * otherwise. */
	public String format(Date value) {
		if (this == DATE10) {
			return String.format("%Tm%<Td%<TH%<TM%<TS", value);
		} else if (this == DATE4) {
			return String.format("%Tm%<Td", value);
		} else if (this == DATE_EXP) {
			return String.format("%Ty%<Tm", value);
		} else if (this == TIME) {
			return String.format("%TH%<TM%<TS", value);
		}
		throw new IllegalArgumentException("Cannot format date as " + this);
	}

	/** Formats the string to the given length (length is only useful if type is ALPHA, NUMERIC or BINARY). */
	public String format(String value, int length) {
		if (this == ALPHA) {
	    	if (value == null) {
	    		value = "";
	    	}
	        if (value.length() > length) {
	            return value.substring(0, length);
	        } else if (value.length() == length) {
	        	return value;
	        } else {
	        	return String.format(String.format("%%-%ds", length), value);
	        }
		} else if (this == LLVAR || this == LLLVAR) {
			return value;
		} else if (this == NUMERIC) {
	        char[] c = new char[length];
	        char[] x = value.toCharArray();
	        if (x.length > length) {
	        	throw new IllegalArgumentException("Numeric value is larger than intended length: " + value + " LEN " + length);
	        }
	        int lim = c.length - x.length;
	        for (int i = 0; i < lim; i++) {
	            c[i] = '0';
	        }
	        System.arraycopy(x, 0, c, lim, x.length);
	        return new String(c);
		} else if (this == AMOUNT) {
			return ISOType.NUMERIC.format(new BigDecimal(value).movePointRight(2).longValue(), 12);
		} else if (this == BINARY || this == CHINESE) {

	    	if (value == null) {
	    		value = "";
	    	}
	        if (value.length() > length) {
	            return value.substring(0, length);
	        }
	        char[] c = new char[length];
	        int end = value.length();
	        if (value.length() % 2 == 1) {
	        	c[0] = '0';
		        System.arraycopy(value.toCharArray(), 0, c, 1, value.length());
		        end++;
	        } else {
		        System.arraycopy(value.toCharArray(), 0, c, 0, value.length());
	        }
	        for (int i = end; i < c.length; i++) {
	            c[i] = '0';
	        }
	        return new String(c);

		} else if (this == LLBIN || this == LLLBIN) {
			return value;
		} else if(this == CHECKSUM){
			if (value == null) {
	    		value = "";
	    	}
	        if (value.length() > length) {
	            return value.substring(0, length);
	        }
	        char[] c = new char[length];
	        int end = value.length();
	        if (value.length() % 2 == 1) {
	        	c[0] = '0';
		        System.arraycopy(value.toCharArray(), 0, c, 1, value.length());
		        end++;
	        } else {
		        System.arraycopy(value.toCharArray(), 0, c, 0, value.length());
	        }
	        for (int i = end; i < c.length; i++) {
	            c[i] = '0';
	        }
	        return new String(c);
		}
		throw new IllegalArgumentException("Cannot format String as " + this);
	}

	/** Formats the integer value as a NUMERIC, an AMOUNT, or a String. */
	public String format(long value, int length) {
		if (this == NUMERIC) {
			String x = String.format(String.format("%%0%dd", length), value);
	        if (x.length() > length) {
	        	throw new IllegalArgumentException("Numeric value is larger than intended length: " + value + " LEN " + length);
	        }
	        return x;
		} else if (this == ALPHA || this == LLVAR || this == LLLVAR) {
			return format(Long.toString(value), length);
		} else if (this == AMOUNT) {
			return String.format("%010d00", value);
		} else if (this == BINARY || this == LLBIN || this == LLLBIN || this == CHINESE) {
			return format(Long.toString(value), length);
		}
		throw new IllegalArgumentException("Cannot format number as " + this);
	}

	/** Formats the BigDecimal as an AMOUNT, NUMERIC, or a String. */
	public String format(BigDecimal value, int length) {
		if (this == AMOUNT) {
			return String.format("%012d", value.movePointRight(2).longValue());
		} else if (this == NUMERIC) {
			return format(value.longValue(), length);
		} else if (this == ALPHA || this == LLVAR || this == LLLVAR) {
			return format(value.toString(), length);
		} else if (this == BINARY || this == LLBIN || this == LLLBIN || this == CHINESE) {
			return format(value.toString(), length);
		}
		throw new IllegalArgumentException("Cannot format BigDecimal as " + this);
	}

	public <T> ISOValue<T> value(T val, int len, String desc) {
		return new ISOValue<T>(this, val, len, desc);
	}

	public <T> ISOValue<T> value(T val, String desc) {
		return new ISOValue<T>(this, val, desc);
	}

	public <T> ISOValue<T> call(T val, int len, String desc) {
		return new ISOValue<T>(this, val, len, desc);
	}

	public <T> ISOValue<T> call(T val, String desc) {
		return new ISOValue<T>(this, val, desc);
	}

	public <T> ISOValue<T> apply(T val, int len, String desc) {
		return new ISOValue<T>(this, val, len, desc);
	}
	public <T> ISOValue<T> apply(T val, String desc) {
		return new ISOValue<T>(this, val, desc);
	}

}
