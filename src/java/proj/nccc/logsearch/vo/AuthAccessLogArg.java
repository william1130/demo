package proj.nccc.logsearch.vo;

import com.edstw.bean.EdsBeanUtil;

import proj.nccc.logsearch.persist.AuthAccessLog;
import proj.nccc.logsearch.persist.AuthLogData;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

public class AuthAccessLogArg extends AuthAccessLog implements ProjPersistableArg, PagingArg {

	private static final long serialVersionUID = 1L;
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();

	// (desc="日期起")
	private String operateDate1;

	// (desc="日期迄")
	private String operateDate2;

	private String reportType;

	public void buildFromAuthPersistable(AuthLogData obj) {

		EdsBeanUtil.getInstance().copyProperties(this, obj);
	}

	public DisplayTagPagingInfos getPagingInfo() {

		if (pagingInfo == null)
			pagingInfo = new DisplayTagPagingInfos();
		return pagingInfo;
	}

	public String getOperateDate1() {

		return operateDate1;
	}

	public void setOperateDate1(String operateDate1) {

		this.operateDate1 = operateDate1;
	}

	public String getOperateDate2() {

		return operateDate2;
	}

	public void setOperateDate2(String operateDate2) {

		this.operateDate2 = operateDate2;
	}

	public String getReportType() {

		return reportType;
	}

	public void setReportType(String reportType) {

		this.reportType = reportType;
	}

	@Override
	public void buildFromProjPersistable(EmvProjPersistable obj) throws Exception {
		EdsBeanUtil.getInstance().copyProperties(this, obj);

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
}
