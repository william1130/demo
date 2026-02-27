/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/21, 下午 06:21:44, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjEntityBuilder.java,v 1.2 2014/09/04 03:19:53 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;

import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.ProjEntity;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.process.ProcessException;
import com.edstw.sql.ResultSetTool;
import com.edstw.user.UserLogger;

/**
 * ProjEntity物件的Builder類別
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
public abstract class ProjEntityBuilder<E extends ProjEntity> extends
		AbstractJdbcPersistableBuilder {

	public E createEmptyEntity() {
		@SuppressWarnings("unchecked")
		Class<E> cls = (Class<E>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		try {
			return cls.newInstance();
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}
	}

	@Override
	public E build(ResultSet rs) throws SQLException {
		E entity = this.createEmptyEntity();
		ResultSetTool rst = new ResultSetTool(rs);
		entity.setId(rst.getLongObject("entity_id"));
		this.buildEntity(entity, rst);
		entity.setCreateInfo(AlterInfo.buildCreateInfo(rst));
		entity.setUpdateInfo(AlterInfo.buildUpdateInfo(rst));
		return entity;
	}

	abstract protected void buildEntity(E entity, ResultSetTool rst)
			throws SQLException;
}
