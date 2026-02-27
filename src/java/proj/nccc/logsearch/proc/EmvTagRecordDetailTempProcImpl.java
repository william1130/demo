package proj.nccc.logsearch.proc;

import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordDetail;
import proj.nccc.logsearch.persist.EmvTagRecordDetailTemp;
import proj.nccc.logsearch.persist.EmvTagRecordMaster;
import proj.nccc.logsearch.persist.EmvTagRecordMasterTemp;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.vo.ApproveParaArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.Persistable;
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
public class EmvTagRecordDetailTempProcImpl extends ParaPairCRUDProc implements
EmvTagRecordDetailTempProc {

/**
 * Creates a new instance of EmvTagRecordDetailTempProcImpl
 */
public EmvTagRecordDetailTempProcImpl() {
}

public BaseCRUDQS getBaseCRUDQS() {
	return ProjServices.getEmvTagRecordDetailTempQS();
}


public EmvProjPersistable createEmptyProjPersistable() {
	return new EmvTagRecordDetailTemp();
}

protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
		throws Exception {
	StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
}

public String getServiceName() {
	return "EmvTagRecordDetailTemp Process";
}

public void setServiceParams(Map map) throws ServiceException {
	// do nothing
}

public EmvProjPersistable createEmptyCorrespondentPersistable() {
	return new EmvTagRecordDetail();
}

public BaseCRUDQS getCorrespondentCRUDQS() {
	return ProjServices.getEmvTagRecordDetailQS();
}


/*public void beforePersistOnApprove(ApproveParaArg approveParaArg, EmvProjPersistable paraObj) {
	EmvTagRecordMaster para = (EmvTagRecordMaster) paraObj;
	para.setStatus(ProjConstants.Para_Status.ON.getCode());		
}

protected void beforePersistOnReject(EmvProjPersistable paraObj) throws Exception {
	EmvTagRecordMaster para = (EmvTagRecordMaster) paraObj;		
	
	para.setStatus(ProjConstants.Para_Status.ON.getCode());		
}*/

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

		List<EmvTagRecordDetailTemp> approveList = getBaseCRUDQS().queryByExample(arg);
		//StatefulPersistableUtil.copyPropertiesWithoutState(arg, approveList);
		ApproveParaArg apArg = (ApproveParaArg) arg;
		List<EmvProjPersistable> paraObj = null;
//		List<EmvProjPersistable> paraObj_T = null;

		if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.ADD.getCode())) {
			pm.insert((EmvProjPersistable) approveList);
		} else if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.UPDATE.getCode())) {
			pm.update( (EmvProjPersistable) paraObj);
		} else if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.DELETE.getCode())) {
			pm.delete( (EmvProjPersistable)paraObj);
		}
		pm.delete( (EmvProjPersistable) approveList);
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
		EmvProjPersistable paraObj_T = createEmptyCorrespondentPersistable();
		StatefulPersistableUtil.copyPropertiesWithoutState(paraObj_T, arg);
		EmvProjPersistable paraObj = getCorrespondentCRUDQS().queryById(paraObj_T.getId());

		if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.ADD.getCode())) {
			
		} else if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.UPDATE.getCode())) {
			beforePersistOnReject(paraObj);
			pm.update(paraObj);
		} else if (apArg.getActiveCode().equals(
				ProjConstants.Active_Code.DELETE.getCode())) {
			beforePersistOnReject(paraObj);
			//paraObj.setUpdateInfo(AlterInfo.createAlterInfo());
			pm.update(paraObj);
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


}
