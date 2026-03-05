/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/17, 下午 05:21:28, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjPersistable.java,v 1.2 2014/09/04 03:19:53 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import com.dxc.nccc.aplog.edstw.persist.AbstractPersistable4ApLog;

/**
 * 對應資料庫table的persistable類別, 若是要套用CRUD pattern, 請改繼承ProjEntity
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
public abstract class ProjPersistable extends AbstractPersistable4ApLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3189303557046606025L;

}
