package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.PagingInfo;
import com.edstw.util.ValidateUtil;

import proj.nccc.logsearch.TransNoUtil;
import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.persist.ChesgDailyTotals;
import proj.nccc.logsearch.vo.AtslogTransLogArg;
import proj.nccc.logsearch.vo.ChesgDailyTotalsArg;

@SuppressWarnings("unchecked")
public class AtslogTransLogQSImpl extends ProjQS {
	private static final String SPEC_ATS = "ATS";
	private static final String SPEC_AE = "AE";
	private static JdbcPersistableBuilder builder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			AtslogTransLogArg obj = new AtslogTransLogArg();
			obj.setSeqId(rs.getLong("seq_id"));
			obj.setLogDate(rs.getString("log_date"));
			obj.setLogTime(rs.getString("log_time"));
			obj.setEdcMti(rs.getString("edc_mti"));
			obj.setEdcProcCode(rs.getString("edc_proc_code"));
			obj.setTranAmount(rs.getDouble("tran_amount"));
			obj.setSysTraceNum(rs.getString("sys_trace_num"));
			obj.setTranYear(rs.getString("tran_year"));
			obj.setTranDate(rs.getString("tran_date"));
			obj.setTranTime(rs.getString("tran_time"));
			obj.setEdcPosEntryMode(rs.getString("edc_pos_entry_mode"));
			obj.setNii(rs.getString("nii"));
			obj.setPosCondCode(rs.getString("pos_cond_code"));
			obj.setAcqId(rs.getString("acq_id"));
			obj.setCardNum(rs.getString("card_num"));
			obj.setExpDate(rs.getString("exp_date"));
			obj.setServiceCode(rs.getString("service_code"));
			obj.setEdcRrn(rs.getString("edc_rrn"));
			obj.setApproveCode(rs.getString("approve_code"));
			obj.setRespCode(rs.getString("resp_code"));
			obj.setTermId(rs.getString("term_id"));
			obj.setMerchantId(rs.getString("merchant_id"));
			obj.setAuthWithPin(rs.getString("auth_with_pin"));
			obj.setAddAmount(rs.getDouble("add_amount"));
			obj.setTag5f2aTranCurrCode(rs.getString("tag_5f2a_tran_curr_code"));
			obj.setTag5f34PanSeqNum(rs.getString("tag_5f34_pan_seq_num"));
			obj.setTag82Aip(rs.getString("tag_82_aip"));
			obj.setTag84DfName(rs.getString("tag_84_df_name"));
			obj.setTag91Arpc(rs.getString("tag_91_arpc"));
			obj.setTag91Arc(rs.getString("tag_91_arc"));
			obj.setTag95Tvr(rs.getString("tag_95_tvr"));
			obj.setTag9aTranDate(rs.getString("tag_9a_tran_date"));
			obj.setTag9bTsi(rs.getString("tag_9b_tsi"));
			obj.setTag9cTranType(rs.getString("tag_9c_tran_type"));
			obj.setTag9f02AmountAuth(rs.getDouble("tag_9f02_amount_auth"));
			obj.setTag9f03AmountOther(rs.getDouble("tag_9f03_amount_other"));
			obj.setTag9f08CardAvn(rs.getString("tag_9f08_card_avn"));
			obj.setTag9f09TermAvn(rs.getString("tag_9f09_term_avn"));
			obj.setTag9f10Iad(rs.getString("tag_9f10_iad"));
			obj.setTag9f1aTermCountryCode(rs.getString("tag_9f1a_term_country_code"));
			obj.setTag9f1eIfdSerialNum(rs.getString("tag_9f1e_ifd_serial_num"));
			obj.setTag9f26Ac(rs.getString("tag_9f26_ac"));
			obj.setTag9f27Cid(rs.getString("tag_9f27_cid"));
			obj.setTag9f33TermCap(rs.getString("tag_9f33_term_cap"));
			obj.setTag9f34CvnResult(rs.getString("tag_9f34_cvn_result"));
			obj.setTag9f35TermType(rs.getString("tag_9f35_term_type"));
			obj.setTag9f36Atc(rs.getString("tag_9f36_atc"));
			obj.setTag9f37Un(rs.getString("tag_9f37_un"));
			obj.setTag9f41TranSeqNum(rs.getString("tag_9f41_tran_seq_num"));
			obj.setTag9f5bIsr(rs.getString("tag_9f5b_isr"));
			obj.setTag9f63CardProductInfo(rs.getString("tag_9f63_card_product_info"));
			obj.setTagDfedChipCondCode(rs.getString("tag_dfed_chip_cond_code"));
			obj.setTagDfeeTermEntryCap(rs.getString("tag_dfee_term_entry_cap"));
			obj.setTagDfefRsnOnlineCode(rs.getString("tag_dfef_rsn_online_code"));
			obj.setCupHotKeyInd(rs.getString("cup_hot_key_ind"));
			obj.setCupTraceNum(rs.getString("cup_trace_num"));
			obj.setCupTranDate(rs.getString("cup_tran_date"));
			obj.setCupTranTime(rs.getString("cup_tran_time"));
			obj.setCupRrn(rs.getString("cup_rrn"));
			obj.setCupSettleDate(rs.getString("cup_settle_date"));
			obj.setDccToNtdFlag(rs.getString("dcc_to_ntd_flag"));
			obj.setChPresentInd(rs.getString("ch_present_ind"));
			obj.setCardPresentInd(rs.getString("card_present_ind"));
			obj.setTranStatusInd(rs.getString("tran_status_ind"));
			obj.setTranSecInd(rs.getString("tran_sec_ind"));
			obj.setChActTermInd(rs.getString("ch_act_term_ind"));
			obj.setTermCapInd(rs.getString("term_cap_ind"));
			obj.setBatchNum(rs.getString("batch_num"));
			obj.setTipsOrigAmount(rs.getDouble("tips_orig_amount"));
			obj.setSeqNum(rs.getString("seq_num"));
			obj.setPayTypeInd(rs.getString("pay_type_ind"));
			obj.setRedeemPoint(rs.getLong("redeem_point"));
			obj.setBalancePoint(rs.getLong("balance_point"));
			obj.setPayAmount(rs.getDouble("pay_amount"));
			obj.setInstallmentPeriod(rs.getString("installment_period"));
			obj.setDownPayment(rs.getDouble("down_payment"));
			obj.setInstallmentPay(rs.getDouble("installment_pay"));
			obj.setFormalityFee(rs.getDouble("formality_fee"));
			obj.setSettleFlag(rs.getString("settle_flag"));
			obj.setAsmCouponNumber(rs.getString("asm_coupon_number"));
			obj.setAsmRedeemMethod(rs.getString("asm_redeem_method"));
			obj.setTranType(rs.getString("tran_type"));
			obj.setIcTran(rs.getString("ic_tran"));
			obj.setResponder(rs.getString("responder"));
			obj.setB24Mti(rs.getString("b24_mti"));
			obj.setB24ProcCode(rs.getString("b24_proc_code"));
			obj.setMcc(rs.getString("mcc"));
			obj.setB24PosEntryMode(rs.getString("b24_pos_entry_mode"));
			obj.setB24PosCondCode(rs.getString("b24_pos_cond_code"));
			obj.setB24AcqId(rs.getString("b24_acq_id"));
			obj.setAtsRrn(rs.getString("ats_rrn"));
			obj.setPostalCode(rs.getString("postal_code"));
			obj.setCardVeryFlag(rs.getString("card_very_flag"));
			obj.setMoToFlag(rs.getString("mo_to_flag"));
			obj.setTermAttendInd(rs.getString("term_attend_ind"));
			obj.setTransactionId(rs.getString("transaction_id"));
			obj.setBankId(rs.getString("bank_id"));
			obj.setCardType(rs.getString("card_type"));
			obj.setStandInFlag(rs.getString("stand_in_flag"));
			obj.setAtsHost(rs.getString("ats_host"));
			obj.setEdcSpec(rs.getString("edc_spec"));
			obj.setBiciPort(rs.getString("bici_port"));
			obj.setDestinationId(rs.getString("destination_id"));
			obj.setTmkIndex(rs.getString("tmk_index"));
			obj.setFromEdcDate(rs.getString("from_edc_date"));
			obj.setFromEdcTime(rs.getString("from_edc_time"));
			obj.setToBase24Date(rs.getString("to_base24_date"));
			obj.setToBase24Time(rs.getString("to_base24_time"));
			obj.setFromBase24Date(rs.getString("from_base24_date"));
			obj.setFromBase24Time(rs.getString("from_base24_time"));
			obj.setToEdcDate(rs.getString("to_edc_date"));
			obj.setToEdcTime(rs.getString("to_edc_time"));
			obj.setToAsmDate(rs.getString("to_asm_date"));
			obj.setToAsmTime(rs.getString("to_asm_time"));
			obj.setFromAsmDate(rs.getString("from_asm_date"));
			obj.setFromAsmTime(rs.getString("from_asm_time"));
			obj.setToHsmMacDate(rs.getString("to_hsm_mac_date"));
			obj.setToHsmMacTime(rs.getString("to_hsm_mac_time"));
			obj.setFromHsmMacDate(rs.getString("from_hsm_mac_date"));
			obj.setFromHsmMacTime(rs.getString("from_hsm_mac_time"));
			obj.setToHsmKeyDate(rs.getString("to_hsm_key_date"));
			obj.setToHsmKeyTime(rs.getString("to_hsm_key_time"));
			obj.setFromHsmKeyDate(rs.getString("from_hsm_key_date"));
			obj.setFromHsmKeyTime(rs.getString("from_hsm_key_time"));
			obj.setToTmsDate(rs.getString("to_tms_date"));
			obj.setToTmsTime(rs.getString("to_tms_time"));
			obj.setFromTmsDate(rs.getString("from_tms_date"));
			obj.setFromTmsTime(rs.getString("from_tms_time"));
			obj.setFromEdcConfDate(rs.getString("from_edc_conf_date"));
			obj.setFromEdcConfTime(rs.getString("from_edc_conf_time"));
			obj.setToEdcConfDate(rs.getString("to_edc_conf_date"));
			obj.setToEdcConfTime(rs.getString("to_edc_conf_time"));
			obj.setFromSpswDate(rs.getString("from_spsw_date"));
			obj.setFromSpswTime(rs.getString("from_spsw_time"));
			obj.setToSpswDate(rs.getString("to_spsw_date"));
			obj.setToSpswTime(rs.getString("to_spsw_time"));
			obj.setToSpswConfDate(rs.getString("to_spsw_conf_date"));
			obj.setToSpswConfTime(rs.getString("to_spsw_conf_time"));
			obj.setTagS1(rs.getString("TAG_S1"));
			obj.setTagS2(rs.getString("TAG_S2"));
			obj.setTagS3(rs.getString("TAG_S3"));
			obj.setTagS4(rs.getString("TAG_S4"));
			obj.setTagS5(rs.getString("TAG_S5"));
			obj.setTagS6(rs.getString("TAG_S6"));
			obj.setTagS7(rs.getString("TAG_S7"));
			obj.setFiscRespReasonCode(rs.getString("FISC_RESP_REASON_CODE"));
			obj.setFiscRespCode(rs.getString("FISC_RESP_CODE"));
			//20150706 M2015043 
			obj.setQuickPayFlag(rs.getString("QUICK_PAY_FLAG"));
			//20150818 M2014245
			obj.setTag9f6eFFI(rs.getString("TAG_9F6E_FFI"));
			//20150908 M2015015
			obj.setOnUsRole(rs.getString("ON_US_ROLE"));
			//20170120 M2016144 電子票證
			obj.setTranAmountBf(rs.getString("TRAN_AMOUNT_BF"));
			obj.setTranAmountAf(rs.getString("TRAN_AMOUNT_AF"));
			obj.setTmSerialNum(rs.getString("TM_SERIAL_NUM"));
			obj.setSwCardNum(rs.getString("SW_CARD_NUM"));
			obj.setSwRespCode(rs.getString("SW_RESP_CODE"));
			obj.setAtsEsvcTxnNum(rs.getString("ATS_ESVC_TXN_NUM"));
			//20170504 M2016029
			obj.setMpasFlag(rs.getString("MPAS_FLAG"));
			obj.setMcpInd(rs.getString("MCP_IND"));
			//M2017080
			obj.setTag71(rs.getString("TAG_71"));
			obj.setTag72(rs.getString("TAG_72"));
			//20180509 add req
			obj.setAtsReversal(rs.getString("ATS_REVERSAL"));
			
			//UPLAN-M2018187
			obj.setUplanFlag(rs.getString("UPLAN_FLAG"));
			obj.setUpDiscountAmt(rs.getString("UP_DISCOUNT_AMT"));
			obj.setUpPreferAmt(rs.getString("UP_PREFER_AMT"));
			obj.setTag9f60CouponInfo(rs.getString("TAG_9F60_COUPON_INFO"));
			obj.setFromUplanDate(rs.getString("FROM_UPLAN_DATE"));
			obj.setFromUplanTime(rs.getString("FROM_UPLAN_TIME"));
			obj.setToUplanDate(rs.getString("TO_UPLAN_DATE"));
			obj.setToUplanTime(rs.getString("TO_UPLAN_TIME"));
			obj.setUpCouponId(rs.getString("UP_COUPON_ID"));
			
			// M2020145_TOKEN_REQUESTOR_ID
			obj.setTokenReqId(rs.getString("TOKEN_REQUESTOR_ID"));
			// M2020218_UNY
			obj.setUnyTranCode(rs.getString("UNY_TRAN_CODE"));
			obj.setFromUnyDate(rs.getString("FROM_UNY_DATE"));
			obj.setFromUnyTime(rs.getString("FROM_UNY_TIME"));
			obj.setToUnyDate(rs.getString("TO_UNY_DATE"));
			obj.setToUnyTime(rs.getString("TO_UNY_TIME"));
			obj.setToUnyNotifyDate(rs.getString("TO_UNY_NOTIFY_DATE"));
			obj.setToUnyNotifyTime(rs.getString("TO_UNY_NOTIFY_TIME"));
			// M2021165_JCB_9F7C Partner Discretionary Data
			obj.setTag9f7c(rs.getString("TAG_9F7C"));
			// M2024060_R113110_信託資訊平台
			obj.setFromTrustDate(rs.getString("FROM_TRUST_DATE"));
			obj.setFromTrustTime(rs.getString("FROM_TRUST_TIME"));
			obj.setToTrustDate(rs.getString("TO_TRUST_DATE"));
			obj.setToTrustTime(rs.getString("TO_TRUST_TIME"));
			obj.setTrustNum(rs.getString("TRUST_NUM"));
			obj.setTrustBankId(rs.getString("TRUST_BANK_ID"));
			obj.setTrustSeqNum(rs.getString("TRUST_SEQ_NUM"));
			// M2025074_R114117_CHESG 電子簽單
			obj.setChesgFlag(BooleanUtils.toBooleanObject(rs.getString("CHESG_FLAG"), "Y", "N", null));
			obj.setChesgId(rs.getString("CHESG_ID"));
			return obj;
		}
	};

	public int queryCount(AtslogTransLogArg arg) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select count(*) cnt from atslog_trans_log where 1 = 1 ");

		prepareArg(arg, params, cmd, SPEC_ATS);
		String value = this.queryString(cmd.toString(), params, "cnt");

		return Integer.parseInt(value);
	}

	private void prepareArg(AtslogTransLogArg arg, List<Object> params, StringBuffer cmd, String spec) {
		//	交易日期起
		String tranDateFrom = arg.getTranDateFrom();
		if (ValidateUtil.isNotBlank(tranDateFrom)) {
			cmd.append("and (log_date >= ? ) ");
			params.add(tranDateFrom);
		}

		//	交易日期迄
		String tranDateTo = arg.getTranDateTo();
		if (ValidateUtil.isNotBlank(tranDateTo)) {
			cmd.append("and (log_date <= ? ) ");
			params.add(tranDateTo);
		}

		//	交易時間起
		String tranTimeFrom = arg.getTranTimeFrom();
		if (ValidateUtil.isNotBlank(tranTimeFrom)) {
			cmd.append("and (log_time >= ? ");
			params.add(tranTimeFrom+"000");
			cmd.append("or log_time = ?) ");
			params.add(tranTimeFrom);
		}

		//	交易日期迄
		String tranTimeTo = arg.getTranTimeTo();
		if (ValidateUtil.isNotBlank(tranTimeTo)) {
			cmd.append("and log_time <= ? ");
			params.add(tranTimeTo+"999");
		}
		
		if(SPEC_AE.equals(spec)) {
			cmd.append("and edc_spec in ('AE','GEDC') ");
		}

		//	卡號
		if (ValidateUtil.isNotBlank(arg.getCardNum())) {

			cmd.append("and card_num = ? ");
			params.add(arg.getCardNum());
		}

		//	特店代號
		if (ValidateUtil.isNotBlank(arg.getMerchantId())) {

			cmd.append("and merchant_id = ? ");
			params.add(arg.getMerchantId());
		}

		//	端末機代號
		if (ValidateUtil.isNotBlank(arg.getTermId())) {

			cmd.append("and term_id = ? ");
			params.add(arg.getTermId());
		}

		//	批次號碼
		if (ValidateUtil.isNotBlank(arg.getBatchNum())) {

			cmd.append("and batch_num = ? ");
			params.add(arg.getBatchNum());
		}

		//	授權碼
		if (ValidateUtil.isNotBlank(arg.getApproveCode())) {

			cmd.append("and approve_code = ? ");
			params.add(arg.getApproveCode());
		}

		//	EDC MTI
		if (ValidateUtil.isNotBlank(arg.getEdcMti())) {

			cmd.append("and edc_mti = ? ");
			params.add(arg.getEdcMti());
		}

		//	B24 MTI
		if (ValidateUtil.isNotBlank(arg.getB24Mti())) {

			cmd.append("and b24_mti = ? ");
			params.add(arg.getB24Mti());
		}

		if (ValidateUtil.isBlank(arg.getEdcMti()) && ValidateUtil.isBlank(arg.getB24Mti())) {
			if (arg.isDefaultMti()) {
				//do nothing
			} else {
				cmd.append("and ( edc_mti like '01%' or edc_mti like '02%' or edc_mti like '04%' or edc_mti like '05%' or edc_mti like '08%' or edc_mti = '' or edc_mti is null)");
				cmd.append("and (b24_mti like '01%' or b24_mti like '02%' or b24_mti like '04%' or b24_mti = '' or b24_mti is null)");
			}
		}

		//	金額起
		if (arg.getTranAmountFrom() != null) {

			cmd.append("and tran_amount >= ? ");
			params.add(arg.getTranAmountFrom());
		}

		//	金額迄
		if (arg.getTranAmountTo() != null) {

			cmd.append("and tran_amount <= ? ");
			params.add(arg.getTranAmountTo());
		}

		//	交易編號
		if (ValidateUtil.isNotBlank(arg.getTransNo())) {

			String transNo = arg.getTransNo();
			String cardNo = TransNoUtil.fromTransNo(transNo);

			if (cardNo == null) {

				cmd.append("and 1 = 0 ");
			} else {

				cmd.append("and card_num = ? ");
				params.add(cardNo);
			}
		}
		
		// B24 Pos Entry Mode
		if (ValidateUtil.isNotBlank(arg.getB24PosEntryMode())) {
			cmd.append("and b24_pos_entry_mode = ? ");
			params.add(arg.getB24PosEntryMode());
		}
		
		// M2020218_UNY Uny Code
		if (ValidateUtil.isNotBlank(arg.getUnyTranCode())) {
			cmd.append("and trim(uny_tran_code) = ? ");
			params.add(arg.getUnyTranCode());
		}
		
		// M2024060_R113110_信託資訊平台序號
		if (ValidateUtil.isNotBlank(arg.getTrustNum())) {
			cmd.append("and trim(trust_num) = ? ");
			params.add(arg.getTrustNum());
		}
		
		// M2024060_R113110_信託銀行代碼
		if (ValidateUtil.isNotBlank(arg.getTrustBankId())) {
			cmd.append("and trim(trust_bank_id) = ? ");
			params.add(arg.getTrustBankId());
		}
		
		// M2022086_R111155_收付訊息整合平台
		if (ValidateUtil.isNotBlank(arg.getCardType())) {
			if (arg.getCardType().equals("credit")) {
				cmd.append("and trim(card_type) not in (SELECT LOOKUP_CODE FROM SYS_PARA WHERE LOOKUP_TYPE = 'CARD_TYPE' AND VALUE_EXT1='e-Wallet') ");
			} else if (arg.getCardType().equals("0957")) {
				// M2024060_R113110_信託資訊平台
				cmd.append("and nii = ? ");
				params.add(arg.getCardType());
			} else if (!arg.getCardType().equals("All")) {
				cmd.append("and trim(card_type) = ? ");
				params.add(arg.getCardType());
			}
		}

		// M2025074_R114117_CHESG
		if (arg.getChesgFlag() != null) {
			cmd.append("AND CHESG_FLAG = ? ");
			params.add(arg.getChesgFlag() ? "Y" : "N");
		}
	}

	/**
	 * @return
	 * @throws QueryServiceException
	 */
	public List<AtslogTransLogArg> query(AtslogTransLogArg arg) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select /*+ FIRST_ROWS(3000) PARALLEL(ATSLOG_TRANS_LOG, 20) */ * ");
		cmd.append("from atslog_trans_log where 1 = 1 ");
		
		prepareArg(arg, params, cmd, SPEC_ATS);

		arg.getPagingInfo().setPageSize(20);
		//	排序選擇 回應碼需要另外處理 排序順序為  空白、A、D、C、P 在 AtslogTransLogProcImpl 處理
		List<AtslogTransLogArg> list = null;
		boolean isDbOrder = !(arg.getSortFields() != null && !arg.getSortFields().isEmpty() && arg.getSortFields().equals("resp_code"));
		if(isDbOrder){
			StringBuffer sortStr = new StringBuffer("order by ");
			if (arg.getSortFields() != null && !arg.getSortFields().isEmpty()) {
				if (arg.getSortFields().equals("tran_date")) {
					sortStr.append(arg.getSortFields()).append(" desc").append(", ").append("log_time");
				} else if (arg.getSortFields().equals("tran_time")) {
					sortStr.append("log_date").append(", ").append("log_time").append(" desc");
				} else {
					sortStr.append(arg.getSortFields()).append(", ").append("log_date, log_time");
				}
			} else {
				sortStr.append("log_date, log_time");
			}
			if (sortStr != null)
				cmd.append(sortStr);
			
			if (arg.getPagingInfo().isEnablePaging()) {
				list = (List<AtslogTransLogArg>) this.queryByPagingInfo(cmd.toString(), params,
						builder, arg.getPagingInfo());
			} else {
				list = (List<AtslogTransLogArg>) this.queryObjectList(cmd.toString(), params, builder);
			}
		}else{
			if (arg.getPagingInfo().isEnablePaging()) {
				list = (List<AtslogTransLogArg>) this.queryByPagingInfo(cmd.toString(), params,
						builder, arg.getPagingInfo());
			} else {
				list = (List<AtslogTransLogArg>) this.queryObjectList(cmd.toString(), params, builder);
			}
			if(list != null && list.size() > 0){
			Collections.sort(list, new Comparator<AtslogTransLogArg>(){
				final String[] order = { "", "A", "D", "C", "P" };
				
				@Override
				public int compare(AtslogTransLogArg arg0, AtslogTransLogArg arg1) {
					int index0 = getIndex(arg0);
					int index1 = getIndex(arg1);
					
					if(index0 != index1){
						return index0 - index1;
					}
					
					if(!arg0.getLogDate().equals(arg1.getLogDate())){
						return arg0.getLogDate().compareTo(arg1.getLogDate());
					}
					
					if(!arg0.getLogTime().equals(arg1.getLogTime())){
						return arg0.getLogTime().compareTo(arg1.getLogTime());
					}
					
					return 0;
				}
				
				private int getIndex(AtslogTransLogArg arg){
					if(arg.getRespCode() != null && !arg.getRespCode().equals("")){
						for(int i = 0;i< order.length;i++){
							String label = ParamBean.getInstance().getResponseCodeNameMap().get(arg.getRespCode());
							if(label != null){
								if(label.equals("")){
									return 0;
								}else if(label.substring(0, 1).equals(order[i])){
									return i;
								}
							}
						}
					}
					return 0;
				}
			});
			
			list = setPageInfo(arg.getPagingInfo(), list);
			}
		}
		
		return list;
	}
	

	private List<AtslogTransLogArg> setPageInfo(PagingInfo pagingInfo, List<AtslogTransLogArg> list){
		int rowCount = 0;
		int pageSize = pagingInfo.getPageSize();
		int currentPage = pagingInfo.getCurrentPage();
		boolean paging = false;
		List<AtslogTransLogArg> result = new ArrayList<AtslogTransLogArg>();
		if (pagingInfo.isEnablePaging()) {

			rowCount = list.size();

			if (pageSize <= 0)
				pageSize = 30;
			else if (pageSize > MAX_RECORD_COUNT)
				pageSize = MAX_RECORD_COUNT;
			
			int pageCount = (rowCount % pageSize) == 0 ?
					rowCount / pageSize :
					(int) (rowCount / pageSize + 1);
			
			if (currentPage <= 0)
				currentPage = 1;
			else if (currentPage > pageCount)
				currentPage = pageCount;
			
			int rowIndexFrom = (currentPage - 1) * pageSize;
			
			for (int i = rowIndexFrom; i < pageSize * currentPage && i < list.size(); i++) {
				
				
				result.add(list.get(i));
			}
			
			pagingInfo.setCurrentPage(currentPage);
			pagingInfo.setTotalCount(pageCount);
			paging = true;
		}
		
		if (!paging) {
			
			result.addAll(list);
			
			pagingInfo.setCurrentPage(1);
			pagingInfo.setTotalCount(1);
		}
		
		pagingInfo.setResultList(result);
		return result;
	}

	/**
	 * @param seqIds
	 * @return
	 * @throws QueryServiceException
	 */
	public List<AtslogTransLogArg> queryByIds(String[] seqIds) throws QueryServiceException {
		if (seqIds == null || seqIds.length <= 0)
			return new ArrayList<AtslogTransLogArg>();

		List<Object> params = new ArrayList<Object>();
		StringBuffer criteria = null;
		int count = 0;

		for (int i = 0; i < seqIds.length; i++) {

			if (seqIds[i] == null)
				continue;

			if (criteria == null)
				criteria = new StringBuffer("seq_id in (?");
			else
				criteria.append(", ?");

			Long seqId = Long.parseLong(seqIds[i]);
			params.add(seqId);
			count++;

			if (count >= 800)
				break;
		}

		if (criteria == null)
			return new ArrayList<AtslogTransLogArg>();

		criteria.append(") ");

		StringBuffer cmd = new StringBuffer().append("select * from atslog_trans_log ").append("where ")
				.append(criteria);

		List<AtslogTransLogArg> list = this.queryObjectList(cmd.toString(), params, builder);

		return list;
	}

	/**
	 * @param seqId
	 * @return
	 * @throws QueryServiceException
	 */
	public AtslogTransLogArg queryById(Long seqId) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();
		StringBuffer cmd = new StringBuffer().append("select * from atslog_trans_log ").append("where seq_id = ? order by log_date desc");
		params.add(seqId);
		return (AtslogTransLogArg) this.queryObject(cmd.toString(), params, builder);
	}
	
	
	/** AE only
	 * @param seqId
	 * @return
	 * @throws QueryServiceException
	 */
	
	public int queryAECount(AtslogTransLogArg arg) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select count(*) cnt from atslog_trans_log where 1 = 1 ");

		prepareArg(arg, params, cmd, SPEC_AE);
		
		String value = this.queryString(cmd.toString(), params, "cnt");

		return Integer.parseInt(value);
	}
	
	/**
	 * @return
	 * @throws QueryServiceException
	 */
	public List<AtslogTransLogArg> queryAE(AtslogTransLogArg arg) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select /*+ FIRST_ROWS(3000) PARALLEL(ATSLOG_TRANS_LOG, 20) */ * ");
		cmd.append("from atslog_trans_log where 1 = 1 ");

		prepareArg(arg, params, cmd, SPEC_AE);
		
		arg.getPagingInfo().setPageSize(20);
		//	排序選擇 回應碼需要另外處理 排序順序為  空白、A、D、C、P 在 AtslogTransLogProcImpl 處理
		List<AtslogTransLogArg> list = null;
		boolean isDbOrder = !(arg.getSortFields() != null && !arg.getSortFields().isEmpty() && arg.getSortFields().equals("resp_code"));
		if(isDbOrder){
			StringBuffer sortStr = new StringBuffer("order by ");
			if (arg.getSortFields() != null && !arg.getSortFields().isEmpty()) {
				if (arg.getSortFields().equals("log_date")) {
					sortStr.append(arg.getSortFields()).append(" desc").append(", ").append("log_time");
				} else if (arg.getSortFields().equals("log_time")) {
					sortStr.append("log_date").append(", ").append("log_time").append(" desc");
				} else {
					sortStr.append(arg.getSortFields()).append(", ").append("log_date, log_time");
				}
			} else {
				sortStr.append("log_date, log_time");
			}
			if (sortStr != null)
				cmd.append(sortStr);
			
			if (arg.getPagingInfo().isEnablePaging()) {
				list = (List<AtslogTransLogArg>) this.queryByPagingInfo(cmd.toString(), params,
						builder, arg.getPagingInfo());
			} else {
				list = (List<AtslogTransLogArg>) this.queryObjectList(cmd.toString(), params, builder);
			}
		}else{
			if (arg.getPagingInfo().isEnablePaging()) {
				list = (List<AtslogTransLogArg>) this.queryByPagingInfo(cmd.toString(), params,
						builder, arg.getPagingInfo());
			} else {
				list = (List<AtslogTransLogArg>) this.queryObjectList(cmd.toString(), params, builder);
			}
			if(list != null && list.size() > 0){
			Collections.sort(list, new Comparator<AtslogTransLogArg>(){
				final String[] order = { "", "A", "D", "C", "P" };
				
				@Override
				public int compare(AtslogTransLogArg arg0, AtslogTransLogArg arg1) {
					int index0 = getIndex(arg0);
					int index1 = getIndex(arg1);
					
					if(index0 != index1){
						return index0 - index1;
					}
					
					if(!arg0.getLogDate().equals(arg1.getLogDate())){
						return arg0.getLogDate().compareTo(arg1.getLogDate());
					}
					
					if(!arg0.getLogTime().equals(arg1.getLogTime())){
						return arg0.getLogTime().compareTo(arg1.getLogTime());
					}
					
					return 0;
				}
				
				private int getIndex(AtslogTransLogArg arg){
					if(arg.getRespCode() != null && !arg.getRespCode().equals("")){
						for(int i = 0;i< order.length;i++){
							String label = ParamBean.getInstance().getResponseCodeNameMap().get(arg.getRespCode());
							if(label != null){
								if(label.equals("")){
									return 0;
								}else if(label.substring(0, 1).equals(order[i])){
									return i;
								}
							}
						}
					}
					return 0;
				}
			});
			
			list = setPageInfo(arg.getPagingInfo(), list);
			}
		}
		
		return list;
	}

	private static JdbcPersistableBuilder chesgBuilder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			ChesgDailyTotals obj = new ChesgDailyTotals();
			obj.setLogDate(rs.getString("LOG_DATE"));
			obj.setMerchantId(rs.getString("MERCHANT_ID"));
			obj.setTermId(rs.getString("TERM_ID"));
			obj.setTotalCount(rs.getInt("TOTAL_COUNT"));
			obj.setChesgCount(rs.getInt("CHESG_COUNT"));
			return obj;
		}
	};

	private static JdbcPersistableBuilder chesgArgBuilder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			ChesgDailyTotalsArg obj = new ChesgDailyTotalsArg();
			obj.setLogDate(rs.getString("LOG_DATE"));
			obj.setMerchantId(rs.getString("MERCHANT_ID"));
			obj.setTermId(rs.getString("TERM_ID"));
			obj.setTotalCount(rs.getInt("TOTAL_COUNT"));
			obj.setChesgCount(rs.getInt("CHESG_COUNT"));
			return obj;
		}
	};

	private static JdbcPersistableBuilder atslogBuilder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			AtslogTransLogArg obj = new AtslogTransLogArg();
			obj.setLogDate(rs.getString("LOG_DATE"));
			obj.setMerchantId(rs.getString("MERCHANT_ID"));
			obj.setTermId(rs.getString("TERM_ID"));
			obj.setTotalCount(rs.getInt("TOTAL_COUNT"));
			obj.setChesgCount(rs.getInt("CHESG_COUNT"));
			return obj;
		}
	};

	public List<AtslogTransLogArg> queryChesgfromTranslog(String dateFrom, String dateTo, boolean isFirstRun) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer()
				.append(" SELECT LOG_DATE, MERCHANT_ID, TERM_ID, ")
				.append(" COUNT(1) AS TOTAL_COUNT, ")
				.append(" SUM(CASE WHEN CHESG_FLAG = 'Y' THEN 1 ELSE 0 END) AS CHESG_COUNT ")
				.append(" FROM ATSLOG_TRANS_LOG WHERE CHESG_FLAG IS NOT NULL ");

		if (isFirstRun) {
			if (ValidateUtil.isNotBlank(dateFrom)) {
				cmd.append(" AND (LOG_DATE >= ? ) ");
				params.add(dateFrom);
			}
			if (ValidateUtil.isNotBlank(dateTo)) {
				cmd.append(" AND (LOG_DATE <= ? ) ");
				params.add(dateTo);
			}
		} else {
			cmd.append(" AND LOG_DATE = ? ");
			params.add(dateTo);
		}

		cmd.append(" GROUP BY LOG_DATE, MERCHANT_ID, TERM_ID  ORDER BY LOG_DATE");

		return (List<AtslogTransLogArg>) this.queryObjectList(cmd.toString(), params, atslogBuilder);
	}

	public int queryChesgCount(ChesgDailyTotalsArg arg, boolean isRange) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("SELECT COUNT(*) CNT FROM CHESG_DAIL_TOTALS WHERE 1 = 1 ");

		if (arg != null) {
			prepareChesgArg(arg, params, cmd, isRange);
		}

		String value = this.queryString(cmd.toString(), params, "CNT");

		return Integer.parseInt(value);
	}

	public List<ChesgDailyTotalsArg> queryChesgList(ChesgDailyTotalsArg arg, String queryDate, boolean isRange)
			throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer()
				.append(" SELECT LOG_DATE, MERCHANT_ID, TERM_ID, TOTAL_COUNT, CHESG_COUNT ")
				.append(" FROM CHESG_DAIL_TOTALS WHERE 1 = 1 ");

		if (arg != null) {
			prepareChesgArg(arg, params, cmd, isRange);
		} else {
			cmd.append(" AND LOG_DATE = ? ");
			params.add(queryDate);
		}

		List<ChesgDailyTotalsArg> list = null;
		if (arg != null && arg.getPagingInfo().isEnablePaging()) {
			list = (List<ChesgDailyTotalsArg>) this.queryByPagingInfo(cmd.toString(), params, chesgArgBuilder,
					arg.getPagingInfo());
		} else {
			list = (List<ChesgDailyTotalsArg>) this.queryObjectList(cmd.toString(), params, chesgArgBuilder);
		}
		return list;
	}

	private void prepareChesgArg(ChesgDailyTotalsArg arg, List<Object> params, StringBuffer cmd, boolean isRange) {
		if (isRange) {
			String tranDateFrom = arg.getTranDateFrom();
			if (ValidateUtil.isNotBlank(tranDateFrom)) {
				cmd.append(" AND (LOG_DATE >= ? ) ");
				params.add(tranDateFrom);
			}
			String tranDateTo = arg.getTranDateTo();
			if (ValidateUtil.isNotBlank(tranDateTo)) {
				cmd.append(" AND (LOG_DATE <= ? ) ");
				params.add(tranDateTo);
			}
		} else {
			cmd.append(" AND LOG_DATE = ? ");
			params.add(arg.getLogDate());
		}

		// 特店代號
		if (ValidateUtil.isNotBlank(arg.getMerchantId())) {
			cmd.append(" AND MERCHANT_ID = ? ");
			params.add(arg.getMerchantId());
		}

		// 端末機代號
		if (ValidateUtil.isNotBlank(arg.getTermId())) {
			cmd.append(" AND TERM_ID = ? ");
			params.add(arg.getTermId());
		}
		cmd.append(" AND MERCHANT_ID <> '----------' ORDER BY LOG_DATE, MERCHANT_ID, TERM_ID ");
	}

	public ChesgDailyTotals queryChesg(AtslogTransLogArg ats) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer()
				.append(" SELECT LOG_DATE, MERCHANT_ID, TERM_ID, TOTAL_COUNT, CHESG_COUNT ")
				.append(" FROM CHESG_DAIL_TOTALS WHERE LOG_DATE = ? AND MERCHANT_ID = ? AND TERM_ID = ? ");

		params.add(ats.getLogDate());
		params.add(ats.getMerchantId());
		params.add(ats.getTermId());

		List list = this.queryObjectList(cmd.toString(), params, chesgBuilder);

		return list.size() > 0 ? (ChesgDailyTotals) list.get(0) : null;
	}
}
