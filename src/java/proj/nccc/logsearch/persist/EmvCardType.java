package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * table=emv_card_type
 * @author Gail Lee
 * @version 1.0
 */

public class EmvCardType extends AbstractProjPersistable {
	
	/**
	 * EMV_CARD_TYPE
	 */
	private static final long serialVersionUID = 1L;
	
	private String cardType;
	private String cardTypeName;
	private String status;
	/*private String createUser;
	private Date createDate;
	private String lastUpdateUser;
	private Date lastUpdateDate;*/
	
	public EmvCardType() {
	}

	@Override
	public Object getId() {
		return this.getCardType();
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;

		/*final ApproveLog other = (ApproveLog) obj;
		if (this.getId() != other.getId()
				&& (this.getId() == null || !this.getId().equals(other.getId())))
			return false;
		else
			return true;*/
		return true;
	}

	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
		return hash;
	}
	
	
	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(cardType);
		vi.addValue(cardTypeName);
		vi.addValue(status);

		return vi;
	}
	

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}


	

}
