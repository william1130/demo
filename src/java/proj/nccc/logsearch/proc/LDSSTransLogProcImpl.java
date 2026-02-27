package proj.nccc.logsearch.proc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.edstw.process.ProcessException;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.qs.AtsRejectCodeSetupQSImpl;
import proj.nccc.logsearch.qs.LDSSTransLogQSImpl;
import proj.nccc.logsearch.qs.MerchantChinQSImpl;
import proj.nccc.logsearch.vo.LDSSTransLogArg;

/**
 * 
 * @author Red Lee
 * @version 1.0
 */
public class LDSSTransLogProcImpl extends ProjProc {

	public int queryCount(LDSSTransLogArg arg) {
		LDSSTransLogQSImpl qs = (LDSSTransLogQSImpl) ProjServices.getLDSSTransLogQS();

		return qs.queryCount(arg);
	}

	/**
	 * @param arg
	 * @return
	 * @throws ProcessException
	 */
	public List<LDSSTransLogArg> getList(LDSSTransLogArg arg) throws ProcessException {

		List<LDSSTransLogArg> list = null;

		try {

			LDSSTransLogQSImpl qs = (LDSSTransLogQSImpl) ProjServices.getLDSSTransLogQS();

			list = qs.query(arg);

			// get Merchant Chinese Name begin
			MerchantChinQSImpl mcQs = (MerchantChinQSImpl) ProjServices.getMerchantChinQS();
			List<String> merchantIds = new ArrayList<String>();

			for (LDSSTransLogArg obj : list) {
				merchantIds.add(obj.getMerchantId().trim());
			}

			Map<String, String> merchantNameMap = mcQs.queryNameMap(merchantIds);
			// get Merchant Chinese Name end

			// 授權碼 name begin
			AtsRejectCodeSetupQSImpl arcsQs = (AtsRejectCodeSetupQSImpl) ProjServices.getAtsRejectCodeSetupQS();
			Map<String, String> rejectCodeNamMap = arcsQs.queryNameMap();
			// 授權碼 name end

			// 交易類別
			Map<String, String> transTypeMap = ParamBean.getInstance().getLdssTransTypeNameMap();

			// set
			for (LDSSTransLogArg obj : list) {
				String transTypeName = "";
				String hostAccord = StringUtils.trimToEmpty(obj.getHostAccord());
				transTypeName = transTypeMap.get(hostAccord + "," + obj.getMti() + "," + obj.getProcCode());

				if (hostAccord.equals("ESVC")) {
					int cardType = Integer.parseInt(obj.getProcCode().substring(0, 2));
					String cardStr = "";
					switch (cardType) {
					case 51:
						cardStr = "悠遊卡";
						break;
					case 52:
						cardStr = "遠鑫卡";
						break;
					case 53:
						cardStr = "一卡通";
						break;
					case 58:
						cardStr = "愛金卡";
						break;
					}
					transTypeName = cardStr + transTypeName;
				}

				setArg(obj, arg.getMaskCardNo(), arg.getShowCardIso(),
						(String) merchantNameMap.get(obj.getMerchantId().trim()),
						(String) rejectCodeNamMap.get(obj.getApproveCode()), transTypeName);
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}

		return list;
	}

	public void setArg(LDSSTransLogArg arg, String maskCardNo, String showCardIso, String merchantChinName,
			String approveCodeName, String transTypeName) {
		arg.setMaskCardNo(maskCardNo);
		arg.setShowCardIso(showCardIso);
		arg.setMerchantChinName(merchantChinName);
		arg.setTranType(transTypeName);
		String approveCode = arg.getApproveCode();
		if (approveCode != null && (approveCode.startsWith("REJ") || (approveCode.startsWith("ID"))))
			arg.setApproveCodeName(approveCodeName);
	}

}
