package proj.nccc.logsearch.user;

import com.edstw.nccc.user.NcccUserInfo;

/**
 *
 * @author (Vincent Shiu)
 * @version $Revision$
 */
public class ProjUserInfo extends NcccUserInfo {
	private boolean bankUser;
	private String bankNo;
	private String bankName;
	private String fiscBankId;

	/** Creates a new instance of ProjUserInfo */
	public ProjUserInfo() {
	}

	public boolean isBankUser() {
		return bankUser;
	}

	public void setBankUser(boolean bankUser) {
		this.bankUser = bankUser;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getFiscBankId() {
		return fiscBankId;
	}

	public void setFiscBankId(String fiscBankId) {
		this.fiscBankId = fiscBankId;
	}

}
