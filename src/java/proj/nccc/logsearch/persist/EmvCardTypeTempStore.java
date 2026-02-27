package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

public class EmvCardTypeTempStore extends ProjStore {
	private static List fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("CARD_TYPE", true));
		fieldInfos.add(new FieldInfo("CARD_TYPE_NAME", false));
		fieldInfos.add(new FieldInfo("ACTIVE_CODE", false));
		fieldInfos.add(new FieldInfo("REQUEST_USER", false));
		fieldInfos.add(new FieldInfo("REQUEST_DATE", false));
	}

	/** Creates a new instance of EventMasterTempStore */
	public EmvCardTypeTempStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "EMV_CARD_TYPE_TEMP";
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
		EmvCardTypeTemp o = (EmvCardTypeTemp) persistable;
		
		pstmt.setString(i++, o.getCardType());
		
		if (o.getCardTypeName() != null)
			pstmt.setString(i++, o.getCardTypeName());
		else
			pstmt.setNull(i++, Types.VARCHAR);
			
		if (o.getActiveCode() != null)
			pstmt.setString(i++, o.getActiveCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		
		pstmt.setString( i++ , o.getRequestInfo().getName() );
		pstmt.setTimestamp( i++ , new Timestamp(o.getRequestInfo().getDate().getTime()) );

	}

	protected void update(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvCardTypeTemp o = (EmvCardTypeTemp) persistable;
		
		if (o.getCardTypeName() != null)
			pstmt.setString(i++, o.getCardTypeName());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		if (o.getActiveCode() != null)
			pstmt.setString(i++, o.getActiveCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		
		if (o.getRequestInfo() != null && o.getRequestInfo().getName() != null) { 
			pstmt.setString( i++ , o.getRequestInfo().getName() );
		} else {
			pstmt.setNull( i++ , Types.VARCHAR );
		}
		
		if (o.getRequestInfo() != null && o.getRequestInfo().getDate() != null) {
			pstmt.setTimestamp( i++ , new Timestamp(o.getRequestInfo().getDate().getTime()) );
		} else {
			pstmt.setNull( i++ , Types.TIMESTAMP );
		}
		
		pstmt.setString(i++, o.getCardType());
	}

	protected void delete(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvCardTypeTemp o = (EmvCardTypeTemp) persistable;
		pstmt.setString(i++, o.getCardType());
	}

}
