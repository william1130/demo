/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: BinBankParmArg.java,v 1.2 2017/04/26 05:57:29 leered Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.vo;

import com.edstw.bean.EdsBeanUtil;

import proj.nccc.logsearch.persist.BinBankParm;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.2 $
 */
public class BinBankParmArg extends BinBankParm implements ProjPersistableArg, PagingArg {

	private static final long serialVersionUID = 1L;
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;

	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();

	public DisplayTagPagingInfos getPagingInfo() {
		return pagingInfo;
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
