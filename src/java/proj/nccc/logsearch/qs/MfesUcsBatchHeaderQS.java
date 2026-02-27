package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import proj.nccc.logsearch.persist.CodeValueVO;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

public interface MfesUcsBatchHeaderQS extends BaseCRUDQS
{
	public static final JdbcPersistableBuilder mfesBatchHeaderBuilder = new AbstractJdbcPersistableBuilder()
	{
		protected JdbcPersistable build(ResultSet rs) throws SQLException
		{
			CodeValueVO obj = new CodeValueVO();
			ResultSetTool rst = new ResultSetTool(rs);
			obj.setItemCode(rst.getString("MERCHANT_ID"));
			obj.setItemValue(rst.getString("TERMINAL_ID"));
			return obj;
		}
	};
	/**
	 * 取得未結帳之端末機代號
	 * @param merchantNo
	 * @return
	 * @throws QueryServiceException
	 */
	public List<CodeValueVO> getUncloseTerminalId(String merchantNo) throws QueryServiceException;
}
