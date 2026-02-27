/*
 * ProjPersistableArg.java
 *
 * Created on 2007年8月20日, 下午 1:58
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.vo;

import com.edstw.persist.NonPersistable;
import proj.nccc.logsearch.persist.EmvProjPersistable;

/**
 * Persistable物件的Arg類別, 用來接受使用者由畫面上輸入之參數.
 * @author 
 * @version 
 */
public interface ProjPersistableArg extends NonPersistable, EmvProjPersistable
{

	void buildFromProjPersistable(EmvProjPersistable obj) throws Exception;

	/**
	 * UI 動作名稱，回傳值非Null或空值時，記錄操作歷程
	 * @return
	 */
	String getUiLogAction();

	String getUiLogFunctionName();

	String getUiLogOther();
}
