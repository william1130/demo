package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.EmvTagRecordDetail;
import proj.nccc.logsearch.persist.EmvTagRecordDetailTemp;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordMasterTemp;
import proj.nccc.logsearch.vo.EmvTagRecordDetailTempArg;
import proj.nccc.logsearch.vo.EmvTagRecordMasterTempArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.sql.ResultSetTool;
import com.edstw.sql.builder.AbstractObjectBuilder;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

public class EmvTagRecordDetailTempQSImpl extends AbstractJdbcPersistableQueryService implements EmvTagRecordDetailTempQS {
	
	public EmvTagRecordDetailTempQSImpl() {
	}

	public String getServiceName() {
		return "EmvTagRecordDetailTemp Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public EmvProjPersistable queryByPrimaryKey(String emvTag, String cardType, Integer posByte, Integer posBit, String sameValueFlag) throws QueryServiceException {
		List params = new LinkedList();
		params.add(emvTag);
		params.add(cardType);
		params.add(posByte);
		params.add(posBit);
		params.add(sameValueFlag);
		StringBuffer cmd = new StringBuffer(
				"SELECT * FROM EMV_TAG_RECORD_DETAIL_TEMP WHERE EMV_TAG =? AND CARD_TYPE = ? AND POS_BYTE = ? AND POS_BIT = ? AND SAME_VALUE_FLAG = ? ");

		return (EmvTagRecordDetailTemp) queryObject(cmd.toString(), params,
				emvTagRecordDetailTempBuilder);
	}

	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		EmvTagRecordDetailTemp.EmvTagRecordDetailTempId idObj = (EmvTagRecordDetailTemp.EmvTagRecordDetailTempId) id;
		return queryByPrimaryKey(idObj.getEmvTag(), idObj.getCardType(),
				idObj.getPosByte(), idObj.getPosBit(), idObj.getSameValueFlag());
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
		EmvTagRecordMasterTempArg obj = (EmvTagRecordMasterTempArg) example;
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getEmvTag())) {
			criteria.append("EMV_TAG=? AND ");
			params.add(obj.getEmvTag());
		}
		
		if (ValidateUtil.isNotBlank(obj.getSameValueFlag())) {
			criteria.append("SAME_VALUE_FLAG=? AND ");
			params.add(obj.getSameValueFlag());
		}

		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_DETAIL_TEMP ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(
					criteria.substring(0, criteria.length() - 4));
		}

		return queryObjectList(cmd.toString(), params, emvTagRecordDetailTempBuilder);
	}

	

	public List queryAll() throws QueryServiceException {
		List params = new LinkedList();
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_DETAIL_TEMP");

		return queryObjectList(cmd.toString(), params, emvTagRecordDetailTempBuilder);

	}	
	
	public int countToBeApprove() {
		List params = new LinkedList();
		String cmd = "SELECT COUNT(*) COUNT_RESULT FROM EMV_TAG_RECORD_DETAIL_TEMP ";
		Integer c = (Integer) super.queryObject(cmd, params, new AbstractObjectBuilder() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool( rs );
				return rst.getIntegerObject("COUNT_RESULT");
			}
		});

		return c == null ? 0 : c.intValue();
	}
	
	public List queryByEmvTagAndSameValueFlag(String emvTag, String sameValueFlag) throws QueryServiceException{
		List params = new LinkedList();

		StringBuffer criteria = new StringBuffer();
		criteria.append("EMV_TAG=?  ");
		params.add(emvTag);
		
		criteria.append("AND SAME_VALUE_FLAG=?  ");
		params.add(sameValueFlag);
		
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_DETAIL_TEMP ");
		//StringBuffer cmd = new StringBuffer("SELECT DISTINCT EMV_TAG, POS_BYTE, VALUE_BYTE, POS_BIT, VALUE_BIT, SAME_VALUE_FLAG FROM EMV_TAG_RECORD_DETAIL ");
		
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria).append("ORDER BY POS_BYTE ASC, POS_BIT DESC");
		}
		
		return queryObjectList(cmd.toString(), params, emvTagRecordDetailTempBuilder);
	}
	
}
