package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvRefSpec;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.sql.ResultSetTool;

public interface EmvRefSpecQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvRefSpecBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException
			{
				EmvRefSpec obj = new EmvRefSpec();
				ResultSetTool rst = new ResultSetTool(rs);
				
				obj.setSpecID(rst.getString("SPEC_ID"));
				obj.setSpecName(rst.getString("SPEC_NAME"));
				obj.setStatus(rst.getString("STATUS"));
				obj.setCreateInfo( AlterInfo.buildCreateInfo( rst ) );
				obj.setUpdateInfo(AlterInfo.buildUpdateInfo( rst ));

				return obj;
			}
		};	
		
		public EmvRefSpec queryByPrimaryKey(String string);
}
