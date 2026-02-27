package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvCardTypeTemp;
import proj.nccc.logsearch.vo.EmvCardTypeTempArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.sql.ResultSetTool;

public interface EmvCardTypeTempQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvCardTypeTempBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException {
				EmvCardTypeTemp obj = new EmvCardTypeTemp();
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setCardType(rst.getString("CARD_TYPE"));
				obj.setCardTypeName(rst.getString("CARD_TYPE_NAME"));
				obj.setActiveCode(rst.getString("ACTIVE_CODE"));
				obj.setRequestInfo( AlterInfo.buildRequestInfo( rst ) );
				/*obj.setRequestUser(rst.getString("REQUEST_USER"));
				obj.setRequestDate(rst.getDate("REQUEST_DATE"));*/

				return obj;
			}
		};
		
		public static final JdbcPersistableBuilder emvCardTypeTempArgBuilder = new AbstractJdbcPersistableBuilder() {
			protected JdbcPersistable build(ResultSet rs) throws SQLException {
				EmvCardTypeTempArg obj = new EmvCardTypeTempArg();
				ResultSetTool rst = new ResultSetTool(rs);

				obj.setCardType(rst.getString("CARD_TYPE"));
				obj.setCardTypeName(rst.getString("CARD_TYPE_NAME"));
				obj.setActiveCode(rst.getString("ACTIVE_CODE"));
				obj.setRequestInfo( AlterInfo.buildRequestInfo( rst ) );
				/*obj.setRequestUser(rst.getString("REQUEST_USER"));
				obj.setRequestDate(rst.getDate("REQUEST_DATE"));*/

				return obj;
			}
		};
		
		public int countToBeApprove();

}
