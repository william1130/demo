package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import proj.nccc.logsearch.persist.EmvTagRecordDetail;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

public interface EmvTagRecordDetailQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvTagRecordDetailBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException
			{
				EmvTagRecordDetail obj = new EmvTagRecordDetail();
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

		
		/*public static final JdbcPersistableBuilder emvTagRecordDetailQueryBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException
			{
				EmvTagRecordDetail obj = new EmvTagRecordDetail();
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
		};*/
		
		public List queryByEmvTagAndSameValueFlag(String emvTag, String sameValueFlag) throws QueryServiceException;
}
