package proj.nccc.logsearch.vo;

import org.apache.commons.beanutils.BeanUtils;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvUiLog;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

public class EmvUiLogArg extends EmvUiLog implements ProjPersistableArg, ProjArg
{
	private static final long serialVersionUID = 1L;
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();
	String dateFrom = null;
	String dateTo = null;
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;

	private int dataTotalCount;
	public void buildFromProjPersistable(EmvProjPersistable c) throws Exception
	{
		BeanUtils.copyProperties(this, c);
	}

	public DisplayTagPagingInfos getPagingInfo()
	{
		return pagingInfo;
	}

	/**
	 * @return the dateFrom
	 */
	public String getDateFrom()
	{
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(String dateFrom)
	{
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTo
	 */
	public String getDateTo()
	{
		return dateTo;
	}

	/**
	 * @param dateTo the dateTo to set
	 */
	public void setDateTo(String dateTo)
	{
		this.dateTo = dateTo;
	}

	/**
	 * @return the uiLogAction
	 */
	public String getUiLogAction()
	{
		return uiLogAction;
	}

	/**
	 * @param uiLogAction the uiLogAction to set
	 */
	public void setUiLogAction(String uiLogAction)
	{
		this.uiLogAction = uiLogAction;
	}

	/**
	 * @return the uiLogFunctionName
	 */
	public String getUiLogFunctionName()
	{
		return uiLogFunctionName;
	}

	/**
	 * @param uiLogFunctionName the uiLogFunctionName to set
	 */
	public void setUiLogFunctionName(String uiLogFunctionName)
	{
		this.uiLogFunctionName = uiLogFunctionName;
	}

	/**
	 * @return the uiLogOther
	 */
	public String getUiLogOther()
	{
		return uiLogOther;
	}

	/**
	 * @param uiLogOther the uiLogOther to set
	 */
	public void setUiLogOther(String uiLogOther)
	{
		this.uiLogOther = uiLogOther;
	}

	public int getDataTotalCount() {
		return dataTotalCount;
	}

	public void setDataTotalCount(int dataTotalCount) {
		this.dataTotalCount = dataTotalCount;
	}
}
