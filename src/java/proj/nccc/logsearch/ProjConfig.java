package proj.nccc.logsearch;

import java.io.Serializable;

import org.apache.commons.beanutils.ConvertUtils;
import org.jdom2.Element;

import com.edstw.bean.EdsBeanUtil;
import com.edstw.bean.converters.IdConverter;
import com.edstw.config.Config;
import com.edstw.config.CustomizeConfig;
import com.edstw.web.WebConfig;

public class ProjConfig implements CustomizeConfig {
	private static ProjConfig instance;

	private WebConfig config;

	public void initCustomizeSetting(Element root) throws Exception {
		EdsBeanUtil.registerNull();
		ConvertUtils.register(new IdConverter(Long.class, null), Serializable.class);
	}

	public static ProjConfig getInstance() {
		if (instance == null)
			instance = new ProjConfig();
		return instance;
	}

	public void setConfig(Config config) {
		this.config = (WebConfig) config;
	}

	public Config getConfig() {
		return this.config;
	}
}
