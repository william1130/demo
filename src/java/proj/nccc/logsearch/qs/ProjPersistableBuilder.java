/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/8/3, 下午 04:15:36, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjPersistableBuilder.java,v 1.2 2014/09/04 03:19:53 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.ProjPersistable;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.process.ProcessException;
import com.edstw.sql.ResultSetTool;
import com.edstw.user.UserLogger;

/**
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
public abstract class ProjPersistableBuilder<P extends ProjPersistable> extends
		AbstractJdbcPersistableBuilder {

	public P createEmptyPersistable() {
		@SuppressWarnings("unchecked")
		Class<P> cls = (Class<P>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		try {
			return cls.newInstance();
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}
	}

	@Override
	public P build(ResultSet rs) throws SQLException {
		P entity = this.createEmptyPersistable();
		ResultSetTool rst = new ResultSetTool(rs);
		this.buildPersistable(entity, rst);
		// entity.setCreateInfo( AlterInfo.buildCreateInfo( rst ) );
		// entity.setUpdateInfo( AlterInfo.buildUpdateInfo( rst ) );
		return entity;
	}

	abstract protected void buildPersistable(P entity, ResultSetTool rst)
			throws SQLException;
}
