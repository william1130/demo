package proj.nccc.logsearch.qs;

import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

@SuppressWarnings("unchecked")
public class LDSSTransTypeMappingQSImpl extends ProjNameMapQS {

	public Map<String, String> queryTransNameMap(String mti, String hostAccord) throws QueryServiceException {

		StringBuffer cmd = new StringBuffer().append("select DISTINCT HOST_ACCORD, MTI, PROC_CODE, ")
				.append("	DISPLAY_NAME ").append("from ldss_trans_type_mapping where 1=1 ");
		if (!hostAccord.toLowerCase().equals("all")) {
			cmd.append(String.format("AND HOST_ACCORD = '%s' ", hostAccord));
			if (mti.length() != 0) {
				cmd.append(String.format("AND MTI = '%s' ", mti));
			}
		} else {
			if (mti.length() != 0) {
				cmd.append(String.format("AND MTI = '%s' ", mti));
			}
		}
		cmd.append("ORDER BY HOST_ACCORD, MTI, PROC_CODE");
		String cmdString = cmd.toString();

		List<ValueObject> list = this.queryObjectList(cmdString, new Builder3("HOST_ACCORD", "MTI", "PROC_CODE", "DISPLAY_NAME"));
		return toMap(list);
	}

}
