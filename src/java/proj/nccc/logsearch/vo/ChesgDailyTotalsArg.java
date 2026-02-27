package proj.nccc.logsearch.vo;

import java.time.LocalDate;

import org.apache.commons.beanutils.BeanUtils;

import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.persist.ChesgDailyTotals;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

public class ChesgDailyTotalsArg extends ChesgDailyTotals implements ProjPersistableArg, PagingArg {

	private static final long serialVersionUID = 1L;
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();
	private int dataTotalCount;

	private String tranDateFrom = LocalDate.now().minusDays(1).format(MyDateUtil.YYYYMMDD);
	private String tranDateTo = LocalDate.now().minusDays(1).format(MyDateUtil.YYYYMMDD);
	private String merchantChinName;;

	public DisplayTagPagingInfos getPagingInfo() {
		return pagingInfo;
	}

	public void buildFromProjPersistable(EmvProjPersistable obj) throws Exception {
		BeanUtils.copyProperties(this, obj);
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

	public int getDataTotalCount() {
		return dataTotalCount;
	}

	public void setDataTotalCount(int dataTotalCount) {
		this.dataTotalCount = dataTotalCount;
	}

	public String getTranDateFrom() {
		return tranDateFrom;
	}

	public void setTranDateFrom(String tranDateFrom) {
		this.tranDateFrom = tranDateFrom;
	}

	public String getTranDateTo() {
		return tranDateTo;
	}

	public void setTranDateTo(String tranDateTo) {
		this.tranDateTo = tranDateTo;
	}

	public String getMerchantChinName() {
		return merchantChinName;
	}

	public void setMerchantChinName(String merchantChinName) {
		this.merchantChinName = merchantChinName;
	}
}
