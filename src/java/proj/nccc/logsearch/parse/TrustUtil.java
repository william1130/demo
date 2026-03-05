package proj.nccc.logsearch.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hp.nccc.iso8583.common.CommonFunction;

import proj.nccc.logsearch.vo.TrustInfoValue;

/**
 * Parse Trust Data
 */
public class TrustUtil {

	public List<TrustInfoValue> parse(String header, String data) {

		Map<String, String> mappingTable = new HashMap<>();
		// transmitData 業務定義進行交換的資料
		mappingTable.put("transmitData.transmitMode", "傳輸模式");
		// beneficiaryData 受益人資料
		mappingTable.put("transmitData.beneficiaryData.beneficiaryId", "受益人ID");
		mappingTable.put("transmitData.beneficiaryData.patientId", "病患ID/入住者ID");
		mappingTable.put("transmitData.beneficiaryData.qrcode", "信託傳遞QR Code資訊");
		mappingTable.put("transmitData.beneficiaryData.authenticateCode", "驗證碼");
		mappingTable.put("bankId", "信託機構代碼");
		mappingTable.put("acquirerTransactionTime", "收單機構送出交易日期時間");
		mappingTable.put("acquirerTransactionNumber", "收單機構交易序號");
		mappingTable.put("acquirerId", "收單機構代碼");
		mappingTable.put("merchantCategoryCode", "商業行業類別碼");
		mappingTable.put("writeOffNumber", "銷帳編號");
		mappingTable.put("amount", "交易金額");
		mappingTable.put("modeId", "模式識別碼");
		mappingTable.put("businessIdNumber", "統一編號");
		mappingTable.put("platformTransactionNumber", "交換平台交易序號");
		mappingTable.put("platformReceiveTime", "交換平台接收請求訊息日期時間");
		mappingTable.put("platformTransmitTime", "交換平台送出請求訊息日期時間");
		mappingTable.put("transactionResultCode", "交易結果代碼");
		mappingTable.put("originalPlatformTransactionNumber", "原始交換平台交易序號(取消、沖銷交易使用)");
		mappingTable.put("transactionReversalMessage", "沖銷交易的說明資訊");

		List<TrustInfoValue> resultList = new ArrayList<TrustInfoValue>();
		String json = CommonFunction.hexToUtf8String(data);
		JSONObject obj = new JSONObject(json);

		flattenJson(obj, "", mappingTable, resultList);

		return resultList;
	}

	private static void flattenJson(Object obj, String prefix, Map<String, String> mappingTable,
			List<TrustInfoValue> list) {
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			for (String key : jsonObject.keySet()) {
				String newPrefix = prefix.isEmpty() ? key : prefix + "." + key;
				flattenJson(jsonObject.get(key), newPrefix, mappingTable, list);
			}
		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (int i = 0; i < jsonArray.length(); i++) {
				String newPrefix = prefix + "[" + i + "]";
				flattenJson(jsonArray.get(i), newPrefix, mappingTable, list);
			}
		} else {
			// desc 先查 mappingTable，沒有就用原始 key
			String desc = mappingTable.getOrDefault(prefix, prefix);
			list.add(new TrustInfoValue(desc, obj));
		}
	}
}
