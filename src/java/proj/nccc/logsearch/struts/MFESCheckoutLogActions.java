package proj.nccc.logsearch.struts;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.user.UserLogger;
import com.edstw.util.ValidateUtil;
import com.edstw.web.WebConstant;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.proc.MFESCheckoutLogProcImpl;
import proj.nccc.logsearch.qs.AtsRejectCodeSetupQSImpl;
import proj.nccc.logsearch.qs.MFESCheckoutLogQSImpl;
import proj.nccc.logsearch.qs.MerchantChinQSImpl;
import proj.nccc.logsearch.vo.MFESCheckoutLogArg;

/**
 * 
 * @author Paco Tsai
 * @version 1.0
 */
public class MFESCheckoutLogActions extends BaseActions
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7856442104399481539L;
	private MFESCheckoutLogArg entity;
	private String errorMsg;

	private static final String RESULT_LIST_SESSION_NAME = LDSSTransLogActions.class.toString() + ".LDSS_TRANS_LOG_RESULT_LIST";

	/**
	 * @return the entity
	 */
	public MFESCheckoutLogArg getEntity()
	{
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(MFESCheckoutLogArg entity)
	{
		this.entity = entity;
	}
	
	public MFESCheckoutLogActions()
	{
		this.setEntity(new MFESCheckoutLogArg());
	}
	
	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public String queryMFESCheckout()
	{
		HttpServletRequest request=ServletActionContext.getRequest();
		LogMaster master = super.currentApLogManager().getLogMaster();
		
		MFESCheckoutLogArg arg = (MFESCheckoutLogArg) this.getEntity();
		try
		{
			MFESCheckoutLogProcImpl proc = (MFESCheckoutLogProcImpl) ProjServices.getMFESCheckoutLogProc();
			int count = proc.queryCount(arg);
			master.addQueryInput("請款代碼", arg.getApplyCode() != null && ValidateUtil.isNotBlank(entity.getApplyCode()) ?  arg.getApplyCode() : "N/A");
			if (count > 3000)
			{
				super.saveError("action.fail", new Object[] { "查詢結果超過3000筆，請縮小查詢範圍。"});
				this.setErrorMsg("查詢結果超過3000筆，請縮小查詢範圍。");
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			if (count == 0)
			{
				super.saveError("action.fail", new Object[] { "查無資料或非MFES特店的請款代碼"});
				this.setErrorMsg("查無資料或非MFES特店的請款代碼");
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}

			List<?> resultList = proc.getList(arg);
			this.setResultList(resultList);
			master.setFunctionCount(this.getResultList().size());
			request.getSession().setAttribute(RESULT_LIST_SESSION_NAME, resultList);
			this.setEntity(entity);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			this.setErrorMsg("error");
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

}
