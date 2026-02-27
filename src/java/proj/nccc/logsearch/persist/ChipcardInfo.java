package proj.nccc.logsearch.persist;

import org.apache.commons.lang3.StringUtils;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * table=chipcard_info
 * @author Red Lee
 * @version 1.0
 */
public class ChipcardInfo extends ProjPersistable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4667308951308090996L;

	//	87, 95, 9F10..etc
	private String tagName;
	
	//	V/M/J/U/C
	private String cardType;
	//	16/18
	private String chipType;
	//	left begin = 1
	private int byteIndex;
	//	right begin = 1
	private int bitIndex;
	//	bit length
	private int bitLength;
	//	display name
	private String meaning;
	//	H=head M=data
	private String dataType;
	//	B=bin H=hex D=Dec M=Table S=String
	private String displayType;
	
	
	/* (non-Javadoc)
	 * @see com.edstw.nccc.sql.log.AbstractJdbcStatefulPersistable4AccLog#createValueImage()
	 */
	public ValueImage createValueImage() {

		return new ValueImage();
	}

	public String getId() {
		return createId(tagName, cardType, chipType);
	}

	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}


	/**
	 * @param tagName the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}


	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}


	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	/**
	 * @return the chipType
	 */
	public String getChipType() {
		return chipType;
	}


	/**
	 * @param chipType the chipType to set
	 */
	public void setChipType(String chipType) {
		this.chipType = chipType;
	}


	/**
	 * @return the byteIndex
	 */
	public int getByteIndex() {
		return byteIndex;
	}


	/**
	 * @param byteIndex the byteIndex to set
	 */
	public void setByteIndex(int byteIndex) {
		this.byteIndex = byteIndex;
	}


	/**
	 * @return the bitIndex
	 */
	public int getBitIndex() {
		return bitIndex;
	}


	/**
	 * @param bitIndex the bitIndex to set
	 */
	public void setBitIndex(int bitIndex) {
		this.bitIndex = bitIndex;
	}


	/**
	 * @return the bitLength
	 */
	public int getBitLength() {
		return bitLength;
	}


	/**
	 * @param bitLength the bitLength to set
	 */
	public void setBitLength(int bitLength) {
		this.bitLength = bitLength;
	}


	/**
	 * @return the meaning
	 */
	public String getMeaning() {
		return meaning;
	}


	/**
	 * @param meaning the meaning to set
	 */
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}


	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}


	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	/**
	 * @return the displayType
	 */
	public String getDisplayType() {
		return displayType;
	}


	/**
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	
	
	
	/**
	 * @param tagName
	 * @param cardType
	 * @param chipType
	 * @return
	 */
	public static String createId(String tagName, String cardType, String chipType) {
		
		return new StringBuffer()
			.append(tagName)
			.append("_")
			.append(StringUtils.isBlank(cardType) ? "" : cardType)
			.append("_")
			.append(StringUtils.isBlank(chipType) ? "" : chipType)
			.toString();
	}

}

/*
chipcard_info
tag_name
card_type
chip_type
byte
bit_index
length
meaning
data_type
display_type

ChipcardInfo
tagName
cardType
chipType
byteIndex
bitIndex
bitLength
meaning
dataType
displayType

TagName
CardType
ChipType
ByteIndex
BitIndex
BitLength
Meaning
DataType
DisplayType
*/