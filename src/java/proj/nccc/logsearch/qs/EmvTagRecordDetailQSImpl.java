package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.EmvTagRecordDetail;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.EmvTagRecordDetailArg;
import proj.nccc.logsearch.vo.EmvTagRecordDetailTempArg;
import proj.nccc.logsearch.vo.EmvTagRecordMasterArg;
import proj.nccc.logsearch.vo.EmvTagRecordMasterTempArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

public class EmvTagRecordDetailQSImpl extends AbstractJdbcPersistableQueryService implements EmvTagRecordDetailQS {
	
	public EmvTagRecordDetailQSImpl() {
	}

	public String getServiceName() {
		return "EmvTagRecordDetail Query Service";
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
				"SELECT * FROM EMV_TAG_RECORD_DETAIL  WHERE EMV_TAG =? AND CARD_TYPE = ? AND POS_BYTE = ? AND POS_BIT = ? and SAME_VALUE_FLAG = ?");

		return (EmvTagRecordDetail) queryObject(cmd.toString(), params,
				emvTagRecordDetailBuilder);
	}

	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		EmvTagRecordDetail.EmvTagRecordDetailId idObj = (EmvTagRecordDetail.EmvTagRecordDetailId) id;
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
		StringBuffer criteria = new StringBuffer();
		
		if(example instanceof EmvTagRecordDetailArg){
			EmvTagRecordDetailArg obj = (EmvTagRecordDetailArg) example;
			if (ValidateUtil.isNotBlank(obj.getEmvTag())) {
				criteria.append("EMV_TAG=? AND  ");
				params.add(obj.getEmvTag());
			}
			
			if (ValidateUtil.isNotBlank(obj.getSameValueFlag())) {
				criteria.append("SAME_VALUE_FLAG=? AND ");
				params.add(obj.getSameValueFlag());
			}
		}
		if(example instanceof EmvTagRecordDetailTempArg){
			EmvTagRecordDetailTempArg obj = (EmvTagRecordDetailTempArg) example;
			if (ValidateUtil.isNotBlank(obj.getEmvTag())) {
				criteria.append("EMV_TAG=? AND ");
				params.add(obj.getEmvTag());
			}

			if (ValidateUtil.isNotBlank(obj.getSameValueFlag())) {
				criteria.append("SAME_VALUE_FLAG=? AND ");
				params.add(obj.getSameValueFlag());
			}
		}
		if(example instanceof EmvTagRecordMasterArg){
			EmvTagRecordMasterArg obj = (EmvTagRecordMasterArg) example;
			if (ValidateUtil.isNotBlank(obj.getEmvTag())) {
				criteria.append("EMV_TAG=? AND ");
				params.add(obj.getEmvTag());
			}

			if (ValidateUtil.isNotBlank(obj.getSameValueFlag())) {
				criteria.append("SAME_VALUE_FLAG=? AND ");
				params.add(obj.getSameValueFlag());
			}
		}
		
		if(example instanceof EmvTagRecordMasterTempArg){
			EmvTagRecordMasterTempArg obj = (EmvTagRecordMasterTempArg) example;
			if (ValidateUtil.isNotBlank(obj.getEmvTag())) {
				criteria.append("EMV_TAG=? AND ");
				params.add(obj.getEmvTag());
			}
			
			if (ValidateUtil.isNotBlank(obj.getSameValueFlag())) {
				criteria.append("SAME_VALUE_FLAG=? AND ");
				params.add(obj.getSameValueFlag());
			}
		}
		
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_DETAIL ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(
					criteria.substring(0, criteria.length() - 4));
		}

		return queryObjectList(cmd.toString(), params, emvTagRecordDetailBuilder);
		
	}
	
	public List queryByEmvTagAndSameValueFlag(String emvTag, String sameValueFlag) throws QueryServiceException{
		List params = new LinkedList();

		StringBuffer criteria = new StringBuffer();
		criteria.append("EMV_TAG=?  ");
		params.add(emvTag);
		
		criteria.append("AND SAME_VALUE_FLAG=?  ");
		params.add(sameValueFlag);
		
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_DETAIL ");
		//StringBuffer cmd = new StringBuffer("SELECT DISTINCT EMV_TAG, POS_BYTE, VALUE_BYTE, POS_BIT, VALUE_BIT, SAME_VALUE_FLAG FROM EMV_TAG_RECORD_DETAIL ");
		
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria).append("ORDER BY POS_BYTE ASC, POS_BIT DESC");
		}
		
		return queryObjectList(cmd.toString(), params, emvTagRecordDetailBuilder);
	}

	public List queryAll() throws QueryServiceException {
		List params = new LinkedList();
		StringBuffer cmd = new StringBuffer("SELECT * FROM EMV_TAG_RECORD_DETAIL");

		return queryObjectList(cmd.toString(), params, emvTagRecordDetailBuilder);

	}	
	
}
