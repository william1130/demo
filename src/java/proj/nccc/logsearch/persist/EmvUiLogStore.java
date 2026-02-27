package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

public class EmvUiLogStore extends ProjStore {
	private static List fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("UI_DATE", true));
		fieldInfos.add(new FieldInfo("USER_ID", true));
		fieldInfos.add(new FieldInfo("UUID", true));
		fieldInfos.add(new FieldInfo("UI_FUNCTION", false));
		fieldInfos.add(new FieldInfo("UI_OTHER", false));
	}

	/** Creates a new instance of UiLogStore */
	public EmvUiLogStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "EMV_UI_LOG";
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
		EmvUiLog o = (EmvUiLog) persistable;
		pstmt.setTimestamp(i++, new Timestamp(o.getUiDate().getTime()));
		pstmt.setString(i++, o.getUserId());
		if(o.getUuid()==null)
			o.setUuid(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
		pstmt.setString(i++, o.getUuid());
		if (o.getUiFunction() != null)
			pstmt.setString(i++, o.getUiFunction());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getUiOther() != null)
			pstmt.setString(i++, o.getUiOther());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

	}

	protected void update(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvUiLog o = (EmvUiLog) persistable;
		if (o.getUiFunction() != null)
			pstmt.setString(i++, o.getUiFunction());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		
		if (o.getUiOther() != null)
			pstmt.setString(i++, o.getUiOther());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		pstmt.setTimestamp(i++, new Timestamp(o.getUiDate().getTime()));
		pstmt.setString(i++, o.getUserId());
		pstmt.setString(i++, o.getUuid());
	}

	protected void delete(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvUiLog o = (EmvUiLog) persistable;
		pstmt.setTimestamp(i++, new Timestamp(o.getUiDate().getTime()));
		pstmt.setString(i++, o.getUserId());
		pstmt.setString(i++, o.getUuid());
	}

}
