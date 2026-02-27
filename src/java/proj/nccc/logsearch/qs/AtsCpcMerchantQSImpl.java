package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;

public class AtsCpcMerchantQSImpl extends ProjNameMapQS {
		
	public String queryNcccMidMap(String merchantId) throws QueryServiceException {
		List params = new LinkedList();
		params.add(merchantId);
		
		StringBuffer cmd = new StringBuffer("SELECT distinct NCCC_MID FROM ATS_CPC_MERCHANT where  CTCB_MID = ?");
		
		String ncccMid = this.queryString(cmd.toString(), params, "NCCC_MID");
		
		return ncccMid;
	
	}
	
	public String queryNcccTidMap(String terminalId) throws QueryServiceException {
		List params = new LinkedList();
		params.add(terminalId);
		
		StringBuffer cmd = new StringBuffer("SELECT distinct NCCC_TID FROM ATS_CPC_MERCHANT where CTCB_TID = ?");
		
		String ncccMid = this.queryString(cmd.toString(), params, "NCCC_TID");
		
		return ncccMid;
	
	}

}
