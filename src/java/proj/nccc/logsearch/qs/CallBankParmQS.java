
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: CallBankParmQS.java,v 1.1 2017/04/24 01:31:15 asiapacific\jih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.edstw.lang.DoubleString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

import proj.nccc.logsearch.persist.CallBankParm;

/**
 * M2010087人工授權交易國外卡監控作業增加CALLBANK 參數
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.1 $
 */
public interface CallBankParmQS extends BaseCRUDQS {
	public static final JdbcPersistableBuilder CallBankParmBuilder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			ResultSetTool rst = new ResultSetTool(rs);
			CallBankParm obj = new CallBankParm();
			obj.setParmType(rst.getString("PARM_TYPE"));
			obj.setParmCode(rst.getString("PARM_CODE"));
			obj.setParmAmt(rst.getString("PARM_AMT"));

			return obj;

		}
	};

	public CallBankParm queryByPrimaryKey(String rowid) throws QueryServiceException;

	public DoubleString getCallBankParmAmt(String parmcode) throws QueryServiceException;

}
