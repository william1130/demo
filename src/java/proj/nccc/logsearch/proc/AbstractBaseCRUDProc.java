/*
 * AbstractBaseCRUDProc.java
 *
 * Created on 2007年8月20日, 下午 2:05
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.proc;

import com.edstw.persist.PersistableManager;
import com.edstw.persist.PersistableTransaction;
import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.process.AbstractProcess;
import com.edstw.process.ProcessException;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvUiLog;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.user.ProjUserInfo;
import proj.nccc.logsearch.user.ProjUserProfile;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author 
 * @version 
 */
public abstract class AbstractBaseCRUDProc extends AbstractProcess implements
		BaseCRUDProc {
	/**
	 * Creates a new instance of AbstractBaseCRUDProc
	 */
	public AbstractBaseCRUDProc() {
	}

	/**
	 * 實作此介面, 傳回本proc對應之query service物件.
	 */
	public abstract BaseCRUDQS getBaseCRUDQS();

	/**
	 * 實作此介面, 傳回本proc對應之空的ems persistable物件.
	 */
	public abstract EmvProjPersistable createEmptyProjPersistable();	

	/**
	 * 此為一call back介面, 執行新增作業時, 在persistable物件實際執行寫入前, 本函式會被呼叫. 若有需要,
	 * 可覆寫本函式以完成新增之額外動作.
	 */
	protected void beforePersistOnCreate(ProjPersistableArg arg,
			EmvProjPersistable obj) throws Exception {
		// do nothing
	}

	/**
	 * 此為一call back介面, 執行新增作業時, 在persistable物件實際執行寫入後, commit之前, 本函式會被呼叫. 若有需要,
	 * 可覆寫本函式以完成新增之額外動作.
	 */
	protected void afterPersistOnCreate(ProjPersistableArg arg,
			EmvProjPersistable obj) throws Exception {
		// do nothing
	}

	/**
	 * 此為一call back介面, 執行刪除作業時, 在persistable物件實際執行刪除前, 本函式會被呼叫. 若有需要,
	 * 可覆寫本函式以完成刪除之額外動作.
	 */
	protected void beforePersistOnDelete(EmvProjPersistable obj) throws Exception {
		// do nothing
	}

	/**
	 * 此為一call back介面, 執行刪除作業時, 在persistable物件實際執行刪除後, commit之前 本函式會被呼叫. 若有需要,
	 * 可覆寫本函式以完成刪除之額外動作.
	 */
	protected void afterPersistOnDelete(EmvProjPersistable obj) throws Exception {
		// do nothing
	}

	/**
	 * 此為一call back介面, 執行修改作業時, 在persistable物件實際執行修改後, commit之前 本函式會被呼叫. 若有需要,
	 * 可覆寫本函式以完成修改之額外動作.
	 */
	protected void afterPersistOnModify(ProjPersistableArg arg,
			EmvProjPersistable obj) throws Exception {
		// do nothing
	}

	/**
	 * 此為一call back介面, 執行修改作業時, 在commit之後 本函式會被呼叫. 若有需要, 可覆寫本函式以完成修改之額外動作.
	 */
	protected void afterCommitOnModify(ProjPersistableArg arg,
			EmvProjPersistable obj) throws Exception {
		// do nothing
	}

	/**
	 * 此為一call back介面, 執行新增作業時, 在commit之後 本函式會被呼叫. 若有需要, 可覆寫本函式以完成新增之額外動作.
	 */
	protected void afterCommitOnCreate(ProjPersistableArg arg,
			EmvProjPersistable obj) throws Exception {
		// do nothing
	}

	/**
	 * 此為一call back介面, 執行刪除作業時, 在commit之後 本函式會被呼叫. 若有需要, 可覆寫本函式以完成刪除之額外動作.
	 */
	protected void afterCommitOnDelete(EmvProjPersistable obj) throws Exception {
		// do nothing
	}

	/**
	 * 由於不同persistable物件允許修改之欄位不一定相同, 所以必須實作本介面, 將允許修改之欄位由arg複製至obj中.
	 */
	protected abstract void mergeOnModify(ProjPersistableArg arg,
			EmvProjPersistable entity) throws Exception;

	/**
	 * 此為一call back介面, 當使用id查詢後, 本函式會被呼叫, 將查詢得到的persistable物件傳入,
	 * 其中會使用id查詢之作業包含queryDetail, toModify, modify及delete.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	protected void afterQueryPersistable(EmvProjPersistable obj) throws Exception {

	}
	
	/**
	 * call back介面, 當使用queryList完成後, 本函式會被呼叫, 將查詢得到的list物件傳入,
	 * @param obj
	 * @param list
	 * @throws Exception
	 */
	protected void afterQueryList(ProjPersistableArg arg, List list) throws Exception {
		
	}

	/**
	 * 預設實作, 沒有做任何動作
	 * 
	 * @param arg
	 *            ProjPersistableArg
	 * @throws com.edstw.process.ProcessException
	 */
	public void toMaintain(ProjPersistableArg arg) throws ProcessException {
		// do nothing
	}

	/**
	 * 預設實作, 沒有做任何動作
	 * 
	 * @param arg
	 *            ProjPersistableArg
	 * @throws com.edstw.process.ProcessException
	 */
	public void toCreate(ProjPersistableArg arg) throws ProcessException {
		// do nothing
	}

	public EmvProjPersistable create(ProjPersistableArg arg)
			throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt.begin();
			EmvProjPersistable obj = createEmptyProjPersistable();
			StatefulPersistableUtil.copyPropertiesWithoutState(obj, arg);
			obj.setCreateInfo(AlterInfo.createAlterInfo());
			obj.setUpdateInfo(AlterInfo.createAlterInfo());
			beforePersistOnCreate(arg, obj);
			pm.persist(obj);
			afterPersistOnCreate(arg, obj);
			pt.commit();
			afterCommitOnCreate(arg, obj);
			return obj;
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

	public void toModify(ProjPersistableArg arg) throws ProcessException {
		try {
			EmvProjPersistable obj = getBaseCRUDQS().queryById(arg.getId());
			this.afterQueryPersistable(obj);
			arg.buildFromProjPersistable(obj);
		} catch (Exception e) {
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
			EmvProjPersistable obj = getBaseCRUDQS().queryById(arg.getId());
			this.afterQueryPersistable(obj);
			// 先將createInfo取出備份, 避免於mergeOnModify被修改
			AlterInfo createInfo = obj.getCreateInfo();
			mergeOnModify(arg, obj);
			// 還原createInfo.
			obj.setCreateInfo(createInfo);
			obj.setUpdateInfo(AlterInfo.createAlterInfo());
			pm.persist(obj);
			afterPersistOnModify(arg, obj);
			pt.commit();
			afterCommitOnModify(arg, obj);
			return obj;
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

	public void delete(Object id) throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt.begin();
			EmvProjPersistable obj = getBaseCRUDQS().queryById(id);
			this.afterQueryPersistable(obj);
			beforePersistOnDelete(obj);
			obj.delete();
			obj.setUpdateInfo(AlterInfo.createAlterInfo());
			pm.persist(obj);
			afterPersistOnDelete(obj);
			pt.commit();
			afterCommitOnDelete(obj);
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

	public void delete(List ids) throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt.begin();
			List objList = getBaseCRUDQS().queryByIds(ids);
			for (Iterator iter = objList.iterator(); iter.hasNext();) {
				EmvProjPersistable obj = (EmvProjPersistable) iter.next();
				this.afterQueryPersistable(obj);
				beforePersistOnDelete(obj);
				obj.delete();
				obj.setUpdateInfo(AlterInfo.createAlterInfo());
				pm.persist(obj);
				afterPersistOnDelete(obj);
			}
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

	public EmvProjPersistable queryById(Object id) 
			throws ProcessException {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		
		try {
			pt = pm.getTransaction();
			pt.begin();
			
			EmvProjPersistable obj = getBaseCRUDQS().queryById(id);
			if (obj == null)
				throw new ProcessException("查不到指定之Persistable物件.");
			this.afterQueryPersistable(obj);
			//addUiLog(arg);
			
			pt.commit();
			return obj;
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
	
	public void approve(ProjPersistableArg arg) 
			throws ProcessException {
		// do Nothing
	}
	
	public void reject(ProjPersistableArg arg) 
			throws ProcessException {
		// do Nothing
	}
	
	protected String getTxnServerForUiLog(ProjPersistableArg arg) {
		String txnServer = "";
		
		try {
			Method method = arg.getClass().getMethod("getTxnServer", new Class[]{});
			Object o = method.invoke(arg, new Object[]{} );
			
			if (o != null)	txnServer = (String) o;
		} catch (Exception e) {
			
		} 
		
		return txnServer;
	}
	
	/**
	 * 記錄操作歷程
	 * @param arg
	 * @param action 動作名稱
	 */
	
	protected void addUiLog(Object id) {
//		ProjPersistableArg arg = (ProjPersistableArg) getBaseCRUDQS().queryById(id);
//			if(arg == null){
//				arg = (ProjPersistableArg) id;
//			}
			ProjPersistableArg arg = (ProjPersistableArg) id;
			if (arg.getUiLogAction() != null && !arg.getUiLogAction().isEmpty()) {
			StringBuffer sb = new StringBuffer()
				.append(ProjConstants.UiLog_Function.valueOf(arg.getUiLogFunctionName()).getName())
				.append(ProjConstants.UiLog_Action.valueOf(arg.getUiLogAction()).getName());			

			PersistableManager pm = super.currentPersistableManager();
			
			AlterInfo createInfo = AlterInfo.createAlterInfo();
			EmvUiLog emvUiLog = new EmvUiLog();
			
			emvUiLog.setUiDate(createInfo.getDate());
			ProjUserInfo ui = (ProjUserInfo) ProjUserProfile.getCurrentUserProfile().getUserInfo();
			emvUiLog.setUserId(ui.getUserID());
			emvUiLog.setUiFunction(sb.toString());
			emvUiLog.setUiOther(arg.getUiLogOther());
			
			pm.persist(emvUiLog);
		}		
	}
	

}
