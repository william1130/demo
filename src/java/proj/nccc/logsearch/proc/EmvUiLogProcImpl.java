package proj.nccc.logsearch.proc;

import java.util.Map;
import java.util.UUID;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvUiLog;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.user.ProjUserInfo;
import proj.nccc.logsearch.user.ProjUserProfile;
import proj.nccc.logsearch.vo.ProjPersistableArg;
import proj.nccc.logsearch.vo.EmvUiLogArg;

import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.service.ServiceException;

public class EmvUiLogProcImpl extends AbstractBaseCRUDProc implements EmvUiLogProc {
	public BaseCRUDQS getBaseCRUDQS() {
		return ProjServices.getEmvUiLogQS();		
	}

	public EmvProjPersistable createEmptyProjPersistable() {
		return new EmvUiLog();
	}

	public String getServiceName() {
		return "UiLog Process";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	@Override
	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity)
			throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState( entity, arg);
	}

	@Override
	public void createUiLog(String function) {		
		AlterInfo createInfo = AlterInfo.createAlterInfo();
		EmvUiLogArg uiLogArg = new EmvUiLogArg();
		
		uiLogArg.setUiDate(createInfo.getDate());
		ProjUserInfo ui = (ProjUserInfo) ProjUserProfile.getCurrentUserProfile().getUserInfo();
		uiLogArg.setUserId(ui.getUserID());
		uiLogArg.setUuid(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
		uiLogArg.setUiFunction(function);
		
		create(uiLogArg);	
	}		
	
}
