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
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.vo.EmvTagRecordDetailArg;
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
public class EmvTagRecordDetailProcImpl extends ParaPairCRUDProc implements EmvTagRecordDetailProc {

	public EmvTagRecordDetailProcImpl() {
	}

	public BaseCRUDQS getBaseCRUDQS() {
		return ProjServices.getEmvTagRecordDetailQS();
	}
	

	public BaseCRUDQS getCorrespondentCRUDQS() {
		return ProjServices.getEmvTagRecordDetailTempQS();
	}

	
	public EmvProjPersistable createEmptyProjPersistable() {
		return new EmvTagRecordDetail();
	}
	
	public EmvProjPersistable createEmptyCorrespondentPersistable() {
		return new EmvTagRecordDetailTemp();
	}
	
	
	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
			throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
	}

	public String getServiceName() {
		return "EmvTagRecordDetail Process";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	/*protected void beforePersistOnCreate(ProjPersistableArg arg,
			EmvProjPersistable approveObj) throws Exception {

		EmvTagRecordDetailTemp approvePara = (EmvTagRecordDetailTemp) approveObj;
		
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
	}
	*/
	@Override
	public EmvProjPersistable create(List<?> list) throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt.begin();
			EmvProjPersistable lastApproveObj = createEmptyCorrespondentPersistable();
			for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
				EmvProjPersistable approveObj = createEmptyCorrespondentPersistable();
				EmvTagRecordDetailArg tempArg = (EmvTagRecordDetailArg) iterator.next(); 
				StatefulPersistableUtil.copyPropertiesWithoutState(approveObj, tempArg);
				pm.persist(approveObj);
				lastApproveObj = approveObj;
			}
			//addUiLog((ProjPersistableArg) list.get(0));
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

}
