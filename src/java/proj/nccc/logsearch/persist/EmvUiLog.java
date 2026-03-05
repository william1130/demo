package proj.nccc.logsearch.persist;

import java.util.Date;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * table=emv_ui_log
 * @author Gail Lee
 * @version 1.0
 */

public class EmvUiLog extends AbstractProjPersistable {
		
	/**
	 * EMV_UI_LOG
	 */
	private static final long serialVersionUID = 1L;
	
	private Date uiDate;
	private String userId;
	private String uuid;
	private String uiFunction;
	private String uiOther;
	
	public EmvUiLog() {
	}
	
	public class EmvUiLogId extends CompositeId {
		public EmvUiLogId(Date uiDate, String userId,String uuid) {
			super(uiDate, userId,uuid);
		}

		public Date getUiDate() {
			return (Date) keys.get(0);
		}

		public String getUserId() {
			return (String) keys.get(1);
		}
		public String getUuid() {
			return (String) keys.get(2);
		}
	}

	public Object getId() {
		return new EmvUiLogId(uiDate, userId,uuid);
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;

		final EmvUiLog other = (EmvUiLog) obj;
		if (this.uiDate != other.uiDate
				&& (this.uiDate == null || !this.uiDate
						.equals(other.uiDate))) {
			return false;
		}
		
		if (this.userId != other.userId
				&& (this.userId == null || !this.userId
						.equals(other.userId))) {
			return false;
		}	
		if (this.uuid != other.uuid
				&& (this.uuid == null || !this.uuid
						.equals(other.uuid))) {
			return false;
		}	
		
		return true;
	}
	

	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(uuid);
		vi.addValue(uiDate);
		vi.addValue(userId);
		vi.addValue(uiFunction);
		vi.addValue(uiOther);
		return vi;
	}

	public Date getUiDate() {
		return uiDate;
	}

	public void setUiDate(Date uiDate) {
		this.uiDate = uiDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUiFunction() {
		return uiFunction;
	}

	public void setUiFunction(String uiFunction) {
		this.uiFunction = uiFunction;
	}

	public String getUiOther() {
		return uiOther;
	}

	public void setUiOther(String uiOther) {
		this.uiOther = uiOther;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


//	UI_DATE
//	USER_ID
//	UI_FUNCTION
//	UI_OTHER

}
