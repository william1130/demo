package proj.nccc.logsearch.vo;

import java.io.Serializable;

import com.hp.nccc.iso8583.core.ISO8583VO;

public class NcccParm implements Serializable {

	private static final long serialVersionUID = 2969190880959623361L;

	/* 授權交易資料 */
	private String cardNum = "";

	private String cardExpireDate = "";

	private String transAmount = "";

	private String cvv2 = "";

	private String respCode = "";

	private String authCode = "";

	private String cancelFlag = "";

	private String foreignCard = "";
	/* M2018168 */
	private String cardType = "";

	/* 分期交易資料 */
	private String instType = "";

	/* I: 表分期交易 */
	private String divNum = "";

	private String firstAmount = "";

	private String everyAmount = "";

	private String procAmount = "";

	/* 紅利交易資料 */
	private String redeemType = "";

	/* 1 或 2 : 表紅利交易 */
	private String disCount = "";

	private String balanceFlag = "";

	private String balanceAmount = "";

	private String contPaySelf = "";

	/* EC 網路交易相關資料 */
	private String eci = "";

	private String cavv = "";

	private String xid = "";

	/* 特店相關資料 */
	private String merchantId = "";

	private String terminalId = "";

	private String acquirerId = "";

	private String merchantName = "";

	private String mccCode = "";

	private String city = "";

	// 特店是否是定期繳款交易
	private String recurringFlag = "";
	// M2018165 M2018168 特店是否是mail order或telephone order
	private String motoFlag = "";

	private String iduToken = "";

	/* 交易控制資料 */
	private String errFlag = "";

	private int anum = 0;

	// Original transaction iso object
	ISO8583VO requestObj;

	public ISO8583VO getRequestObj() {
		return requestObj;
	}

	public void setRequestObj(ISO8583VO requestObj) {
		this.requestObj = requestObj;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getCardExpireDate() {
		return cardExpireDate;
	}

	public void setCardExpireDate(String cardExpireDate) {
		this.cardExpireDate = cardExpireDate;
	}

	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getForeignCard() {
		return foreignCard;
	}

	public void setForeignCard(String foreignCard) {
		this.foreignCard = foreignCard;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getInstType() {
		return instType;
	}

	public void setInstType(String instType) {
		this.instType = instType;
	}

	public String getDivNum() {
		return divNum;
	}

	public void setDivNum(String divNum) {
		this.divNum = divNum;
	}

	public String getFirstAmount() {
		return firstAmount;
	}

	public void setFirstAmount(String firstAmount) {
		this.firstAmount = firstAmount;
	}

	public String getEveryAmount() {
		return everyAmount;
	}

	public void setEveryAmount(String everyAmount) {
		this.everyAmount = everyAmount;
	}

	public String getProcAmount() {
		return procAmount;
	}

	public void setProcAmount(String procAmount) {
		this.procAmount = procAmount;
	}

	public String getRedeemType() {
		return redeemType;
	}

	public void setRedeemType(String redeemType) {
		this.redeemType = redeemType;
	}

	public String getDisCount() {
		return disCount;
	}

	public void setDisCount(String disCount) {
		this.disCount = disCount;
	}

	public String getBalanceFlag() {
		return balanceFlag;
	}

	public void setBalanceFlag(String balanceFlag) {
		this.balanceFlag = balanceFlag;
	}

	public String getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getContPaySelf() {
		return contPaySelf;
	}

	public void setContPaySelf(String contPaySelf) {
		this.contPaySelf = contPaySelf;
	}

	public String getEci() {
		return eci;
	}

	public void setEci(String eci) {
		this.eci = eci;
	}

	public String getCavv() {
		return cavv;
	}

	public void setCavv(String cavv) {
		this.cavv = cavv;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getAcquirerId() {
		return acquirerId;
	}

	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMccCode() {
		return mccCode;
	}

	public void setMccCode(String mccCode) {
		this.mccCode = mccCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRecurringFlag() {
		return recurringFlag;
	}

	public void setRecurringFlag(String recurringFlag) {
		this.recurringFlag = recurringFlag;
	}

	public String getMotoFlag() {
		return motoFlag;
	}

	public void setMotoFlag(String motoFlag) {
		this.motoFlag = motoFlag;
	}

	public String getIduToken() {
		return iduToken;
	}

	public void setIduToken(String iduToken) {
		this.iduToken = iduToken;
	}

	public String getErrFlag() {
		return errFlag;
	}

	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}

	public int getAnum() {
		return anum;
	}

	public void setAnum(int anum) {
		this.anum = anum;
	}

	public void clearValue() {

	}
}
// end of class
