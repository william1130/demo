/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: CardData.java,v 1.4 2019/11/25 05:48:19 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import java.util.Date;

import com.edstw.lang.DateString;
import com.edstw.lang.DoubleString;
import com.edstw.nccc.sql.log.ValueImage;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.4 $
 */
public class CardData extends AbstractProjPersistable {

	private static final long serialVersionUID = -3900369174157621791L;

	// (desc="卡號", mask="cardNum")
	private String cardNo;

	private String bankNo;

	private String lastPurchaseDate;

	private String lastApproveNo;

	private DoubleString billAmt;

	private DoubleString normalLimitAmtMonth;

	private DoubleString normalLimitAmtDay;

	private DoubleString normalLimitAmtTime;

	private DoubleString normalLimitCntMonth;

	private DoubleString normalLimitCntDay;

	private DoubleString normalAmtLastMonth;

	private DoubleString normalAmtMonth;

	private DoubleString normalAmtDay;

	private DoubleString normalCntMonth;

	private DoubleString normalCntDay;

	private DoubleString authAmtMonth;

	private DoubleString authAmtDay;

	private DoubleString authCntMonth;

	private DoubleString authCntDay;

	private DoubleString authRejectCntMonth;

	private DoubleString foreignAmtMonth;

	private DoubleString foreignAmtDay;

	private DoubleString foreignCntMonth;

	private DoubleString foreignCntDay;

	private DoubleString cashLimitAmtMonth;

	private DoubleString cashLimitAmtDay;

	private DoubleString cashLimitAmtTime;

	private DoubleString cashLimitCntMonth;

	private DoubleString cashLimitCntDay;

	private DoubleString cashBillAmt;

	private DoubleString cashAmtLastMonth;

	private DoubleString cashAmtMonth;

	private DoubleString cashAmtDay;

	private DoubleString cashCntMonth;

	private DoubleString cashCntDay;

	private DoubleString totalLimitAmtMonth;

	private DoubleString totalLimitAmtDay;

	private DoubleString totalLimitAmtTime;

	private DoubleString totalLimitCntMonth;

	private DoubleString totalLimitCntDay;

	private DoubleString preAuthAmt;

	private DateString fileDate;

	private String modProgramId;

	private Date updateDate;

	private String updateId;

	private String updateName;

	private String updateDeptName;

	private Date confirmDate;

	private String confirmId;

	private String confirmName;

	private String confirmDeptName;

	private String confirmFlag;

	private String remark;

	private DoubleString totalCreditLimit;

	private DoubleString creditLimit;

	private DoubleString cashCreditLimit;

	public CardData() {

	}

	public int hashCode() {

		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((cardNo == null) ? 0 : cardNo.hashCode());
		return result;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CardData other = (CardData) obj;
		if (cardNo == null) {
			if (other.cardNo != null)
				return false;
		} else if (!cardNo.equals(other.cardNo))
			return false;
		return true;
	}

	public String getId() {

		return getCardNo();
	}

	public String getCardNo() {

		return cardNo;
	}

	public void setCardNo(String cardNo) {

		this.cardNo = cardNo;
	}

	public String getBankNo() {

		return bankNo;
	}

	public void setBankNo(String bankNo) {

		this.bankNo = bankNo;
		this.modified();
	}

	public String getLastPurchaseDate() {

		return lastPurchaseDate;
	}

	public void setLastPurchaseDate(String lastPurchaseDate) {

		this.lastPurchaseDate = lastPurchaseDate;
		this.modified();
	}

	public String getLastApproveNo() {

		return lastApproveNo;
	}

	public void setLastApproveNo(String lastApproveNo) {

		this.lastApproveNo = lastApproveNo;
		this.modified();
	}

	public DoubleString getBillAmt() {

		return billAmt;
	}

	public void setBillAmt(DoubleString billAmt) {

		this.billAmt = billAmt;
		this.modified();
	}

	public DoubleString getNormalLimitAmtMonth() {

		return normalLimitAmtMonth;
	}

	public void setNormalLimitAmtMonth(DoubleString normalLimitAmtMonth) {

		this.normalLimitAmtMonth = normalLimitAmtMonth;
		this.modified();
	}

	public DoubleString getNormalLimitAmtDay() {

		return normalLimitAmtDay;
	}

	public void setNormalLimitAmtDay(DoubleString normalLimitAmtDay) {

		this.normalLimitAmtDay = normalLimitAmtDay;
		this.modified();
	}

	public DoubleString getNormalLimitAmtTime() {

		return normalLimitAmtTime;
	}

	public void setNormalLimitAmtTime(DoubleString normalLimitAmtTime) {

		this.normalLimitAmtTime = normalLimitAmtTime;
		this.modified();
	}

	public DoubleString getNormalLimitCntMonth() {

		return normalLimitCntMonth;
	}

	public void setNormalLimitCntMonth(DoubleString normalLimitCntMonth) {

		this.normalLimitCntMonth = normalLimitCntMonth;
		this.modified();
	}

	public DoubleString getNormalLimitCntDay() {

		return normalLimitCntDay;
	}

	public void setNormalLimitCntDay(DoubleString normalLimitCntDay) {

		this.normalLimitCntDay = normalLimitCntDay;
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

	public DoubleString getNormalAmtDay() {

		return normalAmtDay;
	}

	public void setNormalAmtDay(DoubleString normalAmtDay) {

		this.normalAmtDay = normalAmtDay;
		this.modified();
	}

	public DoubleString getNormalCntMonth() {

		return normalCntMonth;
	}

	public void setNormalCntMonth(DoubleString normalCntMonth) {

		this.normalCntMonth = normalCntMonth;
		this.modified();
	}

	public DoubleString getNormalCntDay() {

		return normalCntDay;
	}

	public void setNormalCntDay(DoubleString normalCntDay) {

		this.normalCntDay = normalCntDay;
		this.modified();
	}

	public DoubleString getAuthAmtMonth() {

		return authAmtMonth;
	}

	public void setAuthAmtMonth(DoubleString authAmtMonth) {

		this.authAmtMonth = authAmtMonth;
		this.modified();
	}

	public DoubleString getAuthAmtDay() {

		return authAmtDay;
	}

	public void setAuthAmtDay(DoubleString authAmtDay) {

		this.authAmtDay = authAmtDay;
		this.modified();
	}

	public DoubleString getAuthCntMonth() {

		return authCntMonth;
	}

	public void setAuthCntMonth(DoubleString authCntMonth) {

		this.authCntMonth = authCntMonth;
		this.modified();
	}

	public DoubleString getAuthCntDay() {

		return authCntDay;
	}

	public void setAuthCntDay(DoubleString authCntDay) {

		this.authCntDay = authCntDay;
		this.modified();
	}

	public DoubleString getAuthRejectCntMonth() {

		return authRejectCntMonth;
	}

	public void setAuthRejectCntMonth(DoubleString authRejectCntMonth) {

		this.authRejectCntMonth = authRejectCntMonth;
		this.modified();
	}

	public DoubleString getForeignAmtMonth() {

		return foreignAmtMonth;
	}

	public void setForeignAmtMonth(DoubleString foreignAmtMonth) {

		this.foreignAmtMonth = foreignAmtMonth;
		this.modified();
	}

	public DoubleString getForeignAmtDay() {

		return foreignAmtDay;
	}

	public void setForeignAmtDay(DoubleString foreignAmtDay) {

		this.foreignAmtDay = foreignAmtDay;
		this.modified();
	}

	public DoubleString getForeignCntMonth() {

		return foreignCntMonth;
	}

	public void setForeignCntMonth(DoubleString foreignCntMonth) {

		this.foreignCntMonth = foreignCntMonth;
		this.modified();
	}

	public DoubleString getForeignCntDay() {

		return foreignCntDay;
	}

	public void setForeignCntDay(DoubleString foreignCntDay) {

		this.foreignCntDay = foreignCntDay;
		this.modified();
	}

	public DoubleString getCashLimitAmtMonth() {

		return cashLimitAmtMonth;
	}

	public void setCashLimitAmtMonth(DoubleString cashLimitAmtMonth) {

		this.cashLimitAmtMonth = cashLimitAmtMonth;
		this.modified();
	}

	public DoubleString getCashLimitAmtDay() {

		return cashLimitAmtDay;
	}

	public void setCashLimitAmtDay(DoubleString cashLimitAmtDay) {

		this.cashLimitAmtDay = cashLimitAmtDay;
		this.modified();
	}

	public DoubleString getCashLimitAmtTime() {

		return cashLimitAmtTime;
	}

	public void setCashLimitAmtTime(DoubleString cashLimitAmtTime) {

		this.cashLimitAmtTime = cashLimitAmtTime;
		this.modified();
	}

	public DoubleString getCashLimitCntMonth() {

		return cashLimitCntMonth;
	}

	public void setCashLimitCntMonth(DoubleString cashLimitCntMonth) {

		this.cashLimitCntMonth = cashLimitCntMonth;
		this.modified();
	}

	public DoubleString getCashLimitCntDay() {

		return cashLimitCntDay;
	}

	public void setCashLimitCntDay(DoubleString cashLimitCntDay) {

		this.cashLimitCntDay = cashLimitCntDay;
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

	public DoubleString getCashAmtDay() {

		return cashAmtDay;
	}

	public void setCashAmtDay(DoubleString cashAmtDay) {

		this.cashAmtDay = cashAmtDay;
		this.modified();
	}

	public DoubleString getCashCntMonth() {

		return cashCntMonth;
	}

	public void setCashCntMonth(DoubleString cashCntMonth) {

		this.cashCntMonth = cashCntMonth;
		this.modified();
	}

	public DoubleString getCashCntDay() {

		return cashCntDay;
	}

	public void setCashCntDay(DoubleString cashCntDay) {

		this.cashCntDay = cashCntDay;
		this.modified();
	}

	public DoubleString getTotalLimitAmtMonth() {

		return totalLimitAmtMonth;
	}

	public void setTotalLimitAmtMonth(DoubleString totalLimitAmtMonth) {

		this.totalLimitAmtMonth = totalLimitAmtMonth;
		this.modified();
	}

	public DoubleString getTotalLimitAmtDay() {

		return totalLimitAmtDay;
	}

	public void setTotalLimitAmtDay(DoubleString totalLimitAmtDay) {

		this.totalLimitAmtDay = totalLimitAmtDay;
		this.modified();
	}

	public DoubleString getTotalLimitAmtTime() {

		return totalLimitAmtTime;
	}

	public void setTotalLimitAmtTime(DoubleString totalLimitAmtTime) {

		this.totalLimitAmtTime = totalLimitAmtTime;
		this.modified();
	}

	public DoubleString getTotalLimitCntMonth() {

		return totalLimitCntMonth;
	}

	public void setTotalLimitCntMonth(DoubleString totalLimitCntMonth) {

		this.totalLimitCntMonth = totalLimitCntMonth;
		this.modified();
	}

	public DoubleString getTotalLimitCntDay() {

		return totalLimitCntDay;
	}

	public void setTotalLimitCntDay(DoubleString totalLimitCntDay) {

		this.totalLimitCntDay = totalLimitCntDay;
		this.modified();
	}

	public DoubleString getPreAuthAmt() {

		return preAuthAmt;
	}

	public void setPreAuthAmt(DoubleString preAuthAmt) {

		this.preAuthAmt = preAuthAmt;
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

	public Date getUpdateDate() {

		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {

		this.updateDate = updateDate;
		this.modified();
	}

	public String getUpdateId() {

		return updateId;
	}

	public void setUpdateId(String updateId) {

		this.updateId = updateId;
		this.modified();
	}

	public String getUpdateName() {

		return updateName;
	}

	public void setUpdateName(String updateName) {

		this.updateName = updateName;
		this.modified();
	}

	public String getUpdateDeptName() {

		return updateDeptName;
	}

	public void setUpdateDeptName(String updateDeptName) {

		this.updateDeptName = updateDeptName;
		this.modified();
	}

	public Date getConfirmDate() {

		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {

		this.confirmDate = confirmDate;
		this.modified();
	}

	public String getConfirmId() {

		return confirmId;
	}

	public void setConfirmId(String confirmId) {

		this.confirmId = confirmId;
		this.modified();
	}

	public String getConfirmName() {

		return confirmName;
	}

	public void setConfirmName(String confirmName) {

		this.confirmName = confirmName;
		this.modified();
	}

	public String getConfirmDeptName() {

		return confirmDeptName;
	}

	public void setConfirmDeptName(String confirmDeptName) {

		this.confirmDeptName = confirmDeptName;
		this.modified();
	}

	public String getConfirmFlag() {

		return confirmFlag;
	}

	public void setConfirmFlag(String confirmFlag) {

		this.confirmFlag = confirmFlag;
		this.modified();
	}

	public String getRemark() {

		return remark;
	}

	public void setRemark(String remark) {

		this.remark = remark;
		this.modified();
	}

	// 卡片預借現金餘額
	public DoubleString getCashCreditLimit() {

		double c0 = this.getCashLimitAmtMonth().doubleValue();
		double c1 = this.getCashBillAmt().doubleValue();
		double c2 = this.getCashAmtMonth().doubleValue();
		double c3 = this.getCashAmtLastMonth().doubleValue();
		double c = c0 - c1 - c2 - c3;
		return new DoubleString(c);
	}

	// 卡片一般餘額
	public DoubleString getCreditLimit() {

		double c0 = this.getNormalLimitAmtMonth().doubleValue();
		double c2 = this.getNormalAmtMonth().doubleValue();
		double c3 = this.getNormalAmtLastMonth().doubleValue();
		double c = c0 - c2 - c3;
		return new DoubleString(c);
	}
	// 卡片總餘額
	/*
	 * public DoubleString getTotalCreditLimit() { double
	 * c0=this.getTotalLimitAmtMonth().doubleValue(); double
	 * c1=this.getBillAmt().doubleValue(); double
	 * c2=this.getNormalAmtMonth().doubleValue(); double
	 * c3=this.getNormalAmtLastMonth().doubleValue(); double
	 * c4=this.getCashAmtMonth().doubleValue(); double
	 * c5=this.getCashAmtLastMonth().doubleValue(); double
	 * c6=this.getForeignAmtMonth().doubleValue(); double c=c0-c2-c3-c4-c5-c6; if
	 * (c1 < 0) c=c-c1; return new DoubleString(c);
	 * 
	 * }
	 */

	@Override
	public ValueImage createValueImage() {
		return new ValueImage();
	}

}
