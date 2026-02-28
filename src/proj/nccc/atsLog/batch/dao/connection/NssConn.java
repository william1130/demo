package proj.nccc.atsLog.batch.dao.connection;

public class NssConn extends BaseConn {
	@Override
	public String getDbProperties() {
		return "db_nss.properties";
	}

}
