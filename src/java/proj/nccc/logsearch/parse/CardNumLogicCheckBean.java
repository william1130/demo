package proj.nccc.logsearch.parse;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * serval check data format functions
 */
public class CardNumLogicCheckBean {
	private static final Logger log = LogManager.getLogger(CardNumLogicCheckBean.class);
	
	/** integer with Master Card */
	public static final int MASTER_CARD = 1;
	/** integer with VISA Card */
	public static final int VISA_CARD = 2;
	/** integer with JCB Card */
	public static final int JCB_CARD = 3;
	/** integer with American Express Card */
	public static final int AMERICAN_EXPRESS = 4;
	/** integer with U Card */
	public static final int U_CARD = 5;
	/** integer with VMJ Card */
	public static final int THREE_CARD = 6;
	/** integer with YYYY/MM/DD data format */
	public static int YYYYsMMsDD = 0;
	/** integer with YY/MM data format */
	public static int YYsMM = 1;
	/** integer with YYYYMMDD data format */
	public static int YYYYMMDD = 2;
	/** integer with YYMMDD data format */
	public static int YYMMDD = 3;
	/** integer with YYMM data format */
	public static int YYMM = 4;

	/** Create check class */
	public CardNumLogicCheckBean() {
	}

//	public boolean checkCardtype(String cardType, String bin) {
//		boolean rtn = false;
//		if (bin != null && bin.length() >= 6) {
//			String binNum = bin.substring(0, 6);
//			if (cardType.equals("U")) {
//				if (binNum.substring(0, 4).equals("4000"))
//					return true;
//				else
//					return false;
//			}
//			if (cardType.equals("V")) {
//				if (binNum.substring(0, 1).equals("4"))
//					return true;
//				else
//					return false;
//			}
//			if (cardType.equals("M")) {
//				if (binNum.substring(0, 1).equals("5"))
//					return true;
//				else
//					return false;
//			}
//			if (cardType.equals("J")) {
//				if (binNum.substring(0, 2).equals("18") || binNum.substring(0, 2).equals("21")
//						|| binNum.substring(0, 2).equals("35"))
//					return true;
//				else
//					return false;
//			}
//			if (cardType.equals("A")) {
//				if (binNum.substring(0, 2).equals("34") || binNum.substring(0, 2).equals("35")
//						|| binNum.substring(0, 2).equals("37"))
//					return true;
//				else
//					return false;
//			}
//			return true;
//		}
//
//		return rtn;
//	}

	/** check credit card number, for type are 'M', 'V', ... */
	public boolean CreditCard(String type, String number) {
		boolean rtn = false;
		try {
			switch (type.charAt(0)) {
			case 'O':
				rtn = CreditCard(THREE_CARD, number);
				break;
			case 'U':
			case 'N':
				rtn = CreditCard(U_CARD, number);
				break;
			case 'A':
				rtn = CreditCard(AMERICAN_EXPRESS, number);
				break;
			default:
				rtn = CreditCard(0, number);
				break;
			}
		} catch (Exception e) {
			rtn = CreditCard(0, number);
		}
		return rtn;
	}

	/** check credit card number, for type are MASTER_CARD, VISA_CARD, ... */
	public boolean CreditCard(int type, String number) {
		int chk_sum;
		int sum;
		String card;
		String u_card_9 = "9000";
		String u_card_4 = "4000";

		/** 比較字串需用equals若用"=="只是比較記憶體位置 */
		try {
			card = number.trim();
			if (card.substring(0, 4).equalsIgnoreCase(u_card_9) || card.substring(0, 4).equalsIgnoreCase(u_card_4)) {
				chk_sum = Integer.parseInt(card.substring(14, 15));
				//				log.info( "chksum:" + chk_sum );
			} else
				chk_sum = Integer.parseInt(card.substring(card.length() - 1));
		} catch (Exception e) {
			chk_sum = 0;
			return false;
		}

		switch (type) {
		case THREE_CARD:
			sum = module10(card, 2);
			break;
		case U_CARD:
			sum = UCARD(card);
			break;
		case AMERICAN_EXPRESS:
			if (card.length() == 15)
				card = "0" + card;
			sum = module10(card, 2);
			break;
		default:
			sum = module10(card, 1);
			break;
		}
		return (chk_sum == sum);
	}

	/** normal credit check */
	private int module10(String number, int powerfrom) {
		int value = 0;
		int rtn = 0;
		/** multiplay */
		try {
			int adder = powerfrom;
			for (int i = 0; i < number.length() - 1; i++) {
				int v1 = Integer.parseInt(number.substring(i, i + 1)) * (2 - (adder++ % 2));
				value += v1 / 10 + v1 % 10;
			}
			rtn = (10 - (value % 10)) % 10;
		} catch (Exception e) {
			rtn = -1;
		}
		return rtn;
	}

	/** U card check function */

	private int UCARD(String number) {
		int result = 0;
		String power1 = "00007356798597";
		String power2 = "597";
		int sum = 0;
		int sumTotal = 0;

		for (int i = 0; i < 14; i++) {
			result += Integer.parseInt(number.substring(i, i + 1)) * Integer.parseInt(power1.substring(i, i + 1));
		}
		/* sum */
		sum += (result / 100) * Integer.parseInt(power2.substring(0, 1));
		sum += (result / 10) * Integer.parseInt(power2.substring(1, 2));
		sum += (result % 10) * Integer.parseInt(power2.substring(2));

		sumTotal = (10 - ((sum + 3) % 10));
		if (sumTotal == 10) {
			//			log.info( "test1:" + sumTotal );
			return 0;
		} else {
			//			log.info( "test2:" + sumTotal );
			return (sumTotal);
		}
	}

//	/** subfunction for JCB card checking */
//	private int JAL(String number) {
//		String power1 = "000037001371371";
//
//		int sum = 0;
//
//		for (int i = 0; i < power1.length(); i++) {
//			sum += Integer.parseInt(number.substring(i, i + 1)) * Integer.parseInt(power1.substring(i, i + 1));
//		}
//		return (10 - (sum % 10));
//	}

	/** check personal ID */
	public boolean personalId(String code) {
		boolean rtn = false;
		String firstCode = "ABCDEFGHJKLMNPQRSTUVXYWZIO";
		String SecondCode = firstCode.substring(0, 4); // 921212修改外國人id查核
		log.info("check.sec-code:" + SecondCode);
		int sum = 0;
		try {
			if (Character.isLetter(code.charAt(0)) && Character.isDigit(code.charAt(1))) { // local

				int check_sum = Integer.parseInt(code.substring(9, 10));
				int x = 10 + firstCode.indexOf(code.substring(0, 1));
				sum = (x - (x % 10)) / 10 + (x % 10) * 9;

				for (int i = 1; i < 9; i++) {
					sum += Integer.parseInt(code.substring(i, i + 1)) * (9 - i);
				}
				rtn = (check_sum == ((10 - (sum % 10)) % 10));
				if (code.length() > 10) {
					char lastChar = code.charAt(10);
					if (rtn) {
						if (!(Character.isDigit(lastChar) || Character.isSpaceChar(lastChar))) {
							rtn = false;
						}
					} else {
						if (lastChar == 'A') {
							rtn = true;
						}
					}
				}
			} else {
				if (Character.isLetter(code.charAt(0)) && Character.isLetter(code.charAt(1))) {
					log.info("check.test2");
					int check_sum = Integer.parseInt(code.substring(9, 10));
					int x = 10 + firstCode.indexOf(code.substring(0, 1));
					log.info("check.SEC2:" + firstCode.indexOf(code.substring(1, 2)));
					log.info("check.x:" + x);
					sum = (x - (x % 10)) / 10 + (x % 10) * 9 + firstCode.indexOf(code.substring(1, 2)) * 8;
					log.info("check.sum:" + sum);
					for (int i = 2; i < 9; i++) {
						sum += Integer.parseInt(code.substring(i, i + 1)) * (9 - i);
						log.info("check.Sum3:" + sum);
					}
					rtn = (check_sum == ((10 - (sum % 10)) % 10));
					log.info("check.check_sum:" + check_sum);
					log.info("check.sum2:" + (10 - (sum % 10)) % 10);

				}
				if (code.length() > 10) {
					char lastChar = code.charAt(10);
					if (rtn) {
						if (!(Character.isDigit(lastChar) || Character.isSpaceChar(lastChar))) {
							rtn = false;
						}
					} else {
						if (lastChar == 'A') {
							rtn = true;
						}
					}
				}
			}
		} catch (Exception e) {
			rtn = false;
		}

		return rtn;
	}

	/** check uniform number */
	public boolean uniformNo(String number) {
		String power1 = "12121241";
		boolean rtn = true;
		int sum = 0;
		/* multiplay */
		try {
			for (int i = 0; i < 8; i++) {
				int value = Integer.parseInt(number.substring(i, i + 1)) * Integer.parseInt(power1.substring(i, i + 1));
				sum += value / 10 + value % 10;
			}
			rtn = (sum % 10 == 0);
			char lastChar = number.charAt(number.length() - 1);
			if (number.length() == 9) {
				if (lastChar == 'A') {
					rtn = true;
				} else {
					if (Character.isLetter(lastChar) || lastChar == '0') {
						rtn = false;
					}
				}
			} else {
				if (!rtn && number.charAt(6) == '7') {
					rtn = ((sum - 9) % 10 == 0);
				}
			}
		} catch (Exception e) {
			rtn = false;
		}

		return rtn;
	}

	/** check string length is in range */
	public boolean length(String value, int minLength, int maxLength) {
		boolean rtn = true;

		try {
			if (value == null) {
				rtn = false;
			} else {
				if (value.trim().length() > maxLength || value.trim().length() < minLength) {
					rtn = false;
				}
			}
		} catch (Exception e) {
			log.info("check length:" + e.toString());
		}
		return rtn;
	}

	/** check data is date format(YYMM) */
	public boolean expireDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMM");
		boolean rtn = true;
		try {
			ParsePosition pos = new ParsePosition(0);
			formatter.parse(date, pos);
			int month = Integer.parseInt(date.substring(2));
			if (month > 12 || month < 1)
				rtn = false;
		} catch (Exception ex) {
			rtn = false;
		}
		return rtn;
	}

	/** check the string is chinese(true) or not(false) */
	public boolean chinese(String name) {
		boolean rtn = true;
		try {

			int len = name.length();
			if (len == 0)
				rtn = false;
			for (int i = 0; i < len; i++) {
				int type = 0;
				char ch = name.charAt(i);

				String word = ch + "";
				if (word.getBytes().length != 2) {// not chinese code
					log.info("no:" + i + "type:" + type + "value:" + ch);
					rtn = false;
					break;
				}
			}
		} catch (Exception e) {
			rtn = false;
		}

		return rtn;
	}

	/** check string is date format "YYYYMMDD" */
	public boolean date1(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		boolean rtn = true;
		try {
			ParsePosition pos = new ParsePosition(0);
			Date currentTime_2 = formatter.parse(date, pos);
			log.info("date:" + currentTime_2);

			String fmtDate = formatter.format(currentTime_2);

			if (!date.equals(fmtDate))
				rtn = false;
		} catch (Exception ex) {
			rtn = false;
		}
		return rtn;
	}

	/** check value is in range */
	public boolean valueRange(String value, int minLength, int maxLength) {
		boolean rtn = false;
		int intValue = 0;
		try {
			if (value != null && value.length() >= 0) {
				intValue = Integer.parseInt(value);

				if (intValue <= maxLength && intValue >= minLength) {
					rtn = true;
				}
			}

		} catch (Exception e) {
			log.info("check valueRange:" + e.toString());
			e.printStackTrace();
		}
		return rtn;
	}

	/** check data is before today */
	public boolean dateRange(String value, int fromDateType) {
		boolean rtn = false;
		SimpleDateFormat formatter[] = { new SimpleDateFormat("yyyy/MM/dd"), new SimpleDateFormat("yy/MM"),
				new SimpleDateFormat("yyyyMMdd"), new SimpleDateFormat("yyMMdd"), new SimpleDateFormat("yyMM") };
		ParsePosition pos = new ParsePosition(0);

		try {
			Date data1 = formatter[fromDateType].parse(value.trim(), pos);
			Date today = new Date();
			if (data1.before(today))
				rtn = true;
		} catch (Exception e) {
		}

		return rtn;
	}

	/** check string is all digits */
	public boolean isDigit(String value) {
		boolean rtn = true;
		try {
			for (int i = 0; i < value.length(); i++) {
				if (!Character.isDigit(value.charAt(i))) {
					rtn = false;
					break;
				}
			}
		} catch (Exception e) {
			rtn = false;
		}
		return rtn;
	}

	public String getCardType(String bin) {
		if (bin != null && bin.length() >= 6) {
			String binNum = bin.substring(0, 6);

			if (binNum.substring(0, 4).equals("9000") || binNum.substring(0, 4).equals("4000")){
				return "U";
			}
			else if (binNum.substring(0, 2).equals("34") || binNum.substring(0, 2).equals("35")
					|| binNum.substring(0, 2).equals("37")){
				return "A";
			}else{
				return "O";
			}
			
//			if (binNum.substring(0, 1).equals("4"))
//				return "V";
//
//			if (binNum.substring(0, 1).equals("5"))
//				return "M";
//
//			if (binNum.substring(0, 2).equals("18") || binNum.substring(0, 2).equals("21")
//					|| binNum.substring(0, 2).equals("35"))
//				return "J";
//
			
		}

		return null;
	}
	
	public boolean checkCreditCard(String cardNo){
		if (cardNo == null || cardNo.length() < 13) {
			return false;
		}
		
		
		int len = cardNo.length();
		boolean odd = true;
		int sum = 0;
		
		for (int i = len - 1; i >= 0; i--) {
			
			int ch = Integer.parseInt(cardNo.substring(i, i + 1));
			int num = odd ? ch * 1 : ch * 2;
			odd = !odd;
			
			if (num >= 10) {
				num = (num % 10) + 1;
			}
			
			sum += num;
		}
		
		if(cardNo.startsWith("4000") ||cardNo.startsWith("9000") ){
			
		}else{
		if ((sum % 10) != 0){
			return false;
		}
		}
		
		String type = getCardType(cardNo.substring(0, 6));
		return CreditCard(type, cardNo);
	}
}
