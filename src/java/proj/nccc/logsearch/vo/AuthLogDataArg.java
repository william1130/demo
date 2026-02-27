/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: AuthLogDataArg.java,v 1.4 2019/11/25 05:48:19 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.vo;

import org.apache.commons.beanutils.BeanUtils;

import com.edstw.bean.EdsBeanUtil;

import proj.nccc.logsearch.persist.AuthLogData;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.4 $
 */
public class AuthLogDataArg extends AuthLogData implements ProjPersistableArg, PagingArg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();

	private String purchaseDate1;
	private String purchaseDate2;
	private String processDate1;
	private String processDate2;

	private String submitAgain;

	// 卡號錯誤強制強詢
	private String bolSuppCardNoError;
	private String reportType;

	// 用來判斷是人工授權(M),還是補登(S)
	private String fromScreen;

	public void buildFromAuthPersistable(AuthLogData obj) {

		EdsBeanUtil.getInstance().copyProperties(this, obj);
	}

	public String getProcessDate1() {

		return processDate1;
	}

	public void setProcessDate1(String processDate1) {

		this.processDate1 = processDate1;
	}

	public String getProcessDate2() {

		return processDate2;
	}

	public void setProcessDate2(String processDate2) {

		this.processDate2 = processDate2;
	}

	public String getSubmitAgain() {

		return submitAgain;
	}

	public void setSubmitAgain(String submitAgain) {

		this.submitAgain = submitAgain;
	}

	public String getBolSuppCardNoError() {

		return bolSuppCardNoError;
	}

	public void setBolSuppCardNoError(String bolSuppCardNoError) {

		this.bolSuppCardNoError = bolSuppCardNoError;
	}

	public String getReportType() {

		return reportType;
	}

	public void setReportType(String reportType) {

		this.reportType = reportType;
	}

	public String getFromScreen() {

		return fromScreen;
	}

	public void setFromScreen(String fromScreen) {

		this.fromScreen = fromScreen;
	}

	public String getPurchaseDate1() {

		return purchaseDate1;
	}

	public void setPurchaseDate1(String purchaseDate1) {

		this.purchaseDate1 = purchaseDate1;
	}

	public String getPurchaseDate2() {

		return purchaseDate2;
	}

	public void setPurchaseDate2(String purchaseDate2) {

		this.purchaseDate2 = purchaseDate2;
	}

	public void buildFromProjPersistable(EmvProjPersistable c) throws Exception {
		BeanUtils.copyProperties(this, c);
	}

	public DisplayTagPagingInfos getPagingInfo() {
		return pagingInfo;
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
