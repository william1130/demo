package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * @author  Gail Lee
 * @version  1.0table IFES_TRANS_LOG
 */
public class IFESTransLog extends ProjPersistable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6526704880953532432L;

	// seq_id Number(16) 序號 
	private long seqId;
	
	// log_date Varchar2(8) LOG記錄日期 
	private String logDate;
	
	// log_time Varchar2(9) LOG記錄時間 
	private String logTime;
	
	// mti Varchar2(4) EDC Message Type Identifier 
	private String mti;
	
	// edc_proc_code Varchar2(6) EDC Processing Code 
	private String procCode;
	
	// tran_amount Number(15,3) Transaction Amount 
	private double tranAmount;
	
	// sys_trace_num Varchar2(6) System Audit Trace Number 
	private String sysTraceNum;
	
	// tran_year Varchar2(4) 交易年 
	private String tranYear;
	
	// tran_date Varchar2(4) 交易日期 
	private String tranDate;
	
	// tran_time Varchar2(6) 交易時間 
	private String tranTime;
	
	// pos_entry_mode Varchar2(4) POS Entry Mode 
	private String posEntryMode;
	
	// acq_id Varchar2(3) 收單行代碼(金融機構代碼) 
	private String acqId;
	
	// card_num Varchar2(19) 卡號 
	private String cardNum;
	
	// exp_date Varchar2(4) 有效期 
	private String expDate;
	
	// approve_code Varchar2(6) 授權碼 
	private String approveCode;
	
	// resp_code Varchar2(2) 回覆碼 
	private String respCode;
	
	// term_id Varchar2(8) 端末機代號 
	private String termId;
	
	// merchant_id Varchar2(15) 特店代號 
	private String merchantId;
	
	// batch_num Varchar2(6) 批次號碼
	private String batchNum;
	
	// seq_num Varchar2(6) 
	private String seqNum;
	
	// host_accord  Varchar2(8) 
	private String hostAccord;
	
	//	---------------------------------------------------------

	/* (non-Javadoc)
	 * @see com.edstw.nccc.sql.log.AbstractJdbcStatefulPersistable4AccLog#createValueImage()
	 */
	public ValueImage createValueImage() {

		return new ValueImage();
	}
	
	//	---------------------------------------------------------

	/**
	 * @return the seqId
	 */
	public long getSeqId() {
		return seqId;
	}

	/**
	 * @param seqId the seqId to set
	 */
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	/**
	 * @return the logDate
	 */
	public String getLogDate() {
		return logDate;
	}

	/**
	 * @param logDate the logDate to set
	 */
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	/**
	 * @return the logTime
	 */
	public String getLogTime() {
		return logTime;
	}

	/**
	 * @param logTime the logTime to set
	 */
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	/**
	 * @return the tranAmount
	 */
	public double getTranAmount() {
		return tranAmount;
	}

	/**
	 * @param tranAmount the tranAmount to set
	 */
	public void setTranAmount(double tranAmount) {
		this.tranAmount = tranAmount;
	}

	/**
	 * @return the sysTraceNum
	 */
	public String getSysTraceNum() {
		return sysTraceNum;
	}

	/**
	 * @param sysTraceNum the sysTraceNum to set
	 */
	public void setSysTraceNum(String sysTraceNum) {
		this.sysTraceNum = sysTraceNum;
	}

	/**
	 * @return the tranYear
	 */
	public String getTranYear() {
		return tranYear;
	}

	/**
	 * @param tranYear the tranYear to set
	 */
	public void setTranYear(String tranYear) {
		this.tranYear = tranYear;
	}

	/**
	 * @return the tranDate
	 */
	public String getTranDate() {
		return tranDate;
	}

	/**
	 * @param tranDate the tranDate to set
	 */
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	/**
	 * @return the tranTime
	 */
	public String getTranTime() {
		return tranTime;
	}

	/**
	 * @param tranTime the tranTime to set
	 */
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}

	/**
	 * @return the acqId
	 */
	public String getAcqId() {
		return acqId;
	}

	/**
	 * @param acqId the acqId to set
	 */
	public void setAcqId(String acqId) {
		this.acqId = acqId;
	}

	/**
	 * @return the cardNum
	 */
	public String getCardNum() {
		return cardNum;
	}

	/**
	 * @param cardNum the cardNum to set
	 */
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	/**
	 * @return the expDate
	 */
	public String getExpDate() {
		return expDate;
	}

	/**
	 * @param expDate the expDate to set
	 */
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	/**
	 * @return the approveCode
	 */
	public String getApproveCode() {
		return approveCode;
	}

	/**
	 * @param approveCode the approveCode to set
	 */
	public void setApproveCode(String approveCode) {
		this.approveCode = approveCode;
	}

	/**
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}

	/**
	 * @param respCode the respCode to set
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	/**
	 * @return the termId
	 */
	public String getTermId() {
		return termId;
	}

	/**
	 * @param termId the termId to set
	 */
	public void setTermId(String termId) {
		this.termId = termId;
	}

	/**
	 * @return the merchantId
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getHostAccord() {
		return hostAccord;
	}

	public void setHostAccord(String hostAccord) {
		this.hostAccord = hostAccord;
	}

	public String getPosEntryMode() {
		return posEntryMode;
	}

	public void setPosEntryMode(String posEntryMode) {
		this.posEntryMode = posEntryMode;
	}

	public String getProcCode() {
		return procCode;
	}

	public void setProcCode(String procCode) {
		this.procCode = procCode;
	}

	public String getMti() {
		return mti;
	}

	public void setMti(String mti) {
		this.mti = mti;
	}


}