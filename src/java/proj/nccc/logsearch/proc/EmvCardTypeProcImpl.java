package proj.nccc.logsearch.proc;

import java.util.Map;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvCardType;
import proj.nccc.logsearch.persist.EmvCardTypeTemp;
import proj.nccc.logsearch.persist.EmvProjPersistable;
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
public class EmvCardTypeProcImpl extends ParaPairCRUDProc implements EmvCardTypeProc {

	public EmvCardTypeProcImpl() {
	}

	public BaseCRUDQS getBaseCRUDQS() {
		return ProjServices.getEmvCardTypeQS();
	}

	public BaseCRUDQS getCorrespondentCRUDQS() {
		return ProjServices.getEmvCardTypeTempQS();
	}

	public EmvProjPersistable createEmptyProjPersistable() {
		return new EmvCardType();
	}

	public EmvProjPersistable createEmptyCorrespondentPersistable() {
		return new EmvCardTypeTemp();
	}
	
	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
			throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
	}

	public String getServiceName() {
		return "EmvCardType Process";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	protected void beforePersistOnCreate(ProjPersistableArg arg,
			EmvProjPersistable approveObj) throws Exception {

		EmvCardTypeTemp approvePara = (EmvCardTypeTemp) approveObj;
		
		/*approvePara.setRequestUser(ProjUserProfile.getCurrentUserProfile().getUserInfo()
				.getSignature());
		approvePara.setRequestDate(approvePara.getUpdateInfo().getDate());*/
		approvePara.setActiveCode(ProjConstants.Active_Code.ADD.getCode());
	}
	
	protected void beforePersistOnDelete(ProjPersistableArg arg, EmvProjPersistable paraObj,
			EmvProjPersistable approveObj) throws Exception {

		EmvCardType para = (EmvCardType) paraObj;
		EmvCardTypeTemp approvePara = (EmvCardTypeTemp) approveObj;
		
		para.setStatus(ProjConstants.Para_Status.INAPPROVE.getCode());
		/*approvePara.setRequestUser(ProjUserProfile.getCurrentUserProfile().getUserInfo()
				.getSignature());
		approvePara.setRequestDate(approvePara.getUpdateInfo().getDate());*/
		approvePara.setActiveCode(ProjConstants.Active_Code.DELETE.getCode());
	}

	
	protected void beforePersistOnModify(ProjPersistableArg arg, EmvProjPersistable paraObj,
			EmvProjPersistable approveObj) throws Exception {
		
		EmvCardType para = (EmvCardType) paraObj;
		EmvCardTypeTemp approvePara = (EmvCardTypeTemp) approveObj;
		
		/*approvePara.setRequestUser(ProjUserProfile.getCurrentUserProfile().getUserInfo()
				.getSignature());*/
		para.setStatus(ProjConstants.Para_Status.INAPPROVE.getCode());
		//approvePara.setRequestDate(approvePara.getUpdateInfo().getDate());
		approvePara.setActiveCode(ProjConstants.Active_Code.UPDATE.getCode());
	}	

	public void deleteCard(ProjPersistableArg arg)
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
