package proj.nccc.logsearch.qs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.EmvCardType;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.ProjNameMapQS.Builder;
import proj.nccc.logsearch.qs.ProjNameMapQS.ValueObject;
import proj.nccc.logsearch.vo.EmvCardTypeArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

public class EmvCardTypeQSImpl extends AbstractJdbcPersistableQueryService implements EmvCardTypeQS {
	
	public EmvCardTypeQSImpl() {
	}

	public String getServiceName() {
		return "EmvCardType Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public EmvProjPersistable queryByPrimaryKey(String cardType) throws QueryServiceException {
		List params = new LinkedList();
		params.add(cardType);
		StringBuffer cmd = new StringBuffer(
				"SELECT * FROM EMV_CARD_TYPE  WHERE CARD_TYPE =? ");

		return (EmvCardType) queryObject(cmd.toString(), params,
				emvCardTypeBuilder);
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
		EmvCardTypeArg obj = (EmvCardTypeArg) example;
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria.append("CARD_TYPE=? AND ");
			params.add(obj.getCardType());
		}
		
		if (ValidateUtil.isNotBlank(obj.getCardTypeName())) {
			criteria.append("CARD_TYPE_NAME=? AND ");
			params.add(obj.getCardTypeName());
		}

		if (ValidateUtil.isNotBlank(obj.getStatus())) {
			criteria.append("STATUS=? AND ");
			params.add(obj.getStatus());
		}		

		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_CARD_TYPE ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(
					criteria.substring(0, criteria.length() - 4));
		}

		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, emvCardTypeBuilder,
					obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, emvCardTypeBuilder);
	}

	public List queryAll() throws QueryServiceException {
		List params = new LinkedList();
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_CARD_TYPE");

		return queryObjectList(cmd.toString(), params, emvCardTypeBuilder);

	}	
	
}
