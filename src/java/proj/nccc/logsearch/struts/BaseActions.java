/*
 * BaseActions.java
 *
 * Created on 2007年8月20日, 下午 4:15
 * ==============================================================================================
 * $Id: BaseActions.java,v 1.1 2017/01/19 10:02:47 linsteph2\cvsuser Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.struts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dxc.nccc.aplog.edstw.struts2.BaseAction4ApLog;
import com.edstw.sql.DisplayTagPagingInfo;
import com.edstw.web.util.ExtraMessage;
import proj.nccc.logsearch.vo.PagingArg;

/**
 *
 * @author 許欽程(Vincent Shiu)
 * @version $Revision: 1.1 $
 */
public class BaseActions extends BaseAction4ApLog {
	private static final long serialVersionUID = 1L;

	private List<?> resultList;

	/** Creates a new instance of BaseActions */

	public BaseActions() {
	}

	protected void saveNoMatchDataMessage(HttpServletRequest request) {
		ExtraMessage msg = new ExtraMessage();
		msg.setMessageType(ExtraMessage.TYPE_WARNING);
		msg.setMessage("查無符合條件之資料");
		saveExtraMessage(msg);
	}

	public void saveExtraMessage(HttpServletRequest request, ExtraMessage msg) {
		super.setRequestAttribute("com.edstw.web.extraMessage", msg);
	}

	public void processPagingInfo(PagingArg arg) {
		// 若是具有分頁功能的arg時, 要處理分頁的參數.
		DisplayTagPagingInfo pi = (DisplayTagPagingInfo) arg.getPagingInfo();
		if (pi.isEnablePaging()) {
			String page = getParameterValue("page");
			if (page != null)
				pi.setCurrentPage(Integer.parseInt(page));
			String sortTarget = getParameterValue("sort");
			if (sortTarget != null)
				pi.setSortTarget(sortTarget);
			String sortType = getParameterValue("dir");
			if (sortType != null)
				pi.setSortType(sortType);
		}
	}

	/**
	 * @return the resultList
	 */
	public List<?> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList the resultList to set
	 */
	public void setResultList(List<?> resultList) {
		this.resultList = resultList;
	}
}
