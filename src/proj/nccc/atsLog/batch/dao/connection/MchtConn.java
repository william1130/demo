package proj.nccc.atsLog.batch.dao.connection;

/**
 * 連接特店系統
 *
 */
public class MchtConn extends BaseConn
{
	@Override
	public String getDbProperties() {
		return "db_mcht.properties";
	}

}
