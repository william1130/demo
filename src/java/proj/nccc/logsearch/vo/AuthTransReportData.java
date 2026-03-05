/*
 * AuthTransReportData.java
 * 
 * Created on 2008年7月14日, 上午 9:34
 * ==============================================================================================
 * $Id: AuthTransReportData.java,v 1.1 2017/04/24 01:31:25 asiapacific\jih Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.vo;

import java.util.List;

/**
 * 
 * @author xz04wy
 * @version $Revision: 1.1 $
 */
public class AuthTransReportData {
	// 宣告變數來當成傳入報表中的Parameters
	private String fileDate1;
	private String fileDate2;
	private String bankNo;
	private String bankName;
	private String dataSource;
	private String cardType;
	private String cardKind;
	private List records;

	public class Record {
		private String transName;
		private String bankNo;
		private String cardNo;
		private String majorCardId;
		private String approvalNo;
		private String merchantNo;
		private String terminalNo;
		private String purchaseDate;
		private String purchaseTime;
		private String purchaseAmt;
		private String processDate;
		private String processTime;
		private String expireDate;
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
		private String cardClass;
		private String limitType;
		private String dataSource;
		private String fileDate;
		private String updateId;
		private String remark;
		private String cardType;
		private String cardKind;
		private String manualEntryResult;
		private String manualForcePostFlag;

		private Double loyaltyOriginalPoint;
		private Double loyaltyRedemptionPoint;
		private Double loyaltyRedemptionAmt;
		private Double loyaltyPaidCreditAmt;
		private Double installPeriodNum;
		private Double installFirstPayment;
		private Double installStagesPayment;
		private Double installFormalityFee;
		private Double mccAmtLimit;
		private Double normalLimitAmtMonth;
		private Double normalAmtLastMonth;
		private Double normalAmtMonth;
		private Double cashLimitAmtMonth;
		private Double cashBillAmt;
		private Double cashAmtLastMonth;
		private Double cashAmtMonth;
		private Double totalLimitAmtMonth;
		private Double amtCurrentMonth;
		private Double amtLastMonth;
		private Double idBillAmt;
		private Double maxLimitAmtMonth;
		private Double cashAmtCurrentMonth;
		private Double idCashAmtLastMonth;
		private Double idCashBillAmt;
		private Double idCreditLimit;

		private Double creditLimit;
		private Double tempCreditLimit;
		private String surchgAmt;

		public String getAcquireBk() {
			return acquireBk;
		}

		public void setAcquireBk(String acquireBk) {
			this.acquireBk = acquireBk;
		}

		public String getAcquireId() {
			return acquireId;
		}

		public void setAcquireId(String acquireId) {
			this.acquireId = acquireId;
		}

		public String getApprovalNo() {
			return approvalNo;
		}

		public void setApprovalNo(String approvalNo) {
			this.approvalNo = approvalNo;
		}

		public String getAuthorizeReason() {
			return authorizeReason;
		}

		public void setAuthorizeReason(String authorizeReason) {
			this.authorizeReason = authorizeReason;
		}

		public String getBankNo() {
			return bankNo;
		}

		public void setBankNo(String bankNo) {
			this.bankNo = bankNo;
		}

		public String getCancelApprovalNo() {
			return cancelApprovalNo;
		}

		public void setCancelApprovalNo(String cancelApprovalNo) {
			this.cancelApprovalNo = cancelApprovalNo;
		}

		public String getCardClass() {
			return cardClass;
		}

		public void setCardClass(String cardClass) {
			this.cardClass = cardClass;
		}

		public String getCardKind() {
			return cardKind;
		}

		public void setCardKind(String cardKind) {
			this.cardKind = cardKind;
		}

		public String getCardNo() {
			return cardNo;
		}

		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}

		public String getCardType() {
			return cardType;
		}

		public void setCardType(String cardType) {
			this.cardType = cardType;
		}

		public String getConditionCode() {
			return conditionCode;
		}

		public void setConditionCode(String conditionCode) {
			this.conditionCode = conditionCode;
		}

		public String getCountryName() {
			return countryName;
		}

		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}

		public Double getCreditLimit() {
			return creditLimit;
		}

		public void setCreditLimit(Double creditLimit) {
			this.creditLimit = creditLimit;
		}

		// M98085國內臨時額度第二階段-增加臨時餘額
		public Double getTempCreditLimit() {
			return tempCreditLimit;
		}

		public void setTempcreditLimit(Double tempCreditLimit) {
			this.tempCreditLimit = tempCreditLimit;
		}

		public String getCvv2CheckResult() {
			return cvv2CheckResult;
		}

		public void setCvv2CheckResult(String cvv2CheckResult) {
			this.cvv2CheckResult = cvv2CheckResult;
		}

		public String getDataSource() {
			return dataSource;
		}

		public void setDataSource(String dataSource) {
			this.dataSource = dataSource;
		}

		public String getEdcTxType() {
			return edcTxType;
		}

		public void setEdcTxType(String edcTxType) {
			this.edcTxType = edcTxType;
		}

		public String getEntryMode() {
			return entryMode;
		}

		public void setEntryMode(String entryMode) {
			this.entryMode = entryMode;
		}

		public String getExpireDate() {
			return expireDate;
		}

		public void setExpireDate(String expireDate) {
			this.expireDate = expireDate;
		}

		public String getFileDate() {
			return fileDate;
		}

		public void setFileDate(String fileDate) {
			this.fileDate = fileDate;
		}

		public String getIcFlag() {
			return icFlag;
		}

		public void setIcFlag(String icFlag) {
			this.icFlag = icFlag;
		}

		public String getInstallmentFlag() {
			return installmentFlag;
		}

		public void setInstallmentFlag(String installmentFlag) {
			this.installmentFlag = installmentFlag;
		}

		public String getInstIndicator() {
			return instIndicator;
		}

		public void setInstIndicator(String instIndicator) {
			this.instIndicator = instIndicator;
		}

		public String getLimitType() {
			return limitType;
		}

		public void setLimitType(String limitType) {
			this.limitType = limitType;
		}

		public String getLoyaltyFlag() {
			return loyaltyFlag;
		}

		public void setLoyaltyFlag(String loyaltyFlag) {
			this.loyaltyFlag = loyaltyFlag;
		}

		public String getLyltIndicator() {
			return lyltIndicator;
		}

		public void setLyltIndicator(String lyltIndicator) {
			this.lyltIndicator = lyltIndicator;
		}

		public String getMajorCardId() {
			return majorCardId;
		}

		public void setMajorCardId(String majorCardId) {
			this.majorCardId = majorCardId;
		}

		public String getManualEntryResult() {
			return manualEntryResult;
		}

		public void setManualEntryResult(String manualEntryResult) {
			this.manualEntryResult = manualEntryResult;
		}

		public String getManualForcePostFlag() {
			return manualForcePostFlag;
		}

		public void setManualForcePostFlag(String manualForcePostFlag) {
			this.manualForcePostFlag = manualForcePostFlag;
		}

		public String getMccCode() {
			return mccCode;
		}

		public void setMccCode(String mccCode) {
			this.mccCode = mccCode;
		}

		public String getMerchantNo() {
			return merchantNo;
		}

		public void setMerchantNo(String merchantNo) {
			this.merchantNo = merchantNo;
		}

		public String getMessageType() {
			return messageType;
		}

		public void setMessageType(String messageType) {
			this.messageType = messageType;
		}

		public String getMpasFlag() {
			return mpasFlag;
		}

		public void setMpasFlag(String mpasFlag) {
			this.mpasFlag = mpasFlag;
		}

		public String getMpasMember() {
			return mpasMember;
		}

		public void setMpasMember(String mpasMember) {
			this.mpasMember = mpasMember;
		}

		public String getOriginator() {
			return originator;
		}

		public void setOriginator(String originator) {
			this.originator = originator;
		}

		public String getProcessDate() {
			return processDate;
		}

		public void setProcessDate(String processDate) {
			this.processDate = processDate;
		}

		public String getProcessTime() {
			return processTime;
		}

		public void setProcessTime(String processTime) {
			this.processTime = processTime;
		}

		public String getPurchaseAmt() {
			return purchaseAmt;
		}

		public void setPurchaseAmt(String purchaseAmt) {
			this.purchaseAmt = purchaseAmt;
		}

		public String getPurchaseDate() {
			return purchaseDate;
		}

		public void setPurchaseDate(String purchaseDate) {
			this.purchaseDate = purchaseDate;
		}

		public String getPurchaseTime() {
			return purchaseTime;
		}

		public void setPurchaseTime(String purchaseTime) {
			this.purchaseTime = purchaseTime;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getResponder() {
			return responder;
		}

		public void setResponder(String responder) {
			this.responder = responder;
		}

		public String getReturnHostCode() {
			return returnHostCode;
		}

		public void setReturnHostCode(String returnHostCode) {
			this.returnHostCode = returnHostCode;
		}

		public String getReturnIsoCode() {
			return returnIsoCode;
		}

		public void setReturnIsoCode(String returnIsoCode) {
			this.returnIsoCode = returnIsoCode;
		}

		public String getReturnTandemCode() {
			return returnTandemCode;
		}

		public void setReturnTandemCode(String returnTandemCode) {
			this.returnTandemCode = returnTandemCode;
		}

		public String getTransName() {
			return transName;
		}

		public void setTransName(String transName) {
			this.transName = transName;
		}

		public String getTerminalNo() {
			return terminalNo;
		}

		public void setTerminalNo(String terminalNo) {
			this.terminalNo = terminalNo;
		}

		public String getTermTraceNo() {
			return termTraceNo;
		}

		public void setTermTraceNo(String termTraceNo) {
			this.termTraceNo = termTraceNo;
		}

		public String getTransType() {
			return transType;
		}

		public void setTransType(String transType) {
			this.transType = transType;
		}

		public String getTwoScreenApTx() {
			return twoScreenApTx;
		}

		public void setTwoScreenApTx(String twoScreenApTx) {
			this.twoScreenApTx = twoScreenApTx;
		}

		public String getUpdateId() {
			return updateId;
		}

		public void setUpdateId(String updateId) {
			this.updateId = updateId;
		}

		public Double getAmtCurrentMonth() {
			return amtCurrentMonth;
		}

		public void setAmtCurrentMonth(Double amtCurrentMonth) {
			this.amtCurrentMonth = amtCurrentMonth;
		}

		public Double getAmtLastMonth() {
			return amtLastMonth;
		}

		public void setAmtLastMonth(Double amtLastMonth) {
			this.amtLastMonth = amtLastMonth;
		}

		public Double getCashAmtCurrentMonth() {
			return cashAmtCurrentMonth;
		}

		public void setCashAmtCurrentMonth(Double cashAmtCurrentMonth) {
			this.cashAmtCurrentMonth = cashAmtCurrentMonth;
		}

		public Double getCashAmtLastMonth() {
			return cashAmtLastMonth;
		}

		public void setCashAmtLastMonth(Double cashAmtLastMonth) {
			this.cashAmtLastMonth = cashAmtLastMonth;
		}

		public Double getCashAmtMonth() {
			return cashAmtMonth;
		}

		public void setCashAmtMonth(Double cashAmtMonth) {
			this.cashAmtMonth = cashAmtMonth;
		}

		public Double getCashBillAmt() {
			return cashBillAmt;
		}

		public void setCashBillAmt(Double cashBillAmt) {
			this.cashBillAmt = cashBillAmt;
		}

		public Double getCashLimitAmtMonth() {
			return cashLimitAmtMonth;
		}

		public void setCashLimitAmtMonth(Double cashLimitAmtMonth) {
			this.cashLimitAmtMonth = cashLimitAmtMonth;
		}

		public Double getIdBillAmt() {
			return idBillAmt;
		}

		public void setIdBillAmt(Double idBillAmt) {
			this.idBillAmt = idBillAmt;
		}

		public Double getIdCashAmtLastMonth() {
			return idCashAmtLastMonth;
		}

		public void setIdCashAmtLastMonth(Double idCashAmtLastMonth) {
			this.idCashAmtLastMonth = idCashAmtLastMonth;
		}

		public Double getIdCashBillAmt() {
			return idCashBillAmt;
		}

		public void setIdCashBillAmt(Double idCashBillAmt) {
			this.idCashBillAmt = idCashBillAmt;
		}

		public Double getIdCreditLimit() {
			return idCreditLimit;
		}

		public void setIdCreditLimit(Double idCreditLimit) {
			this.idCreditLimit = idCreditLimit;
		}

		public Double getInstallFirstPayment() {
			return installFirstPayment;
		}

		public void setInstallFirstPayment(Double installFirstPayment) {
			this.installFirstPayment = installFirstPayment;
		}

		public Double getInstallFormalityFee() {
			return installFormalityFee;
		}

		public void setInstallFormalityFee(Double installFormalityFee) {
			this.installFormalityFee = installFormalityFee;
		}

		public Double getInstallPeriodNum() {
			return installPeriodNum;
		}

		public void setInstallPeriodNum(Double installPeriodNum) {
			this.installPeriodNum = installPeriodNum;
		}

		public Double getInstallStagesPayment() {
			return installStagesPayment;
		}

		public void setInstallStagesPayment(Double installStagesPayment) {
			this.installStagesPayment = installStagesPayment;
		}

		public Double getLoyaltyOriginalPoint() {
			return loyaltyOriginalPoint;
		}

		public void setLoyaltyOriginalPoint(Double loyaltyOriginalPoint) {
			this.loyaltyOriginalPoint = loyaltyOriginalPoint;
		}

		public Double getLoyaltyPaidCreditAmt() {
			return loyaltyPaidCreditAmt;
		}

		public void setLoyaltyPaidCreditAmt(Double loyaltyPaidCreditAmt) {
			this.loyaltyPaidCreditAmt = loyaltyPaidCreditAmt;
		}

		public Double getLoyaltyRedemptionAmt() {
			return loyaltyRedemptionAmt;
		}

		public void setLoyaltyRedemptionAmt(Double loyaltyRedemptionAmt) {
			this.loyaltyRedemptionAmt = loyaltyRedemptionAmt;
		}

		public Double getLoyaltyRedemptionPoint() {
			return loyaltyRedemptionPoint;
		}

		public void setLoyaltyRedemptionPoint(Double loyaltyRedemptionPoint) {
			this.loyaltyRedemptionPoint = loyaltyRedemptionPoint;
		}

		public Double getMaxLimitAmtMonth() {
			return maxLimitAmtMonth;
		}

		public void setMaxLimitAmtMonth(Double maxLimitAmtMonth) {
			this.maxLimitAmtMonth = maxLimitAmtMonth;
		}

		public Double getMccAmtLimit() {
			return mccAmtLimit;
		}

		public void setMccAmtLimit(Double mccAmtLimit) {
			this.mccAmtLimit = mccAmtLimit;
		}

		public Double getNormalAmtLastMonth() {
			return normalAmtLastMonth;
		}

		public void setNormalAmtLastMonth(Double normalAmtLastMonth) {
			this.normalAmtLastMonth = normalAmtLastMonth;
		}

		public Double getNormalAmtMonth() {
			return normalAmtMonth;
		}

		public void setNormalAmtMonth(Double normalAmtMonth) {
			this.normalAmtMonth = normalAmtMonth;
		}

		public Double getNormalLimitAmtMonth() {
			return normalLimitAmtMonth;
		}

		public void setNormalLimitAmtMonth(Double normalLimitAmtMonth) {
			this.normalLimitAmtMonth = normalLimitAmtMonth;
		}

		public Double getTotalLimitAmtMonth() {
			return totalLimitAmtMonth;
		}

		public void setTotalLimitAmtMonth(Double totalLimitAmtMonth) {
			this.totalLimitAmtMonth = totalLimitAmtMonth;
		}

		/**
		 * @return the surchgAmt
		 */
		public String getSurchgAmt() {
			return surchgAmt;
		}

		/**
		 * @param surchgAmt the surchgAmt to set
		 */
		public void setSurchgAmt(String surchgAmt) {
			this.surchgAmt = surchgAmt;
		}

	}

	/** Creates a new instance of AuthLogReportData */
	public AuthTransReportData() {
	}

	public List getRecords() {
		return records;
	}

	public void setRecords(List records) {
		this.records = records;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getCardKind() {
		return cardKind;
	}

	public void setCardKind(String cardKind) {
		this.cardKind = cardKind;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getFileDate1() {
		return fileDate1;
	}

	public void setFileDate1(String fileDate1) {
		this.fileDate1 = fileDate1;
	}

	public String getFileDate2() {
		return fileDate2;
	}

	public void setFileDate2(String fileDate2) {
		this.fileDate2 = fileDate2;
	}

}
