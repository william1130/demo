package proj.nccc.logsearch.user;

import java.io.Serializable;

import com.edstw.nccc.user.NcccUserProfile;

/**
 *
 * @author(Vincent Shiu)
 * @version $Revision$
 */
public class ProjUserProfile extends NcccUserProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of EmsUserProfile */
	public ProjUserProfile() {
	}

	public boolean isNcccUser() {
		return (getUserInfo() instanceof ProjUserInfo);
	}
}
