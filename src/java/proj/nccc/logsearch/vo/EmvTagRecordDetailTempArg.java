package proj.nccc.logsearch.vo;

import org.apache.commons.beanutils.BeanUtils;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordDetailTemp;

public class EmvTagRecordDetailTempArg extends EmvTagRecordDetailTemp
{
	private static final long serialVersionUID = 1L;

	public void buildFromProjPersistable(EmvProjPersistable c) throws Exception
	{
		BeanUtils.copyProperties(this, c);
	}
}
