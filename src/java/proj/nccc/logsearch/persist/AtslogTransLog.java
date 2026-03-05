package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * @author  Red Lee
 * @version  1.0table atslog_trans_log
 */
public class AtslogTransLog extends ProjPersistable {
	
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
	
	// edc_mti Varchar2(4) EDC Message Type Identifier 
	private String edcMti;
	
	// edc_proc_code Varchar2(6) EDC Processing Code 
	private String edcProcCode;
	
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
	
	// edc_pos_entry_mode Varchar2(4) POS Entry Mode 
	private String edcPosEntryMode;
	
	// nii Varchar2(4) Network International Identifier 
	private String nii;
	
	// pos_cond_code Varchar2(2) POS Condition Code 
	private String posCondCode;
	
	// acq_id Varchar2(3) 收單行代碼(金融機構代碼) 
	private String acqId;
	
	// card_num Varchar2(19) 卡號 
	private String cardNum;
	
	// exp_date Varchar2(4) 有效期 
	private String expDate;
	
	// service_code Varchar2(3) Service Code 
	private String serviceCode;
	
	// edc_rrn Varchar2(12) Retrieval Reference Number 
	private String edcRrn;
	
	// approve_code Varchar2(6) 授權碼 
	private String approveCode;
	
	// resp_code Varchar2(2) 回覆碼 
	private String respCode;
	
	// term_id Varchar2(8) 端末機代號 
	private String termId;
	
	// merchant_id Varchar2(15) 特店代號 
	private String merchantId;
	
	// auth_with_pin Varchar2(1) Y:有PINBLOCK 
	private String authWithPin;
	
	// add_amount Number(15,3) Additional Amount 
	private double addAmount;
	
	// tag_5f2a_tran_curr_code Varchar2(4) Transaction Currency Code 
	private String tag5f2aTranCurrCode;
	
	// tag_5f34_pan_seq_num Varchar2(2) Application PAN Sequence Number 
	private String tag5f34PanSeqNum;
	
	// tag_82_aip Varchar2(4) Application Interchange Profile 
	private String tag82Aip;
	
	// tag_84_df_name Varchar2(32) Dedicated file name 
	private String tag84DfName;
	
	// tag_91_arpc Varchar2(16) Issuer Authentication Data 
	private String tag91Arpc;
	
	// tag_91_arc Varchar2(2) Issuer Authentication Data 
	private String tag91Arc;
	
	// tag_95_tvr Varchar2(10) Terminal Verification Results 
	private String tag95Tvr;
	
	// tag_9a_tran_date Varchar2(6) Transaction Date 
	private String tag9aTranDate;
	
	// tag_9b_tsi Varchar2(4) Transaction Status Information 
	private String tag9bTsi;
	
	// tag_9c_tran_type Varchar2(2) Transaction Type 
	private String tag9cTranType;
	
	// tag_9f02_amount_auth Number(15,3) Amount, Authorized 
	private double tag9f02AmountAuth;
	
	// tag_9f03_amount_other Number(15,3) Amount, Other 
	private double tag9f03AmountOther;
	
	// tag_9f08_Card_avn Varchar2(4) Application version number in card 
	private String tag9f08CardAvn;
	
	// tag_9f09_term_avn Varchar2(4) Application version number in terminal 
	private String tag9f09TermAvn;
	
	// tag_9f10_iad Varchar2(64) Issuer Application Data 
	private String tag9f10Iad;
	
	// tag_9f1a_term_country_code Varchar2(4) Terminal Country Code 
	private String tag9f1aTermCountryCode;
	
	// tag_9f1e_ifd_serial_num Varchar2(16) Interface Device (IFD) Serial Number 
	private String tag9f1eIfdSerialNum;
	
	// tag_9f26_ac Varchar2(16) Application Cryptogram 
	private String tag9f26Ac;
	
	// tag_9f27_cid Varchar2(2) Cryptogram Information Data 
	private String tag9f27Cid;
	
	// tag_9f33_term_cap Varchar2(6) Terminal Capability Profile 
	private String tag9f33TermCap;
	
	// tag-9f34_cvn_result Varchar2(6) Cardholder Verification Method Results 
	private String tag9f34CvnResult;
	
	// tag_9f35_term_type Varchar2(2) Terminal Type 
	private String tag9f35TermType;
	
	// tag_9f36_atc Varchar2(4) Application Transaction Counter 
	private String tag9f36Atc;
	
	// tag_9f37_un Varchar2(8) Unpredictable Number 
	private String tag9f37Un;
	
	// tag_9f41_tran_seq_num Varchar2(14) Transaction sequence counter 
	private String tag9f41TranSeqNum;
	
	// tag_9f5b_isr Varchar2(80) Issuer Script Results 
	private String tag9f5bIsr;
	
	// tag_9f63_card_product_info Varchar2(32) Card Product Label Information(卡產品標識信息) 
	private String tag9f63CardProductInfo;
	
	// tag_9f6e_ffi Varchar2(8) Form Factor Indicator 
	private String tag9f6eFFI;
	
	// tag_dfed_chip_cond_code Varchar2(2) Chip Condition Code 
	private String tagDfedChipCondCode;
	
	// tag_dfee_term_entry_cap Varchar2(2) Terminal Entry Capability 
	private String tagDfeeTermEntryCap;
	
	// tag_dfef_rsn_online_code Varchar2(4) Reason Online Code (BASE24 use) 
	private String tagDfefRsnOnlineCode;
	
	// cup_hot_key_ind Varchar2(1) CUP hot key indicator 
	private String cupHotKeyInd;
	
	// cup_trace_num Varchar2(6) CUP Trace Number 
	private String cupTraceNum;
	
	// cup_tran_date Varchar2(4) CUP Transaction Date 
	private String cupTranDate;
	
	// cup_tran_time Varchar2(6) CUP Transaction Time 
	private String cupTranTime;
	
	// cup_rrn Varchar2(12) CUP Retrieve Reference Number 
	private String cupRrn;
	
	// cup_settle_date Varchar2(4) CUP Settlement date 
	private String cupSettleDate;
	
	// dcc_to_ntd_flag Varchar2(1) DCC轉台幣flag 
	private String dccToNtdFlag;
	
	// ch_present_ind Varchar2(1) Cardholder Present Indicator 
	private String chPresentInd;
	
	// card_present_ind Varchar2(1) Card Present Indicator 
	private String cardPresentInd;
	
	// tran_status_ind Varchar2(1) Transaction Status Indicator 
	private String tranStatusInd;
	
	// tran_sec_ind Varchar2(1) Transaction Security Indicator 
	private String tranSecInd;
	
	// ch_act_term_ind Varchar2(1) Cardholder Activated Terminal Indicator 
	private String chActTermInd;
	
	// term_cap_ind Varchar2(1) Terminal Input Capability Indicator 
	private String termCapInd;
	
	// batch_num Varchar2(6) 批次號碼 
	private String batchNum;
	
	// tips_orig_amount Number(15,3) 小費交易原始金額 
	private double tipsOrigAmount;
	
	// seq_num Varchar2(6) 調閱編號 
	private String seqNum;
	
	// pay_type_ind Varchar2(1) 持卡人繳款方式 '1':紅利積點全額扣抵 '2':紅利積點部分扣抵
	// 'I':分期付款內含手續費 'E':分期付款外加手續費 
	private String payTypeInd;
	
	// redeem_point Number(12) 紅利扣抵點數 
	private long redeemPoint;
	
	// balance_point Number(12) 紅利剩餘點數 
	private long balancePoint;
	
	// pay_amount Number(15,3) 現金繳款金額 
	private double payAmount;
	
	// installment_period Varchar2(2) 分期期數 
	private String installmentPeriod;
	
	// down_payment Number(15,3) 分期首期金額 
	private double downPayment;
	
	// installment_pay Number(15,3) 分期每期金 額 
	private double installmentPay;
	
	// formality_fee Number(15,3) 手續費 
	private double formalityFee;
	
	// settle_flag Varchar2(2) Settlement flag 
	private String settleFlag;
	
	// asm_coupon_number Varchar2(1) Space:無優惠功能
	// '0'=表示無優惠活動 '1'=表示僅有一個優惠活動 '2'=表示有兩個優惠活動 
	private String asmCouponNumber;
	
	// asm_redeem_method Varchar2(1) 兌換方式
	// '1'=以條碼當作兌換資訊，透過收銀機條碼資訊。 '2'=以條碼當作兌換資訊，端末機接BarCode Reader掃描兌換(核銷)條碼。
	// '3'=以條碼當作兌換資訊，手動於端末機輸入兌換(核銷)條碼。'4'=以卡號當作兌換資訊，於端末機上刷卡。
	// '5'=以卡號當作兌換資訊，於端末機上手動輸入。
	private String asmRedeemMethod;
	
	// tran_type Varchar2(3) Transaction Type 
	private String tranType;
	
	// ic_tran Varchar2(4)
	// ICTX:IC Transaction, ICFB:IC Fallback Transaction, ICTM:磁條卡交易,
	// ICOF:IC Offline Transaction, TCUP:TC Upload Transaction 
	private String icTran;
	
	// responder Varchar2(1) 交易處理單位 
	private String responder;
	
	// b24_mti Varchar2(4) BASE24 Message Type Indicator 
	private String b24Mti;
	
	// b24_proc_code Varchar2(6) BASE24 Processing Code 
	private String b24ProcCode;
	
	// mcc Varchar2(4) Merchant Category Code 
	private String mcc;
	
	// b24_pos_entry_mode Varchar2(3) BASE24 POS Entry Mode 
	private String b24PosEntryMode;
	
	// b24_pos_cond_code Varchar2(2) BASE24 POS Condition Code 
	private String b24PosCondCode;
	
	// b24_acq_id Varchar2(13) 收單行代號 
	private String b24AcqId;

	// ats_rrn Varchar2(12) Retrieval Reference Number 
	private String atsRrn;
	
	// postal_code Varchar2(10) 郵遞區號 
	private String postalCode;
	
	// card_very_flag Varchar2(1) 卡片驗證結果 
	private String cardVeryFlag;
	
	// mo_to_flag Varchar2(1) MO/TO Flag 
	private String moToFlag;
	
	// term_attend_ind Varchar2(1) 有人或無人端末機 '0':有人 '1':無人 
	private String termAttendInd;
	
	// transaction_id VarChar2(15) VISA Transaction ID Mastercard Trace ID 
	private String transactionId;
	
	// bank_id Varchar2(3) 金融機構代碼 
	private String bankId;
	
	// card_type Varchar2(1) U':U card 'V':VISA 'M':Mastercard 'J':JCB 'A':AMEX 'C':銀聯 
	private String cardType;
	
	// stand_in_flag Varchar2(2) 代行值 spaces:非代行交易 'NS':ATS代行 'BS:'BASE24代行 
	private String standInFlag;
	
	// ats_host Varchar2(20) 交易進那一台ATS主機 
	private String atsHost;
	
	// edc_spec Varchar2(4) RFES/ATS 
	private String edcSpec;
	
	// bici_port Varchar2(5) 交易轉接至那一個port 
	private String biciPort;
	
	// destination_id Varchar2(4) 授權主機名稱(B24) 
	private String destinationId;
	
	// tmk_index Varchar2(2) 使用那一把TMK INDEX 
	private String tmkIndex;
	
	// from_edc_date Varchar2(8) ATS收到EDC交易日期 
	private String fromEdcDate;
	
	// from_edc_time Varchar2(9) ATS收到EDC交易時間 
	private String fromEdcTime;
	
	// to_base24_date Varchar2(8) ATS將交易送給BASE24日期 
	private String toBase24Date;
	
	// to_base24_time Varchar2(9) ATS將交易送給BASE24時間 
	private String toBase24Time;
	
	// from_base24_date Varchar2(8) ATS收到BASE24回覆日期 
	private String fromBase24Date;
	
	// from_base24_time Varchar2(9) ATS收到BASE24回覆時間 
	private String fromBase24Time;
	
	// to_edc_date Varchar2(8) ATS回覆EDC日期 
	private String toEdcDate;
	
	// to_edc_time Varchar2(9) ATS回覆EDC時間 
	private String toEdcTime;
	
	// to_asm_date Varchar2(8) ATS送給優惠平台日期 
	private String toAsmDate;
	
	// to_asm_time Varchar2(9) ATS送給優惠平台時間 
	private String toAsmTime;
	
	// from_asm_date Varchar2(8) ATS收到優惠平台回覆日期 
	private String fromAsmDate;
	
	// from_asm_time Varchar2(9) ATS收到優惠平台回覆時間 
	private String fromAsmTime;
	
	// to_hsm_mac_date Varchar2(8) ATS送給HSM驗MAC的日期 
	private String toHsmMacDate;
	
	// to_hsm_mac_time Varchar2(9) ATS送給HSM驗MAC的時間 
	private String toHsmMacTime;
	
	// from_hsm_mac_date Varchar2(8) ATS收到HSM回覆驗MAC結果的日期 
	private String fromHsmMacDate;
	
	// from_hsm_mac_time Varchar2(9) ATS收到HSM回覆驗MAC結果的時間 
	private String fromHsmMacTime;
	
	// to_hsm_key_date Varchar2(8) ATS送給HSM轉換PIN BLOCK的日期 
	private String toHsmKeyDate;
	
	// to_hsm_key_time Varchar2(9) ATS送給HSM轉換PIN BLOCK的時間 
	private String toHsmKeyTime;
	
	// from_hsm_key_date Varchar2(8) ATS收到HSM回覆轉換結果的日期 
	private String fromHsmKeyDate;
	
	// from_hsm_key_time Varchar2(9) ATS收到HSM回覆轉換結果的時間 
	private String fromHsmKeyTime;
	
	// to_tms_date Varchar2(8) ATS送給TMS的日期 
	private String toTmsDate;
	
	// to_tms_time Varchar2(9) ATS送給TMS的時間 
	private String toTmsTime;
	
	// from_tms_date Varchar2(8) ATS收到TMS回覆的日期 
	private String fromTmsDate;
	
	// from_tms_time Varchar2(9) ATS收到TMS回覆的時間 
	private String fromTmsTime;
	
	private String fromEdcConfDate;
	
	private String fromEdcConfTime;
	
	private String toEdcConfDate;
	
	private String toEdcConfTime;
	
	private String fromSpswDate;
	
	private String fromSpswTime;
	
	private String toSpswDate;
	
	private String toSpswTime;
	
	private String toSpswConfDate;
	
	private String toSpswConfTime;
	
	//Smart Pay Tag S1~S7
	private String tagS1;
	private String tagS2;
	private String tagS3;
	private String tagS4;
	private String tagS5;
	private String tagS6;
	private String tagS7;
	
	//Smartpay response reason code
	private String fiscRespReasonCode;
	
	private String fiscRespCode;
	
	//免簽名 QUICK_PAY_FLAG
	private String quickPayFlag;
	
	//on us ON_US_ROLE
	private String onUsRole;
	//是否為小額付款，Y:是, N:不是
	private String mpasFlag;
	//20170120 M2016144 電子票證
	//交易前金額 TRAN_AMOUNT_BF
	private String tranAmountBf;
	
	//交易後餘額 TRAN_AMOUNT_AF
	private String tranAmountAf;
	
	//設備交易序號 TM_SERIAL_NUM
	private String tmSerialNum;
	
	//票證卡號 SW_CARD_NUM
	private String swCardNum;
	
	//票證卡號 SW_RESP_CODE
	private String swRespCode;
	
	//電票交易序號 ATS_ESVC_TXN_NUM
	private String atsEsvcTxnNum;
	
	//行動感應支付MCP_IND
	private String mcpInd;

	//M2017080
	private String tag71;
	private String tag72;
	
	//20180509 add req
	private String atsReversal;
	
	//UPLAN-M2018187
	//是否為銀聯優計畫交易，Y:是, N/空白:不是
	private String uplanFlag;
	
	//優計畫-折價後金額 UP_DISCOUNT_AMT
	private String upDiscountAmt;
	
	//優計畫-優惠金額 UP_PREFER_AMT
	private String upPreferAmt;
	
	//Coupon Information TAG_9F60_COUPON_INFO
	private String tag9f60CouponInfo;
	
	private String fromUplanDate;
	
	private String fromUplanTime;
	
	private String toUplanDate;
	
	private String toUplanTime;
	
	//UP_COUPON_ID
	private String upCouponId;

	// TOKEN_REQUESTOR_ID
	private String tokenReqId;
	
	// M2020218_UNY
	private String unyTranCode;
	private String fromUnyDate;
	private String fromUnyTime;
	private String toUnyDate;
	private String toUnyTime;
	private String toUnyNotifyDate;
	private String toUnyNotifyTime;
	
	// M2021165 TAG_9F7C Varchar2(64) Partner Discretionary Data
	private String tag9f7c;
	
	// M2024060_R113110_信託資訊平台
	private String fromTrustDate;
	private String fromTrustTime;
	private String toTrustDate;
	private String toTrustTime;
	private String trustNum; // 信託資訊平台序號
	private String trustBankId; // 信託銀行代碼
	private String trustSeqNum; // 交換平台交易序號

	// M2025074_R114117_CHESG 是否同意使用電子簽單
	private Boolean chesgFlag;
	private String chesgId;
	
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
	 * @return the edcMti
	 */
	public String getEdcMti() {
		return edcMti;
	}

	/**
	 * @param edcMti the edcMti to set
	 */
	public void setEdcMti(String edcMti) {
		this.edcMti = edcMti;
	}

	/**
	 * @return the edcProcCode
	 */
	public String getEdcProcCode() {
		return edcProcCode;
	}

	/**
	 * @param edcProcCode the edcProcCode to set
	 */
	public void setEdcProcCode(String edcProcCode) {
		this.edcProcCode = edcProcCode;
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
	 * @return the edcPosEntryMode
	 */
	public String getEdcPosEntryMode() {
		return edcPosEntryMode;
	}

	/**
	 * @param edcPosEntryMode the edcPosEntryMode to set
	 */
	public void setEdcPosEntryMode(String edcPosEntryMode) {
		this.edcPosEntryMode = edcPosEntryMode;
	}

	/**
	 * @return the nii
	 */
	public String getNii() {
		return nii;
	}

	/**
	 * @param nii the nii to set
	 */
	public void setNii(String nii) {
		this.nii = nii;
	}

	/**
	 * @return the posCondCode
	 */
	public String getPosCondCode() {
		return posCondCode;
	}

	/**
	 * @param posCondCode the posCondCode to set
	 */
	public void setPosCondCode(String posCondCode) {
		this.posCondCode = posCondCode;
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
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return the edcRrn
	 */
	public String getEdcRrn() {
		return edcRrn;
	}

	/**
	 * @param edcRrn the edcRrn to set
	 */
	public void setEdcRrn(String edcRrn) {
		this.edcRrn = edcRrn;
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

	/**
	 * @return the authWithPin
	 */
	public String getAuthWithPin() {
		return authWithPin;
	}

	/**
	 * @param authWithPin the authWithPin to set
	 */
	public void setAuthWithPin(String authWithPin) {
		this.authWithPin = authWithPin;
	}

	/**
	 * @return the addAmount
	 */
	public double getAddAmount() {
		return addAmount;
	}

	/**
	 * @param addAmount the addAmount to set
	 */
	public void setAddAmount(double addAmount) {
		this.addAmount = addAmount;
	}

	/**
	 * @return the tag5f2aTranCurrCode
	 */
	public String getTag5f2aTranCurrCode() {
		return tag5f2aTranCurrCode;
	}

	/**
	 * @param tag5f2aTranCurrCode the tag5f2aTranCurrCode to set
	 */
	public void setTag5f2aTranCurrCode(String tag5f2aTranCurrCode) {
		this.tag5f2aTranCurrCode = tag5f2aTranCurrCode;
	}

	/**
	 * @return the tag5f34PanSeqNum
	 */
	public String getTag5f34PanSeqNum() {
		return tag5f34PanSeqNum;
	}

	/**
	 * @param tag5f34PanSeqNum the tag5f34PanSeqNum to set
	 */
	public void setTag5f34PanSeqNum(String tag5f34PanSeqNum) {
		this.tag5f34PanSeqNum = tag5f34PanSeqNum;
	}

	/**
	 * @return the tag82Aip
	 */
	public String getTag82Aip() {
		return tag82Aip;
	}

	/**
	 * @param tag82Aip the tag82Aip to set
	 */
	public void setTag82Aip(String tag82Aip) {
		this.tag82Aip = tag82Aip;
	}

	/**
	 * @return the tag84DfName
	 */
	public String getTag84DfName() {
		return tag84DfName;
	}

	/**
	 * @param tag84DfName the tag84DfName to set
	 */
	public void setTag84DfName(String tag84DfName) {
		this.tag84DfName = tag84DfName;
	}

	/**
	 * @return the tag91Arpc
	 */
	public String getTag91Arpc() {
		return tag91Arpc;
	}

	/**
	 * @param tag91Arpc the tag91Arpc to set
	 */
	public void setTag91Arpc(String tag91Arpc) {
		this.tag91Arpc = tag91Arpc;
	}

	/**
	 * @return the tag91Arc
	 */
	public String getTag91Arc() {
		return tag91Arc;
	}

	/**
	 * @param tag91Arc the tag91Arc to set
	 */
	public void setTag91Arc(String tag91Arc) {
		this.tag91Arc = tag91Arc;
	}

	/**
	 * @return the tag95Tvr
	 */
	public String getTag95Tvr() {
		return tag95Tvr;
	}

	/**
	 * @param tag95Tvr the tag95Tvr to set
	 */
	public void setTag95Tvr(String tag95Tvr) {
		this.tag95Tvr = tag95Tvr;
	}

	/**
	 * @return the tag9aTranDate
	 */
	public String getTag9aTranDate() {
		return tag9aTranDate;
	}

	/**
	 * @param tag9aTranDate the tag9aTranDate to set
	 */
	public void setTag9aTranDate(String tag9aTranDate) {
		this.tag9aTranDate = tag9aTranDate;
	}

	/**
	 * @return the tag9bTsi
	 */
	public String getTag9bTsi() {
		return tag9bTsi;
	}

	/**
	 * @param tag9bTsi the tag9bTsi to set
	 */
	public void setTag9bTsi(String tag9bTsi) {
		this.tag9bTsi = tag9bTsi;
	}

	/**
	 * @return the tag9cTranType
	 */
	public String getTag9cTranType() {
		return tag9cTranType;
	}

	/**
	 * @param tag9cTranType the tag9cTranType to set
	 */
	public void setTag9cTranType(String tag9cTranType) {
		this.tag9cTranType = tag9cTranType;
	}

	/**
	 * @return the tag9f02AmountAuth
	 */
	public double getTag9f02AmountAuth() {
		return tag9f02AmountAuth;
	}

	/**
	 * @param tag9f02AmountAuth the tag9f02AmountAuth to set
	 */
	public void setTag9f02AmountAuth(double tag9f02AmountAuth) {
		this.tag9f02AmountAuth = tag9f02AmountAuth;
	}

	/**
	 * @return the tag9f03AmountOther
	 */
	public double getTag9f03AmountOther() {
		return tag9f03AmountOther;
	}

	/**
	 * @param tag9f03AmountOther the tag9f03AmountOther to set
	 */
	public void setTag9f03AmountOther(double tag9f03AmountOther) {
		this.tag9f03AmountOther = tag9f03AmountOther;
	}

	public String getTag9f08CardAvn() {
		return tag9f08CardAvn;
	}

	public void setTag9f08CardAvn(String tag9f08CardAvn) {
		this.tag9f08CardAvn = tag9f08CardAvn;
	}

	/**
	 * @return the tag9f09TermAvn
	 */
	public String getTag9f09TermAvn() {
		return tag9f09TermAvn;
	}

	/**
	 * @param tag9f09TermAvn the tag9f09TermAvn to set
	 */
	public void setTag9f09TermAvn(String tag9f09TermAvn) {
		this.tag9f09TermAvn = tag9f09TermAvn;
	}

	/**
	 * @return the tag9f10Iad
	 */
	public String getTag9f10Iad() {
		return tag9f10Iad;
	}

	/**
	 * @param tag9f10Iad the tag9f10Iad to set
	 */
	public void setTag9f10Iad(String tag9f10Iad) {
		this.tag9f10Iad = tag9f10Iad;
	}

	/**
	 * @return the tag9f1aTermCountryCode
	 */
	public String getTag9f1aTermCountryCode() {
		return tag9f1aTermCountryCode;
	}

	/**
	 * @param tag9f1aTermCountryCode the tag9f1aTermCountryCode to set
	 */
	public void setTag9f1aTermCountryCode(String tag9f1aTermCountryCode) {
		this.tag9f1aTermCountryCode = tag9f1aTermCountryCode;
	}

	/**
	 * @return the tag9f1eIfdSerialNum
	 */
	public String getTag9f1eIfdSerialNum() {
		return tag9f1eIfdSerialNum;
	}

	/**
	 * @param tag9f1eIfdSerialNum the tag9f1eIfdSerialNum to set
	 */
	public void setTag9f1eIfdSerialNum(String tag9f1eIfdSerialNum) {
		this.tag9f1eIfdSerialNum = tag9f1eIfdSerialNum;
	}

	/**
	 * @return the tag9f26Ac
	 */
	public String getTag9f26Ac() {
		return tag9f26Ac;
	}

	/**
	 * @param tag9f26Ac the tag9f26Ac to set
	 */
	public void setTag9f26Ac(String tag9f26Ac) {
		this.tag9f26Ac = tag9f26Ac;
	}

	/**
	 * @return the tag9f27Cid
	 */
	public String getTag9f27Cid() {
		return tag9f27Cid;
	}

	/**
	 * @param tag9f27Cid the tag9f27Cid to set
	 */
	public void setTag9f27Cid(String tag9f27Cid) {
		this.tag9f27Cid = tag9f27Cid;
	}

	/**
	 * @return the tag9f33TermCap
	 */
	public String getTag9f33TermCap() {
		return tag9f33TermCap;
	}

	/**
	 * @param tag9f33TermCap the tag9f33TermCap to set
	 */
	public void setTag9f33TermCap(String tag9f33TermCap) {
		this.tag9f33TermCap = tag9f33TermCap;
	}

	/**
	 * @return the tag9f34CvnResult
	 */
	public String getTag9f34CvnResult() {
		return tag9f34CvnResult;
	}

	/**
	 * @param tag9f34CvnResult the tag9f34CvnResult to set
	 */
	public void setTag9f34CvnResult(String tag9f34CvnResult) {
		this.tag9f34CvnResult = tag9f34CvnResult;
	}

	/**
	 * @return the tag9f35TermType
	 */
	public String getTag9f35TermType() {
		return tag9f35TermType;
	}

	/**
	 * @param tag9f35TermType the tag9f35TermType to set
	 */
	public void setTag9f35TermType(String tag9f35TermType) {
		this.tag9f35TermType = tag9f35TermType;
	}

	/**
	 * @return the tag9f36Atc
	 */
	public String getTag9f36Atc() {
		return tag9f36Atc;
	}

	/**
	 * @param tag9f36Atc the tag9f36Atc to set
	 */
	public void setTag9f36Atc(String tag9f36Atc) {
		this.tag9f36Atc = tag9f36Atc;
	}

	/**
	 * @return the tag9f37Un
	 */
	public String getTag9f37Un() {
		return tag9f37Un;
	}

	/**
	 * @param tag9f37Un the tag9f37Un to set
	 */
	public void setTag9f37Un(String tag9f37Un) {
		this.tag9f37Un = tag9f37Un;
	}

	/**
	 * @return the tag9f41TranSeqNum
	 */
	public String getTag9f41TranSeqNum() {
		return tag9f41TranSeqNum;
	}

	/**
	 * @param tag9f41TranSeqNum the tag9f41TranSeqNum to set
	 */
	public void setTag9f41TranSeqNum(String tag9f41TranSeqNum) {
		this.tag9f41TranSeqNum = tag9f41TranSeqNum;
	}

	/**
	 * @return the tag9f5bIsr
	 */
	public String getTag9f5bIsr() {
		return tag9f5bIsr;
	}

	/**
	 * @param tag9f5bIsr the tag9f5bIsr to set
	 */
	public void setTag9f5bIsr(String tag9f5bIsr) {
		this.tag9f5bIsr = tag9f5bIsr;
	}

	/**
	 * @return the tag9f63CardProductInfo
	 */
	public String getTag9f63CardProductInfo() {
		return tag9f63CardProductInfo;
	}

	/**
	 * @param tag9f63CardProductInfo the tag9f63CardProductInfo to set
	 */
	public void setTag9f63CardProductInfo(String tag9f63CardProductInfo) {
		this.tag9f63CardProductInfo = tag9f63CardProductInfo;
	}

	/**
	 * @return the tagDfedChipCondCode
	 */
	public String getTagDfedChipCondCode() {
		return tagDfedChipCondCode;
	}

	/**
	 * @param tagDfedChipCondCode the tagDfedChipCondCode to set
	 */
	public void setTagDfedChipCondCode(String tagDfedChipCondCode) {
		this.tagDfedChipCondCode = tagDfedChipCondCode;
	}

	/**
	 * @return the tagDfeeTermEntryCap
	 */
	public String getTagDfeeTermEntryCap() {
		return tagDfeeTermEntryCap;
	}

	/**
	 * @param tagDfeeTermEntryCap the tagDfeeTermEntryCap to set
	 */
	public void setTagDfeeTermEntryCap(String tagDfeeTermEntryCap) {
		this.tagDfeeTermEntryCap = tagDfeeTermEntryCap;
	}

	/**
	 * @return the tagDfefRsnOnlineCode
	 */
	public String getTagDfefRsnOnlineCode() {
		return tagDfefRsnOnlineCode;
	}

	/**
	 * @param tagDfefRsnOnlineCode the tagDfefRsnOnlineCode to set
	 */
	public void setTagDfefRsnOnlineCode(String tagDfefRsnOnlineCode) {
		this.tagDfefRsnOnlineCode = tagDfefRsnOnlineCode;
	}

	/**
	 * @return the cupHotKeyInd
	 */
	public String getCupHotKeyInd() {
		return cupHotKeyInd;
	}

	/**
	 * @param cupHotKeyInd the cupHotKeyInd to set
	 */
	public void setCupHotKeyInd(String cupHotKeyInd) {
		this.cupHotKeyInd = cupHotKeyInd;
	}

	/**
	 * @return the cupTraceNum
	 */
	public String getCupTraceNum() {
		return cupTraceNum;
	}

	/**
	 * @param cupTraceNum the cupTraceNum to set
	 */
	public void setCupTraceNum(String cupTraceNum) {
		this.cupTraceNum = cupTraceNum;
	}

	/**
	 * @return the cupTranDate
	 */
	public String getCupTranDate() {
		return cupTranDate;
	}

	/**
	 * @param cupTranDate the cupTranDate to set
	 */
	public void setCupTranDate(String cupTranDate) {
		this.cupTranDate = cupTranDate;
	}

	/**
	 * @return the cupTranTime
	 */
	public String getCupTranTime() {
		return cupTranTime;
	}

	/**
	 * @param cupTranTime the cupTranTime to set
	 */
	public void setCupTranTime(String cupTranTime) {
		this.cupTranTime = cupTranTime;
	}

	/**
	 * @return the cupRrn
	 */
	public String getCupRrn() {
		return cupRrn;
	}

	/**
	 * @param cupRrn the cupRrn to set
	 */
	public void setCupRrn(String cupRrn) {
		this.cupRrn = cupRrn;
	}

	/**
	 * @return the cupSettleDate
	 */
	public String getCupSettleDate() {
		return cupSettleDate;
	}

	/**
	 * @param cupSettleDate the cupSettleDate to set
	 */
	public void setCupSettleDate(String cupSettleDate) {
		this.cupSettleDate = cupSettleDate;
	}

	/**
	 * @return the dccToNtdFlag
	 */
	public String getDccToNtdFlag() {
		return dccToNtdFlag;
	}

	/**
	 * @param dccToNtdFlag the dccToNtdFlag to set
	 */
	public void setDccToNtdFlag(String dccToNtdFlag) {
		this.dccToNtdFlag = dccToNtdFlag;
	}

	/**
	 * @return the chPresentInd
	 */
	public String getChPresentInd() {
		return chPresentInd;
	}

	/**
	 * @param chPresentInd the chPresentInd to set
	 */
	public void setChPresentInd(String chPresentInd) {
		this.chPresentInd = chPresentInd;
	}

	/**
	 * @return the cardPresentInd
	 */
	public String getCardPresentInd() {
		return cardPresentInd;
	}

	/**
	 * @param cardPresentInd the cardPresentInd to set
	 */
	public void setCardPresentInd(String cardPresentInd) {
		this.cardPresentInd = cardPresentInd;
	}

	/**
	 * @return the tranStatusInd
	 */
	public String getTranStatusInd() {
		return tranStatusInd;
	}

	/**
	 * @param tranStatusInd the tranStatusInd to set
	 */
	public void setTranStatusInd(String tranStatusInd) {
		this.tranStatusInd = tranStatusInd;
	}

	/**
	 * @return the tranSecInd
	 */
	public String getTranSecInd() {
		return tranSecInd;
	}

	/**
	 * @param tranSecInd the tranSecInd to set
	 */
	public void setTranSecInd(String tranSecInd) {
		this.tranSecInd = tranSecInd;
	}

	/**
	 * @return the chActTermInd
	 */
	public String getChActTermInd() {
		return chActTermInd;
	}

	/**
	 * @param chActTermInd the chActTermInd to set
	 */
	public void setChActTermInd(String chActTermInd) {
		this.chActTermInd = chActTermInd;
	}

	/**
	 * @return the termCapInd
	 */
	public String getTermCapInd() {
		return termCapInd;
	}

	/**
	 * @param termCapInd the termCapInd to set
	 */
	public void setTermCapInd(String termCapInd) {
		this.termCapInd = termCapInd;
	}

	/**
	 * @return the batchNum
	 */
	public String getBatchNum() {
		return batchNum;
	}

	/**
	 * @param batchNum the batchNum to set
	 */
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	/**
	 * @return the tipsOrigAmount
	 */
	public double getTipsOrigAmount() {
		return tipsOrigAmount;
	}

	/**
	 * @param tipsOrigAmount the tipsOrigAmount to set
	 */
	public void setTipsOrigAmount(double tipsOrigAmount) {
		this.tipsOrigAmount = tipsOrigAmount;
	}

	/**
	 * @return the seqNum
	 */
	public String getSeqNum() {
		return seqNum;
	}

	/**
	 * @param seqNum the seqNum to set
	 */
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	/**
	 * @return the payTypeInd
	 */
	public String getPayTypeInd() {
		return payTypeInd;
	}

	/**
	 * @param payTypeInd the payTypeInd to set
	 */
	public void setPayTypeInd(String payTypeInd) {
		this.payTypeInd = payTypeInd;
	}

	/**
	 * @return the redeemPoint
	 */
	public long getRedeemPoint() {
		return redeemPoint;
	}

	/**
	 * @param redeemPoint the redeemPoint to set
	 */
	public void setRedeemPoint(long redeemPoint) {
		this.redeemPoint = redeemPoint;
	}

	/**
	 * @return the balancePoint
	 */
	public long getBalancePoint() {
		return balancePoint;
	}

	/**
	 * @param balancePoint the balancePoint to set
	 */
	public void setBalancePoint(long balancePoint) {
		this.balancePoint = balancePoint;
	}

	/**
	 * @return the payAmount
	 */
	public double getPayAmount() {
		return payAmount;
	}

	/**
	 * @param payAmount the payAmount to set
	 */
	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}

	/**
	 * @return the installmentPeriod
	 */
	public String getInstallmentPeriod() {
		return installmentPeriod;
	}

	/**
	 * @param installmentPeriod the installmentPeriod to set
	 */
	public void setInstallmentPeriod(String installmentPeriod) {
		this.installmentPeriod = installmentPeriod;
	}

	/**
	 * @return the downPayment
	 */
	public double getDownPayment() {
		return downPayment;
	}

	/**
	 * @param downPayment the downPayment to set
	 */
	public void setDownPayment(double downPayment) {
		this.downPayment = downPayment;
	}

	/**
	 * @return the installmentPay
	 */
	public double getInstallmentPay() {
		return installmentPay;
	}

	/**
	 * @param installmentPay the installmentPay to set
	 */
	public void setInstallmentPay(double installmentPay) {
		this.installmentPay = installmentPay;
	}

	/**
	 * @return the formalityFee
	 */
	public double getFormalityFee() {
		return formalityFee;
	}

	/**
	 * @param formalityFee the formalityFee to set
	 */
	public void setFormalityFee(double formalityFee) {
		this.formalityFee = formalityFee;
	}

	/**
	 * @return the settleFlag
	 */
	public String getSettleFlag() {
		return settleFlag;
	}

	/**
	 * @param settleFlag the settleFlag to set
	 */
	public void setSettleFlag(String settleFlag) {
		this.settleFlag = settleFlag;
	}

	/**
	 * @return the asmCouponNumber
	 */
	public String getAsmCouponNumber() {
		return asmCouponNumber;
	}

	/**
	 * @param asmCouponNumber the asmCouponNumber to set
	 */
	public void setAsmCouponNumber(String asmCouponNumber) {
		this.asmCouponNumber = asmCouponNumber;
	}

	/**
	 * @return the asmRedeemMethod
	 */
	public String getAsmRedeemMethod() {
		return asmRedeemMethod;
	}

	/**
	 * @param asmRedeemMethod the asmRedeemMethod to set
	 */
	public void setAsmRedeemMethod(String asmRedeemMethod) {
		this.asmRedeemMethod = asmRedeemMethod;
	}

	/**
	 * @return the tranType
	 */
	public String getTranType() {
		return tranType;
	}

	/**
	 * @param tranType the tranType to set
	 */
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	/**
	 * @return the icTran
	 */
	public String getIcTran() {
		return icTran;
	}

	/**
	 * @param icTran the icTran to set
	 */
	public void setIcTran(String icTran) {
		this.icTran = icTran;
	}

	/**
	 * @return the responder
	 */
	public String getResponder() {
		return responder;
	}

	/**
	 * @param responder the responder to set
	 */
	public void setResponder(String responder) {
		this.responder = responder;
	}

	/**
	 * @return the b24Mti
	 */
	public String getB24Mti() {
		return b24Mti;
	}

	/**
	 * @param b24Mti the b24Mti to set
	 */
	public void setB24Mti(String b24Mti) {
		this.b24Mti = b24Mti;
	}

	/**
	 * @return the b24ProcCode
	 */
	public String getB24ProcCode() {
		return b24ProcCode;
	}

	/**
	 * @param b24ProcCode the b24ProcCode to set
	 */
	public void setB24ProcCode(String b24ProcCode) {
		this.b24ProcCode = b24ProcCode;
	}

	/**
	 * @return the mcc
	 */
	public String getMcc() {
		return mcc;
	}

	/**
	 * @param mcc the mcc to set
	 */
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the b24PosEntryMode
	 */
	public String getB24PosEntryMode() {
		return b24PosEntryMode;
	}

	/**
	 * @param b24PosEntryMode the b24PosEntryMode to set
	 */
	public void setB24PosEntryMode(String b24PosEntryMode) {
		this.b24PosEntryMode = b24PosEntryMode;
	}

	/**
	 * @return the b24PosCondCode
	 */
	public String getB24PosCondCode() {
		return b24PosCondCode;
	}

	/**
	 * @param b24PosCondCode the b24PosCondCode to set
	 */
	public void setB24PosCondCode(String b24PosCondCode) {
		this.b24PosCondCode = b24PosCondCode;
	}

	/**
	 * @return the b24AcqId
	 */
	public String getB24AcqId() {
		return b24AcqId;
	}

	/**
	 * @param b24AcqId the b24AcqId to set
	 */
	public void setB24AcqId(String b24AcqId) {
		this.b24AcqId = b24AcqId;
	}

	/**
	 * @return the atsRrn
	 */
	public String getAtsRrn() {
		return atsRrn;
	}

	/**
	 * @param atsRrn the atsRrn to set
	 */
	public void setAtsRrn(String atsRrn) {
		this.atsRrn = atsRrn;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the cardVeryFlag
	 */
	public String getCardVeryFlag() {
		return cardVeryFlag;
	}

	/**
	 * @param cardVeryFlag the cardVeryFlag to set
	 */
	public void setCardVeryFlag(String cardVeryFlag) {
		this.cardVeryFlag = cardVeryFlag;
	}

	/**
	 * @return the moToFlag
	 */
	public String getMoToFlag() {
		return moToFlag;
	}

	/**
	 * @param moToFlag the moToFlag to set
	 */
	public void setMoToFlag(String moToFlag) {
		this.moToFlag = moToFlag;
	}

	/**
	 * @return the termAttendInd
	 */
	public String getTermAttendInd() {
		return termAttendInd;
	}

	/**
	 * @param termAttendInd the termAttendInd to set
	 */
	public void setTermAttendInd(String termAttendInd) {
		this.termAttendInd = termAttendInd;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the bankId
	 */
	public String getBankId() {
		return bankId;
	}

	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the standInFlag
	 */
	public String getStandInFlag() {
		return standInFlag;
	}

	/**
	 * @param standInFlag the standInFlag to set
	 */
	public void setStandInFlag(String standInFlag) {
		this.standInFlag = standInFlag;
	}

	/**
	 * @return the atsHost
	 */
	public String getAtsHost() {
		return atsHost;
	}

	/**
	 * @param atsHost the atsHost to set
	 */
	public void setAtsHost(String atsHost) {
		this.atsHost = atsHost;
	}

	/**
	 * @return the edcSpec
	 */
	public String getEdcSpec() {
		return edcSpec;
	}

	/**
	 * @param edcSpec the edcSpec to set
	 */
	public void setEdcSpec(String edcSpec) {
		this.edcSpec = edcSpec;
	}

	/**
	 * @return the biciPort
	 */
	public String getBiciPort() {
		return biciPort;
	}

	/**
	 * @param biciPort the biciPort to set
	 */
	public void setBiciPort(String biciPort) {
		this.biciPort = biciPort;
	}

	/**
	 * @return the destinationId
	 */
	public String getDestinationId() {
		return destinationId;
	}

	/**
	 * @param destinationId the destinationId to set
	 */
	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	/**
	 * @return the tmkIndex
	 */
	public String getTmkIndex() {
		return tmkIndex;
	}

	/**
	 * @param tmkIndex the tmkIndex to set
	 */
	public void setTmkIndex(String tmkIndex) {
		this.tmkIndex = tmkIndex;
	}

	/**
	 * @return the fromEdcDate
	 */
	public String getFromEdcDate() {
		return fromEdcDate;
	}

	/**
	 * @param fromEdcDate the fromEdcDate to set
	 */
	public void setFromEdcDate(String fromEdcDate) {
		this.fromEdcDate = fromEdcDate;
	}

	/**
	 * @return the fromEdcTime
	 */
	public String getFromEdcTime() {
		return fromEdcTime;
	}

	/**
	 * @param fromEdcTime the fromEdcTime to set
	 */
	public void setFromEdcTime(String fromEdcTime) {
		this.fromEdcTime = fromEdcTime;
	}

	/**
	 * @return the toBase24Date
	 */
	public String getToBase24Date() {
		return toBase24Date;
	}

	/**
	 * @param toBase24Date the toBase24Date to set
	 */
	public void setToBase24Date(String toBase24Date) {
		this.toBase24Date = toBase24Date;
	}

	/**
	 * @return the toBase24Time
	 */
	public String getToBase24Time() {
		return toBase24Time;
	}

	/**
	 * @param toBase24Time the toBase24Time to set
	 */
	public void setToBase24Time(String toBase24Time) {
		this.toBase24Time = toBase24Time;
	}

	/**
	 * @return the fromBase24Date
	 */
	public String getFromBase24Date() {
		return fromBase24Date;
	}

	/**
	 * @param fromBase24Date the fromBase24Date to set
	 */
	public void setFromBase24Date(String fromBase24Date) {
		this.fromBase24Date = fromBase24Date;
	}

	/**
	 * @return the fromBase24Time
	 */
	public String getFromBase24Time() {
		return fromBase24Time;
	}

	/**
	 * @param fromBase24Time the fromBase24Time to set
	 */
	public void setFromBase24Time(String fromBase24Time) {
		this.fromBase24Time = fromBase24Time;
	}

	/**
	 * @return the toEdcDate
	 */
	public String getToEdcDate() {
		return toEdcDate;
	}

	/**
	 * @param toEdcDate the toEdcDate to set
	 */
	public void setToEdcDate(String toEdcDate) {
		this.toEdcDate = toEdcDate;
	}

	/**
	 * @return the toEdcTime
	 */
	public String getToEdcTime() {
		return toEdcTime;
	}

	/**
	 * @param toEdcTime the toEdcTime to set
	 */
	public void setToEdcTime(String toEdcTime) {
		this.toEdcTime = toEdcTime;
	}

	/**
	 * @return the toAsmDate
	 */
	public String getToAsmDate() {
		return toAsmDate;
	}

	/**
	 * @param toAsmDate the toAsmDate to set
	 */
	public void setToAsmDate(String toAsmDate) {
		this.toAsmDate = toAsmDate;
	}

	/**
	 * @return the toAsmTime
	 */
	public String getToAsmTime() {
		return toAsmTime;
	}

	/**
	 * @param toAsmTime the toAsmTime to set
	 */
	public void setToAsmTime(String toAsmTime) {
		this.toAsmTime = toAsmTime;
	}

	/**
	 * @return the fromAsmDate
	 */
	public String getFromAsmDate() {
		return fromAsmDate;
	}

	/**
	 * @param fromAsmDate the fromAsmDate to set
	 */
	public void setFromAsmDate(String fromAsmDate) {
		this.fromAsmDate = fromAsmDate;
	}

	/**
	 * @return the fromAsmTime
	 */
	public String getFromAsmTime() {
		return fromAsmTime;
	}

	/**
	 * @param fromAsmTime the fromAsmTime to set
	 */
	public void setFromAsmTime(String fromAsmTime) {
		this.fromAsmTime = fromAsmTime;
	}

	/**
	 * @return the toHsmMacDate
	 */
	public String getToHsmMacDate() {
		return toHsmMacDate;
	}

	/**
	 * @param toHsmMacDate the toHsmMacDate to set
	 */
	public void setToHsmMacDate(String toHsmMacDate) {
		this.toHsmMacDate = toHsmMacDate;
	}

	/**
	 * @return the toHsmMacTime
	 */
	public String getToHsmMacTime() {
		return toHsmMacTime;
	}

	/**
	 * @param toHsmMacTime the toHsmMacTime to set
	 */
	public void setToHsmMacTime(String toHsmMacTime) {
		this.toHsmMacTime = toHsmMacTime;
	}

	/**
	 * @return the fromHsmMacDate
	 */
	public String getFromHsmMacDate() {
		return fromHsmMacDate;
	}

	/**
	 * @param fromHsmMacDate the fromHsmMacDate to set
	 */
	public void setFromHsmMacDate(String fromHsmMacDate) {
		this.fromHsmMacDate = fromHsmMacDate;
	}

	/**
	 * @return the fromHsmMacTime
	 */
	public String getFromHsmMacTime() {
		return fromHsmMacTime;
	}

	/**
	 * @param fromHsmMacTime the fromHsmMacTime to set
	 */
	public void setFromHsmMacTime(String fromHsmMacTime) {
		this.fromHsmMacTime = fromHsmMacTime;
	}

	/**
	 * @return the toHsmKeyDate
	 */
	public String getToHsmKeyDate() {
		return toHsmKeyDate;
	}

	/**
	 * @param toHsmKeyDate the toHsmKeyDate to set
	 */
	public void setToHsmKeyDate(String toHsmKeyDate) {
		this.toHsmKeyDate = toHsmKeyDate;
	}

	/**
	 * @return the toHsmKeyTime
	 */
	public String getToHsmKeyTime() {
		return toHsmKeyTime;
	}

	/**
	 * @param toHsmKeyTime the toHsmKeyTime to set
	 */
	public void setToHsmKeyTime(String toHsmKeyTime) {
		this.toHsmKeyTime = toHsmKeyTime;
	}

	/**
	 * @return the fromHsmKeyDate
	 */
	public String getFromHsmKeyDate() {
		return fromHsmKeyDate;
	}

	/**
	 * @param fromHsmKeyDate the fromHsmKeyDate to set
	 */
	public void setFromHsmKeyDate(String fromHsmKeyDate) {
		this.fromHsmKeyDate = fromHsmKeyDate;
	}

	/**
	 * @return the fromHsmKeyTime
	 */
	public String getFromHsmKeyTime() {
		return fromHsmKeyTime;
	}

	/**
	 * @param fromHsmKeyTime the fromHsmKeyTime to set
	 */
	public void setFromHsmKeyTime(String fromHsmKeyTime) {
		this.fromHsmKeyTime = fromHsmKeyTime;
	}

	/**
	 * @return the toTmsDate
	 */
	public String getToTmsDate() {
		return toTmsDate;
	}

	/**
	 * @param toTmsDate the toTmsDate to set
	 */
	public void setToTmsDate(String toTmsDate) {
		this.toTmsDate = toTmsDate;
	}

	/**
	 * @return the toTmsTime
	 */
	public String getToTmsTime() {
		return toTmsTime;
	}

	/**
	 * @param toTmsTime the toTmsTime to set
	 */
	public void setToTmsTime(String toTmsTime) {
		this.toTmsTime = toTmsTime;
	}

	/**
	 * @return the fromTmsDate
	 */
	public String getFromTmsDate() {
		return fromTmsDate;
	}

	/**
	 * @param fromTmsDate the fromTmsDate to set
	 */
	public void setFromTmsDate(String fromTmsDate) {
		this.fromTmsDate = fromTmsDate;
	}

	/**
	 * @return the fromTmsTime
	 */
	public String getFromTmsTime() {
		return fromTmsTime;
	}

	/**
	 * @param fromTmsTime the fromTmsTime to set
	 */
	public void setFromTmsTime(String fromTmsTime) {
		this.fromTmsTime = fromTmsTime;
	}
	
	public String getMtic(){
		if(this.getEdcMti() != null && this.getEdcMti().length() > 2){
			return this.getEdcMti().substring(0, 2);
		}else{
			return "";
		}
	}

	public String getFromEdcConfDate() {
		return fromEdcConfDate;
	}

	public void setFromEdcConfDate(String fromEdcConfDate) {
		this.fromEdcConfDate = fromEdcConfDate;
	}

	public String getFromEdcConfTime() {
		return fromEdcConfTime;
	}

	public void setFromEdcConfTime(String fromEdcConfTime) {
		this.fromEdcConfTime = fromEdcConfTime;
	}

	public String getToEdcConfDate() {
		return toEdcConfDate;
	}

	public void setToEdcConfDate(String toEdcConfDate) {
		this.toEdcConfDate = toEdcConfDate;
	}

	public String getToEdcConfTime() {
		return toEdcConfTime;
	}

	public void setToEdcConfTime(String toEdcConfTime) {
		this.toEdcConfTime = toEdcConfTime;
	}

	public String getFromSpswDate() {
		return fromSpswDate;
	}

	public void setFromSpswDate(String fromSpswDate) {
		this.fromSpswDate = fromSpswDate;
	}

	public String getFromSpswTime() {
		return fromSpswTime;
	}

	public void setFromSpswTime(String fromSpswTime) {
		this.fromSpswTime = fromSpswTime;
	}

	public String getToSpswDate() {
		return toSpswDate;
	}

	public void setToSpswDate(String toSpswDate) {
		this.toSpswDate = toSpswDate;
	}

	public String getToSpswTime() {
		return toSpswTime;
	}

	public void setToSpswTime(String toSpswTime) {
		this.toSpswTime = toSpswTime;
	}

	public String getToSpswConfDate() {
		return toSpswConfDate;
	}

	public void setToSpswConfDate(String toSpswConfDate) {
		this.toSpswConfDate = toSpswConfDate;
	}

	public String getToSpswConfTime() {
		return toSpswConfTime;
	}

	public void setToSpswConfTime(String toSpswConfTime) {
		this.toSpswConfTime = toSpswConfTime;
	}
	

	public String getTagS1() {
		return tagS1;
	}

	public void setTagS1(String tagS1) {
		this.tagS1 = tagS1;
	}

	public String getTagS2() {
		return tagS2;
	}

	public void setTagS2(String tagS2) {
		this.tagS2 = tagS2;
	}

	public String getTagS3() {
		return tagS3;
	}

	public void setTagS3(String tagS3) {
		this.tagS3 = tagS3;
	}

	public String getTagS4() {
		return tagS4;
	}

	public void setTagS4(String tagS4) {
		this.tagS4 = tagS4;
	}

	public String getTagS5() {
		return tagS5;
	}

	public void setTagS5(String tagS5) {
		this.tagS5 = tagS5;
	}

	public String getTagS6() {
		return tagS6;
	}

	public void setTagS6(String tagS6) {
		this.tagS6 = tagS6;
	}

	public String getTagS7() {
		return tagS7;
	}

	public void setTagS7(String tagS7) {
		this.tagS7 = tagS7;
	}

	/**
	 * @return the fiscRespReasonCode
	 */
	public String getFiscRespReasonCode() {
		return fiscRespReasonCode;
	}

	/**
	 * @param fiscRespReasonCode the fiscRespReasonCode to set
	 */
	public void setFiscRespReasonCode(String fiscRespReasonCode) {
		this.fiscRespReasonCode = fiscRespReasonCode;
	}

	/**
	 * @return the fiscRespCode
	 */
	public String getFiscRespCode() {
		return fiscRespCode;
	}

	/**
	 * @param fiscRespCode the fiscRespCode to set
	 */
	public void setFiscRespCode(String fiscRespCode) {
		this.fiscRespCode = fiscRespCode;
	}

	/**
	 * @return the quickPayFlag
	 */
	public String getQuickPayFlag() {
		return quickPayFlag;
	}

	/**
	 * @param quickPayFlag the quickPayFlag to set
	 */
	public void setQuickPayFlag(String quickPayFlag) {
		this.quickPayFlag = quickPayFlag;
	}
	

	
	public String getTag9f6eFFI() {
		return tag9f6eFFI;
	}

	public void setTag9f6eFFI(String tag9f6eFFI) {
		this.tag9f6eFFI = tag9f6eFFI;
	}

	public String getOnUsRole() {
		return onUsRole;
	}

	public void setOnUsRole(String onUsRole) {
		this.onUsRole = onUsRole;
	}
	
	public String getMpasFlag() {
		return mpasFlag;
	}

	public void setMpasFlag(String mpasFlag) {
		this.mpasFlag = mpasFlag;
	}


	public String getTranAmountBf() {
		return tranAmountBf;
	}

	public void setTranAmountBf(String tranAmountBf) {
		this.tranAmountBf = tranAmountBf;
	}

	public String getTranAmountAf() {
		return tranAmountAf;
	}

	public void setTranAmountAf(String tranAmountAf) {
		this.tranAmountAf = tranAmountAf;
	}

	public String getTmSerialNum() {
		return tmSerialNum;
	}

	public void setTmSerialNum(String tmSerialNum) {
		this.tmSerialNum = tmSerialNum;
	}

	public String getSwCardNum() {
		if(swCardNum == null || swCardNum.trim().length() == 0){
			swCardNum = "無卡號";
		}
		return swCardNum;
	}

	public void setSwCardNum(String swCardNum) {
		this.swCardNum = swCardNum;
	}

	public String getSwRespCode() {
		return swRespCode;
	}

	public void setSwRespCode(String swRespCode) {
		this.swRespCode = swRespCode;
	}

	public String getAtsEsvcTxnNum() {
		return atsEsvcTxnNum;
	}

	public void setAtsEsvcTxnNum(String atsEsvcTxnNum) {
		this.atsEsvcTxnNum = atsEsvcTxnNum;
	}

	public String getMcpInd() {
		return mcpInd;
	}

	public void setMcpInd(String mcpInd) {
		this.mcpInd = mcpInd;
	}

	public String getTag71() {
		return tag71;
	}

	public void setTag71(String tag71) {
		this.tag71 = tag71;
	}

	public String getTag72() {
		return tag72;
	}

	public void setTag72(String tag72) {
		this.tag72 = tag72;
	}

	public String getAtsReversal() {
		return atsReversal;
	}

	public void setAtsReversal(String atsReversal) {
		this.atsReversal = atsReversal;
	}

	public String getUplanFlag() {
		return uplanFlag;
	}

	public void setUplanFlag(String uplanFlag) {
		this.uplanFlag = uplanFlag;
	}

	public String getUpDiscountAmt() {
		return upDiscountAmt;
	}

	public void setUpDiscountAmt(String upDiscountAmt) {
		this.upDiscountAmt = upDiscountAmt;
	}

	public String getUpPreferAmt() {
		return upPreferAmt;
	}

	public void setUpPreferAmt(String upPreferAmt) {
		this.upPreferAmt = upPreferAmt;
	}

	public String getTag9f60CouponInfo() {
		return tag9f60CouponInfo;
	}

	public void setTag9f60CouponInfo(String tag9f60CouponInfo) {
		this.tag9f60CouponInfo = tag9f60CouponInfo;
	}

	public String getFromUplanDate() {
		return fromUplanDate;
	}

	public void setFromUplanDate(String fromUplanDate) {
		this.fromUplanDate = fromUplanDate;
	}

	public String getFromUplanTime() {
		return fromUplanTime;
	}

	public void setFromUplanTime(String fromUplanTime) {
		this.fromUplanTime = fromUplanTime;
	}

	public String getToUplanDate() {
		return toUplanDate;
	}

	public void setToUplanDate(String toUplanDate) {
		this.toUplanDate = toUplanDate;
	}

	public String getToUplanTime() {
		return toUplanTime;
	}

	public void setToUplanTime(String toUplanTime) {
		this.toUplanTime = toUplanTime;
	}

	public String getUpCouponId() {
		return upCouponId;
	}

	public void setUpCouponId(String upCouponId) {
		this.upCouponId = upCouponId;
	}

	public String getTokenReqId() {
		return tokenReqId;
	}

	public void setTokenReqId(String tokenReqId) {
		this.tokenReqId = tokenReqId;
	}

	public String getUnyTranCode() {
		return unyTranCode;
	}

	public void setUnyTranCode(String unyTranCode) {
		this.unyTranCode = unyTranCode;
	}

	public String getFromUnyDate() {
		return fromUnyDate;
	}

	public void setFromUnyDate(String fromUnyDate) {
		this.fromUnyDate = fromUnyDate;
	}

	public String getFromUnyTime() {
		return fromUnyTime;
	}

	public void setFromUnyTime(String fromUnyTime) {
		this.fromUnyTime = fromUnyTime;
	}

	public String getToUnyDate() {
		return toUnyDate;
	}

	public void setToUnyDate(String toUnyDate) {
		this.toUnyDate = toUnyDate;
	}

	public String getToUnyTime() {
		return toUnyTime;
	}

	public void setToUnyTime(String toUnyTime) {
		this.toUnyTime = toUnyTime;
	}

	public String getToUnyNotifyDate() {
		return toUnyNotifyDate;
	}

	public void setToUnyNotifyDate(String toUnyNotifyDate) {
		this.toUnyNotifyDate = toUnyNotifyDate;
	}

	public String getToUnyNotifyTime() {
		return toUnyNotifyTime;
	}

	public void setToUnyNotifyTime(String toUnyNotifyTime) {
		this.toUnyNotifyTime = toUnyNotifyTime;
	}

	public String getTag9f7c() {
		return tag9f7c;
	}

	public void setTag9f7c(String tag9f7c) {
		this.tag9f7c = tag9f7c;
	}

	public String getFromTrustDate() {
		return fromTrustDate;
	}

	public void setFromTrustDate(String fromTrustDate) {
		this.fromTrustDate = fromTrustDate;
	}

	public String getFromTrustTime() {
		return fromTrustTime;
	}

	public void setFromTrustTime(String fromTrustTime) {
		this.fromTrustTime = fromTrustTime;
	}

	public String getToTrustDate() {
		return toTrustDate;
	}

	public void setToTrustDate(String toTrustDate) {
		this.toTrustDate = toTrustDate;
	}

	public String getToTrustTime() {
		return toTrustTime;
	}

	public void setToTrustTime(String toTrustTime) {
		this.toTrustTime = toTrustTime;
	}

	public String getTrustNum() {
		return trustNum;
	}

	public void setTrustNum(String trustNum) {
		this.trustNum = trustNum;
	}

	public String getTrustBankId() {
		return trustBankId;
	}

	public void setTrustBankId(String trustBankId) {
		this.trustBankId = trustBankId;
	}

	public String getTrustSeqNum() {
		return trustSeqNum;
	}

	public void setTrustSeqNum(String trustSeqNum) {
		this.trustSeqNum = trustSeqNum;
	}

	public Boolean getChesgFlag() {
		return chesgFlag;
	}

	public void setChesgFlag(Boolean chesgFlag) {
		this.chesgFlag = chesgFlag;
	}

	public String getChesgId() {
		return chesgId;
	}

	public void setChesgId(String chesgId) {
		this.chesgId = chesgId;
	}
}
