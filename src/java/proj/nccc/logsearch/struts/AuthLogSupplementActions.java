package proj.nccc.logsearch.struts;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.report.Report;
import com.edstw.user.UserLogger;
import com.edstw.util.ValidateUtil;
import com.edstw.web.WebConstant;
import com.edstw.web.util.ResponseUtil;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.parse.ISOUtil;
import proj.nccc.logsearch.proc.AuthLogReportProc;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.qs.AuthLogDataQS;
import proj.nccc.logsearch.qs.AuthLogDataQSImpl;
import proj.nccc.logsearch.qs.AuthRuleQSImpl.AuthRuleException;
import proj.nccc.logsearch.user.ProjUserInfo;
import proj.nccc.logsearch.user.ProjUserProfile;
import proj.nccc.logsearch.vo.AuthLogDataArg;

/**
 * 
 * @author Stephen Lin
 * @version $Revision: 1.5 $
 */
public class AuthLogSupplementActions extends BaseCRUDActions {
	private static final long serialVersionUID = 1L;
	private AuthLogDataArg entity;
	private static final Logger log = LogManager.getLogger(AuthLogSupplementActions.class);

	public AuthLogSupplementActions() {

		this.setEntity(new AuthLogDataArg());
	}

	public AuthLogDataArg getEntity() {
		return entity;
	}

	public void setEntity(AuthLogDataArg entity) {
		this.entity = entity;
	}

	// *****
	// 信用卡授權資料查詢QL1
	// (queryInput=true)
	public String queryAuthLogListByCredit() {

		try {
			AuthLogDataArg entity = (AuthLogDataArg) this.getEntity();
			// 若是具有分頁功能的arg時, 要處理分頁的參數.
			// 人工授權資料查詢(APLog的query_input)
			LogMaster master = super.currentApLogManager().getLogMaster();
			if (ValidateUtil.isNotBlank(entity.getCardNo()))
				master.addQueryInput("卡號", ISOUtil.getCardNumMask(entity.getCardNo()));
			if (ValidateUtil.isNotBlank(entity.getBolSuppCardNoError()))
				master.addQueryInput("卡號錯誤強制查詢", entity.getBolSuppCardNoError());
			if (ValidateUtil.isNotBlank(entity.getApprovalNo()))
				master.addQueryInput("授權碼", entity.getApprovalNo());
			if (ValidateUtil.isNotBlank(entity.getPurchaseDate1()))
				master.addQueryInput("消費日期", entity.getPurchaseDate1());
			if (ValidateUtil.isNotBlank(entity.getPurchaseDate2()))
				master.addQueryInput("消費日期", entity.getPurchaseDate2());
			
			super.processPagingInfo(entity);
			// 20081222 不檢核卡號長度,才能讀出AE及銀聯卡資料
			// if(entity.getCardNo()==null ||
			// entity.getCardNo().trim().length()< 16)
			// {
			// super.saveError( request ,"msg","msg.warning", new Object[]
			// {"卡號錯誤!!" } );
			// return mapping.findForward( WebConstant.FAIL_KEY );
			// }
			ProjUserInfo ui = null;
			ProjUserProfile up = (ProjUserProfile) ProjUserProfile.getCurrentUserProfile();
			if (up != null)
				ui = (ProjUserInfo) up.getUserInfo();
			if (up != null && ui != null && ui.getUserID() != null) {
				// String sBankNo=(String)
				// request.getSession().getAttribute("sessionBANKID");
				String sBankNo = "";
				if (ui.isBankUser()) {
					sBankNo = ui.getBankNo();
				}
				try {
					Integer.parseInt(sBankNo);
					if (!ProjServices.getBinBankParmQS().isOnUsCard(sBankNo, entity.getCardNo())) {
						super.saveMessage("msg.warning", new Object[] { "非所屬發卡行卡號!!" });
						return WebConstant.FAIL_KEY;
					}
					entity.setBankNo(sBankNo);
				} catch (Exception xxx) {
					entity.setBankNo(null);
				}
				super.setRequestAttribute("ACTION", "BANK");

				// 卡號錯誤強制強詢
				if (!"Y".equals(entity.getBolSuppCardNoError())) {
					if (entity.getCardNo().trim().length() <= 6)
						throw new Exception("卡號錯誤!!");
					if (!("62".equals(entity.getCardNo().substring(0, 2)))
							&& !("81".equals(entity.getCardNo().substring(0, 2)))) { // M2017128_銀聯國際81卡號開頭與8碼BIN
						try {
							ProjServices.getAuthRuleQS().isCorrectCardNumber(entity.getCardNo());
						} catch (AuthRuleException e) {
							super.saveError("msg.warning", new Object[] { e.getMessage() });
							return WebConstant.FAIL_KEY;
						}
					}
				}
				AuthLogDataQS qs = ProjServices.getAuthLogDataQS();
				List<?> list = qs.queryByExample(entity);
				// insert MaintenanceLog
//				LogIdentityBean liBean = new LogIdentityBean();
//				liBean.insertMaintenanceLog(null, "UCA-AUTH-W00130", "I", "424000", null, this.getClass()
//						.getSimpleName());
				// //////////////////////
				if (list == null || list.size() <= 0) {
					super.saveMessage("msg.warning", new Object[] { "無符合條件之資料 !!" });
					return WebConstant.FAIL_KEY;
				} else {
					this.setResultList(list);
					return WebConstant.SUCCESS_KEY;
				}
			} else {
				super.saveError("action.fail", new Object[] { "session timeout !!" });
				return WebConstant.FAIL_KEY;
			}
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			return WebConstant.FAIL_KEY;
		}
	}

	// *****
	// 信用卡授權交易資料查詢--報表
	public String queryAuthLogAuthTransReport() {

		Report report = null;
		try {
			AuthLogDataArg entity = (AuthLogDataArg) this.getEntity();
			// 人工授權資料查詢(APLog的query_input)
			LogMaster master = super.currentApLogManager().getLogMaster();
			if (ValidateUtil.isNotBlank(entity.getCardNo()))
				master.addQueryInput("卡號", ISOUtil.getCardNumMask(entity.getCardNo()));
			if (ValidateUtil.isNotBlank(entity.getBolSuppCardNoError()))
				master.addQueryInput("卡號錯誤強制查詢", entity.getBolSuppCardNoError());
			if (ValidateUtil.isNotBlank(entity.getApprovalNo()))
				master.addQueryInput("授權碼", entity.getApprovalNo());
			if (ValidateUtil.isNotBlank(entity.getPurchaseDate1()))
				master.addQueryInput("消費日期", entity.getPurchaseDate1());
			if (ValidateUtil.isNotBlank(entity.getPurchaseDate2()))
				master.addQueryInput("消費日期", entity.getPurchaseDate2());
			if (ValidateUtil.isNotBlank(entity.getReportType()))
				master.addQueryInput("產生報表的格式", entity.getReportType());
			
			ProjUserInfo ui = null;
			ProjUserProfile up = (ProjUserProfile) ProjUserProfile.getCurrentUserProfile();
			if (up != null)
				ui = (ProjUserInfo) up.getUserInfo();

			if (up == null || ui == null || ui.getUserID() == null) {
				super.saveError("action.fail", new Object[] { "Session timeout !!" });
				return WebConstant.FAIL_KEY;
			} else {
				String sBankNo = "";
				if (ui.isBankUser()) {
					sBankNo = ui.getBankNo();
				}
				if (entity.getCardNo() == null || !ValidateUtil.isNumber(entity.getCardNo(), false)) {
					super.saveError("action.fail", new Object[] { "卡號錯誤 !!" });
					return WebConstant.FAIL_KEY;
				}
				if ((ValidateUtil.isNotBlank(entity.getProcessDate1())
						&& !ValidateUtil.isNumber(entity.getProcessDate1(), false))
						|| (ValidateUtil.isNotBlank(entity.getProcessDate2())
								&& !ValidateUtil.isNumber(entity.getProcessDate2(), false))) {
					super.saveError("action.fail", new Object[] { "日期格式錯誤 !!" });
					return WebConstant.FAIL_KEY;
				} else {
					try {
						Integer.parseInt(sBankNo);
						if (!ProjServices.getBinBankParmQS().isOnUsCard(sBankNo, entity.getCardNo())) {
							super.saveMessage("msg.warning", new Object[] { "非所屬發卡行卡號!!" });
							return WebConstant.FAIL_KEY;
						}
						entity.setBankNo(sBankNo);
					} catch (Exception xxx) {
						entity.setBankNo(null);
					}
					// entity.setBankNo(sBankNo);
					super.setRequestAttribute("ACTION", "BANK");
					// 卡號錯誤強制強詢
					if (entity.getBolSuppCardNoError() == null || !entity.getBolSuppCardNoError().equals("Y")) {
						try {
							ProjServices.getAuthRuleQS().isCorrectCardNumber(entity.getCardNo());
						} catch (AuthRuleException e) {
							super.saveError("msg.warning", new Object[] { e.getMessage() });
						}
					}
					// 若是具有分頁功能的arg時, 要處理分頁的參數.
					super.processPagingInfo(entity);
					AuthLogDataQS qs = ProjServices.getAuthLogDataQS();
					String sTotalRecord = qs.getTotalRecord(entity);
					int iMax = 5000;
//					try {
//						iMax = Integer.parseInt(SysDefaultBean.getInstance().getMaxRecordOfQuery());
//					}
//					catch (Exception n) {
//						log.info("ERROR : get Max Record per page from System Default : " + n);
//					}
					if (Integer.parseInt(sTotalRecord) <= 0) {
						super.saveError("action.fail", new Object[] { "查無資料 !!" });
						return WebConstant.FAIL_KEY;
					} else if (Integer.parseInt(sTotalRecord) <= iMax) {
						AuthLogReportProc analysis = ProjServices.getAuthLogReportProc();
						report = analysis.authLogAuthTransReport((AuthLogDataArg) this.getEntity());
						if (report == null) {
							super.saveError("action.fail", new Object[] { "查無資料 !!" });
							return WebConstant.FAIL_KEY;
						}
						ResponseUtil.sendReport(ServletActionContext.getRequest(), ServletActionContext.getResponse(),
								report);
						return null;
					} else {
						super.setRequestAttribute("MAXRECORD", "" + iMax + "(" + sTotalRecord + ")");
						super.saveMessage("msg.warning", new Object[] {
								"所查詢之資料筆數(共 " + sTotalRecord + "筆)超過限制(" + iMax + "),請確認查詢條件後再重新查詢!!  " });
						return WebConstant.SUCCESS_KEY;
					}
				}
			}
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			return WebConstant.FAIL_KEY;
		} finally {
			report = null;
			log.info("[AuthTran Report]" + new Date() + ": Finally queryAuthLogAuthTransReport ..");
		}
	}

	// ********
	// 授權室授權交易查詢
	public String authorizerLogQuery() {

		try {
			AuthLogDataArg entity = (AuthLogDataArg) this.getEntity();
			ProjUserInfo ui = null;
			ProjUserProfile up = (ProjUserProfile) ProjUserProfile.getCurrentUserProfile();
			if (up != null)
				ui = (ProjUserInfo) up.getUserInfo();

			String sBankNo = "";
			if (ui.isBankUser()) {
				sBankNo = ui.getBankNo();
			}
			if (ServletActionContext.getRequest().getSession().getAttribute("CARDNO") == null) {
				super.saveError("action.fail", new Object[] { "session timeout !!" });
				return WebConstant.FAIL_KEY;
			} else {
				try {
					// 20090107 pf1 授權資料查詢第二頁無法閱讀的問題
					// Integer.parseInt(sBankNo);
					// super.saveError( request ,"error","action.fail", new
					// Object[] {"銀行："+sBankNo+"不可執行此一功能 !!"} );
					// return mapping.findForward( WebConstant.FAIL_KEY );
				} catch (Exception xxx) {
				}
				entity.setCardNo((String) ServletActionContext.getRequest().getSession().getAttribute("CARDNO"));
				super.setRequestAttribute("ACTION", sBankNo);
				// 若是具有分頁功能的arg時, 要處理分頁的參數.
				super.processPagingInfo(entity);
				ServletActionContext.getRequest().getSession().setAttribute("MustView", entity.getCardNo());
				AuthLogDataQSImpl qs = (AuthLogDataQSImpl) ProjServices.getAuthLogDataQS();
				List<?> list = qs.queryByExample(entity);
				this.setResultList(list);
				return WebConstant.SUCCESS_KEY;
			}
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			return WebConstant.FAIL_KEY;
		}
	}

	@Override
	protected BaseCRUDProc getBaseCRUDProc() {
		return ProjServices.getAuthLogDataProc();
	}

}
