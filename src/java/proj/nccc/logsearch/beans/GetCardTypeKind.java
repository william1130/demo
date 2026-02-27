/*
 * GetCardTypeKind.java
 * 
 * Created on 2008年5月9日, 下午 3:07
 * ==============================================================================================
 * $Id: GetCardTypeKind.java,v 1.1 2017/04/24 01:31:16 asiapacific\jih Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.beans;

import java.util.Date;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.BinBinoParm;

import com.edstw.process.ProcessException;
import com.edstw.user.UserLogger;

/**
 * 
 * @author Stephen Lin
 * @versionType $Revision: 1.1 $
 */
public class GetCardTypeKind {

	private String cardType;
	private BinBinoParm binParm;
	// private String cardKind;
	{
	}

	public boolean query(String bin, Date today) {
		try {
			if (bin == null)
				return false;
			else {
				if (today == null)
					today = new Date();
				BinBinoParm binParm = ProjServices.getBinBinoParmQS().getBinParm(bin, today);
				if (binParm == null)
					return false;
				else {
					// this.setCardKind( binParm.getCardKind() );
					this.setCardType(binParm.getCardType());
					this.setBinParm(binParm);
				}
				return true;
			}
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}

	}

	/*
	 * public String getCardKind() { return cardKind; }
	 * 
	 * public void setCardKind(String cardKind) { this.cardKind = cardKind; }
	 */

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public BinBinoParm getBinParm() {
		return binParm;
	}

	public void setBinParm(BinBinoParm binParm) {
		this.binParm = binParm;
	}

}
