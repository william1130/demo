package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

public class ParameterStore extends ProjStore {
	private static List<FieldInfo> fieldInfos;

	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList<FieldInfo>();
		fieldInfos.add(new FieldInfo("UUID", true));
		fieldInfos.add(new FieldInfo("LOOKUP_TYPE", false));
		fieldInfos.add(new FieldInfo("LOOKUP_CODE", false));
		fieldInfos.add(new FieldInfo("LOOKUP_DESC", false));
		fieldInfos.add(new FieldInfo("VALUE", false));
		fieldInfos.add(new FieldInfo("VALUE_EXT1", false));
		fieldInfos.add(new FieldInfo("VALUE_EXT2", false));
		fieldInfos.add(new FieldInfo("VALUE_EXT3", false));
		fieldInfos.add(new FieldInfo("STATUS", false));
		fieldInfos.add(new FieldInfo("LIST_SEQ", false));
		fieldInfos.add(new FieldInfo("CREATE_USER", false));
		fieldInfos.add(new FieldInfo("CREATE_DATE", false));
		fieldInfos.add(new FieldInfo("UPDATE_USER", false));
		fieldInfos.add(new FieldInfo("UPDATE_DATE", false));
	}

	/** Creates a new instance of ParameterStore */
	public ParameterStore() {
	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {
		return "SYS_PARA";
	}

	/**
	 * 提供table欄位的相關資訊, 以便組出PreparedStatement所需的SQL command.
	 */
	public List<FieldInfo> getFieldInfos() {
		return fieldInfos;
	}

	protected void insert(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		Parameter o = (Parameter) persistable;
		if(o.getUuid()==null){
			o.setUuid(UUID.randomUUID().toString());
		}
		pstmt.setString(i++, o.getUuid());
		pstmt.setString(i++, o.getType());
		pstmt.setString(i++, o.getCode());
		if ("SMS_LIST".equals(o.getType()) && o.getValue() != null
				&& o.getValue().length() > 1) {
			String s = o.getValue();
			if (s.length() >= 3)
				o.setValue(s.substring(0, 1) + "Ｏ" + s.substring(2));
			else if (s.length() == 2)
				o.setValue(s.substring(0, 1) + "Ｏ");
			pstmt.setString(i++, o.getCode()); // as Description
			pstmt.setString(i++, o.getValue());
		}else{
			pstmt.setString(i++, o.getDescription());
			pstmt.setString(i++, o.getValue());
		}
		if (o.getValueExt1() != null)
			pstmt.setString(i++, o.getValueExt1());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		if (o.getValueExt2() != null)
			pstmt.setString(i++, o.getValueExt2());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		if (o.getValueExt3() != null)
			pstmt.setString(i++, o.getValueExt3());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		if (o.getStatus() != null)
			pstmt.setString(i++, o.getStatus());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		if (o.getListSeq() != null)
			pstmt.setInt(i++, o.getListSeq().intValue());
		else
			pstmt.setNull(i++, Types.INTEGER);
		
		if (o.getCreateUser() != null)
			pstmt.setString(i++, o.getCreateUser());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCreateDate() != null)
			pstmt.setTimestamp(i++, new Timestamp(o.getCreateDate().getTime()));
		else
			pstmt.setNull(i++, java.sql.Types.TIMESTAMP);

		if (o.getUpdateUser() != null)
			pstmt.setString(i++, o.getUpdateUser());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getUpdateDate() != null)
			pstmt.setTimestamp(i++, new Timestamp(o.getUpdateDate().getTime()));
		else
			pstmt.setNull(i++, java.sql.Types.TIMESTAMP);
	}

	protected void update(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		Parameter o = (Parameter) persistable;
		
		pstmt.setString(i++, o.getType());
		pstmt.setString(i++, o.getCode());
		pstmt.setString(i++, o.getDescription());
		
		pstmt.setString(i++, o.getValue());
		
		if (o.getValueExt1() != null)
			pstmt.setString(i++, o.getValueExt1());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		if (o.getValueExt2() != null)
			pstmt.setString(i++, o.getValueExt2());
		else
			pstmt.setNull(i++, Types.VARCHAR);
		
		if( o.getValueExt3() != null )
			pstmt.setString( i++ , o.getValueExt3() );
		else
			pstmt.setNull( i++ , Types.VARCHAR );
		
		if( o.getStatus() != null )
			pstmt.setString( i++ , o.getStatus() );
		else
			pstmt.setNull( i++ , Types.VARCHAR );
		
		if (o.getListSeq() != null)
			pstmt.setInt(i++, o.getListSeq().intValue());
		else
			pstmt.setNull(i++, Types.INTEGER);

		if (o.getCreateUser() != null)
			pstmt.setString(i++, o.getCreateUser());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getCreateDate() != null)
			pstmt.setTimestamp(i++, new Timestamp(o.getCreateDate().getTime()));
		else
			pstmt.setNull(i++, java.sql.Types.TIMESTAMP);

		if (o.getUpdateUser() != null)
			pstmt.setString(i++, o.getUpdateUser());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getUpdateDate() != null)
			pstmt.setTimestamp(i++, new Timestamp(o.getUpdateDate().getTime()));
		else
			pstmt.setNull(i++, java.sql.Types.TIMESTAMP);
		
		pstmt.setString(i++, o.getUuid());
	}

	protected void delete(Persistable persistable, PreparedStatement pstmt)
			throws SQLException {
		int i = 1;
		Parameter o = (Parameter) persistable;
		pstmt.setString(i++, o.getUuid());
	}

}
