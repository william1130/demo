package proj.nccc.logsearch.proc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.edstw.process.ProcessException;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.qs.MFESCheckoutLogGetTerminalQSImpl;
import proj.nccc.logsearch.qs.MFESCheckoutLogQSImpl;
import proj.nccc.logsearch.vo.MFESCheckoutLogArg;

public class MFESCheckoutLogProcImpl extends ProjProc{

	public int queryCount(MFESCheckoutLogArg arg) {
		MFESCheckoutLogQSImpl qs = (MFESCheckoutLogQSImpl) ProjServices.getMFESCheckoutLogQS();

		return qs.queryCount(arg);
	}
	

	/**
	 * @param arg
	 * @return
	 * @throws ProcessException
	 */
	public List<MFESCheckoutLogArg> getList(MFESCheckoutLogArg arg) throws ProcessException {

		List<MFESCheckoutLogArg> list = null;
		List<MFESCheckoutLogArg> terminalList = new ArrayList<MFESCheckoutLogArg>();
		try {

			MFESCheckoutLogQSImpl qs = (MFESCheckoutLogQSImpl) ProjServices.getMFESCheckoutLogQS();
			list = qs.query(arg);
			
			if (list.size() != 0) {
				// -- 查詢各特店代號下所有端末機代號
				MFESCheckoutLogGetTerminalQSImpl terminalQs = (MFESCheckoutLogGetTerminalQSImpl) ProjServices.getMFESCheckoutLogGetTerminalQS();
				for(Iterator<MFESCheckoutLogArg> itr = list.iterator(); itr.hasNext(); ){					
					MFESCheckoutLogArg vo = (MFESCheckoutLogArg) itr.next();
					List<MFESCheckoutLogArg> tempList = terminalQs.queryByMerchId(vo.getMerchantId());
					terminalList.addAll(tempList);
				}
			}
			else {
				return list;
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}

		return terminalList;
	}
	
}
