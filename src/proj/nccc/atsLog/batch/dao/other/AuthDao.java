package proj.nccc.atsLog.batch.dao.other;

import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.connection.AuthConn;

@Log4j2
public class AuthDao extends AuthConn {
	
	public int count(String table, String where) throws Exception {
		int i = 0;
		StringBuffer cmd = new StringBuffer("");
		try {
			con = super.getConnection();
			cmd.append("SELECT count(*) FROM " + table);
			cmd.append(" WHERE " + where);
			log.debug(cmd.toString());
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			rs = ps.executeQuery();
			if (rs.next()) {
				String s = rs.getString(1);
				try {
					i = Integer.parseInt(s);
				} catch (Exception e) {
				}
			}
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				this.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void commit() {
		try {
			if (con == null || con.isClosed()) {
				con = super.getConnection();
			}
			con.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	
	/**
	 * - 取得CallBank 資料
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryCallBank() throws Exception{
		String sql = "SELECT  CM_PARA_TYPE, CM_PARA_CODE, CM_PARA_AMT "
				+ " FROM CM_CALLBANKPARA "
				+ " WHERE CM_PARA_TYPE = '1' "
				+ " OR CM_PARA_TYPE = '2' ";
		return super.queryListMap(sql, null);
	}
}
