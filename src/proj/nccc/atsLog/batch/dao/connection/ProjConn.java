package proj.nccc.atsLog.batch.dao.connection;

/**
 * connect proj db
 *
 */
public class ProjConn extends BaseConn {
	@Override
	public String getDbProperties() {
		return "db_proj.properties";
	}

}
