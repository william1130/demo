/*
 * BaseCRUDProc.java
 *
 * Created on 2007年8月20日, 下午 1:56
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.proc;

import com.edstw.process.Process;
import com.edstw.process.ProcessException;
import java.util.List;

import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 * 系統中所有具備CRUD作業的process介面.
 * @author 
 * @version 
 */
public interface BaseCRUDProc extends Process
{

	/**
	 * 進入維護persistable的前置作業.
	 */
	void toMaintain( ProjPersistableArg arg ) throws ProcessException;

	/**
	 * 新增persistable的前置作業.
	 */
	void toCreate( ProjPersistableArg arg ) throws ProcessException;
	
	/**
	 * 使用傳入的persistable arg之資料建立對應之persistable物件後新增至資料庫.
	 * 新增完畢後, 將新增之物件傳回.
	 */
	EmvProjPersistable create( ProjPersistableArg arg ) throws ProcessException;

	/**
	 * 修改persistable的前置作業.
	 */
	void toModify( ProjPersistableArg arg ) throws ProcessException;
	
	/**
	 * 使用傳入的persistable arg之id由資料庫讀取原資料後, 再依arg之內容更新persistable物件.
	 * 修改完畢後, 將被修改之物件傳回.
	 */
	EmvProjPersistable modify( ProjPersistableArg arg ) throws ProcessException;
	
	/**
	 * 使用傳入的persistable arg之id由資料庫讀取原資料後, 將其刪除.
	 */
	void delete( Object id ) throws ProcessException;
	
	/**
	 * 將傳入的所有persistable id對應之資料由資料庫中刪除, 實作時應由資料庫讀取原資料, 然後再刪除.
	 */
	void delete( List ids ) throws ProcessException;
	
	/**
	 * 依傳入的persistable物件的內容執行查詢.
	 */
	List query( ProjPersistableArg arg ) throws ProcessException;
	
	/**
	 * 依傳入的ProjPersistableArg查詢persistable物件.
	 */
	EmvProjPersistable queryById( Object id ) throws ProcessException;
	
	/**
	 * 覆核作業 核准
	 */
	void approve(ProjPersistableArg arg ) throws ProcessException;
	
	/**
	 * 覆核作業 拒絕
	 */
	void reject(ProjPersistableArg arg ) throws ProcessException;
}
