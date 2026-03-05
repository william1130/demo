package proj.nccc.logsearch.qs;

import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

public class NssmbankQSImpl extends ProjNameMapQS {

	/**
	 * 機構
	 * @return
	 */
	public Map<String, String> queryNameMap() throws QueryServiceException {		
		String cmd = new StringBuffer()
			.append("select nssmbank_bank_id, ")
			.append("	nssmbank_abbr_name ")
			.append("from nssmbank ")
			.toString();
		@SuppressWarnings("unchecked")
		List<ValueObject> list = this.queryObjectList(cmd,
				new Builder("nssmbank_bank_id", "nssmbank_abbr_name"));
		return toMap(list);
	}

}
