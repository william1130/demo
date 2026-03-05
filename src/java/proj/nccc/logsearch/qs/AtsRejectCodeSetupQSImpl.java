package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.edstw.service.QueryServiceException;

public class AtsRejectCodeSetupQSImpl extends ProjNameMapQS {
	
	/**
	 * @return
	 * @throws QueryServiceException
	 */
	public Map<String, String> queryNameMap() throws QueryServiceException {
		
		String cmd = new StringBuffer()
			.append("select reject_code, ")
			.append("	reject_code_desc ")
			.append("from ats_reject_code_setup ")
			.toString();
		
		@SuppressWarnings("unchecked")
		List<ValueObject> list = this.queryObjectList(cmd,
				new Builder("reject_code", "reject_code_desc"));
		ValueObject vo = new ValueObject();
		vo.value = "身分證字號錯誤";
		vo.label = "ID";
		list.add(vo);
		return toMap(list);
	}
	
	
	public String queryNameString(String approveCode) throws QueryServiceException {
		List params = new LinkedList();
		params.add(approveCode);
		
		StringBuffer cmd = new StringBuffer("SELECT reject_code_desc FROM ats_reject_code_setup where reject_code = ?");
		
		String approveCodeName = this.queryString(cmd.toString(), params, "reject_code_desc");
		
		return approveCodeName;
	
	}

}

