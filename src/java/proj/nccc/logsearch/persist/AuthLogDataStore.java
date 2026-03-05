/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id:
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.2 $
 */
public class AuthLogDataStore extends ProjStore {

	private static List<FieldInfo> fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("BANK_NO", false));
		fieldInfos.add(new FieldInfo("CARD_NO", false));
		fieldInfos.add(new FieldInfo("BIN_NO", false));
		fieldInfos.add(new FieldInfo("MAJOR_CARD_ID", false));
		fieldInfos.add(new FieldInfo("APPROVAL_NO", false));
		fieldInfos.add(new FieldInfo("MERCHANT_NO", false));
		fieldInfos.add(new FieldInfo("TERMINAL_NO", false));
		fieldInfos.add(new FieldInfo("PURCHASE_DATE", false));
		fieldInfos.add(new FieldInfo("PURCHASE_TIME", false));
		fieldInfos.add(new FieldInfo("PURCHASE_AMT", false));
		fieldInfos.add(new FieldInfo("PROCESS_DATE", false));
		fieldInfos.add(new FieldInfo("PROCESS_TIME", false));
		fieldInfos.add(new FieldInfo("EXPIRE_DATE", false));
		fieldInfos.add(new FieldInfo("TRANS_TYPE", false));
		fieldInfos.add(new FieldInfo("CONDITION_CODE", false));
		fieldInfos.add(new FieldInfo("IC_FLAG", false));
		fieldInfos.add(new FieldInfo("AUTHORIZE_REASON", false));
		fieldInfos.add(new FieldInfo("TERM_TRACE_NO", false));
		fieldInfos.add(new FieldInfo("ACQUIRE_ID", false));
		fieldInfos.add(new FieldInfo("ACQUIRE_BK", false));
		fieldInfos.add(new FieldInfo("CANCEL_APPROVAL_NO", false));
		fieldInfos.add(new FieldInfo("COUNTRY_NAME", false));
		fieldInfos.add(new FieldInfo("MCC_CODE", false));
		fieldInfos.add(new FieldInfo("ENTRY_MODE", false));
		fieldInfos.add(new FieldInfo("TWO_SCREEN_AP_TX", false));
		fieldInfos.add(new FieldInfo("RETURN_TANDEM_CODE", false));
		fieldInfos.add(new FieldInfo("RETURN_ISO_CODE", false));
		fieldInfos.add(new FieldInfo("RETURN_HOST_CODE", false));
		fieldInfos.add(new FieldInfo("ORIGINATOR", false));
		fieldInfos.add(new FieldInfo("RESPONDER", false));
		fieldInfos.add(new FieldInfo("MESSAGE_TYPE", false));
		fieldInfos.add(new FieldInfo("EDC_TX_TYPE", false));
		fieldInfos.add(new FieldInfo("CVV2_CHECK_RESULT", false));
		fieldInfos.add(new FieldInfo("MPAS_FLAG", false));
		fieldInfos.add(new FieldInfo("MPAS_MEMBER", false));
		fieldInfos.add(new FieldInfo("INSTALLMENT_FLAG", false));
		fieldInfos.add(new FieldInfo("INST_INDICATOR", false));
		fieldInfos.add(new FieldInfo("LOYALTY_FLAG", false));
		fieldInfos.add(new FieldInfo("LYLT_INDICATOR", false));
		fieldInfos.add(new FieldInfo("LOYALTY_ORIGINAL_POINT", false));
		fieldInfos.add(new FieldInfo("LOYALTY_REDEMPTION_POINT", false));
		fieldInfos.add(new FieldInfo("LOYALTY_REDEMPTION_AMT", false));
		fieldInfos.add(new FieldInfo("LOYALTY_PAID_CREDIT_AMT", false));
		fieldInfos.add(new FieldInfo("INSTALL_PERIOD_NUM", false));
		fieldInfos.add(new FieldInfo("INSTALL_FIRST_PAYMENT", false));
		fieldInfos.add(new FieldInfo("INSTALL_STAGES_PAYMENT", false));
		fieldInfos.add(new FieldInfo("INSTALL_FORMALITY_FEE", false));
		fieldInfos.add(new FieldInfo("MCC_AMT_LIMIT", false));
		fieldInfos.add(new FieldInfo("CARD_CLASS", false));
		fieldInfos.add(new FieldInfo("LIMIT_TYPE", false));
		fieldInfos.add(new FieldInfo("NORMAL_LIMIT_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("NORMAL_AMT_LAST_MONTH", false));
		fieldInfos.add(new FieldInfo("NORMAL_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_LIMIT_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_BILL_AMT", false));
		fieldInfos.add(new FieldInfo("CASH_AMT_LAST_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("TOTAL_LIMIT_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("AMT_CURRENT_MONTH", false));
		fieldInfos.add(new FieldInfo("AMT_LAST_MONTH", false));
		fieldInfos.add(new FieldInfo("ID_BILL_AMT", false));
		fieldInfos.add(new FieldInfo("MAX_LIMIT_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_AMT_CURRENT_MONTH", false));
		fieldInfos.add(new FieldInfo("ID_CASH_AMT_LAST_MONTH", false));
		fieldInfos.add(new FieldInfo("ID_CASH_BILL_AMT", false));
		fieldInfos.add(new FieldInfo("ID_CREDIT_LIMIT", false));
		fieldInfos.add(new FieldInfo("DATA_SOURCE", false));
		fieldInfos.add(new FieldInfo("BANK_NO_2", false));
		fieldInfos.add(new FieldInfo("BUSINESS_TYPE", false));
		fieldInfos.add(new FieldInfo("CARDHOLDER_ID", false));

		fieldInfos.add(new FieldInfo("CARD_TYPE", false));
		fieldInfos.add(new FieldInfo("CARD_KIND", false));
		fieldInfos.add(new FieldInfo("MANUAL_ENTRY_RESULT", false));
		fieldInfos.add(new FieldInfo("MANUAL_FORCE_POST_FLAG", false));
		fieldInfos.add(new FieldInfo("PRE_AUTH_AMT", false));

		fieldInfos.add(new FieldInfo("FILE_DATE", false));
		fieldInfos.add(new FieldInfo("MOD_PROGRAM_ID", false));
		fieldInfos.add(new FieldInfo("UPDATE_ID", false));
		fieldInfos.add(new FieldInfo("REMARK", false));
		// M98085國內臨時額度-增加臨時餘額
		fieldInfos.add(new FieldInfo("TEMP_CREDIT_LIMIT", false));
		// M2013085國際組織跨國交易收取surcharge amount
		fieldInfos.add(new FieldInfo("SURCHG_AMT", false));
		fieldInfos.add(new FieldInfo("DEPT_ID", false));

	}

	/** Creates a new instance of AuthLogDataStore */
	public AuthLogDataStore() {

	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {

		return "AUTH_LOG_DATA";
	}

	/**
	 * 提供table欄位的相關資訊, 以便組出PreparedStatement所需的SQL command.
	 */
	public List getFieldInfos() {

		return fieldInfos;
	}

	protected void insert(Persistable persistable, PreparedStatement pstmt) throws SQLException {

		int i = 1;
		AuthLogData o = (AuthLogData) persistable;
		if (o.getBankNo() != null)
			pstmt.setString(i++, o.getBankNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCardNo() != null)
			pstmt.setString(i++, o.getCardNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getBinNo() != null)
			pstmt.setString(i++, o.getBinNo());
		else {
			if (o.getCardNo() != null)
				pstmt.setString(i++, o.getCardNo().substring(0, 6));
			else
				pstmt.setNull(i++, java.sql.Types.VARCHAR);
		}
		if (o.getMajorCardId() != null)
			pstmt.setString(i++, o.getMajorCardId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getApprovalNo() != null)
			pstmt.setString(i++, o.getApprovalNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getMerchantNo() != null)
			pstmt.setString(i++, o.getMerchantNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getTerminalNo() != null)
			pstmt.setString(i++, o.getTerminalNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getPurchaseDate() != null)
			pstmt.setString(i++, o.getPurchaseDate());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getPurchaseTime() != null)
			pstmt.setString(i++, o.getPurchaseTime());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getPurchaseAmt() != null)
			pstmt.setLong(i++, o.getPurchaseAmt().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getProcessDate() != null)
			pstmt.setString(i++, o.getProcessDate());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getProcessTime() != null)
			pstmt.setString(i++, o.getProcessTime());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getExpireDate() != null)
			pstmt.setString(i++, o.getExpireDate());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getTransType() != null)
			pstmt.setString(i++, o.getTransType());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getConditionCode() != null)
			pstmt.setString(i++, o.getConditionCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getIcFlag() != null)
			pstmt.setString(i++, o.getIcFlag());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getAuthorizeReason() != null)
			pstmt.setString(i++, o.getAuthorizeReason());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getTermTraceNo() != null)
			pstmt.setString(i++, o.getTermTraceNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getAcquireId() != null)
			pstmt.setString(i++, o.getAcquireId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getAcquireBk() != null)
			pstmt.setString(i++, o.getAcquireBk());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCancelApprovalNo() != null)
			pstmt.setString(i++, o.getCancelApprovalNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCountryName() != null)
			pstmt.setString(i++, o.getCountryName());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getMccCode() != null)
			pstmt.setString(i++, o.getMccCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getEntryMode() != null)
			pstmt.setString(i++, o.getEntryMode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getTwoScreenApTx() != null)
			pstmt.setString(i++, o.getTwoScreenApTx());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getReturnTandemCode() != null)
			pstmt.setString(i++, o.getReturnTandemCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getReturnIsoCode() != null)
			pstmt.setString(i++, o.getReturnIsoCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getReturnHostCode() != null)
			pstmt.setString(i++, o.getReturnHostCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getOriginator() != null)
			pstmt.setString(i++, o.getOriginator());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getResponder() != null)
			pstmt.setString(i++, o.getResponder());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getMessageType() != null)
			pstmt.setString(i++, o.getMessageType());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getEdcTxType() != null)
			pstmt.setString(i++, o.getEdcTxType());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCvv2CheckResult() != null)
			pstmt.setString(i++, o.getCvv2CheckResult());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getMpasFlag() != null)
			pstmt.setString(i++, o.getMpasFlag());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getMpasMember() != null)
			pstmt.setString(i++, o.getMpasMember());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getInstallmentFlag() != null)
			pstmt.setString(i++, o.getInstallmentFlag());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getInstIndicator() != null)
			pstmt.setString(i++, o.getInstIndicator());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getLoyaltyFlag() != null)
			pstmt.setString(i++, o.getLoyaltyFlag());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getLyltIndicator() != null)
			pstmt.setString(i++, o.getLyltIndicator());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getLoyaltyOriginalPoint() != null)
			pstmt.setInt(i++, o.getLoyaltyOriginalPoint().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getLoyaltyRedemptionPoint() != null)
			pstmt.setInt(i++, o.getLoyaltyRedemptionPoint().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getLoyaltyRedemptionAmt() != null)
			pstmt.setInt(i++, o.getLoyaltyRedemptionAmt().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getLoyaltyPaidCreditAmt() != null)
			pstmt.setInt(i++, o.getLoyaltyPaidCreditAmt().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getInstallPeriodNum() != null)
			pstmt.setInt(i++, o.getInstallPeriodNum().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getInstallFirstPayment() != null)
			pstmt.setInt(i++, o.getInstallFirstPayment().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getInstallStagesPayment() != null)
			pstmt.setInt(i++, o.getInstallStagesPayment().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getInstallFormalityFee() != null)
			pstmt.setInt(i++, o.getInstallFormalityFee().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getMccAmtLimit() != null)
			pstmt.setLong(i++, o.getMccAmtLimit().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCardClass() != null)
			pstmt.setString(i++, o.getCardClass());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getLimitType() != null)
			pstmt.setString(i++, o.getLimitType());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		
		if (o.getNormalLimitAmtMonth() != null)
			pstmt.setLong(i++, o.getNormalLimitAmtMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalAmtLastMonth() != null)
			pstmt.setLong(i++, o.getNormalAmtLastMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalAmtMonth() != null)
			pstmt.setLong(i++, o.getNormalAmtMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitAmtMonth() != null)
			pstmt.setLong(i++, o.getCashLimitAmtMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashBillAmt() != null)
			pstmt.setLong(i++, o.getCashBillAmt().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtLastMonth() != null)
			pstmt.setLong(i++, o.getCashAmtLastMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtMonth() != null)
			pstmt.setLong(i++, o.getCashAmtMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitAmtMonth() != null)
			pstmt.setLong(i++, o.getTotalLimitAmtMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAmtCurrentMonth() != null)
			pstmt.setLong(i++, o.getAmtCurrentMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAmtLastMonth() != null)
			pstmt.setLong(i++, o.getAmtLastMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getIdBillAmt() != null)
			pstmt.setLong(i++, o.getIdBillAmt().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getMaxLimitAmtMonth() != null)
			pstmt.setLong(i++, o.getMaxLimitAmtMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtCurrentMonth() != null)
			pstmt.setLong(i++, o.getCashAmtCurrentMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getIdCashAmtLastMonth() != null)
			pstmt.setLong(i++, o.getIdCashAmtLastMonth().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getIdCashBillAmt() != null)
			pstmt.setLong(i++, o.getIdCashBillAmt().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getIdCreditLimit() != null)
			pstmt.setLong(i++, o.getIdCreditLimit().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getDataSource() != null)
			pstmt.setString(i++, o.getDataSource());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getBankNo2() != null)
			pstmt.setString(i++, o.getBankNo2());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getBusinessType() != null)
			pstmt.setString(i++, o.getBusinessType());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCardholderId() != null)
			pstmt.setString(i++, o.getCardholderId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCardType() != null)
			pstmt.setString(i++, o.getCardType());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getCardKind() != null)
			pstmt.setString(i++, o.getCardKind());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getManualEntryResult() != null)
			pstmt.setString(i++, o.getManualEntryResult());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getManualForcePostFlag() != null)
			pstmt.setString(i++, o.getManualForcePostFlag());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getPreAuthAmt() != null)
			pstmt.setLong(i++, o.getPreAuthAmt().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getFileDate() != null)
			pstmt.setString(i++, o.getFileDate().toString());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getModProgramId() != null)
			pstmt.setString(i++, o.getModProgramId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getUpdateId() != null)
			pstmt.setString(i++, o.getUpdateId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getRemark() != null)
			pstmt.setString(i++, o.getRemark());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		// M98085國內臨時額度-增加臨時餘額

		if (o.getTempCreditLimit() != null)
			pstmt.setLong(i++, o.getTempCreditLimit().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		// M2013085-國際組織跨國交易收取surcharge amount
		if (o.getSurchgAmt() != null)
			pstmt.setLong(i++, o.getSurchgAmt().longValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);
		
		if (o.getDeptId() != null)
			pstmt.setString(i++, o.getDeptId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
	}

	@Override
	protected void delete(Persistable arg0, PreparedStatement arg1) throws SQLException {

		// TODO Auto-generated method stub

	}

	@Override
	protected void update(Persistable arg0, PreparedStatement arg1) throws SQLException {

		// TODO Auto-generated method stub

	}

}
