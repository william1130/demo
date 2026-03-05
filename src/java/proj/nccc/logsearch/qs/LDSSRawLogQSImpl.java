package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import proj.nccc.logsearch.persist.LDSSRawLog;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;

public class LDSSRawLogQSImpl extends ProjQS {
	
	private static JdbcPersistableBuilder builder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			LDSSRawLog obj = new LDSSRawLog();
			obj.setSeqId(rs.getLong("seq_id"));
			obj.setLogDate(rs.getString("log_date"));
			obj.setLogTime(rs.getString("log_time"));
			obj.setRawType(rs.getString("raw_type"));
			obj.setRawData(rs.getString("raw_data"));
			return obj;
		}
	};

	/**
	 * @param arg
	 * @return
	 * @throws QueryServiceException
	 */
	public LDSSRawLog queryByPrimaryKey(long seqId, String rawType) throws QueryServiceException {
		
		List<Object> params = new ArrayList<Object>();
		
		String cmd = new StringBuffer()
			.append("select * from ldss_raw_log ")
			.append("where seq_id = ? and raw_type = ? order by log_date desc, log_time desc")
			.toString();

		params.add(Long.valueOf(seqId));
		params.add(rawType);

		@SuppressWarnings("unchecked")
		List<LDSSRawLog> list = this.queryObjectList(cmd, params, builder);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public List<LDSSRawLog> queryBySeqId(long seqId) throws QueryServiceException {
		
		List<Object> params = new ArrayList<Object>();
		
		String cmd = new StringBuffer()
			.append("select * from ldss_raw_log ")
			.append("where seq_id = ? order by log_date asc, log_time asc")
			.toString();

		params.add(Long.valueOf(seqId));

		@SuppressWarnings("unchecked")
		List<LDSSRawLog> list = this.queryObjectList(cmd, params, builder);
		return list;
	}

	public LDSSRawLog queryByPKLogTime(long seqId, String rawType, String logTime) throws QueryServiceException {
		
		List<Object> params = new ArrayList<Object>();
		
		String cmd = new StringBuffer()
			.append("select * from ldss_raw_log ")
			.append("where seq_id = ? and raw_type = ? and log_time = ? ")
			.append(" order by log_date desc, log_time desc")
			.toString();

		params.add(Long.valueOf(seqId));
		params.add(rawType);
		params.add(logTime);

		@SuppressWarnings("unchecked")
		List<LDSSRawLog> list = this.queryObjectList(cmd, params, builder);
		return list.size() > 0 ? list.get(0) : null;
	}

}
