/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/21, 下午 06:07:38, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjEntity.java,v 1.2 2014/09/04 03:19:54 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import com.edstw.crud.AlterUserInfo;
import com.edstw.crud.CRUDPersistable;

/**
 * 使用CRUD pattern的persistable類別. 目前預設ID之型別為Long, 若專案中使用不同型別, 可於本類別中修改即可,
 * 但所有繼承本類別之物件的ID均必須為相同之型別.
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
public abstract class ProjEntity extends ProjPersistable implements
		CRUDPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7578823838467090799L;
	private Long entityId;
	private AlterUserInfo createInfo;
	private AlterUserInfo updateInfo;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.getEntityId();
	}

	/**
	 * 設定ID.
	 * 
	 * @param id
	 */
	public void setId(Object id) {
		if (id != null)
			this.setEntityId(Long.parseLong(id.toString()));
	}

	/**
	 * 設定ID.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.setEntityId(id);
	}

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the createInfo
	 */
	public AlterUserInfo getCreateInfo() {
		return createInfo;
	}

	/**
	 * @param createInfo
	 *            the createInfo to set
	 */
	public void setCreateInfo(AlterUserInfo createInfo) {
		this.createInfo = createInfo;
	}

	/**
	 * @return the updateInfo
	 */
	public AlterUserInfo getUpdateInfo() {
		return updateInfo;
	}

	/**
	 * @param updateInfo
	 *            the updateInfo to set
	 */
	public void setUpdateInfo(AlterUserInfo updateInfo) {
		this.updateInfo = updateInfo;
	}
}
