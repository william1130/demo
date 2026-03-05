
package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChesgDailyTotalsStore extends ProjStore {

	private static List fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("LOG_DATE", true));
		fieldInfos.add(new FieldInfo("MERCHANT_ID", true));
		fieldInfos.add(new FieldInfo("TERM_ID", true));
		fieldInfos.add(new FieldInfo("TOTAL_COUNT", false));
		fieldInfos.add(new FieldInfo("CHESG_COUNT", false));
	}

	/** Creates a new instance of ChesgDailyTotalsStore */
	public ChesgDailyTotalsStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "CHESG_DAIL_TOTALS";
	}

	/**
	 * 提供table欄位的相關資訊, 以便組出PreparedStatement所需的SQL command.
	 */
	public List getFieldInfos() {
		return fieldInfos;
	}

	protected void insert(Persistable persistable, PreparedStatement pstmt) throws SQLException {
		int i = 1;
		ChesgDailyTotals o = (ChesgDailyTotals) persistable;
		pstmt.setString(i++, o.getLogDate());
		pstmt.setString(i++, o.getMerchantId());
		pstmt.setString(i++, o.getTermId());

		if (o.getTotalCount() != null)
			pstmt.setInt(i++, o.getTotalCount());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getChesgCount() != null)
			pstmt.setInt(i++, o.getChesgCount());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
	}

	protected void update(Persistable persistable, PreparedStatement pstmt) throws SQLException {
		int i = 1;
		ChesgDailyTotals o = (ChesgDailyTotals) persistable;

		if (o.getTotalCount() != null)
			pstmt.setInt(i++, o.getTotalCount());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getChesgCount() != null)
			pstmt.setInt(i++, o.getChesgCount());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		pstmt.setString(i++, o.getLogDate());
		pstmt.setString(i++, o.getMerchantId());
		pstmt.setString(i++, o.getTermId());
	}

	protected void delete(Persistable persistable, PreparedStatement pstmt) throws SQLException {
		int i = 1;
		ChesgDailyTotals o = (ChesgDailyTotals) persistable;
		pstmt.setString(i++, o.getLogDate());
		pstmt.setString(i++, o.getMerchantId());
		pstmt.setString(i++, o.getTermId());
	}
}
