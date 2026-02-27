/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: CreditInfoArg.java,v 1.2 2017/04/26 05:57:29 leered Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.vo;

import org.apache.commons.beanutils.BeanUtils;

import com.edstw.bean.EdsBeanUtil;
import com.edstw.lang.DoubleString;

import proj.nccc.logsearch.persist.CardData;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.2 $
 */
public class CreditInfoArg extends CardData implements ProjPersistableArg, PagingArg {
	private static final long serialVersionUID = 1L;
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;

	private String fromTempLimit;

	private String icFlag;

	private String currentCode;

	private String expireDate;

	private String cardClass;

	private String cardHolderId;

	// 累積交易次數-日
	private DoubleString totalCntDay;

	// 累積交易次數-月
	private DoubleString totalCntMonth;

	// 累積交易金額-日
	private DoubleString totalAmtDay;

	// 累積交易金額-月
	private DoubleString totalAmtMonth;

	// 20081126 , Normal 需包括Foreign
	private DoubleString normalForeignCntDay;

	private DoubleString normalForeignCntMonth;

	private DoubleString normalForeignAmtDay;

	private DoubleString normalForeignAmtMonth;

	// 紅利點數
	private DoubleString lastLoyaltyPoint;

	// 公司卡別
	private String corpType;

	// ID本月累積總消費金額(含預借現金)
	private DoubleString idAmtCurrentMonth;

	// ID上月累積總消費金額(含預借現金)idAmtCurrentMonth+idAmtLastMonth
	private DoubleString idAmtCurrentLastMonth;

	// ID帳務金額
	private DoubleString idBillAmt;

	// ID最大總月限額
	private DoubleString idMaxLimitAmtMonth;

	// ID本月累積預借現金金額
	private DoubleString idCashAmtCurrentMonth;

	// ID預借現金本月+上月累積消費金額
	private DoubleString idCashAmtCurrentLastMonth;

	// ID預借現金帳務金額
	private DoubleString idCashBillAmt;

	// ID預先授權金額
	private DoubleString idPreAuthAmt;

	// ID 信用餘額
	private DoubleString idCreditLimit;

	// ID 預借信用餘額
	private DoubleString idCashCreditLimit;

	// Corp 本月累積
	private DoubleString corpNormalAmtMonth;

	// 上月
	private DoubleString corpNormalAmtLastMonth;

	// 本月+上月
	private DoubleString corpNormalAmtCurrentLastMonth;

	// 帳務金額
	private DoubleString corpBillAmt;

	private DoubleString corpCashAmtMonth;

	private DoubleString corpCashAmtLastMonth;

	// 一般限額
	private DoubleString corpNormalLimitAmtMonth;

	// 預借限額
	private DoubleString corpCashLimitAmtMonth;

	// 總限額
	private DoubleString corpTotalLimitAmtMonth;

	// 公司戶預先授權金額
	private DoubleString corpPreAuthAmt;

	// 公司
	private DoubleString corpCreditLimit;

	private DoubleString corpCashCreditLimit;

	// 臨時額度之purgeDate_20141226併M2014231
	private String purgeDate;

	public void buildFromAuthPersistable(CardData obj) {
		EdsBeanUtil.getInstance().copyProperties(this, obj);
	}

	public DisplayTagPagingInfos getPagingInfo() {

		if (pagingInfo == null)
			pagingInfo = new DisplayTagPagingInfos();
		return pagingInfo;
	}

	public String getCardClass() {

		return cardClass;
	}

	public void setCardClass(String cardClass) {

		this.cardClass = cardClass;
	}

	public String getCurrentCode() {

		return currentCode;
	}

	public void setCurrentCode(String currentCode) {

		this.currentCode = currentCode;
	}

	public String getExpireDate() {

		return expireDate;
	}

	public void setExpireDate(String expireDate) {

		this.expireDate = expireDate;
	}

	public String getIcFlag() {

		return icFlag;
	}

	public void setIcFlag(String icFlag) {

		this.icFlag = icFlag;
	}

	public DoubleString getLastLoyaltyPoint() {

		return lastLoyaltyPoint;
	}

	public void setLastLoyaltyPoint(DoubleString lastLoyaltyPoint) {

		this.lastLoyaltyPoint = lastLoyaltyPoint;
	}

	public DoubleString getCorpCashLimitAmtMonth() {

		return corpCashLimitAmtMonth;
	}

	public void setCorpCashLimitAmtMonth(DoubleString corpCashLimitAmtMonth) {

		this.corpCashLimitAmtMonth = corpCashLimitAmtMonth;
	}

	public DoubleString getCorpNormalLimitAmtMonth() {

		return corpNormalLimitAmtMonth;
	}

	public void setCorpNormalLimitAmtMonth(DoubleString corpNormalLimitAmtMonth) {

		this.corpNormalLimitAmtMonth = corpNormalLimitAmtMonth;
	}

	public DoubleString getCorpBillAmt() {

		return corpBillAmt;
	}

	public void setCorpBillAmt(DoubleString corpBillAmt) {

		this.corpBillAmt = corpBillAmt;
	}

	public DoubleString getCorpNormalAmtCurrentLastMonth() {

		return corpNormalAmtCurrentLastMonth;
	}

	public void setCorpNormalAmtCurrentLastMonth(DoubleString corpNormalAmtCurrentLastMonth) {

		this.corpNormalAmtCurrentLastMonth = corpNormalAmtCurrentLastMonth;
	}

	public DoubleString getCorpNormalAmtMonth() {

		return corpNormalAmtMonth;
	}

	public void setCorpNormalAmtMonth(DoubleString corpNormalAmtMonth) {

		this.corpNormalAmtMonth = corpNormalAmtMonth;
	}

	public DoubleString getCorpTotalLimitAmtMonth() {

		return corpTotalLimitAmtMonth;
	}

	public void setCorpTotalLimitAmtMonth(DoubleString corpTotalLimitAmtMonth) {

		this.corpTotalLimitAmtMonth = corpTotalLimitAmtMonth;
	}

	public DoubleString getIdAmtCurrentLastMonth() {

		return idAmtCurrentLastMonth;
	}

	public void setIdAmtCurrentLastMonth(DoubleString idAmtCurrentLastMonth) {

		this.idAmtCurrentLastMonth = idAmtCurrentLastMonth;
	}

	public DoubleString getIdAmtCurrentMonth() {

		return idAmtCurrentMonth;
	}

	public void setIdAmtCurrentMonth(DoubleString idAmtCurrentMonth) {

		this.idAmtCurrentMonth = idAmtCurrentMonth;
	}

	public DoubleString getIdBillAmt() {

		return idBillAmt;
	}

	public void setIdBillAmt(DoubleString idBillAmt) {

		this.idBillAmt = idBillAmt;
	}

	public DoubleString getIdCashAmtCurrentLastMonth() {

		return idCashAmtCurrentLastMonth;
	}

	public void setIdCashAmtCurrentLastMonth(DoubleString idCashAmtCurrentLastMonth) {

		this.idCashAmtCurrentLastMonth = idCashAmtCurrentLastMonth;
	}

	public DoubleString getIdCashAmtCurrentMonth() {

		return idCashAmtCurrentMonth;
	}

	public void setIdCashAmtCurrentMonth(DoubleString idCashAmtCurrentMonth) {

		this.idCashAmtCurrentMonth = idCashAmtCurrentMonth;
	}

	public DoubleString getIdCashBillAmt() {

		return idCashBillAmt;
	}

	public void setIdCashBillAmt(DoubleString idCashBillAmt) {

		this.idCashBillAmt = idCashBillAmt;
	}

	public DoubleString getIdMaxLimitAmtMonth() {

		return idMaxLimitAmtMonth;
	}

	public void setIdMaxLimitAmtMonth(DoubleString idMaxLimitAmtMonth) {

		this.idMaxLimitAmtMonth = idMaxLimitAmtMonth;
	}

	public DoubleString getTotalCntDay() {

		return new DoubleString((getNormalCntDay() == null ? 0 : getNormalCntDay().doubleValue())
				+ (getCashCntDay() == null ? 0 : getCashCntDay().doubleValue())
				+ (getForeignCntDay() == null ? 0 : getForeignCntDay().doubleValue()));
	}

	public void setTotalCntDay(DoubleString totalCntDay) {

		this.totalCntDay = totalCntDay;
	}

	public DoubleString getTotalCntMonth() {

		return new DoubleString((getNormalCntMonth() == null ? 0 : getNormalCntMonth().doubleValue())
				+ (getCashCntMonth() == null ? 0 : getCashCntMonth().doubleValue())
				+ (getForeignCntMonth() == null ? 0 : getForeignCntMonth().doubleValue()));
	}

	public void setTotalCntMonth(DoubleString totalCntMonth) {

		this.totalCntMonth = totalCntMonth;
	}

	public DoubleString getTotalAmtDay() {

		return new DoubleString((getNormalAmtDay() == null ? 0 : getNormalAmtDay().doubleValue())
				+ (getCashAmtDay() == null ? 0 : getCashAmtDay().doubleValue())
				+ (getForeignAmtDay() == null ? 0 : getForeignAmtDay().doubleValue()));
	}

	public void setTotalAmtDay(DoubleString totalAmtDay) {

		this.totalAmtDay = totalAmtDay;
	}

	public DoubleString getTotalAmtMonth() {

		return new DoubleString((getNormalAmtMonth() == null ? 0 : getNormalAmtMonth().doubleValue())
				+ (getCashAmtMonth() == null ? 0 : getCashAmtMonth().doubleValue())
				+ (getForeignAmtMonth() == null ? 0 : getForeignAmtMonth().doubleValue()));
	}

	public void setTotalAmtMonth(DoubleString totalAmtMonth) {

		this.totalAmtMonth = totalAmtMonth;
	}

	public void setPagingInfo(DisplayTagPagingInfos pagingInfo) {

		this.pagingInfo = pagingInfo;
	}

	public String getCorpType() {

		return corpType;
	}

	public void setCorpType(String corpType) {

		this.corpType = corpType;
	}

	public String getCardHolderId() {

		return cardHolderId;
	}

	public void setCardHolderId(String cardHolderId) {

		this.cardHolderId = cardHolderId;
	}

	public DoubleString getCorpCashAmtLastMonth() {

		return corpCashAmtLastMonth;
	}

	public void setCorpCashAmtLastMonth(DoubleString corpCashAmtLastMonth) {

		this.corpCashAmtLastMonth = corpCashAmtLastMonth;
	}

	public DoubleString getCorpCashAmtMonth() {

		return corpCashAmtMonth;
	}

	public void setCorpCashAmtMonth(DoubleString corpCashAmtMonth) {

		this.corpCashAmtMonth = corpCashAmtMonth;
	}

	public DoubleString getCorpNormalAmtLastMonth() {

		return corpNormalAmtLastMonth;
	}

	public void setCorpNormalAmtLastMonth(DoubleString corpNormalAmtLastMonth) {

		this.corpNormalAmtLastMonth = corpNormalAmtLastMonth;
	}

	public String getFromTempLimit() {

		return fromTempLimit;
	}

	public void setFromTempLimit(String fromTempLimit) {

		this.fromTempLimit = fromTempLimit;
	}

	public DoubleString getCorpCashCreditLimit() {

		try {
			double c0 = this.getCorpCashLimitAmtMonth().doubleValue();
			// double c1 = this.getCorpBillAmt().doubleValue();
			double c2 = this.getCorpCashAmtMonth().doubleValue();
			double c3 = this.getCorpCashAmtLastMonth().doubleValue();
			double c = c0 - c2 - c3;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	public DoubleString getCorpCreditLimit() {

		try {
			double c0 = this.getCorpTotalLimitAmtMonth().doubleValue();
			double c1 = this.getCorpBillAmt().doubleValue();
			// double c2=this.getCorpNormalAmtMonth().doubleValue();
			double c3 = this.getCorpNormalAmtCurrentLastMonth().doubleValue();
			double c = c0 - c1 - c3;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	public DoubleString getIdCreditLimit() {

		try {
			double c0 = this.getIdMaxLimitAmtMonth().doubleValue();
			double c1 = this.getIdBillAmt().doubleValue();
			// double c2=this.getIdAmtCurrentMonth().doubleValue();
			double c3 = this.getIdAmtCurrentLastMonth().doubleValue();
			double c4 = this.getIdPreAuthAmt().doubleValue();
			double c = c0 - c1 - c3 - c4;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	public DoubleString getIdCashCreditLimit() {

		try {
			double c0 = this.getCashLimitAmtMonth().doubleValue();
			double c1 = this.getIdCashBillAmt().doubleValue();
			// double c2=this.getIdAmtCurrentMonth().doubleValue();
			double c3 = this.getIdCashAmtCurrentLastMonth().doubleValue();
			double c = c0 - c1 - c3;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	public DoubleString getIdPreAuthAmt() {

		return idPreAuthAmt;
	}

	public void setIdPreAuthAmt(DoubleString idPreAuthAmt) {

		this.idPreAuthAmt = idPreAuthAmt;
	}

	public DoubleString getCorpPreAuthAmt() {

		return corpPreAuthAmt;
	}

	public void setCorpPreAuthAmt(DoubleString corpPreAuthAmt) {

		this.corpPreAuthAmt = corpPreAuthAmt;
	}

	public DoubleString getNormalForeignAmtDay() {

		try {
			double c0 = this.getNormalAmtDay().doubleValue();
			double c1 = this.getForeignAmtDay().doubleValue();
			double c = c0 + c1;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	public DoubleString getNormalForeignAmtMonth() {

		try {
			double c0 = this.getNormalAmtMonth().doubleValue();
			double c1 = this.getForeignAmtMonth().doubleValue();
			double c = c0 + c1;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	public DoubleString getNormalForeignCntDay() {

		try {
			double c0 = this.getNormalCntDay().doubleValue();
			double c1 = this.getForeignCntDay().doubleValue();
			double c = c0 + c1;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	public DoubleString getNormalForeignCntMonth() {

		try {
			double c0 = this.getNormalCntMonth().doubleValue();
			double c1 = this.getForeignCntMonth().doubleValue();
			double c = c0 + c1;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	// M98085國內臨時額度-增加臨時餘額
	public DoubleString getTempCreditLimit() {

		try {
			double c0 = this.getTotalLimitAmtMonth().doubleValue();
			double c1 = this.getIdAmtCurrentMonth().doubleValue();
			double c2 = this.getIdAmtCurrentLastMonth().doubleValue();
			double c3 = this.getIdBillAmt().doubleValue();
			double c = c0 - c2 - c3;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	// 20100107 Jennifer 卡片餘額應該要是卡片總消費月限額-本月-上月 若帳務金額為負才要減帳務金額
	public DoubleString getTotalCreditLimit() {

		try {
			double c0 = this.getTotalLimitAmtMonth().doubleValue();
			// double c1=this.getIdAmtCurrentMonth().doubleValue();
			double c2 = this.getIdAmtCurrentLastMonth().doubleValue();
			double c3 = this.getIdBillAmt().doubleValue();
			double c = c0 - c2;
			if (c3 < 0)
				c = c - c3;
			return new DoubleString(c);
		} catch (Exception x) {
			return new DoubleString(0);
		}
	}

	// 臨時額度之purgeDate_20141226併M2014231
	public String getPurgeDate() {

		return purgeDate;
	}

	// 臨時額度之purgeDate_20141226併M2014231
	public void setPurgeDate(String purgeDate) {

		this.purgeDate = purgeDate;
	}

	@Override
	public void buildFromProjPersistable(EmvProjPersistable c) throws Exception {
		BeanUtils.copyProperties(this, c);
	}

	/**
	 * @return the uiLogAction
	 */
	public String getUiLogAction() {
		return uiLogAction;
	}

	/**
	 * @param uiLogAction the uiLogAction to set
	 */
	public void setUiLogAction(String uiLogAction) {
		this.uiLogAction = uiLogAction;
	}

	/**
	 * @return the uiLogFunctionName
	 */
	public String getUiLogFunctionName() {
		return uiLogFunctionName;
	}

	/**
	 * @param uiLogFunctionName the uiLogFunctionName to set
	 */
	public void setUiLogFunctionName(String uiLogFunctionName) {
		this.uiLogFunctionName = uiLogFunctionName;
	}

	/**
	 * @return the uiLogOther
	 */
	public String getUiLogOther() {
		return uiLogOther;
	}

	/**
	 * @param uiLogOther the uiLogOther to set
	 */
	public void setUiLogOther(String uiLogOther) {
		this.uiLogOther = uiLogOther;
	}
}
