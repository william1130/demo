package proj.nccc.logsearch.struts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.user.UserLogger;
import com.edstw.web.WebConstant;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordDetail;
import proj.nccc.logsearch.persist.EmvTagRecordDetailTemp;
import proj.nccc.logsearch.persist.EmvTagRecordMaster;
import proj.nccc.logsearch.persist.EmvTagRecordMasterTemp;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.proc.EmvTagRecordMasterTempProcImpl;
import proj.nccc.logsearch.vo.EmvTagRecordMasterTempArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

public class EmvTagRecordTempActions extends BaseCRUDActions
{

	private static final long serialVersionUID = 1L;
	private List<?> resultTempList = null;
	private EmvTagRecordMaster masterData = null;
	private List<?> modDetailList = null;
	private List<?> oriDetailList = null;
	private EmvTagRecordMasterTempArg entity;
	private static final String RESULT_LIST_SESSION_NAME = EmvTagRecordTempActions.class.toString() + ".EMV_TAG_TEMP_RESULT_LIST";
	
	
	protected BaseCRUDProc getBaseCRUDProc()
	{
		return ProjServices.getEmvTagRecordMasterTempProc();
	}
	protected BaseCRUDProc getBaseCRUDProc(String type)
	{
		return ProjServices.getEmvTagRecordDetailTempProc();
	}
	
	public EmvTagRecordMasterTempArg getEntity() {
		return entity;
	}

	public void setEntity(EmvTagRecordMasterTempArg entity) {
		this.entity = entity;
	}

	public EmvTagRecordTempActions() {
		this.setEntity(new EmvTagRecordMasterTempArg());		
	}

	public String modDetail()
	{
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try
		{
			ProjPersistableArg arg = (ProjPersistableArg) this.getEntity();
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

	@Override
	public String approve()
	{
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_C);
		try
		{
			getBaseCRUDProc().approve((ProjPersistableArg) this.getEntity());
			super.saveMessage("msg.approved", null);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	@Override
	public String reject()
	{
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_C);
		try
		{
			getBaseCRUDProc().reject((ProjPersistableArg) this.getEntity());
			super.saveMessage("msg.rejected", null);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	/**
	 * @return the resultTempList
	 */
	public List getResultTempList()
	{
		if (resultTempList == null && (getResultList() != null || this.getEntity() != null))
		{
			List l = new ArrayList();
			for (Iterator itr = getResultList().iterator(); itr.hasNext(); )
			{
				EmvTagRecordMasterTemp emvTagRecordTemp = (EmvTagRecordMasterTemp) itr.next();
				String emvTag = emvTagRecordTemp.getEmvTag();
				String sameValueFlag = emvTagRecordTemp.getOriSameValueFlag();
				EmvTagRecordMaster emvTagMaster = (EmvTagRecordMaster) ProjServices.getEmvTagRecordMasterQS().queryByPrimaryKey(emvTag, sameValueFlag);
				if (emvTagMaster == null)
				{
					emvTagMaster = (EmvTagRecordMaster) ProjServices.getEmvTagRecordMasterQS().queryByPrimaryKey(emvTag, "Y");
				}
				l.add(emvTagMaster);
			}
			resultTempList = l;
		}
		return resultTempList;
	}

	/**
	 * @return the masterData
	 */
	public EmvTagRecordMaster getMasterData()
	{
		if (masterData == null && this.getEntity() != null)
		{
			List l = new ArrayList();
			EmvTagRecordMasterTemp emvTagRecordTemp = (EmvTagRecordMasterTemp) this.getEntity();
			String emvTag = emvTagRecordTemp.getEmvTag();
			String sameValueFlag = emvTagRecordTemp.getOriSameValueFlag();
			EmvTagRecordMaster emvTagMaster = (EmvTagRecordMaster) ProjServices.getEmvTagRecordMasterQS().queryByPrimaryKey(emvTag, sameValueFlag);
			if (emvTagMaster == null)
			{
				emvTagMaster = (EmvTagRecordMaster) ProjServices.getEmvTagRecordMasterQS().queryByPrimaryKey(emvTag, "Y");
			}
			masterData = emvTagMaster;
		}
		return masterData;
	}

	/**
	 * @return the modDetailList
	 */
	public List getModDetailList()
	{
		if (modDetailList == null && this.getEntity() != null)
		{
			List l = new ArrayList();
			EmvTagRecordMasterTemp emvTagRecordTemp = (EmvTagRecordMasterTemp) this.getEntity();
			String emvTag = emvTagRecordTemp.getEmvTag();
			String sameValueFlag = emvTagRecordTemp.getSameValueFlag();
			Integer tempBytePos = 0;
			Integer tempBitPos = 0;
			List emvTagRecordDetailTemp = ProjServices.getEmvTagRecordDetailTempQS().queryByEmvTagAndSameValueFlag(emvTag, sameValueFlag);
			for (Iterator<EmvTagRecordDetailTemp> itr = emvTagRecordDetailTemp.iterator(); itr.hasNext(); )
			{
				EmvTagRecordDetailTemp detail = itr.next();
				if (tempBytePos != detail.getPosByte() || tempBitPos != detail.getPosBit())
				{
					tempBytePos = detail.getPosByte();
					tempBitPos = detail.getPosBit();
					l.add(detail);
				}
			}
			modDetailList = l;
		}
		return modDetailList;
	}

	/**
	 * @return the oriDetailList
	 */
	public List<?> getOriDetailList()
	{
		if (oriDetailList == null && this.getEntity() != null)
		{
			List l = new ArrayList();
			EmvTagRecordMasterTemp emvTagRecordTemp = (EmvTagRecordMasterTemp) this.getEntity();
			String emvTag = emvTagRecordTemp.getEmvTag();
			String sameValueFlag = emvTagRecordTemp.getOriSameValueFlag();
			Integer tempBytePos = 0;
			Integer tempBitPos = 0;
			List emvTagRecordDetail = ProjServices.getEmvTagRecordDetailQS().queryByEmvTagAndSameValueFlag(emvTag, sameValueFlag);
			if (emvTagRecordDetail == null || emvTagRecordDetail.size() == 0)
			{
				emvTagRecordDetail = ProjServices.getEmvTagRecordDetailQS().queryByEmvTagAndSameValueFlag(emvTag, "Y");
			}
			if (emvTagRecordDetail == null || emvTagRecordDetail.size() == 0)
			{
				String[] flagArg = sameValueFlag.split(",");
				for(int f = 0; f < flagArg.length; f++ ){
					emvTagRecordDetail = ProjServices.getEmvTagRecordDetailQS().queryByEmvTagAndSameValueFlag(emvTag, flagArg[f]);
					if(emvTagRecordDetail != null){
						break;
					}
				}
			}
			for (Iterator<EmvTagRecordDetail> itr = emvTagRecordDetail.iterator(); itr.hasNext(); )
			{
				EmvTagRecordDetail detail = itr.next();
				if (tempBytePos != detail.getPosByte() || tempBitPos != detail.getPosBit())
				{
					tempBytePos = detail.getPosByte();
					tempBitPos = detail.getPosBit();
					l.add(detail);
				}
			}
			oriDetailList = l;
		}
		return oriDetailList;
	}
	
	@Override
	public String queryList()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		EmvTagRecordMasterTempArg arg = this.getEntity();
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try
		{
			EmvTagRecordMasterTempProcImpl proc = (EmvTagRecordMasterTempProcImpl) ProjServices.getEmvTagRecordMasterTempProc();

			arg.getPagingInfo().setEnablePaging(true);
			List<?> resultList = proc.getList(arg);
			this.setResultList(resultList);
			master.setFunctionCount(this.getResultList().size());
			request.getSession().setAttribute(RESULT_LIST_SESSION_NAME, resultList);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}
	
}
