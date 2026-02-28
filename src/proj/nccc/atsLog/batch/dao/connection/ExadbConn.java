package proj.nccc.atsLog.batch.dao.connection;

/**
 * 連接系統
 *
 */
public class ExadbConn extends BaseConn {
	@Override
	public String getDbProperties() {
		return "db_exadb.properties";
	}

}
