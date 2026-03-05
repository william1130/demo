package proj.nccc.logsearch.struts;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.report.Report;
import com.edstw.user.UserLogger;
import com.edstw.util.ValidateUtil;
import com.edstw.web.WebConstant;
import com.edstw.web.util.ResponseUtil;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.proc.ChesgDailyTotalsProcImpl;
import proj.nccc.logsearch.vo.ChesgDailyTotalsArg;

public class ReportActions extends BaseActions {

	private static final long serialVersionUID = -7856442104399481539L;
	private static final Logger log = LogManager.getLogger(ReportActions.class);

	private ChesgDailyTotalsArg entity;
	private String errorMsg;
	private int queryMax = 3000;

	public ChesgDailyTotalsArg getEntity() {
		return entity;
	}

	public void setEntity(ChesgDailyTotalsArg entity) {
		this.entity = entity;
	}

	public ReportActions() {
		this.setEntity(new ChesgDailyTotalsArg());
	}

	private static final String RESULT_LIST_SESSION_NAME = ReportActions.class.toString()
			+ ".ATS_LOG_REPORT_RESULT_LIST";

	/**
	 * M2025047_R114117_數位化簽帳單查詢
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public String queryChesg() throws IllegalAccessException, InvocationTargetException {
		HttpServletRequest request = ServletActionContext.getRequest();
		ChesgDailyTotalsArg arg = (ChesgDailyTotalsArg) this.getEntity();

		LogMaster master = super.currentApLogManager().getLogMaster();
		genChesgQueryInput(master);
		try {
			ChesgDailyTotalsProcImpl proc = (ChesgDailyTotalsProcImpl) ProjServices.getChesgDailyTotalsProc();
			int count = proc.queryChesgCount(arg);
			arg.setDataTotalCount(count);
			log.info("查詢結果" + count + "筆");
			if (count > queryMax) {
				super.saveError("action.fail", new Object[] { "查詢結果超過" + queryMax + "筆(總共" + count + "筆)，請縮小查詢範圍。" });
				this.setErrorMsg("查詢結果超過" + queryMax + "筆(總共" + count + "筆)，請縮小查詢範圍。");
				master.setSuccessFlag("N");
				return WebConstant.FAIL_KEY;
			}

			List<?> resultList = proc.getChesgList(arg);
			this.setResultList(resultList);
			master.setFunctionCount(this.getResultList().size());
			request.getSession().setAttribute(RESULT_LIST_SESSION_NAME, resultList);
			this.setEntity(entity);
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			log.error(e.getMessage());
			super.saveError("action.fail", new Object[] { e.getMessage() });
			this.setErrorMsg("error");
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	/**
	 * M2025047_R114117_數位化簽帳單查詢 產製Excel報表
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public String queryChesgRpt() throws IllegalAccessException, InvocationTargetException {
		LogMaster master = super.currentApLogManager().getLogMaster();
		ChesgDailyTotalsArg arg = (ChesgDailyTotalsArg) this.getEntity();
		genChesgQueryInput(master);
		master.setAccessType(ProjConstants.ACCESS_TYPE_PR);
		master.setFunctionCount(1);
		try {
			ChesgDailyTotalsProcImpl proc = (ChesgDailyTotalsProcImpl) ProjServices.getChesgDailyTotalsProc();
			arg.getPagingInfo().setEnablePaging(false);
			int count = proc.queryChesgCount(arg);
//			arg.setDataTotalCount(count);
			log.info("查詢結果" + count + "筆");

			List<?> resultList = proc.getChesgList(arg);
			String queryRange = String.format("%s-%s", entity.getTranDateFrom(), entity.getTranDateTo());
			Report report = proc.genChesgReport(resultList, queryRange);
			ResponseUtil.sendReport(ServletActionContext.getRequest(), ServletActionContext.getResponse(), report);
			return null;
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			log.error(e.getMessage());
			super.saveError("action.fail", new Object[] { e.getMessage() });
			this.setErrorMsg("error");
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	private void genChesgQueryInput(LogMaster master) {
		master.addQueryInput("交易日期起迄", String.format("%s-%s", entity.getTranDateFrom(), entity.getTranDateTo()));
		master.addQueryInput("特店代號",
				entity.getMerchantId() != null && ValidateUtil.isNotBlank(entity.getMerchantId())
						? entity.getMerchantId()
						: "N/A");
		master.addQueryInput("端末機代號",
				entity.getTermId() != null && ValidateUtil.isNotBlank(entity.getTermId()) ? entity.getTermId() : "N/A");
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
