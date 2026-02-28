package proj.nccc.atsLog.batch.dao.entity;

import java.io.Serializable;
import java.util.Date;

public class Nsshmsgl implements Serializable {
	private static final long serialVersionUID = 8590180129221941416L;
	
	private Date sysDate;
	private String pid;
	private String errMsg;
	private String fileName;
	private String code;
	private String sysMsg;
	private String level;
	private String type;
	private String apMsg;

	/** Creates a new instance of Nsshmsgl */
	public Nsshmsgl() {
	}

	public Date getSysDate() {
		return sysDate;
	}

	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSysMsg() {
		return sysMsg;
	}

	public void setSysMsg(String sysMsg) {
		this.sysMsg = sysMsg;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApMsg() {
		return apMsg;
	}

	public void setApMsg(String apMsg) {
		this.apMsg = apMsg;
	}
}
