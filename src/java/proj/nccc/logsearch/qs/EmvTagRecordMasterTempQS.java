package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvTagRecordMasterTemp;
import proj.nccc.logsearch.vo.EmvTagRecordMasterTempArg;
import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.sql.ResultSetTool;

public interface EmvTagRecordMasterTempQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvTagRecordMasterTempBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException {
				EmvTagRecordMasterTemp obj = new EmvTagRecordMasterTemp();
				ResultSetTool rst = new ResultSetTool(rs);
				
				obj.setEmvTag(rst.getString("EMV_TAG"));
				obj.setEmvName(rst.getString("EMV_NAME"));
				obj.setEmvAbbr(rst.getString("EMV_ABBR"));
				obj.setEmvDesc(rst.getString("EMV_DESC"));
				obj.setEmvLen(rst.getInt("EMV_LEN"));
				obj.setCardType(rst.getString("CARD_TYPE"));
				obj.setSameValueFlag(rst.getString("SAME_VALUE_FLAG"));
				obj.setActiveCode(rst.getString("ACTIVE_CODE"));
				obj.setOriSameValueFlag(rst.getString("ORI_SAME_VALUE_FLAG"));
				obj.setRequestInfo( AlterInfo.buildRequestInfo( rst ) );

				return obj;
			}
		};
		
		public static final JdbcPersistableBuilder emvTagRecordMasterTempArgBuilder = new AbstractJdbcPersistableBuilder() {
			protected JdbcPersistable build(ResultSet rs) throws SQLException {
				EmvTagRecordMasterTempArg obj = new EmvTagRecordMasterTempArg();
				ResultSetTool rst = new ResultSetTool(rs);

				obj.setEmvTag(rst.getString("EMV_TAG"));
				obj.setEmvName(rst.getString("EMV_NAME"));
				obj.setEmvAbbr(rst.getString("EMV_ABBR"));
				obj.setEmvDesc(rst.getString("EMV_DESC"));
				obj.setEmvLen(rst.getInt("EMV_LEN"));
				obj.setCardType(rst.getString("CARD_TYPE"));
				obj.setSameValueFlag(rst.getString("SAME_VALUE_FLAG"));
				obj.setActiveCode(rst.getString("ACTIVE_CODE"));
				obj.setOriSameValueFlag(rst.getString("ORI_SAME_VALUE_FLAG"));
				obj.setRequestInfo( AlterInfo.buildRequestInfo( rst ) );

				return obj;
			}
		};
		
		public int countToBeApprove();

}
