package proj.nccc.logsearch.proc;

import java.util.List;

import proj.nccc.logsearch.persist.EmvProjPersistable;

import com.edstw.process.ProcessException;

/**
 *
 * @author 
 * @version
 */
public interface EmvTagRecordDetailProc extends BaseCRUDProc
{

	public EmvProjPersistable create(List<?> arg) throws ProcessException; 
}
