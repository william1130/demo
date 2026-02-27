package proj.nccc.logsearch.struts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvRefSpec;
import proj.nccc.logsearch.persist.EmvRefSpecTemp;
import proj.nccc.logsearch.proc.BaseCRUDProc;
import proj.nccc.logsearch.vo.EmvRefSpecTempArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

public class EmvRefSpecTempActions extends BaseCRUDActions
{
	private static final long serialVersionUID = 1L;
	private List resultTempList = null;
	private EmvRefSpecTempArg entity;
	protected BaseCRUDProc getBaseCRUDProc()
	{
		return ProjServices.getEmvRefSpecTempProc();
	}
	protected ProjPersistableArg createEmptyProjPersistableArg()
	{
		return new EmvRefSpecTempArg();
	}
	
	public EmvRefSpecTempArg getEntity() {
		return entity;
	}

	public void setEntity(EmvRefSpecTempArg entity) {
		this.entity = entity;
	}

	public EmvRefSpecTempActions() {
		this.setEntity(new EmvRefSpecTempArg());		
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
				EmvRefSpecTemp emvRefSpecTemp = (EmvRefSpecTemp) itr.next();
				String specID = emvRefSpecTemp.getSpecID();
				EmvRefSpec emvRefSpec = (EmvRefSpec) ProjServices.getEmvRefSpecQS().queryById(specID);
				l.add(emvRefSpec);
			}
			resultTempList = l;
		}
		return resultTempList;
	}
}
