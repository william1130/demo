/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/22, 下午 02:29:05, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjEntityProc.java,v 1.2 2014/09/04 03:19:52 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.proc;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import proj.nccc.logsearch.persist.AlterInfo;
import proj.nccc.logsearch.persist.ProjEntity;
import proj.nccc.logsearch.qs.ProjEntityQS;
import proj.nccc.logsearch.vo.ProjEntityArg;

import com.edstw.crud.AlterUserInfo;
import com.edstw.crud.CRUDPersistable;
import com.edstw.crud.proc.AbstractBaseCRUDProc;
import com.edstw.crud.sql.BaseCRUDQS;
import com.edstw.process.ProcessException;
import com.edstw.service.ServiceException;
import com.edstw.service.ServiceManager;

/**
 * 套用CRUD pattern的Proc類別
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
public abstract class ProjEntityProc<E extends ProjEntity, A extends ProjEntityArg, Q extends ProjEntityQS<E, A>>
		extends AbstractBaseCRUDProc {

	@SuppressWarnings("unchecked")
	@Override
	public BaseCRUDQS getBaseCRUDQS() {
		Class<Q> cls = (Class<Q>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[2];
		return (Q) ServiceManager.getInstance().getService(cls.getName());
	}

	@Override
	public CRUDPersistable createEmptyCRUDPersistable() {
		@SuppressWarnings("unchecked")
		Class<E> cls = (Class<E>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		try {
			return cls.newInstance();
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}
	}

	@Override
	protected AlterUserInfo createAlterUserInfo() {
		return AlterInfo.createAlterInfo();
	}

	public String getServiceName() {
		return this.getClass().getName();
	}

	public void setServiceParams(@SuppressWarnings("rawtypes") Map map) throws ServiceException {
		// do nothing
	}
}
