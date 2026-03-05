package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

import proj.nccc.logsearch.persist.Parameter;

public class ParameterQSImpl extends ProjQS {
	
	public static JdbcPersistableBuilder parameterBuilder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			Parameter obj = new Parameter();
			ResultSetTool rst = new ResultSetTool(rs);
			obj.setUuid(rst.getString("UUID"));
			obj.setType(rst.getString("LOOKUP_TYPE"));
			obj.setCode(rst.getString("LOOKUP_CODE"));
			obj.setDescription(rst.getString("LOOKUP_DESC"));
			obj.setValue(rst.getString("VALUE"));
			obj.setValueExt1(rst.getString("VALUE_EXT1"));
			obj.setValueExt2(rst.getString("VALUE_EXT2"));
			obj.setValueExt3(rst.getString("VALUE_EXT3"));
			obj.setStatus(rst.getString("STATUS"));
			obj.setListSeq(rst.getIntegerString("LIST_SEQ"));
			obj.setCreateUser(rst.getString("CREATE_USER"));
			obj.setCreateDate(rst.getDate("CREATE_DATE"));
			obj.setUpdateUser(rst.getString("LAST_UPDATE_USER"));
			obj.setUpdateDate(rst.getDate("LAST_UPDATE_DATE"));
			return obj;
		}
	};
	
	public List queryAll() throws QueryServiceException {
		StringBuffer cmd = new StringBuffer("SELECT * FROM SYS_PARA ORDER BY LOOKUP_TYPE, LIST_SEQ");
		return super.queryObjectList(cmd.toString(), parameterBuilder);
	}

	public Parameter queryByPrimaryKey(String type, String code) throws QueryServiceException {
		List params = new LinkedList();
		params.add(type);
		params.add(code);
		StringBuffer cmd = new StringBuffer("SELECT * FROM SYS_PARA WHERE LOOKUP_TYPE=? AND LOOKUP_CODE=? ");
		return (Parameter) super.queryObject(cmd.toString(), params, parameterBuilder);
	}

	public List queryByType(String type) throws QueryServiceException {
		List params = new LinkedList();
		params.add(type);
		StringBuffer cmd = new StringBuffer("SELECT * FROM SYS_PARA WHERE LOOKUP_TYPE=? ORDER BY LIST_SEQ");
		return super.queryObjectList(cmd.toString(),params, parameterBuilder);
	}

}
