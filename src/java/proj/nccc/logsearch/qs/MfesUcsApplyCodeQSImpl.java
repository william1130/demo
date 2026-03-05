package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.CodeValueVO;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;

public class MfesUcsApplyCodeQSImpl extends AbstractJdbcPersistableQueryService implements MfesUcsApplyCodeQS {
	
	public MfesUcsApplyCodeQSImpl() {
	}

	public String getServiceName() {
		return "MfesUcsApplyCodeQS Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
	}

	@Override
	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		throw new NotImplementedException();
	}

	@Override
	public List queryByIds(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	@Override
	public List queryByExample(ProjPersistableArg example) throws QueryServiceException {
		throw new NotImplementedException();
	}

	@Override
	public List queryAll() throws QueryServiceException {
		throw new NotImplementedException();
	
	}
	
	public List<CodeValueVO> getApplyCode(String merchantNo) throws QueryServiceException {
		List params = new LinkedList();
		params.add(merchantNo);
		
		StringBuffer cmd = new StringBuffer("SELECT A.MCHT_NO, A.APPLY_CODE "
				+ " FROM EMS_MFES A, EMS_MFES_PARAM B "
				+ " WHERE A.APPLY_CODE = B.APPLY_CODE "
				+ " AND B.MFES_AUTO_DOWNLOAD_FLAG = 'Y' " 
				+ " AND B.CLOUD_FES = 'Y' " 
				+ " AND A.FES_TYPE=  'Y' "  
				+ " AND A.MCHT_NO = ? "
				+ " AND A.MCHT_TYPE IN ('11','12','13') " 
				+ " AND A.START_DATE <= TO_CHAR(SYSDATE,'yyyymmdd') "
				+ " AND (A.END_DATE IS NULL OR A.END_DATE >= TO_CHAR(SYSDATE,'yyyymmdd'))");
		return queryObjectList(cmd.toString(), params, mfesApplyCodeBuilder);
	}
	
	public List<CodeValueVO> getMerchId(String applyCode) throws QueryServiceException {
		List params = new LinkedList();
		params.add(applyCode);
		
		StringBuffer cmd = new StringBuffer("SELECT A.MCHT_NO, A.APPLY_CODE "
				+ " FROM EMS_MFES A, EMS_MFES_PARAM B "
				+ " WHERE A.APPLY_CODE = B.APPLY_CODE "
				+ " AND B.MFES_AUTO_DOWNLOAD_FLAG = 'Y' " 
				+ " AND B.CLOUD_FES = 'Y' " 
				+ " AND A.FES_TYPE=  'Y' "  
				+ " AND A.APPLY_CODE = ? "
				+ " AND A.MCHT_TYPE IN ('11','12','13') " 
				+ " AND A.START_DATE <= TO_CHAR(SYSDATE,'yyyymmdd') "
				+ " AND (A.END_DATE IS NULL OR A.END_DATE >= TO_CHAR(SYSDATE,'yyyymmdd')) order by a.MCHT_NO");
		return queryObjectList(cmd.toString(), params, mfesApplyCodeBuilder);
	}

	
}
