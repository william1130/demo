package proj.nccc.logsearch.parse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.edstw.process.ProcessException;
import com.hp.nccc.iso8583.common.CommonFunction;

import proj.nccc.logsearch.vo.UnyInfoValue;

/**
 * Parse UNY Data 用
 */
public class UnyUtil {

	private static int[] toUnySize = { 2, 19, 20 };
	private static String[] toUnyDesc = { "交易類型", "BAR CODE", "ATS交易碼" };

	private static int[] fromUnySize = { 2, 19, 10, 19, 4, 3, 60, 20 };
	private static String[] fromUnyDesc = { "交易代碼", "BAR CODE", "綁卡特店代號", "卡號", "卡片到期日 YYMM", "回應碼 (左靠右補空白)", "回應訊息",
			"ATS交易碼" };

	private static int[] toUnyNotifySize = { 2, 10, 8, 19, 4, 1, 12, 8, 8, 6, 3, 60, 2, 1, 8, 8, 6, 1, 8, 8, 8, 19,	20 };
	private static String[] toUnyNotifyDesc = { "交易代碼", "商店代號", "端末機代號", "交易卡號（部份遮掩）", "卡片到期日YYMM", "交易類別", "交易金額",
			"交易日期", "交易時間", "授權碼", "授權回應碼", "授權回應訊息", "分期期數", "分期手續費計價方式", "首期金額", "每期金額", "分期手續費", "紅利折抵方式", "紅利折抵點數",
			"紅利餘額", "持卡人自付金額", "BAR CODE", "ATS交易序號" };

	public List<UnyInfoValue> parse(String header, String data) {

		String dataList = "";
		int[] colSize = (int[]) null;
		String[] colDesc = (String[]) null;

		try {
			dataList = new String(CommonFunction.hexDecode(data), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ProcessException(e);
		}

		if (header.indexOf("TO UNY NOTIFY") != -1) {
			colSize = toUnyNotifySize;
			colDesc = toUnyNotifyDesc;
		} else if (header.indexOf("TO UNY") != -1) {
			colSize = toUnySize;
			colDesc = toUnyDesc;
		} else if (header.indexOf("FROM UNY") != -1) {
			colSize = fromUnySize;
			colDesc = fromUnyDesc;
		}

		List<UnyInfoValue> resultList = new ArrayList<UnyInfoValue>();
		int cursor = 0;
		for (int j = 0; j < colSize.length; j++) {
			String value = dataList.substring(cursor, colSize[j] + cursor).trim();
			
			if (j == 3) {
				value = ISOUtil.getCardNumMask(value);
			}

			resultList.add(new UnyInfoValue(colDesc[j], value));
			cursor += colSize[j];
		}
		
		return resultList;

	}
}
