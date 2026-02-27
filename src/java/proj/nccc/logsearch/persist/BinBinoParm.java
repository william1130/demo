/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: BinBinoParm.java,v 1.3 2019/11/25 05:48:19 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import proj.nccc.logsearch.ProjServices;

import com.edstw.lang.DateString;
import com.edstw.nccc.sql.log.ValueImage;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.3 $
 */
public class BinBinoParm extends AbstractProjPersistable {

	// dumy primary key for framework..

	private static final long serialVersionUID = 1L;

	private String rowId;

	private String binNo;

	private String fid;

	private String acqIss;

	private String cardType;

	private String cardKind;

	private String inDate;

	private String outDate;

	private String ica;

	private int cardLen;

	private String purFlag;

	private String bpFlag;

	private String authOutDate;

	private String modPgm;

	private DateString modTime;

	private String tokenFlag;

	private BinBankParm binBank;

	public BinBinoParm() {

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

	public String getBinNo() {

		return binNo;
	}

	public void setBinNo(String binNo) {

		this.binNo = binNo;
		this.modified();
	}

	public String getFid() {

		return fid;
	}

	public void setFid(String fid) {

		this.fid = fid;
		this.modified();
	}

	public String getAcqIss() {

		return acqIss;
	}

	public void setAcqIss(String acqIss) {

		this.acqIss = acqIss;
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

	public String getIca() {

		return ica;
	}

	public void setIca(String ica) {

		this.ica = ica;
		this.modified();
	}

	public int getCardLen() {

		return cardLen;
	}

	public void setCardLen(int cardLen) {

		this.cardLen = cardLen;
		this.modified();
	}

	public String getPurFlag() {

		return purFlag;
	}

	public void setPurFlag(String purFlag) {

		this.purFlag = purFlag;
		this.modified();
	}

	public String getBpFlag() {

		return bpFlag;
	}

	public void setBpFlag(String bpFlag) {

		this.bpFlag = bpFlag;
		this.modified();
	}

	public String getAuthOutDate() {

		return authOutDate;
	}

	public void setAuthOutDate(String authOutDate) {

		this.authOutDate = authOutDate;
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

	public BinBankParm getBinBank() {

		// System.out.println("Quesr BinBank =============");
		return ProjServices.getBinBankParmQS().getBinBankByFid(fid);
	}

	public void setBinBankParm(BinBankParm binBank) {

		this.binBank = binBank;
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
