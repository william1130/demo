package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

public class EmvTagRecordMasterTempStore extends ProjStore {
	private static List fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("EMV_TAG", true));
		fieldInfos.add(new FieldInfo("EMV_NAME", false));
		fieldInfos.add(new FieldInfo("EMV_ABBR", false));
		fieldInfos.add(new FieldInfo("EMV_DESC", false));
		fieldInfos.add(new FieldInfo("EMV_LEN", false));
		fieldInfos.add(new FieldInfo("CARD_TYPE", false));;
		fieldInfos.add(new FieldInfo("SAME_VALUE_FLAG", true));
		fieldInfos.add(new FieldInfo("ACTIVE_CODE", false));
		fieldInfos.add(new FieldInfo("ORI_SAME_VALUE_FLAG", false));
		fieldInfos.add(new FieldInfo("REQUEST_USER", false));
		fieldInfos.add(new FieldInfo("REQUEST_DATE", false));
	}

	/** Creates a new instance of EventMasterStore */
	public EmvTagRecordMasterTempStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "EMV_TAG_RECORD_MASTER_TEMP";
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
		EmvTagRecordMasterTemp o = (EmvTagRecordMasterTemp) persistable;
		
		pstmt.setString(i++, o.getEmvTag());
		pstmt.setString(i++, o.getEmvName());
		pstmt.setString(i++, o.getEmvAbbr());
		
		if (o.getEmvDesc() != null)
			pstmt.setString(i++, o.getEmvDesc());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		pstmt.setInt(i++, o.getEmvLen());	
		pstmt.setString(i++, o.getCardType());
		pstmt.setString(i++, o.getSameValueFlag());
		
		if (o.getActiveCode() != null)
			pstmt.setString(i++, o.getActiveCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		
		if (o.getOriSameValueFlag() != null)
			pstmt.setString(i++, o.getOriSameValueFlag());
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
	}

	protected void update(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		
		EmvTagRecordMasterTemp o = (EmvTagRecordMasterTemp) persistable;
		
		pstmt.setString(i++, o.getEmvName());
		pstmt.setString(i++, o.getEmvAbbr());
		
		if (o.getEmvDesc() != null)
			pstmt.setString(i++, o.getEmvDesc());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		pstmt.setInt(i++, o.getEmvLen());
		pstmt.setString(i++, o.getCardType());

		if (o.getActiveCode() != null)
			pstmt.setString(i++, o.getActiveCode());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		
		if (o.getOriSameValueFlag() != null)
			pstmt.setString(i++, o.getOriSameValueFlag());
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
		
		pstmt.setString(i++, o.getEmvTag());
		pstmt.setString(i++, o.getSameValueFlag());

	}

	protected void delete(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvTagRecordMasterTemp o = (EmvTagRecordMasterTemp) persistable;

		pstmt.setString(i++, o.getEmvTag());
		pstmt.setString(i++, o.getSameValueFlag());
	}
}