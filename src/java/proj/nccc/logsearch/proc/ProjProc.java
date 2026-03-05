/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/20, 下午 01:49:54, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjProc.java,v 1.2 2014/09/04 03:19:52 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.proc;

import java.util.Map;

import com.edstw.process.AbstractProcess;
import com.edstw.service.ServiceException;

/**
 * 一般作業的Proc類別, 若是要套用CRUD pattern, 請改繼承ProjEntityProc
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
public abstract class ProjProc extends AbstractProcess {

	public String getServiceName() {
		return this.getClass().getName();
	}

	public void setServiceParams(@SuppressWarnings("rawtypes") Map map) throws ServiceException {
		// do nothing
	}
}
