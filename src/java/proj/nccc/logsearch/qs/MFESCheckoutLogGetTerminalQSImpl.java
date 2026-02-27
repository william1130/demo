package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import proj.nccc.logsearch.vo.MFESCheckoutLogArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;

public class MFESCheckoutLogGetTerminalQSImpl extends ProjQS {

	private static JdbcPersistableBuilder builder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			MFESCheckoutLogArg obj = new MFESCheckoutLogArg();
			obj.setApplyCode(rs.getString("TERMINAL_ID"));
			return obj;
		}
	};

	/**
	 * @return
	 * @throws QueryServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<MFESCheckoutLogArg> query(List<MFESCheckoutLogArg> list) throws QueryServiceException {
		
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append("select TERMINAL_ID from ATS_CURRENT_BATCH_HEADER where CLOSE_DATETIME = ' ' and MERCHANT_ID in ");

		StringBuffer merchantIdString = new StringBuffer().append ("(");
		for(Iterator itr = list.iterator(); itr.hasNext();){
			MFESCheckoutLogArg arg = (MFESCheckoutLogArg) itr.next();
			merchantIdString.append("'").append(arg.getMerchantId()).append("'").append(",");
		}
		cmd.append(merchantIdString.substring(0, merchantIdString.length() -1));
		cmd.append(")");
		
		List<MFESCheckoutLogArg> result = this.queryObjectList(cmd.toString(), params, builder);
		
		return result;
	}
	
	/**
	 * @return
	 * @throws QueryServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<MFESCheckoutLogArg> queryByMerchId(String merchId) throws QueryServiceException {
		
		
		List params = new LinkedList();
		params.add(merchId);
		
		StringBuffer cmd = new StringBuffer("SELECT TERMINAL_ID "
				+ " FROM ATS_CURRENT_BATCH_HEADER "
				+ " WHERE CLOSE_DATETIME = ' ' "
				+ " AND MERCHANT_ID = ?  ORDER BY TERMINAL_ID ");
		
		List<MFESCheckoutLogArg> result = this.queryObjectList(cmd.toString(), params, builder);
		
		return result;
	}
}
