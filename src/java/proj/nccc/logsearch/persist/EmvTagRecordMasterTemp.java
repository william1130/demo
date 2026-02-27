package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * table=emv_tag_record_master_temp
 * @author Gail Lee
 * @version 1.0
 */

public class EmvTagRecordMasterTemp extends AbstractProjPersistable {

	/**
	 * EMV_TAG_RECORD_TEMP
	 */
	private static final long serialVersionUID = 1L;
	
	private String emvTag;
	private String emvName;
	private String emvAbbr;
	private String emvDesc;
	private Integer emvLen;
	private String cardType;
	private String sameValueFlag;
	private String activeCode;
	private String oriSameValueFlag;

	@Override
	public Object getId() {
		return new EmvTagRecordMasterTempId(emvTag, sameValueFlag);
	}
	
	public class EmvTagRecordMasterTempId extends CompositeId {
		public EmvTagRecordMasterTempId(String emvTag, String sameValueFlag) {
			super(emvTag, sameValueFlag);
		}

		public String getEmvTag() {
			return (String) keys.get(0);
		}
		
		public String getSameValueFlag() {
			return (String) keys.get(1);
		}		
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;
		
		final EmvTagRecordMasterTemp other = (EmvTagRecordMasterTemp) obj;
		if (this.emvTag != other.emvTag
				&& (this.emvTag == null || !this.emvTag
						.equals(other.emvTag))) {
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


	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(emvTag);
		vi.addValue(emvName);
		vi.addValue(emvAbbr);
		vi.addValue(emvDesc);
		vi.addValue(emvLen);
		vi.addValue(cardType);
		vi.addValue(sameValueFlag);
		vi.addValue(activeCode);
		vi.addValue(oriSameValueFlag);
		return vi;
	}


	public String getEmvTag() {
		return emvTag;
	}


	public void setEmvTag(String emvTag) {
		this.emvTag = emvTag;
	}


	public String getEmvName() {
		return emvName;
	}


	public void setEmvName(String emvName) {
		this.emvName = emvName;
	}


	public String getEmvAbbr() {
		return emvAbbr;
	}


	public void setEmvAbbr(String emvAbbr) {
		this.emvAbbr = emvAbbr;
	}


	public String getEmvDesc() {
		return emvDesc;
	}


	public void setEmvDesc(String emvDesc) {
		this.emvDesc = emvDesc;
	}


	public Integer getEmvLen() {
		return emvLen;
	}


	public void setEmvLen(Integer emvLen) {
		this.emvLen = emvLen;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public String getSameValueFlag() {
		return sameValueFlag;
	}


	public void setSameValueFlag(String sameValueFlag) {
		this.sameValueFlag = sameValueFlag;
	}


	public String getActiveCode() {
		return activeCode;
	}


	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}


	public String getOriSameValueFlag() {
		return oriSameValueFlag;
	}


	public void setOriSameValueFlag(String oriSameValueFlag) {
		this.oriSameValueFlag = oriSameValueFlag;
	}

}
