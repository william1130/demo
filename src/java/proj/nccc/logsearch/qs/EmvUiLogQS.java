package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.EmvUiLog;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.sql.ResultSetTool;

public interface EmvUiLogQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvUiLogBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException
			{
				EmvUiLog obj = new EmvUiLog();
				ResultSetTool rst = new ResultSetTool( rs );

				obj.setUiDate(rst.getDate("UI_DATE"));
				obj.setUserId(rst.getString("USER_ID"));
				obj.setUuid(rst.getString("UUID"));
				obj.setUiFunction(rst.getString("UI_FUNCTION"));
				obj.setUiOther(rst.getString("UI_OTHER"));

				return obj;
			}
		};	
		
}
