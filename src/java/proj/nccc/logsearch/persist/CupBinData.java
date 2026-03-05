/*
 * CupBinData.java
 *
 * Created on 2008/11/6 上午 10:34:41 by 林芷萍(Maxi Lin, lzp704)
 * ==============================================================================================
 * $Id: CupBinData.java,v 1.3 2019/11/25 05:48:19 redlee Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.persist;

import com.edstw.lang.DateString;
import com.edstw.nccc.sql.log.ValueImage;

/**
 * 
 * @author 林芷萍(Maxi Lin, lzp704)
 * @version $Revision 1.1 $
 */
public class CupBinData extends AbstractProjPersistable {

	private static final long serialVersionUID = 7282889597948719686L;

	private String cutBin;

	private DateString fileDate;

	private String modProgramId;

	public String getId() {

		return cutBin;
	}

	public String getCutBin() {

		return cutBin;
	}

	public void setCutBin(String cutBin) {

		this.cutBin = cutBin;
	}

	public DateString getFileDate() {

		return fileDate;
	}

	public void setFileDate(DateString fileDate) {

		this.fileDate = fileDate;
	}

	public String getModProgramId() {

		return modProgramId;
	}

	public void setModProgramId(String modProgramId) {

		this.modProgramId = modProgramId;
	}

	@Override
	public ValueImage createValueImage() {
		// TODO Auto-generated method stub
		ValueImage vi = new ValueImage();
		vi.addValue(cutBin);
		vi.addValue(fileDate);
		vi.addValue(modProgramId);
		return vi;
	}

}
