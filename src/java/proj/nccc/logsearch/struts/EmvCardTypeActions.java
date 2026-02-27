package proj.nccc.logsearch.struts;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.web.WebConstant;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.vo.EmvCardTypeArg;

public class EmvCardTypeActions extends BaseCRUDActions
{
	private static final long serialVersionUID = 1L;
	private EmvCardTypeArg entity;
	private final String DEL_NAME = "DELETE";
	private final String FUNC_NAME = "EMV_CARD_TYPE";
	protected BaseCRUDProc getBaseCRUDProc()
	{
		return ProjServices.getEmvCardTypeProc();
	}

	public EmvCardTypeArg getEntity() {
		return entity;
	}

	public void setEntity(EmvCardTypeArg entity) {
		this.entity = entity;
	}

	public EmvCardTypeActions() {
		this.setEntity(new EmvCardTypeArg());		
	}
	
	public String toModify()
	{
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try
		{
			EmvCardTypeArg arg = (EmvCardTypeArg) this.getEntity();
			EmvProjPersistable obj = getBaseCRUDProc().queryById(arg.getId());		
			arg.buildFromProjPersistable(obj);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}
	
	public String delete() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_D);
		try {
			EmvCardTypeArg entity = (EmvCardTypeArg) this.getEntity();
			entity.setUiLogAction(DEL_NAME);
			entity.setUiLogFunctionName(FUNC_NAME);
			entity.setUiLogOther("卡別["+entity.getCardType()+"]");
			ProjServices.getEmvCardTypeProc().deleteCard(entity);
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}
	

}
