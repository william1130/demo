
package proj.nccc.logsearch.beans;

import com.edstw.bean.BeanException;
import com.edstw.bean.CacheableBean;


/**
 * M2017092 : Build merchant Ear 需用到
 *
 */
public class ParamBeanEstm implements CacheableBean {
	
	private static final ParamBeanEstm instance = new ParamBeanEstm();
	public static ParamBeanEstm getInstance() {
		return instance;
	}

	@Override
	public void init() throws BeanException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() throws BeanException {
		// TODO Auto-generated method stub
		
	}
	

	
	
}
