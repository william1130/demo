package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.EmvCardTypeTemp;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.EmvCardTypeTempArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.sql.ResultSetTool;
import com.edstw.sql.builder.AbstractObjectBuilder;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

public class EmvCardTypeTempQSImpl extends AbstractJdbcPersistableQueryService implements EmvCardTypeTempQS {
	
	public EmvCardTypeTempQSImpl() {
	}

	public String getServiceName() {
		return "EmvCardTypeTemp Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public EmvProjPersistable queryByPrimaryKey(String cardType) throws QueryServiceException {
		List params = new LinkedList();
		params.add(cardType);
		StringBuffer cmd = new StringBuffer(
				"SELECT * FROM EMV_CARD_TYPE_TEMP  WHERE CARD_TYPE =? ");

		return (EmvCardTypeTemp) queryObject(cmd.toString(), params,
				emvCardTypeTempBuilder);
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
		EmvCardTypeTempArg obj = (EmvCardTypeTempArg) example;
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria.append("CARD_TYPE=? AND ");
			params.add(obj.getCardType());
		}

		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_CARD_TYPE_TEMP ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(
					criteria.substring(0, criteria.length() - 4));
		}

		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, emvCardTypeTempArgBuilder,
					obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, emvCardTypeTempArgBuilder);
	}

	public List queryAll() throws QueryServiceException {
		List params = new LinkedList();
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_CARD_TYPE_TEMP");

		return queryObjectList(cmd.toString(), params, emvCardTypeTempBuilder);

	}	
	
	public int countToBeApprove() {
		List params = new LinkedList();
		String cmd = "SELECT COUNT(*) COUNT_RESULT FROM EMV_CARD_TYPE_TEMP ";
		Integer c = (Integer) super.queryObject(cmd, params, new AbstractObjectBuilder() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool( rs );
				return rst.getIntegerObject("COUNT_RESULT");
			}
		});

		return c == null ? 0 : c.intValue();
	}
	
}
