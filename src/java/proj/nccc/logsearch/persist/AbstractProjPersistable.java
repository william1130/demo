package proj.nccc.logsearch.persist;

import com.dxc.nccc.aplog.edstw.persist.AbstractPersistable4ApLog;

/**
 *
 * @author 許欽程(Vincent Shiu)
 * @version $Revision$
 */
public abstract class AbstractProjPersistable extends AbstractPersistable4ApLog implements EmvProjPersistable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AlterInfo createInfo;
	private AlterInfo updateInfo;
	private AlterInfo requestInfo;
	
	/**
	 * Creates a new instance of AbstractEmsPersistable
	 */
	public AbstractProjPersistable()
	{
	}

	public AlterInfo getCreateInfo()
	{
		return createInfo;
	}

	public void setCreateInfo(AlterInfo createInfo)
	{
		this.createInfo = createInfo;
	}

	public AlterInfo getUpdateInfo()
	{
		return updateInfo;
	}

	public void setUpdateInfo(AlterInfo updateInfo)
	{
		this.updateInfo = updateInfo;
	}
	
	public AlterInfo getRequestInfo()
	{
		return requestInfo;
	}

	public void setRequestInfo(AlterInfo requestInfo)
	{
		this.requestInfo = requestInfo;
	}
	
}
