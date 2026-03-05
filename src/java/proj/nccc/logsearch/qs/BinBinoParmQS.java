
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: BinBinoParmQS.java,v 1.1 2017/04/24 01:31:15 asiapacific\jih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

import proj.nccc.logsearch.persist.BinBinoParm;

/**
 *
 * @author APAC\czrm4t
 * @version $Revision: 1.1 $
 */
public interface BinBinoParmQS extends BaseCRUDQS {
	public static final JdbcPersistableBuilder BinBinoParmBuilder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {

			BinBinoParm obj = new BinBinoParm();
			ResultSetTool rst = new ResultSetTool(rs);
			obj.setFid(rst.getString("FID"));
			obj.setAcqIss(rst.getString("ACQ_ISS"));
			obj.setCardType(rst.getString("CARD_TYPE"));
			obj.setInDate(rst.getString("IN_DATE"));
			obj.setOutDate(rst.getString("OUT_DATE"));
			obj.setIca(rst.getString("ICA"));
			obj.setCardLen(rst.getInt("CARD_LEN"));
			obj.setAuthOutDate(rst.getString("AUTH_OUT_DATE"));
			obj.setModPgm(rst.getString("MOD_PGM"));
			obj.setModTime(rst.getDateString("MOD_TIME"));
			obj.setTokenFlag(rst.getString("token_flag"));
			return obj;
		}
	};

	public BinBinoParm queryByPrimaryKey(String cardNo) throws QueryServiceException;

	public BinBinoParm getBinParm(String bin, Date today) throws QueryServiceException;

}
