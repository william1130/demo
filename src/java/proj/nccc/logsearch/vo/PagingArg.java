/*
 * PagingArg.java
 *
 * Created on 2007年9月13日, 下午 6:03
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.vo;

import com.edstw.sql.DisplayTagPagingInfo;

import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

/**
 *
 * @author 許欽程(Vincent Shiu)
 * @version $Revision$
 */
public interface PagingArg
{

	DisplayTagPagingInfos getPagingInfo();
}
