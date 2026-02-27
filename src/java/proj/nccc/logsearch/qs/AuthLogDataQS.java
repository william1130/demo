
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: AuthLogDataQS.java,v 1.1 2017/04/24 01:31:15 asiapacific\jih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.edstw.lang.LongString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

import proj.nccc.logsearch.persist.AuthLogData;
import proj.nccc.logsearch.vo.AuthLogDataArg;

/**
 *
 * @author APAC\czrm4t
 * @version $Revision: 1.1 $
 */
public interface AuthLogDataQS extends BaseCRUDQS {
	public static final JdbcPersistableBuilder LogDataBuilder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			AuthLogData obj = new AuthLogData();
			ResultSetTool rst = new ResultSetTool(rs);

			obj.setRowId(rst.getString("X"));
			obj.setBankNo(rst.getString("BANK_NO"));
			obj.setCardNo(rst.getString("CARD_NO"));
			obj.setBinNo(rst.getString("BIN_NO"));
			obj.setMajorCardId(rst.getString("MAJOR_CARD_ID"));
			obj.setApprovalNo(rst.getString("APPROVAL_NO"));
			obj.setMerchantNo(rst.getString("MERCHANT_NO"));
			obj.setTerminalNo(rst.getString("TERMINAL_NO"));
			obj.setPurchaseDate(rst.getString("PURCHASE_DATE"));
			obj.setPurchaseTime(rst.getString("PURCHASE_TIME"));
			obj.setPurchaseAmt(rst.getLongString("PURCHASE_AMT"));
			obj.setProcessDate(rst.getString("PROCESS_DATE"));
			obj.setProcessTime(rst.getString("PROCESS_TIME"));
			obj.setExpireDate(rst.getString("EXPIRE_DATE"));
			obj.setTransType(rst.getString("TRANS_TYPE"));
			obj.setConditionCode(rst.getString("CONDITION_CODE"));
			obj.setIcFlag(rst.getString("IC_FLAG"));
			obj.setAuthorizeReason(rst.getString("AUTHORIZE_REASON"));
			obj.setTermTraceNo(rst.getString("TERM_TRACE_NO"));
			obj.setAcquireId(rst.getString("ACQUIRE_ID"));
			obj.setAcquireBk(rst.getString("ACQUIRE_BK"));
			obj.setCancelApprovalNo(rst.getString("CANCEL_APPROVAL_NO"));
			obj.setCountryName(rst.getString("COUNTRY_NAME"));
			obj.setMccCode(rst.getString("MCC_CODE"));
			obj.setEntryMode(rst.getString("ENTRY_MODE"));
			obj.setTwoScreenApTx(rst.getString("TWO_SCREEN_AP_TX"));
			obj.setReturnTandemCode(rst.getString("RETURN_TANDEM_CODE"));
			obj.setReturnIsoCode(rst.getString("RETURN_ISO_CODE"));
			obj.setReturnHostCode(rst.getString("RETURN_HOST_CODE"));
			obj.setOriginator(rst.getString("ORIGINATOR"));
			obj.setResponder(rst.getString("RESPONDER"));
			obj.setMessageType(rst.getString("MESSAGE_TYPE"));
			obj.setEdcTxType(rst.getString("EDC_TX_TYPE"));
			obj.setCvv2CheckResult(rst.getString("CVV2_CHECK_RESULT"));
			obj.setMpasFlag(rst.getString("MPAS_FLAG"));
			obj.setMpasMember(rst.getString("MPAS_MEMBER"));
			obj.setInstallmentFlag(rst.getString("INSTALLMENT_FLAG"));
			obj.setInstIndicator(rst.getString("INST_INDICATOR"));
			obj.setLoyaltyFlag(rst.getString("LOYALTY_FLAG"));
			obj.setLyltIndicator(rst.getString("LYLT_INDICATOR"));
			obj.setLoyaltyOriginalPoint(rst.getDoubleString("LOYALTY_ORIGINAL_POINT"));
			obj.setLoyaltyRedemptionPoint(rst.getDoubleString("LOYALTY_REDEMPTION_POINT"));
			obj.setLoyaltyRedemptionAmt(rst.getDoubleString("LOYALTY_REDEMPTION_AMT"));
			obj.setLoyaltyPaidCreditAmt(rst.getDoubleString("LOYALTY_PAID_CREDIT_AMT"));
			obj.setInstallPeriodNum(rst.getDoubleString("INSTALL_PERIOD_NUM"));
			obj.setInstallFirstPayment(rst.getDoubleString("INSTALL_FIRST_PAYMENT"));
			obj.setInstallStagesPayment(rst.getDoubleString("INSTALL_STAGES_PAYMENT"));
			obj.setInstallFormalityFee(rst.getDoubleString("INSTALL_FORMALITY_FEE"));
			obj.setMccAmtLimit(rst.getDoubleString("MCC_AMT_LIMIT"));
			obj.setCardClass(rst.getString("CARD_CLASS"));
			obj.setLimitType(rst.getString("LIMIT_TYPE"));
			obj.setNormalLimitAmtMonth(rst.getDoubleString("NORMAL_LIMIT_AMT_MONTH"));
			obj.setNormalAmtLastMonth(rst.getDoubleString("NORMAL_AMT_LAST_MONTH"));
			obj.setNormalAmtMonth(rst.getDoubleString("NORMAL_AMT_MONTH"));
			obj.setCashLimitAmtMonth(rst.getDoubleString("CASH_LIMIT_AMT_MONTH"));
			obj.setCashBillAmt(rst.getDoubleString("CASH_BILL_AMT"));
			obj.setCashAmtLastMonth(rst.getDoubleString("CASH_AMT_LAST_MONTH"));
			obj.setCashAmtMonth(rst.getDoubleString("CASH_AMT_MONTH"));
			obj.setTotalLimitAmtMonth(rst.getDoubleString("TOTAL_LIMIT_AMT_MONTH"));
			obj.setAmtCurrentMonth(rst.getDoubleString("AMT_CURRENT_MONTH"));
			obj.setAmtLastMonth(rst.getDoubleString("AMT_LAST_MONTH"));
			obj.setIdBillAmt(rst.getDoubleString("ID_BILL_AMT"));
			obj.setMaxLimitAmtMonth(rst.getDoubleString("MAX_LIMIT_AMT_MONTH"));
			obj.setCashAmtCurrentMonth(rst.getDoubleString("CASH_AMT_CURRENT_MONTH"));
			obj.setIdCashAmtLastMonth(rst.getDoubleString("ID_CASH_AMT_LAST_MONTH"));
			obj.setIdCashBillAmt(rst.getDoubleString("ID_CASH_BILL_AMT"));
			obj.setIdCreditLimit(rst.getDoubleString("ID_CREDIT_LIMIT"));
			obj.setDataSource(rst.getString("DATA_SOURCE"));
			obj.setFileDate(rst.getDateString("FILE_DATE"));
			obj.setModProgramId(rst.getString("MOD_PROGRAM_ID"));
			obj.setUpdateId(rst.getString("UPDATE_ID"));
			obj.setRemark(rst.getString("REMARK"));
			obj.setBankNo2(rst.getString("BANK_NO_2"));
			obj.setBusinessType(rst.getString("BUSINESS_TYPE"));
			obj.setCardholderId(rst.getString("CARDHOLDER_ID"));

			obj.setCardType(rst.getString("CARD_TYPE"));
			obj.setCardKind(rst.getString("CARD_KIND"));
			obj.setManualEntryResult(rst.getString("MANUAL_ENTRY_RESULT"));
			obj.setManualForcePostFlag(rst.getString("MANUAL_FORCE_POST_FLAG"));
			obj.setPreAuthAmt(rst.getDoubleString("PRE_AUTH_AMT"));
			// M98085國內臨時額度-增加臨時餘額欄位
			obj.setTempCreditLimit(rst.getDoubleString("TEMP_CREDIT_LIMIT"));
			// M2013085-國際組織跨國交易收取surcharge amount
			obj.setSurchgAmt(rst.getDoubleString("SURCHG_AMT"));
			obj.setDeptId(rst.getString("DEPT_ID"));
			return obj;
		}
	};

	public List<AuthLogData> queryAll() throws QueryServiceException;

	public String getTotalRecord(AuthLogDataArg obj) throws QueryServiceException;

	public String getQsCriteria();

	public boolean isExist(String cardNo, String merchantNo, String approveNo, String purchaseDate,
			LongString purchaseAmt) throws QueryServiceException;

	public List<AuthLogData> queryByExampleByBank(AuthLogDataArg obj) throws QueryServiceException;

	public String getTotalRecordByBank(AuthLogDataArg obj) throws QueryServiceException;
}
