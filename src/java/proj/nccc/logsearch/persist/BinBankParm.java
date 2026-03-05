/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: BinBankParm.java,v 1.3 2019/11/25 05:48:18 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import com.edstw.lang.DateString;
import com.edstw.nccc.sql.log.ValueImage;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.3 $
 */
public class BinBankParm extends AbstractProjPersistable {

	// dumy primary key for framework..

	private static final long serialVersionUID = 1L;

	private String rowId;

	private String fid;

	private String bankId;

	private String manIdNccc;

	private String mainId;

	private String fullName;

	private String abbrName;

	private String engName;

	private String abbrEng;

	private String addr;

	private String zipCode;

	private String addrBuss;

	private String bussZip;

	private String uniformNoBank;

	private String inDate;

	private String outDate;

	private String outDateFlag;

	private String procId;

	private String procName;

	private String media;

	private String chnCode;

	private String engCode;

	private String memType;

	private String rptWay;

	private String settle;

	private String settleFlag;

	private String bussType;

	private String filePrd;

	private String nmipSvrId;

	private String aBussMan;

	private String aBussTel;

	private String iBussMan;

	private String iBussTel;

	private String act;

	private String aprFlag;

	private String usrId;

	private String rptPageNum;

	private String tapeFid;

	private String lnkCtr;

	private String specBuss;

	private String ntcAcqFlag;

	private String ntcIssFlag;

	private String dailyChk;

	private String dailyChkFlag;

	private String standIn;

	private String standInFlag;

	private String invoName;

	private String procFeeMode;

	private String feeModeFlag;

	private String nmipFid;

	private String modPgm;

	private DateString modTime;

	private String authTel1;

	private String authTel2;

	public BinBankParm() {

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

	public String getFid() {

		return fid;
	}

	public void setFid(String fid) {

		this.fid = fid;
		this.modified();
	}

	public String getBankId() {

		return bankId;
	}

	public void setBankId(String bankId) {

		this.bankId = bankId;
		this.modified();
	}

	public String getManIdNccc() {

		return manIdNccc;
	}

	public void setManIdNccc(String manIdNccc) {

		this.manIdNccc = manIdNccc;
		this.modified();
	}

	public String getMainId() {

		return mainId;
	}

	public void setMainId(String mainId) {

		this.mainId = mainId;
		this.modified();
	}

	public String getFullName() {

		return fullName;
	}

	public void setFullName(String fullName) {

		this.fullName = fullName;
		this.modified();
	}

	public String getAbbrName() {

		return abbrName;
	}

	public void setAbbrName(String abbrName) {

		this.abbrName = abbrName;
		this.modified();
	}

	public String getEngName() {

		return engName;
	}

	public void setEngName(String engName) {

		this.engName = engName;
		this.modified();
	}

	public String getAbbrEng() {

		return abbrEng;
	}

	public void setAbbrEng(String abbrEng) {

		this.abbrEng = abbrEng;
		this.modified();
	}

	public String getAddr() {

		return addr;
	}

	public void setAddr(String addr) {

		this.addr = addr;
		this.modified();
	}

	public String getZipCode() {

		return zipCode;
	}

	public void setZipCode(String zipCode) {

		this.zipCode = zipCode;
		this.modified();
	}

	public String getAddrBuss() {

		return addrBuss;
	}

	public void setAddrBuss(String addrBuss) {

		this.addrBuss = addrBuss;
		this.modified();
	}

	public String getBussZip() {

		return bussZip;
	}

	public void setBussZip(String bussZip) {

		this.bussZip = bussZip;
		this.modified();
	}

	public String getUniformNoBank() {

		return uniformNoBank;
	}

	public void setUniformNoBank(String uniformNoBank) {

		this.uniformNoBank = uniformNoBank;
		this.modified();
	}

	public String getInDate() {

		return inDate;
	}

	public void setInDate(String inDate) {

		this.inDate = inDate;
		this.modified();
	}

	public String getOutDate() {

		return outDate;
	}

	public void setOutDate(String outDate) {

		this.outDate = outDate;
		this.modified();
	}

	public String getOutDateFlag() {

		return outDateFlag;
	}

	public void setOutDateFlag(String outDateFlag) {

		this.outDateFlag = outDateFlag;
		this.modified();
	}

	public String getProcId() {

		return procId;
	}

	public void setProcId(String procId) {

		this.procId = procId;
		this.modified();
	}

	public String getProcName() {

		return procName;
	}

	public void setProcName(String procName) {

		this.procName = procName;
		this.modified();
	}

	public String getMedia() {

		return media;
	}

	public void setMedia(String media) {

		this.media = media;
		this.modified();
	}

	public String getChnCode() {

		return chnCode;
	}

	public void setChnCode(String chnCode) {

		this.chnCode = chnCode;
		this.modified();
	}

	public String getEngCode() {

		return engCode;
	}

	public void setEngCode(String engCode) {

		this.engCode = engCode;
		this.modified();
	}

	public String getMemType() {

		return memType;
	}

	public void setMemType(String memType) {

		this.memType = memType;
		this.modified();
	}

	public String getRptWay() {

		return rptWay;
	}

	public void setRptWay(String rptWay) {

		this.rptWay = rptWay;
		this.modified();
	}

	public String getSettle() {

		return settle;
	}

	public void setSettle(String settle) {

		this.settle = settle;
		this.modified();
	}

	public String getSettleFlag() {

		return settleFlag;
	}

	public void setSettleFlag(String settleFlag) {

		this.settleFlag = settleFlag;
		this.modified();
	}

	public String getBussType() {

		return bussType;
	}

	public void setBussType(String bussType) {

		this.bussType = bussType;
		this.modified();
	}

	public String getFilePrd() {

		return filePrd;
	}

	public void setFilePrd(String filePrd) {

		this.filePrd = filePrd;
		this.modified();
	}

	public String getNmipSvrId() {

		return nmipSvrId;
	}

	public void setNmipSvrId(String nmipSvrId) {

		this.nmipSvrId = nmipSvrId;
		this.modified();
	}

	public String getABussMan() {

		return aBussMan;
	}

	public void setABussMan(String aBussMan) {

		this.aBussMan = aBussMan;
		this.modified();
	}

	public String getABussTel() {

		return aBussTel;
	}

	public void setABussTel(String aBussTel) {

		this.aBussTel = aBussTel;
		this.modified();
	}

	public String getIBussMan() {

		return iBussMan;
	}

	public void setIBussMan(String iBussMan) {

		this.iBussMan = iBussMan;
		this.modified();
	}

	public String getIBussTel() {

		return iBussTel;
	}

	public void setIBussTel(String iBussTel) {

		this.iBussTel = iBussTel;
		this.modified();
	}

	public String getAct() {

		return act;
	}

	public void setAct(String act) {

		this.act = act;
		this.modified();
	}

	public String getAprFlag() {

		return aprFlag;
	}

	public void setAprFlag(String aprFlag) {

		this.aprFlag = aprFlag;
		this.modified();
	}

	public String getUsrId() {

		return usrId;
	}

	public void setUsrId(String usrId) {

		this.usrId = usrId;
		this.modified();
	}

	public String getRptPageNum() {

		return rptPageNum;
	}

	public void setRptPageNum(String rptPageNum) {

		this.rptPageNum = rptPageNum;
		this.modified();
	}

	public String getTapeFid() {

		return tapeFid;
	}

	public void setTapeFid(String tapeFid) {

		this.tapeFid = tapeFid;
		this.modified();
	}

	public String getLnkCtr() {

		return lnkCtr;
	}

	public void setLnkCtr(String lnkCtr) {

		this.lnkCtr = lnkCtr;
		this.modified();
	}

	public String getSpecBuss() {

		return specBuss;
	}

	public void setSpecBuss(String specBuss) {

		this.specBuss = specBuss;
		this.modified();
	}

	public String getNtcAcqFlag() {

		return ntcAcqFlag;
	}

	public void setNtcAcqFlag(String ntcAcqFlag) {

		this.ntcAcqFlag = ntcAcqFlag;
		this.modified();
	}

	public String getNtcIssFlag() {

		return ntcIssFlag;
	}

	public void setNtcIssFlag(String ntcIssFlag) {

		this.ntcIssFlag = ntcIssFlag;
		this.modified();
	}

	public String getDailyChk() {

		return dailyChk;
	}

	public void setDailyChk(String dailyChk) {

		this.dailyChk = dailyChk;
		this.modified();
	}

	public String getDailyChkFlag() {

		return dailyChkFlag;
	}

	public void setDailyChkFlag(String dailyChkFlag) {

		this.dailyChkFlag = dailyChkFlag;
		this.modified();
	}

	public String getStandIn() {

		return standIn;
	}

	public void setStandIn(String standIn) {

		this.standIn = standIn;
		this.modified();
	}

	public String getStandInFlag() {

		return standInFlag;
	}

	public void setStandInFlag(String standInFlag) {

		this.standInFlag = standInFlag;
		this.modified();
	}

	public String getInvoName() {

		return invoName;
	}

	public void setInvoName(String invoName) {

		this.invoName = invoName;
		this.modified();
	}

	public String getProcFeeMode() {

		return procFeeMode;
	}

	public void setProcFeeMode(String procFeeMode) {

		this.procFeeMode = procFeeMode;
		this.modified();
	}

	public String getFeeModeFlag() {

		return feeModeFlag;
	}

	public void setFeeModeFlag(String feeModeFlag) {

		this.feeModeFlag = feeModeFlag;
		this.modified();
	}

	public String getNmipFid() {

		return nmipFid;
	}

	public void setNmipFid(String nmipFid) {

		this.nmipFid = nmipFid;
		this.modified();
	}

	public String getModPgm() {

		return modPgm;
	}

	public void setModPgm(String modPgm) {

		this.modPgm = modPgm;
		this.modified();
	}

	public DateString getModTime() {

		return modTime;
	}

	public void setModTime(DateString modTime) {

		this.modTime = modTime;
		this.modified();
	}

	public String getAuthTel1() {

		return authTel1;
	}

	public void setAuthTel1(String authTel1) {

		this.authTel1 = authTel1;
	}

	public String getAuthTel2() {

		return authTel2;
	}

	public void setAuthTel2(String authTel2) {

		this.authTel2 = authTel2;
	}

	@Override
	public ValueImage createValueImage() {
		return new ValueImage();
	}

}
