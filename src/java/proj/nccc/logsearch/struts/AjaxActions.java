/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/20, 下午 03:44:07, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: AjaxActions.java,v 1.2 2018/01/17 10:13:02 linsteph2\cvsuser Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.struts;

import com.edstw.struts2.action.BaseAction;
import com.edstw.user.UserLogger;
import com.edstw.util.LabelValueBean;
import com.edstw.web.util.AjaxOptionsResult;
import com.edstw.web.util.AjaxResult;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.BinBankParm;
import proj.nccc.logsearch.persist.BinBinoParm;
import proj.nccc.logsearch.qs.IFESTransTypeMappingQSImpl;
import proj.nccc.logsearch.qs.LDSSTransTypeMappingQSImpl;
import proj.nccc.logsearch.qs.MerchantQS;
import proj.nccc.logsearch.vo.MerchantVO;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
@SuppressWarnings("rawtypes")
public class AjaxActions extends BaseAction {

	private static final long serialVersionUID = -2066737430020114422L;
	public static final String OPTIONS_VERSION = "version";
	public static final String OPTIONS_TERM_MODEL = "termModel";
	public static final String OPTIONS_COMTYPE = "comType";
	private String optionType;
	private String masterId;
	private boolean includeEmpty;
	private List resultList;
	private String mti;
	private String hostAccord;

	public String getMti() {
		return mti;
	}

	public void setMti(String mti) {
		this.mti = mti;
	}

	public String getHostAccord() {
		return hostAccord;
	}

	public void setHostAccord(String hostAccord) {
		this.hostAccord = hostAccord;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public boolean isIncludeEmpty() {
		return includeEmpty;
	}

	public void setIncludeEmpty(boolean includeEmpty) {
		this.includeEmpty = includeEmpty;
	}

	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public String showReturnWord() {
		AjaxResult result = new AjaxResult();

		super.setRequestAttribute("ajaxResult", result);
		try {

			String keyAry = getMasterId();

			result.addResult("returnValue", keyAry + "1");
			result.addResult("returnValue2", keyAry + "2");
			result.addResult("returnValue3", keyAry + "3");
			result.setSuccess(true);
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(e.toString());
		}

		return "Result";
	}

	public String queryTranTypeOptionsFromMti() {

		AjaxOptionsResult result = new AjaxOptionsResult();
		super.setRequestAttribute("ajaxResult", result);

		try {

			List<LabelValueBean> options = new LinkedList<LabelValueBean>();
			options.clear();
			LDSSTransTypeMappingQSImpl proc = (LDSSTransTypeMappingQSImpl) ProjServices.getLDSSTransTypeMappingQS();

			options.add(new LabelValueBean("全部", "all"));

			Map<String, String> transName = proc.queryTransNameMap(mti, hostAccord);

			if (transName != null) {
				for (Map.Entry<String, String> entry : transName.entrySet()) {
					options.add(new LabelValueBean(entry.getValue(), entry.getKey()));
				}
			}

			result.addLabelValueBeans(options);
			result.setSuccess(true);
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(e.toString());
		}

		return "Result";
	}
	
	public String queryTranTypeOptionsFromHost() {

		AjaxOptionsResult result = new AjaxOptionsResult();
		super.setRequestAttribute("ajaxResult", result);

		try {

			List<LabelValueBean> options = new LinkedList<LabelValueBean>();
			options.clear();
			IFESTransTypeMappingQSImpl ifesQS = (IFESTransTypeMappingQSImpl) ProjServices.getIFESTransTypeMappingQS();

			options.add(new LabelValueBean("全部", "all"));

			Map<String, String> transName = ifesQS.queryTransNameMap(hostAccord);

			if (transName != null) {
				for (Map.Entry<String, String> entry : transName.entrySet()) {
					options.add(new LabelValueBean(entry.getValue(), entry.getKey()));
				}
			}

			result.addLabelValueBeans(options);
			result.setSuccess(true);
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(e.toString());
		}

		return "Result";
	}

	private String cardNo;

	public String getCardNo() {

		return cardNo;
	}

	public void setCardNo(String cardNo) {

		this.cardNo = cardNo;
	}

	public String queryBankBase() {

		AjaxResult result = new AjaxResult();
		super.setRequestAttribute("ajaxResult", result);
		try {
			boolean isCupCard = ProjServices.getSetlBinDatasQS().isCupCard(this.getCardNo());
			StringBuffer bankInfo = new StringBuffer();
			/* M2010043 CUP 發卡要判斷是否為國內銀聯 START */
			BinBinoParm binParm = ProjServices.getBinBinoParmQS().getBinParm(this.getCardNo(), new java.util.Date());
			/* M2013115 */
			if ((isCupCard) && (binParm == null)) {
				// M2010043 bankInfo.append("銀聯卡");
				bankInfo.append("國外銀聯卡");
			} else {
				// java.util.Date());
				if (binParm != null) {
					BinBankParm binBankParm = binParm.getBinBank();
					bankInfo.append("(").append(binBankParm.getBankId()).append(")").append(binBankParm.getAbbrName())
							.append(" - ");
					if (binParm.getCardType().equals("A"))
						bankInfo.append("非會員");
					else {
						if (binBankParm.getMemType().equals("1"))
							bankInfo.append("會員");
						else if (binBankParm.getMemType().equals("2"))
							bankInfo.append("非會員");
						else
							bankInfo.append("其他");
					}
					// bankInfo.append(" - ").append(binBankParm.getIBussTel());
					// bankInfo.append(", ").append(binBankParm.getABussTel());
					bankInfo.append(" - ").append(binBankParm.getAuthTel1());
					bankInfo.append(", ").append(binBankParm.getAuthTel2());
				} else {
					bankInfo.append("國外卡");
				}
			}
			result.addResult("bankInfo", bankInfo.toString());
			result.setSuccess(true);
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(e.toString());
		}
		return "Result";
	}

	public String queryMchtBase() {

		AjaxResult result = new AjaxResult();
		super.setRequestAttribute("ajaxResult", result);
		try {
			MerchantQS qs = ProjServices.getMerchantQS();
			String sMchtNo = this.getMasterId();
			MerchantVO voStore = null;
			/* M2018066 QR主掃需求 */
			if (sMchtNo.length() == 16) {
				voStore = qs.queryQrMerchantBase(sMchtNo);
			} else {
				voStore = qs.queryMerchantBase(sMchtNo);
			}

			if (voStore != null) {
				result.addResult("mchtNo", voStore.getMchtNo());
				result.addResult("merchantName", voStore.getMchtName());
				result.addResult("industryCode", voStore.getIndustryCode());
				result.addResult("currentCode", voStore.getCurrentCode());
				result.addResult("telNo1", voStore.getTelNo1());
				result.addResult("zipCode3", voStore.getZipCode3());
				result.addResult("zipCode2", voStore.getZipCode2());
				result.addResult("chinZipCity", voStore.getChinZipCity());
				result.addResult("chinAddr", voStore.getChinAddr());
				result.addResult("merchantType", voStore.getMchtType());
				result.addResult("edcFlag", voStore.getEdcFlag());
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setMessage("查無該特店資料不存在.");
			}
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(e.toString());
		}
		return "Result";
	}
}
