package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordMaster;
import proj.nccc.logsearch.vo.EmvTagRecordMasterArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmvTagRecordMasterQSImpl extends ProjQS implements EmvTagRecordMasterQS {
	
	public EmvTagRecordMasterQSImpl() {
	}

	public String getServiceName() {
		return "EmvTagRecordMaster Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public EmvProjPersistable queryByPrimaryKey(String emvTag, String sameValueFlag) throws QueryServiceException {
		List params = new LinkedList();
		params.add(emvTag);
		params.add(sameValueFlag);

		StringBuffer cmd = new StringBuffer(
				"SELECT * FROM EMV_TAG_RECORD_MASTER  WHERE EMV_TAG =? AND SAME_VALUE_FLAG = ? ");

		return (EmvTagRecordMaster) queryObject(cmd.toString(), params,
				emvTagRecordMasterBuilder);
	}

	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		if(id instanceof EmvTagRecordMaster.EmvTagRecordMasterId){
			EmvTagRecordMaster.EmvTagRecordMasterId idObj = (EmvTagRecordMaster.EmvTagRecordMasterId) id;
			return queryByPrimaryKey(idObj.getEmvTag(), idObj.getSameValueFlag());
		}else{
			EmvTagRecordMaster idObj = (EmvTagRecordMaster) id;
			return queryByPrimaryKey(idObj.getEmvTag(), idObj.getSameValueFlag());
		}
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
		EmvTagRecordMasterArg obj = (EmvTagRecordMasterArg) example;
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getEmvTag())) {
			criteria.append("EMV_TAG=?  AND ");
			params.add(obj.getEmvTag());
		}
		
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria.append("CARD_TYPE LIKE ? AND ");
			params.add("%"+obj.getCardType()+"%");
		}
		

		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_MASTER ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(
					criteria.substring(0, criteria.length() - 4));
		}

		if (obj.getPagingInfo().isEnablePaging())
			return this.queryByPagingInfo(cmd.toString(), params, emvTagRecordMasterBuilder,
					obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, emvTagRecordMasterBuilder);
	}

	public List queryAll() throws QueryServiceException {
		List params = new LinkedList();
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_MASTER");

		return queryObjectList(cmd.toString(), params, emvTagRecordMasterBuilder);

	}
	
}
