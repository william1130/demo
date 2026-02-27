/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2008/5/2, 下午 02:28:26, By 許欽程(Shiu Vincent, sz12tk)
 * 
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.beans;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.edstw.util.ValidateUtil;

/**
 *
 * @author 許欽程(Shiu Vincent, sz12tk)
 * @version $Revision$
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Util {
	private static Set nullSet = new HashSet();
	static {
		nullSet.add(null);
	}

	public static int[] createIntIndexes(String ynStr) {
		if (ValidateUtil.isBlank(ynStr))
			return new int[0];

		// 要建立之int array主要是用於畫面的checkbox, 字串為'Y'者, 才要記錄其index, 所以int陣列長度不一定會與字串長度相同,
		// 但在此為簡化起見, 字串不為'Y'者給其值為-1, 在畫面上並不會造成影響. 程式中也不會用到-1這個值.
		char[] array = ynStr.toCharArray();
		int[] indexes = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i] == 'Y')
				indexes[i] = i;
			else
				indexes[i] = -1;
		}
		return indexes;
	}

	public static String createYNString(int[] indexes, int length) {
		char[] array = new char[length];
		Arrays.fill(array, 'N');
		if (indexes != null && indexes.length > 0) {
			for (int i = 0; i < indexes.length; i++) {
				array[indexes[i]] = 'Y';
			}
		}
		return new String(array);
	}

	public static void removeNullValue(Collection c) {
		if (c != null && c.size() > 0) {
			c.removeAll(nullSet);
		}
	}

	// 驗證字符串是否是數字
	public static boolean isNumeric(String str) {

		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 將傳入之姓名保留第一個字, 後面加上'先生/小姐' 若傳入之值為null則傳回null
	 * 
	 * @param name
	 * @return
	 */
	public static String hideName(String name) {
		if (!ValidateUtil.isBlank(name)) {
			return name.substring(0, 1) + " 先生/小姐";
		}
		return name;
	}

	/**
	 * 以byte的方式取得子字串, 若傳入之字串為空(null或空字串), 則傳回原字串, 若byte長度不足startByte所指之長度,
	 * 則傳回空字串("").
	 * 
	 * @param str
	 * @param startByte
	 * @param endByte
	 * @return
	 */
	public static String substringByByte(String str, int startByte, int endByte) {
		if (StringUtils.isBlank(str))
			return str;
		byte[] b = str.getBytes();
		if (startByte > b.length)
			return "";
		if (endByte > b.length)
			endByte = b.length;
		int len = endByte - startByte;
		String s1 = new String(b, startByte, len);
		return s1;
	}

	public static Boolean checkBooleanValue(Object o) {
		Boolean rv = Boolean.FALSE;
		if (o != null)
			rv = Boolean.parseBoolean(o.toString());

		return rv;
	}

	public static Integer checkIntValue(Object o) {
		Integer rv = null;
		if (o != null)
			rv = Integer.valueOf(o.toString());

		return rv;
	}

	public static Object checkObject(Object o) {
		return o;
	}

	// M2020150_R109316_後端人工授權系統整併入ATSLOG
	// pad with " " to the right to the given length (n)
	public static String padRight(String s, int n) {
		if (s == null) {
			s = "";
		}
		n = n - (s.getBytes().length - s.length());
		return String.format("%1$-" + n + "s", s);
	}

	/**
	 * M2025074_R114117 將標準 UUID 字串編譯為 Base64 URL-safe 編碼的短 UUID 字串。
	 * 
	 * @param 標準 UUID 字串
	 * @return shortUuid 短 UUID 字串
	 */
	public static String encodeShortUuid(String uuidStr) {
		UUID uuid = UUID.fromString(uuidStr);
//		String originalUuidStr = uuid.toString();
//		System.out.println("\n原始 UUID 字串: " + originalUuidStr);
//		System.out.println("\n原始 UUID 字串長度: " + originalUuidStr.length());

		// 2. 將 UUID 轉換為 16 個位元組的二進位資料
		// UUID 的 mostSignificantBits (MSB) 和 leastSignificantBits (LSB) 組合起來就是 16 位元組
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		byte[] uuidBytes = bb.array();

		// 3. 使用 Base64 URL-safe 編碼
		// getUrlEncoder() 會處理 URL-Safe 規則 (如 - 和 _)
		// withoutPadding() 會省略結尾的 = 符號
		String shortUuid = Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes);

		return shortUuid;
	}
}

