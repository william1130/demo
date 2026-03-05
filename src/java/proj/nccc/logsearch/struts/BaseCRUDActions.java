/*
 * BaseCRUDActions.java
 *
 * Created on 2007年8月20日, 下午 4:15
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.struts;

import java.util.Arrays;
import java.util.List;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.web.WebConstant;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.vo.PagingArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * Use for ATSLOG Admin CRUD and Query action process
 */
public abstract class BaseCRUDActions extends BaseActions
{

	private static final long serialVersionUID = 1L;
	private List resultList;
	private boolean queried;
	private Object[] ids;
	
	protected abstract BaseCRUDProc getBaseCRUDProc();

	public abstract EmvProjPersistable getEntity();

	public String toMaintain() {
		try {
			getBaseCRUDProc().toMaintain((ProjPersistableArg) getEntity());
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String queryList() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		try {
			// 若是具有分頁功能的arg時, 要處理分頁的參數.
			if ((ProjPersistableArg) getEntity() instanceof PagingArg) {
				super.processPagingInfo((PagingArg) (ProjPersistableArg) getEntity());
			}
			this.setResultList(getBaseCRUDProc().query(
					(ProjPersistableArg) getEntity()));
			master.setFunctionCount(this.getResultList().size());
			this.setQueried(true);
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			e.printStackTrace();
			master.setSuccessFlag("N");
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String toCreate() {
		try {
			getBaseCRUDProc().toCreate((ProjPersistableArg) getEntity());
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String create() {
		try {
			LogMaster master = super.currentApLogManager().getLogMaster();
			master.setAccessType(ProjConstants.ACCESS_TYPE_A);
			getBaseCRUDProc().create((ProjPersistableArg) getEntity());
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String queryProjPersistable() {
		try {
			LogMaster master = super.currentApLogManager().getLogMaster();
			master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
			ProjPersistableArg arg = (ProjPersistableArg) getEntity();
			EmvProjPersistable obj = getBaseCRUDProc().queryById(arg.getId());
			arg.buildFromProjPersistable(obj);
						
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String toModify() {
		try {
			getBaseCRUDProc().toModify((ProjPersistableArg) getEntity());
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String modify() {
		try {
			LogMaster master = super.currentApLogManager().getLogMaster();
			master.setAccessType(ProjConstants.ACCESS_TYPE_U);
			getBaseCRUDProc().modify((ProjPersistableArg) getEntity());
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String delete() {
		try {
			LogMaster master = super.currentApLogManager().getLogMaster();
			master.setAccessType(ProjConstants.ACCESS_TYPE_D);
			// 若有指定單一key, 就使用單一key來執行		
			if (this.getEntity().getId() != null)
				getBaseCRUDProc().delete(this.getEntity().getId());
			else if (this.getIds() != null)
				getBaseCRUDProc().delete(Arrays.asList(this.getIds()));
			else
				throw new IllegalArgumentException("未指定要刪除之id");
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}
	
	public List getResultList() {
		return resultList;
	}
	
	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
	
	public boolean isQueried() {
		return queried;
	}
	
	public void setQueried(boolean queried) {
		this.queried = queried;
	}
	
	public Object[] getIds() {
		return ids;
	}
	
	public void setIds(Object[] ids) {
		this.ids = ids;
	}

	public String approve() {
		try {

			LogMaster master = super.currentApLogManager().getLogMaster();
			master.setAccessType(ProjConstants.ACCESS_TYPE_C);
			getBaseCRUDProc().approve((ProjPersistableArg) getEntity());
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String reject() {
		try {
			LogMaster master = super.currentApLogManager().getLogMaster();
			master.setAccessType(ProjConstants.ACCESS_TYPE_C);
			getBaseCRUDProc().reject((ProjPersistableArg) getEntity());
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

}
