/*
 * BaseCRUDQS.java
 *
 * Created on 2007年8月20日, 上午 11:58
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.qs;

import com.edstw.persist.jdbc.JdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;

import java.util.List;

import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.ProjPersistableArg;


/**
 * 系統中針對基本CRUD的查詢介面
 * @author 許欽程(Vincent Shiu)
 * @version $Revision$
 */
public interface BaseCRUDQS extends JdbcPersistableQueryService
{
	
	//public ProjPersistable queryByPrimaryKey( Object id ) throws QueryServiceException;
	
	//public List queryByPrimaryKeys( List ids ) throws QueryServiceException;
	
	EmvProjPersistable queryById( Object id ) throws QueryServiceException;
	
	List queryByIds( List ids ) throws QueryServiceException;
	
	List queryByExample( ProjPersistableArg example ) throws QueryServiceException;
	
	List queryAll() throws QueryServiceException;
	
}
