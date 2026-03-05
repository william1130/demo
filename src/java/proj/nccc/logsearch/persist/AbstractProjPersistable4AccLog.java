package proj.nccc.logsearch.persist;

import com.dxc.nccc.aplog.edstw.persist.AbstractPersistable4ApLog;

/**
 *
 * @author 許欽程(Shiu Vincent, sz12tk)
 * @version $Revision$
 */
public abstract class AbstractProjPersistable4AccLog extends AbstractPersistable4ApLog implements EmvProjPersistable
{
	private static final long serialVersionUID = 530592375741239329L;
	private AlterInfo createInfo;
	private AlterInfo updateInfo;

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
}
