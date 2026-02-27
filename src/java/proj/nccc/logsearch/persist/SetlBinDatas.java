/*
 *
 *
 * Created on 2008年4月29日, 上午 11:03
 * ==============================================================================================
 * $Id: SetlBinDatas.java,v 1.3 2019/11/25 05:48:19 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * 
 * @author :$ $
 * @version $Revision: 1.3 $
 */
public class SetlBinDatas extends AbstractProjPersistable {

	// SETL_BIN_BINO_PARM Alias_Name a

	private static final long serialVersionUID = -4986487088412803490L;

	private String binNoA; // BIN_NO

	private String fidA; // FID參加機構清算代碼

	private String acqIssA; // 收單/發卡別

	private String cardTypeA; // 卡別

	private String cardKindA; // 卡種

	private String inDateA; // 上線日期

	private String outDateA; // 終止日期

	// SETL_BIN_BUSINESS_PARM Alias_Name b

	private String binNob; // BIN_NO

	private String fidb; // FID參加機構清算代碼

	private String embKindAtmb; // 有無ATM預借現金功能

	// SETL_BIN_BANK_PARM Alias_Name c

	private String fidC; // FID參加機構清算代碼

	private String abbrNameC; // 參加機構簡稱

	private String addrC; // 登記地址

	private String inDateC; // 上線日期

	private String outDateC; // 終止日期

	private String outDateFlagC; // 退出日期是否覆核

	private String aBussTelC; // 收單聯絡電話

	private String bussType; // 業務類別, 1 =收單, 2=發卡, 0=both

	private String mainId;

	private String tokenFlag;

	public String getId() {

		return binNoA;
	}

	public String getBinNoA() {

		return binNoA;
	}

	public void setBinNoA(String binNoA) {

		this.binNoA = binNoA;
	}

	public String getFidA() {

		return fidA;
	}

	public void setFidA(String fidA) {

		this.fidA = fidA;
	}

	public String getAcqIssA() {

		return acqIssA;
	}

	public void setAcqIssA(String acqIssA) {

		this.acqIssA = acqIssA;
	}

	public String getCardTypeA() {

		return cardTypeA;
	}

	public void setCardTypeA(String cardTypeA) {

		this.cardTypeA = cardTypeA;
	}

	public String getCardKindA() {

		return cardKindA;
	}

	public void setCardKindA(String cardKindA) {

		this.cardKindA = cardKindA;
	}

	public String getInDateA() {

		return inDateA;
	}

	public void setInDateA(String inDateA) {

		this.inDateA = inDateA;
	}

	public String getOutDateA() {

		return outDateA;
	}

	public void setOutDateA(String outDateA) {

		this.outDateA = outDateA;
	}

	public String getBinNob() {

		return binNob;
	}

	public void setBinNob(String binNob) {

		this.binNob = binNob;
	}

	public String getFidb() {

		return fidb;
	}

	public void setFidb(String fidb) {

		this.fidb = fidb;
	}

	public String getEmbKindAtmb() {

		return embKindAtmb;
	}

	public void setEmbKindAtmb(String embKindAtmb) {

		this.embKindAtmb = embKindAtmb;
	}

	public String getFidC() {

		return fidC;
	}

	public void setFidC(String fidC) {

		this.fidC = fidC;
	}

	public String getAbbrNameC() {

		return abbrNameC;
	}

	public void setAbbrNameC(String abbrNameC) {

		this.abbrNameC = abbrNameC;
	}

	public String getAddrC() {

		return addrC;
	}

	public void setAddrC(String addrC) {

		this.addrC = addrC;
	}

	public String getInDateC() {

		return inDateC;
	}

	public void setInDateC(String inDateC) {

		this.inDateC = inDateC;
	}

	public String getOutDateC() {

		return outDateC;
	}

	public void setOutDateC(String outDateC) {

		this.outDateC = outDateC;
	}

	public String getOutDateFlagC() {

		return outDateFlagC;
	}

	public void setOutDateFlagC(String outDateFlagC) {

		this.outDateFlagC = outDateFlagC;
	}

	public String getABussTelC() {

		return aBussTelC;
	}

	public void setABussTelC(String aBussTelC) {

		this.aBussTelC = aBussTelC;
	}

	public String getBussType() {

		return bussType;
	}

	public void setBussType(String bussType) {

		this.bussType = bussType;
	}

	public String getMainId() {

		return mainId;
	}

	public void setMainId(String mainId) {

		this.mainId = mainId;
	}

	public String getTokenFlag() {

		return tokenFlag;
	}

	public void setTokenFlag(String tokenFlag) {

		this.tokenFlag = tokenFlag;
	}

	@Override
	public ValueImage createValueImage() {
		return new ValueImage();
	}

}
