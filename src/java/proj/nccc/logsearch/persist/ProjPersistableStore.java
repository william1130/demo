/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/17, 下午 05:21:54, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjPersistableStore.java,v 1.1 2014/01/20 08:39:15 asiapacific\hungmike Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.AbstractJdbcPersistableStore4AccLog;
import com.edstw.persist.Persistable;

/**
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.1 $
 */
public abstract class ProjPersistableStore extends
		AbstractJdbcPersistableStore4AccLog {

	protected String getInsertCmd(Persistable prstbl) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected String getUpdateCmd(Persistable prstbl) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected String getDeleteCmd(Persistable prstbl) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
