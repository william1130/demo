package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import proj.nccc.logsearch.persist.ChipcardInfo;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;

public class ChipcardInfoQSImpl extends ProjQS {
	
	private static JdbcPersistableBuilder builder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			ChipcardInfo obj = new ChipcardInfo();
			obj.setTagName(rs.getString("tag_name"));
			obj.setCardType(rs.getString("card_type"));
			obj.setChipType(rs.getString("chip_type"));
			obj.setByteIndex(rs.getInt("byte"));
			obj.setBitIndex(rs.getInt("bit_index"));
			obj.setBitLength(rs.getInt("length"));
			obj.setMeaning(rs.getString("meaning"));
			obj.setDataType(rs.getString("data_type"));
			obj.setDisplayType(rs.getString("display_type"));
			return obj;
		}
	};
	
	/**
	 * @return
	 * @throws QueryServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<ChipcardInfo> queryAll() throws QueryServiceException {

		String cmd = new StringBuffer()
			.append("select * from chipcard_info ")
			.toString();
		
		return (List<ChipcardInfo>)this.queryObjectList(cmd, builder);
	}

}
