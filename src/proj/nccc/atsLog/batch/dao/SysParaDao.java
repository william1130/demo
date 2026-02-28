package proj.nccc.atsLog.batch.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import proj.nccc.atsLog.batch.dao.entity.SysPara;

public class SysParaDao extends ProjDao {
	
	@Override
	protected SysPara buildObj(ResultSet rs) throws Exception {
		SysPara o = new SysPara();
		o.setType(rs.getString("LOOKUP_TYPE"));
		o.setCode(rs.getString("LOOKUP_CODE"));
		o.setDescription(rs.getString("LOOKUP_DESC"));
		o.setValue(rs.getString("VALUE"));
		o.setValueExt1(rs.getString("VALUE_EXT1"));
		o.setValueExt2(rs.getString("VALUE_EXT2"));
		o.setValueExt3(rs.getString("VALUE_EXT3"));
		return o;
	}

	/**
	 * -  取得參數表
	 * @param lookupType
	 * @return
	 * @throws Exception 
	 */
	public Map<String, SysPara> getSysParaMap(String lookupType) throws Exception {
		String sql = "select * from SYS_PARA "
				+ " where LOOKUP_TYPE = ? "
				+ " and STATUS in ('Y','A') ";
		List<String> param = new LinkedList<String>();
		param.add(lookupType);
		
		@SuppressWarnings("unchecked")
		List<SysPara> list = (List<SysPara>) queryList(sql, param);
		
		if (list == null || list.size() <= 0) {
			return null;
		}
		Map<String, SysPara> map = new HashMap<String, SysPara>();
		for(SysPara o : list) {
			map.put(o.getCode(), o);
		}
		return map;
		
	}
	
	/**
	 * - 取得非服務時間
	 * @return
	 * @throws Exception
	 */
	public SysPara queryUnserviceTime() throws Exception {
		
		Map<String, SysPara> map = this.getSysParaMap("SYS");
		return (SysPara) MapUtils.getObject(map, "UNSERVICE");
	}
	
	/**
	 * - 取得系統參數
	 * @param lookupType
	 * @param lookupCode
	 * @return
	 * @throws Exception 
	 */
	public SysPara getSysParameters(String lookupType, String lookupCode) throws Exception {
		Map<String, SysPara> map = this.getSysParaMap(lookupType);
		return (SysPara) MapUtils.getObject(map, lookupCode);
	}
}
