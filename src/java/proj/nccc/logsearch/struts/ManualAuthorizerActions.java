package proj.nccc.logsearch.struts;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.lang.DoubleString;
import com.edstw.user.UserLogger;
import com.edstw.util.ValidateUtil;
import com.hp.nccc.iso8583.core.ISO8583VO;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.Active0200Util;
import proj.nccc.logsearch.beans.CardNumLogicCheckBean;
import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.parse.ISOUtil;
import proj.nccc.logsearch.persist.BinBankParm;
import proj.nccc.logsearch.persist.BinBinoParm;
import proj.nccc.logsearch.proc.AuthBizOut0200Proc;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.qs.AuthRuleQSImpl.AuthRuleException;
import proj.nccc.logsearch.user.ProjUserInfo;
import proj.nccc.logsearch.user.ProjUserProfile;
import proj.nccc.logsearch.vo.ManualAuthorizerArg;
import proj.nccc.logsearch.vo.MerchantVO;
import proj.nccc.logsearch.vo.NcccParm;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author Stephen Lin $
 * @version $Revision: 1.6 $
 */
public class ManualAuthorizerActions extends BaseCRUDActions {

	private static final long serialVersionUID = 5151627263282106729L;
	private ManualAuthorizerArg entity;
	private static final Logger log = LogManager.getLogger(ManualAuthorizerActions.class);

	public void setEntity(ManualAuthorizerArg entity) {
		this.entity = entity;
	}

	public ManualAuthorizerArg getEntity() {
		return entity;
	}

	public ManualAuthorizerActions() {

		this.setEntity(new ManualAuthorizerArg());
	}

	protected ProjPersistableArg createEmptyAuthPersistableArg() {

		return new ManualAuthorizerArg();
	}

	@Override
	protected BaseCRUDProc getBaseCRUDProc() {
		return ProjServices.getAuthLogDataProc();
	}

	// 授權室授權 - 第一頁 submit vvvvv
	public String queryAuthorize() {

		try {

			// 人工授權資料登錄(APLog的query_input)
			LogMaster master = super.currentApLogManager().getLogMaster();
			ManualAuthorizerArg arg = (ManualAuthorizerArg) this.getEntity();
			if (ValidateUtil.isNotBlank(arg.getMerchantNo()))
				master.addQueryInput("特店代號", arg.getMerchantNo());
			if (ValidateUtil.isNotBlank(arg.getCardNo1()))
				master.addQueryInput("卡號1", arg.getCardNo1());
			if (ValidateUtil.isNotBlank(arg.getCardNo2()))
				master.addQueryInput("卡號2", "****");
			if (ValidateUtil.isNotBlank(arg.getCardNo3()))
				master.addQueryInput("卡號3", "****");
			if (ValidateUtil.isNotBlank(arg.getCardNo4()))
				master.addQueryInput("卡號4", arg.getCardNo4());
			if (ValidateUtil.isNotBlank(arg.getCardNo5()))
				master.addQueryInput("卡號5", arg.getCardNo5());
			if (ValidateUtil.isNotBlank(arg.getExpireDate()))
				master.addQueryInput("有效日期(YYMM 西元年)", arg.getExpireDate());
			if (ValidateUtil.isNotBlank(arg.getPurchaseAmt().toString()))
				master.addQueryInput("金額", arg.getPurchaseAmt().toString());
			if (ValidateUtil.isNotBlank(arg.getProcessDate()))
				master.addQueryInput("授權日期", arg.getProcessDate());
			if (ValidateUtil.isNotBlank(arg.getProcessTime()))
				master.addQueryInput("授權時間", arg.getProcessTime());
			if (ValidateUtil.isNotBlank(arg.getAddFlag()))
				master.addQueryInput("補登交易", arg.getAddFlag());
			if (ValidateUtil.isNotBlank(arg.getCvc2()))
				master.addQueryInput("CVC2", ISOUtil.getCVC2Mask(arg.getCvc2()));
			
			ProjUserInfo ui = null;
			ProjUserProfile up = (ProjUserProfile) ProjUserProfile.getCurrentUserProfile();
			if (up != null)
				ui = (ProjUserInfo) up.getUserInfo();

			if (up == null || ui == null || ui.getUserID() == null) {
				super.saveError("action.fail", new Object[] { "sesson timeout !!" });
				return ProjConstants.FAIL_KEY;
			}
			String sBankNo = "";
			if (ui.isBankUser()) {
				sBankNo = ui.getBankNo();
			}
			try {
				Integer.parseInt(sBankNo);
				super.saveError("action.fail", new Object[] { "銀行：" + sBankNo + "不可執行此一功能 !!" });
				return ProjConstants.FAIL_KEY;
			} catch (Exception xxx) {
			}

			ServletActionContext.getRequest().getSession().removeAttribute("MustView");
			ManualAuthorizerArg entity = (ManualAuthorizerArg) this.getEntity();
			// 補登交易
			boolean bAddFlag = false;
			/* M2010043 CUP 發卡專案 */
			if (entity.getAddFlag() != null && entity.getAddFlag().equals("Y"))
				bAddFlag = true;
			if (ValidateUtil.isNumber(entity.getCardNo1(), true) && ValidateUtil.isNumber(entity.getCardNo2(), true)
					&& ValidateUtil.isNumber(entity.getCardNo3(), true)
					&& ValidateUtil.isNumber(entity.getCardNo4(), true)) {
				if (ValidateUtil.isNumber(entity.getCardNo5(), true)) {
					StringBuffer cardNobuf = new StringBuffer(entity.getCardNo1().trim())
							.append(entity.getCardNo2().trim()).append(entity.getCardNo3().trim())
							.append(entity.getCardNo4().trim()).append(entity.getCardNo5().trim());
					entity.setCardNo(cardNobuf.toString());

				} else {
					StringBuffer cardNobuf = new StringBuffer(entity.getCardNo1().trim())
							.append(entity.getCardNo2().trim()).append(entity.getCardNo3().trim())
							.append(entity.getCardNo4().trim());
					entity.setCardNo(cardNobuf.toString());
				}
				String cardNo = entity.getCardNo();

				/** 特店資料檢核 */
				MerchantVO mVo = null;
				entity.setQrMcht("N");
				entity.setRealMerchantNo(entity.getMerchantNo());
				boolean motoTx = false;

				// M2018165 品牌需求
				String iduCode = null;

				if (ValidateUtil.isNotEqual(entity.getMerchantNo(), "0000000000")
						&& entity.getMerchantNo().length() >= 10) {
					// M2018066 QR主掃
					if (entity.getMerchantNo().length() == 16) {
						mVo = ProjServices.getMerchantQS().queryQrMerchantBase(entity.getMerchantNo());
						entity.setQrMcht("Y");

					} else if (entity.getMerchantNo().length() >= 10) {
						mVo = ProjServices.getMerchantQS().queryMerchantBase(entity.getMerchantNo());
						entity.setQrMcht("N");
					}

					if (mVo == null) {
						this.setEntity(entity);
						super.saveError("msg.warning", new Object[] { "無此特店！資料不存在！" });
						return ProjConstants.FAIL_KEY;
					}
					iduCode = mVo.getIndustryCode();
					// M2018165 品牌需求
					if (iduCode.equals("016") || iduCode.equals("316") || iduCode.equals("006")) {
						motoTx = true;
					}

					entity.setRealMerchantNo(mVo.getMchtNo());
				}

				// M2018066 QR特店僅可進行補登功能-start
				if (entity.getQrMcht().equals("Y") && !bAddFlag) {
					super.saveError("msg.warning", new Object[] { "本功能不接受此QR特店交易!" });
					return ProjConstants.FAIL_KEY;
				}

				// 銀聯卡flag、銀聯特店flag
				boolean isCupCard = ProjServices.getSetlBinDatasQS().isCupCard(cardNo);
				boolean isCupMcht = ProjServices.getMerchantQS().isCupMcht(entity.getRealMerchantNo());
				boolean isDfsMcht = ProjServices.getMerchantQS().isDfsMcht(entity.getRealMerchantNo());
				boolean isTwCupCard = false;
				// M2013115
				boolean isForeignCupCard = false;
				// M2013115
				boolean isPureCupCard = false;
				// M2010043國外銀聯卡判斷
				String binNo = cardNo.substring(0, 6);
				BinBinoParm binParm = ProjServices.getBinBinoParmQS().getBinParm(cardNo, new java.util.Date());
				/* M2015042 MasterCard 新增bin range 222100~272099 */
				if ((((cardNo.startsWith("3") || cardNo.startsWith("4") || cardNo.startsWith("5"))
						|| (binNo.compareTo("222100") >= 0 && binNo.compareTo("272099") <= 0)) && cardNo.length() > 16)
						&& !isCupCard) {
					super.saveError("msg.warning", new Object[] { "卡號長度不可大於16碼！" });
					return ProjConstants.FAIL_KEY;
				}
				if (((cardNo.startsWith("34") || cardNo.startsWith("37")) && cardNo.length() != 15) && !isCupCard) {
					super.saveError("msg.warning", new Object[] { "AE卡長度需為15碼！" });
					return ProjConstants.FAIL_KEY;
				}
				// if (cardNo.startsWith("36") && cardNo.length() == 14) {
				// super.saveError("msg.warning", new Object[] {
				// "本功能不接授大來卡授權！"
				// });
				// return ProjConstants.FAIL_KEY;
				// }
				if (isCupCard) {
					if (binParm == null) {
						isForeignCupCard = true;
					} else {
						isTwCupCard = true;
					}
				}
				// M2017128_銀聯國際81卡號開頭與8碼BIN
				if (cardNo.startsWith("81")) {
					isForeignCupCard = true;
					isCupCard = true;
				}
				// M2010043此步驟往上移 String binNo=cardNo.substring(0, 6);
				if (ProjServices.getBinBankParmQS().isMemberBank(cardNo))
					entity.setMemberBank("Y");
				else
					entity.setMemberBank("N");
				// M2010043此步驟往上移 BinBinoParm
				// binParm=ProjServices.getBinBinoParmQS().getBinParm(binNo, new
				// java.util.Date());
				// as default
				String cardType = " ";
				// String cardKind=" ";
				String cardBankNo = "   ";
				String cardBankNo2 = "  ";
				String cardBankName = "";
				String infoTel = "";
				String infoTel1 = "";
				String tokenflag = "";
				/* M2016073 TSP專案 */
				if (binParm != null) {
					cardType = binParm.getCardType();
					// cardKind=binParm.getCardKind();
					BinBankParm binBankParm = binParm.getBinBank();
					cardBankNo = binBankParm.getBankId();
					cardBankNo2 = binBankParm.getMainId();
					cardBankName = binBankParm.getAbbrName();
					// infoTel=binBankParm.getIBussTel();
					// infoTel1=binBankParm.getABussTel();
					infoTel = binBankParm.getAuthTel1();
					infoTel1 = binBankParm.getAuthTel2();
					tokenflag = binParm.getTokenFlag();
					/* M2016073 TSP專案 */
				} else {
					cardType = CardNumLogicCheckBean.simpleCheckCardType(cardNo);

					if (cardType == null && isCupCard) {

						/* 純銀聯才卡別才會是'C'雙幣別卡都會放國際組織卡別 */
						cardType = "C";
					}

					if (ValidateUtil.isBlank(cardType)) {
						super.saveError("msg.warning", new Object[] { "查無卡別, 卡號錯誤 !!" });
						return ProjConstants.FAIL_KEY;
					}
				}
				// entity.setCardKind(cardKind);
				entity.setCardType(cardType);
				entity.setCardBankNo(cardBankNo);
				entity.setCardBankNo2(cardBankNo2);
				entity.setCardBankName(cardBankName);
				entity.setIssueInfoTel(infoTel);
				entity.setIssueInfoTel1(infoTel1);
				entity.setPurchaseDate(entity.getProcessDate());
				entity.setPurchaseTime(entity.getProcessTime());
				if (ProjServices.getMerchantQS().isRecurring(entity.getRealMerchantNo()))
					entity.setRecurringFlag("Y");
				else
					entity.setRecurringFlag("N");

				/*
				 * M2020178 CVC2異動為選擇性欄位 log.info("check cvc2 before--recurring flag=" +
				 * entity.getRecurringFlag()); if (entity.getCvc2().length() == 0) { if
				 * ((entity.getCardType().equals("T") || entity.getCardType().equals("D")) &&
				 * !bAddFlag && (motoTx || (entity.getRecurringFlag() != null &&
				 * entity.getRecurringFlag().equals("Y")))) { super.saveError("msg.warning", new
				 * Object[] { "資料錯誤：CVC2未輸入" }); return ProjConstants.FAIL_KEY; } } M2020178
				 * CVC2異動為選擇性欄位
				 */
				/* 20191104 for 客服榆清要求CVC2防呆 */
				if (entity.getCvc2().length() > 0 && entity.getCvc2() != null) {
					if (entity.getCardType().equals("A") && (entity.getCvc2().length() != 4)) {
						super.saveError("msg.warning", new Object[] { "資料錯誤：CVC2必須輸入4碼" });
						return ProjConstants.FAIL_KEY;
					} else if (!entity.getCardType().equals("A") && (entity.getCvc2().length() != 3)) {
						super.saveError("msg.warning", new Object[] { "資料錯誤：CVC2必須輸入3碼" });
						return ProjConstants.FAIL_KEY;
					}
				}
				/* 20191104 for 客服榆清要求CVC2防呆 */

				if (entity.getCardType().equals("A"))
					entity.setMemberBank("N");
				/* M2013115純銀聯卡旗標 */
				if (entity.getCardType().equals("C")) {
					isPureCupCard = true;
					entity.setPureCupCard("Y");
				}
				log.info("卡別[" + entity.getCardType() + "]");
				try {
					if (isPureCupCard && isForeignCupCard) {
						/* M2013115哲勳表示只有國外的純銀聯是不查卡號邏輯的 */
					} else {
						ProjServices.getAuthRuleQS().isCorrectCardNumber(entity.getCardNo());
					}
				} catch (Exception e) {
					if (isForeignCupCard) {
						isPureCupCard = true;
						entity.setPureCupCard("Y");
						entity.setCardType("C");
						cardType = "C";
					} else {
						super.saveError("msg.warning", new Object[] { "卡號" + cardNo + "檢核錯誤 !!" });
						return ProjConstants.FAIL_KEY;
					}
				}
				boolean isAcptTyp = false;
				if (entity.getMerchantNo().length() >= 10) {
					mVo = ProjServices.getMerchantQS().queryMerchantBase(entity.getRealMerchantNo(), cardType);

					isAcptTyp = ProjServices.getMerchantQS().isSuitableCardForMcht(entity.getRealMerchantNo(),
							cardType);
				}
				if ((isCupCard) && (!isPureCupCard) && (!isAcptTyp && isCupMcht)) {
					isPureCupCard = true;
					entity.setPureCupCard("Y");
					entity.setCardType("C");
					cardType = "C";
				}
				if (entity.getCardType().equals("D") && !bAddFlag) {
					if (isDfsMcht) {
						if (motoTx || (entity.getRecurringFlag() != null && entity.getRecurringFlag().equals("Y"))) {
							// DFS僅MO/TO recurring可接受連線授權
						} else {
							super.saveError("msg.warning", new Object[] { "本功能不接受此類DFS卡交易!" });
							return ProjConstants.FAIL_KEY;
						}
					} else {
						super.saveError("msg.warning", new Object[] { "特店未簽該卡別 !!" });
						return ProjConstants.FAIL_KEY;
					}
				}

				// M2013115 國內銀聯卡僅可進行補登功能-start
				if (isTwCupCard && !bAddFlag) {
					if (isCupMcht)
						super.saveError("msg.warning", new Object[] { "本功能不接受此類銀聯卡交易!(國內銀聯卡)" });
					else
						super.saveError("msg.warning", new Object[] { "卡號錯誤 !!特店未簽該卡別 !!" });
					return ProjConstants.FAIL_KEY;
				}
				// M2013115 國外純銀聯卡僅可進行補登功能-start
				if (isForeignCupCard && isPureCupCard && !bAddFlag) {
					if (isCupMcht)
						super.saveError("msg.warning", new Object[] { "本功能不接受此類銀聯卡交易！" });
					else
						super.saveError("msg.warning", new Object[] { "卡號錯誤 !!特店未簽該卡別 !!" });
					return ProjConstants.FAIL_KEY;
				}
				// M2016073 TSP專案 token BIN僅可進行補登功能-start
				if (tokenflag != null && tokenflag.equals("Y") && !bAddFlag) {
					super.saveError("msg.warning", new Object[] { "本功能不接受此類虛擬卡交易！" });
					return ProjConstants.FAIL_KEY;
				}
				if (tokenflag != null && tokenflag.equals("Y")) {
					entity.setTokenFlag(true);
				}
				// M2016073 TSP專案 token BIN僅可進行補登功能-end

				if (bAddFlag && (entity.getRealMerchantNo().length() == 4 || entity.getRealMerchantNo().length() == 6
						|| entity.getRealMerchantNo().length() == 7
						|| entity.getRealMerchantNo().equals("0000000000"))) {
					/** 補登交易, 特店代碼長度4,6,7 或10個0 , 不檢核特店資訊 */
					entity.setMccCode("");
					entity.setActionStep("ADDFLAG");
					mVo = null;
					/** 取得收單銀行名稱 */
					// 20090113 補登不查核bin
					// entity.setAcqName(
					// ProjServices.getAuthRuleQS().getAcqAbbrName(merchantNo)
					// );
					entity.setAcqName(ProjServices.getAuthRuleQS().getAcqAbbrNamePF8(entity.getRealMerchantNo()));
				} else {
					// 檢核特店簽約卡別
					ProjServices.getAuthRuleQS().toValidMerchantDataPF8(entity.getRealMerchantNo(), "AP", cardType);
					// 非補登交易
					if (!bAddFlag) {
						// default
						entity.setActionStep("PREAPPROVE");
						// 聯銀卡特店判斷
						if (isForeignCupCard && isCupMcht) {
							entity.setActionStep("NONCENTERAUTH");
							super.saveMessage("msg.warning", new Object[] { "本功能不接受此類銀聯卡交易！(國際卡-'" + cardType + "')" });
						}
						entity.setAuthorizeReason("C1");
						// log.info("國內銀聯卡:"+isTwCupCard);
						if (!entity.getCardType().equals("A")) {
							/** 會員、非會員判斷 */
							if (entity.getMemberBank().equals("Y"))
							// 會員銀行

							{
								if (!ProjServices.getMerchantQS().isAuthByCentralize(entity.getRealMerchantNo()))
								// 非集中授權特店

								{
									entity.setActionStep("NONCENTERAUTH");
									if (isTwCupCard)
									// M2010043 國內銀聯卡

									{
										super.saveMessage("msg.warning", new Object[] { "此特店不適用集中授權" });
										super.saveMessage("msg.warning", new Object[] { "國內銀聯卡" });
									} else {
										super.saveMessage("msg.warning", new Object[] { "此特店不適用集中授權" });
									}
								} else // 集中授權特店

								{
									entity.setActionStep("PREAPPROVE");
									if (isTwCupCard)
									// M2010043國內銀聯卡

									{
										entity.setActionStep("NONCENTERAUTH");
										super.saveMessage("msg.warning", new Object[] { "此特店不適用集中授權" });
										super.saveMessage("msg.warning", new Object[] { "國內銀聯卡" });
									} else {
										super.saveMessage("msg.warning", new Object[] { "此特店適用集中授權" });
									}
									/* M2011006哲勳要求針對會員VISA集中特店要SHOW 警訊 */
									if (entity.getCardType().equals("V")) {

										if (!(iduCode.equals("008") || // M2016016新增EC行業別
												iduCode.equals("308") || // M2016016新增EC行業別
												iduCode.equals("608") || // M2016016新增EC行業別
												iduCode.equals("908") || // M2016016新增郵購行業別
												iduCode.equals("316") || iduCode.equals("016") || iduCode.equals("018")
												|| iduCode.equals("210") || iduCode.equals("211")
												|| iduCode.equals("216") || iduCode.equals("229")
												|| iduCode.equals("291") || iduCode.equals("331")
												|| iduCode.equals("991") || iduCode.equals("299")
												|| iduCode.equals("206") || iduCode.equals("250")
												|| iduCode.equals("233") || iduCode.equals("226"))
												&& (mVo.getEdcFlag().equals("Y") || mVo.getPosFlag().equals("Y"))) {
											super.saveMessage("msg.warning",
													new Object[] { "VISA 卡於POS/EDC店消費, 需先查詢交易記錄，再依狀況授權!!" });
										}
									} else if (entity.getCardType().equals("T")) {

										if (!(iduCode.equals("008") || // M2016016新增EC行業別
												iduCode.equals("308") || // M2016016新增EC行業別
												iduCode.equals("608") || // M2016016新增EC行業別
												iduCode.equals("908") || // M2016016新增郵購行業別
												iduCode.equals("316") || iduCode.equals("016") || iduCode.equals("018")
												|| iduCode.equals("210") || iduCode.equals("211")
												|| iduCode.equals("216") || iduCode.equals("229")
												|| iduCode.equals("291") || iduCode.equals("331")
												|| iduCode.equals("991") || iduCode.equals("299")
												|| iduCode.equals("206") || iduCode.equals("250")
												|| iduCode.equals("233") || iduCode.equals("226"))
												&& (mVo.getEdcFlag().equals("Y") || mVo.getPosFlag().equals("Y"))) {
											super.saveMessage("msg.warning",
													new Object[] { "品牌卡於POS/EDC店消費, 需先查詢交易記錄，再依狀況授權!!" });
										}
									}
								}
							} else // 非會員銀行

							{
								// //M2010087人工授權交易國外卡監控作業增加CALLBANK 參數 START
								Long inLimit = entity.getPurchaseAmt().longValue();
								DoubleString callBankLimit = ProjServices.getCallBankParmQS()
										.getCallBankParmAmt(mVo.getMchtNo());
								if (binParm == null && callBankLimit != null && inLimit > callBankLimit.longValue()) {
									super.saveMessage("msg.warning",
											new Object[] { "外國卡交易，控管金額" + callBankLimit + "元!" });
								}
								if (isTwCupCard)
								// M2010043 國內銀聯卡

								{
									entity.setActionStep("NONCENTERAUTH");
									super.saveMessage("msg.warning", new Object[] { "國內銀聯卡!!" });
								}
								if (entity.getCardType().equals("V")) {

									if (!(iduCode.equals("008") || // M2016016新增EC行業別
											iduCode.equals("308") || // M2016016新增EC行業別
											iduCode.equals("608") || // M2016016新增EC行業別
											iduCode.equals("908") || // M2016016新增郵購行業別
											iduCode.equals("316") || iduCode.equals("016") || iduCode.equals("018")
											|| iduCode.equals("210") || iduCode.equals("211") || iduCode.equals("216")
											|| iduCode.equals("229") || iduCode.equals("291") || iduCode.equals("331")
											|| iduCode.equals("991") || iduCode.equals("299") || iduCode.equals("206")
											|| iduCode.equals("250") || iduCode.equals("233") || iduCode.equals("226"))
											&& (mVo.getEdcFlag().equals("Y") || mVo.getPosFlag().equals("Y"))) {
										entity.setActionStep("NONCENTERAUTH");
										super.saveMessage("msg.warning",
												new Object[] { "VISA 卡於POS/EDC店消費, 先查詢交易記錄!" });
									}
								} // M2018168品牌卡專案
								else if (entity.getCardType().equals("T")) {

									if (!(motoTx || (entity.getRecurringFlag() != null
											&& entity.getRecurringFlag().equals("Y")))// 定期性繳款特店
									) {
										entity.setActionStep("NONCENTERAUTH");
										super.saveMessage("msg.warning", new Object[] { "品牌卡於POS/EDC店消費, 先查詢交易記錄!" });
									}
								}
								
								// M2022061_大來(Diners)國內發卡
								// 非集中授權特店
								if (!ProjServices.getMerchantQS().isAuthByCentralize(entity.getRealMerchantNo())) {
									if (entity.getCardType().equals("D")) {
										String[] iduList = { "016", "216", "316", "006", "250", "210", "211", "226",
												"240", "251", "290", "291", "008", "308" };
										if (Arrays.asList(iduList).indexOf(iduCode) < 0
												&& (mVo.getEdcFlag().equals("Y") || mVo.getPosFlag().equals("Y"))) {
											entity.setActionStep("NONCENTERAUTH");
											super.saveMessage("msg.warning", new Object[] { "此特店不適用集中授權" });
											super.saveMessage("msg.warning",
													new Object[] { "大來卡國內卡於POS/EDC店消費，先查詢交易記錄!!" });
										}
									}
								}
							}
							/****
							 * M2011006 MASTER/ JCB卡哲勳表示不論是否為會員銀行皆比照VISA辦理提醒先查交易LOG但不需要強制授權
							 */
							if (entity.getCardType().equals("M") || entity.getCardType().equals("J")) {
								/*** M2011006 哲勳表示會員MJ在非集中授權特店不要SHOW警訊 */
								if ((entity.getMemberBank().equals("Y")) && (!ProjServices.getMerchantQS()
										.isAuthByCentralize(entity.getRealMerchantNo()))) {
									/* do nothing */
								} else {

									if (!(iduCode.equals("008") || // M2016016新增EC行業別
											iduCode.equals("308") || // M2016016新增EC行業別
											iduCode.equals("608") || // M2016016新增EC行業別
											iduCode.equals("908") || // M2016016新增郵購行業別
											iduCode.equals("316") || iduCode.equals("016") || iduCode.equals("018")
											|| iduCode.equals("210") || iduCode.equals("211") || iduCode.equals("216")
											|| iduCode.equals("229") || iduCode.equals("291") || iduCode.equals("331")
											|| iduCode.equals("991") || iduCode.equals("299") || iduCode.equals("206")
											|| iduCode.equals("250") || iduCode.equals("233") || iduCode.equals("226"))
											&& (mVo.getEdcFlag().equals("Y") || mVo.getPosFlag().equals("Y"))) {
										super.saveMessage("msg.warning",
												new Object[] { "MASTER/JCB 卡於POS/EDC店消費, 需先查詢交易記錄，再依狀況授權!!" });
									}
								}
							}
						}
						// 非補登交易之日期時間要重抓
						entity.setPurchaseDate(MyDateUtil.getSysDateTime(MyDateUtil.YYYYMMDD));
						entity.setPurchaseTime(MyDateUtil.getSysDateTime(MyDateUtil.HHMMSS));
						entity.setProcessDate(MyDateUtil.getSysDateTime(MyDateUtil.YYYYMMDD));
						entity.setProcessTime(MyDateUtil.getSysDateTime(MyDateUtil.HHMMSS));
					} else {
						entity.setActionStep("ADDFLAG");
						// 20081222 德勝:補登第二頁索取授權碼預設值為否
						entity.setGetAuthCode("N");
					}
					// 授權室授權一律為AP交易
					/** 卡片為AE卡不做特店的限額檢核 */
					boolean bToCheckMchtLimit = false;
					// !entity.getCardType().equals("A"))
					if (mVo.getSepcialTreatyLimit().longValue() != 999999)
						bToCheckMchtLimit = true;
					// 特店為AE Only 不做特店的限額檢核 */
					if (mVo.getContractType() != null && mVo.getContractType().trim().length() <= 2
							&& mVo.getContractType().indexOf("A") >= 0)
						bToCheckMchtLimit = false;
					if (bToCheckMchtLimit) {
						Long inLimit = entity.getPurchaseAmt().longValue();
						Long checkLimit = Long.parseLong(mVo.getSepcialTreatyLimit().toString());
						// mVo.getSepcialTreatyLimit()!=null)
						if (inLimit > checkLimit) {
							super.saveMessage("msg.warning", new Object[] { "該特店單筆交易金額不得超過" + checkLimit + "元" });
							return ProjConstants.FAIL_KEY;
						}
						if (entity.getRealMerchantNo().substring(2, 5).equals("251") && inLimit > 100000) {
							super.saveMessage("msg.warning", new Object[] { "該行業別單筆交易金額不得超過十萬元！" });
							return ProjConstants.FAIL_KEY;
						}
					}
					String mEngName = ProjServices.getMerchantQS().merchantEngName(entity.getRealMerchantNo());
					if (mEngName != null)
						mVo.setMchtEngName(mEngName);
					else
						mVo.setMchtEngName("");
					entity.setMccCode(mVo.getMccCode());

				}
				entity.setTerminalNo("VOIC  ##");
				if (entity.getCardType().equals("D") || entity.getCardType().equals("T"))// M2018168
				{
					entity.setAcquireId("650722");
				} else {
					entity.setAcquireId("493817");
				}
				entity.setAcquireBk("99");
				entity.setForceAuth("N");
				entity.setManualForcePostFlag("N");
				/** 特店限額為999999時,畫面顯示空白 */
				if (mVo != null && mVo.getSepcialTreatyLimit().longValue() == 999999)
					mVo.setSepcialTreatyLimit(null);
				entity.setMerchantVO(mVo);
				// 發卡行資訊取得
				entity.setBankNo(cardBankNo);
				ServletActionContext.getRequest().getSession().setAttribute("CARDNO", entity.getCardNo());
				ServletActionContext.getRequest().getSession().setAttribute("manualAUTHINFO", entity);
				this.setEntity(entity);
				return ProjConstants.SUCCESS_KEY;
			} else {
				super.saveError("action.fail", new Object[] { "卡號格式錯誤 !!" });
				return ProjConstants.FAIL_KEY;
			}
		} catch (AuthRuleException e) {
			log.info("AuthRuleException in :" + e.getMessage());
			super.saveError("msg.warning", new Object[] { e.getMessage() });
			return ProjConstants.FAIL_KEY;
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			return ProjConstants.FAIL_KEY;
		}
	}

	// 人工授權資料登錄 - 更正回首頁 vvvv
	public String goHome() {

		super.setRequestAttribute("ACTION", "BANK");
		return ProjConstants.SUCCESS_KEY;
	}

	// 授權室授權 - 取授權碼 vvvv
	public String getApproveNo() {

		try {
			// 人工授權資料登錄(APLog的query_input)
			LogMaster master = super.currentApLogManager().getLogMaster();
			ManualAuthorizerArg arg = (ManualAuthorizerArg) this.getEntity();
			if (ValidateUtil.isNotBlank(arg.getMerchantNo()))
				master.addQueryInput("特店代號", arg.getMerchantNo());
			if (ValidateUtil.isNotBlank(arg.getForceAuth()))
				master.addQueryInput("強制授權", arg.getForceAuth());
			if (ValidateUtil.isNotBlank(arg.getAuthManuEntry()))
				master.addQueryInput("授權接受否", arg.getAuthManuEntry());
			if (ValidateUtil.isNotBlank(arg.getApprovalNo()))
				master.addQueryInput("授權碼", arg.getApprovalNo());
			if (ValidateUtil.isNotBlank(arg.getReturnIsoCode()))
				master.addQueryInput("授權回應碼", arg.getReturnIsoCode());

			ProjUserInfo ui = null;
			ProjUserProfile up = (ProjUserProfile) ProjUserProfile.getCurrentUserProfile();
			if (up != null)
				ui = (ProjUserInfo) up.getUserInfo();

			if (up == null || ui == null || ui.getUserID() == null) {
				super.saveError("action.fail", new Object[] { "sesson timeout !!" });
				return ProjConstants.FAIL_KEY;
			}
			String sBankNo = "";
			if (ui.isBankUser()) {
				sBankNo = ui.getBankNo();
			}
			ManualAuthorizerArg entity = (ManualAuthorizerArg) this.getEntity();
			String cancelFlag = entity.getAuthManuEntry();
			String forceAuth = entity.getForceAuth();
			entity = (ManualAuthorizerArg) ServletActionContext.getRequest().getSession()
					.getAttribute("manualAUTHINFO");
			// 補登交易
			boolean bAddFlag = false;
			if (entity.getAddFlag() != null && entity.getAddFlag().equals("Y"))
				bAddFlag = true;
			String merchantNo = entity.getMerchantNo();
			MerchantVO mVo = entity.getMerchantVO();
			entity.setForceAuth(forceAuth);
			entity.setManualForcePostFlag(forceAuth);
			// log.info("bAddFlag :"+bAddFlag);
			if (bAddFlag && merchantNo != null && (merchantNo.length() == 4 || merchantNo.length() == 6
					|| merchantNo.length() == 7 || merchantNo.equals("0000000000"))) {
				// 補登交易, 特店代碼長度4,6,7 或10個0 , 不檢核特店資訊
				entity.setMccCode("");
			}
			NcccParm ret = null;
			if (!bAddFlag) {
				// 非補登交易
				Active0200Util a0200 = new Active0200Util();
				NcccParm inp = null;
				if (cancelFlag != null && cancelFlag.equals("C")) {
					// 取消交易
					inp = (NcccParm) ServletActionContext.getRequest().getSession().getAttribute("0200NcccParm");
					inp.setCancelFlag("Y");
//					log.info("== 取消交易 =============================");
//					log.info("== approve No = " + inp.getAuthCode());
					// action step
					entity.setActionStep("CANCELAPPROVED");
					entity.setMessageType("0400");
					entity.setConditionCode("C");
				} else {
					inp = new NcccParm();
					inp.setCardType(entity.getCardType());
					/* M2018168卡別 */
					inp.setCardNum(entity.getCardNo());
					/* 卡號 */
					inp.setCardExpireDate(entity.getExpireDate());
					/* 有效日期 */
					inp.setTransAmount(entity.getPurchaseAmt().toString());
					/* 交易金額 */
					inp.setCvv2(entity.getCvc2());
					/* CVV2 */
					inp.setMerchantId(entity.getMerchantNo());
					/* 特店代號 */
					inp.setTerminalId(entity.getTerminalNo());
					/* 端末機代號 */
					inp.setAcquirerId(entity.getAcquireId());
					/* 收單行代號 */
					inp.setMerchantName(mVo.getMchtEngName());
					/* 特店英文名稱 */
					inp.setMccCode(entity.getMccCode());
					/* 行業類別碼 */
					inp.setCity(mVo.getEngCityName());
					/* 程式英文名稱 */
					inp.setEci("0");
					/* EC Flag */
					log.info("== 一般交易 =============================");
					// action step
					entity.setActionStep("APPROVED");
					entity.setMessageType("0210");
					entity.setConditionCode(null);
				}
				if (entity.getRecurringFlag() != null && entity.getRecurringFlag().equals("Y")) {
					inp.setRecurringFlag("Y");
				} else {
					inp.setRecurringFlag("N");
				}
				String iduCode = inp.getMerchantId().substring(2, 5);
				if (entity.getCardType().equals("D") || entity.getCardType().equals("T")) // M2018165 M2018168 品牌/DFS
				{
					if (iduCode.equals("016") || iduCode.equals("316")) {
						inp.setIduToken("2");
					} else if (iduCode.equals("006")) {
						inp.setIduToken("3");
					} else if (entity.getRecurringFlag().equals("Y")) {
						inp.setIduToken("4");
					}
				} else {
					if (iduCode.equals("008") || iduCode.equals("308") || iduCode.equals("608") || iduCode.equals("908")
							|| iduCode.equals("018")) {
						inp.setIduToken("1");
					} else if (iduCode.equals("016") || iduCode.equals("316") || iduCode.equals("206")) {
						inp.setIduToken("2");
					} else if (iduCode.equals("210") || iduCode.equals("211") || iduCode.equals("226")
							|| iduCode.equals("229") || iduCode.equals("250") || iduCode.equals("291")
							|| iduCode.equals("299") || iduCode.equals("991")) {
						inp.setIduToken("4");
					} else {
						inp.setIduToken("0");
					}
				}

//				log.info("inp.cardNum = "+inp.cardNum);
//				log.info("inp.cardExpireDate = "+inp.cardExpireDate);
//				log.info("inp.transAmount	= " + inp.transAmount);
//				log.info("inp.cvv2 = "+inp.cvv2);
//				log.info("inp.merchantId	 = " + inp.merchantId);
//				log.info("inp.terminalId	 = " + inp.terminalId);
//				log.info("inp.acquirerId	 = " + inp.acquirerId);
//				log.info("inp.merchantName   = " + inp.merchantName);
//				log.info("inp.mccCode		= " + inp.mccCode);
//				log.info("inp.recurringFlag  = " + inp.recurringFlag);
//				log.info("inp.iduToken	   = " + inp.iduToken);
//				log.info("inp.city		   = " + inp.city);
//				log.info("=======================================> Send to AuthGateway	   :" + new Date());
				// a0200.setTimeOutSec(10);

				HttpServletRequest request = ServletActionContext.getRequest();
				HttpSession session = request.getSession();

				if (inp.getCancelFlag().equals("Y")) {
					inp.setRequestObj((ISO8583VO) session.getAttribute("REV_OBJ"));
				}
				ret = a0200.sendAts(inp, sBankNo);
//				log.info("=======================================> retrieve from AuthGateway :" + new Date());
				if (ret == null) {
					entity.setActionStep("ATSERROR");
					this.setEntity(entity);
					super.saveError("action.fail", new Object[] { "連線失敗 !!" });
					return ProjConstants.FAIL_KEY;
				} else {
					if (!inp.getCancelFlag().equals("Y")) {
						session.setAttribute("REV_OBJ", ret.getRequestObj());
					}
//					log.info("approve ISOCode38==>   [" + ret.authCode + "]");
//					log.info("Resp	ISOCode39==>   [" + ret.respCode + "]");
					if (ret.getRespCode() != null) {
						if (ret.getRespCode().equals("00"))
							entity.setReturnHostCode("00");
						else {
							if (ret.getAuthCode() != null && ret.getAuthCode().length() > 2)
								entity.setReturnHostCode(ret.getAuthCode().substring(0, 2));
							else
								entity.setReturnHostCode(ret.getAuthCode());
						}
					}
					entity.setReturnIsoCode(ret.getRespCode());
					if (entity.getActionStep().equals("CANCELAPPROVED") && ret.getAuthCode() != null) {
						entity.setCancelApprovalNo(ret.getAuthCode().trim());
						entity.setTransType("IP");
					} else if (entity.getActionStep().equals("APPROVED") && ret.getAuthCode() != null) {
						log.info("set ApproveNo=" + ret.getAuthCode().trim());
						entity.setApprovalNo(ret.getAuthCode().trim());
						entity.setTransType("IP");
					} else {
						super.saveError("action.fail", new Object[] { "unknow Action step !!" });
						return ProjConstants.FAIL_KEY;
					}
					if (entity.getReturnIsoCode().equals("00")) {
						entity.setAuthManuEntry("Y");
						// insert AuthLog when retrieve from B24
						// 只有成功才寫IP
						AuthBizOut0200Proc out0200Proc = ProjServices.getAuthBizOut0200Proc();
						if (entity.getActionStep().equals("APPROVED")) {
							// IP
							out0200Proc.insertAuthLogWhenB24(entity, entity.getAuthManuEntry());
						} else if (entity.getActionStep().equals("CANCELAPPROVE")) {
							// 取消 ,AP
							out0200Proc.insertAuthLogIn0220(entity);
						}
					} else if (entity.getReturnIsoCode().equals("04") || entity.getReturnIsoCode().equals("41")
							|| entity.getReturnIsoCode().equals("56") || entity.getReturnIsoCode().equals("43")
							|| (entity.getReturnIsoCode().compareTo("34") >= 0
									&& entity.getReturnIsoCode().compareTo("37") <= 0))
						entity.setAuthManuEntry("P");
					else if (entity.getReturnIsoCode().equals("01") || entity.getReturnIsoCode().equals("02")
							|| entity.getReturnIsoCode().equals("03") || entity.getReturnIsoCode().equals("58"))
						entity.setAuthManuEntry("R");
					else
						entity.setAuthManuEntry("N");
					/** 當授權回覆碼為'00'時, 授權碼欄位清空 */
					if (!entity.getReturnIsoCode().equals("00"))
						entity.setApprovalNo("");
				}
			} else {
				// action step
				entity.setActionStep("APPROVED");
			}
			// 發卡行資訊取得
			ServletActionContext.getRequest().getSession().setAttribute("manualAUTHINFO", entity);
			ServletActionContext.getRequest().getSession().setAttribute("0200NcccParm", ret);
			this.setEntity(entity);
			return ProjConstants.SUCCESS_KEY;
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			return ProjConstants.FAIL_KEY;
		}
	}

	// 授權室授權 - 寫入AuthLog
	public String createAuthorize() {

		try {
			// 人工授權資料登錄(APLog的query_input)
			LogMaster master = super.currentApLogManager().getLogMaster();
			ManualAuthorizerArg arg = (ManualAuthorizerArg) this.getEntity();
			if (ValidateUtil.isNotBlank(arg.getMerchantNo()))
				master.addQueryInput("特店代號", arg.getMerchantNo());
			if (ValidateUtil.isNotBlank(arg.getForceAuth()))
				master.addQueryInput("強制授權", arg.getForceAuth());
			if (ValidateUtil.isNotBlank(arg.getAuthManuEntry()))
				master.addQueryInput("授權接受否", arg.getAuthManuEntry());
			if (ValidateUtil.isNotBlank(arg.getApprovalNo()))
				master.addQueryInput("授權碼", arg.getApprovalNo());
			if (ValidateUtil.isNotBlank(arg.getReturnIsoCode()))
				master.addQueryInput("授權回應碼", arg.getReturnIsoCode());
			if (ValidateUtil.isNotBlank(arg.getRemark()))
				master.addQueryInput("授權者備註", arg.getRemark());
			
			ManualAuthorizerArg entity = (ManualAuthorizerArg) this.getEntity();
			String sAuthorizeMemo = entity.getAuthorizeMemo();
			String sAuthManuEntry = entity.getAuthManuEntry();
			String sApproveNo = entity.getApprovalNo();
			String sAuthorizeReason = entity.getAuthorizeReason();
			String sForceAuth = entity.getForceAuth();
			if (sAuthManuEntry != null) {
				// entity.forceAuth
				// entity.approvalNo
				// retrieve from session to ensure all infomation was currect !!
				entity = (ManualAuthorizerArg) ServletActionContext.getRequest().getSession()
						.getAttribute("manualAUTHINFO");
				entity.setAuthorizeMemo(sAuthorizeMemo);
				entity.setAuthManuEntry(sAuthManuEntry);
				entity.setApprovalNo(sApproveNo);
				entity.setAuthorizeReason(sAuthorizeReason);
				entity.setForceAuth(sForceAuth);
				AuthBizOut0200Proc out0200Proc = ProjServices.getAuthBizOut0200Proc();
				if (entity.getAddFlag() != null && entity.getAddFlag().equals("Y")) {
					out0200Proc.insertAuthLogInAdd(entity, sAuthManuEntry);
				} else {
					// 一般交易
					if (entity.getActionStep().equals("CANCELAPPROVE")) {
						// 取消
						// super.saveMessage( request ,"msg","msg.warning", new
						// Object[] {"作業 OK !!" } );
					} else {
						// 正常確認
						out0200Proc.insertAuthLogIn0200(entity, sAuthManuEntry);
						// super.saveMessage( request ,"msg","msg.insertOk",
						// null );
					}
				}
				// entity = new ManualAuthorizerArg("R");
				entity.setProcessDate(MyDateUtil.getSysDateTime(MyDateUtil.YYYYMMDD));
				entity.setProcessTime(MyDateUtil.getSysDateTime(MyDateUtil.HHMMSS));
				entity.setAddFlag("N");
				entity.setGetAuthCode("Y");
				entity.setForceAuth("N");
				entity.setActionStep("");
				this.setEntity(entity);
				super.saveMessage("msg.warning", new Object[] { "作業 OK !!" });
				return ProjConstants.SUCCESS_KEY;
			} else {
				super.saveMessage("msg.warning", new Object[] { "請選擇授權結果 !!" });
				return ProjConstants.FAIL_KEY;
			}
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			return ProjConstants.FAIL_KEY;
		} finally {
			ServletActionContext.getRequest().getSession().removeAttribute("manualAUTHINFO");
			ServletActionContext.getRequest().getSession().removeAttribute("0200NcccParm");
		}
	}

	public String doNothing() {

		return ProjConstants.SUCCESS_KEY;
	}
}
