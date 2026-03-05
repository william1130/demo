/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: AuthLogData.java,v 1.4 2019/11/25 05:48:19 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import com.edstw.lang.DateString;
import com.edstw.lang.DoubleString;
import com.edstw.lang.LongString;
import com.edstw.nccc.sql.log.ValueImage;
import com.edstw.util.ValidateUtil;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.4 $
 */
public class AuthLogData extends AbstractProjPersistable {

	private static final long serialVersionUID = -1790620701218180122L;

	// dumy primary key for framework..v
	private String rowId;

	// (desc="銀行別")
	private String bankNo;

	// (desc="卡號", mask="cardNum")
	private String cardNo;

	private String binNo;

	private String majorCardId;

	// (desc="授權碼")
	private String approvalNo;

	// (desc="特店代號")
	private String merchantNo;

	private String terminalNo;

	private String purchaseDate;

	private String purchaseTime;

	// (desc="金額")
	private LongString purchaseAmt;

	// (desc="授權日期")
	private String processDate;

	// (desc="授權時間")
	private String processTime;

	// (desc="有效日期")
	private String expireDate;

	// (desc="類別")
	private String transType;

	private String conditionCode;

	private String icFlag;

	private String authorizeReason;

	private String termTraceNo;

	private String acquireId;

	private String acquireBk;

	private String cancelApprovalNo;

	private String countryName;

	private String mccCode;

	private String entryMode;

	private String twoScreenApTx;

	private String returnTandemCode;

	private String returnIsoCode;

	private String returnHostCode;

	private String originator;

	private String responder;

	private String messageType;

	private String edcTxType;

	private String cvv2CheckResult;

	private String mpasFlag;

	private String mpasMember;

	private String installmentFlag;

	private String instIndicator;

	private String loyaltyFlag;

	private String lyltIndicator;

	private DoubleString loyaltyOriginalPoint;

	private DoubleString loyaltyRedemptionPoint;

	private DoubleString loyaltyRedemptionAmt;

	private DoubleString loyaltyPaidCreditAmt;

	private DoubleString installPeriodNum;

	private DoubleString installFirstPayment;

	private DoubleString installStagesPayment;

	private DoubleString installFormalityFee;

	private DoubleString mccAmtLimit;

	private String cardClass;

	private String limitType;

	private DoubleString normalLimitAmtMonth;

	private DoubleString normalAmtLastMonth;

	private DoubleString normalAmtMonth;

	private DoubleString cashLimitAmtMonth;

	private DoubleString cashBillAmt;

	private DoubleString cashAmtLastMonth;

	private DoubleString cashAmtMonth;

	private DoubleString totalLimitAmtMonth;

	private DoubleString amtCurrentMonth;

	private DoubleString amtLastMonth;

	private DoubleString idBillAmt;

	private DoubleString maxLimitAmtMonth;

	private DoubleString cashAmtCurrentMonth;

	private DoubleString idCashAmtLastMonth;

	private DoubleString idCashBillAmt;

	private DoubleString idCreditLimit;

	// (desc="資料來源")
	private String dataSource;

	private DateString fileDate;

	private String modProgramId;

	private String updateId;

	private String remark;

	private String bankNo2;

	private String businessType;

	private String cardholderId;

	// (desc="卡別")
	private String cardType;

	// (desc="卡種")
	private String cardKind;

	private String manualEntryResult;

	private String manualForcePostFlag;

	private DoubleString preAuthAmt;

	// M98085國內臨時額度第二階段-增加臨時餘額

	private DoubleString tempCreditLimit;

	// 20081126 當類別 為I 開頭都要顯示approveNo

	private String displayApproveNo;

	// M2013085國際組織跨國交易收取surcharge amount

	private DoubleString surchgAmt;
	
	private String deptId;

	// 收單別 F: 國外 D: 國內
	// (desc="收單別")
	private String acquirerType;

	public AuthLogData() {

	}

	public String getId() {

		return getRowId();
	}

	public String getRowId() {

		return rowId;
	}

	public void setRowId(String rowId) {

		this.rowId = rowId;
	}

	public String getBankNo() {

		return bankNo;
	}

	public void setBankNo(String bankNo) {

		this.bankNo = bankNo;
		this.modified();
	}

	public String getCardNo() {

		if (cardNo != null) {
			cardNo = cardNo.trim();
		}
		return cardNo;
	}

	public void setCardNo(String cardNo) {

		this.cardNo = cardNo;
		if (this.cardNo != null) {
			this.cardNo = this.cardNo.trim();
		}
		this.modified();
	}

	public String getBinNo() {

		if (cardNo != null && cardNo.trim().length() > 6) {
			binNo = cardNo.substring(0, 6);
		}
		return binNo;
	}

	public void setBinNo(String binNo) {

		this.binNo = binNo;
	}

	public String getMajorCardId() {

		return majorCardId;
	}

	public void setMajorCardId(String majorCardId) {

		this.majorCardId = majorCardId;
		this.modified();
	}

	public String getApprovalNo() {

		return approvalNo;
	}

	public void setApprovalNo(String approvalNo) {

		this.approvalNo = approvalNo;
		this.modified();
	}

	public String getMerchantNo() {

		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {

		this.merchantNo = merchantNo;
		this.modified();
	}

	public String getTerminalNo() {

		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {

		this.terminalNo = terminalNo;
		this.modified();
	}

	public String getPurchaseDate() {

		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {

		this.purchaseDate = purchaseDate;
		this.modified();
	}

	public String getPurchaseTime() {

		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {

		this.purchaseTime = purchaseTime;
		this.modified();
	}

	public LongString getPurchaseAmt() {

		return purchaseAmt;
	}

	public void setPurchaseAmt(LongString purchaseAmt) {

		this.purchaseAmt = purchaseAmt;
		this.modified();
	}

	public String getProcessDate() {

		return processDate;
	}

	public void setProcessDate(String processDate) {

		this.processDate = processDate;
		this.modified();
	}

	public String getProcessTime() {

		return processTime;
	}

	public void setProcessTime(String processTime) {

		this.processTime = processTime;
		this.modified();
	}

	public String getExpireDate() {

		return expireDate;
	}

	public void setExpireDate(String expireDate) {

		this.expireDate = expireDate;
		this.modified();
	}

	public String getTransType() {

		return transType;
	}

	public void setTransType(String transType) {

		this.transType = transType;
		this.modified();
	}

	public String getConditionCode() {

		return conditionCode;
	}

	public void setConditionCode(String conditionCode) {

		this.conditionCode = conditionCode;
		this.modified();
	}

	public String getIcFlag() {

		return icFlag;
	}

	public void setIcFlag(String icFlag) {

		this.icFlag = icFlag;
		this.modified();
	}

	public String getAuthorizeReason() {

		return authorizeReason;
	}

	public void setAuthorizeReason(String authorizeReason) {

		this.authorizeReason = authorizeReason;
		this.modified();
	}

	public String getTermTraceNo() {

		return termTraceNo;
	}

	public void setTermTraceNo(String termTraceNo) {

		this.termTraceNo = termTraceNo;
		this.modified();
	}

	public String getAcquireId() {

		return acquireId;
	}

	public void setAcquireId(String acquireId) {

		this.acquireId = acquireId;
		this.modified();
	}

	public String getAcquireBk() {

		return acquireBk;
	}

	public void setAcquireBk(String acquireBk) {

		this.acquireBk = acquireBk;
		this.modified();
	}

	public String getCancelApprovalNo() {

		return cancelApprovalNo;
	}

	public void setCancelApprovalNo(String cancelApprovalNo) {

		this.cancelApprovalNo = cancelApprovalNo;
		this.modified();
	}

	public String getCountryName() {

		return countryName;
	}

	public void setCountryName(String countryName) {

		this.countryName = countryName;
		this.modified();
	}

	public String getMccCode() {

		return mccCode;
	}

	public void setMccCode(String mccCode) {

		this.mccCode = mccCode;
		this.modified();
	}

	public String getEntryMode() {

		return entryMode;
	}

	public void setEntryMode(String entryMode) {

		this.entryMode = entryMode;
		this.modified();
	}

	public String getTwoScreenApTx() {

		return twoScreenApTx;
	}

	public void setTwoScreenApTx(String twoScreenApTx) {

		this.twoScreenApTx = twoScreenApTx;
		this.modified();
	}

	public String getReturnTandemCode() {

		return returnTandemCode;
	}

	public void setReturnTandemCode(String returnTandemCode) {

		this.returnTandemCode = returnTandemCode;
		this.modified();
	}

	public String getReturnIsoCode() {

		return returnIsoCode;
	}

	public void setReturnIsoCode(String returnIsoCode) {

		this.returnIsoCode = returnIsoCode;
		this.modified();
	}

	public String getReturnHostCode() {

		return returnHostCode;
	}

	public void setReturnHostCode(String returnHostCode) {

		this.returnHostCode = returnHostCode;
		this.modified();
	}

	public String getOriginator() {

		return originator;
	}

	public void setOriginator(String originator) {

		this.originator = originator;
		this.modified();
	}

	public String getResponder() {

		return responder;
	}

	public void setResponder(String responder) {

		this.responder = responder;
		this.modified();
	}

	public String getMessageType() {

		return messageType;
	}

	public void setMessageType(String messageType) {

		this.messageType = messageType;
		this.modified();
	}

	public String getEdcTxType() {

		return edcTxType;
	}

	public void setEdcTxType(String edcTxType) {

		this.edcTxType = edcTxType;
		this.modified();
	}

	public String getCvv2CheckResult() {

		return cvv2CheckResult;
	}

	public void setCvv2CheckResult(String cvv2CheckResult) {

		this.cvv2CheckResult = cvv2CheckResult;
		this.modified();
	}

	public String getMpasFlag() {

		return mpasFlag;
	}

	public void setMpasFlag(String mpasFlag) {

		this.mpasFlag = mpasFlag;
		this.modified();
	}

	public String getMpasMember() {

		return mpasMember;
	}

	public void setMpasMember(String mpasMember) {

		this.mpasMember = mpasMember;
		this.modified();
	}

	public String getInstallmentFlag() {

		return installmentFlag;
	}

	public void setInstallmentFlag(String installmentFlag) {

		this.installmentFlag = installmentFlag;
		this.modified();
	}

	public String getInstIndicator() {

		return instIndicator;
	}

	public void setInstIndicator(String instIndicator) {

		this.instIndicator = instIndicator;
		this.modified();
	}

	public String getLoyaltyFlag() {

		return loyaltyFlag;
	}

	public void setLoyaltyFlag(String loyaltyFlag) {

		this.loyaltyFlag = loyaltyFlag;
		this.modified();
	}

	public String getLyltIndicator() {

		return lyltIndicator;
	}

	public void setLyltIndicator(String lyltIndicator) {

		this.lyltIndicator = lyltIndicator;
		this.modified();
	}

	public DoubleString getLoyaltyOriginalPoint() {

		return loyaltyOriginalPoint;
	}

	public void setLoyaltyOriginalPoint(DoubleString loyaltyOriginalPoint) {

		this.loyaltyOriginalPoint = loyaltyOriginalPoint;
		this.modified();
	}

	public DoubleString getLoyaltyRedemptionPoint() {

		return loyaltyRedemptionPoint;
	}

	public void setLoyaltyRedemptionPoint(DoubleString loyaltyRedemptionPoint) {

		this.loyaltyRedemptionPoint = loyaltyRedemptionPoint;
		this.modified();
	}

	public DoubleString getLoyaltyRedemptionAmt() {

		return loyaltyRedemptionAmt;
	}

	public void setLoyaltyRedemptionAmt(DoubleString loyaltyRedemptionAmt) {

		this.loyaltyRedemptionAmt = loyaltyRedemptionAmt;
		this.modified();
	}

	public DoubleString getLoyaltyPaidCreditAmt() {

		return loyaltyPaidCreditAmt;
	}

	public void setLoyaltyPaidCreditAmt(DoubleString loyaltyPaidCreditAmt) {

		this.loyaltyPaidCreditAmt = loyaltyPaidCreditAmt;
		this.modified();
	}

	public DoubleString getInstallPeriodNum() {

		return installPeriodNum;
	}

	public void setInstallPeriodNum(DoubleString installPeriodNum) {

		this.installPeriodNum = installPeriodNum;
		this.modified();
	}

	public DoubleString getInstallFirstPayment() {

		return installFirstPayment;
	}

	public void setInstallFirstPayment(DoubleString installFirstPayment) {

		this.installFirstPayment = installFirstPayment;
		this.modified();
	}

	public DoubleString getInstallStagesPayment() {

		return installStagesPayment;
	}

	public void setInstallStagesPayment(DoubleString installStagesPayment) {

		this.installStagesPayment = installStagesPayment;
		this.modified();
	}

	public DoubleString getInstallFormalityFee() {

		return installFormalityFee;
	}

	public void setInstallFormalityFee(DoubleString installFormalityFee) {

		this.installFormalityFee = installFormalityFee;
		this.modified();
	}

	public DoubleString getMccAmtLimit() {

		return mccAmtLimit;
	}

	public void setMccAmtLimit(DoubleString mccAmtLimit) {

		this.mccAmtLimit = mccAmtLimit;
		this.modified();
	}

	public String getCardClass() {

		return cardClass;
	}

	public void setCardClass(String cardClass) {

		this.cardClass = cardClass;
		this.modified();
	}

	public String getLimitType() {

		return limitType;
	}

	public void setLimitType(String limitType) {

		this.limitType = limitType;
		this.modified();
	}

	public DoubleString getNormalLimitAmtMonth() {

		return normalLimitAmtMonth;
	}

	public void setNormalLimitAmtMonth(DoubleString normalLimitAmtMonth) {

		this.normalLimitAmtMonth = normalLimitAmtMonth;
		this.modified();
	}

	public DoubleString getNormalAmtLastMonth() {

		return normalAmtLastMonth;
	}

	public void setNormalAmtLastMonth(DoubleString normalAmtLastMonth) {

		this.normalAmtLastMonth = normalAmtLastMonth;
		this.modified();
	}

	public DoubleString getNormalAmtMonth() {

		return normalAmtMonth;
	}

	public void setNormalAmtMonth(DoubleString normalAmtMonth) {

		this.normalAmtMonth = normalAmtMonth;
		this.modified();
	}

	public DoubleString getCashLimitAmtMonth() {

		return cashLimitAmtMonth;
	}

	public void setCashLimitAmtMonth(DoubleString cashLimitAmtMonth) {

		this.cashLimitAmtMonth = cashLimitAmtMonth;
		this.modified();
	}

	public DoubleString getCashBillAmt() {

		return cashBillAmt;
	}

	public void setCashBillAmt(DoubleString cashBillAmt) {

		this.cashBillAmt = cashBillAmt;
		this.modified();
	}

	public DoubleString getCashAmtLastMonth() {

		return cashAmtLastMonth;
	}

	public void setCashAmtLastMonth(DoubleString cashAmtLastMonth) {

		this.cashAmtLastMonth = cashAmtLastMonth;
		this.modified();
	}

	public DoubleString getCashAmtMonth() {

		return cashAmtMonth;
	}

	public void setCashAmtMonth(DoubleString cashAmtMonth) {

		this.cashAmtMonth = cashAmtMonth;
		this.modified();
	}

	public DoubleString getTotalLimitAmtMonth() {

		return totalLimitAmtMonth;
	}

	public void setTotalLimitAmtMonth(DoubleString totalLimitAmtMonth) {

		this.totalLimitAmtMonth = totalLimitAmtMonth;
		this.modified();
	}

	public DoubleString getAmtCurrentMonth() {

		return amtCurrentMonth;
	}

	public void setAmtCurrentMonth(DoubleString amtCurrentMonth) {

		this.amtCurrentMonth = amtCurrentMonth;
		this.modified();
	}

	public DoubleString getAmtLastMonth() {

		return amtLastMonth;
	}

	public void setAmtLastMonth(DoubleString amtLastMonth) {

		this.amtLastMonth = amtLastMonth;
		this.modified();
	}

	public DoubleString getIdBillAmt() {

		return idBillAmt;
	}

	public void setIdBillAmt(DoubleString idBillAmt) {

		this.idBillAmt = idBillAmt;
		this.modified();
	}

	public DoubleString getMaxLimitAmtMonth() {

		return maxLimitAmtMonth;
	}

	public void setMaxLimitAmtMonth(DoubleString maxLimitAmtMonth) {

		this.maxLimitAmtMonth = maxLimitAmtMonth;
		this.modified();
	}

	public DoubleString getCashAmtCurrentMonth() {

		return cashAmtCurrentMonth;
	}

	public void setCashAmtCurrentMonth(DoubleString cashAmtCurrentMonth) {

		this.cashAmtCurrentMonth = cashAmtCurrentMonth;
		this.modified();
	}

	public DoubleString getIdCashAmtLastMonth() {

		return idCashAmtLastMonth;
	}

	public void setIdCashAmtLastMonth(DoubleString idCashAmtLastMonth) {

		this.idCashAmtLastMonth = idCashAmtLastMonth;
		this.modified();
	}

	public DoubleString getIdCashBillAmt() {

		return idCashBillAmt;
	}

	public void setIdCashBillAmt(DoubleString idCashBillAmt) {

		this.idCashBillAmt = idCashBillAmt;
		this.modified();
	}

	public DoubleString getIdCreditLimit() {

		return idCreditLimit;
	}

	public void setIdCreditLimit(DoubleString idCreditLimit) {

		this.idCreditLimit = idCreditLimit;
		this.modified();
	}

	public String getDataSource() {

		return dataSource;
	}

	public void setDataSource(String dataSource) {

		this.dataSource = dataSource;
		this.modified();
	}

	public DateString getFileDate() {

		return fileDate;
	}

	public void setFileDate(DateString fileDate) {

		this.fileDate = fileDate;
		this.modified();
	}

	public String getModProgramId() {

		return modProgramId;
	}

	public void setModProgramId(String modProgramId) {

		this.modProgramId = modProgramId;
		this.modified();
	}

	public String getUpdateId() {

		return updateId;
	}

	public void setUpdateId(String updateId) {

		this.updateId = updateId;
		this.modified();
	}

	public String getRemark() {

		return remark;
	}

	public void setRemark(String remark) {

		this.remark = remark;
		this.modified();
	}

	public String getBankNo2() {

		return bankNo2;
	}

	public void setBankNo2(String bankNo2) {

		this.bankNo2 = bankNo2;
		this.modified();
	}

	public String getBusinessType() {

		return businessType;
	}

	public void setBusinessType(String businessType) {

		this.businessType = businessType;
		this.modified();
	}

	public String getCardholderId() {

		return cardholderId;
	}

	public void setCardholderId(String cardholderId) {

		this.cardholderId = cardholderId;
		this.modified();
	}

	public String getCardType() {

		return cardType;
	}

	public void setCardType(String cardType) {

		this.cardType = cardType;
		this.modified();
	}

	public String getCardKind() {

		return cardKind;
	}

	public void setCardKind(String cardKind) {

		this.cardKind = cardKind;
		this.modified();
	}

	public String getManualEntryResult() {

		return manualEntryResult;
	}

	public void setManualEntryResult(String manualEntryResult) {

		this.manualEntryResult = manualEntryResult;
		this.modified();
	}

	public String getManualForcePostFlag() {

		return manualForcePostFlag;
	}

	public void setManualForcePostFlag(String manualForcePostFlag) {

		this.manualForcePostFlag = manualForcePostFlag;
	}

	public String getAcquirerType() {

		return acquirerType;
	}

	public void setAcquirerType(String acquirerType) {

		this.acquirerType = acquirerType;
	}

	public DoubleString getPreAuthAmt() {

		return preAuthAmt;
	}

	public void setPreAuthAmt(DoubleString preAuthAmt) {

		this.preAuthAmt = preAuthAmt;
		this.modified();
	}

	public String getDisplayApproveNo() {

		displayApproveNo = "N";
		if (this.transType != null && this.transType.trim().length() > 1) {
			if (this.transType.trim().substring(0, 1).equals("I"))
				displayApproveNo = "Y";
		}
		// 20090211 fallback資料授權不成功時,不顯示授權碼
		if (!this.getDataSource().equals("4") && ValidateUtil.isNotBlank(this.getReturnIsoCode())
				&& ValidateUtil.isNotEqual(this.getReturnIsoCode(), "00")) {
			displayApproveNo = "N";
		}
		// 20090223 Stephen.Lin , DataSource=5 ,無條件顯示授權碼
		// 20110325 芳英表示後端授權交易及前端回灌的交易都要顯示授權碼
		if (this.getDataSource().equals("1") || this.getDataSource().equals("5")) {
			displayApproveNo = "Y";
		}
		return displayApproveNo;
	}

	public void setDisplayApproveNo(String displayApproveNo) {

		this.displayApproveNo = displayApproveNo;
	}

	// M98085國內臨時額度第二階段--增加臨時餘額
	public DoubleString getTempCreditLimit() {

		return tempCreditLimit;
	}

	public void setTempCreditLimit(DoubleString tempCreditLimit) {

		this.tempCreditLimit = tempCreditLimit;
		this.modified();
	}

	/**
	 * @return the surchgAmt
	 */
	public DoubleString getSurchgAmt() {

		return surchgAmt;
	}

	/**
	 * @param surchgAmt the surchgAmt to set
	 */
	public void setSurchgAmt(DoubleString surchgAmt) {

		this.surchgAmt = surchgAmt;
		this.modified();
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
		this.modified();
	}

	@Override
	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(bankNo);
		vi.addValue(cardNo);
		vi.addValue(majorCardId);
		vi.addValue(approvalNo);
		vi.addValue(merchantNo);
		vi.addValue(terminalNo);
		vi.addValue(purchaseDate);
		vi.addValue(purchaseTime);
		vi.addValue(purchaseAmt);
		vi.addValue(processDate);
		vi.addValue(processTime);
		vi.addValue(expireDate);
		vi.addValue(transType);
		vi.addValue(conditionCode);
		vi.addValue(icFlag);
		vi.addValue(authorizeReason);
		vi.addValue(termTraceNo);
		vi.addValue(acquireId);
		vi.addValue(acquireBk);
		vi.addValue(cancelApprovalNo);
		vi.addValue(countryName);
		vi.addValue(mccCode);
		vi.addValue(entryMode);
		vi.addValue(twoScreenApTx);
		vi.addValue(returnTandemCode);
		vi.addValue(returnIsoCode);
		vi.addValue(returnHostCode);
		vi.addValue(originator);
		vi.addValue(responder);
		vi.addValue(messageType);
		vi.addValue(edcTxType);
		vi.addValue(cvv2CheckResult);
		vi.addValue(mpasFlag);
		vi.addValue(mpasMember);
		vi.addValue(installmentFlag);
		vi.addValue(instIndicator);
		vi.addValue(loyaltyFlag);
		vi.addValue(lyltIndicator);
		vi.addValue(loyaltyOriginalPoint);
		vi.addValue(loyaltyRedemptionPoint);
		vi.addValue(loyaltyRedemptionAmt);
		vi.addValue(loyaltyPaidCreditAmt);
		vi.addValue(installPeriodNum);
		vi.addValue(installFirstPayment);
		vi.addValue(installStagesPayment);
		vi.addValue(installFormalityFee);
		vi.addValue(mccAmtLimit);
		vi.addValue(cardClass);
		vi.addValue(limitType);
		vi.addValue(normalLimitAmtMonth);
		vi.addValue(normalAmtLastMonth);
		vi.addValue(normalAmtMonth);
		vi.addValue(cashLimitAmtMonth);
		vi.addValue(cashBillAmt);
		vi.addValue(cashAmtLastMonth);
		vi.addValue(cashAmtMonth);
		vi.addValue(totalLimitAmtMonth);
		vi.addValue(amtCurrentMonth);
		vi.addValue(amtLastMonth);
		vi.addValue(idBillAmt);
		vi.addValue(maxLimitAmtMonth);
		vi.addValue(cashAmtCurrentMonth);
		vi.addValue(idCashAmtLastMonth);
		vi.addValue(idCashBillAmt);
		vi.addValue(idCreditLimit);
		vi.addValue(dataSource);
		vi.addValue(fileDate);
		vi.addValue(modProgramId);
		vi.addValue(updateId);
		vi.addValue(remark);
		vi.addValue(bankNo2);
		vi.addValue(businessType);
		vi.addValue(cardholderId);
//		vi.addValue(actionCode);
//		vi.addValue(actionCodeMem);
//		vi.addValue(authLinkType);
//		vi.addValue(serviceCode);
//		vi.addValue(crdhActvtTermInd);
//		vi.addValue(posConditionCode);
		vi.addValue(cardType);
		vi.addValue(cardKind);
		vi.addValue(manualEntryResult);
		vi.addValue(manualForcePostFlag);
		vi.addValue(binNo);
		vi.addValue(preAuthAmt);
//		vi.addValue(seqNo);
		vi.addValue(tempCreditLimit);
		vi.addValue(surchgAmt);
		vi.addValue(deptId);

		return vi;
	}

}
