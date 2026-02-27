package proj.nccc.logsearch.qs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

public class AtsResponseCodeSetupQSImpl extends ProjNameMapQS {
	
	/**
	 * @return
	 * @throws QueryServiceException
	 */
	public Map<String, String> queryNameMap() throws QueryServiceException {
		
		String cmd = new StringBuffer()
			.append("select resp_code, ")
			.append("	resp_message ")
			.append("from ats_response_code_setup ")
			.toString();
		@SuppressWarnings("unchecked")
		List<ValueObject> list = this.queryObjectList(cmd,
				new Builder("resp_code", "resp_message"));
		
		Map<String, String> map = new HashMap<String, String>();
		
		for (ValueObject obj : list) {
			String label = obj.label == null ? "" :
				obj.label.length() <= 0 ? "" :
					obj.label.substring(0, 1);
			String nameLagel = new StringBuffer()
				.append(label)
				.append("(")
				.append(obj.value)
				.append(")")
				.toString();
			map.put(obj.value, nameLagel);
		}
		
		return map;
	}
}
