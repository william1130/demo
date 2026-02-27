package proj.nccc.logsearch.qs;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvUiLog;
import proj.nccc.logsearch.vo.ProjPersistableArg;
import proj.nccc.logsearch.vo.EmvUiLogArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

public class EmvUiLogQSImpl extends AbstractJdbcPersistableQueryService implements
		EmvUiLogQS {
	/** Creates a new instance of EmvUiLogQSImpl */
	public EmvUiLogQSImpl() {
	}

	public String getServiceName() {
		return "EmvUiLog Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public EmvProjPersistable queryByPrimaryKey(Date uiDate, String userId, String uuid)
			throws QueryServiceException {
		List params = new LinkedList();
		MyDateUtil dateUtil = new MyDateUtil();
		params.add(dateUtil.toString(uiDate, dateUtil.FULL_DATETIME));
		params.add(userId);
		StringBuffer cmd = new StringBuffer(
				"SELECT * FROM EMV_UI_LOG  WHERE UI_DATE = CONVERT( VARCHAR , ?) AND USER_ID=? AND UUID=? ");

		return (EmvUiLog) queryObject(cmd.toString(), params, emvUiLogBuilder);
	}

	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		EmvUiLog.EmvUiLogId idObj = (EmvUiLog.EmvUiLogId) id;
		return queryByPrimaryKey(idObj.getUiDate(), idObj.getUserId(),idObj.getUuid());
	}

	public List queryByIds(List ids) throws QueryServiceException {
		return null;
	}

	public List queryByPrimaryKeys(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	public List queryByExample(ProjPersistableArg example)
			throws QueryServiceException {
		List params = new LinkedList();
		MyDateUtil dateUtil = new MyDateUtil();
		EmvUiLogArg obj = (EmvUiLogArg) example;
		StringBuffer criteria = new StringBuffer();
		
		if (ValidateUtil.isNotBlank(obj.getDateFrom())) {
			criteria.append("UI_DATE >= CONVERT( VARCHAR , ?) AND ");
			params.add(obj.getDateFrom());
		}
		
		if (ValidateUtil.isNotBlank(obj.getDateTo())) {
			criteria.append("UI_DATE < CONVERT( VARCHAR , ?) AND ");
			Date afterDateTo =  dateUtil.getDateBeforeAfterByDay(
					dateUtil.toDateYYYYMMDD(obj.getDateTo()), 1, "AFTER");
			params.add(dateUtil.toString(afterDateTo, dateUtil.YYYYMMDD));
		}		

		if (ValidateUtil.isNotBlank(obj.getUserId())) {
			criteria.append("USER_ID=? AND ");
			params.add(obj.getUserId());
		}
		
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_UI_LOG ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(
					criteria.substring(0, criteria.length() - 4));
		}
		
		cmd.append("ORDER BY UI_DATE");

		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, emvUiLogBuilder,
					obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, emvUiLogBuilder);
	}	
	
	public List queryAll() throws QueryServiceException {
		throw new UnsupportedOperationException();
		// StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_UI_LOG ");
		// return super.query( cmd.toString(), uiLogBuilder );
	}	
	
}
