package proj.nccc.atsLog.batch.dao.other;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.connection.NmipConn;

@Log4j2
public class NmipDao extends NmipConn {

	public Map<String, String> getNmipKeyMap() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer cmd = new StringBuffer("");
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			// M2023089_R112153_檔案及字串加密演算法修改，bank_id 由"997"改為"996"，取得的KEY_ID=C(需知會清算檔名有變動)
			cmd.append("select KEY_ID, KEY_LABEL, KEY_TYPE from NMIP_MKEY_ID ");
			cmd.append("where bank_id='996' and TO_CHAR (SYSDATE, 'YYYYMMDD') >= start_date ");
			cmd.append("and (end_date is null or TO_CHAR (SYSDATE, 'YYYYMMDD') <= end_date ) ");
			cmd.append("and data_from='B' ");
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("KEY_ID", rs.getString("KEY_ID"));
				map.put("KEY_LABEL", rs.getString("KEY_LABEL"));
				map.put("KEY_TYPE", rs.getString("KEY_TYPE"));
			}
			return map;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				// 不close connect , for performance
				// this.close();
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("MchtUtilDao.getAreaMap.finally exception:" + e);
			}
		}
	}
}
