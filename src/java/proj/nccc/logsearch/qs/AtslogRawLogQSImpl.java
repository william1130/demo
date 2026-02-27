package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import proj.nccc.logsearch.persist.AtslogRawLog;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;

public class AtslogRawLogQSImpl extends ProjQS {
	
	private static JdbcPersistableBuilder builder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			AtslogRawLog obj = new AtslogRawLog();
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
	public AtslogRawLog queryByPrimaryKey(long seqId, String rawType) throws QueryServiceException {
		
		List<Object> params = new ArrayList<Object>();
		
		String cmd = new StringBuffer()
			.append("select * from atslog_raw_log ")
			.append("where seq_id = ? and raw_type = ? order by log_date desc, log_time desc")
			.toString();

		params.add(Long.valueOf(seqId));
		params.add(rawType);

		@SuppressWarnings("unchecked")
		List<AtslogRawLog> list = this.queryObjectList(cmd, params, builder);
		return list.size() > 0 ? list.get(0) : null;
	}

}
