package proj.nccc.logsearch.persist;

import java.util.Date;

import com.edstw.nccc.sql.log.ValueImage;


public class EmvUiLogQuery extends ProjPersistable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6526704880953532432L;

	private Date uiDate;
	private String userId;
	private String uuid;
	private String uiFunction;
	private String uiOther;
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public ValueImage createValueImage() {

		return new ValueImage();
	}
	
}

