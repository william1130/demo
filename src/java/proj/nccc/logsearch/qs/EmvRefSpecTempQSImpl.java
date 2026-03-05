package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.EmvRefSpecTemp;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.EmvRefSpecTempArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.sql.ResultSetTool;
import com.edstw.sql.builder.AbstractObjectBuilder;
import com.edstw.util.NotImplementedException;

public class EmvRefSpecTempQSImpl extends AbstractJdbcPersistableQueryService implements EmvRefSpecTempQS {
	
	public EmvRefSpecTempQSImpl() {
	}

	public String getServiceName() {
		return "EmvRefSpecTemp Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public EmvProjPersistable queryByPrimaryKey(String specID) throws QueryServiceException {
		List params = new LinkedList();
		params.add(specID);
		StringBuffer cmd = new StringBuffer(
				"SELECT * FROM EMV_REF_SPEC_TEMP  WHERE SPEC_ID = ? ");

		return (EmvRefSpecTemp) queryObject(cmd.toString(), params,
				emvRefSpecTempBuilder);
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
		EmvRefSpecTempArg obj = (EmvRefSpecTempArg) example;
		StringBuffer criteria = new StringBuffer();
		if (obj.getSpecID() != null) {
			criteria.append("SPEC_ID =? AND ");
			params.add(obj.getSpecID());
		}

		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_REF_SPEC_TEMP ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(
					criteria.substring(0, criteria.length() - 4));
		}

		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, emvRefSpecTempArgBuilder,
					obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, emvRefSpecTempArgBuilder);
	}

	public List queryAll() throws QueryServiceException {
		List params = new LinkedList();
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_REF_SPEC_TEMP");

		return queryObjectList(cmd.toString(), params, emvRefSpecTempBuilder);

	}	
	
	public int countToBeApprove() {
		List params = new LinkedList();
		String cmd = "SELECT COUNT(*) COUNT_RESULT FROM EMV_REF_SPEC_TEMP ";
		Integer c = (Integer) super.queryObject(cmd, params, new AbstractObjectBuilder() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool( rs );
				return rst.getIntegerObject("COUNT_RESULT");
			}
		});

		return c == null ? 0 : c.intValue();
	}
	
}
