package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

public class ApproveLogStore extends ProjStore {
	private static List fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("APPROVE_ITEM", false));
		fieldInfos.add(new FieldInfo("APPROVE_DATE", false));
		fieldInfos.add(new FieldInfo("APPROVE_RESULT", false));
		fieldInfos.add(new FieldInfo("APPROVE_USER", false));
	}

	/** Creates a new instance of ApproveLogStore */
	public ApproveLogStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "SPSW_APPROVE_LOG";
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
		ApproveLog o = (ApproveLog) persistable;
		if (o.getApproveItem() != null)
			pstmt.setString(i++, o.getApproveItem());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getApproveDate() != null)
			pstmt.setTimestamp(i++, new Timestamp(o.getApproveDate().getTime()));
		else
			pstmt.setNull(i++, java.sql.Types.DATE);

		if (o.getApproveResult() != null)
			pstmt.setString(i++, o.getApproveResult());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getApproveUser() != null)
			pstmt.setString(i++, o.getApproveUser());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

	}

	protected void update(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		ApproveLog o = (ApproveLog) persistable;
		if (o.getApproveItem() != null)
			pstmt.setString(i++, o.getApproveItem());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getApproveDate() != null)
			pstmt.setTimestamp(i++, new Timestamp(o.getApproveDate().getTime()));
		else
			pstmt.setNull(i++, java.sql.Types.DATE);

		if (o.getApproveResult() != null)
			pstmt.setString(i++, o.getApproveResult());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getApproveUser() != null)
			pstmt.setString(i++, o.getApproveUser());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

	}

	protected void delete(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		ApproveLog o = (ApproveLog) persistable;
	}

}
