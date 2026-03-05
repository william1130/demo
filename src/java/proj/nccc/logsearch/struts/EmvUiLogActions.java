package proj.nccc.logsearch.struts;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.user.UserLogger;
import com.edstw.util.ValidateUtil;
import com.edstw.web.WebConstant;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.proc.EmvUiLogQueryProcImpl;
import proj.nccc.logsearch.vo.EmvUiLogArg;

public class EmvUiLogActions extends BaseActions
{
	private static final long serialVersionUID = 1L;
	private EmvUiLogArg entity;

	public EmvUiLogActions() {
		this.setEntity(new EmvUiLogArg());		
	}
	
	private static final String RESULT_LIST_SESSION_NAME = EmvUiLogActions.class.toString() + ".EMV_UI_LOG_RESULT_LIST";

	public String queryUiLog()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		EmvUiLogArg arg = this.getEntity();
		LogMaster master = super.currentApLogManager().getLogMaster();
		genQueryInput(master);
		try
		{
			EmvUiLogQueryProcImpl proc = (EmvUiLogQueryProcImpl) ProjServices.getEmvUiLogQueryProc();
			int count = proc.queryCount(arg);
			if (count > 3000)
			{
				super.saveError("action.fail", new Object[] { "查詢結果超過3000筆(總共"+count+"筆)，請縮小查詢範圍。"});
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			arg.getPagingInfo().setEnablePaging(true);
			List<?> resultList = proc.getList(arg);
			this.setResultList(resultList);
			master.setFunctionCount(this.getResultList().size());
			request.getSession().setAttribute(RESULT_LIST_SESSION_NAME, resultList);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			return WebConstant.FAIL_KEY;
		}
	}
	
	private void genQueryInput(LogMaster master) {
		master.addQueryInput( "日期區間", String.format("%s-%s", entity.getDateFrom(), entity.getDateTo()) );
		master.addQueryInput( "使用者代號", entity.getUserId() != null && ValidateUtil.isNotBlank(entity.getUserId()) ? entity.getUserId() : "N/A" );
	}

	/**
	 * @return the entity
	 */
	public EmvUiLogArg getEntity()
	{
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(EmvUiLogArg entity)
	{
		this.entity = entity;
	}

}
