package proj.nccc.logsearch.proc;

import java.util.Map;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvCardType;
import proj.nccc.logsearch.persist.EmvCardTypeTemp;
import proj.nccc.logsearch.persist.EmvProjPersistable;
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
public class EmvCardTypeTempProcImpl extends ParaPairCRUDProc implements
EmvCardTypeTempProc {

/**
 * Creates a new instance of UplinkParaTempProcImpl
 */
public EmvCardTypeTempProcImpl() {
}

public BaseCRUDQS getBaseCRUDQS() {
	return ProjServices.getEmvCardTypeTempQS();
}

public EmvProjPersistable createEmptyProjPersistable() {
	return new EmvCardTypeTemp();
}

protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
		throws Exception {
	StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
}

public String getServiceName() {
	return "EmvCardTypeTemp Process";
}

public void setServiceParams(Map map) throws ServiceException {
	// do nothing
}

public EmvProjPersistable createEmptyCorrespondentPersistable() {
	return new EmvCardType();
}

public BaseCRUDQS getCorrespondentCRUDQS() {
	return ProjServices.getEmvCardTypeQS();
}	

public void beforePersistOnApprove(ApproveParaArg approveParaArg, EmvProjPersistable paraObj) {
	EmvCardType para = (EmvCardType) paraObj;
	para.setStatus(ProjConstants.Para_Status.ON.getCode());		
}

protected void beforePersistOnReject(EmvProjPersistable paraObj) throws Exception {
	EmvCardType para = (EmvCardType) paraObj;		
	
	para.setStatus(ProjConstants.Para_Status.ON.getCode());		
}

}
