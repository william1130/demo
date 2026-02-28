package proj.nccc.atsLog.batch.util;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import proj.nccc.atsLog.batch.dao.entity.HouseKeeping;

public class ExpireDateFileNameFilter implements FilenameFilter {
	private HouseKeeping config;

	public HouseKeeping getConfig() {
		return config;
	}

	public void setConfig(HouseKeeping config) {
		this.config = config;
	}

	@Override
	public boolean accept(File dir, String name) {
		SimpleDateFormat sdf = new SimpleDateFormat(config.getDatePattern());
		if (config.getName().contains("${datePattern}")) {
			String[] ss = config.getName().split("\\$\\{datePattern\\}");
			String dateString = name;
			if (ss[0] != null && !"".equals(ss[0])) {
				String[] subStrings = name.split(ss[0]);
				if (subStrings.length < 2) {
					return false;
				}
				dateString = subStrings[1];
			}
			if (ss.length > 1 && ss[1] != null && !"".equals(ss[1])
					&& Pattern.matches("^.*" + ss[1] + "$", dateString)) {
				dateString = dateString.split(ss[1])[0];
			}
			try {
				Date fileDate = sdf.parse(dateString);
				return fileDate.before(config.getExpireDate());
			} catch (ParseException e) {
				return false;
			}
		} else if (name.startsWith(config.getName())) {
			String[] subNames = name.split(config.getName());
			if (subNames.length < 2) {
				return false;
			}
			String dateString = subNames[1];
			try {
				Date fileDate = sdf.parse(dateString);
				return fileDate.before(config.getExpireDate());
			} catch (ParseException e) {
				return false;
			}
		}
		return false;
	}

}
