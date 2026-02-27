/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 * 
 * ==============================================================================================
 * $Id: ManualAuthorizerArg.java,v 1.6 2019/11/25 05:48:19 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.vo;

import org.apache.commons.beanutils.BeanUtils;

import com.edstw.bean.EdsBeanUtil;

import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.persist.AuthLogData;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

/**
 * 
 * @author APAC\czrm4t $
 * @version $Revision: 1.6 $
 */
public class ManualAuthorizerArg extends AuthLogData implements ProjPersistableArg, PagingArg {
	private static final long serialVersionUID = 1L;
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;
	private DisplayTagPagingInfos pagingInfo;

	// (desc="卡號1")
	private String cardNo1;

	// (desc="卡號2", mask="cardNum")
	private String cardNo2;

	// (desc="卡號3", mask="cardNum")
	private String cardNo3;

	// (desc="卡號4")
	private String cardNo4;

	/* M2010043 CUP發卡 */
	// (desc="卡號5")
	private String cardNo5;

	// (desc="CVC2")
	private String cvc2;

	// (desc="補登交易")
	private String addFlag = "N";

	private String authManuEntry;

	private String authorizeMemo;

	private String cardBankNo;

	private String cardBankNo2;

	private String memberBank;

	private String cardBankName;

	private String issueInfoTel;

	private String issueInfoTel1;

	private String getAuthCode;

	private String forceAuth;

	private String actionStep;

	private String recurringFlag;

	private String acqName;

	private String pureCupCard;

	private String qrMcht;

	public String getQrMcht() {
		return qrMcht;
	}

	public void setQrMcht(String qrMcht) {
		this.qrMcht = qrMcht;
	}

	public String realMerchantNo;

	public String getRealMerchantNo() {
		return realMerchantNo;
	}

	public void setRealMerchantNo(String realMerchantNo) {
		this.realMerchantNo = realMerchantNo;
	}

	private boolean tokenFlag = false;

	/* M2016073 TSP專案 */
	// 特店資訊
	private MerchantVO merchantVO;

	// 發卡資訊
	private CreditInfoArg creditInfoVO;

	public void buildFromAuthPersistable(AuthLogData obj) {

		EdsBeanUtil.getInstance().copyProperties(this, obj);
	}

	public ManualAuthorizerArg() {

	}

	public ManualAuthorizerArg(String str) {

		if (str.equals("R")) {
			this.setProcessDate(MyDateUtil.getSysDateTime(MyDateUtil.YYYYMMDD));
			this.setProcessTime(MyDateUtil.getSysDateTime(MyDateUtil.HHMMSS));
			this.setAddFlag("N");
			this.setGetAuthCode("Y");
			this.setForceAuth("N");
			this.setPureCupCard("N");
		}
	}

	public String getCardNo1() {

		return cardNo1;
	}

	public void setCardNo1(String cardNo1) {

		this.cardNo1 = cardNo1;
	}

	public String getCardNo2() {

		return cardNo2;
	}

	public void setCardNo2(String cardNo2) {

		this.cardNo2 = cardNo2;
	}

	public String getCardNo3() {

		return cardNo3;
	}

	public void setCardNo3(String cardNo3) {

		this.cardNo3 = cardNo3;
	}

	public String getCardNo4() {

		return cardNo4;
	}

	public void setCardNo4(String cardNo4) {

		this.cardNo4 = cardNo4;
	}

	public String getCardNo5() {

		return cardNo5;
	}

	public void setCardNo5(String cardNo5) {

		this.cardNo5 = cardNo5;
	}

	public String getCvc2() {

		return cvc2;
	}

	public void setCvc2(String cvc2) {

		this.cvc2 = cvc2;
	}

	public String getAddFlag() {

		return addFlag;
	}

	public void setAddFlag(String addFlag) {

		this.addFlag = addFlag;
	}

	public MerchantVO getMerchantVO() {

		return merchantVO;
	}

	public void setMerchantVO(MerchantVO merchantVO) {

		this.merchantVO = merchantVO;
	}

	public CreditInfoArg getCreditInfoVO() {

		return creditInfoVO;
	}

	public void setCreditInfoVO(CreditInfoArg creditInfoVO) {

		this.creditInfoVO = creditInfoVO;
	}

	public String getAuthManuEntry() {

		return authManuEntry;
	}

	public void setAuthManuEntry(String authManuEntry) {

		this.authManuEntry = authManuEntry;
	}

	public String getAuthorizeMemo() {

		return authorizeMemo;
	}

	public void setAuthorizeMemo(String authorizeMemo) {

		this.authorizeMemo = authorizeMemo;
	}

	public String getCardBankNo() {

		return cardBankNo;
	}

	public void setCardBankNo(String cardBankNo) {

		this.cardBankNo = cardBankNo;
	}

	public String getCardBankNo2() {

		return cardBankNo2;
	}

	public void setCardBankNo2(String cardBankNo2) {

		this.cardBankNo2 = cardBankNo2;
	}

	public String getMemberBank() {

		return memberBank;
	}

	public void setMemberBank(String memberBank) {

		this.memberBank = memberBank;
	}

	public String getCardBankName() {

		return cardBankName;
	}

	public void setCardBankName(String cardBankName) {

		this.cardBankName = cardBankName;
	}

	public String getIssueInfoTel() {

		return issueInfoTel;
	}

	public void setIssueInfoTel(String issueInfoTel) {

		this.issueInfoTel = issueInfoTel;
	}

	public String getIssueInfoTel1() {

		return issueInfoTel1;
	}

	public void setIssueInfoTel1(String issueInfoTel1) {

		this.issueInfoTel1 = issueInfoTel1;
	}

	public String getForceAuth() {

		return forceAuth;
	}

	public void setForceAuth(String forceAuth) {

		this.forceAuth = forceAuth;
	}

	public String getGetAuthCode() {

		return getAuthCode;
	}

	public void setGetAuthCode(String getAuthCode) {

		this.getAuthCode = getAuthCode;
	}

	public String getActionStep() {

		return actionStep;
	}

	public void setActionStep(String actionStep) {

		this.actionStep = actionStep;
	}

	public String getRecurringFlag() {

		return recurringFlag;
	}

	public void setRecurringFlag(String recurringFlag) {

		this.recurringFlag = recurringFlag;
	}

	public String getAcqName() {

		return acqName;
	}

	public String getPureCupCard() {

		return pureCupCard;
	}

	public void setAcqName(String acqName) {

		this.acqName = acqName;
	}

	public void setPureCupCard(String pureCupCard) {

		this.pureCupCard = pureCupCard;
	}

	/* M2016073 TSP專案 */
	public boolean getTokenFlag() {

		return tokenFlag;
	}

	/* M2016073 TSP專案 */
	public void setTokenFlag(boolean tokenFlag) {

		this.tokenFlag = tokenFlag;
	}

	public void buildFromProjPersistable(EmvProjPersistable c) throws Exception {
		BeanUtils.copyProperties(this, c);
	}

	public DisplayTagPagingInfos getPagingInfo() {
		return pagingInfo;
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
