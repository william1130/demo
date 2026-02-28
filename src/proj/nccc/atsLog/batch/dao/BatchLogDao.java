package proj.nccc.atsLog.batch.dao;

import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.entity.BatchLog;

@Log4j2
public class BatchLogDao extends ProjDao {
	
	
	@Override
	protected BatchLog buildObj(ResultSet rs) throws Exception {
		BatchLog o = new BatchLog();
		o.setBatchId(rs.getString("BATCH_ID"));
		o.setUuid(rs.getString("UUID"));
		if (rs.getTimestamp("BATCH_START_DATE") != null) {
			o.setBatchStartDate(rs.getTimestamp("BATCH_START_DATE"));
		}
		
		if (rs.getTimestamp("BATCH_END_DATE") != null) {
			o.setBatchEndDate(rs.getTimestamp("BATCH_END_DATE"));
		}
		
		o.setBatchStatus(rs.getString("BATCH_STATUS"));
		o.setDescription(rs.getString("DESCRIPTION"));
		return o;
	}
	
	/**
	 * insert into BatchLog
	 * 
	 * @param jobName
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public String insertBatchLog(String jobName, String status) throws Exception {
		
		String sql = "INSERT into SYS_BATCH_LOG "
				+ " ( UUID, BATCH_ID, BATCH_START_DATE, BATCH_END_DATE, BATCH_STATUS )"
				+ " Values ( ?, ?, ?, ?, ? ) ";
		Date startDate = new Date();
		List<Object> params = new LinkedList<Object>();
		String uuid = UUID.randomUUID().toString();
		params.add(uuid);
		params.add(jobName);
		params.add(startDate);
		params.add(null);
		params.add(status);
		
		super.execUpdate(sql, params);
		return uuid;
		
	}

	/**
	 * update batch log
	 * @param jobName
	 * @param startDate
	 * @param status
	 * @param description
	 * @return
	 * @throws Exception
	 */
	public boolean updateBatchLog(String uuid, String status, String description) throws Exception {
		String sql = "update SYS_BATCH_LOG "
				+ " set BATCH_END_DATE = ?, BATCH_STATUS = ?, DESCRIPTION = ? "
				+ " where UUID = ? ";
		List<Object> params = new LinkedList<Object>();
		
		params.add(new Date());
		params.add(status);
		params.add(StringUtils.left(description, 255));
		
		params.add(uuid);
		
		if (super.execUpdate(sql, params) > 0) {
			return true;
		}else {
			log.info("update fail.");
			return false;
		}
	}
	
	public List<BatchLog> queryBatchLogList(String batchId) throws Exception {
		String sql = "select * from SYS_BATCH_LOG where BATCH_ID = ? order by BATCH_START_DATE desc";
		List<String> params = new LinkedList<String>();
		params.add(batchId);
		@SuppressWarnings("unchecked")
		List<BatchLog> list = (List<BatchLog>) queryList(sql, params);
		return list;
	}
	
	/**
	 * 取得最後一筆
	 * @param batchId
	 * @return
	 * @throws Exception
	 */
	public BatchLog queryLastItem(String batchId) throws Exception {
		List<BatchLog> list = this.queryBatchLogList(batchId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
