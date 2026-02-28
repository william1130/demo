package proj.nccc.atsLog.batch.vo;

import java.io.Serializable;

public class ReturnVO implements Serializable {

	private static final long serialVersionUID = -5903277802091314800L;

	private String message;
	private int totalCount;
	private int successCount;
	private boolean success;

	public ReturnVO() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
