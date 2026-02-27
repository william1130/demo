package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

public class EmvTagRecordMasterStore extends ProjStore {
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
		fieldInfos.add(new FieldInfo("STATUS", false));
		fieldInfos.add(new FieldInfo("CREATE_USER", false));
		fieldInfos.add(new FieldInfo("CREATE_DATE", false));
		fieldInfos.add(new FieldInfo("LAST_UPDATE_USER", false));
		fieldInfos.add(new FieldInfo("LAST_UPDATE_DATE", false));
	}

	/** Creates a new instance of EventMasterStore */
	public EmvTagRecordMasterStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "EMV_TAG_RECORD_MASTER";
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
		EmvTagRecordMaster o = (EmvTagRecordMaster) persistable;
		
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
		
		EmvTagRecordMaster o = (EmvTagRecordMaster) persistable;
		
		pstmt.setString(i++, o.getEmvName());
		pstmt.setString(i++, o.getEmvAbbr());
		
		if (o.getEmvDesc() != null)
			pstmt.setString(i++, o.getEmvDesc());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		pstmt.setInt(i++, o.getEmvLen());
		pstmt.setString(i++, o.getCardType());

		if (o.getStatus() != null)
			pstmt.setString(i++, o.getStatus());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);


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
		
		pstmt.setString(i++, o.getEmvTag());
		pstmt.setString(i++, o.getSameValueFlag());

	}

	protected void delete(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvTagRecordMaster o = (EmvTagRecordMaster) persistable;

		pstmt.setString(i++, o.getEmvTag());
		pstmt.setString(i++, o.getSameValueFlag());
	}
}