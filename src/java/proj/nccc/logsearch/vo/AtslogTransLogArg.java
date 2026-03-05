package proj.nccc.logsearch.vo;

import com.edstw.lang.LongString;
import java.text.SimpleDateFormat;
import java.util.Date;
import proj.nccc.logsearch.parse.ISOUtil;
import proj.nccc.logsearch.persist.AtslogTransLog;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

/**
 *
 * @author Red Lee
 * @version 1.0
 */
public class AtslogTransLogArg extends AtslogTransLog implements ProjArg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2863676746315272398L;
	public static final String[] SORT_FIELD_NAMES = new String[] { "tran_time", "card_num", "tran_amount", "acq_id", "approve_code", "resp_code", "tran_date", "merchant_id", "term_id" };
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();
	private String tranDateFrom = sdf.format(new Date());
	private String tranDateTo = sdf.format(new Date());
	private String tranTimeFrom = "000000";
	private String tranTimeTo = "235959";
	private String checkCardNum;
	private LongString tranAmountFrom;
	private LongString tranAmountTo;
	private String sortFields;
	private String transNo;
	private String cardNo;
	private String merchantChinName;
	private String approveCodeName;
	private String maskCardNo;
	private String showCardIso = "Y";
	private boolean defaultMti;
	private String ncccMid;
	private String ncccTid;
	private String ctcbMid;
	private String ctcbTid;
	//M2016144 票證需求
	private String etData;
	private String tksTranTypeName;
	private String checkStates;
	// M2020050 AE 產檔格式
	private String exFileType;

	private int dataTotalCount;

	//UPLAN-M2018187 
	private double upPreferAmtD;
	private double upDiscountAmtD;
	
	// M2025074_R114117_CHESG 電子簽單URI
	private String chesgUri;
	private Integer totalCount;
	private Integer chesgCount;
	private boolean showChesg;

	public boolean isDefaultMti()
	{
		return defaultMti;
	}

	public void setDefaultMti(boolean defaultMti)
	{
		this.defaultMti = defaultMti;
	}

	public String getCardNumMask()
	{
		String cardNum = getCardNum();
		if (cardNum == null)
			return cardNum;
		if (!"Y".equalsIgnoreCase(maskCardNo))
			return cardNum;
		return ISOUtil.getCardNumMask(cardNum);
	}

	/**
	 * @return the tranDateFrom
	 */
	public String getTranDateFrom()
	{
		return tranDateFrom;
	}

	/**
	 * @param tranDateFrom the tranDateFrom to set
	 */
	public void setTranDateFrom(String tranDateFrom)
	{
		this.tranDateFrom = tranDateFrom;
	}

	/**
	 * @return the tranDateTo
	 */
	public String getTranDateTo()
	{
		return tranDateTo;
	}

	/**
	 * @param tranDateTo the tranDateTo to set
	 */
	public void setTranDateTo(String tranDateTo)
	{
		this.tranDateTo = tranDateTo;
	}

	public String getTranTimeFrom()
	{
		return tranTimeFrom;
	}

	public void setTranTimeFrom(String tranTimeFrom)
	{
		this.tranTimeFrom = tranTimeFrom;
	}

	public String getTranTimeTo()
	{
		return tranTimeTo;
	}

	public void setTranTimeTo(String tranTimeTo)
	{
		this.tranTimeTo = tranTimeTo;
	}

	/**
	 * @return the checkCardNum
	 */
	public String getCheckCardNum()
	{
		return checkCardNum;
	}

	/**
	 * @param checkCardNum the checkCardNum to set
	 */
	public void setCheckCardNum(String checkCardNum)
	{
		this.checkCardNum = checkCardNum;
	}

	/**
	 * @return the tranAmountFrom
	 */
	public LongString getTranAmountFrom()
	{
		return tranAmountFrom;
	}

	/**
	 * @param tranAmountFrom the tranAmountFrom to set
	 */
	public void setTranAmountFrom(LongString tranAmountFrom)
	{
		this.tranAmountFrom = tranAmountFrom;
	}

	/**
	 * @return the tranAmountTo
	 */
	public LongString getTranAmountTo()
	{
		return tranAmountTo;
	}

	/**
	 * @param tranAmountTo the tranAmountTo to set
	 */
	public void setTranAmountTo(LongString tranAmountTo)
	{
		this.tranAmountTo = tranAmountTo;
	}

	/**
	 * @return the sortFields
	 */
	public String getSortFields()
	{
		return sortFields;
	}

	/**
	 * @param sortFields the sortFields to set
	 */
	public void setSortFields(String sortFields)
	{
		this.sortFields = sortFields;
	}

	/**
	 * @return the transNo
	 */
	public String getTransNo()
	{
		return transNo;
	}

	/**
	 * @param transNo the transNo to set
	 */
	public void setTransNo(String transNo)
	{
		this.transNo = transNo;
	}

	/**
	 * @return the merchantChinName
	 */
	public String getMerchantChinName()
	{
		return merchantChinName;
	}

	/**
	 * @param merchantChinName the merchantChinName to set
	 */
	public void setMerchantChinName(String merchantChinName)
	{
		this.merchantChinName = merchantChinName;
	}

	/**
	 * @return the approveCodeName
	 */
	public String getApproveCodeName()
	{
		return approveCodeName;
	}

	/**
	 * @param approveCodeName the approveCodeName to set
	 */
	public void setApproveCodeName(String approveCodeName)
	{
		this.approveCodeName = approveCodeName;
	}

	public String getChipType9F10()
	{
		if (!"M".equals(this.getCardType()))
			return "";
		String tag9f10Iad = this.getTag9f10Iad();
		int len = tag9f10Iad == null ? 0 : tag9f10Iad.trim().length();
		if (len == 16)
			return "16";
		else
			if (len == 18) return "18"; else if (len > 18) return "36"; else return "";
	}

	/**
	 * 158 031 VOID_SALE 取消銷售 
	 * 142 032 VOID_REFUND 取消退貨 
	 * B卡目前不知道可能是TWIN卡是T
	 * @return
	 */
	public String getMtiStatus()
	{
		String edcMti = this.getEdcMti();
		String b24Mti = this.getB24Mti();
		// 20180427 DFS other requirement
		String transCode = this.getTranType();
		String respCode = this.getRespCode();
		String cardType = this.getCardType();
		if( ("D".equals(cardType) || "B".equals(cardType))  && ( "031".equals(transCode) || "032".equals(transCode) ) && "00".equals(respCode) ) {
			if (b24Mti != null && b24Mti.length() >= 2 && "04".equals(b24Mti.substring(0, 2)))
				return "";
		}
		if (edcMti != null && edcMti.length() >= 2 && "04".equals(edcMti.substring(0, 2)))
			return "E";
		if (b24Mti != null && b24Mti.length() >= 2 && "04".equals(b24Mti.substring(0, 2)))
			return "E";
		return "";
	}
	

	public DisplayTagPagingInfos getPagingInfo()
	{
		return pagingInfo;
	}

	public String getMaskCardNo()
	{
		return maskCardNo;
	}

	public void setMaskCardNo(String maskCardNo)
	{
		this.maskCardNo = maskCardNo;
	}

	/**
	 * @return the showCardIso
	 */
	public String getShowCardIso()
	{
		return showCardIso;
	}

	/**
	 * @param showCardIso the showCardIso to set
	 */
	public void setShowCardIso(String showCardIso)
	{
		this.showCardIso = showCardIso;
	}

	public String getCtcbMid()
	{
		return ctcbMid;
	}

	public void setCtcbMid(String ctcbMid)
	{
		this.ctcbMid = ctcbMid;
	}

	public String getCtcbTid()
	{
		return ctcbTid;
	}

	public void setCtcbTid(String ctcbTid)
	{
		this.ctcbTid = ctcbTid;
	}

	public String getNcccMid()
	{
		return ncccMid;
	}

	public void setNcccMid(String ncccMid)
	{
		this.ncccMid = ncccMid;
	}

	public String getNcccTid()
	{
		return ncccTid;
	}

	public void setNcccTid(String ncccTid)
	{
		this.ncccTid = ncccTid;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEtData() {
		return etData;
	}

	public void setEtData(String etData) {
		this.etData = etData;
	}

	public String getTksTranTypeName() {
		return tksTranTypeName;
	}

	public void setTksTranTypeName(String tksTranTypeName) {
		this.tksTranTypeName = tksTranTypeName;
	}

	public String getCheckStates() {
		return checkStates;
	}

	public void setCheckStates(String checkStates) {
		this.checkStates = checkStates;
	}

	public int getDataTotalCount() {
		return dataTotalCount;
	}

	public void setDataTotalCount(int dataTotalCount) {
		this.dataTotalCount = dataTotalCount;
	}

	public String getExFileType() {
		return exFileType;
	}

	public void setExFileType(String exFileType) {
		this.exFileType = exFileType;
	}

	public double getUpPreferAmtD() {
		return upPreferAmtD;
	}

	public void setUpPreferAmtD(double upPreferAmtD) {
		this.upPreferAmtD = upPreferAmtD;
	}

	public double getUpDiscountAmtD() {
		return upDiscountAmtD;
	}

	public void setUpDiscountAmtD(double upDiscountAmtD) {
		this.upDiscountAmtD = upDiscountAmtD;
	}

	public String getChesgUri() {
		return chesgUri;
	}

	public void setChesgUri(String chesgUri) {
		this.chesgUri = chesgUri;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getChesgCount() {
		return chesgCount;
	}

	public void setChesgCount(Integer chesgCount) {
		this.chesgCount = chesgCount;
	}

	public boolean isShowChesg() {
		return this.getRespCode() != null && "00".equals(this.getRespCode()) && Boolean.TRUE.equals(this.getChesgFlag())
				&& this.getChesgId() != null;
	}

	public void setShowChesg(boolean showChesg) {
		this.showChesg = showChesg;
	}
}
