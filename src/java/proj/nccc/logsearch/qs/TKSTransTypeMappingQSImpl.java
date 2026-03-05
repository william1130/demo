package proj.nccc.logsearch.qs;

import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

public class TKSTransTypeMappingQSImpl extends ProjNameMapQS {

	
	public Map<String, String> queryTKSNameMap() throws QueryServiceException {
		
		String cmd = new StringBuffer()
			.append("select MTI, PROC_CODE, ")
			.append("	DISPLAY_NAME ")
			.append("from atslog_trans_type_mapping ")
			.append("where HOST_ACCORD in ('IP','IC','EZ','HC') ")
			.toString();
		@SuppressWarnings("unchecked")
		List<ValueObject> list = this.queryObjectList(cmd,
				new Builder2("MTI", "PROC_CODE", "DISPLAY_NAME"));
		return toMap(list);
	}
	
	
	
	
}
