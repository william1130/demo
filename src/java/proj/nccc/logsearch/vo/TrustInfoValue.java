package proj.nccc.logsearch.vo;

/**
 * Trust Data Page 顯示用
 */
public class TrustInfoValue {
	private String desc;
	private Object value;

	public TrustInfoValue(String desc, Object obj) {
		this.desc = desc;
		this.value = obj;
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
