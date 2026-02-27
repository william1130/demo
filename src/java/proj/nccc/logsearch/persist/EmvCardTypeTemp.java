package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * table=emv_card_type_temp
 * @author Gail Lee
 * @version 1.0
 */

public class EmvCardTypeTemp  extends AbstractProjPersistable {
	
	/**
	 * EMV_CARD_TYPE_TEMP
	 */
	private static final long serialVersionUID = 1L;
	
	private String cardType;
	private String cardTypeName;
	private String activeCode;

	
	public EmvCardTypeTemp() {
	}
	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((cardType == null) ? 0 : cardType.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EmvCardTypeTemp other = (EmvCardTypeTemp) obj;
		if (cardType == null) {
			if (other.cardType != null)
				return false;
		} else if (!cardType.equals(other.cardType))
			return false;
		return true;
	}
	
	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(cardType);
		vi.addValue(cardTypeName);
		vi.addValue(activeCode);
		return vi;
	}
	
	public Object getId() {
		return getCardType();
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}


}
