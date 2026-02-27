/*
 * EmvProjPersistable.java
 *
 * Created on 2013年9月24日, 下午 2:25
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.persist;

import com.edstw.persist.jdbc.JdbcStatefulPersistable;

/**
 *
 * @author Stephen Lin
 * @version $Revision$
 */
public interface EmvProjPersistable extends JdbcStatefulPersistable
{
	/**
	 * 實作本介面, 傳回代表該persistable物件的key值.
	 */
	Object getId();

	AlterInfo getCreateInfo();

	void setCreateInfo(AlterInfo createInfo);

	AlterInfo getUpdateInfo();

	void setUpdateInfo(AlterInfo updateInfo);
	
	AlterInfo getRequestInfo();

	void setRequestInfo(AlterInfo requestInfo);
	
}
