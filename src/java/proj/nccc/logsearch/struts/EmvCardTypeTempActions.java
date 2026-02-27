package proj.nccc.logsearch.struts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.edstw.web.WebConstant;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvCardType;
import proj.nccc.logsearch.persist.EmvCardTypeTemp;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.vo.EmvCardTypeTempArg;

public class EmvCardTypeTempActions extends BaseCRUDActions
{

	private static final long serialVersionUID = 1L;
	private EmvCardTypeTempArg entity;
	private Map<String, Integer> approveCount = null;
	private List<?> resultTempList = null;
	protected BaseCRUDProc getBaseCRUDProc()
	{
		return ProjServices.getEmvCardTypeTempProc();
	}


	public EmvCardTypeTempArg getEntity() {
		return entity;
	}

	public void setEntity(EmvCardTypeTempArg entity) {
		this.entity = entity;
	}

	public EmvCardTypeTempActions() {
		this.setEntity(new EmvCardTypeTempArg());		
	}
	
	public String queryApproveCount()
	{
		try
		{
			Map<String, Integer> approveCount = new HashMap<String, Integer>();
			approveCount.put("cardType", ProjServices.getEmvCardTypeTempQS().countToBeApprove());
			approveCount.put("emvTag", ProjServices.getEmvTagRecordMasterTempQS().countToBeApprove());
			approveCount.put("emvSpec", ProjServices.getEmvRefSpecTempQS().countToBeApprove());
			this.setApproveCount(approveCount);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			return WebConstant.FAIL_KEY;
		}
	}

	/**
	 * @return the resultTempList
	 */
	public List getResultTempList()
	{
		if (resultTempList == null && getResultList() != null)
		{
			List l = new ArrayList();
			for (Iterator itr = getResultList().iterator(); itr.hasNext(); )
			{
				EmvCardTypeTemp emvCardTypeTemp = (EmvCardTypeTemp) itr.next();
				String cardType = emvCardTypeTemp.getCardType();
				EmvCardType emvCardType = (EmvCardType) ProjServices.getEmvCardTypeQS().queryById(cardType);
				l.add(emvCardType);
			}
			resultTempList = l;
		}
		return resultTempList;
	}

	/**
	 * @return the approveCount
	 */
	public Map<String, Integer> getApproveCount()
	{
		return approveCount;
	}

	/**
	 * @param approveCount the approveCount to set
	 */
	public void setApproveCount(Map<String, Integer> approveCount)
	{
		this.approveCount = approveCount;
	}
}
