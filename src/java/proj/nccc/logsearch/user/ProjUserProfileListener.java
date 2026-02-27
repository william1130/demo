package proj.nccc.logsearch.user;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.edstw.nccc.security.NewUserListener;
import com.edstw.nccc.user.NcccUserProfile;
import com.edstw.process.ProcessException;
import com.edstw.user.UserLogger;
import com.edstw.util.ValidateUtil;

public class ProjUserProfileListener implements NewUserListener {
	private static final Logger logger = LogManager.getLogger(ProjUserProfileListener.class);

	/**
	 * Creates a new instance of ProjUserProfileListener
	 */
	public ProjUserProfileListener() {
	}

	public void userProfileCreated(NcccUserProfile ncccUserProfile) {
		try {
			logger.info("ProjUserProfileListener.userProfileCreated ..");
			ProjUserProfile sup = (ProjUserProfile) ncccUserProfile;
			ProjUserInfo sui = (ProjUserInfo) sup.getUserInfo();

			String id = "";
			logger.info("Before Login Info :" + sui.getUserID() + ",[" + sui.getDepartmentID() + "]"
					+ sui.getDepartmentName() + "," + new Date());
			if (sui.getUserID().length() >= 3) {
				id = sui.getUserID().substring(0, 3);
			} else {
				id = sui.getUserID();
			}
			String deptId = null;
			if (sui.getDepartmentID() != null && sui.getDepartmentID().trim().length() >= 3) {
				deptId = sui.getDepartmentID().substring(0, 3);
			} else {
				/*
				 * 理論上在正式環境(Portal/Ldap)應不會跑下面程式 做為判斷部門代號,通常用於測試
				 */
				String[] deptPool = { "CSD", "ASD", "SEAMD", "ABDD", "CPD" };
				String[] deptPoolName = { "客戶服務部", "清算會計部", "商店管理部", "通路推展部", "綜合企劃部" };
				String ssid = id.toUpperCase();
				int dlen = deptPool.length;
				for (int i = 0; i < dlen; i++) {

					if (deptPool[i].indexOf(ssid) == 0) {
						deptId = deptPool[i];
						sui.setDepartmentID(deptId);
						sui.setDepartmentName(deptPoolName[i]);
					}
				}
				if (deptId == null) {
					deptId = id.substring(0, 3);
					sui.setDepartmentID(deptId);
					sui.setDepartmentName(deptId);
				}
			}

			// 若使用者代碼前三碼與部門代碼前三碼相同, 且ID中有任一碼為數字則代表其為bank user.
			if (id.equals(deptId)) {
				if (ValidateUtil.isNumber(id.substring(0, 1), false) || ValidateUtil.isNumber(id.substring(1, 2), false)
						|| ValidateUtil.isNumber(id.substring(2), false)) {
					sui.setBankUser(true);
					try {
						// String bankName = (String) ParamBean.getInstance().getBankNameMap().get(id);
						// if (bankName == null)
						// throw new ProcessException("查無金融機構"+id+"與本系統對應關係, 請連絡系統管理員.");
						sui.setBankNo(id);
						sui.setBankName(id);
						sui.setDepartmentName(id);
						sui.setDepartmentID(id);
						sui.setFiscBankId(id);

//                        CodeValueVO b=ProjServices.getParameterQS().getSysBankId(id);
//                        if(b==null)
//                            throw new ProcessException("查無金融機構"+id+"與本系統對應關係, 請連絡系統管理員.");
//                        sui.setBankNo( b.getCode() );
//                        sui.setBankName( b.getValue() );
//                        sui.setDepartmentName( b.getValue());
//                        sui.setDepartmentID(b.getCode());
//                        sui.setFiscBankId(id);
					} catch (Exception x) {
						x.printStackTrace();
						UserLogger.getLog(this.getClass()).error(x.getMessage(), x);
						throw new ProcessException(x);
					}
				}
			}
			logger.info("After  Login Info :" + sui.getUserID() + ",[" + sui.getDepartmentID() + "]"
					+ sui.getDepartmentName() + "," + new Date());
		} catch (Exception e) {
			logger.error(e);
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			throw new ProcessException(e);
		}
	}

}
