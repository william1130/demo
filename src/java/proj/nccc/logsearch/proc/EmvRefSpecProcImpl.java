package proj.nccc.logsearch.proc;

import java.util.Map;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvRefSpec;
import proj.nccc.logsearch.persist.EmvRefSpecTemp;
import proj.nccc.logsearch.qs.BaseCRUDQS;
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
public class EmvRefSpecProcImpl extends ParaPairCRUDProc implements EmvRefSpecProc {

	public EmvRefSpecProcImpl() {
	}

	public BaseCRUDQS getBaseCRUDQS() {
		return ProjServices.getEmvRefSpecQS();
	}

	public BaseCRUDQS getCorrespondentCRUDQS() {
		return ProjServices.getEmvRefSpecTempQS();
	}

	public EmvProjPersistable createEmptyProjPersistable() {
		return new EmvRefSpec();
	}

	public EmvProjPersistable createEmptyCorrespondentPersistable() {
		return new EmvRefSpecTemp();
	}
	
	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
			throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
	}

	public String getServiceName() {
		return "EmvRefSpec Process";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	protected void beforePersistOnCreate(ProjPersistableArg arg,
			EmvProjPersistable approveObj) throws Exception {

		EmvRefSpecTemp approvePara = (EmvRefSpecTemp) approveObj;
		approvePara.setActiveCode(ProjConstants.Active_Code.ADD.getCode());
	}
	
	protected void beforePersistOnDelete(ProjPersistableArg arg, EmvProjPersistable paraObj,
			EmvProjPersistable approveObj) throws Exception {

		EmvRefSpec para = (EmvRefSpec) paraObj;
		EmvRefSpecTemp approvePara = (EmvRefSpecTemp) approveObj;
		
		para.setStatus(ProjConstants.Para_Status.INAPPROVE.getCode());
		approvePara.setActiveCode(ProjConstants.Active_Code.DELETE.getCode());
	}

	
	protected void beforePersistOnModify(ProjPersistableArg arg, EmvProjPersistable paraObj,
			EmvProjPersistable approveObj) throws Exception {
		
		EmvRefSpec para = (EmvRefSpec) paraObj;
		EmvRefSpecTemp approvePara = (EmvRefSpecTemp) approveObj;

		para.setStatus(ProjConstants.Para_Status.INAPPROVE.getCode());
		approvePara.setActiveCode(ProjConstants.Active_Code.UPDATE.getCode());
	}
	
	public void deleteRefSpec(ProjPersistableArg arg)
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
