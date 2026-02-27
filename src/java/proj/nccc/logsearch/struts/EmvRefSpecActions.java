package proj.nccc.logsearch.struts;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.web.WebConstant;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.vo.EmvRefSpecArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

public class EmvRefSpecActions extends BaseCRUDActions
{

	private static final long serialVersionUID = 1L;
	private EmvRefSpecArg entity;
	private final String DEL_NAME = "DELETE";
	private final String FUNC_NAME = "EMV_SPEC";
	protected BaseCRUDProc getBaseCRUDProc()
	{
		return ProjServices.getEmvRefSpecProc();
	}

	public EmvRefSpecArg getEntity() {
		return entity;
	}

	public void setEntity(EmvRefSpecArg entity) {
		this.entity = entity;
	}

	public EmvRefSpecActions() {
		this.setEntity(new EmvRefSpecArg());		
	}
	
	public String toModify()
	{
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try
		{
			EmvRefSpecArg arg = (EmvRefSpecArg) this.getEntity();
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
			EmvRefSpecArg entity = (EmvRefSpecArg) this.getEntity();
			entity.setUiLogAction(DEL_NAME);
			entity.setUiLogFunctionName(FUNC_NAME);
			entity.setUiLogOther("參考規格["+entity.getSpecName()+"]");
			ProjServices.getEmvRefSpecProc().deleteRefSpec(entity);
			
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}
	
	@Override
	public String create() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_A);
		try {
			EmvRefSpecArg entity = (EmvRefSpecArg) this.getEntity();
			Date today = new Date();
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
			String specID = f.format(today);
			entity.setSpecID(specID);
			getBaseCRUDProc().create((ProjPersistableArg) entity);
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}
	
}
