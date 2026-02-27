package proj.nccc.logsearch.qs;

import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

public class SpswResponseCodeSetupQSImpl extends ProjNameMapQS {
	
	/**
	 * @return
	 * @throws QueryServiceException
	 */
	public Map<String, String> queryNameMap() throws QueryServiceException {
		
		String cmd = new StringBuffer()
			.append("select ref_code1, ")
			.append("	ref_name ")
			.append("from SPSWLOG_REF_LIST  where ref_type='RESP_CODE' ")
			.toString();
		@SuppressWarnings("unchecked")
		List<ValueObject> list = this.queryObjectList(cmd,
				new Builder("ref_code1", "ref_name"));
		
		return toMap(list);
	}
}
