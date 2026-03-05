package proj.nccc.logsearch.struts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.text.StringEscapeUtils;

import com.dxc.nccc.aplog.ApLogConfig;
import com.dxc.nccc.aplog.ApLogManager;
import com.dxc.nccc.aplog.LogMaster;
import com.edstw.user.UserLogger;
import com.edstw.web.WebConstant;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.CodeValueVO;

public class MfesUcsActions extends BaseActions {

	private static final long serialVersionUID = -7856442104399481539L;
	private final int FACTOR_PLUS = 25631;
	private final int FACTOR_MULTIPLIER = 13;
	private String actionResult;
	private String mchtNo;

	public MfesUcsActions() {
	}

	/**
	 * 提供於外界(Estm)系統以轉址方式調用 特店代號原則: (‘1’+特店代號+’1’)*13 - 25631
	 * <c:set var="stordId" value="${sessionScope.store_id}" />
	 * <c:set var="maskStoreId" value="1${stordId}1" />
	 * <c:set var="maskStoreId1" value="${maskStoreId *13 - 25631}" /> Ex:
	 * http://192.168.15.115:38081/esmh/mcht/toModifyMerchant.do?mchtNo=1314040432112
	 * 0101000240 = ( 101010002401 * 13 ) - 25631 = 1313130005582 (1313130005582 +
	 * 25631) / 13 = 1XXXXXXXXXX1
	 * 
	 * 0108000431 = ( 101080004311 * 13 ) - 25631 = 1314040030412 (1314040030412 +
	 * 25631) / 13 = 1XXXXXXXXXX1
	 * 
	 * 0105900022 = (101059000221 * 13 ) - 25631 = 1313766977242 (1313766977242 +
	 * 25631) / 13 = 1XXXXXXXXXX1
	 * 
	 * 6601000081 = (166010000811 * 13 ) - 25631 = 2158129984912 (2158129984912 +
	 * 25631) / 13 = 1XXXXXXXXXX1
	 * 
	 * @return
	 */
	public String mfesUncloseStat() {
		LogMaster master = null;
		UserLogger.getLog(this.getClass()).info("mfesUncloseStat get request ............");
		String merchantId = null;
		actionResult = "N";

		ApLogManager apLogManager = ApLogConfig.getInstance().getApLogManagerFactory().currentApLogManager();
		apLogManager.setEnable(true);
		master = apLogManager.getLogMaster();

		final String ACCESS_TYPE = "Q";
		master.setGuid(getSaltString());
		master.setAccessType(ACCESS_TYPE);
		master.setFunctionId("A009");
		master.setFunctionName("MFES未結帳端末機查詢");
		master.setSystemId("AP004");
		try {
			// --------------------------------------------------------
			// -- 驗證url傳入的特店代號
			mchtNo = StringEscapeUtils.unescapeHtml4(mchtNo);
			UserLogger.getLog(this.getClass()).info("request mchtNo :" + mchtNo);
			long m = Long.parseLong(mchtNo);
			long m2 = (m + this.FACTOR_PLUS) / this.FACTOR_MULTIPLIER;
			String m3 = "" + m2;
			merchantId = (m3).substring(1, m3.length() - 1);
			UserLogger.getLog(this.getClass()).debug("User retrieve merchantNo " + mchtNo + ":" + merchantId);
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			if (mchtNo == null) {
				super.saveError("action.fail", new Object[] { "發生錯誤, 代號不得為空值" });
			} else {
				super.saveError("action.fail", new Object[] { "發生錯誤, 無法辨識代號 : " + mchtNo });
			}
			master.setUserId(mchtNo == null ? "NULL" : mchtNo);
			master.setUserName(mchtNo == null ? "NULL" : mchtNo);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
		if (merchantId == null) {
			super.saveError("action.fail", new Object[] { "發生錯誤, 代號不得為空值 " });
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}

		master.setUserId(merchantId);
		master.setUserName(merchantId);
		try {
			// -------------------------------------------------------------------
			// -- 至EMS相關TABLE中撈出請款代碼，再依該請款代碼至ATS DB查詢出未結帳端末機代號並顯示於畫面上
			List<CodeValueVO> list = ProjServices.getMfesUcsApplyCodeQS().getApplyCode(merchantId);
			List<CodeValueVO> terminalList = new ArrayList<CodeValueVO>();
			if (list != null && list.size() > 0) {
				List<CodeValueVO> merchList = new ArrayList<CodeValueVO>();
				// -- 取得該請款代碼下所有特店代號
				for (Iterator<CodeValueVO> it = list.iterator(); it.hasNext();) {
					CodeValueVO vo = (CodeValueVO) it.next();
					List<CodeValueVO> tempList = ProjServices.getMfesUcsApplyCodeQS().getMerchId(vo.getItemValue());
					merchList.addAll(tempList);
				}
				// -- 查詢各特店代號下所有端末機代號
				for (Iterator<CodeValueVO> itr = merchList.iterator(); itr.hasNext();) {
					CodeValueVO vo = (CodeValueVO) itr.next();
					List<CodeValueVO> tempList = ProjServices.getMfesUcsBatchHeaderQS()
							.getUncloseTerminalId(vo.getItemCode());
					terminalList.addAll(tempList);
				}
				super.setResultList(terminalList);
				master.setFunctionCount(terminalList.size());
				actionResult = "Y"; // Y:執行成功
			} else {
				actionResult = "Z"; // Z: 非MFES特店, 無相關資料
				master.setFunctionCount(0);
			}
			master.setSuccessFlag("Y");

			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { "發生錯誤, 請洽系統管理者, 代碼: close_stat" });
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	public String getActionResult() {
		return actionResult;
	}

	public void setActionResult(String actionResult) {
		this.actionResult = actionResult;
	}

	public String getMchtNo() {
		return mchtNo;
	}

	public void setMchtNo(String mchtNo) {
		this.mchtNo = mchtNo;
	}

	public String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghigklmnopqrstuvwxyz1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 20) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}
}
