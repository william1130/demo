package proj.nccc.atsLog.batch.dao;

import java.util.List;
import java.util.Map;

public class AuthMonrpt0003Dao extends ProjDao {
	

	
	/**
	 * -- queryManCountByCardTypeName as open_a054_cursor
	 * @return : CARD_TYPE_NAME, MAN_COUNT
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryManCountByCardTypeName() throws Exception{
		
		String sql = "SELECT "
				+ "( "
				+ "  CASE WHEN A.CARD_TYPE = '9' THEN "
				+ "         '會員－非中心特店' "
				+ "       WHEN A.CARD_TYPE = '0' THEN "
				+ "         '會員－中心特店' "
				+ "       WHEN A.CARD_TYPE = 'A' THEN "
				+ "         nvl(B.card_type_desc,A.CARD_TYPE) "
				+ "       ELSE "
				+ "         '非會員－' || nvl(B.card_type_desc,A.CARD_TYPE) "
				+ "       END "
				+ ") as CARD_TYPE_NAME "
				+ ", sum(A.bf_man_count) AS MAN_COUNT "
				+ " FROM auth_monrpt_0003 A ,"
				+ " (SELECT Lookup_code as card_type, lookup_desc as  card_type_desc , list_seq as  card_type_seq from sys_para) B "
				+ " WHERE A.card_type = B.card_type(+) "
				+ " GROUP BY A.CARD_TYPE,B.card_type_desc, B.card_type_seq "
				+ " ORDER BY nvl(cast(B.card_type_seq as varchar(5)),A.CARD_TYPE)";
		return super.queryListMap(sql, null);
	}
	
	
	/**
	 * @return : UPDATE_ID, MAN_COUNT
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryManCount() throws Exception{
		
		String sql = "SELECT BF_UPDATE_ID AS UPDATE_ID, sum(BF_MAN_COUNT) as MAN_COUNT "
				+ " FROM auth_monrpt_0003"
				+ " GROUP  BY BF_UPDATE_ID "
				+ " ORDER  BY BF_UPDATE_ID";
		return super.queryListMap(sql, null);
	}

	
	
}
