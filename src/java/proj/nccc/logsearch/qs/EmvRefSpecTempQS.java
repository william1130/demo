package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvRefSpecTemp;
import proj.nccc.logsearch.vo.EmvRefSpecTempArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.sql.ResultSetTool;

public interface EmvRefSpecTempQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvRefSpecTempBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException {
				EmvRefSpecTemp obj = new EmvRefSpecTemp();
				ResultSetTool rst = new ResultSetTool(rs);
				
				obj.setSpecID(rst.getString("SPEC_ID"));
				obj.setSpecName(rst.getString("SPEC_NAME"));
				obj.setActiveCode(rst.getString("ACTIVE_CODE"));
				obj.setRequestInfo( AlterInfo.buildRequestInfo( rst ) );
				
				return obj;
			}
		};
		
		public static final JdbcPersistableBuilder emvRefSpecTempArgBuilder = new AbstractJdbcPersistableBuilder() {
			protected JdbcPersistable build(ResultSet rs) throws SQLException {
				EmvRefSpecTempArg obj = new EmvRefSpecTempArg();
				ResultSetTool rst = new ResultSetTool(rs);

				obj.setSpecID(rst.getString("SPEC_ID"));
				obj.setSpecName(rst.getString("SPEC_NAME"));
				obj.setActiveCode(rst.getString("ACTIVE_CODE"));
				obj.setRequestInfo( AlterInfo.buildRequestInfo( rst ) );

				return obj;
			}
		};
		
		public int countToBeApprove();

}
