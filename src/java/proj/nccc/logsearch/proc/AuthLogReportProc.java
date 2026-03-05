/*
 * ArtDepositB90ReportProc.java
 *
 * Created on 2008年3月3日, 上午 9:34
 * ==============================================================================================
 * $Id: AuthLogReportProc.java,v 1.1 2017/04/24 01:31:26 asiapacific\jih Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.proc;

import com.edstw.process.Process;
import com.edstw.process.ProcessException;
import com.edstw.report.Report;

import proj.nccc.logsearch.vo.AuthLogDataArg;

/**
 *
 * @author lzltp3
 * @version $Revision: 1.1 $
 */
public interface AuthLogReportProc extends Process {
	Report authLogAuthTransReport(AuthLogDataArg arg) throws ProcessException;
}
