package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * table=emv_tag_record_detail
 * @author Gail Lee
 * @version 1.0
 */

public class EmvTagRecordDetailTemp extends AbstractProjPersistable {

	/**
	 * EMV_TAG_RECORD
	 */
	private static final long serialVersionUID = 1L;
	
	private String emvTag;
	private String cardType;
	private Integer posByte;
	private String valueByte;
	private Integer posBit;
	private String valueBit;
	private String sameValueFlag;

	
	public class EmvTagRecordDetailTempId extends CompositeId {
		public EmvTagRecordDetailTempId(String emvTag, String cardType, Integer posByte, Integer posBit, String sameValueFlag) {
			super(emvTag, cardType, posByte, posBit, sameValueFlag);
		}

		public String getEmvTag() {
			return (String) keys.get(0);
		}
		
		public String getCardType() {
			return (String) keys.get(1);
		}		

		public Integer getPosByte() {
			return (Integer) keys.get(2);
		}
		
		public Integer getPosBit() {
			return (Integer) keys.get(3);
		}
		
		public String getSameValueFlag() {
			return (String) keys.get(4);
		}
	}	
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;
		
		final EmvTagRecordDetailTemp other = (EmvTagRecordDetailTemp) obj;
		if (this.emvTag != other.emvTag
				&& (this.emvTag == null || !this.emvTag
						.equals(other.emvTag))) {
			return false;
		}
		
		if (this.cardType != other.cardType
				&& (this.cardType == null || !this.cardType
						.equals(other.cardType))) {
			return false;
		}
		
		if (this.posByte != other.posByte
				&& (this.posByte == null || !this.posByte
						.equals(other.posByte))) {
			return false;
		}
		
		if (this.posBit != other.posBit
				&& (this.posBit == null || !this.posBit
						.equals(other.posBit))) {
			return false;
		}
		
		if (this.sameValueFlag != other.sameValueFlag
				&& (this.sameValueFlag == null || !this.sameValueFlag
						.equals(other.sameValueFlag))) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int hash = 13;
		hash = 83 * hash + this.getId().hashCode();
		return hash;
	}

	public Object getId() {
		// 使用composite物件當成primary key的物件.
		return new EmvTagRecordDetailTempId(emvTag, cardType, posByte, posBit, sameValueFlag);
	}
	
	
	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(emvTag);
		vi.addValue(cardType);
		vi.addValue(posByte);
		vi.addValue(valueByte);
		vi.addValue(posBit);
		vi.addValue(valueBit);
		vi.addValue(sameValueFlag);
		return vi;
	}

	public String getEmvTag() {
		return emvTag;
	}


	public void setEmvTag(String emvTag) {
		this.emvTag = emvTag;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public Integer getPosByte() {
		return posByte;
	}


	public void setPosByte(Integer posByte) {
		this.posByte = posByte;
	}


	public String getValueByte() {
		return valueByte;
	}


	public void setValueByte(String valueByte) {
		this.valueByte = valueByte;
	}


	public Integer getPosBit() {
		return posBit;
	}


	public void setPosBit(Integer posBit) {
		this.posBit = posBit;
	}


	public String getValueBit() {
		return valueBit;
	}


	public void setValueBit(String valueBit) {
		this.valueBit = valueBit;
	}


	public String getSameValueFlag() {
		return sameValueFlag;
	}


	public void setSameValueFlag(String sameValueFlag) {
		this.sameValueFlag = sameValueFlag;
	}

}
