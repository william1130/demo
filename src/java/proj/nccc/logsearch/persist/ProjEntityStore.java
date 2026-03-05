/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/21, 下午 06:12:22, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjEntityStore.java,v 1.2 2014/09/04 03:19:54 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import com.edstw.nccc.sql.log.AbstractJdbcPersistableStore4AccLog;
import com.edstw.persist.Persistable;

/**
 * ProjEntity的Store類別, 注意ProjEntity的ID欄位需與本類別中之定義相同
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
public abstract class ProjEntityStore<E extends ProjEntity> extends
		AbstractJdbcPersistableStore4AccLog {
	public ProjEntityStore() {
		// 預設ID欄位名稱為id
		super.setGeneratedKeyDBField("entity_id");
		// 預設ID屬性名稱為id
		super.setGeneratedKeyProperty("entityId");
	}

	abstract int fillCommonField(int i, E obj, PreparedStatement pstmt)
			throws SQLException;

	@Override
	protected void insert(Persistable p, PreparedStatement pstmt)
			throws SQLException {
		@SuppressWarnings("unchecked")
		E obj = (E) p;
		int i = 1;
		pstmt.setNull(i++, Types.INTEGER);
		i = this.fillCommonField(i, obj, pstmt);

		AlterInfo ai = AlterInfo.createAlterInfo();
		obj.setCreateInfo(ai);
		obj.setUpdateInfo(ai);
		pstmt.setString(i++, ai.getName());
		pstmt.setTimestamp(i++, new Timestamp(ai.getDate().getTime()));
		pstmt.setString(i++, ai.getName());
		pstmt.setTimestamp(i++, new Timestamp(ai.getDate().getTime()));
	}

	@Override
	protected void update(Persistable p, PreparedStatement pstmt)
			throws SQLException {
		@SuppressWarnings("unchecked")
		E obj = (E) p;
		int i = 1;
		i = this.fillCommonField(i, obj, pstmt);
		AlterInfo ai = (AlterInfo) obj.getCreateInfo();
		pstmt.setString(i++, ai.getName());
		pstmt.setTimestamp(i++, new Timestamp(ai.getDate().getTime()));

		ai = AlterInfo.createAlterInfo();
		obj.setUpdateInfo(ai);
		pstmt.setString(i++, ai.getName());
		pstmt.setTimestamp(i++, new Timestamp(ai.getDate().getTime()));

		pstmt.setLong(i++, obj.getId().longValue());
	}

	@Override
	protected void delete(Persistable p, PreparedStatement pstmt)
			throws SQLException {
		@SuppressWarnings("unchecked")
		E obj = (E) p;
		int i = 1;
		pstmt.setLong(i++, obj.getId().longValue());
	}

	@Override
	protected String getInsertCmd(Persistable prstbl) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected String getUpdateCmd(Persistable prstbl) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected String getDeleteCmd(Persistable prstbl) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
