package proj.nccc.logsearch.struts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.user.UserLogger;
import com.edstw.util.ValidateUtil;
import com.edstw.web.WebConstant;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.parse.ISOUtil;
import proj.nccc.logsearch.proc.LDSSTransLogProcImpl;
import proj.nccc.logsearch.qs.LDSSRawLogQSImpl;
import proj.nccc.logsearch.vo.LDSSTransLogArg;

/**
 * 
 * @author Paco Tsai
 * @version 1.0
 */
public class LDSSTransLogActions extends BaseActions
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7856442104399481539L;
	private LDSSTransLogArg entity;
	private String errorMsg;

	private static final String RESULT_LIST_SESSION_NAME = LDSSTransLogActions.class.toString() + ".LDSS_TRANS_LOG_RESULT_LIST";

	/**
	 * @return the entity
	 */
	public LDSSTransLogArg getEntity()
	{
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(LDSSTransLogArg entity)
	{
		this.entity = entity;
	}
	
	public LDSSTransLogActions()
	{
		this.setEntity(new LDSSTransLogArg());
	}
	private static final Logger log = LogManager.getLogger(LDSSTransLogActions.class);
	
	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public String queryTransLog()
	{
		HttpServletRequest request=ServletActionContext.getRequest();
		LogMaster master = super.currentApLogManager().getLogMaster();
		genQueryInput(master);
		LDSSTransLogArg arg = (LDSSTransLogArg) this.getEntity();
		try
		{
			if (this.getEntity().getTermId().startsWith("${auth}"))
			{
				super.saveError("action.fail", new Object[] { "權限字串:" + request.getHeader("nccc_ap004") });
				this.setErrorMsg("權限字串:" + request.getHeader("nccc_ap004"));
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}

			LDSSTransLogProcImpl proc = (LDSSTransLogProcImpl) ProjServices.getLDSSTransLogProc();
			int count = proc.queryCount(arg);
			arg.setDataTotalCount(count);
			log.info("查詢結果"+count+"筆");
			if (count > 3000)
			{
				super.saveError("action.fail", new Object[] { "查詢結果超過3000筆(總共"+count+"筆)，請縮小查詢範圍。"});
				this.setErrorMsg("查詢結果超過3000筆(總共"+count+"筆)，請縮小查詢範圍。");
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}

			List<?> resultList = proc.getList(arg);
			this.setResultList(resultList);
			master.setFunctionCount(resultList.size());
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

	

	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public String queryTransLogISO()
	{
		LDSSTransLogArg arg = this.getEntity();
		LDSSTransLogArg argTmp = new LDSSTransLogArg();
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try
		{
			LDSSTransLogProcImpl proc = (LDSSTransLogProcImpl) ProjServices.getLDSSTransLogProc();
			argTmp.setTranDateFrom(null);
			argTmp.setTranDateTo(null);
			argTmp.setSeqId(arg.getSeqId());
			List<LDSSTransLogArg> resultList = proc.getList(argTmp);
			
			if (resultList == null || resultList.size() < 1)
			{
				super.saveError("action.fail", new Object[] { "查無資料!" });
				this.setErrorMsg("查無資料");
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			LDSSTransLogArg entity = resultList.get(0);
			
			LDSSRawLogQSImpl qs_raw = (LDSSRawLogQSImpl) ProjServices.getLDSSRawLogQS();
			// M2020020 ESC/DCC
			entity.setRawList(qs_raw.queryBySeqId(arg.getSeqId()));
			
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


	private void genQueryInput(LogMaster master) {
		ParamBean param = ParamBean.getInstance(); 
		master.addQueryInput( "交易日期起迄", String.format("%s-%s", entity.getTranDateFrom(), entity.getTranDateTo()) );
		String timeFrom = "";
		String timeTo = "";
		if(entity.getTranTimeFrom() != null && ValidateUtil.isNotBlank(entity.getTranTimeFrom()) ) {
			timeFrom = entity.getTranTimeFrom();
		}
		if(entity.getTranTimeTo() != null && ValidateUtil.isNotBlank(entity.getTranTimeTo()) ) {
			timeTo = entity.getTranTimeTo();
		}
		if( (timeFrom.length() + timeTo.length() ) == 0)  {
			master.addQueryInput( "起訖時間", "N/A" );
		}else {
			master.addQueryInput( "起訖時間", String.format("%s-%s", entity.getTranTimeFrom(), entity.getTranTimeTo()) );
		}
		String mCardNum = "N/A";
		if(entity.getCardNum() != null  && ValidateUtil.isNotBlank(entity.getCardNum())) {
			mCardNum = ISOUtil.getCardNumMask(entity.getCardNum());
		} 
		master.addQueryInput( "卡號", mCardNum );
		master.addQueryInput( "LDSS主機", entity.getHostName() != null  && "all".equalsIgnoreCase(entity.getHostName()) ? "全部" : entity.getHostName() );
		master.addQueryInput( "特店代號", entity.getMerchantId() != null && ValidateUtil.isNotBlank(entity.getMerchantId()) ? entity.getMerchantId() : "N/A" );
		master.addQueryInput( "交易主機", entity.getHostAccord() != null && "ALL".equals(entity.getHostAccord()) ? "全部" : entity.getHostAccord() );
		master.addQueryInput( "授權碼", entity.getApproveCode() != null && ValidateUtil.isNotBlank(entity.getApproveCode()) ? entity.getApproveCode() : "N/A" );
		master.addQueryInput( "端末機代號", entity.getTermId() != null && ValidateUtil.isNotBlank(entity.getTermId()) ? entity.getTermId() : "N/A" );
		master.addQueryInput( "批次號碼", entity.getBatchNum() != null && ValidateUtil.isNotBlank(entity.getBatchNum()) ? entity.getBatchNum() : "N/A" );
		master.addQueryInput( "MTI", entity.getMti() != null && ValidateUtil.isNotBlank(entity.getMti()) ? entity.getMti() : "N/A" );
		master.addQueryInput( "交易別", entity.getTranType() != null && "all".equalsIgnoreCase(entity.getTranType()) ? "全部" : param.getLdssTransTypeNameMap().get(entity.getTranType()) );
		String amtFrom = "";
		String amtTo = "";
		if(entity.getTranAmountFrom() != null && ValidateUtil.isNotBlank(entity.getTranAmountFrom().getString()) ) {
			amtFrom = entity.getTranAmountFrom().getString();
		}
		if(entity.getTranAmountTo() != null && ValidateUtil.isNotBlank(entity.getTranAmountTo().getString()) ) {
			amtTo = entity.getTranAmountTo().getString();
		}
		if( (amtFrom.length() + amtTo.length() ) == 0)  {
			master.addQueryInput( "金額起迄", "N/A" );
		}else {
			master.addQueryInput( "金額起迄", String.format("%s-%s", amtFrom, amtTo ) );
		}
		master.addQueryInput( "排序選擇", entity.getSortFields() != null ? param.getSortMap().get(entity.getSortFields()) : "N/A" );
	}
	
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
