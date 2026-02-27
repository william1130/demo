package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * table=IFES_RAW_LOG
 * @author Gail Lee
 * @version 1.0
 */
public class LDSSRawLog extends ProjPersistable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9042222325294870265L;

	//	seq_id pk fk->atslog_trans_log.seq_id
	private long seqId;
	
	//	raw_type pk
	private String rawType;
	
	//	log_date
	private String logDate;
	
	//	log_time
	private String logTime;
	
	//	raw_data
	private String rawData;
	
	//	---------------------------------------------------------

	/* (non-Javadoc)
	 * @see com.edstw.nccc.sql.log.AbstractJdbcStatefulPersistable4AccLog#createValueImage()
	 */
	public ValueImage createValueImage() {

		return new ValueImage();
	}
	
	//	---------------------------------------------------------

	/**
	 * @return the seqId
	 */
	public long getSeqId() {
		return seqId;
	}

	/**
	 * @param seqId the seqId to set
	 */
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	/**
	 * @return the rawType
	 */
	public String getRawType() {
		return rawType;
	}

	/**
	 * @param rawType the rawType to set
	 */
	public void setRawType(String rawType) {
		this.rawType = rawType;
	}

	/**
	 * @return the logDate
	 */
	public String getLogDate() {
		return logDate;
	}

	/**
	 * @param logDate the logDate to set
	 */
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	/**
	 * @return the logTime
	 */
	public String getLogTime() {
		return logTime;
	}

	/**
	 * @param logTime the logTime to set
	 */
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	/**
	 * @return the rawData
	 */
	public String getRawData() {
		return rawData;
	}

	/**
	 * @param rawData the rawData to set
	 */
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	
	
}
