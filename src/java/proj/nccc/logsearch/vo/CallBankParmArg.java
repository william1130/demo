/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: CallBankParmArg.java,v 1.2 2017/04/26 05:57:29 leered Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.vo;

import proj.nccc.logsearch.persist.CallBankParm;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

import org.apache.commons.beanutils.BeanUtils;

import com.edstw.bean.EdsBeanUtil;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.2 $
 */
public class CallBankParmArg extends CallBankParm implements ProjPersistableArg, PagingArg {

	private static final long serialVersionUID = 1L;

	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;

	public void buildFromAuthPersistable(CallBankParm obj) {

		EdsBeanUtil.getInstance().copyProperties(this, obj);
	}

	public DisplayTagPagingInfos getPagingInfo() {

		if (pagingInfo == null)
			pagingInfo = new DisplayTagPagingInfos();
		return pagingInfo;
	}

	public void setPagingInfo(DisplayTagPagingInfos pagingInfo) {

		this.pagingInfo = pagingInfo;
	}

	@Override
	public void buildFromProjPersistable(EmvProjPersistable c) throws Exception {
		BeanUtils.copyProperties(this, c);
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
