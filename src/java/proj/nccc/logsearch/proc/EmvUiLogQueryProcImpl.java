package proj.nccc.logsearch.proc;

import java.util.List;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.qs.EmvUiLogQueryQSImpl;
import proj.nccc.logsearch.vo.EmvUiLogArg;

import com.edstw.process.ProcessException;

/**
 * 
 * @author Red Lee
 * @version 1.0
 */
public class EmvUiLogQueryProcImpl extends ProjProc {

	public int queryCount(EmvUiLogArg arg) {
		EmvUiLogQueryQSImpl qs = (EmvUiLogQueryQSImpl) ProjServices.getEmvUiLogQueryQS();

		return qs.queryCount(arg);
	}

	/**
	 * @param arg
	 * @return
	 * @throws ProcessException
	 */
	public List<EmvUiLogArg> getList(EmvUiLogArg arg) throws ProcessException {

		List<EmvUiLogArg> list = null;

		try {

			EmvUiLogQueryQSImpl qs = (EmvUiLogQueryQSImpl) ProjServices.getEmvUiLogQueryQS();

			list = qs.query(arg);

		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}

		return list;
	}


}
