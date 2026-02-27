package proj.nccc.logsearch.vo;

/**
 * UNY Data Page 顯示用
 */
public class UnyInfoValue {
	private String desc;
	private Object value;

	public UnyInfoValue(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
