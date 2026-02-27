/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: CallBankParm.java,v 1.3 2019/11/25 05:48:19 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * M2010087人工授權交易國外卡監控作業增加CALLBANK 參數
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.3 $
 */
public class CallBankParm extends AbstractProjPersistable {

	// dumy primary key for framework..

	private static final long serialVersionUID = -8010461817174658271L;

	private String rowId;

	private String parmType;

	private String parmCode;

	private String parmAmt;

	public CallBankParm() {

	}

	public String getId() {

		return getRowId();
	}

	public String getRowId() {

		return rowId;
	}

	public void setRowId(String rowId) {

		this.rowId = rowId;
	}

	public String getParmAmt() {

		return parmAmt;
	}

	public void setParmAmt(String parmAmt) {

		this.parmAmt = parmAmt;
	}

	public String getParmCode() {

		return parmCode;
	}

	public void setParmCode(String parmCode) {

		this.parmCode = parmCode;
	}

	public String getParmType() {

		return parmType;
	}

	public void setParmType(String parmType) {

		this.parmType = parmType;
	}

	@Override
	public ValueImage createValueImage() {
		// TODO Auto-generated method stub
		return new ValueImage();
	}

}
