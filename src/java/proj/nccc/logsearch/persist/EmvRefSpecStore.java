package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

public class EmvRefSpecStore extends ProjStore {
	private static List fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("SPEC_ID", true));
		fieldInfos.add(new FieldInfo("SPEC_NAME", false));
		fieldInfos.add(new FieldInfo("STATUS", false));
		fieldInfos.add(new FieldInfo("CREATE_USER", false));
		fieldInfos.add(new FieldInfo("CREATE_DATE", false));
		fieldInfos.add(new FieldInfo("LAST_UPDATE_USER", false));
		fieldInfos.add(new FieldInfo("LAST_UPDATE_DATE", false));
	}

	/** Creates a new instance of EventMasterStore */
	public EmvRefSpecStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "EMV_REF_SPEC";
	}

	/**
	 * 提供table欄位的相關資訊, 以便組出PreparedStatement所需的SQL command.
	 */
	public List getFieldInfos() {
		return fieldInfos;
	}

	protected void insert(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvRefSpec o = (EmvRefSpec) persistable;
		pstmt.setString(i++, o.getSpecID());
		pstmt.setString(i++, o.getSpecName());
		
		if (o.getStatus() != null)
			pstmt.setString(i++, o.getStatus());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		pstmt.setString( i++ , o.getCreateInfo().getName() );
		pstmt.setTimestamp( i++ , new Timestamp(o.getCreateInfo().getDate().getTime()) );

		if (o.getUpdateInfo() != null && o.getUpdateInfo().getName() != null) { 
			pstmt.setString( i++ , o.getUpdateInfo().getName() );
		} else {
			pstmt.setNull( i++ , Types.VARCHAR );
		}
		
		if (o.getUpdateInfo() != null && o.getUpdateInfo().getDate() != null) {
			pstmt.setTimestamp( i++ , new Timestamp(o.getUpdateInfo().getDate().getTime()) );
		} else {
			pstmt.setNull( i++ , Types.TIMESTAMP );
		}

	}

	protected void update(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvRefSpec o = (EmvRefSpec) persistable;
		
		if (o.getSpecName() != null)
			pstmt.setString(i++, o.getSpecName());
		else
			pstmt.setNull(i++, Types.VARCHAR);

		if (o.getStatus() != null)
			pstmt.setString(i++, o.getStatus());
		else
			pstmt.setNull(i++, Types.VARCHAR);

		if (o.getCreateInfo() != null && o.getCreateInfo().getName() != null) { 
			pstmt.setString( i++ , o.getCreateInfo().getName() );
		} else {
			pstmt.setNull( i++ , Types.VARCHAR );
		}
		
		if (o.getCreateInfo() != null && o.getCreateInfo().getDate() != null) {
			pstmt.setTimestamp( i++ , new Timestamp(o.getCreateInfo().getDate().getTime()) );
		} else {
			pstmt.setNull( i++ , Types.TIMESTAMP );
		}
		
		pstmt.setString( i++ , o.getUpdateInfo().getName() );
		pstmt.setTimestamp( i++ , new Timestamp(o.getUpdateInfo().getDate().getTime()) );
		
		pstmt.setString(i++, o.getSpecID());
	}

	protected void delete(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvRefSpec o = (EmvRefSpec) persistable;
		pstmt.setString(i++, o.getSpecID());
	}
}