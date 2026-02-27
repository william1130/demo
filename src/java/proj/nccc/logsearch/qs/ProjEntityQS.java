package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;

import proj.nccc.logsearch.persist.ProjEntity;
import proj.nccc.logsearch.vo.ProjEntityArg;
import com.edstw.crud.sql.BaseCRUDQS;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.SqlUtil;

public abstract class ProjEntityQS<E extends ProjEntity, A extends ProjEntityArg>
		extends ProjQS implements BaseCRUDQS {

	abstract public String getTableName();

	@SuppressWarnings("rawtypes")
	abstract public ProjEntityBuilder getEntityBuilder();

	@SuppressWarnings("unchecked")
	public E queryById(Object o) throws QueryServiceException {
		StringBuilder cmd = new StringBuilder("SELECT * FROM ").append(
				this.getTableName()).append(" WHERE entity_id=?");
		List<Object> params = new LinkedList<Object>();
		params.add(o);
		return (E) super
				.queryObject(cmd.toString(), params, getEntityBuilder());
	}

	@SuppressWarnings("unchecked")
	public List<E> queryByIds(@SuppressWarnings("rawtypes") List list) throws QueryServiceException {
		StringBuilder cmd = new StringBuilder("SELECT * FROM ")
				.append(this.getTableName()).append(" WHERE ")
				.append(SqlUtil.createInStatement("entity_id", list));
		return super.queryObjectList(cmd.toString(), getEntityBuilder());
	}

	public List<?> queryAll() throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
