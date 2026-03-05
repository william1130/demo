package proj.nccc.logsearch.vo;

import com.edstw.lang.LongString;
import java.text.SimpleDateFormat;
import java.util.Date;
import proj.nccc.logsearch.parse.ISOUtil;
import proj.nccc.logsearch.persist.IFESTransLog;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

/**
 *
 * @author Gail Lee
 * @version 1.0
 */
public class IFESTransLogArg extends IFESTransLog implements ProjArg
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
	private LongString tranAmountFrom;
	private LongString tranAmountTo;
	private String sortFields;
	private String merchantChinName;
	private String approveCodeName;
	private String maskCardNo;
	private String showCardIso = "Y";
	private boolean defaultMti;
	private String tranType;
	private String checkStates;

	private int dataTotalCount;
	
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

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
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

}
