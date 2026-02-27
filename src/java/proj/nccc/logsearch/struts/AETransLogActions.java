package proj.nccc.logsearch.struts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.report.Report;
import com.edstw.user.UserLogger;
import com.edstw.util.ValidateUtil;
import com.edstw.web.WebConstant;
import com.edstw.web.util.ResponseUtil;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.parse.CardNumLogicCheckBean;
import proj.nccc.logsearch.parse.ChipUtil;
import proj.nccc.logsearch.parse.ISOUtil;
import proj.nccc.logsearch.proc.AtslogTransLogProcImpl;
import proj.nccc.logsearch.qs.AtsCpcMerchantQSImpl;
import proj.nccc.logsearch.qs.AtsRejectCodeSetupQSImpl;
import proj.nccc.logsearch.qs.AtslogTransLogQSImpl;
import proj.nccc.logsearch.qs.MerchantChinQSImpl;
import proj.nccc.logsearch.vo.AtslogTransLogArg;

public class AETransLogActions extends BaseActions
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String RESULT_LIST_SESSION_NAME = AETransLogActions.class.toString() + ".ATS_LOG_TRANS_LOG_RESULT_LIST";
	private AtslogTransLogArg entity;
	private String errorMsg;
	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public String queryAETransLog()
	{
		HttpServletRequest request=ServletActionContext.getRequest();
		AtslogTransLogArg arg = this.getEntity();
		LogMaster master = super.currentApLogManager().getLogMaster();
		genQueryInput(master);
		try
		{
			if (this.getEntity().getTermId().startsWith("${auth}"))
			{
				super.saveError("action.fail", new Object[] { "權限字串:" + request.getHeader("nccc_ap004") });
				this.setErrorMsg("權限字串:" + request.getHeader("nccc_ap004"));
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			if ("Y".equals(arg.getCheckCardNum()))
			{
				CardNumLogicCheckBean bean = new CardNumLogicCheckBean();
				String cardNum = arg.getCardNum();
				if (!bean.checkCreditCard(cardNum))
				{
					super.saveError("action.fail", new Object[] { "卡號檢核失敗。" });
					this.setErrorMsg("卡號檢核失敗。");
					master.setSuccessFlag("N");
					return WebConstant.FAIL_KEY;
				}
			}
			AtslogTransLogProcImpl proc = (AtslogTransLogProcImpl) ProjServices.getAtslogTransLogProc();
			int count = proc.queryAECount(arg);
			arg.setDataTotalCount(count);
			if (count > 3000)
			{
				super.saveError("action.fail", new Object[] { "查詢結果超過3000筆(總共"+count+"筆)，請縮小查詢範圍。"});
				this.setErrorMsg("查詢結果超過3000筆(總共"+count+"筆)，請縮小查詢範圍。");
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			arg.getPagingInfo().setEnablePaging(true);
			List<?> resultList = proc.getAEList(arg);
			this.setResultList(resultList);
			master.setFunctionCount(this.getResultList().size());
			request.getSession().setAttribute(RESULT_LIST_SESSION_NAME, resultList);
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
	public String queryAETransLogRptAll()
	{
		AtslogTransLogArg arg = this.getEntity();
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_PR);
		genQueryInput(master);
		try
		{
			if ("Y".equals(arg.getCheckCardNum()))
			{
				CardNumLogicCheckBean bean = new CardNumLogicCheckBean();
				String cardNum = arg.getCardNum();
				if (!bean.checkCreditCard(cardNum))
				{
					super.saveError("action.fail", new Object[] { "卡號檢核失敗。" });
					this.setErrorMsg("卡號檢核失敗。");
					master.setSuccessFlag("N");
					return WebConstant.FAIL_KEY;
				}
			}
			arg.setMaskCardNo("Y");  //20180126 報表卡號都要遮掩
			AtslogTransLogProcImpl proc = (AtslogTransLogProcImpl) ProjServices.getAtslogTransLogProc();
			arg.getPagingInfo().setEnablePaging(false);
			List<?> resultList = proc.getAEList(arg);
			if (resultList.size() <= 0)
			{
				super.saveError("action.fail", new Object[] { "無符合資料." });
				this.setErrorMsg("無符合資料。");
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			master.setFunctionCount(1);
			Report report = null;
			// M2020050 新增產出TXT報表
			if ("TXT".equals(arg.getExFileType())) {
				report = proc.genTxtReport(resultList);
			} else {
				report = proc.genAEReport(resultList);
			}
			ResponseUtil.sendReport(ServletActionContext.getRequest(), ServletActionContext.getResponse(), report);
			return null;
		}
		catch (Exception e)
		{
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
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
	public String queryAETransLogRpt()
	{
		AtslogTransLogArg arg = this.getEntity();
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_PR);
		try
		{
			String[] seqIds = super.getParameters("seqIds");
//			String maskCardNo = super.getParameter("maskCardNo");
			String maskCardNo = "Y"; //20180126 報表卡號都要遮掩
			if (seqIds == null || seqIds.length <= 0)
			{
				super.saveError("action.fail", new Object[] { "未選取資料!" });
				this.setErrorMsg("無符合資料。");
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			master.setFunctionCount(1);
			AtslogTransLogProcImpl proc = (AtslogTransLogProcImpl) ProjServices.getAtslogTransLogProc();
			Report report = proc.genReportById(seqIds, maskCardNo, arg.getExFileType());
			ResponseUtil.sendReport(ServletActionContext.getRequest(), ServletActionContext.getResponse(), report);
			return null;
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
	public String queryAETransLogISO()
	{
		AtslogTransLogArg arg = this.getEntity();
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try
		{
			AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
			AtslogTransLogArg entity = qs.queryById(arg.getSeqId());
			if (entity == null)
			{
				super.saveError("action.fail", new Object[] { "查無資料!" });
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			MerchantChinQSImpl mcQs = (MerchantChinQSImpl) ProjServices.getMerchantChinQS();
			AtsRejectCodeSetupQSImpl arcsQs = (AtsRejectCodeSetupQSImpl) ProjServices.getAtsRejectCodeSetupQS();
			String merchantId = arg.getMerchantId();
			String approveCode = arg.getApproveCode();
			if (merchantId != null)
				arg.setMerchantChinName(mcQs.queryNameString(merchantId));
			if (approveCode != null)
				arg.setApproveCodeName(arcsQs.queryNameString(approveCode));
			AtslogTransLogProcImpl proc = (AtslogTransLogProcImpl) ProjServices.getAtslogTransLogProc();
			proc.setArg(entity, arg.getMaskCardNo(), arg.getMaskCardNo(), arg.getMerchantChinName(), arg.getApproveCodeName(),"");
			this.setEntity(entity);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
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
	public String queryAETransLogIC()
	{
		AtslogTransLogArg arg = this.getEntity();
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try
		{
			AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
			AtslogTransLogArg entity = qs.queryById(arg.getSeqId());
			if (entity == null)
			{
				super.saveError("action.fail", new Object[] { "查無資料!" });
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			if (StringUtils.isNotBlank(ChipUtil.getInstance().getMeaning("9F35", entity.getTag9f35TermType())))
			{
				entity.setTag9f35TermType(String.format("%s (%s)", entity.getTag9f35TermType(), ChipUtil.getInstance().getMeaning("9F35", entity.getTag9f35TermType())));
			}
			if (StringUtils.isNotBlank(ChipUtil.getInstance().getMeaning("9C", entity.getTag9cTranType())))
			{
				entity.setTag9cTranType(String.format("%s (%s)", entity.getTag9cTranType(), ChipUtil.getInstance().getMeaning("9C", entity.getTag9cTranType())));
			}
			if (StringUtils.isNotBlank(ChipUtil.getInstance().getMeaning("DFEF", entity.getTagDfefRsnOnlineCode())))
			{
				entity.setTagDfefRsnOnlineCode(String.format("%s (%s)", entity.getTagDfefRsnOnlineCode(), ChipUtil.getInstance().getMeaning("DFEF", entity.getTagDfefRsnOnlineCode())));
			}
			if (entity.getTagDfedChipCondCode() != null && entity.getTagDfedChipCondCode().length() > 0)
			{
				String dfedValue = entity.getTagDfedChipCondCode().substring(entity.getTagDfedChipCondCode().length() - 1, entity.getTagDfedChipCondCode().length());
				if (StringUtils.isNotBlank(ChipUtil.getInstance().getMeaning("DFED", dfedValue)))
				{
					entity.setTagDfedChipCondCode(String.format("%s (%s)", entity.getTagDfedChipCondCode(), ChipUtil.getInstance().getMeaning("DFED", dfedValue)));
				}
			}
			MerchantChinQSImpl mcQs = (MerchantChinQSImpl) ProjServices.getMerchantChinQS();
			AtsRejectCodeSetupQSImpl arcsQs = (AtsRejectCodeSetupQSImpl) ProjServices.getAtsRejectCodeSetupQS();
			String merchantId = arg.getMerchantId();
			String approveCode = arg.getApproveCode();
			if (merchantId != null)
				arg.setMerchantChinName(mcQs.queryNameString(merchantId));
			if (approveCode != null)
				arg.setApproveCodeName(arcsQs.queryNameString(approveCode));
			AtslogTransLogProcImpl proc = (AtslogTransLogProcImpl) ProjServices.getAtslogTransLogProc();
			proc.setArg(entity, arg.getMaskCardNo(), arg.getMaskCardNo(), arg.getMerchantChinName(), arg.getApproveCodeName(),"");
			this.setEntity(entity);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
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
	public String transformCPCNo()
	{
		LogMaster master = super.currentApLogManager().getLogMaster();
		try
		{
			AtslogTransLogArg arg = this.getEntity();
			AtslogTransLogArg entity = new AtslogTransLogArg();
			
			String ctcbMid = arg.getCtcbMid() != null && ValidateUtil.isNotBlank(arg.getCtcbMid()) ? arg.getCtcbMid() : "";
			String ctcbTid = arg.getCtcbTid() != null && ValidateUtil.isNotBlank(arg.getCtcbTid()) ? arg.getCtcbTid() : "";
			master.addQueryInput( "商店代號", ctcbMid.equals("") ? "N/A" : ctcbMid );
			master.addQueryInput( "端末機代號", ctcbTid.equals("") ? "N/A" : ctcbTid );
			if (ctcbMid.equals("") && ctcbTid.equals(""))
			{
				super.saveError("action.fail", new Object[] { "查詢條件錯誤" });
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}
			AtsCpcMerchantQSImpl mcQs = (AtsCpcMerchantQSImpl) ProjServices.getAtsCpcMerchantQS();
			entity.setCtcbMid(ctcbMid);
			int funcCount = 0;
			if (!ctcbMid.equals(""))
			{
				String ncccMid = mcQs.queryNcccMidMap(ctcbMid);
				entity.setNcccMid(ncccMid != null && !ncccMid.equals("") ? ncccMid : "查無資料");
				if(ncccMid != null && !ncccMid.equals("")) {
					funcCount++;
				}
			}
			entity.setCtcbTid(ctcbTid);
			if (!ctcbTid.equals(""))
			{
				String ncccTid = mcQs.queryNcccTidMap(ctcbTid);
				entity.setNcccTid(ncccTid != null && !ncccTid.equals("") ? ncccTid : "查無資料");
				if(ncccTid != null && !ncccTid.equals("")) {
					funcCount++;
				}
			}
			master.setFunctionCount(funcCount);
			this.setEntity(entity);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}
	
	
	private void genQueryInput(LogMaster master) {
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
		//master.addQueryInput( "卡號", entity.getCardNum() != null  && ValidateUtil.isNotBlank(entity.getCardNum()) ? entity.getCardNum() : "N/A" );
		String mCardNum = "N/A";
		if(entity.getCardNum() != null  && ValidateUtil.isNotBlank(entity.getCardNum())) {
			mCardNum = ISOUtil.getCardNumMask(entity.getCardNum());
		} 
		master.addQueryInput( "卡號", mCardNum );
		master.addQueryInput( "檢核卡號", entity.getCheckCardNum() != null && "Y".equalsIgnoreCase(entity.getCheckCardNum()) ? "是" : "否" );
		master.addQueryInput( "特店代號", entity.getMerchantId() != null && ValidateUtil.isNotBlank(entity.getMerchantId()) ? entity.getMerchantId() : "N/A" );
		master.addQueryInput( "端末機代號", entity.getTermId() != null && ValidateUtil.isNotBlank(entity.getTermId()) ? entity.getTermId() : "N/A" );
		master.addQueryInput( "批次號碼", entity.getBatchNum() != null && ValidateUtil.isNotBlank(entity.getBatchNum()) ? entity.getBatchNum() : "N/A" );
		master.addQueryInput( "授權碼", entity.getApproveCode() != null && ValidateUtil.isNotBlank(entity.getApproveCode()) ? entity.getApproveCode() : "N/A" );
		master.addQueryInput( "EDC MTI", entity.getEdcMti() != null && ValidateUtil.isNotBlank(entity.getEdcMti()) ? entity.getEdcMti() : "N/A" );
		master.addQueryInput( "B24 MTI", entity.getB24Mti() != null && ValidateUtil.isNotBlank(entity.getB24Mti()) ? entity.getB24Mti() : "N/A" );
		master.addQueryInput( "顯示全部MTI", entity.isDefaultMti() ? "是" : "否" );
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
		master.addQueryInput( "交易編號", entity.getTransNo() != null && ValidateUtil.isNotBlank(entity.getTransNo()) ? entity.getTransNo() : "N/A" );
		master.addQueryInput( "排序選擇", entity.getSortFields() != null ? ParamBean.getInstance().getSortMap().get(entity.getSortFields()) : "N/A" );
	}
	

	public AtslogTransLogArg getEntity() {
		return entity;
	}

	public void setEntity(AtslogTransLogArg entity) {
		this.entity = entity;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
