package proj.nccc.atsLog.batch.dao.connection;

/**
 * 連接風控系統
 *
 */
public class AuthConn extends BaseConn {
	@Override
	public String getDbProperties() {
		return "db_auth.properties";
	}

}
