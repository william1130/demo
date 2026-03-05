package proj.nccc.logsearch.proc;

import java.util.Map;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvRefSpec;
import proj.nccc.logsearch.persist.EmvRefSpecTemp;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.vo.ApproveParaArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.process.ProcessException;
import com.edstw.service.ServiceException;

/**
 *
 * @author
 * @version
 * */
public class EmvRefSpecTempProcImpl extends ParaPairCRUDProc implements
EmvRefSpecTempProc {

/**
 * Creates a new instance of UplinkParaTempProcImpl
 */
public EmvRefSpecTempProcImpl() {
}

public BaseCRUDQS getBaseCRUDQS() {
	return ProjServices.getEmvRefSpecTempQS();
}

public EmvProjPersistable createEmptyProjPersistable() {
	return new EmvRefSpecTemp();
}

protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
		throws Exception {
	StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
}

public String getServiceName() {
	return "EmvRefSpecTemp Process";
}

public void setServiceParams(Map map) throws ServiceException {
	// do nothing
}

public EmvProjPersistable createEmptyCorrespondentPersistable() {
	return new EmvRefSpec();
}

public BaseCRUDQS getCorrespondentCRUDQS() {
	return ProjServices.getEmvRefSpecQS();
}	

public void beforePersistOnApprove(ApproveParaArg approveParaArg, EmvProjPersistable paraObj) {
	EmvRefSpec para = (EmvRefSpec) paraObj;
	para.setStatus(ProjConstants.Para_Status.ON.getCode());		
}

protected void beforePersistOnReject(EmvProjPersistable paraObj) throws Exception {
	EmvRefSpec para = (EmvRefSpec) paraObj;		
	
	para.setStatus(ProjConstants.Para_Status.ON.getCode());		
}

}
