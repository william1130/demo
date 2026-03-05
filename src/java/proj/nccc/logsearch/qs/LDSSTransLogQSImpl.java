package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.PagingInfo;
import com.edstw.util.ValidateUtil;

import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.vo.LDSSTransLogArg;

public class LDSSTransLogQSImpl extends ProjQS {

	private static JdbcPersistableBuilder builder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			LDSSTransLogArg obj = new LDSSTransLogArg();
			obj.setSeqId(rs.getLong("seq_id"));
			obj.setLogDate(rs.getString("log_date"));
			obj.setLogTime(rs.getString("log_time"));
			obj.setMti(rs.getString("mti"));
			obj.setProcCode(rs.getString("proc_code"));
			obj.setTranAmount(rs.getDouble("tran_amount"));
			obj.setSysTraceNum(rs.getString("sys_trace_num"));
			obj.setTranYear(rs.getString("tran_year"));
			obj.setTranDate(rs.getString("tran_date"));
			obj.setTranTime(rs.getString("tran_time"));
			obj.setPosEntryMode(rs.getString("pos_entry_mode"));
			obj.setAcqId(rs.getString("acq_id"));
			obj.setCardNum(rs.getString("card_num"));
			obj.setExpDate(rs.getString("exp_date"));
			obj.setApproveCode(rs.getString("approve_code"));
			obj.setRespCode(rs.getString("resp_code"));
			obj.setTermId(rs.getString("term_id"));
			obj.setMerchantId(rs.getString("merchant_id"));
			obj.setBatchNum(rs.getString("batch_num"));
			obj.setSeqNum(rs.getString("seq_num"));
			obj.setHostAccord(rs.getString("host_accord"));
			obj.setHostName(rs.getString("host_name"));
			return obj;
		}
	};

	public int queryCount(LDSSTransLogArg arg) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select count(*) cnt from ldss_trans_log where 1 = 1 ");

		prepareArg(arg, params, cmd);

		String value = this.queryString(cmd.toString(), params, "cnt");

		return Integer.parseInt(value);
	}

	private void prepareArg(LDSSTransLogArg arg, List<Object> params, StringBuffer cmd) {
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
			cmd.append("and log_time >= ? ");
			params.add(tranTimeFrom+"000");
		}

		//	交易日期迄
		String tranTimeTo = arg.getTranTimeTo();
		if (ValidateUtil.isNotBlank(tranTimeTo)) {
			cmd.append("and log_time <= ? ");
			params.add(tranTimeTo+"999");
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
		

		String[] tranTypeAry = null;
		//"HOST_ACCORD","MTI", "PROC_CODE"
		if (ValidateUtil.isNotBlank(arg.getTranType()) && !arg.getTranType().equalsIgnoreCase("all")) {
			tranTypeAry = arg.getTranType().split(",");
		}
		
		// 授權主機
		if (ValidateUtil.isNotBlank(arg.getHostAccord()) && !arg.getHostAccord().equalsIgnoreCase("ALL")) {

			cmd.append("and host_accord = ? ");
			params.add(arg.getHostAccord());
		}else{
			if(tranTypeAry != null){
				cmd.append("and host_accord = ? ");
				params.add(tranTypeAry[0]);
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
		
		//LDSS主機
		if (ValidateUtil.isNotBlank(arg.getHostName()) && !arg.getHostName().equalsIgnoreCase("all")) {
			cmd.append("and host_name = ? ");
			params.add(arg.getHostName());
		}

		
		//"HOST_ACCORD","MTI", "PROC_CODE"
		//mti
		if (ValidateUtil.isNotBlank(arg.getMti())) {
			cmd.append("and MTI = ? ");
			params.add(arg.getMti());
		} else {
			if(tranTypeAry != null){
				cmd.append("and MTI = ? ");
				params.add(tranTypeAry[1]);
			}
		}
		
		////"HOST_ACCORD","MTI", "PROC_CODE"
		if(tranTypeAry != null){
			cmd.append("and PROC_CODE = ? ");
			params.add(tranTypeAry[2]);
		}
		
		// M2020020 新增"卡號"查詢條件
		if (ValidateUtil.isNotBlank(arg.getCardNum())) {
			String cardNum = maskCardNo(arg.getCardNum());
			cmd.append("and CARD_NUM = ? ");
			params.add(cardNum);
		}
		
		// M2020020 新增"SEQ_ID"查詢條件
		if (arg.getSeqId() != 0L) {
			cmd.append("and SEQ_ID = ? ");
			params.add(arg.getSeqId());
		}
	}

	/**
	 * @return
	 * @throws QueryServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<LDSSTransLogArg> query(LDSSTransLogArg arg) throws QueryServiceException {

		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select * from ldss_trans_log where 1 = 1 and rownum < 3001 ");

		prepareArg(arg, params, cmd);
//		arg.getPagingInfo().setEnablePaging(true);
		arg.getPagingInfo().setPageSize(20);
		//	排序選擇 回應碼需要另外處理 排序順序為  空白、A、D、C、P 在 LDSSTransLogProcImpl 處理
		List<LDSSTransLogArg> list = null;
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
			
			list = (List<LDSSTransLogArg>) this.queryByPagingInfo(cmd.toString(), params,
					builder, arg.getPagingInfo());
		}else{
			list = this.queryObjectList(cmd.toString(), params, builder);
//			list = (List<LDSSTransLogArg>) this.queryByPagingInfo(cmd.toString(), params,
//					builder, arg.getPagingInfo());
			if(list != null && list.size() > 0){
			Collections.sort(list, new Comparator<LDSSTransLogArg>(){
				final String[] order = { "", "A", "D", "C", "P" };
				
				@Override
				public int compare(LDSSTransLogArg arg0, LDSSTransLogArg arg1) {
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
				
				private int getIndex(LDSSTransLogArg arg){
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
	

	private List<LDSSTransLogArg> setPageInfo(PagingInfo pagingInfo, List<LDSSTransLogArg> list){
		int rowCount = 0;
		int pageSize = pagingInfo.getPageSize();
		int currentPage = pagingInfo.getCurrentPage();
		boolean paging = false;
		List<LDSSTransLogArg> result = new ArrayList<LDSSTransLogArg>();
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
	public List<LDSSTransLogArg> queryByIds(String[] seqIds) throws QueryServiceException {

		if (seqIds == null || seqIds.length <= 0)
			return new ArrayList<LDSSTransLogArg>();

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
			return new ArrayList<LDSSTransLogArg>();

		criteria.append(") ");

		StringBuffer cmd = new StringBuffer().append("select * from ldss_trans_log ").append("where ")
				.append(criteria);

		@SuppressWarnings("unchecked")
		List<LDSSTransLogArg> list = this.queryObjectList(cmd.toString(), params, builder);

		return list;
	}

	/**
	 * @param seqId
	 * @return
	 * @throws QueryServiceException
	 */
	public LDSSTransLogArg queryById(Long seqId) throws QueryServiceException {

		List<Object> params = new ArrayList<Object>();
		StringBuffer cmd = new StringBuffer().append("select * from ldss_trans_log ").append("where seq_id = ? order by log_date desc");
		params.add(seqId);

		return (LDSSTransLogArg) this.queryObject(cmd.toString(), params, builder);
	}

	/**
	 * 遮罩卡號
	 * 16碼(含)以上，僅留前6後4碼為名碼, 中間顯示 *.
	 * 不到16碼， 遮罩卡號, 自訂第6位之後的遮罩區間.
	 * 
	 * @param cardNo
	 */
	private String maskCardNo(String cardNum) {
		if (ValidateUtil.isNotBlank(cardNum) || !"0000000000000000".equals(cardNum)) {
			StringBuffer buffer = new StringBuffer(cardNum);
			StringBuffer sb = new StringBuffer();
			int endIndex = cardNum.length();
			if (endIndex >= 16) {
				int start = 6, last = 4, keep = start + last;
				if (endIndex >= keep) {
					for (int i = 0; i < endIndex - keep; i++) {
						sb.append("*");
					}
					buffer.replace(start, buffer.length() - last, sb.toString());
				}
				return buffer.toString();
			} else {
				if (endIndex >= 6) {
					sb.append(cardNum.substring(0, 6));
					endIndex = (endIndex > 12) ? 12 : endIndex;
					for (int m = 6; m < endIndex; m++) {
						sb.append("*");
					}
					if (endIndex > 12) {
						sb.append(cardNum.substring(12));
					}
				} else {
					sb.append(cardNum);
				}
				return sb.toString();
			}
		}
		return cardNum;
	}
}
