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

public class MfesUcsBatchHeaderQSImpl extends AbstractJdbcPersistableQueryService implements MfesUcsBatchHeaderQS {
	
	public MfesUcsBatchHeaderQSImpl() {
	}

	public String getServiceName() {
		return "MfesUcsBatchHeaderQS Query Service";
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
	
	@Override
	public List<CodeValueVO> getUncloseTerminalId(String merchantNo) throws QueryServiceException {
		List params = new LinkedList();
		params.add(merchantNo);
		
		StringBuffer cmd = new StringBuffer("SELECT MERCHANT_ID, TERMINAL_ID "
				+ " FROM ATS_CURRENT_BATCH_HEADER "
				+ " WHERE CLOSE_DATETIME = ' ' "
				+ " AND MERCHANT_ID = ?  ORDER BY TERMINAL_ID ");
		return queryObjectList(cmd.toString(), params, mfesBatchHeaderBuilder);
	}

	
}
