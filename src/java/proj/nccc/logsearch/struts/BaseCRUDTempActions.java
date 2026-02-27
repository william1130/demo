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

import com.edstw.web.WebConstant;

import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.vo.PagingArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author 許欽程(Vincent Shiu)
 * @version $Revision$
 */
public abstract class BaseCRUDTempActions extends BaseActions
{

	private static final long serialVersionUID = 1L;
	/** Creates a new instance of BaseCRUDActions */
	private List resultList;
	public abstract EmvProjPersistable getEntity();
	private Object[] ids;
	public BaseCRUDTempActions()
	{
	}

	/**
	 * 實作本介面, 傳回此action對應之crud的process物件.
	 */
	protected abstract BaseCRUDProc getBaseCRUDProc();

	/**
	 * 實作本介面, 傳回此action對應之crud的空的EmpPersistableArg
	 */
	protected abstract ProjPersistableArg createEmptyProjPersistableArg();

	public String toMaintain()
	{
		try
		{
//			BaseCRUDForm form = (BaseCRUDForm) actionForm;
			getBaseCRUDProc().toMaintain((ProjPersistableArg) getEntity());
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String queryList()
	{
		try
		{
//			BaseCRUDForm form = (BaseCRUDForm) actionForm;
			ProjPersistableArg entity = (ProjPersistableArg) getEntity();
			if (entity instanceof PagingArg)
			{
				super.processPagingInfo((PagingArg) entity);
			}
			this.setResultList(getBaseCRUDProc().query(entity));
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String toCreate()
	{
		try
		{
//			BaseCRUDForm form = (BaseCRUDForm) actionForm;
			getBaseCRUDProc().toCreate((ProjPersistableArg) getEntity());
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String create()
	{
		try
		{
//			BaseCRUDForm form = (BaseCRUDForm) actionForm;
			getBaseCRUDProc().create((ProjPersistableArg) getEntity());
			this.saveCreatedMessage();
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String queryProjPersistable()
	{
		try
		{
//			BaseCRUDForm form = (BaseCRUDForm) actionForm;
			ProjPersistableArg arg = (ProjPersistableArg) getEntity();
			EmvProjPersistable obj = getBaseCRUDProc().queryById(arg.getId());
			arg.buildFromProjPersistable(obj);
			
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String toModify()
	{
		try
		{
//			BaseCRUDForm form = (BaseCRUDForm) actionForm;
			getBaseCRUDProc().toModify((ProjPersistableArg) this.getEntity());
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String modify()
	{
		try
		{
//			BaseCRUDForm form = (BaseCRUDForm) actionForm;
			getBaseCRUDProc().modify((ProjPersistableArg) this.getEntity());
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	public String delete()
	{
		try
		{
//			BaseCRUDForm form = (BaseCRUDForm) actionForm;
			if (this.getEntity().getId() != null)
				getBaseCRUDProc().delete((ProjPersistableArg) this.getEntity());
			else
				if (this.getIds() != null) getBaseCRUDProc().delete(Arrays.asList(this.getIds())); else throw new IllegalArgumentException("未指定要刪除之id");
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	protected void saveCreatedMessage()
	{
		super.saveMessage("msg.created", null);
	}

	
	public List getResultList() {
		return resultList;
	}
	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
	public Object[] getIds() {
		return ids;
	}

	public void setIds(Object[] ids) {
		this.ids = ids;
	}
}
