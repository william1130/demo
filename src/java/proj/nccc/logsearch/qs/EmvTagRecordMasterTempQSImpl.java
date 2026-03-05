package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.sql.ResultSetTool;
import com.edstw.sql.builder.AbstractObjectBuilder;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordMasterTemp;
import proj.nccc.logsearch.vo.EmvTagRecordMasterTempArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

public class EmvTagRecordMasterTempQSImpl extends AbstractJdbcPersistableQueryService
		implements EmvTagRecordMasterTempQS {

	public EmvTagRecordMasterTempQSImpl() {
	}

	public String getServiceName() {
		return "EmvTagRecordMasterTemp Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public EmvProjPersistable queryByPrimaryKey(String emvTag, String sameValueFlag) throws QueryServiceException {
		List params = new LinkedList();
		params.add(emvTag);
		params.add(sameValueFlag);
		StringBuffer cmd = new StringBuffer(
				"SELECT * FROM EMV_TAG_RECORD_MASTER_TEMP WHERE EMV_TAG =? AND SAME_VALUE_FLAG = ? ORDER BY REQUEST_DATE DESC ");

		return (EmvTagRecordMasterTemp) queryObject(cmd.toString(), params, emvTagRecordMasterTempBuilder);
	}

	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		if (id instanceof EmvTagRecordMasterTemp.EmvTagRecordMasterTempId) {
			EmvTagRecordMasterTemp.EmvTagRecordMasterTempId idObj = (EmvTagRecordMasterTemp.EmvTagRecordMasterTempId) id;
			return queryByPrimaryKey(idObj.getEmvTag(), idObj.getSameValueFlag());
		} else {
			EmvTagRecordMasterTemp idObj = (EmvTagRecordMasterTemp) id;
			return queryByPrimaryKey(idObj.getEmvTag(), idObj.getSameValueFlag());
		}
	}

	public List queryByIds(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	public List queryByPrimaryKeys(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	public List queryByExample(ProjPersistableArg example) throws QueryServiceException {
		List params = new LinkedList();
		EmvTagRecordMasterTempArg obj = (EmvTagRecordMasterTempArg) example;
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getEmvTag())) {
			criteria.append("AND EMV_TAG=? ");
			params.add(obj.getCardType());
		}

		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria.append("AND SAME_VALUE_FLAG=? ");
			params.add(obj.getCardType());
		}

		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_MASTER_TEMP ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria.substring(0, criteria.length() - 4))
					.append(" ORDER BY REQUEST_DATE DESC");
		}

		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, emvTagRecordMasterTempArgBuilder, obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, emvTagRecordMasterTempArgBuilder);
	}

	public List queryAll() throws QueryServiceException {
		List params = new LinkedList();
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_MASTER_TEMP");

		return queryObjectList(cmd.toString(), params, emvTagRecordMasterTempBuilder);

	}

	public int countToBeApprove() {
		List params = new LinkedList();
		String cmd = "SELECT COUNT(*) COUNT_RESULT FROM EMV_TAG_RECORD_MASTER_TEMP ";
		Integer c = (Integer) super.queryObject(cmd, params, new AbstractObjectBuilder() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				return rst.getIntegerObject("COUNT_RESULT");
			}
		});

		return c == null ? 0 : c.intValue();
	}

	/**
	 * @return
	 * @throws QueryServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<EmvTagRecordMasterTempArg> query(EmvTagRecordMasterTempArg arg) throws QueryServiceException {

		List<Object> params = new ArrayList<Object>();
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(arg.getDateFrom())) {
			// criteria.append("AND request_date >= CONVERT( VARCHAR , ?)");
			criteria.append("AND TO_CHAR(request_date, 'YYYYMMDD') >= ? ");
			params.add(arg.getDateFrom());
		}

		// 日期迄
		if (ValidateUtil.isNotBlank(arg.getDateTo())) {
			// criteria.append("AND request_date < DATEADD(DAY , 1 , ?) ");
			criteria.append("AND request_date < TO_DATE(? ,'YYYY-MM-DD') + INTERVAL '1' DAY ");
			params.add(arg.getDateTo());
		}

		if (ValidateUtil.isNotBlank(arg.getUserId())) {
			criteria.append("AND USER_ID=?  ");
			params.add(arg.getUserId());
		}

		StringBuffer cmd = new StringBuffer().append("select * from EMV_TAG_RECORD_MASTER_TEMP where 1 = 1 ");
		if (criteria.length() > 0) {
			cmd.append(criteria.substring(0, criteria.length())).append(" ORDER BY REQUEST_DATE DESC");
		}

		if (arg.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, emvTagRecordMasterTempArgBuilder, arg.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, emvTagRecordMasterTempArgBuilder);
	}

}
