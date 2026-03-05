package proj.nccc.logsearch.vo;

import org.apache.commons.beanutils.BeanUtils;

import proj.nccc.logsearch.persist.MFESCheckoutLog;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;


public class MFESCheckoutLogArg extends MFESCheckoutLog implements ProjArg
{
	private static final long serialVersionUID = 1L;
	private String applyCode;
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();
	private String sortFields;

	public String getApplyCode() {
		return applyCode;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public DisplayTagPagingInfos getPagingInfo() {
		return pagingInfo;
	}
	public void setPagingInfo(DisplayTagPagingInfos pagingInfo) {
		this.pagingInfo = pagingInfo;
	}
	public String getSortFields() {
		return sortFields;
	}
	public void setSortFields(String sortFields) {
		this.sortFields = sortFields;
	}
}