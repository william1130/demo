package proj.nccc.logsearch.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.parse.chip.ChipParse;
import proj.nccc.logsearch.parse.chip.ChipParseMulti;
import proj.nccc.logsearch.persist.ChipcardInfo;
import proj.nccc.logsearch.qs.ChipcardInfoQSImpl;

import com.edstw.bean.BeanException;
import com.edstw.bean.CacheableBean;

/**
 * Parse Chip Data 用
 * @author Red Lee
 * @version 1.0
 */
public class ChipUtil implements CacheableBean {
	
	private static final ChipUtil instance = new ChipUtil();

	//	Chip Parse Mapping
	private Map<String, ChipParse> chipMap = null;
	
	//	Chip Value 對映值
	private Map<String, Map<String, String>> codeMaps = null;
	
	/**
	 * @return
	 */
	public static ChipUtil getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.edstw.bean.BaseBean#init()
	 */
	public void init() throws BeanException {

		refresh();
	}

	/* (non-Javadoc)
	 * @see com.edstw.bean.CacheableBean#refresh()
	 */
	public void refresh() throws BeanException {

		ChipcardInfoQSImpl qs = (ChipcardInfoQSImpl)
				ProjServices.getChipcardInfoQS();
		
		chipMap = new HashMap<String, ChipParse>();
		codeMaps = new HashMap<String, Map<String, String>>();
		
		try {
		
			List<ChipcardInfo> list = qs.queryAll();
			
			for (ChipcardInfo obj : list) {
				
				String dataType = obj.getDataType();
				String id = obj.getId();
				
				//	C為 Value 對映值
				if ("C".equals(dataType)) {
					
					String tagName = obj.getTagName();
					
					Map<String, String> codeMap = codeMaps.get(tagName);
					
					if (codeMap == null) {
						
						codeMap = new HashMap<String, String>();
						codeMaps.put(tagName, codeMap);
					}
					
					codeMap.put(obj.getChipType(), obj.getMeaning());
				}
				//	其它為需 Parse 的值
				else {

					ChipParse cp = (ChipParse) chipMap.get(id);
					
					if (cp == null) {
						
						cp = (ChipParse) ("9F10".equals(obj.getTagName()) ?
								new ChipParseMulti() : new ChipParse());
						chipMap.put(id, cp);
					}
					
					cp.addChipcardInfo(obj);
				}
			}
			
			//	初始化, 進行排序
			for (ChipParse cp : chipMap.values()) {
				cp.init();
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
			throw new BeanException(e);
		}
	}
	
	/**
	 * 由組合的 key 取得Parse Chip Object
	 * @param key
	 * @return
	 */
	public ChipParse getChipParse(String key) {
		
		return (ChipParse) chipMap.get(key);
	}
	
	
	/**
	 * 由組合的 key 及 value 取得值的文字說明
	 * @param key
	 * @param code
	 * @return
	 */
	public String getMeaning(String key, String code) {
		
		Map<String, String> codeMap = codeMaps.get(key);
		
		if (codeMap == null)
			return "";
		
		String value = codeMap.get(code);
		return value == null ? "" : value;
	}
}
