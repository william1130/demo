package proj.nccc.logsearch.vo;

/**
 * EMV Chip Data Page 顯示用
 * @author Red Lee
 * @version 1.0
 */
public class ChipcardInfoValue
{
	private String type;
	private int byteIndex;
	private int bitIndex;
	private int bitLength;
	private String desc;
	private Object value;
	/**
	 * @return the type
	 */

	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the byteIndex
	 */
	public String getByteIndex()
	{
		if (bitLength <= 8)
			return String.valueOf(byteIndex);
		int len = ((bitLength % 8) == 0) ? bitLength / 8 : bitLength / 8 + 1;
		int endIndex = byteIndex + len - 1;
		return new StringBuffer().append(byteIndex).append("-").append(endIndex).toString();
	}

	/**
	 * @param byteIndex the byteIndex to set
	 */
	public void setByteIndex(int byteIndex)
	{
		this.byteIndex = byteIndex;
	}

	/**
	 * @return the bitIndex
	 */
	public String getBitIndex()
	{
		if (bitLength >= 8)
			return "";
		if (bitLength == 1)
			return String.valueOf(bitIndex);
		int endIndex = bitIndex - bitLength + 1;
		if (endIndex <= 0)
			return String.valueOf(bitIndex);
		return new StringBuffer().append(bitIndex).append("-").append(endIndex).toString();
	}

	/**
	 * @param bitIndex the bitIndex to set
	 */
	public void setBitIndex(int bitIndex)
	{
		this.bitIndex = bitIndex;
	}

	/**
	 * @return the bitLength
	 */
	public int getBitLength()
	{
		return bitLength;
	}

	/**
	 * @param bitLength the bitLength to set
	 */
	public void setBitLength(int bitLength)
	{
		this.bitLength = bitLength;
	}

	/**
	 * @return the desc
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	/**
	 * @return the value
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}
}
