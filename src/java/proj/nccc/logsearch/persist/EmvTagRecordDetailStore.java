package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

public class EmvTagRecordDetailStore extends ProjStore {
	private static List fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("EMV_TAG", true));
		fieldInfos.add(new FieldInfo("CARD_TYPE", false));;
		fieldInfos.add(new FieldInfo("POS_BYTE", true));
		fieldInfos.add(new FieldInfo("VALUE_BYTE", false));
		fieldInfos.add(new FieldInfo("POS_BIT", true));
		fieldInfos.add(new FieldInfo("VALUE_BIT", false));
		fieldInfos.add(new FieldInfo("SAME_VALUE_FLAG", true));
	}

	/** Creates a new instance of EventMasterStore */
	public EmvTagRecordDetailStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "EMV_TAG_RECORD_DETAIL";
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
		EmvTagRecordDetail o = (EmvTagRecordDetail) persistable;
		
		pstmt.setString(i++, o.getEmvTag());
		pstmt.setString(i++, o.getCardType());
		pstmt.setInt(i++, o.getPosByte());
		
		if (o.getValueByte() != null){
			pstmt.setString(i++, o.getValueByte());
		}else{
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		}
		
		pstmt.setInt(i++, o.getPosBit());
		
		if (o.getValueBit() != null){
			pstmt.setString(i++, o.getValueBit());
		}else{
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		}
	
		pstmt.setString(i++, o.getSameValueFlag());

		
	}

	protected void update(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		
		EmvTagRecordDetail o = (EmvTagRecordDetail) persistable;
		
		pstmt.setString(i++, o.getCardType());
		
		if (o.getValueByte() != null){
			pstmt.setString(i++, o.getValueByte());
		}else{
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		}
		
		if (o.getValueBit() != null){
			pstmt.setString(i++, o.getValueBit());
		}else{
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		}
		
		pstmt.setString(i++, o.getEmvTag());
		pstmt.setInt(i++, o.getPosByte());
		pstmt.setInt(i++, o.getPosBit());
		pstmt.setString(i++, o.getSameValueFlag());

	}

	protected void delete(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		EmvTagRecordDetail o = (EmvTagRecordDetail) persistable;

		pstmt.setString(i++, o.getEmvTag());
		pstmt.setInt(i++, o.getPosByte());
		pstmt.setInt(i++, o.getPosBit());
		pstmt.setString(i++, o.getSameValueFlag());
	}
}