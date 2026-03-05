/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: AuthAccessLog.java,v 1.2 2019/10/21 01:10:59 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

public class AuthAccessLog extends AbstractProjPersistable {

	private static final long serialVersionUID = 1L;

	// dumy primary key for framework..
	private String rowId;

	private String bankNo;

	private String cardData;

	private String updateDept;

	private String updateId;

	private String modProgramId;

	private String modProgramName;

	private String processDate;

	private String processTime;

	private String orgId;

	private String teamId;

	public AuthAccessLog() {

	}

	public String getId() {

		return getRowId();
	}

	public String getRowId() {

		return rowId;
	}

	public void setRowId(String rowId) {

		this.rowId = rowId;
		this.modified();
	}

	public String getBankNo() {

		return bankNo;
	}

	public void setBankNo(String bankNo) {

		this.bankNo = bankNo;
		this.modified();
	}

	public String getCardData() {

		return cardData;
	}

	public void setCardData(String cardData) {

		this.cardData = cardData;
	}

	public String getUpdateId() {

		return updateId;
	}

	public void setUpdateId(String updateId) {

		this.updateId = updateId;
	}

	public String getModProgramId() {

		return modProgramId;
	}

	public void setModProgramId(String modProgramId) {

		this.modProgramId = modProgramId;
		this.modified();
	}

	public String getProcessDate() {

		return processDate;
	}

	public void setProcessDate(String processDate) {

		this.processDate = processDate;
		this.modified();
	}

	public String getProcessTime() {

		return processTime;
	}

	public void setProcessTime(String processTime) {

		this.processTime = processTime;
		this.modified();
	}

	public String getUpdateDept() {

		return updateDept;
	}

	public void setUpdateDept(String updateDept) {

		this.updateDept = updateDept;
		this.modified();
	}

	public String getModProgramName() {

		if ("MGOA100".equals(this.modProgramId))
			return "人工授權資料登錄";
		else if ("MGOA900".equals(this.modProgramId))
			return "信用卡開卡資料";
		else
			return this.modProgramId;
	}

	public void setModProgramName(String modProgramName) {

		this.modProgramName = modProgramName;
	}

	public String getOrgId() {

		return orgId;
	}

	public void setOrgId(String orgId) {

		this.orgId = orgId;
		this.modified();
	}

	public String getTeamId() {

		return teamId;
	}

	public void setTeamId(String teamId) {

		this.teamId = teamId;
		this.modified();
	}

	@Override
	public ValueImage createValueImage() {
		// TODO Auto-generated method stub
		return null;
	}

}
