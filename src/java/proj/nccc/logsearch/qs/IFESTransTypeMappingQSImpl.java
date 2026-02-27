package proj.nccc.logsearch.qs;

import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

@SuppressWarnings("unchecked")
public class IFESTransTypeMappingQSImpl extends ProjNameMapQS {

	public Map<String, String> queryTransNameMap(String hostAccord) throws QueryServiceException {

		StringBuffer cmd = new StringBuffer().append("SELECT DISTINCT HOST_ACCORD, MTI, PROC_CODE, DISPLAY_NAME ")
				.append("FROM IFES_TRANS_TYPE_MAPPING WHERE 1=1 ");
		if (!hostAccord.toLowerCase().equals("all")) {
			cmd.append(String.format("AND HOST_ACCORD = '%s' ", hostAccord));
		}
		cmd.append("ORDER BY HOST_ACCORD, MTI, PROC_CODE");

		List<ValueObject> list = this.queryObjectList(cmd.toString(),
				new Builder3("HOST_ACCORD", "MTI", "PROC_CODE", "DISPLAY_NAME"));
		return toMap(list);
	}
}
