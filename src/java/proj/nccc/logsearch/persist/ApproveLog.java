
package proj.nccc.logsearch.persist;

import java.util.Date;

import com.edstw.nccc.sql.log.ValueImage;

public class ApproveLog extends AbstractProjPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String approveItem;
	private Date approveDate;
	private String approveResult;
	private String approveUser;

	public ApproveLog() {
	}

	@Override
	public Object getId() {		
		return this.getApproveDate();
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;

		final ApproveLog other = (ApproveLog) obj;
		if (this.getId() != other.getId()
				&& (this.getId() == null || !this.getId().equals(other.getId())))
			return false;
		else
			return true;
	}

	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
		return hash;
	}
	
	public String getApproveItem() {
		return approveItem;
	}

	public void setApproveItem(String approveItem) {
		this.approveItem = approveItem;
		this.modified();
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
		this.modified();
	}

	public String getApproveResult() {
		return approveResult;
	}

	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
		this.modified();
	}

	public String getApproveUser() {
		return approveUser;
	}

	public void setApproveUser(String approveUser) {
		this.approveUser = approveUser;
		this.modified();
	}

	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(approveItem);
		vi.addValue(approveDate);
		vi.addValue(approveResult);
		vi.addValue(approveUser);
		return vi;
	}	
}
