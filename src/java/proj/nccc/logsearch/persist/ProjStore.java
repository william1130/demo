package proj.nccc.logsearch.persist;

import com.dxc.nccc.aplog.edstw.persist.AbstractPersistable4ApLogStore;
import com.edstw.util.NotImplementedException;
import java.util.List;

public abstract class ProjStore extends AbstractPersistable4ApLogStore
{
	
	/** Creates a new instance of EmsStore */
	public ProjStore()
	{
	}
	
	/**
	 * 若要提供FieldInfo, 可覆寫本函式, 將store要使用之欄位逐一建立FieldInfo物件, 依照順序置於List後傳回.
	 * 若實作了本函式, 則必需再實作getTableName(), 傳回store對應的table, 
	 * 此外, 無需再覆寫getPreparedInsertCmd(), getPreparedUpdateCmd()及getPreparedDeleteCmd().
	 */
	protected List getFieldInfos()
	{
		throw new NotImplementedException();
	}
	
	/**
	 * 傳回store對應的table name. 若覆寫了本函式, 應再覆寫getFieldInfos(), 提供所有欄位的資訊.
	 */
	protected String getTableName()
	{
		throw new NotImplementedException();
	}
	
	/**
	 * 預設實作為丟出NotImplementedException, 若有需要, 可於子類別中覆寫.
	 */
	public String getUpdateCmd(com.edstw.persist.Persistable persistable)
	{
		throw new NotImplementedException();
	}
	
	/**
	 * 預設實作為丟出NotImplementedException, 若有需要, 可於子類別中覆寫.
	 */
	public String getInsertCmd(com.edstw.persist.Persistable persistable)
	{
		throw new NotImplementedException();
	}
	
	/**
	 * 預設實作為丟出NotImplementedException, 若有需要, 可於子類別中覆寫.
	 */
	public String getDeleteCmd(com.edstw.persist.Persistable persistable)
	{
		throw new NotImplementedException();
	}
	
}
