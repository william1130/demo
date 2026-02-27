package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordMaster;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

public interface EmvTagRecordMasterQS extends BaseCRUDQS
{
		public static final JdbcPersistableBuilder emvTagRecordMasterBuilder = new AbstractJdbcPersistableBuilder()
		{
			protected JdbcPersistable build(ResultSet rs) throws SQLException
			{
				EmvTagRecordMaster obj = new EmvTagRecordMaster();
				ResultSetTool rst = new ResultSetTool(rs);
				
				obj.setEmvTag(rst.getString("EMV_TAG"));
				obj.setEmvName(rst.getString("EMV_NAME"));
				obj.setEmvAbbr(rst.getString("EMV_ABBR"));
				obj.setEmvDesc(rst.getString("EMV_DESC"));
				obj.setEmvLen(rst.getInt("EMV_LEN"));
				obj.setCardType(rst.getString("CARD_TYPE"));
				obj.setSameValueFlag(rst.getString("SAME_VALUE_FLAG"));
				obj.setStatus(rst.getString("STATUS"));
				obj.setCreateInfo( AlterInfo.buildCreateInfo( rst ) );
				obj.setUpdateInfo(AlterInfo.buildUpdateInfo( rst ));

				return obj;
			}
		};	

		//public EmvTagRecordMaster queryByEmvTagAndSameValueFlag(String emvTag, String sameValueFlag) throws QueryServiceException;
		public EmvProjPersistable queryByPrimaryKey(String emvTag, String sameValueFlag) throws QueryServiceException;
}
