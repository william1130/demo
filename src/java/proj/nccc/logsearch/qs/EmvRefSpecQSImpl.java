package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.EmvRefSpec;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.EmvRefSpecArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

public class EmvRefSpecQSImpl extends AbstractJdbcPersistableQueryService implements EmvRefSpecQS {
	
	public EmvRefSpecQSImpl() {
	}

	public String getServiceName() {
		return "EmvRefSpec Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public EmvRefSpec queryByPrimaryKey(String specID) throws QueryServiceException {
		List params = new LinkedList();
		params.add(specID);
		StringBuffer cmd = new StringBuffer(
				"SELECT * FROM EMV_REF_SPEC  WHERE SPEC_ID = ? ");

		return (EmvRefSpec) queryObject(cmd.toString(), params,
				emvRefSpecBuilder);
	}

	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		return queryByPrimaryKey((String) id);
	}

	public List queryByIds(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	public List queryByPrimaryKeys(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	public List queryByExample(ProjPersistableArg example)
			throws QueryServiceException {
		List params = new LinkedList();
		EmvRefSpecArg obj = (EmvRefSpecArg) example;
		StringBuffer criteria = new StringBuffer();
		if (obj.getSpecID() != null) {
			criteria.append("SPEC_ID=? AND ");
			params.add(obj.getSpecID());
		} else {
			criteria.append("SPEC_ID != 'TASK_TOKEN' AND ");
		}
		
		if (ValidateUtil.isNotBlank(obj.getSpecName())) {
			criteria.append("SPEC_NAME=? AND ");
			params.add(obj.getSpecName());
		}

		if (ValidateUtil.isNotBlank(obj.getStatus())) {
			criteria.append("STATUS=? AND ");
			params.add(obj.getStatus());
		}		

		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_REF_SPEC ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(
					criteria.substring(0, criteria.length() - 4));
		}

		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, emvRefSpecBuilder,
					obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, emvRefSpecBuilder);
	}

	public List queryAll() throws QueryServiceException {
		List params = new LinkedList();
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_REF_SPEC where SPEC_ID != 'TASK_TOKEN'");

		return queryObjectList(cmd.toString(), params, emvRefSpecBuilder);

	}	
	
}
