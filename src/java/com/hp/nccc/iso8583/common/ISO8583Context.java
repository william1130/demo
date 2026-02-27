/**
 * 
 */
package com.hp.nccc.iso8583.common;

import com.hp.nccc.iso8583.exception.HeaderParseException;

/**
 * @author hsiehes
 * 
 */
public class ISO8583Context {
	public static int ISO8583FORMAT_LENGTH_LENGTH = 2;

	public enum MTIClass {
		Authorization_Messages, 
		Financial_Transaction, 
		Batch_Update_Messages, 
		Reversal, 
		Reconciliation_Control_Messages, 
		Network_Management, 
		B24_Standin_Messages,
		ESC_Upload_Message, 
		FE_Authorization_Messages,
		FE_Financial_Transaction, 
		FE_Batch_Update_Messages, 
		FE_Reversal, 
		FE_Reconciliation_Control_Messages, 
		FE_Network_Management, 
		FE_ESC_Upload_Message, ;

		public static MTIClass valueOf(byte value) throws HeaderParseException {
			if (value == (byte) 0x01) {
				return Authorization_Messages;
			} else if (value == (byte) 0x02) {
				return Financial_Transaction;
			} else if (value == (byte) 0x03) {
				return Batch_Update_Messages;
			} else if (value == (byte) 0x04) {
				return Reversal;
			} else if (value == (byte) 0x05) {
				return Reconciliation_Control_Messages;
			} else if (value == (byte) 0x07) {
				return ESC_Upload_Message;
			} else if (value == (byte) 0x08) {
				return Network_Management;
			} else if (value == (byte) 0x09) {
				return B24_Standin_Messages;
			} else if (value == (byte) 0x91) {
				return FE_Authorization_Messages;
			} else if (value == (byte) 0x92) {
				return FE_Financial_Transaction;
			} else if (value == (byte) 0x93) {
				return FE_Batch_Update_Messages;
			} else if (value == (byte) 0x94) {
				return FE_Reversal;
			} else if (value == (byte) 0x95) {
				return FE_Reconciliation_Control_Messages;
			} else if (value == (byte) 0x98) {
				return FE_Network_Management;
			} else if (value == (byte) 0x97) {
				return FE_ESC_Upload_Message;
			}
			throw new HeaderParseException("cant parse MTI class", 0, HeaderParseException.FiledMTI);
		}

		public static byte toByte(MTIClass c) {
			switch (c) {
			case Authorization_Messages:
				return (byte) 0x01;
			case Financial_Transaction:
				return (byte) 0x02;
			case Batch_Update_Messages:
				return (byte) 0x03;
			case Network_Management:
				return (byte) 0x08;
			case Reversal:
				return (byte) 0x04;
			case Reconciliation_Control_Messages:
				return (byte) 0x05;
			case ESC_Upload_Message:
				return (byte) 0x07;
			case B24_Standin_Messages:
				return (byte) 0x09;
			case FE_Authorization_Messages:
				return (byte) 0x91;
			case FE_Financial_Transaction:
				return (byte) 0x92;
			case FE_Batch_Update_Messages:
				return (byte) 0x93;
			case FE_Reversal:
				return (byte) 0x94;
			case FE_Reconciliation_Control_Messages:
				return (byte) 0x95;
			case FE_ESC_Upload_Message:
				return (byte) 0x97;
			case FE_Network_Management:
				return (byte) 0x98;
			default:
				return (byte) 0x00;
			}
		}

		public String toString() {
			switch (this) {
			case Financial_Transaction:
				return "02";
			case Network_Management:
				return "08";
			case Reversal:
				return "04";
			case Authorization_Messages:
				return "01";
			case Batch_Update_Messages:
				return "03";
			case Reconciliation_Control_Messages:
				return "05";
			case ESC_Upload_Message:
				return "07";
			case B24_Standin_Messages:
				return "09";
			case FE_Financial_Transaction:
				return "92";
			case FE_Network_Management:
				return "98";
			case FE_Reversal:
				return "94";
			case FE_Authorization_Messages:
				return "91";
			case FE_Batch_Update_Messages:
				return "93";
			case FE_Reconciliation_Control_Messages:
				return "95";
			case FE_ESC_Upload_Message:
				return "97";
			default:
				return "00";

			}
		}
	}

	public enum MTIMode {
		Issuer_Reconciliation_Request, Issuer_Reconciliation_Request_Response, Issuer_Reconciliation_Advice, Issuer_Reconciliation_Advice_Repeat, Issuer_Reconciliation_Advice_Response, Interactive, Interactive_Response, Non_Interactive_Advice, Advice_Repeat, Non_Interactive_Advice_Response, DCC_Parameter_Update_Request, DCC_Parameter_Update_Response;

		public static MTIMode valueOf(byte value) throws HeaderParseException {
			if (value == (byte) 0x00) {
				return Interactive;
			} else if (value == (byte) 0x10) {
				return Interactive_Response;
			} else if (value == (byte) 0x20) {
				return Non_Interactive_Advice;
			} else if (value == (byte) 0x30) {
				return Non_Interactive_Advice_Response;
			} else if (value == (byte) 0x21) {
				return Advice_Repeat;
			} else if (value == (byte) 0x02) {
				return Issuer_Reconciliation_Request;
			} else if (value == (byte) 0x12) {
				return Issuer_Reconciliation_Request_Response;
			} else if (value == (byte) 0x22) {
				return Issuer_Reconciliation_Advice;
			} else if (value == (byte) 0x23) {
				return Issuer_Reconciliation_Advice_Repeat;
			} else if (value == (byte) 0x32) {
				return Issuer_Reconciliation_Advice_Response;
			} else if (value == (byte) 0x40) {
				return DCC_Parameter_Update_Request;
			} else if (value == (byte) 0x50) {
				return DCC_Parameter_Update_Response;
			} 
			throw new HeaderParseException("cant parse MTI mode", 0, HeaderParseException.FiledMTI);
		}

		public static byte toByte(MTIMode m) {
			switch (m) {
			case Interactive:
				return (byte) 0x00;
			case Interactive_Response:
				return (byte) 0x10;
			case Non_Interactive_Advice:
				return (byte) 0x20;
			case Non_Interactive_Advice_Response:
				return (byte) 0x30;
			case Advice_Repeat:
				return (byte) 0x21;
			case Issuer_Reconciliation_Request:
				return (byte) 0x02;
			case Issuer_Reconciliation_Request_Response:
				return (byte) 0x12;
			case Issuer_Reconciliation_Advice:
				return (byte) 0x22;
			case Issuer_Reconciliation_Advice_Repeat:
				return (byte) 0x23;
			case Issuer_Reconciliation_Advice_Response:
				return (byte) 0x32;
			case DCC_Parameter_Update_Request:
				return (byte) 0x40;
			case DCC_Parameter_Update_Response:
				return (byte) 0x50;
			default:
				return (byte) 0x00;
			}
		}

		public String toString() {
			switch (this) {
			case Interactive:
				return "00";
			case Interactive_Response:
				return "10";
			case Non_Interactive_Advice:
				return "20";
			case Non_Interactive_Advice_Response:
				return "30";
			case Advice_Repeat:
				return "21";
			case Issuer_Reconciliation_Request:
				return "02";
			case Issuer_Reconciliation_Request_Response:
				return "12";
			case Issuer_Reconciliation_Advice:
				return "22";
			case Issuer_Reconciliation_Advice_Repeat:
				return "23";
			case Issuer_Reconciliation_Advice_Response:
				return "32";
			case DCC_Parameter_Update_Request:
				return "40";
			case DCC_Parameter_Update_Response:
				return "50";
			default:
				return "00";

			}
		}

	}

	public enum MTIModeSimple {
		Interactive, Interactive_Response, Non_Interactive_Advice, Non_Interactive_Advice_Response;

		public static MTIModeSimple valueOf(byte value) throws HeaderParseException {
			if (value == (byte) 0x00) {
				return Interactive;
			} else if (value == (byte) 0x10) {
				return Interactive_Response;
			} else if (value == (byte) 0x20) {
				return Non_Interactive_Advice;
			} else if (value == (byte) 0x30) {
				return Non_Interactive_Advice_Response;
			} else if (value == (byte) 0x21) {
				return Non_Interactive_Advice;
			} else if (value == (byte) 0x02) {
				return Interactive;
			} else if (value == (byte) 0x12) {
				return Interactive_Response;
			} else if (value == (byte) 0x22) {
				return Non_Interactive_Advice;
			} else if (value == (byte) 0x23) {
				return Non_Interactive_Advice;
			} else if (value == (byte) 0x32) {
				return Non_Interactive_Advice_Response;
			}
			throw new HeaderParseException("cant parse MTI mode", 0, HeaderParseException.FiledMTI);
		}

		public static byte toByte(MTIModeSimple m) {
			switch (m) {
			case Interactive:
				return (byte) 0x00;
			case Interactive_Response:
				return (byte) 0x10;
			case Non_Interactive_Advice:
				return (byte) 0x20;
			case Non_Interactive_Advice_Response:
				return (byte) 0x30;
			default:
				return (byte) 0x00;
			}
		}

		public String toString() {
			switch (this) {
			case Interactive:
				return "00";
			case Interactive_Response:
				return "10";
			case Non_Interactive_Advice:
				return "20";
			case Non_Interactive_Advice_Response:
				return "30";
			default:
				return "00";

			}
		}

		public static MTIModeSimple convertMTIModeSimple(MTIMode mtiMode) {
			switch (mtiMode) {
			case Interactive:
			case Issuer_Reconciliation_Request:
				return Interactive;
			case Interactive_Response:
			case Issuer_Reconciliation_Request_Response:
				return Interactive_Response;
			case Non_Interactive_Advice:
			case Advice_Repeat:
			case Issuer_Reconciliation_Advice:
			case Issuer_Reconciliation_Advice_Repeat:
				return Non_Interactive_Advice;
			case Non_Interactive_Advice_Response:
			case Issuer_Reconciliation_Advice_Response:
				return Non_Interactive_Advice_Response;
			default:
				return null;
			}
		}

	}
}
