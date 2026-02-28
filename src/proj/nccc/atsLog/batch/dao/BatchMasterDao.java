package proj.nccc.atsLog.batch.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.atsLog.batch.dao.entity.BatchMaster;

public class BatchMasterDao extends ProjDao {
	
	@Override
	protected BatchMaster buildObj(ResultSet rs) throws Exception {
		BatchMaster o = new BatchMaster();
		o.setBatchId(rs.getString("BATCH_ID"));
		o.setBatchName(rs.getString("BATCH_NAME"));
		o.setBatchCycleType(rs.getString("BATCH_CYCLE_TYPE"));
		o.setBatchCycleTime(rs.getInt("BATCH_CYCLE_TIME"));
		o.setExecWarningOverTime(rs.getInt("EXEC_WARNING_OVER_TIME"));
		o.setNoticeType(rs.getString("NOTICE_TYPE"));
		o.setStatus(rs.getString("STATUS"));
		o.setNoAlertMinus(rs.getInt("NO_ALERT_MINUS"));
		o.setNsshmsglLevel(rs.getString("NSSHMSGL_LEVEL"));
		return o;
	}
	
	/**
	 * - 取得上次執行時間
	 * @param batchId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	public Date queryLastStartTime(String batchId, Date startDate, Date endDate) throws Exception {
		String sql = "select BATCH_START_DATE from SYS_BATCH_LOG "
				+ " where BATCH_ID = ? and BATCH_STATUS = 'START' "
				+ " and BATCH_START_DATE >= ? and BATCH_START_DATE <= ? "
				+ " order by BATCH_START_DATE ";
		
		List<Object> params = new LinkedList<Object>();
		params.add(batchId);
		params.add(startDate);
		params.add(endDate);
		
		List<Map<String, Object>> ret = super.queryListMap(sql, params);
		if (ret == null || ret.size() <= 0) {
			return null;
		}
		Map<String, Object> map = ret.get(0);
		Date batchStartDate = (Date) map.get("BATCH_START_DATE") ;
		return batchStartDate;
	}
	
	/**
	 * - 逾期
	 * @param batchId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	public boolean isExecuteOverTime(String batchId, Date startDate, Date endDate) 
			throws Exception {
		return queryStatusByExecuteTime(batchId, "LIMIT", startDate, endDate);
	}
	
	/**
	 * - 是否有執行
	 * @param batchId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	public boolean isExecuted(String batchId, Date startDate, Date endDate) 
			throws Exception {
		return queryStatusByExecuteTime(batchId, null, startDate, endDate);
	}
	
	/**
	 * @param batchId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	private boolean queryStatusByExecuteTime(String batchId, String type, 
			Date startDate, Date endDate) throws Exception {
		
		String sql = "select * from SYS_BATCH_LOG "
				+ " where BATCH_ID=? "
				+ " and BATCH_START_DATE >= ? and BATCH_START_DATE <= ?";
		if ("LIMIT".equals(type)){
			sql = "select * from SYS_BATCH_LOG "
					+ " where BATCH_ID=? and BATCH_STATUS='CLOSE' "
					+ " and BATCH_END_DATE >= ? and BATCH_END_DATE <= ?";
		}
		boolean b = false;
		try {
			if (con == null || con.isClosed()) {
				con = super.getConnection();
				b = true;
			}
			ps = con.prepareStatement(sql);
			ps.clearParameters();
			int i = 1;
			ps.setString(i++, batchId);
			ps.setTimestamp(i++, new Timestamp(startDate.getTime()));
			ps.setTimestamp(i++, new Timestamp(endDate.getTime()));
			
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		}finally {
			if (b) {
				super.close();
			}else {
				super.closeRsPs();
			}
		}
	}
	
	public List<BatchMaster> getBatchMasterList() throws Exception {
		String sql = "select * from SYS_BATCH_MASTER where STATUS = 'Y' ";
		
		@SuppressWarnings("unchecked")
		List<BatchMaster> list = (List<BatchMaster>) queryList(sql, null);
		return list;
	}

}
