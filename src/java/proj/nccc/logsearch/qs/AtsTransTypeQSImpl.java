package proj.nccc.logsearch.qs;

import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

public class AtsTransTypeQSImpl extends ProjNameMapQS {
	
	/**
	 * @return
	 * @throws QueryServiceException
	 */
	public Map<String, String> queryNameMap() throws QueryServiceException {
		
		String cmd = new StringBuffer()
			.append("select trans_type, ")
			.append("	chin_display ")
			.append("from ats_trans_type ")
			.toString();
		@SuppressWarnings("unchecked")
		List<ValueObject> list = this.queryObjectList(cmd,
				new Builder("trans_type", "chin_display"));
		return toMap(list);
	}
}
