package proj.nccc.logsearch;

/**
 * 交易編號還原
 * @author Red Lee
 * @version 1.0
 */
public class TransNoUtil {

	/*
	 * 第一位數為鍵值二第3碼 8 + 2
	 * 第二位數為鍵值一第5碼 0 + 4
	 * 第三位數為鍵值二第7碼 8 + 6
	 * 第四位數為鍵值一第3碼 0 + 2
	 * 第五位數為鍵值二第1碼 8 + 0
	 * 第六位數為鍵值一第6碼 0 + 5
	 * 第七位數為鍵值二第2碼 8 + 1
	 * 第八位數為鍵值一第2碼 0 + 1
	 * 第九位數為鍵值二第5碼 8 + 4
	 * 第十位數為鍵值一第7碼 0 + 6
	 * 第十一位數為鍵值二第8碼 8 + 7
	 * 第十二位數為鍵值一第4碼 0 + 3
	 * 第十三位數為鍵值二第4碼 8 + 3
	 * 第十四位數為鍵值一第8碼 0 + 7
	 * 第十五位數為鍵值二第6碼 8 + 5
	 * 第十六位數為鍵值一第1碼 0 + 0
	 * 第十七位數為鍵值二第3碼 8 + 2
	 * 第十八位數為鍵值一第5碼 0 + 4
	 * 第十九位數為鍵值二第7碼 8 + 6
	 */
	private static final int[] CHECK_INDEX = new int[] {
		10, 4, 14, 2, 8, 5, 9, 1, 12, 6, 15, 3, 11, 7, 13, 0, 10, 4, 14
	};
	
	/**
	 * @param cardNo
	 * @param termId
	 * @return 還原失敗 return null
	 */
	public static String toTransNo(String cardNo, String termId) {
		
		if (!checkIsNumber(cardNo) || !checkIsNumber(termId))
			return null;
		
		int[] cardNums = fromString(cardNo);
		int cardLen = cardNums.length;
		
		if (cardLen != 11 && cardLen != 15 &&
				cardLen != 16 && cardLen != 19) {
			return null;
		}
		
		int[] checkNum4 = fromString(termId);
		
		if (checkNum4.length < 4)
			return null;
		
		int[] checkNum16 = createCheckNums16(checkNum4);
		
		int[] transNums = new int[cardLen + 4];
		
		for (int i = 0; i < cardLen; i++) {
			
			int val = cardNums[i];
			int add = checkNum16[CHECK_INDEX[i]];
			
			if ((i % 2) != 0)
				add *= -1;
			
			val += add;
			
			if (val >= 10)
				val -= 10;
			if (val < 0)
				val += 10;
			
			int transIndex = (i < 2) ? i :
				(i < 4) ? i + 1 :
				(i < 6) ? i + 2 :
				(i < 8) ? i + 3 : i + 4;
			
			transNums[transIndex] = val;
		}
		
		/*
		 * 交易編號：以上計算的結果再將原端末機代號的後4碼值放入第3、5、7、9 byte中，
		 * 故交易編號值將比卡號長度多4碼；還原時，則將交易編號的第3、6、9、12 byte值截取
		 * 出來當端末機代號後4碼來做還原的計算，扣除該4 bytes取得卡號還原值
		 */
		transNums[2] = checkNum16[4];
		transNums[5] = checkNum16[5];
		transNums[8] = checkNum16[6];
		transNums[11] = checkNum16[7];
		
		return toString(transNums);
	}
	
	/**
	 * @param transNo
	 * @return 還原失敗 return null
	 */
	public static String fromTransNo(String transNo) {
		
		if (!checkIsNumber(transNo))
			return null;
		
		int[] transNums = fromString(transNo);
		int transLen = transNums.length;
		
		if (transLen != 15 && transLen != 19 &&
				transLen != 20 && transLen != 23) {
			return null;
		}
		
		int[] checkNum4 = new int[4];
		checkNum4[0] = transNums[2];
		checkNum4[1] = transNums[5];
		checkNum4[2] = transNums[8];
		checkNum4[3] = transNums[11];
		
		int[] checkNum16 = createCheckNums16(checkNum4);
		int[] cardNums = new int[transLen - 4];
		
		for (int i = 0; i < cardNums.length; i++) {

			int transIndex = (i < 2) ? i :
				(i < 4) ? i + 1 :
				(i < 6) ? i + 2 :
				(i < 8) ? i + 3 : i + 4;
			
			int val = transNums[transIndex];
			int add = checkNum16[CHECK_INDEX[i]];
			
			if ((i % 2) == 0)
				add *= -1;
			
			val += add;
			
			if (val >= 10)
				val -= 10;
			if (val < 0)
				val += 10;
			
			cardNums[i] = val;
		}
		
		return toString(cardNums);
	}
	
	
	/**
	 * @param src
	 * @return
	 */
	private static int[] fromString(String src) {
		
		char[] chars = src.toCharArray();
		int[] result = new int[chars.length];
		
		for (int i = 0; i < result.length; i++) {
			
			int val = (int) chars[i];
			result[i] = val - '0';
		}
		
		return result;
	}
	
	/**
	 * @param src
	 * @return
	 */
	private static String toString(int[] src) {
		
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < src.length; i++) {
			
			char c = (char) (src[i] + '0');
			sb.append(c);
		}

		return sb.toString();
	}
	
	/**
	 * @param src
	 * @return
	 */
	private static boolean checkIsNumber(String src) {
		
		if (src == null)
			return false;
		
		return src.matches("^[0-9]*$");
	}
	
	/**
	 * 鍵值一：用10減端末機代號後4碼，連同原端末機代號後4碼，得到鍵值一，舉例
	 * 原端末機代號為13994017
	 * 鍵值一: 60934017
	 * 鍵值二: 用鍵值一每個數值加上本身的數值後取個位數 208	68024
	 * @param checkNum4
	 * @return
	 */
	private static int[] createCheckNums16(int[] checkNum4) {
		
		int beginIndex = checkNum4.length - 4;
		int[] checkNum16 = new int[16];
		
		for (int i = 0; i < 4; i++) {
			
			int val = checkNum4[beginIndex + i];
			int chk1 = val == 0 ? 0 : 10 - val;
			int chk2 = val;
			int chk3 = (chk1 * 2) % 10;
			int chk4 = (chk2 * 2) % 10;
			
			checkNum16[i + 0] = chk1;
			checkNum16[i + 4] = chk2;
			checkNum16[i + 8] = chk3;
			checkNum16[i + 12] = chk4;
		}
	
		return checkNum16;
	}
}
