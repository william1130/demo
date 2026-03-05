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

public interface MfesUcsApplyCodeQS extends BaseCRUDQS
{
	public static final JdbcPersistableBuilder mfesApplyCodeBuilder = new AbstractJdbcPersistableBuilder()
	{
		protected JdbcPersistable build(ResultSet rs) throws SQLException
		{
			CodeValueVO obj = new CodeValueVO();
			ResultSetTool rst = new ResultSetTool(rs);
			obj.setItemCode(rst.getString("MCHT_NO"));
			obj.setItemValue(rst.getString("APPLY_CODE"));
			return obj;
		}
	};
	
	/**
	 * 依特店代號取得ApplyCode(請款代碼)
	 * @param merchantNo
	 * @return
	 */
	public List<CodeValueVO> getApplyCode(String merchantNo);
	
	/**
	 * 依ApplyCode(請款代碼)取得特店代號
	 * @param applyCode
	 * @return
	 */
	public List<CodeValueVO> getMerchId(String applyCode) throws QueryServiceException;
}
