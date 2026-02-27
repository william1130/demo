package proj.nccc.logsearch.qs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.edstw.service.QueryServiceException;

public class MerchantChinQSImpl extends ProjNameMapQS {
	
	public Map<String, String> queryNameMap(List<String> merchantIds) throws QueryServiceException {
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer criteria = null;
		int count = 0;

		for (String merchantId:merchantIds) {
			
			if (merchantId == null)
				continue;
			
			if (criteria == null)
				criteria = new StringBuffer("merchant_no in (?");
			else
				criteria.append(", ?");
			
			params.add(merchantId);
			count++;
			
			if (count >= 800)
				break;
		}
		
		if (criteria == null)
			return new HashMap<String, String>();
		
		criteria.append(") ");
		
		StringBuffer cmd = new StringBuffer()
			.append("select distinct merchant_no, ")
			.append("	chin_name ")
			.append("from merchant_chinese_data ")
			.append("where card_code = '02' and ")
			.append(criteria);
		
		@SuppressWarnings("unchecked")
		List<ValueObject> list = this.queryObjectList(cmd.toString(), params,
				new Builder("merchant_no", "chin_name"));
		return toMap(list);
	}
	
	
	public String queryNameString(String merchantId) throws QueryServiceException {
		List params = new LinkedList();
		params.add(merchantId);
		
		StringBuffer cmd = new StringBuffer("SELECT distinct merchant_no, chin_name FROM merchant_chinese_data where card_code = '02' and merchant_no = ?");
		
		String chin_name = this.queryString(cmd.toString(), params, "chin_name");
		
		return chin_name;
	
	}
	

	public String queryApplyCode(String merchantId) throws QueryServiceException {
		List params = new LinkedList();
		params.add(merchantId);
		
		StringBuffer cmd = new StringBuffer("SELECT apply_code FROM EMSPROD.EMS_MFES where MCHT_NO = ? ");
		
		String applyCode = this.queryString(cmd.toString(),params, "apply_code");
		
		return applyCode;
	
	}
}
