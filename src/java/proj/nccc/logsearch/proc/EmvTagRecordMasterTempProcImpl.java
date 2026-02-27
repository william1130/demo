package proj.nccc.logsearch.proc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordDetail;
import proj.nccc.logsearch.persist.EmvTagRecordDetailTemp;
import proj.nccc.logsearch.persist.EmvTagRecordMaster;
import proj.nccc.logsearch.persist.EmvTagRecordMasterTemp;
import proj.nccc.logsearch.persist.EmvTagRecordMaster.EmvTagRecordMasterId;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.qs.EmvTagRecordMasterTempQSImpl;
import proj.nccc.logsearch.qs.EmvUiLogQueryQSImpl;
import proj.nccc.logsearch.vo.ApproveParaArg;
import proj.nccc.logsearch.vo.EmvTagRecordMasterArg;
import proj.nccc.logsearch.vo.EmvTagRecordMasterTempArg;
import proj.nccc.logsearch.vo.EmvUiLogArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.PersistableManager;
import com.edstw.persist.PersistableTransaction;
import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.process.ProcessException;
import com.edstw.service.ServiceException;

/**
 *
 * @author
 * @version
 * */
public class EmvTagRecordMasterTempProcImpl extends ParaPairCRUDProc implements
EmvTagRecordMasterTempProc {

/**
 * Creates a new instance of EmvTagRecordTempProcImpl
 */
public EmvTagRecordMasterTempProcImpl() {
}

public BaseCRUDQS getBaseCRUDQS() {
	return ProjServices.getEmvTagRecordMasterTempQS();
}
public BaseCRUDQS getDetailCRUDQS() {
	return ProjServices.getEmvTagRecordDetailTempQS();
}

public EmvProjPersistable createEmptyProjPersistable() {
	return new EmvTagRecordMasterTemp();
}

protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
		throws Exception {
	StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
}

public String getServiceName() {
	return "EmvTagRecordTemp Process";
}

public void setServiceParams(Map map) throws ServiceException {
	// do nothing
}

public EmvProjPersistable createEmptyCorrespondentPersistable() {
	return new EmvTagRecordMaster();
}

public EmvProjPersistable createEmptyCorrespondentDetailPersistable() {
	return new EmvTagRecordDetail();
}

public BaseCRUDQS getCorrespondentCRUDQS() {
	return ProjServices.getEmvTagRecordMasterQS();
}

public BaseCRUDQS getCorrespondentDetailCRUDQS() {
	return ProjServices.getEmvTagRecordDetailQS();
}

public void beforePersistOnApprove(ApproveParaArg approveParaArg, EmvProjPersistable paraObj) {
	EmvTagRecordMaster para = (EmvTagRecordMaster) paraObj;
	para.setStatus(ProjConstants.Para_Status.ON.getCode());		
}

protected void beforePersistOnReject(EmvProjPersistable paraObj) throws Exception {
	EmvTagRecordMaster para = (EmvTagRecordMaster) paraObj;		
	
	para.setStatus(ProjConstants.Para_Status.ON.getCode());		
}

@Override
public List query(ProjPersistableArg arg) throws ProcessException {
	PersistableManager pm = super.currentPersistableManager();
	PersistableTransaction pt = pm.getTransaction();
	
	try {
		pt = pm.getTransaction();
		pt.begin();
		
		List list = getBaseCRUDQS().queryByExample(arg);
		afterQueryList(arg, list);
		//addUiLog(arg);	
		pt.commit();
		
		return list;
	} catch (Exception e) {
		try {
			pt.rollback();
		} catch (Exception e1) {
			getLog().error(e1.getMessage(), e);
		}
		getLog().error(e.getMessage(), e);
		throw new ProcessException(e.getMessage(), e);
	}
}

	@Override
public void approve(ProjPersistableArg arg) throws ProcessException {
	PersistableManager pm = super.currentPersistableManager();
	PersistableTransaction pt = pm.getTransaction();
	try {
		pt.begin();

		EmvProjPersistable approveObj = getBaseCRUDQS().queryById(arg.getId());
		EmvTagRecordMasterTemp emvTemp = (EmvTagRecordMasterTemp) approveObj;
		List<EmvTagRecordDetailTemp> approveList = ProjServices.getEmvTagRecordDetailTempQS().queryByExample(arg);
		StatefulPersistableUtil.copyPropertiesWithoutState(arg, approveObj);
		ApproveParaArg apArg = (ApproveParaArg) arg;
		String emvTag = "";
		String sameValueFlag = "";
		if(approveList!=null && approveList.size() >0){
			emvTag = approveList.get(0).getEmvTag();
			sameValueFlag = approveList.get(0).getSameValueFlag();
		}
		
		EmvProjPersistable paraObj = null;
		EmvProjPersistable paraDetailObj = null;
		EmvProjPersistable paraObj_T = null;

		if (apArg.getActiveCode().equals(ProjConstants.Active_Code.ADD.getCode())) {
			paraObj = createEmptyCorrespondentPersistable();
			StatefulPersistableUtil.copyPropertiesWithoutState(paraObj,
					approveObj);
			if(arg.getCreateInfo() != null){
				paraObj.setCreateInfo(arg.getCreateInfo());
			}else{
				paraObj.setCreateInfo(arg.getRequestInfo());
			}

			beforePersistOnApprove(apArg, paraObj);
			pm.insert(paraObj);
			for (Iterator<EmvTagRecordDetailTemp> iterator = approveList.iterator(); iterator.hasNext();) {
				EmvTagRecordDetailTemp approveListDetail = (EmvTagRecordDetailTemp) iterator.next();
				paraDetailObj = createEmptyCorrespondentDetailPersistable();
				StatefulPersistableUtil.copyPropertiesWithoutState(paraDetailObj, approveListDetail);
				pm.insert( (EmvProjPersistable) paraDetailObj);
				pm.delete ( approveListDetail);
			}
		} else if (apArg.getActiveCode().equals(ProjConstants.Active_Code.UPDATE.getCode())) {
			paraObj_T = createEmptyCorrespondentPersistable();
			StatefulPersistableUtil.copyPropertiesWithoutState(paraObj_T, arg);
			//EmvTagRecordMaster emvTag = (EmvTagRecordMaster) arg;
			paraObj = getCorrespondentCRUDQS().queryById(paraObj_T.getId());			
			if(paraObj == null){
				String oriSameValueFlag = emvTemp.getOriSameValueFlag() != null ? emvTemp.getOriSameValueFlag(): "Y";
				EmvTagRecordMaster.EmvTagRecordMasterId id = new EmvTagRecordMaster().new EmvTagRecordMasterId(emvTemp.getEmvTag(), oriSameValueFlag);
				paraObj = getCorrespondentCRUDQS().queryById(id);
			}
			
			if(paraObj == null){
				
				paraObj = createEmptyCorrespondentPersistable();
				StatefulPersistableUtil.copyPropertiesWithoutState(paraObj,
						approveObj);
				if(arg.getCreateInfo() != null){
					paraObj.setCreateInfo(arg.getCreateInfo());
				}else{
					paraObj.setCreateInfo(arg.getRequestInfo());
				}

				beforePersistOnApprove(apArg, paraObj);
				pm.insert(paraObj);
				
				EmvTagRecordMaster oriEmvTag = (EmvTagRecordMaster) ProjServices.getEmvTagRecordMasterQS().queryByPrimaryKey(emvTag, "Y");				
				//StatefulPersistableUtil.copyPropertiesWithoutState(oriEmvTag, approveObj);
				if(arg.getCreateInfo() != null){
					oriEmvTag.setCreateInfo(arg.getCreateInfo());
				}else{
					oriEmvTag.setCreateInfo(arg.getRequestInfo());
				}
				oriEmvTag.setUpdateInfo(arg.getRequestInfo());				
				oriEmvTag.setCardType(oriEmvTag.getCardType().replace(sameValueFlag, ""));
				oriEmvTag.setCardType(oriEmvTag.getCardType().replace(",,", ","));
				oriEmvTag.setStatus("Y");
				//String updateCardType = oriEmvTag.getCardType();
				pm.update(oriEmvTag);
				
				List<EmvTagRecordDetail> updateList = ProjServices.getEmvTagRecordDetailQS().queryByEmvTagAndSameValueFlag(emvTag, "Y");
				for (Iterator<EmvTagRecordDetail> iterator = updateList.iterator(); iterator.hasNext();) {
					EmvTagRecordDetail updateListDetail = (EmvTagRecordDetail) iterator.next();
					updateListDetail.setCardType(oriEmvTag.getCardType());
					paraDetailObj = createEmptyCorrespondentDetailPersistable();
					StatefulPersistableUtil.copyPropertiesWithoutState(paraDetailObj, updateListDetail);
					pm.update(paraDetailObj);
				}
				
				for (Iterator<EmvTagRecordDetailTemp> iterator = approveList.iterator(); iterator.hasNext();) {
					EmvTagRecordDetailTemp approveListDetail = (EmvTagRecordDetailTemp) iterator.next();
					paraDetailObj = createEmptyCorrespondentDetailPersistable();
					StatefulPersistableUtil.copyPropertiesWithoutState(paraDetailObj, approveListDetail);
					pm.insert( (EmvProjPersistable) paraDetailObj);
					pm.delete ( approveListDetail);
				}				
				
			}else{
				StatefulPersistableUtil.copyPropertiesWithoutState(paraObj, approveObj);
				paraObj.setCreateInfo(paraObj.getCreateInfo());
				paraObj.setUpdateInfo(arg.getRequestInfo());
				beforePersistOnApprove(apArg, paraObj);
				
				pm.update(paraObj);
				List<EmvTagRecordDetail> updateList = ProjServices.getEmvTagRecordDetailQS().queryByExample(arg);
				for (Iterator<EmvTagRecordDetail> iterator = updateList.iterator(); iterator.hasNext();) {
					EmvTagRecordDetail updateListDetail = (EmvTagRecordDetail) iterator.next();
					pm.delete ( updateListDetail);
				}
				
				for (Iterator<EmvTagRecordDetailTemp> iterator = approveList.iterator(); iterator.hasNext();) {
					EmvTagRecordDetailTemp approveListDetail = (EmvTagRecordDetailTemp) iterator.next();
					paraDetailObj = createEmptyCorrespondentDetailPersistable();
					StatefulPersistableUtil.copyPropertiesWithoutState(paraDetailObj, approveListDetail);
					pm.insert( (EmvProjPersistable) paraDetailObj);
					pm.delete ( approveListDetail);
				}
			}
			
			
			
		} else if (apArg.getActiveCode().equals(ProjConstants.Active_Code.DELETE.getCode())) {
			paraObj_T = createEmptyCorrespondentPersistable();
			StatefulPersistableUtil.copyPropertiesWithoutState(paraObj_T, arg);
			paraObj = getCorrespondentCRUDQS().queryById(paraObj_T.getId());
			pm.delete(paraObj);
			List<EmvTagRecordDetail> deleteList = ProjServices.getEmvTagRecordDetailQS().queryByExample(arg);
			
			for (Iterator<EmvTagRecordDetail> iterator = deleteList.iterator(); iterator.hasNext();) {
				EmvTagRecordDetail deleteListDetail = (EmvTagRecordDetail) iterator.next();
				paraDetailObj = createEmptyCorrespondentDetailPersistable();
				StatefulPersistableUtil.copyPropertiesWithoutState(paraDetailObj, deleteListDetail);
				pm.delete ( deleteListDetail);
			}
			
		}
		pm.delete(approveObj);
		addUiLog(arg);
		pt.commit();
	} catch (Exception e) {
		try {
			pt.rollback();
		} catch (Exception e1) {
			getLog().error(e1.getMessage(), e);
		}
		getLog().error(e.getMessage(), e);
		throw new ProcessException(e.getMessage(), e);
	}
}

@Override
public void reject(ProjPersistableArg arg) throws ProcessException {
	PersistableManager pm = super.currentPersistableManager();
	PersistableTransaction pt = pm.getTransaction();
	
	try {
		pt.begin();
		EmvProjPersistable approveObj = getBaseCRUDQS().queryById(arg.getId());
		StatefulPersistableUtil.copyPropertiesWithoutState(arg, approveObj);
		ApproveParaArg apArg = (ApproveParaArg) arg;
		List<EmvTagRecordDetailTemp> approveList = ProjServices.getEmvTagRecordDetailTempQS().queryByExample(arg);
		EmvProjPersistable paraObj_T = createEmptyCorrespondentPersistable();
		EmvTagRecordMasterTemp argReal = (EmvTagRecordMasterTemp) arg;
		StatefulPersistableUtil.copyPropertiesWithoutState(paraObj_T, arg);
		EmvTagRecordMaster.EmvTagRecordMasterId paraId = new EmvTagRecordMaster().new EmvTagRecordMasterId(argReal.getEmvTag(), !argReal.getOriSameValueFlag().equals("") ? argReal.getOriSameValueFlag() : argReal.getSameValueFlag() );
		EmvProjPersistable paraObj = getCorrespondentCRUDQS().queryById(paraId);
		EmvProjPersistable paraDetailObj = null;
		
		if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.ADD.getCode())) {
			
		} else if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.UPDATE.getCode())) {
			beforePersistOnReject(paraObj);
			pm.update(paraObj);
		} else if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.DELETE.getCode())) {
			beforePersistOnReject(paraObj);
			paraObj.setUpdateInfo(AlterInfo.createAlterInfo());
			pm.update(paraObj);
		}
		
		for (Iterator<EmvTagRecordDetailTemp> iterator = approveList.iterator(); iterator.hasNext();) {
			EmvTagRecordDetailTemp approveListDetail = (EmvTagRecordDetailTemp) iterator.next();
			paraDetailObj = createEmptyCorrespondentDetailPersistable();
			StatefulPersistableUtil.copyPropertiesWithoutState(paraDetailObj, approveListDetail);
			pm.delete ( approveListDetail);
		}
		
		pm.delete(approveObj);
		addUiLog(arg);
		pt.commit();
	} catch (Exception e) {
		try {
			pt.rollback();
		} catch (Exception e1) {
			getLog().error(e1.getMessage(), e);
		}
		getLog().error(e.getMessage(), e);
		throw new ProcessException(e.getMessage(), e);
	}
}

public List<EmvTagRecordMasterTempArg> getList(EmvTagRecordMasterTempArg arg) throws ProcessException {

	List<EmvTagRecordMasterTempArg> list = null;

	try {

		EmvTagRecordMasterTempQSImpl qs = (EmvTagRecordMasterTempQSImpl) ProjServices.getEmvTagRecordMasterTempQS();

		list = qs.query(arg);

	} catch (Exception e) {

		e.printStackTrace();
		throw new ProcessException(e);
	}

	return list;
}

}
