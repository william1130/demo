
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: AuthLogDataProcImpl.java,v 1.1 2017/04/24 01:31:18 asiapacific\jih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.proc;

import java.util.Map;

import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.service.ServiceException;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.AuthLogData;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author APAC\czrm4t
 * @version $Revision: 1.1 $
 */
public class AuthLogDataProcImpl extends ParaPairCRUDProc implements AuthLogDataProc {

	public String getServiceName() {
		return "LogData Process ";
	}

	@Override
	public void setServiceParams(Map arg0) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public EmvProjPersistable createEmptyCorrespondentPersistable() {
		return null;
	}

	@Override
	public BaseCRUDQS getCorrespondentCRUDQS() {
		return ProjServices.getAuthLogDataQS();
	}

	@Override
	public BaseCRUDQS getBaseCRUDQS() {
		return ProjServices.getAuthLogDataQS();
	}

	@Override
	public EmvProjPersistable createEmptyProjPersistable() {
		return new AuthLogData();
	}

	@Override
	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity) throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);

	}

}