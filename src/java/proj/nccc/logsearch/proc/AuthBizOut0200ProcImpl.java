package proj.nccc.logsearch.proc;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.edstw.lang.DateString;
import com.edstw.persist.PersistableManager;
import com.edstw.persist.PersistableTransaction;
import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.process.ProcessException;
import com.edstw.service.ServiceException;
import com.edstw.util.ValidateUtil;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.AuthLogData;
import proj.nccc.logsearch.persist.BinBankParm;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.user.ProjUserInfo;
import proj.nccc.logsearch.user.ProjUserProfile;
import proj.nccc.logsearch.vo.ManualAuthorizerArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author APAC\czrm4t $
 * @version $Revision: 1.1 $
 */
public class AuthBizOut0200ProcImpl extends AbstractBaseCRUDProc implements AuthBizOut0200Proc {
	private static final Logger log = LogManager.getLogger(AuthBizOut0200ProcImpl.class);

	public String getServiceName() {
		return "AuthBizOut0200ProcImpl";
	}

	public void setServiceParams(Map arg0) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseCRUDQS getBaseCRUDQS() {
		return ProjServices.getAuthLogDataQS();
	}

	// 補登, insertAuthLog
	public boolean insertAuthLogInAdd(ManualAuthorizerArg entity, String sAuthManuEntry) {
		entity.setTransType("AP");
		return this.insertAuthLog(entity, true, sAuthManuEntry);
	}

	// B24 IP, insertAuthLog when receive B24
	public boolean insertAuthLogWhenB24(ManualAuthorizerArg entity, String sAuthManuEntry) {
		entity.setTransType("IP");
		return this.insertAuthLog(entity, false, sAuthManuEntry);
	}

	// 一般交易, insertAuthLog
	public boolean insertAuthLogIn0200(ManualAuthorizerArg entity, String sAuthManuEntry) {
		entity.setTransType("AP");
		entity.setMessageType(null);
		return this.insertAuthLog(entity, false, sAuthManuEntry);
	}

	// 一般交易-取消0220, insertAuthLog
	public boolean insertAuthLogIn0220(ManualAuthorizerArg entity) {
		entity.setTransType("AP");
		entity.setConditionCode("C");
		entity.setMessageType(null);
		return this.insertAuthLog(entity, false, "C");
	}

	// bAddFlag : 補登 Y/N
	// sAuthManuEntry : 交易選擇, Y/N/C/.....
	// cancelApprovalNo : 取消交易的授權碼
	private boolean insertAuthLog(ManualAuthorizerArg entity, boolean bAddFlag, String sAuthManuEntry) {
		// 授權結果寫入AUTH_LOG_DATA
		// insert AuthLog
		PersistableManager pm = ProjServices.getAuthLogDataProc().currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {

			pt.begin();
			AuthLogData authLog = new AuthLogData();
			authLog.setBankNo(entity.getCardBankNo());
			authLog.setCardNo(entity.getCardNo());
			// authLog.setMajorCardId (arg.getMajorCardId ());
			if (bAddFlag) {
				if (sAuthManuEntry.equals("N"))
					entity.setApprovalNo("N00000");
				else if (sAuthManuEntry.equals("P"))
					entity.setApprovalNo("P00000");
				else if (sAuthManuEntry.equals("R"))
					entity.setApprovalNo("R00000");
				else if (sAuthManuEntry.equals("A"))
					entity.setApprovalNo("A00000");
				else if (sAuthManuEntry.equals("D"))
					entity.setApprovalNo("D00000");
				else if (sAuthManuEntry.equals("C"))
					entity.setCancelApprovalNo(entity.getApprovalNo());
				// "Y" FROM SCREEN ENTER

			} else {
				if (sAuthManuEntry.equals("C")) {
					entity.setApprovalNo(entity.getCancelApprovalNo());
					authLog.setConditionCode(entity.getConditionCode());
				} else if (sAuthManuEntry.equals("N"))
					entity.setApprovalNo("N00000");
			}
			// 20090204 PF4狀況欄顯示condition code
			if (ValidateUtil.isBlank(authLog.getConditionCode()))
				authLog.setConditionCode(sAuthManuEntry);
			else
				authLog.setConditionCode(entity.getConditionCode());
			authLog.setMerchantNo(entity.getMerchantNo());
			authLog.setTerminalNo("VOIC  ##");
			authLog.setApprovalNo(entity.getApprovalNo());
			authLog.setCancelApprovalNo(entity.getCancelApprovalNo());
			authLog.setMessageType(entity.getMessageType());
			// authLog.setConditionCode (entity.getConditionCode());
			log.info("MsgType=" + authLog.getMessageType() + ":" + authLog.getConditionCode() + ":"
					+ authLog.getApprovalNo());
			authLog.setPurchaseDate(entity.getPurchaseDate());
			authLog.setPurchaseTime(entity.getPurchaseTime());
			authLog.setPurchaseAmt(entity.getPurchaseAmt());
			Date today = new Date();
			authLog.setProcessDate(MyDateUtil.toString(today, MyDateUtil.YYYYMMDD));
			authLog.setProcessTime(MyDateUtil.toString(today, MyDateUtil.HHMMSS));
			authLog.setExpireDate(entity.getExpireDate());
			authLog.setTransType(entity.getTransType());
			authLog.setAuthorizeReason(entity.getAuthorizeReason());
			authLog.setAcquireId(entity.getAcquireId());
			authLog.setAcquireBk(entity.getAcquireBk());
			authLog.setCountryName("TW");

			authLog.setMccCode(entity.getMccCode());
			authLog.setReturnHostCode(entity.getReturnHostCode());
			authLog.setReturnIsoCode(entity.getReturnIsoCode());
			authLog.setDataSource("4");
			authLog.setEntryMode("00");
			authLog.setTwoScreenApTx("Y");
			authLog.setMessageType(entity.getMessageType());
			authLog.setCardType(entity.getCardType());
			authLog.setCardKind(entity.getCardKind());
			authLog.setBankNo2(entity.getCardBankNo2());
			// 20081201 Stephen 補登交易 , ManualForcePostFlag='N'
			if (bAddFlag)
				authLog.setManualForcePostFlag("N");
			else
				authLog.setManualForcePostFlag(entity.getManualForcePostFlag());
			// authLog.setManualEntryResult (entity.getAuthorizeMemo());
			if (entity.getTransType() != null && entity.getTransType().equals("IP")) {
				authLog.setManualEntryResult(null);
				authLog.setManualForcePostFlag("N");
			} else
				authLog.setManualEntryResult(sAuthManuEntry);

			// 20090107 AUTH_LOG_DATA之BANK_NO,BIN_NO,BANK_NO_2資料補入
			BinBankParm bbp = ProjServices.getSetlBinDatasQS().getBinBankParmByBinNo(entity.getCardNo());
			entity.setBinNo(entity.getCardNo().substring(0, 6));
			if (bbp == null) {
				authLog.setBankNo("956");
				authLog.setBankNo2("99");
			} else {
				authLog.setBankNo(bbp.getBankId());
				authLog.setBankNo2(bbp.getMainId());
			}
			authLog.setRemark(entity.getAuthorizeMemo());
			authLog.setFileDate(new DateString());
			authLog.setModProgramId("MGOA800");
			authLog.setUpdateInfo(AlterInfo.createAlterInfo());
			String deptId = null;
			String userId = null;
			ProjUserInfo ui = null;
			ProjUserProfile up = (ProjUserProfile) ProjUserProfile.getCurrentUserProfile();
			if (up != null) {
				ui = (ProjUserInfo) up.getUserInfo();
				if (StringUtils.isNotBlank(ui.getDepartmentID()) && ui.getDepartmentID().trim().length() >= 3) {
					deptId = ui.getDepartmentID().substring(0, 3);
				}
				if (StringUtils.isNotBlank(ui.getUserID())) {
					userId = ui.getUserID();
				}
			}
	        authLog.setUpdateId(userId);
			authLog.setDeptId(deptId);
			pm.persist(authLog);

			pt.commit();
			pm.close();
		} catch (Exception e) {
			if (pt != null && pt.isActive())
				pt.rollback();
			getLog().error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		} finally {
			if (pm != null)
				pm.close();
		}
		return true;
	}

	@Override
	public EmvProjPersistable createEmptyProjPersistable() {
		return new AuthLogData();
	}

	@Override
	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity) throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
	}

}