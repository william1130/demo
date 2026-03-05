package proj.nccc.logsearch.vo;

import java.text.SimpleDateFormat;
import java.util.Date;
import proj.nccc.logsearch.persist.EmvUiLogQuery;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

public class EmvUiLogQueryArg extends EmvUiLogQuery implements ProjArg
{
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();
	private String dateFrom = sdf.format(new Date());
	private String dateTo = sdf.format(new Date());

	private int dataTotalCount;

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

	public int getDataTotalCount() {
		return dataTotalCount;
	}

	public void setDataTotalCount(int dataTotalCount) {
		this.dataTotalCount = dataTotalCount;
	}
}
