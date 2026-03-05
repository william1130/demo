package proj.nccc.logsearch.proc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordMaster;
import proj.nccc.logsearch.persist.EmvTagRecordMasterTemp;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.vo.ApproveParaArg;
import proj.nccc.logsearch.vo.EmvTagRecordMasterArg;
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
public class EmvTagRecordMasterProcImpl extends ParaPairCRUDProc implements EmvTagRecordMasterProc {

	public EmvTagRecordMasterProcImpl() {
	}

	public BaseCRUDQS getBaseCRUDQS() {
		return ProjServices.getEmvTagRecordMasterQS();
	}
	

	public BaseCRUDQS getCorrespondentCRUDQS() {
		return ProjServices.getEmvTagRecordMasterTempQS();
	}

	
	public EmvProjPersistable createEmptyProjPersistable() {
		return new EmvTagRecordMaster();
	}
	
	public EmvProjPersistable createEmptyCorrespondentPersistable() {
		return new EmvTagRecordMasterTemp();
	}
	
	
	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
			throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
	}

	public String getServiceName() {
		return "EmvTagRecord Process";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	protected void beforePersistOnCreate(ProjPersistableArg arg,
			EmvProjPersistable approveObj) throws Exception {

		EmvTagRecordMasterTemp approvePara = (EmvTagRecordMasterTemp) approveObj;
		
		approvePara.setActiveCode(ProjConstants.Active_Code.ADD.getCode());
	}
	
	protected void beforePersistOnDelete(ProjPersistableArg arg, EmvProjPersistable paraObj,
			EmvProjPersistable approveObj) throws Exception {

		EmvTagRecordMaster para = (EmvTagRecordMaster) paraObj;
		EmvTagRecordMasterTemp approvePara = (EmvTagRecordMasterTemp) approveObj;
		
		para.setStatus(ProjConstants.Para_Status.INAPPROVE.getCode());
		approvePara.setActiveCode(ProjConstants.Active_Code.DELETE.getCode());
	}

	
	protected void beforePersistOnModify(ProjPersistableArg arg, EmvProjPersistable paraObj,
			EmvProjPersistable approveObj) throws Exception {
		
		EmvTagRecordMaster para = (EmvTagRecordMaster) paraObj;
		EmvTagRecordMasterTemp approvePara = (EmvTagRecordMasterTemp) approveObj;
		
		para.setStatus(ProjConstants.Para_Status.INAPPROVE.getCode());
		approvePara.setActiveCode(ProjConstants.Active_Code.UPDATE.getCode());
		approvePara.setOriSameValueFlag(para.getSameValueFlag());
	}
	
	@Override
	public EmvProjPersistable create(List<?> list) throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt.begin();
			EmvProjPersistable lastApproveObj = createEmptyCorrespondentPersistable();
			for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
				EmvProjPersistable approveObj = createEmptyCorrespondentPersistable();
				ProjPersistableArg tempArg = (ProjPersistableArg) iterator.next(); 
				StatefulPersistableUtil.copyPropertiesWithoutState(approveObj, tempArg);
				approveObj.setCreateInfo(AlterInfo.createAlterInfo());
				approveObj.setUpdateInfo(AlterInfo.createAlterInfo());
				approveObj.setRequestInfo(AlterInfo.createAlterInfo());
				beforePersistOnCreate(tempArg, approveObj);
				pm.persist(approveObj);
				afterPersistOnCreate(tempArg, approveObj);
				//addUiLog(tempArg);
				//afterCommitOnCreate(tempArg, approveObj);
				lastApproveObj = approveObj;
			}
			addUiLog((ProjPersistableArg) list.get(0));
			pt.commit();
			return lastApproveObj;
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
	public List query(ProjPersistableArg arg) throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		
		try {
			pt = pm.getTransaction();
			pt.begin();
			
			List list = getBaseCRUDQS().queryByExample(arg);
			//afterQueryList(arg, list);
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
	public EmvProjPersistable modify(ProjPersistableArg arg)
			throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		EmvTagRecordMasterArg emv = (EmvTagRecordMasterArg) arg;
		try {
			pt.begin();
			EmvProjPersistable paraObj = getBaseCRUDQS().queryById(arg.getId());
			if(paraObj == null){
				String oriSameValueFlag = emv.getNowSameValueFlag() != null ? emv.getNowSameValueFlag(): emv.getOriCardType();
				EmvTagRecordMaster.EmvTagRecordMasterId id = new EmvTagRecordMaster().new EmvTagRecordMasterId(emv.getEmvTag(), oriSameValueFlag);
				paraObj = getBaseCRUDQS().queryById(id);
			}
			EmvProjPersistable approveObj = createEmptyCorrespondentPersistable();
			StatefulPersistableUtil.copyPropertiesWithoutState(approveObj, arg);
			paraObj.setUpdateInfo(AlterInfo.createAlterInfo());
			paraObj.setRequestInfo(AlterInfo.createAlterInfo());
			approveObj.setCreateInfo(paraObj.getCreateInfo() != null 
					&& paraObj.getCreateInfo().getDate() != null 
					? paraObj.getCreateInfo(): AlterInfo.createAlterInfo());
			approveObj.setUpdateInfo(AlterInfo.createAlterInfo());
			approveObj.setRequestInfo(AlterInfo.createAlterInfo());
			beforePersistOnModify(arg, paraObj, approveObj);
			pm.persist(approveObj);
			pm.update(paraObj);
			afterPersistOnModify(arg, paraObj, approveObj);
			addUiLog(arg);
			pt.commit();
			afterCommitOnModify(arg, paraObj, approveObj);

			return approveObj;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				pt.rollback();
			} catch (Exception e1) {
				getLog().error(e1.getMessage(), e);
			}
			getLog().error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}
	}
	
	public void deleteEmv(ProjPersistableArg arg)
			throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();

		try {
			pt.begin();
			EmvProjPersistable paraObj = getBaseCRUDQS().queryById(arg.getId());
			EmvProjPersistable approveObj = createEmptyCorrespondentPersistable();
			StatefulPersistableUtil.copyPropertiesWithoutState(approveObj, paraObj);
			paraObj.setUpdateInfo(AlterInfo.createAlterInfo());
			approveObj.setCreateInfo(paraObj.getCreateInfo() != null 
					&& paraObj.getCreateInfo().getDate() != null 
					? paraObj.getCreateInfo(): AlterInfo.createAlterInfo());
			approveObj.setUpdateInfo(AlterInfo.createAlterInfo());
			approveObj.setRequestInfo(AlterInfo.createAlterInfo());

			beforePersistOnDelete(arg, paraObj, approveObj);
			pm.persist(approveObj);
			pm.update(paraObj);
			afterPersistOnDelete(arg, paraObj, approveObj);
			addUiLog(arg);
			pt.commit();
			afterCommitOnDelete(arg, paraObj, approveObj);
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
