package proj.nccc.atsLog.batch.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proj.nccc.atsLog.batch.dao.entity.SysEventMaster;


public class EventMasterDao extends ProjDao {
	
	@Override
	public SysEventMaster buildObj(ResultSet resultSet) throws Exception {
		SysEventMaster o = new SysEventMaster();
		o.setEventCode(rs.getString("EVENT_CODE"));
		o.setEventDesc(rs.getString("EVENT_DESC"));
		o.setEventLogicalRule(rs.getString("EVENT_LOGICAL_RULE"));
		o.setEventRuleValue(rs.getString("EVENT_RULE_VALUE"));
		o.setEventNotice(rs.getString("EVENT_NOTICE"));
		o.setNextEventTime(rs.getInt("NEXT_EVENT_TIME"));
		return o;
	}
	
	
	public Map<String, SysEventMaster> getSysEventMaster() throws Exception {
		String sql = "select * from SYS_EVENT_MASTER "
				+ " where STATUS in('Y','A') ";
		
		@SuppressWarnings("unchecked")
		List<SysEventMaster> list = (List<SysEventMaster>) queryList(sql, null);
		
		if (list == null || list.size() <= 0) {
			return null;
		}
		Map<String, SysEventMaster> map = new HashMap<String, SysEventMaster>();
		for(SysEventMaster o : list) {
			map.put(o.getEventCode(), o);
		}
		return map;
	}
}
