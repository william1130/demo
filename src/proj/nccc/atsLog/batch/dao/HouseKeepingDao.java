package proj.nccc.atsLog.batch.dao;

import java.sql.ResultSet;
import java.util.List;

import proj.nccc.atsLog.batch.dao.entity.HouseKeeping;

public class HouseKeepingDao extends ProjDao {
	
	@Override
	public HouseKeeping buildObj(ResultSet resultSet) throws Exception {
		HouseKeeping o = new HouseKeeping();
		o.setItemCategory(resultSet.getString("ITEM_CATEGORY"));
		o.setTablePath(resultSet.getString("TABLE_PATH"));
		o.setFieldFile(resultSet.getString("FIELD_FILE"));
		o.setLogicalRule(resultSet.getString("LOGICAL_RULE"));
		o.setKeepDays(resultSet.getInt("KEEP_DAYS"));
		o.setStatus(resultSet.getString("STATUS"));
		return o;
	}
	
	
	public List<HouseKeeping> getHouseKeepingTable() throws Exception {
		
		String sql = "select * from SYS_HOUSEKEEPING "
				+ " where STATUS in ('Y','A') ";
		
		@SuppressWarnings("unchecked")
		List<HouseKeeping> list = (List<HouseKeeping>) queryList(sql, null);
		return list;
	}
}
