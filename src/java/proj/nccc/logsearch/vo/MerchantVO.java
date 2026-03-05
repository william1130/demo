/*
 * MerchantVO.java
 * 
 * Created on 2008年4月29日, 上午 11:03
 * ==============================================================================================
 * $Id: MerchantVO.java,v 1.3 2019/10/08 11:24:46 \jjih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.vo;

import com.edstw.lang.LongString;

/**
 * 
 * @author Stephen Lin $
 * @version $Revision: 1.3 $
 */
public class MerchantVO {

	private String mccCode;

	private String mchtNo;

	private String mchtName;

	private String mchtEngName;

	private String engCityName;

	private String industryCode;

	private String currentCode;

	private String telNo1;

	private String articleClass;

	private String trustFileDate;

	private String fileDate;

	private String zipCode3;

	private String zipCode2;

	private String chinZipCity;

	private String chinAddr;

	private String Manager;

	private String imprinterCnt;

	private String belongBank;

	private String bizType003Count;

	private String bizType002Count;

	private String mchtType;

	private String riskFileDate;

	private String contractDateByU;

	private String contractDateByA;

	// 合約種類
	private String contractType;

	// 簽約日
	private String contractDate;

	// 解約日
	private String contractEndDate;

	// 風險評等
	private String trustClass;

	private String edcFlag;

	private String icFlag;

	private String posFlag;

	// 分期only flag
	private String installmentOnly;

	private LongString sepcialTreatyLimit;
	/* M2018066 QR主掃需求 start */
	private LongString merchantPan;

	public LongString getMerchantPan() {
		return merchantPan;
	}

	public void setMerchantPan(LongString merchantPan) {
		this.merchantPan = merchantPan;
	}

	/* M2018066 QR主掃需求 end */
	public Object getId() {

		return getMchtNo();
	}

	public String getArticleClass() {

		return articleClass;
	}

	public void setArticleClass(String articleClass) {

		this.articleClass = articleClass;
	}

	public String getBelongBank() {

		return belongBank;
	}

	public void setBelongBank(String belongBank) {

		this.belongBank = belongBank;
	}

	public String getBizType002Count() {

		return bizType002Count;
	}

	public void setBizType002Count(String bizType002Count) {

		this.bizType002Count = bizType002Count;
	}

	public String getBizType003Count() {

		return bizType003Count;
	}

	public void setBizType003Count(String bizType003Count) {

		this.bizType003Count = bizType003Count;
	}

	public String getChinAddr() {

		return chinAddr;
	}

	public void setChinAddr(String chinAddr) {

		this.chinAddr = chinAddr;
	}

	public String getChinZipCity() {

		return chinZipCity;
	}

	public void setChinZipCity(String chinZipCity) {

		this.chinZipCity = chinZipCity;
	}

	public String getCurrentCode() {

		return currentCode;
	}

	public void setCurrentCode(String currentCode) {

		this.currentCode = currentCode;
	}

	public String getEdcFlag() {

		return edcFlag;
	}

	public void setEdcFlag(String edcFlag) {

		this.edcFlag = edcFlag;
	}

	public String getFileDate() {

		return fileDate;
	}

	public void setFileDate(String fileDate) {

		this.fileDate = fileDate;
	}

	public String getImprinterCnt() {

		return imprinterCnt;
	}

	public void setImprinterCnt(String imprinterCnt) {

		this.imprinterCnt = imprinterCnt;
	}

	public String getIndustryCode() {

		return industryCode;
	}

	public void setIndustryCode(String industryCode) {

		this.industryCode = industryCode;
	}

	public String getManager() {

		return Manager;
	}

	public void setManager(String manager) {

		Manager = manager;
	}

	public String getMchtName() {

		return mchtName;
	}

	public void setMchtName(String mchtName) {

		this.mchtName = mchtName;
	}

	public String getMchtNo() {

		return mchtNo;
	}

	public void setMchtNo(String mchtNo) {

		this.mchtNo = mchtNo;
	}

	public String getMchtType() {

		return mchtType;
	}

	public void setMchtType(String mchtType) {

		this.mchtType = mchtType;
	}

	public String getRiskFileDate() {

		return riskFileDate;
	}

	public void setRiskFileDate(String riskFileDate) {

		this.riskFileDate = riskFileDate;
	}

	public String getTelNo1() {

		return telNo1;
	}

	public void setTelNo1(String telNo1) {

		this.telNo1 = telNo1;
	}

	public String getTrustFileDate() {

		return trustFileDate;
	}

	public void setTrustFileDate(String trustFileDate) {

		this.trustFileDate = trustFileDate;
	}

	public String getZipCode2() {

		return zipCode2;
	}

	public void setZipCode2(String zipCode2) {

		this.zipCode2 = zipCode2;
	}

	public String getZipCode3() {

		return zipCode3;
	}

	public void setZipCode3(String zipCode3) {

		this.zipCode3 = zipCode3;
	}

	public String getContractDateByA() {

		return contractDateByA;
	}

	public void setContractDateByA(String contractDateByA) {

		this.contractDateByA = contractDateByA;
	}

	public String getContractDateByU() {

		return contractDateByU;
	}

	public void setContractDateByU(String contractDateByU) {

		this.contractDateByU = contractDateByU;
	}

	public String getContractDate() {

		return contractDate;
	}

	public void setContractDate(String contractDate) {

		this.contractDate = contractDate;
	}

	public String getContractEndDate() {

		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {

		this.contractEndDate = contractEndDate;
	}

	public String getIcFlag() {

		return icFlag;
	}

	public void setIcFlag(String icFlag) {

		this.icFlag = icFlag;
	}

	public String getPosFlag() {

		return posFlag;
	}

	public void setPosFlag(String posFlag) {

		this.posFlag = posFlag;
	}

	public String getTrustClass() {

		return trustClass;
	}

	public void setTrustClass(String trustClass) {

		this.trustClass = trustClass;
	}

	public String getMccCode() {

		return mccCode;
	}

	public void setMccCode(String mccCode) {

		this.mccCode = mccCode;
	}

	public LongString getSepcialTreatyLimit() {

		return sepcialTreatyLimit;
	}

	public void setSepcialTreatyLimit(LongString sepcialTreatyLimit) {

		this.sepcialTreatyLimit = sepcialTreatyLimit;
	}

	public String getMchtEngName() {

		return mchtEngName;
	}

	public void setMchtEngName(String mchtEngName) {

		this.mchtEngName = mchtEngName;
	}

	public String getEngCityName() {

		return engCityName;
	}

	public void setEngCityName(String engCityName) {

		this.engCityName = engCityName;
	}

	public String getContractType() {

		return contractType;
	}

	public void setContractType(String contractType) {

		this.contractType = contractType;
	}

	public String getInstallmentOnly() {

		return installmentOnly;
	}

	public void setInstallmentOnly(String installmentOnly) {

		this.installmentOnly = installmentOnly;
	}
}
