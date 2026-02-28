package proj.nccc.atsLog.batch.dao.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import lombok.extern.log4j.Log4j2;
import oracle.sql.BLOB;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.DecodeUtil;
import proj.nccc.atsLog.batch.util.ProjInit;

@Log4j2
public class BaseConn {

	public static Map<String, Properties> dbMap = new HashMap<String, Properties>();
	protected Connection con;
	protected PreparedStatement ps;
	protected Statement st;
	protected ResultSet rs;

	public BaseConn() {
	}

	/**
	 * - 資料庫連線參數
	 * 
	 * @return
	 */
	protected String getDbProperties() {
		return "";
	}

	public Connection getConnection() throws SQLException, IOException, Exception {

		String dbproperties = getDbProperties();

		Properties props = new Properties();
		InputStream in = null;
		Connection conn = null;

		DecodeUtil decodeUtil = new DecodeUtil();
		try {
			if (dbMap.containsKey(dbproperties)) {
				props = dbMap.get(dbproperties);
				log.info("DB info from Map...");
			} else {
				String sPFile = decodeUtil.execDecode(ProjInit.propertyFolder + dbproperties + ".Z",
						ProjInit.propertyFolder + dbproperties);

				in = new FileInputStream(sPFile);
				props.load(in);
				dbMap.put(dbproperties, props);
				log.info("DB info from file...");
			}
			Class.forName(props.getProperty("connection.driver"));

			conn = DriverManager.getConnection(props.getProperty("connection.url"), props);
			return conn;
		} catch (FileNotFoundException e) {
			throw new Exception(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new Exception(e.getMessage(), e);
		} catch (SQLException e) {
			if (EventLogService.errorCount < 1) {
				EventLogService.errorCount++;
				EventLogService.sendLog(dbproperties, EventType.DB_ALERT,
						dbproperties.substring(0, dbproperties.indexOf(".")));
			}
			throw new SQLException(e.getMessage(), e);
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
					decodeUtil.deleteFile();
				}
			} catch (Exception x) {
				log.error(x);
			}
		}
	}

	public void close() {
		try {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			} finally {
				rs = null;
			}
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
			} finally {
				ps = null;
			}
			try {
				if (st != null) {
					st.close();
				}
			} catch (Exception e) {
			} finally {
				st = null;
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	public void closeRsPs() {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
		} finally {
			rs = null;
		}
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
		} finally {
			ps = null;
		}
		try {
			if (st != null) {
				st.close();
			}
		} catch (Exception e) {
		} finally {
			st = null;
		}
	}

	public void setAutoCommit(boolean flag) throws IOException, Exception {
		if (con == null) {
			con = this.getConnection();
		}
		con.setAutoCommit(flag);
	}

	public void commit() throws SQLException {
		if (con != null)
			con.commit();
	}

	public void rollback() throws SQLException {
		if (con != null)
			con.rollback();
	}

	public int execSql(StringBuffer cmd, List<String> parms) throws Exception {
		return execSql(cmd.toString(), parms);
	}

	public int execSql(Connection con, String cmd, List<String> params) throws SQLException, Exception {
		String paramsStr = "";
		try {
			ps = con.prepareStatement(cmd);
			ps.clearParameters();
			if (params != null && params.size() > 0) {
				for (int i = 0; i < params.size(); i++) {
					String s = String.valueOf(params.get(i));
					paramsStr += s + ", ";
					ps.setString(i + 1, s);
				}
			}
			int k = ps.executeUpdate();
			return k;
		} catch (SQLException e) {
			throw new SQLException(e.getMessage() + "\n[" + cmd + "], [" + paramsStr + "]", e);
		} finally {
			if (ps != null) {
				ps.close();
			}
			ps = null;
		}
	}

	public int execSqlByObj(String cmd, List<Object> parms) throws SQLException, Exception {
		String params = "";
		try {
			if (con == null) {
				con = this.getConnection();
			}
			ps = con.prepareStatement(cmd);
			ps.clearParameters();
			if (parms != null && parms.size() > 0) {
				for (int i = 0; i < parms.size(); i++) {
					Object obj = parms.get(i);
					int idx = i + 1;
					if (obj == null) {
						ps.setNull(idx, Types.NULL);
					} else if (obj instanceof String) {
						if (obj != null) {
							ps.setString(idx, obj.toString());
						}
					} else if (obj instanceof Integer) {
						ps.setInt(idx, Integer.parseInt(obj.toString()));
					} else if (obj instanceof Long) {
						ps.setLong(idx, Long.valueOf(obj.toString()));
					} else if (obj instanceof java.sql.Date) {
						if (obj != null) {
							ps.setDate(idx, (java.sql.Date) obj);
						}
					} else if (obj instanceof java.sql.Timestamp) {
						if (obj != null) {
							Date date = new Date(((java.sql.Timestamp) obj).getTime());
							ps.setTimestamp(idx, new Timestamp(date.getTime()));
						}
					} else {
						ps.setString(idx, String.valueOf(obj));
					}
				}
			}
			int k = ps.executeUpdate();
			return k;
		} catch (SQLException e) {
			throw new SQLException(e.getMessage() + "\n[" + cmd + "], [" + params + "]", e);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public int execSql(String cmd, List<String> parms) throws SQLException, Exception {
		String params = "";
		try {
			if (con == null) {
				con = this.getConnection();
			}
			ps = con.prepareStatement(cmd);
			ps.clearParameters();
			if (parms != null && parms.size() > 0) {
				for (int i = 0; i < parms.size(); i++) {
					String s = String.valueOf(parms.get(i));
					params += s + ", ";
					ps.setString(i + 1, s);
				}
				params = params.substring(0, params.length() - 2);
			}
			int k = ps.executeUpdate();
			return k;
		} catch (SQLException e) {
			throw new SQLException(e.getMessage() + "\n[" + cmd + "], [" + params + "]", e);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public boolean execBatchSql(String cmd, List<String[]> params) throws Exception {
		try {
			if (con == null) {
				con = this.getConnection();
			}
			ps = con.prepareStatement(cmd.toString());
			for (String[] param : params) {
				ps.clearParameters();
				if (param != null && param.length > 0) {
					for (int i = 0; i < param.length; i++) {
						String s = param[i];
						ps.setString(i + 1, s);
					}
				}
				ps.addBatch();
			}
			int[] k = ps.executeBatch();
			log.debug("UtilDao. execBatchSql result=" + k.length);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				this.close();
			} catch (Exception e) {
			}
		}
	}

	public boolean truncate(String tableName) throws Exception {
		boolean resultExecute = false;
		log.info("truncate table " + tableName);
		try {
			if (con == null)
				con = this.getConnection();
			ps = con.prepareStatement("truncate table " + tableName);
			resultExecute = ps.execute();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			ps.close();
		}
		return resultExecute;
	}

	public int execSqlRetCnt(String cmd, List<String> parms) throws Exception {
		try {
			con = this.getConnection();
			
			ps = con.prepareStatement(cmd);
			ps.clearParameters();
			if (parms != null && parms.size() > 0) {
				for (int i = 0; i < parms.size(); i++) {
					String s = (String) parms.get(i);
					ps.setString(i + 1, s);
				}
			}
			int k = ps.executeUpdate();
			log.debug("UtilDao. execSql result=" + k);
			return k;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				this.close();
			} catch (Exception e) {
			}
		}
	}

	public int multiExecSql(List<String> commitList) throws Exception {
		int errorNum = 0;
		int insertCnt = 0;
		String sql = "";
		Statement st = null;
		try {
			con = this.getConnection();
			boolean commitStat = con.getAutoCommit();
			con.setAutoCommit(false);
			st = con.createStatement();
			for (int i = 0; i < commitList.size(); i++) {
				errorNum = i;
				sql = (String) commitList.get(i);
				// System.out.println(sql);
				int k = st.executeUpdate(sql);
				insertCnt += k;
			}
			con.commit();
			con.setAutoCommit(commitStat);
			return insertCnt;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(this.getClass().getName() + " : [" + errorNum + "][" + sql + "]" + e.getMessage());
			return -1;
		} finally {
			if (st != null)
				st.close();
			st = null;
			try {
				this.close();
			} catch (Exception e) {
			}
		}
	}

	public int multiExecSql(Connection conn, List<String> commitList) throws Exception {
		int errorNum = 0;
		int insertCnt = 0;
		String sql = "";
		Statement st = null;
		try {
			st = conn.createStatement();
			for (int i = 0; i < commitList.size(); i++) {
				errorNum = i;
				sql = (String) commitList.get(i);
				// System.out.println(sql);
				int k = st.executeUpdate(sql);
				insertCnt += k;
			}
			return insertCnt;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(this.getClass().getName() + " : [" + errorNum + "][" + sql + "]" + e.getMessage());
			return -1;
		} finally {
			if (st != null)
				st.close();
			st = null;
		}
	}

	public int count(String table, String where) throws Exception {
		int i = 0;
		StringBuffer cmd = new StringBuffer("");
		try {
			if (con == null || con.isClosed()) {
				con = this.getConnection();
			}
			cmd.append("SELECT count(*) FROM " + table);
			cmd.append(" WHERE " + where);

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
			throw new Exception(e.getMessage(), e);
		} finally {
			if (rs != null) {
				rs.close();
			}
			rs = null;
			if (ps != null) {
				ps.close();
			}
			ps = null;
		}
	}

	/**
	 * - 執行 SQL query 單一資料
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String query(String sql, List<Object> params) throws Exception {
		List<Map<String, Object>> list = queryListMap(sql, params);
		if (list != null && list.size() > 0) {
			Map<String, Object> map = list.get(0);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() instanceof BigDecimal) {
					return String.valueOf(entry.getValue());
				} else if (entry.getValue() instanceof BigInteger) {
					return String.valueOf(entry.getValue());
				} else if (entry.getValue() instanceof Long) {
					return String.valueOf(entry.getValue());
				} else if (entry.getValue() instanceof java.sql.Timestamp) {
					if (entry.getValue() != null) {
						Date date = new Date(((java.sql.Timestamp) entry.getValue()).getTime());
						return DateUtil.dateToString(date, "yyyyMMdd HHmmss");
					}
					return null;
				} else {
					return String.valueOf(entry.getValue());
				}
			}
		}
		return null;
	}

	/**
	 * 執行 Query
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryListMap(String sql, List<Object> params) throws Exception {

		boolean b = false;
		if (con == null) {
			con = this.getConnection();
		}
		ps = con.prepareStatement(sql);
		ps.clearParameters();
		rs = null;
		ResultSetMetaData rsmd = null;
		List<Map<String, Object>> dataList = null;

		int i = 1;
		try {
			if (params != null) {
				for (Object obj : params) {
					if (obj instanceof String) {
						ps.setString(i, obj.toString());
					} else if (obj instanceof Integer) {
						ps.setInt(i, Integer.parseInt(obj.toString()));
					} else if (obj instanceof Long) {
						ps.setLong(i, Long.valueOf(obj.toString()));
					} else {
						ps.setString(i, String.valueOf(obj));
					}
					i++;
				}
			}
			rs = ps.executeQuery();
			if (rs != null) {
				dataList = new ArrayList<Map<String, Object>>();
				while (rs.next()) {
					rsmd = rs.getMetaData();
					if (rsmd != null) {
						int cnt = rsmd.getColumnCount();
						@SuppressWarnings("unchecked")
						Map<String, Object> data = new CaseInsensitiveMap(); // 不區分大小寫

						for (int j = 1; j <= cnt; j++) {
							if (rs.getObject(j) == null) {
								data.put(rsmd.getColumnLabel(j).toUpperCase(), null);
							} else if (rs.getObject(j) instanceof String) {
								data.put(rsmd.getColumnLabel(j).toUpperCase(), String.valueOf(rs.getObject(j)));
							} else if (rs.getObject(j) instanceof Integer) {
								data.put(rsmd.getColumnLabel(j).toUpperCase(),
										Integer.parseInt(rs.getObject(j).toString()));
							} else if (rs.getObject(j) instanceof Long) {
								data.put(rsmd.getColumnLabel(j).toUpperCase(),
										Long.valueOf(rs.getObject(j).toString()));
							} else if (rs.getObject(j) instanceof java.sql.Date) {
								data.put(rsmd.getColumnLabel(j).toUpperCase(), new Date(rs.getTimestamp(j).getTime()));
							} else {
								data.put(rsmd.getColumnLabel(j).toUpperCase(), String.valueOf(rs.getObject(j)));
							}
						}
						dataList.add(data);
					}
				}
			}
		} finally {
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (b) {
				con.close();
				con = null;
			}
		}

		return dataList;
	}

	/**
	 * 執行 SQL stmt, 用於 Insert Delete Update
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int execUpdate(String sql, List<Object> params) throws Exception {
		boolean b = false;
		try {
			if (con == null || con.isClosed()) {
				con = this.getConnection();
				b = true;
			}
			int i = this.execUpdate(con, sql, params);
			return i;
		} finally {
			if (b) {
				con.close();
				con = null;
			}
		}
	}

	/**
	 * 傳入conn 執行 SQL stmt, 用於 Insert Delete Update
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int execUpdate(Connection conn, String sql, List<Object> params) throws Exception {
		ps = conn.prepareStatement(sql);
		ps.clearParameters();
		int i = 1;
		try {
			if (params != null) {
				for (Object obj : params) {
					if (obj == null) {
						ps.setNull(i, Types.NULL);
					} else if (obj instanceof String) {
						ps.setString(i, obj.toString());
					} else if (obj instanceof Integer) {
						ps.setInt(i, Integer.parseInt(String.valueOf(obj.toString())));
					} else if (obj instanceof Long) {
						ps.setLong(i, Long.valueOf(obj.toString()));
					} else if (obj instanceof Date) {
						ps.setTimestamp(i, new Timestamp(((Date) obj).getTime()));
					} else if (obj instanceof BLOB) {
						ps.setBlob(i, (Blob) obj);
					} else {
						ps.setObject(i, String.valueOf(obj));
					}
					i++;
				}
			}
			return ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
				ps = null;
			}
		}
	}

	/**
	 * 執行多筆SQL 同時commit
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int[] execMultiUpdate(List<String> sqlList, List<List<Object>> paramsList) throws Exception {
		boolean f = false;
		if (con == null || con.isClosed()) {
			con = this.getConnection();
			f = true;
		}
		boolean b = con.getAutoCommit();
		con.setAutoCommit(false);
		int[] ret = new int[sqlList.size()];
		try {
			for (int idx = 0; idx < sqlList.size(); idx++) {
				String sql = sqlList.get(idx);
				List<Object> params = paramsList.get(idx);
				ret[idx] = this.execUpdate(con, sql, params);
			}
			con.commit();
			con.setAutoCommit(b);
			return ret;
		} catch (Exception x) {
			x.printStackTrace();
			con.rollback();
			con.setAutoCommit(b);
			throw new Exception(x);
		} finally {
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (f) {
				con.close();
				con = null;
			}
		}
	}

}
