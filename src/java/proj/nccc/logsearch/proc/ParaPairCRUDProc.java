package proj.nccc.logsearch.proc;



import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.vo.ApproveParaArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.PersistableManager;
import com.edstw.persist.PersistableTransaction;
import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.process.ProcessException;

public abstract class ParaPairCRUDProc extends AbstractBaseCRUDProc {

	public abstract EmvProjPersistable createEmptyCorrespondentPersistable();

	public abstract BaseCRUDQS getCorrespondentCRUDQS();

	/**
	 * 覆核作業_核准 主參數資料儲存前執行函式
	 * @param approveParaArg TODO
	 * @param paraObj
	 */
	protected void beforePersistOnApprove(ApproveParaArg approveParaArg, EmvProjPersistable paraObj)
			throws Exception {
	}
	
	/**
	 * 覆核作業_拒絕 主參數資料儲存前執行函式
	 * 
	 * @param paraObj
	 */
	protected void beforePersistOnReject(EmvProjPersistable paraObj)
			throws Exception {
	}

	/**
	 * 維護作業_修改 主參數與覆核參數資料儲存前執行函式
	 * 
	 * @param arg
	 * @param paraObj
	 * @param approveObj
	 */
	protected void beforePersistOnModify(ProjPersistableArg arg,
			EmvProjPersistable paraObj, EmvProjPersistable approveObj)
			throws Exception {
	}

	/**
	 * 維護作業_修改 主參數與覆核參數資料儲存後執行函式
	 * 
	 * @param arg
	 * @param paraObj
	 * @param approveObj
	 */
	protected void afterPersistOnModify(ProjPersistableArg arg,
			EmvProjPersistable paraObj, EmvProjPersistable approveObj)
			throws Exception {
	}

	/**
	 * 維護作業_修改 主參數與覆核參數資料commit後執行函式
	 * 
	 * @param arg
	 * @param paraObj
	 * @param approveObj
	 */
	protected void afterCommitOnModify(ProjPersistableArg arg,
			EmvProjPersistable paraObj, EmvProjPersistable approveObj)
			throws Exception {
	}
	
	/**
	 * 維護作業_刪除 主參數與覆核參數資料儲存前執行函式
	 * 
	 * @param arg
	 * @param paraObj
	 * @param approveObj
	 */
	protected void beforePersistOnDelete(ProjPersistableArg arg,
			EmvProjPersistable paraObj, EmvProjPersistable approveObj)
			throws Exception {
	}
	
	/**
	 * 維護作業_刪除 主參數與覆核參數資料儲存後執行函式
	 * 
	 * @param arg
	 * @param paraObj
	 * @param approveObj
	 */
	protected void afterPersistOnDelete(ProjPersistableArg arg,
			EmvProjPersistable paraObj, EmvProjPersistable approveObj)
			throws Exception {
	}

	/**
	 * 維護作業_刪除 主參數與覆核參數資料commit後執行函式
	 * 
	 * @param arg
	 * @param paraObj
	 * @param approveObj
	 */
	protected void afterCommitOnDelete(ProjPersistableArg arg,
			EmvProjPersistable paraObj, EmvProjPersistable approveObj)
			throws Exception {

	}

	public EmvProjPersistable create(ProjPersistableArg arg)
			throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt.begin();
			EmvProjPersistable approveObj = createEmptyCorrespondentPersistable();
			StatefulPersistableUtil.copyPropertiesWithoutState(approveObj, arg);
			approveObj.setCreateInfo(AlterInfo.createAlterInfo());
			approveObj.setUpdateInfo(AlterInfo.createAlterInfo());
			approveObj.setRequestInfo(AlterInfo.createAlterInfo());
			beforePersistOnCreate(arg, approveObj);
			pm.persist(approveObj);
			afterPersistOnCreate(arg, approveObj);
			addUiLog(arg);
			pt.commit();
			afterCommitOnCreate(arg, approveObj);

			return approveObj;
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

	public EmvProjPersistable modify(ProjPersistableArg arg)
			throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();

		try {
			pt.begin();
			EmvProjPersistable paraObj = getBaseCRUDQS().queryById(arg.getId());
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
	
	public void delete(ProjPersistableArg arg)
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

	public void approve(ProjPersistableArg arg) throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt.begin();

			EmvProjPersistable approveObj = getBaseCRUDQS().queryById(arg.getId());
			StatefulPersistableUtil.copyPropertiesWithoutState(arg, approveObj);
			ApproveParaArg apArg = (ApproveParaArg) arg;
			EmvProjPersistable paraObj = null;
			EmvProjPersistable paraObj_T = null;

			if (apArg.getActiveCode().equals(
					ProjConstants.Active_Code.ADD.getCode())) {
				paraObj = createEmptyCorrespondentPersistable();
				StatefulPersistableUtil.copyPropertiesWithoutState(paraObj,
						approveObj);
				if(arg.getCreateInfo() != null){
					paraObj.setCreateInfo(arg.getCreateInfo());
				}else{
					paraObj.setCreateInfo(arg.getRequestInfo());
				}
				
				//paraObj.setUpdateInfo(AlterInfo.createAlterInfo());

				beforePersistOnApprove(apArg, paraObj);
				pm.insert(paraObj);
			} else if (apArg.getActiveCode().equals(
					ProjConstants.Active_Code.UPDATE.getCode())) {
				paraObj_T = createEmptyCorrespondentPersistable();
				StatefulPersistableUtil.copyPropertiesWithoutState(paraObj_T, arg);
				paraObj = getCorrespondentCRUDQS().queryById(paraObj_T.getId());
				
				AlterInfo oriInfo = paraObj.getCreateInfo();
				
				StatefulPersistableUtil.copyPropertiesWithoutState(paraObj,approveObj);
				
				paraObj.setCreateInfo(oriInfo);
				paraObj.setUpdateInfo(arg.getRequestInfo());

				beforePersistOnApprove(apArg, paraObj);
				pm.update(paraObj);
			} else if (apArg.getActiveCode().equals(
					ProjConstants.Active_Code.DELETE.getCode())) {
				paraObj_T = createEmptyCorrespondentPersistable();
				StatefulPersistableUtil.copyPropertiesWithoutState(paraObj_T, arg);
				paraObj = getCorrespondentCRUDQS().queryById(paraObj_T.getId());
				pm.delete(paraObj);
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
				paraObj.setUpdateInfo(AlterInfo.createAlterInfo());
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
