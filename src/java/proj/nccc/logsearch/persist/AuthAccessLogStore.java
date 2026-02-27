/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id:
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

/**
 * 
 * @author APAC\xz04wy
 * @version $Revision: 1.2 $
 */
public class AuthAccessLogStore extends ProjStore {

	private static List<FieldInfo> fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("BANK_NO", false));
		fieldInfos.add(new FieldInfo("CARD_DATA", false));
		fieldInfos.add(new FieldInfo("UPDATE_DEPT", false));
		fieldInfos.add(new FieldInfo("UPDATE_ID", false));
		fieldInfos.add(new FieldInfo("MOD_PROGRAM_ID", false));
		fieldInfos.add(new FieldInfo("PROCESS_DATE", false));
		fieldInfos.add(new FieldInfo("PROCESS_TIME", false));
		fieldInfos.add(new FieldInfo("ORG_ID", false));
		fieldInfos.add(new FieldInfo("TEAM_ID", false));
	}

	/** Creates a new instance of AuthAccessLogStore */
	public AuthAccessLogStore() {

	}

	public String getTableName() {

		return "AUTH_ACCESS_LOG";
	}

	public List getFieldInfos() {

		return fieldInfos;
	}

	protected void insert(Persistable persistable, PreparedStatement pstmt) throws SQLException {

		int i = 1;
		AuthAccessLog o = (AuthAccessLog) persistable;
		if (o.getBankNo() != null)
			pstmt.setString(i++, o.getBankNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCardData() != null)
			pstmt.setString(i++, o.getCardData());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getUpdateDept() != null)
			pstmt.setString(i++, o.getUpdateDept());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getUpdateId() != null)
			pstmt.setString(i++, o.getUpdateId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getModProgramId() != null)
			pstmt.setString(i++, o.getModProgramId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getProcessDate() != null)
			pstmt.setString(i++, o.getProcessDate());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getProcessTime() != null)
			pstmt.setString(i++, o.getProcessTime());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getOrgId() != null)
			pstmt.setString(i++, o.getOrgId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getTeamId() != null)
			pstmt.setString(i++, o.getTeamId());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
	}

	@Override
	protected void delete(Persistable arg0, PreparedStatement arg1) throws SQLException {

		// TODO Auto-generated method stub
	}

	@Override
	protected void update(Persistable arg0, PreparedStatement arg1) throws SQLException {

		// TODO Auto-generated method stub
	}
}
