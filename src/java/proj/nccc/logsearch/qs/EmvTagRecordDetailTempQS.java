package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import proj.nccc.logsearch.persist.EmvTagRecordDetailTemp;
import proj.nccc.logsearch.vo.EmvTagRecordDetailTempArg;
import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

public interface EmvTagRecordDetailTempQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvTagRecordDetailTempBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException {
				EmvTagRecordDetailTemp obj = new EmvTagRecordDetailTemp();
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setEmvTag(rst.getString("EMV_TAG"));
				obj.setCardType(rst.getString("CARD_TYPE"));
				obj.setPosByte(rst.getInt("POS_BYTE"));
				obj.setValueByte(rst.getString("VALUE_BYTE"));
				obj.setPosBit(rst.getInt("POS_BIT"));
				obj.setValueBit(rst.getString("VALUE_BIT"));
				obj.setSameValueFlag(rst.getString("SAME_VALUE_FLAG"));

				return obj;
			}
		};
		
		public static final JdbcPersistableBuilder emvTagRecordDetailTempArgBuilder = new AbstractJdbcPersistableBuilder() {
			protected JdbcPersistable build(ResultSet rs) throws SQLException {
				EmvTagRecordDetailTempArg obj = new EmvTagRecordDetailTempArg();
				ResultSetTool rst = new ResultSetTool(rs);

				obj.setEmvTag(rst.getString("EMV_TAG"));
				obj.setCardType(rst.getString("CARD_TYPE"));
				obj.setPosByte(rst.getInt("POS_BYTE"));
				obj.setValueByte(rst.getString("VALUE_BYTE"));
				obj.setPosBit(rst.getInt("POS_BIT"));
				obj.setValueBit(rst.getString("VALUE_BIT"));
				obj.setSameValueFlag(rst.getString("SAME_VALUE_FLAG"));

				return obj;
			}
		};
		
		public int countToBeApprove();
		public List queryByEmvTagAndSameValueFlag(String emvTag, String sameValueFlag) throws QueryServiceException;
}
