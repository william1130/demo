package proj.nccc.logsearch.qs;

import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

public class EmvCardTypeSetupQSImpl extends ProjNameMapQS {
	
	/**
	 * @return
	 * @throws QueryServiceException
	 */

	public Map<String, String> queryNameMap() throws QueryServiceException {
		
		String cmd = new StringBuffer()
		.append("select card_type, ")
		.append("	card_type_name ")
		.append("from emv_card_type ")
		.toString();
		@SuppressWarnings("unchecked")
		List<ValueObject> list = this.queryObjectList(cmd,
				new Builder("card_type", "card_type_name"));
		return toMap(list);
	}
}

