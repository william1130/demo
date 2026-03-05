package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvCardType;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.sql.ResultSetTool;

public interface EmvCardTypeQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvCardTypeBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException
			{
				EmvCardType obj = new EmvCardType();
				ResultSetTool rst = new ResultSetTool(rs);
				
				obj.setCardType(rst.getString("CARD_TYPE"));
				obj.setCardTypeName(rst.getString("CARD_TYPE_NAME"));
				obj.setStatus(rst.getString("STATUS"));
				obj.setCreateInfo( AlterInfo.buildCreateInfo( rst ) );
				obj.setUpdateInfo(AlterInfo.buildUpdateInfo( rst ));
				/*obj.setCreateUser(rs.getString("CREATE_USER"));
				obj.setCreateDate(rs.getDate("CREATE_DATE"));
				obj.setLastUpdateUser(rs.getString("LAST_UPDATE_USER"));
				obj.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));*/
				return obj;
			}
		};	
}
