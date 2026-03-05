package proj.nccc.logsearch.persist;

import java.util.Date;

import com.edstw.lang.IntegerString;
import com.edstw.nccc.sql.log.ValueImage;

public class Parameter extends ProjPersistable {
	private static final long serialVersionUID = -1732694292235416864L;

	/** type常數, 授權來源 */
	public static final String TYPE_AUTH_SOURCE = "AUTH_SOURCE";
	/** type常數, 卡別來源 */
	public static final String TYPE_CARD_SOURCE = "CARD_TYPE";
	/** type常數, 卡別-電子錢包 來源 */
	public static final String TYPE_CARD_eWallet_SOURCE = "e-Wallet";
	/** type常數, CHESG 電子簽單URI */
	public static final String TYPE_CHESG_URI = "CHESG_URI";

	private String uuid;
	private String type;
	private String code;
	private String description;
	private String value;
	private String valueExt1;
	private String valueExt2;
	private String valueExt3;
	private String status;
	private IntegerString listSeq;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	/**
	 * Creates a new instance of Parameter
	 */
	public Parameter() {
	}

	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(uuid);
		vi.addValue(type);
		vi.addValue(code);
		vi.addValue(description);
		vi.addValue(value);
		vi.addValue(valueExt1);
		vi.addValue(valueExt2);
		vi.addValue(valueExt3);
		vi.addValue(status);
		vi.addValue(listSeq);
		vi.addValue(createUser);
		vi.addValue(createDate);
		vi.addValue(updateUser);
		vi.addValue(updateDate);
		return vi;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final Parameter other = (Parameter) obj;
		if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId())))
			return false;
		else
			return true;
	}

	public int hashCode() {
		int hash = 13;
		hash = 83 * hash + this.getId().hashCode();
		return hash;
	}

	public Object getId() {
		return this.getUuid();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		this.modified();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.modified();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		this.modified();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.modified();
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.modified();
		this.value = value;
	}

	public IntegerString getListSeq() {
		return listSeq;
	}

	public void setListSeq(IntegerString listSeq) {
		this.modified();
		this.listSeq = listSeq;
	}

	public String getValueExt1() {
		return valueExt1;
	}

	public void setValueExt1(String valueExt1) {
		this.modified();
		this.valueExt1 = valueExt1;
	}

	public String getValueExt2() {
		return valueExt2;
	}

	public void setValueExt2(String valueExt2) {
		this.modified();
		this.valueExt2 = valueExt2;
	}

	/**
	 * @return the valueExt3
	 */
	public String getValueExt3() {
		return valueExt3;
	}

	/**
	 * @param valueExt3 the valueExt3 to set
	 */
	public void setValueExt3(String valueExt3) {
		this.modified();
		this.valueExt3 = valueExt3;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.modified();
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
		this.modified();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		this.modified();
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
		this.modified();
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		this.modified();
	}
}