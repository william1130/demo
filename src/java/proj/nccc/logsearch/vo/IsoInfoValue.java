package proj.nccc.logsearch.vo;

/**
 * ISO Data Page 顯示用
 * @author Red Lee
 * @version 1.0
 */
public class IsoInfoValue
{
	private String type;
	private String title;
	private String desc;
	private Object value;
	private String length;
	//20170502 票證需求 欄位63用
	private String tksName;

	public IsoInfoValue()
	{
	}

	public IsoInfoValue(String type, String title, String desc)
	{
		this.type = type;
		this.title = title;
		this.desc = desc;
	}

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
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
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

	/**
	 * @return the length
	 */
	public String getLength()
	{
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(String length)
	{
		this.length = length;
	}

	public String getTksName() {
		return tksName;
	}

	public void setTksName(String tksName) {
		this.tksName = tksName;
	}
}
